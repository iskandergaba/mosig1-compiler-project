package backend;

import java.util.*;

/**
 * Visits the ASML AST and generates ARM assembly code.
 */
public class CodeGenerationVisitor implements ObjVisitor<InstructionBlock> {

    // Generates label for branching (if's, function calls, ...)
    private LabelGenerator labelGenerator;

    // The function name currently parsing
    private String currentFunction;

    // The memory construct, to perform register allocation
    private Memory memory = new Memory();

    // A mapping between function names and labels
    private Map<String, String> functionLabels;

    // The instruction factory, used to create and parse ARM instructions
    // with the correct indentation and form.
    private InstructionFactory factory;

    private Boolean[] isRegisterPushed;

    private int getOffset(Id id) {
        return memory.idOffMap.get(this.currentFunction + "." + (id.id));
    }

    // This function returns an available register for variable retrieval.
    // Currently, if there are no registers available for retrieval, the
    // function prints an error and returns a -1 register, effectively producing
    // an erroneous assembly output.
    private InstructionBlock getNextAvailableRegister() {
        for(int i = 4; i <= 12; i++) {
            if (i == 11) continue;
            if (memory.regIsFree[i]) {
                memory.regIsFree[i] = false;
                return new InstructionBlock().useRegister(i);
            }
        }
        
        // At this point, we will check if we can use a used register by
        // pushing it on the stack and popping it afterwards
        for(int i = 4; i <= 12; i++) {
            if (i == 11) continue;
            if (!isRegisterPushed[i]) {
                isRegisterPushed[i] = true;
                return new InstructionBlock(factory.instr("PUSH", "{r" + i + "}"))
                    .useRegister(i);
            }
        }
        return null;
    }

    private InstructionBlock freeRegister(int reg) {
        if (isRegisterPushed[reg]) {
            isRegisterPushed[reg] = false;
            return new InstructionBlock(factory.instr("POP", "{r" + reg + "}"));
        } else {
            memory.regIsFree[reg] = true;
            return new InstructionBlock();
        }
    }

    private InstructionBlock retrieveVariable(Id id, int reg) {
        return new InstructionBlock(factory.instr("LDR", "r" + reg, "[sp,#" + getOffset(id) + "]"));
    }

    private InstructionBlock spillVariable(Id id, int reg) {
        return new InstructionBlock(factory.instr("STR", "r" + reg, "[sp,#" + getOffset(id) + "]"));
    }

    private InstructionBlock prologue(int size) {
        return new InstructionBlock()
            .add(factory.instr("MOV", "ip", "sp"))
            .add(factory.instr("PUSH", "{fp, ip, lr, pc}"))
            .add(factory.instr("SUB", "fp", "ip", "#4"))
            .commentFirst("Function " + currentFunction);
    }

    private InstructionBlock epilogue(int size) {
        return new InstructionBlock()
            .add(factory.instr("LDM", "sp", "{fp, sp, lr}"));
    }

    // Used for generating arithmetic operations like ADD, SUB, ...
    private InstructionBlock arithmeticOperation(String op, Id id, Exp e) {
        memory.allocate(id);

        InstructionBlock leftOperandBlock = getNextAvailableRegister();
        int leftOperandRegister = leftOperandBlock.getUsedRegisters().get(0);

        InstructionBlock leftOperand = retrieveVariable(id, leftOperandRegister);
        InstructionBlock rightOperand = e.accept(this);
        int rightOperandRegister = rightOperand.getUsedRegisters().get(0);

        InstructionBlock operation = new InstructionBlock(factory.instr(op, "$", "r" + leftOperandRegister, "r" + rightOperandRegister));
        
        InstructionBlock freeLeftOperand = freeRegister(leftOperandRegister);
        InstructionBlock freeRightOperand = freeRegister(rightOperandRegister);

        return leftOperandBlock
            .chain(leftOperand)
            .chain(rightOperand)
            .chain(operation)
            .chain(freeRightOperand)
            .chain(freeLeftOperand);
    }

    // Used for generating condition checking, like =, >, ...
    private InstructionBlock conditionOperation(String condition, Id id, Exp e) {
        memory.allocate(id);
        
        InstructionBlock leftOperandBlock = getNextAvailableRegister();
        int leftOperandRegister = leftOperandBlock.getUsedRegisters().get(0);

        InstructionBlock leftOperand = retrieveVariable(id, leftOperandRegister);
        InstructionBlock rightOperand = e.accept(this);
        int rightOperandRegister = rightOperand.getUsedRegisters().get(0);

        InstructionBlock operation = new InstructionBlock(factory.instr("CMP", "r" + leftOperandRegister, "r" + rightOperandRegister))
            .add(factory.instr(condition, "$"));
        
        InstructionBlock freeLeftOperand = freeRegister(leftOperandRegister);
        InstructionBlock freeRightOperand = freeRegister(rightOperandRegister);

        return leftOperandBlock
            .chain(leftOperand)
            .chain(rightOperand)
            .chain(operation)
            .chain(freeRightOperand)
            .chain(freeLeftOperand);
    }

    public CodeGenerationVisitor() {
        this.labelGenerator = new LabelGenerator();
        this.currentFunction = "";
        this.functionLabels = new HashMap<String, String>();
        this.factory = new InstructionFactory();
        this.isRegisterPushed = new Boolean[16];

        for (int i = 0; i < 16; i++) {
            this.isRegisterPushed[i] = false;
        }
    }

    @Override
    public InstructionBlock visit(Int e) {
        InstructionBlock block = getNextAvailableRegister();
        int reg = block.getUsedRegisters().get(0);

        return block.add(factory.instr("MOV", "r" + reg, "#" + e.i))
            .useRegister(reg);
    }

    @Override
    public InstructionBlock visit(Float e) {
        return null;   
    }

    @Override
    public InstructionBlock visit(Neg e) {
        memory.allocate(e.id);
        InstructionBlock registerBlock = getNextAvailableRegister();
        int reg = registerBlock.getUsedRegisters().get(0);
        
        InstructionBlock b = new InstructionBlock(factory.instr("RSB", "$", "#0", "r" + reg));
        
        freeRegister(reg);
        
        return registerBlock.chain(b);
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
        return null;
    }

    @Override
    public InstructionBlock visit(FAdd e) {
        return null;
    }

    @Override
    public InstructionBlock visit(FSub e) {
        return null;
    }

    @Override
    public InstructionBlock visit(FMul e) {
        return null;
    }

    @Override
    public InstructionBlock visit(FDiv e) {
        return null;
    }

    @Override
    public InstructionBlock visit(Eq e) {
        return conditionOperation("BNE", e.id, e.e);
    }

    @Override
    public InstructionBlock visit(LE e) {
        return conditionOperation("BGE", e.id, e.e);
    }

    @Override
    public InstructionBlock visit(GE e) {
        return conditionOperation("BLE", e.id, e.e);
    }

    @Override
    public InstructionBlock visit(FEq e) {
        return null;
    }

    @Override
    public InstructionBlock visit(FLE e) {
        return null;
    }

    @Override
    public InstructionBlock visit(If e) {
        String labelElse = labelGenerator.getLabel();
        String labelEnd = labelGenerator.getLabel();

        InstructionBlock condition = e.cond.accept(this).setReturn(labelElse);
        InstructionBlock thenBlock = e.e1.accept(this);
        Instruction branchToEnd = factory.instr("B", labelEnd);
        
        factory.setLabel(labelElse);
        InstructionBlock elseBlock = e.e2.accept(this);


        factory.setLabel(labelEnd);

        return condition
            .chain(thenBlock)
            .add(branchToEnd)
            .chain(elseBlock);
    }

    @Override
    public InstructionBlock visit(Let e) {
        memory.allocate(e.id);
        
        // Allocation of the register for spilling the result of the
        // let expression (not the in expression)
        InstructionBlock registerBlock = getNextAvailableRegister();
        int reg = registerBlock.getUsedRegisters().get(0);

        InstructionBlock letExpression = e.e1.accept(this)
            .commentFirst("let " + e.id + " = ? in...");
        
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

        letExpression.setReturn("r" + reg);

        return registerBlock
            .chain(letExpression)
            .chain(saveVariable)
            .chain(freeReg)
            .chain(inBlock);
    }

    @Override
    public InstructionBlock visit(Var e) {
        memory.allocate(e.id);

        InstructionBlock registerBlock = getNextAvailableRegister();
        int reg = registerBlock.getUsedRegisters().get(0);
        
        return registerBlock
            .chain(retrieveVariable(e.id, reg))
            .useRegister(reg);
    }

    @Override
    public InstructionBlock visit(LetRec e) {
        // We need to get the 'size' of the function (the number of local variables)
        // so that we can generate the prologue
        int size = e.accept(new SizeVisitor());
        
        memory.memInit();
        memory.UpdateScope(e.fd.fun.l);
        this.currentFunction = e.fd.fun.l.label;

        String functionLabel = this.labelGenerator.getLabel();
        functionLabels.put(currentFunction, functionLabel);

        factory.setLabel(functionLabel);

        InstructionBlock result = prologue(size)
            .chain(e.e.accept(this)).setReturn("r0")
            .chain(epilogue(size))
            .add(factory.instr("BX", "lr")).comment("Return");

        return result.setFunctionLabel(functionLabel);
    }   

    @Override
    public InstructionBlock visit(Call e) {
        // For the moment this will do.
        if (e.args.size() > 4) {
            System.err.println("Warning: too many arguments in syscall.");
        }

        InstructionBlock result = new InstructionBlock();
        int callParameterRegister = 0;

        for (Id id: e.args) {
            if (callParameterRegister < 4) { // Don't put more than 4 parameters
                result.chain(retrieveVariable(id, callParameterRegister++));
            }
        }

        String functionLabel = functionLabels.get(e.f.label);
        
        // For the moment this is how we handle external functions.
        if (functionLabel == null) { 
            functionLabel = e.f.label;
        }

        return result
            .add(factory.instr("BL", functionLabel)).comment("call " + e.f.label)
            .add(factory.instr("MOV", "$", "r0"));
    }

    @Override
    public InstructionBlock visit(New e) {
        InstructionBlock size = e.size.accept(this);
        int sizeRegister = size.getUsedRegisters().get(0);

        InstructionBlock heapBaseAddrBlock = getNextAvailableRegister();
        InstructionBlock heapOffsetAddrBlock = getNextAvailableRegister();
        InstructionBlock heapBaseRegisterBlock = getNextAvailableRegister();
        InstructionBlock heapOffsetRegisterBlock = getNextAvailableRegister();

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

        InstructionBlock offset = e.offset.accept(this);
        int offsetRegister = offset.getUsedRegisters().get(0);

        base.chain(offset)
            .add(factory.instr("ADD", "r"+baseRegister, "r"+baseRegister, "r"+offsetRegister))
            .add(factory.instr("LDR", "$", "[r"+baseRegister+"]"));

        InstructionBlock freeOffsetRegister = freeRegister(offsetRegister);
        InstructionBlock freeBaseRegister = freeRegister(baseRegister);

        return base
            .chain(offset)
            .chain(freeOffsetRegister)
            .chain(freeBaseRegister);
    }

    @Override
    public InstructionBlock visit(Put e) {
        InstructionBlock base = e.base.accept(this);
        int baseRegister = base.getUsedRegisters().get(0);
        
        InstructionBlock offset = e.offset.accept(this);
        int offsetRegister = offset.getUsedRegisters().get(0);

        InstructionBlock valueRegisterBlock = getNextAvailableRegister();
        int valueRegister = valueRegisterBlock.getUsedRegisters().get(0);

        InstructionBlock value = retrieveVariable(e.dest, valueRegister);

        InstructionBlock oldValueRegisterBlock = getNextAvailableRegister();
        int oldValueRegister = oldValueRegisterBlock.getUsedRegisters().get(0);

        base.chain(offset)
            .chain(value)
            .add(factory.instr("ADD", "r"+baseRegister, "r"+baseRegister, "r"+offsetRegister))
            .add(factory.instr("LDR", "r"+oldValueRegister, "[r"+baseRegister+"]"))
            .add(factory.instr("STR", "r"+valueRegister, "[r"+baseRegister+"]"))
            .add(factory.instr("MOV", "$", "r"+oldValueRegister));

        InstructionBlock freeOffsetRegister = freeRegister(offsetRegister);
        InstructionBlock freeBaseRegister = freeRegister(baseRegister);
        InstructionBlock freeValueRegister = freeRegister(valueRegister);
        InstructionBlock freeOldValueRegister = freeRegister(oldValueRegister);

        return base
            .chain(valueRegisterBlock)
            .chain(oldValueRegisterBlock)
            .chain(base)
            .chain(freeOffsetRegister)
            .chain(freeOldValueRegister)
            .chain(freeValueRegister)
            .chain(freeBaseRegister);
    }

    @Override
    public InstructionBlock visit(Nop e) {

        return new InstructionBlock(factory.instr("NOP"));
    }

    @Override
    public InstructionBlock visit(Fun e) {
        InstructionBlock regBlock = getNextAvailableRegister();
        int reg = regBlock.getUsedRegisters().get(0);

        return new InstructionBlock()
            .add(factory.instr("LDR", "r" + reg, "=" + functionLabels.get(e.l.label)))
            .useRegister(reg);
    }

    @Override
    public InstructionBlock visit(AppClosure e) {
        memory.allocate(e.id);

        if (e.args.size() > 4) {
            System.err.println("Too many arguments in closure application.");
            return null;
        }

        InstructionBlock result = new InstructionBlock();
        int callParameterRegister = 0;

        for (Id id: e.args) {
            if (callParameterRegister < 4) { // Don't put more than 4 parameters
                result.chain(retrieveVariable(id, callParameterRegister++));
            }
        }

        // Retrieve closure address
        InstructionBlock closureAddrRegBlock = getNextAvailableRegister();
        int closureAddrRegister = closureAddrRegBlock.getUsedRegisters().get(0);

        InstructionBlock closureArrayRegBlock = getNextAvailableRegister();
        int closureArrayRegister = closureArrayRegBlock.getUsedRegisters().get(0);

        InstructionBlock closureArray = retrieveVariable(e.id, closureArrayRegister);
        InstructionBlock closureAddr = new InstructionBlock()
            .add(factory.instr("LDR", "r" + closureAddrRegister, "[r" + closureArrayRegister + "]"));

        InstructionBlock freeClosureArray = freeRegister(closureArrayRegister);
        InstructionBlock freeClosureAddr = freeRegister(closureAddrRegister);
        
        return result
            .chain(closureAddrRegBlock)
            .chain(closureArrayRegBlock)
            .chain(closureArray)
            .chain(closureAddr)
            .add(factory.instr("PUSH", "{r" + closureArrayRegister + "}"))
            .add(factory.instr("BX", "r" + closureAddrRegister))
            .chain(freeClosureArray)
            .chain(freeClosureAddr);
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
        InstructionBlock regBlock = getNextAvailableRegister();
        int reg = regBlock.getUsedRegisters().get(0);

        return new InstructionBlock()
            .add(factory.instr("LDR", "r" + reg, "[fp, #-4]"))
            .useRegister(reg);
    }
}