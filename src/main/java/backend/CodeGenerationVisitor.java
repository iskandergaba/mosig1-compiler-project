package backend;

import java.util.*;
import java.util.stream.Collectors;

import common.asml.*;
import common.asml.Float;
import common.visitor.*;

/**
 * Visits the ASML AST and generates ARM assembly code.
 */
public class CodeGenerationVisitor implements ObjVisitor<InstructionBlock> {

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

    private InstructionBlock prologue(int size) {
        return new InstructionBlock()
            .add(factory.instr("MOV", "ip", "sp"))
            .add(factory.instr("PUSH", "{fp, ip, lr, pc}"))
            .add(factory.instr("MOV", "fp", "ip"))
            .add(factory.instr("ADD", "sp", "ip", "#-" + (4 * size)))
            .commentFirst("Function " + currentFunction);
    }

    private InstructionBlock epilogue() {
        return new InstructionBlock()
            .add(factory.instr("LDM", "sp", "{fp, sp, lr}"));
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

    public CodeGenerationVisitor(Map<String, Integer> registers, Map<String, Integer> locations) {
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
        return new InstructionBlock(factory.instr("MOV", "$", "#" + e.i));
    }

    @Override
    public InstructionBlock visit(Float e) {
        return null;   
    }

    @Override
    public InstructionBlock visit(Neg e) {
        InstructionBlock registerBlock = getRegister(e.id);
        int reg = registerBlock.getUsedRegisters().get(0);
        
        InstructionBlock b = new InstructionBlock(factory.instr("RSB", "$", "#0", "r" + reg));
        
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
        // Allocation of the register for spilling the result of the
        // let expression (not the in expression)
        InstructionBlock letExpression = e.e1.accept(this);

        InstructionBlock registerBlock = getRegister(e.id);
        int reg = registerBlock.getUsedRegisters().get(0);
        
        letExpression.setReturn("r" + reg);
        
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
        
        return registerBlock
            .useRegister(reg);
    }

    @Override
    public InstructionBlock visit(LetRec e) {
        // We need to get the 'size' of the function (the number of local variables)
        // so that we can generate the prologue
        int size = e.accept(new SizeVisitor());
        
        this.currentFunction = e.fd.fun.l.label;

        String functionLabel = this.labelGenerator.getLabel();
        functionLabels.put(currentFunction, functionLabel);

        factory.setLabel(functionLabel);

        InstructionBlock result = prologue(size)
            .chain(e.e.accept(this)).setReturn("r0")
            .chain(epilogue())
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

        List<String> argumentRegisterList = new ArrayList<String>();

        for (Id id: e.args) {
            if (callParameterRegister < 4) { // Don't put more than 4 parameters
                InstructionBlock paramBlock = getRegister(id);
                int paramReg = paramBlock.getUsedRegisters().get(0);

                result.chain(paramBlock)
                    .add(factory.instr("MOV", "r" + callParameterRegister, "r" + paramReg));

                argumentRegisterList.add("r" + callParameterRegister++);
            }
        }

        String argumentRegisters = argumentRegisterList
            .stream()
            .collect(Collectors.joining(", "));
        InstructionBlock pushArguments = new InstructionBlock(factory.instr("PUSH", "{" + argumentRegisters + "}"));
        InstructionBlock popArguments = new InstructionBlock(factory.instr("POP", "{" + argumentRegisters + "}"));

        String functionLabel = functionLabels.get(e.f.label);
        
        // For the moment this is how we handle external functions.
        if (functionLabel == null) { 
            functionLabel = e.f.label;
        }

        return result
            .chain(pushArguments)
            .add(factory.instr("BL", functionLabel)).comment("call " + e.f.label)
            .chain(popArguments)
            .add(factory.instr("MOV", "$", "r0"));
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
        
        InstructionBlock base = e.base.accept(this);
        int baseRegister = base.getUsedRegisters().get(0);
        

        InstructionBlock oldValueRegisterBlock = getTemporaryRegister();
        int oldValueRegister = oldValueRegisterBlock.getUsedRegisters().get(0);

        base.chain(offset)
            .add(factory.instr("ADD", "r"+baseRegister, "r"+baseRegister, "r"+offsetRegister))
            .add(factory.instr("LDR", "r"+oldValueRegister, "[r"+baseRegister+"]"))
            .add(factory.instr("STR", "r"+valueRegister, "[r"+baseRegister+"]"))
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
        InstructionBlock regBlock = getTemporaryRegister();
        int reg = regBlock.getUsedRegisters().get(0);

        return new InstructionBlock()
            .add(factory.instr("LDR", "r" + reg, "=" + functionLabels.get(e.l.label)))
            .useRegister(reg);
    }

    @Override
    public InstructionBlock visit(AppClosure e) {
        if (e.args.size() > 4) {
            System.err.println("Too many arguments in closure application.");
            return null;
        }

        InstructionBlock result = new InstructionBlock();
        int callParameterRegister = 0;

        List<String> argumentRegisterList = new ArrayList<String>();

        for (Id id: e.args) {
            if (callParameterRegister < 4) { // Don't put more than 4 parameters
                InstructionBlock paramBlock = getRegister(id);
                int paramReg = paramBlock.getUsedRegisters().get(0);

                result.chain(paramBlock)
                    .add(factory.instr("MOV", "r" + callParameterRegister, "r" + paramReg));

                argumentRegisterList.add("r" + callParameterRegister++);
            }
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
        
        argumentRegisterList.add("r" + closureArrayRegister);

        String argumentRegisters = argumentRegisterList
        .stream()
        .collect(Collectors.joining(", "));
        InstructionBlock pushArguments = new InstructionBlock(factory.instr("PUSH", "{" + argumentRegisters + "}"));
        InstructionBlock popArguments = new InstructionBlock(factory.instr("POP", "{" + argumentRegisters + "}"));

        return result
            .chain(closureAddrRegBlock)
            .chain(closureArrayRegBlock)
            .chain(closureAddr)
            .chain(pushArguments)
            .add(factory.instr("BX", "r" + closureAddrRegister)).comment("Apply closure")
            .chain(popArguments)
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
        InstructionBlock regBlock = getTemporaryRegister();
        int reg = regBlock.getUsedRegisters().get(0);

        return new InstructionBlock()
            .add(factory.instr("LDR", "r" + reg, "[fp, #20]"))
            .useRegister(reg);
    }
}