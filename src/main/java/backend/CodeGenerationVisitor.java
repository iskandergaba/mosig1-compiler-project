package backend;

import java.util.*;

import common.asml.*;
import common.asml.Float;
import common.visitor.*;

/**
 * Visits the ASML AST and generates ARM assembly code.
 */
public class CodeGenerationVisitor implements ObjVisitor<InstructionBlock> {
    public String data;

    // Generates label for branching (if's, function calls, ...)
    private LabelGenerator labelGenerator;

    // The function name currently parsing
    private String currentFunction;

    // A mapping between function names and labels
    private Map<String, String> functionLabels;

    // The instruction factory, used to create and parse ARM instructions
    // with the correct indentation and form.
    private InstructionFactory factory;

    private Map<String, Integer> registers;
    private Map<String, Integer> locations;
    private Boolean[] registersInUse;
    private Boolean[] namedRegistersInUse;
    private Boolean[] pushedRegisters;

    private String getFullName(Id id) {
        return currentFunction + "." + id.id;
    }

    private InstructionBlock visitIdentOrImmediate(Exp e) {
        InstructionBlock block = e.accept(this);
        int reg;

        if (block.getUsedRegisters().size() == 0) { // Int
            InstructionBlock regBlock = getTemporaryRegister();
            reg = regBlock.getUsedRegisters().get(0);

            block.setReturn("r" + reg);
            return regBlock.chain(block).useRegister(reg);
        }
        // Imm
        return block;
    }

    private InstructionBlock getTemporaryRegister() {
        for (int i = 4; i <= 12; i++) {
            if (i == 11) continue;
            if (!registersInUse[i] && !namedRegistersInUse[i]) {
                registersInUse[i] = true;
                return new InstructionBlock().useRegister(i);
            }
        }

        // I think this part will produce bugs at runtime
        for (int i = 4; i <= 12; i++) {
            if (i == 11) continue;
            if (!pushedRegisters[i]) {
                pushedRegisters[i] = true;
                return new InstructionBlock()
                    .add(factory.instr("PUSH", "{r" + i + "}"))
                    .useRegister(i);
            }
        }

        // Don't know what to do at this stage: no more free registers
        // and all used registers are pushed
        return null;
    }

    private InstructionBlock getRegister(Id id) {
        if (registers.get(getFullName(id)) != null) {
            int reg = registers.get(getFullName(id));
            registersInUse[reg] = true;
            namedRegistersInUse[reg] = true;
            return new InstructionBlock()
                .useRegister(reg);
        } else {
            InstructionBlock regBlock = getTemporaryRegister();
            int reg = regBlock.getUsedRegisters().get(0);

            if (locations.get(getFullName(id)) == -1) {
                System.err.println("Warning: invalid offset for " + id);
            }

            return regBlock
                .add(factory.instr("LDR", "r" + reg, "[fp, #" + locations.get(getFullName(id)) + "]"))
                .useRegister(reg);
        }
    }

    private InstructionBlock freeRegister(int register) {
        if (pushedRegisters[register]) {
            pushedRegisters[register] = false;
            return new InstructionBlock(factory.instr("POP", "{r" + register + "}"));
        }
        registersInUse[register] = false;
        return new InstructionBlock();
    }

    private InstructionBlock spillVariable(Id id, int reg) {
        registersInUse[reg] = false;

        Integer offset = locations.get(getFullName(id));
        if (offset != null) {
            return new InstructionBlock(factory.instr("STR", "r" + reg, "[fp, #" + offset + "]"));
        }
        return new InstructionBlock();
    }

    private InstructionBlock singlePrecisionOpRoutine(Id id1, Id id2) {
        InstructionBlock moveLeftOperand = new InstructionBlock();
        if (registers.get(getFullName(id1)) != null) {
            moveLeftOperand.add(factory.instr("VMOV.32", "s14", "r" + registers.get(getFullName(id1))));
        } else {
            moveLeftOperand.add(factory.instr("VLDR", "s14", "[fp, #" + locations.get(getFullName(id1)) + "]"));
        }

        InstructionBlock moveRightOperand = new InstructionBlock();
        if (registers.get(getFullName(id2)) != null) {
            moveRightOperand.add(factory.instr("VMOV.32", "s15", "r" + registers.get(getFullName(id2))));
        } else {
            moveRightOperand.add(factory.instr("VLDR", "s15", "[fp, #" + locations.get(getFullName(id2)) + "]"));
        }

        return moveLeftOperand.chain(moveRightOperand);
    }

    private InstructionBlock prologue(int size) {
        return new InstructionBlock()
            .add(factory.instr("PUSH", "{r4-r11, lr}"))
            .add(factory.instr("ADD", "r11", "sp", "#0"))
            .add(factory.instr("SUB", "sp", "sp", "#" + (4 * size)))
            .commentFirst("Function " + currentFunction);
    }

    private InstructionBlock epilogue() {
        return new InstructionBlock()
            .add(factory.instr("SUB", "sp", "r11", "#0"))
            .add(factory.instr("POP", "{r4-r11, lr}"));
    }

    // Used for generating arithmetic operations like ADD, SUB, ...
    private InstructionBlock arithmeticOperation(String op, Id id, Exp e) {
        InstructionBlock leftOperandBlock = getRegister(id);
        int leftOperandRegister = leftOperandBlock.getUsedRegisters().get(0);

        InstructionBlock rightOperandBlock = visitIdentOrImmediate(e);
        int rightOperandRegister = rightOperandBlock.getUsedRegisters().get(0);

        InstructionBlock operation = new InstructionBlock()
            .add(factory.instr(op, "$", "r" + leftOperandRegister, "r" + rightOperandRegister));

        InstructionBlock freeLeftOperand = freeRegister(leftOperandRegister);
        InstructionBlock freeRightOperand = freeRegister(rightOperandRegister);

        return leftOperandBlock
            .chain(rightOperandBlock)
            .chain(operation)
            .chain(freeRightOperand)
            .chain(freeLeftOperand);
    }

    // Used for generating arithmetic operations like FADD, FSUB, ...
    private InstructionBlock singlePrecisionArithmeticOperation(String op, Id id1, Id id2) {

        InstructionBlock moveOperands = singlePrecisionOpRoutine(id1, id2);
        InstructionBlock operation = new InstructionBlock().add(factory.instr(op, "s15", "s14", "s15"));
        InstructionBlock movResult = new InstructionBlock().add(factory.instr("VMOV.32", "$", "s15"));


        return moveOperands.chain(operation).chain(movResult);
    }

    // Used for generating condition checking, like =, >, ...
    private InstructionBlock conditionOperation(String condition, Id id, Exp e) {

        InstructionBlock leftOperandBlock = getRegister(id);
        int leftOperandRegister = leftOperandBlock.getUsedRegisters().get(0);

        InstructionBlock rightOperand = visitIdentOrImmediate(e);
        int rightOperandRegister = rightOperand.getUsedRegisters().get(0);

        InstructionBlock operation = new InstructionBlock(factory.instr("CMP", "r" + leftOperandRegister, "r" + rightOperandRegister))
            .add(factory.instr(condition, "$"));

        InstructionBlock freeLeftOperand = freeRegister(leftOperandRegister);
        InstructionBlock freeRightOperand = freeRegister(rightOperandRegister);

        return leftOperandBlock
            .chain(rightOperand)
            .chain(operation)
            .chain(freeRightOperand)
            .chain(freeLeftOperand);
    }

    // Used for generating condition checking, like =., >., ...
    private InstructionBlock singlePrecisionConditionOperation(String condition, Id id1, Id id2) {

        InstructionBlock moveOperands = singlePrecisionOpRoutine(id1, id2);
        InstructionBlock operation = new InstructionBlock()
            .add(factory.instr("VCMP.F32", "s14", "s15"))
            .add(factory.instr("VMRS", "APSR_nzcv", "FPSCR"))
            .add(factory.instr(condition, "$"));

        return moveOperands.chain(operation);
    }

    public CodeGenerationVisitor(Map<String, Integer> registers, Map<String, Integer> locations) {
        this.data = new String();
        this.labelGenerator = new LabelGenerator();
        this.currentFunction = "";
        this.functionLabels = new HashMap<String, String>();
        this.factory = new InstructionFactory();
        this.registers = registers;
        this.locations = locations;
        this.registersInUse = new Boolean[16];
        Arrays.fill(this.registersInUse, false);
        this.pushedRegisters = new Boolean[16];
        Arrays.fill(this.pushedRegisters, false);
        this.namedRegistersInUse = new Boolean[16];
        Arrays.fill(this.namedRegistersInUse, false);
    }

    @Override
    public InstructionBlock visit(Int e) {
        if (e.i > 255) {
            return new InstructionBlock(factory.instr("LDR", "$", "=#" + e.i));
        }
        return new InstructionBlock(factory.instr("MOV", "$", "#" + e.i));
    }

    @Override
    public InstructionBlock visit(Float e) {
        data += e.l.label + ": .single 0r" + e.f + "\n";
        return new InstructionBlock();
    }

    @Override
    public InstructionBlock visit(Neg e) {
        InstructionBlock registerBlock = getRegister(e.id);
        int reg = registerBlock.getUsedRegisters().get(0);

        InstructionBlock b = new InstructionBlock(factory.instr("RSB", "$","r" + reg, "#0"));

        InstructionBlock freeReg = freeRegister(reg);

        return registerBlock
            .chain(b)
            .chain(freeReg);
    }

    @Override
    public InstructionBlock visit(Add e) {
        return arithmeticOperation("ADD", e.id, e.e);
    }

    @Override
    public InstructionBlock visit(Sub e) {
        return arithmeticOperation("SUB", e.id, e.e);
    }

    @Override
    public InstructionBlock visit(FNeg e) {
        InstructionBlock moveOperand = new InstructionBlock();
        if (registers.get(getFullName(e.id)) != null) {
            moveOperand.add(factory.instr("VMOV.32", "s15", "r" + registers.get(getFullName(e.id))));
        } else {
            moveOperand.add(factory.instr("VLDR", "s15", "[fp, #" + locations.get(getFullName(e.id)) + "]"));
        }
        InstructionBlock operation = new InstructionBlock().add(factory.instr("VNEG.F32", "s15", "s15"));
        InstructionBlock movResult = new InstructionBlock().add(factory.instr("VMOV.32", "$", "s15"));

        return moveOperand.chain(operation).chain(movResult);
    }

    @Override
    public InstructionBlock visit(FAdd e) {
        return singlePrecisionArithmeticOperation("VADD.F32", e.id1, e.id2);
    }

    @Override
    public InstructionBlock visit(FSub e) {
        return singlePrecisionArithmeticOperation("VSUB.F32", e.id1, e.id2);
    }

    @Override
    public InstructionBlock visit(FMul e) {
        return singlePrecisionArithmeticOperation("VMUL.F32", e.id1, e.id2);
    }

    @Override
    public InstructionBlock visit(FDiv e) {
        return singlePrecisionArithmeticOperation("VDIV.F32", e.id1, e.id2);
    }

    @Override
    public InstructionBlock visit(Eq e) {
        return conditionOperation("BNE", e.id, e.e);
    }

    @Override
    public InstructionBlock visit(LE e) {
        return conditionOperation("BGT", e.id, e.e);
    }

    @Override
    public InstructionBlock visit(GE e) {
        return conditionOperation("BLT", e.id, e.e);
    }

    @Override
    public InstructionBlock visit(FEq e) {
        return singlePrecisionConditionOperation("BNE", e.id1, e.id2);
    }

    @Override
    public InstructionBlock visit(FLE e) {
        return singlePrecisionConditionOperation("BHI", e.id1, e.id2);
    }

    @Override
    public InstructionBlock visit(If e) {
        String labelElse = labelGenerator.getLabel();
        String labelEnd = labelGenerator.getLabel();

        InstructionBlock condition = e.cond.accept(this);
        condition.setReturn(labelElse);
        InstructionBlock thenBlock = e.e1.accept(this);

        if (thenBlock.varInRegister && !thenBlock.hasReturned)  {
            thenBlock.add(factory.instr("MOV", "$", "r" + thenBlock.getUsedRegisters().get(0)));
            thenBlock.hasReturned = true;
        }

        Instruction branchToEnd = factory.instr("B", labelEnd);

        factory.setLabel(labelElse);
        InstructionBlock elseBlock = e.e2.accept(this);
        if (elseBlock.storedLabel == null && elseBlock.instructionCount() == 0) {
            elseBlock.storedLabel = labelElse;
        }

        if (elseBlock.varInRegister && !elseBlock.hasReturned) {
            elseBlock.add(factory.instr("MOV", "$", "r" + elseBlock.getUsedRegisters().get(0)));
            elseBlock.hasReturned = true;
        }

        InstructionBlock result = condition
            .chain(thenBlock)
            .add(branchToEnd)
            .chain(elseBlock);

        if (elseBlock.varInRegister && elseBlock.instructionCount() == 0) {
            factory.setLabelForce(labelEnd);
        } else {
            String maybeNewerLabel = factory.setLabel(labelEnd);

            if (maybeNewerLabel != null) {
                result.replaceLabels(labelEnd, maybeNewerLabel);
            }
        }

        return result;
    }

    @Override
    public InstructionBlock visit(Let e) {
        // Allocation of the register for spilling the result of the
        // let expression (not the in expression)
        InstructionBlock letExpression = e.e1.accept(this);

        InstructionBlock registerBlock = getRegister(e.id);
        int reg = registerBlock.getUsedRegisters().get(0);


        if (letExpression.instructionCount() > 0 && !letExpression.setReturn("r" + reg)) {
            letExpression.add(factory.instr("MOV", "r" + reg, "r" + letExpression.getUsedRegisters().get(0)));
        }

        List<InstructionBlock> freedRegisters = new ArrayList<InstructionBlock>();

        // Aggregating the list of registers that were eventually
        // pushed on the stack and then freed.
        for (int register: letExpression.getUsedRegisters()) {
            freedRegisters.add(freeRegister(register));
        }

        // Spill result of let expression to its variable in the stack
        InstructionBlock saveVariable = spillVariable(e.id, reg);

        // Eventually popping the freed registers of the let expression
        for (InstructionBlock b: freedRegisters) {
            saveVariable.chain(b);
        }

        // Eventually popping the register used for spilling the result of
        // the let expression
        InstructionBlock freeReg = freeRegister(reg);

        // `in` part of the expression
        InstructionBlock inBlock = e.e2.accept(this);


        return registerBlock
            .chain(letExpression)
            .chain(saveVariable)
            .chain(freeReg)
            .chain(inBlock)
            .commentFirst("let " + e.id + " = ? in...");
    }

    @Override
    public InstructionBlock visit(Var e) {
        InstructionBlock registerBlock = getRegister(e.id);
        int reg = registerBlock.getUsedRegisters().get(0);

        //if (registerBlock.instructionCount() == 0) {
            registerBlock.varInRegister = true;
        //}

        return registerBlock
            .useRegister(reg);
    }

    @Override
    public InstructionBlock visit(LetRec e) {
        // We need to get the 'size' of the function (the number of local variables)
        // so that we can generate the prologue
        int size = e.accept(new SizeVisitor());

        this.currentFunction = e.fd.fun.l.label;
        Arrays.fill(this.registersInUse, false);
        Arrays.fill(this.pushedRegisters, false);
        Arrays.fill(this.namedRegistersInUse, false);

        String functionLabel = this.labelGenerator.getLabel();
        functionLabels.put(currentFunction, functionLabel);

        factory.setLabel(functionLabel);

        InstructionBlock prologue = prologue(size);
        InstructionBlock body = e.fd.e.accept(this);
        InstructionBlock epilogue = epilogue();

        if (body.varInRegister && body.storedLabel != null) {
            factory.setLabel(body.storedLabel);
            body.storedLabel = null;
            body.add(factory.instr("MOV", "r0", "r" + body.getUsedRegisters().get(0)));
        } else if (body.varInRegister) {
            body.add(factory.instr("MOV", "r0", "r" + body.getUsedRegisters().get(0)));
        }

        InstructionBlock result = prologue
            .chain(body);
        result.setReturn("r0");
        result = result
            .chain(epilogue)
            .add(factory.instr("BX", "lr")).comment("Return");

        return result.setFunctionLabel(functionLabel);
    }

    @Override
    public InstructionBlock visit(Call e) {
        InstructionBlock result = new InstructionBlock();

        InstructionBlock freeArgumentRegisters = new InstructionBlock();

        int counter = 4 - (e.args.size() % 4);

        for (int i = e.args.size() - 1; i >= 0; i--) {
            if (counter == 4) {
                counter = 0;
            }
            Id id = e.args.get(i);
            InstructionBlock paramBlock = getRegister(id);
            int paramReg = paramBlock.getUsedRegisters().get(0);
            result.chain(paramBlock)
                .add(factory.instr("MOV", "r" + (3 - counter), "r" + paramReg));

            result.add(factory.instr("PUSH", "{r" + (3 - counter) + "}"));
            freeArgumentRegisters.chain(freeRegister(paramReg));
            counter ++;
        }

        InstructionBlock popArguments = new InstructionBlock(factory.instr("ADD", "sp", "sp", "#" + (4 + (4 * e.args.size()))));

        String functionLabel = functionLabels.get(e.f.label);

        // For the moment this is how we handle external functions.
        if (functionLabel == null) {
            functionLabel = e.f.label;
        }

        return result
            .add(factory.instr("SUB", "sp", "sp", "#4")).comment("Placeholder for closure info")
            .add(factory.instr("BL", functionLabel)).comment("call " + e.f.label)
            .add(factory.instr("MOV", "$", "r0"))
            .chain(popArguments)
            .chain(freeArgumentRegisters);
    }

    @Override
    public InstructionBlock visit(New e) {
        InstructionBlock size = visitIdentOrImmediate(e.size);
        int sizeRegister = size.getUsedRegisters().get(0);

        InstructionBlock heapBaseAddrBlock = getTemporaryRegister();
        InstructionBlock heapOffsetAddrBlock = getTemporaryRegister();
        InstructionBlock heapBaseRegisterBlock = getTemporaryRegister();
        InstructionBlock heapOffsetRegisterBlock = getTemporaryRegister();

        int heapBaseAddr = heapBaseAddrBlock.getUsedRegisters().get(0);
        int heapOffsetAddr = heapOffsetAddrBlock.getUsedRegisters().get(0);
        int heapBaseRegister = heapBaseRegisterBlock.getUsedRegisters().get(0);
        int heapOffsetRegister = heapOffsetRegisterBlock.getUsedRegisters().get(0);

        /*
         *  The strategy here for creating a new array is the following:
         *  - Load the heap start address and the current heap offset
         *  - Add the current offset to the heap address to get the array address
         *  - Add the array size to the current offset
         *  - Store the new offset
         *  - Return the address + old
         */
        size
            .add(factory.instr("LDR", "r" + heapBaseAddr, "heap_start"))
            .add(factory.instr("LDR", "r" + heapOffsetAddr, "heap_offset"))
            .add(factory.instr("LDR", "r" + heapBaseRegister, "[r" + heapBaseAddr + "]"))
            .add(factory.instr("LDR", "r" + heapOffsetRegister, "[r" + heapOffsetAddr + "]"))
            .add(factory.instr("ADD", "r" + heapBaseRegister, "r" + heapBaseRegister, "r" + heapOffsetRegister))
            .add(factory.instr("ADD", "r" + heapOffsetRegister, "r" + heapOffsetRegister, "r" + sizeRegister))
            .add(factory.instr("STR", "r" + heapOffsetRegister, "[r" + heapOffsetAddr + "]"))
            .add(factory.instr("MOV", "$", "r" + heapBaseRegister));


        InstructionBlock freeSize = freeRegister(sizeRegister);
        InstructionBlock freeHeapBaseAddr = freeRegister(heapBaseAddr);
        InstructionBlock freeHeapOffsetAddr = freeRegister(heapOffsetAddr);
        InstructionBlock freeHeapBaseRegister = freeRegister(heapBaseRegister);
        InstructionBlock freeHeapOffsetRegister = freeRegister(heapOffsetRegister);

        return heapBaseAddrBlock
            .chain(heapOffsetAddrBlock)
            .chain(heapBaseRegisterBlock)
            .chain(heapOffsetRegisterBlock)
            .chain(size)
            .chain(freeSize)
            .chain(freeHeapOffsetRegister)
            .chain(freeHeapBaseRegister)
            .chain(freeHeapOffsetAddr)
            .chain(freeHeapBaseAddr);
    }

    @Override
    public InstructionBlock visit(Get e) {
        InstructionBlock base = e.base.accept(this);
        int baseRegister = base.getUsedRegisters().get(0);

        InstructionBlock offset = visitIdentOrImmediate(e.offset);
        int offsetRegister = offset.getUsedRegisters().get(0);
        offset.add(factory.instr("LSL", "r" + offsetRegister, "#2")); // Multiply offset by 4 to align on 4 byte values

        InstructionBlock get = new InstructionBlock()
            .add(factory.instr("ADD", "r"+baseRegister, "r"+baseRegister, "r"+offsetRegister))
            .add(factory.instr("LDR", "$", "[r"+baseRegister+"]"));

        InstructionBlock freeOffsetRegister = freeRegister(offsetRegister);
        InstructionBlock freeBaseRegister = freeRegister(baseRegister);

        return base
            .chain(offset)
            .chain(get)
            .chain(freeOffsetRegister)
            .chain(freeBaseRegister);
    }

    @Override
    public InstructionBlock visit(Put e) {
        InstructionBlock valueRegisterBlock = getRegister(e.dest);
        int valueRegister = valueRegisterBlock.getUsedRegisters().get(0);

        InstructionBlock offset = visitIdentOrImmediate(e.offset);
        int offsetRegister = offset.getUsedRegisters().get(0);
        offset.add(factory.instr("LSL", "r" + offsetRegister, "#2")); // Multiply offset by 4 to align on 4 byte values

        InstructionBlock base = e.base.accept(this);
        int baseRegister = base.getUsedRegisters().get(0);


        InstructionBlock oldValueRegisterBlock = getTemporaryRegister();
        int oldValueRegister = oldValueRegisterBlock.getUsedRegisters().get(0);

        base.chain(offset)
            .add(factory.instr("LDR", "r"+oldValueRegister, "[r"+baseRegister+"]"))
            .add(factory.instr("STR", "r"+valueRegister, "[r"+baseRegister+", r" + offsetRegister + "]"))
            .add(factory.instr("MOV", "$", "r"+oldValueRegister));

        InstructionBlock freeOffsetRegister = freeRegister(offsetRegister);
        InstructionBlock freeBaseRegister = freeRegister(baseRegister);
        InstructionBlock freeValueRegister = freeRegister(valueRegister);
        InstructionBlock freeOldValueRegister = freeRegister(oldValueRegister);

        return valueRegisterBlock
            .chain(oldValueRegisterBlock)
            .chain(base)
            .chain(freeOffsetRegister)
            .chain(freeOldValueRegister)
            .chain(freeValueRegister)
            .chain(freeBaseRegister);
    }

    @Override
    public InstructionBlock visit(Nop e) {
        return new InstructionBlock();
    }

    @Override
    public InstructionBlock visit(Fun e) {

        /*
        InstructionBlock regBlock = getTemporaryRegister();
        int reg = regBlock.getUsedRegisters().get(0);
        */
        if (functionLabels.get(e.l.label) == null) {
            return new InstructionBlock()
                .add(factory.instr("LDR", "$", "=" + e.l.label));
        }
        return new InstructionBlock()
            .add(factory.instr("LDR", "$", "=" + functionLabels.get(e.l.label)));
    }

    @Override
    public InstructionBlock visit(AppClosure e) {
        InstructionBlock result = new InstructionBlock();

        InstructionBlock freeArgumentRegisters = new InstructionBlock();

        int counter = 4 - (e.args.size() % 4);
        for (int i = e.args.size() - 1; i >= 0; i--) {
            if (counter == 4) {
                counter = 0;
            }
            Id id = e.args.get(i);
            InstructionBlock paramBlock = getRegister(id);
            int paramReg = paramBlock.getUsedRegisters().get(0);
            result.chain(paramBlock)
                .add(factory.instr("MOV", "r" + (3 - counter), "r" + paramReg));

            result.add(factory.instr("PUSH", "{r" + (3 - counter) + "}"));
            freeArgumentRegisters.chain(freeRegister(paramReg));
            counter ++;
        }

        // Retrieve closure address
        InstructionBlock closureAddrRegBlock = getTemporaryRegister();
        int closureAddrRegister = closureAddrRegBlock.getUsedRegisters().get(0);

        InstructionBlock closureArrayRegBlock = getRegister(e.id);
        int closureArrayRegister = closureArrayRegBlock.getUsedRegisters().get(0);

        InstructionBlock closureAddr = new InstructionBlock()
        .add(factory.instr("LDR", "r" + closureAddrRegister, "[r" + closureArrayRegister + "]"));

        InstructionBlock freeClosureArray = freeRegister(closureArrayRegister);
        InstructionBlock freeClosureAddr = freeRegister(closureAddrRegister);

        InstructionBlock popArguments = new InstructionBlock(factory.instr("ADD", "sp", "sp", "#" + (4 + (4 * e.args.size()))));

        return result
            .chain(closureAddrRegBlock)
            .chain(closureArrayRegBlock)
            .chain(closureAddr)
            .add(factory.instr("PUSH", "{r" + closureArrayRegister + "}")).comment("Closure info")
            .add(factory.instr("BLX", "r" + closureAddrRegister)).comment("Apply closure")
            .add(factory.instr("MOV", "$", "r0"))
            .chain(popArguments)
            .chain(freeClosureArray)
            .chain(freeClosureAddr)
            .chain(freeArgumentRegisters);
    }

    @Override
    public InstructionBlock visit(FunDefs e) {
        InstructionBlock result = new InstructionBlock();
        for (Exp exp: e.funs) {
            result.chain(exp.accept(this).endWithNewline());
        }
        return result;
    }

    @Override
    public InstructionBlock visit(Self e) {
        InstructionBlock regBlock = getTemporaryRegister();
        int reg = regBlock.getUsedRegisters().get(0);

        return new InstructionBlock()
            .add(factory.instr("LDR", "r" + reg, "[fp, #36]"))
            .useRegister(reg);
    }
}