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

    private int getOffset(Id id) {
        return memory.idOffMap.get(this.currentFunction + "." + (id.id));
    }

    // This function returns an available register for variable retrieval.
    // Currently, if there are no registers available for retrieval, the
    // function prints an error and returns a -1 register, effectively producing
    // an erroneous assembly output.
    private int getNextAvailableRegister() {
        for(int i = 4; i < 12; i++) {
            if (i == 1) continue;
            if (memory.regIsFree[i]) {
                memory.regIsFree[i] = false;
                return i;
            }
        }
        System.err.println("Warning: no more available register found.");
        return -1;
    }

    private void freeRegister(int reg) {
        memory.regIsFree[reg] = true;
    }

    private InstructionBlock retrieveVariable(Id id, int reg) {
        return new InstructionBlock(factory.instr("LDR", "r" + reg, "[sp,#" + getOffset(id) + "]"));
    }

    private InstructionBlock spillVariable(Id id, int reg) {
        return new InstructionBlock(factory.instr("STR", "r" + reg, "[sp,#" + getOffset(id) + "]"));
    }

    private InstructionBlock prologue(int size) {
        return new InstructionBlock(factory.instr("PUSH", "{r0-r3,r14}"))
            .comment("Prologue for function " + currentFunction)
            .add(factory.instr("ADD", "sp", "sp", "#-" + (4 * size)));
    }

    private InstructionBlock exit() {
        return new InstructionBlock(factory.instr("MOV", "r0", "#0"))
            .comment("Exit syscall")
            .add(factory.instr("MOV", "r7", "#1"))
            .add(factory.instr("SWI", "#0"));
    }

    private InstructionBlock epilogue(int size) {
        return new InstructionBlock(factory.instr("POP", "{r0-r3,r14}"))
            .add(factory.instr("ADD", "sp", "sp", "#" + (4 * size)));
    }

    private String header() {
        return ".text\n.global _start\n\n";
    }

    // Used for generating arithmetic operations like ADD, SUB, ...
    private InstructionBlock arithmeticOperation(String op, Id id, Exp e) {
        memory.allocate(id);

        int leftOperandRegister = getNextAvailableRegister();
        InstructionBlock leftOperand = retrieveVariable(id, leftOperandRegister);
        InstructionBlock rightOperand = e.accept(this);
        int rightOperandRegister = rightOperand.getUsedRegisters().get(0);

        InstructionBlock operation = new InstructionBlock(factory.instr(op, "$", "r" + leftOperandRegister, "r" + rightOperandRegister));
        freeRegister(leftOperandRegister);
        freeRegister(rightOperandRegister);

        return leftOperand.chain(rightOperand).chain(operation);
    }

    // Used for generating condition checking, like =, >, ...
    private InstructionBlock conditionOperation(String condition, Id id, Exp e) {
        memory.allocate(id);
        
        int leftOperandRegister = getNextAvailableRegister();
        InstructionBlock leftOperand = retrieveVariable(id, leftOperandRegister);
        InstructionBlock rightOperand = e.accept(this);
        int rightOperandRegister = rightOperand.getUsedRegisters().get(0);

        InstructionBlock operation = new InstructionBlock(factory.instr("CMP", "r" + leftOperandRegister, "r" + rightOperandRegister))
            .add(factory.instr(condition, "$"));
        
        freeRegister(leftOperandRegister);
        freeRegister(rightOperandRegister);

        return leftOperand.chain(rightOperand).chain(operation);
    }

    public CodeGenerationVisitor() {
        this.labelGenerator = new LabelGenerator();
        this.currentFunction = "";
        this.functionLabels = new HashMap<String, String>();
        this.factory = new InstructionFactory();
    }

    @Override
    public InstructionBlock visit(Int e) {
        int reg = getNextAvailableRegister();
        return new InstructionBlock(factory.instr("MOV", "r" + reg, "#" + e.i))
            .useRegister(reg);
    }

    @Override
    public InstructionBlock visit(Float e) {
        return null;   
    }

    @Override
    public InstructionBlock visit(Neg e) {
        memory.allocate(e.id);
        int reg = getNextAvailableRegister();
        
        InstructionBlock b = new InstructionBlock(factory.instr("RSB", "$", "#0", "r" + reg));
        
        freeRegister(reg);
        
        return b;
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
        
        int reg = getNextAvailableRegister();
        InstructionBlock letExpression = e.e1.accept(this);
        freeRegister(reg);

        InstructionBlock saveVariable = spillVariable(e.id, reg);
        InstructionBlock inBlock = e.e2.accept(this);

        letExpression.setReturn("r" + reg);

        return letExpression.chain(saveVariable).chain(inBlock);
    }

    @Override
    public InstructionBlock visit(Var e) {
        memory.allocate(e.id);

        int reg = getNextAvailableRegister();
        
        return retrieveVariable(e.id, reg).useRegister(reg);
    }

    @Override
    public InstructionBlock visit(LetRec e) {
        // We need to get the 'size' of the function (the number of local variables)
        // so that we can generate the prologue
        int size = e.accept(new SizeVisitor());
        
        memory.memInit();
        memory.UpdateScope(e.fd.fun.l);
        this.currentFunction = e.fd.fun.l.label;

        String functionLabel = currentFunction == "_" ? "_start" : this.labelGenerator.getLabel();
        factory.setLabel(functionLabel);

        InstructionBlock result = prologue(size)
            .chain(e.e.accept(this))
            .chain(epilogue(size));

        if (currentFunction == "_") {
            result.chain(exit());
        }

        return result.setReturn("r0");
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
            .add(factory.instr("PUSH", "lr"))
            .add(factory.instr("BL", functionLabel))
            .add(factory.instr("POP", "lr"));
            
    }

    @Override
    public InstructionBlock visit(New e) {
        return null;
    }

    @Override
    public InstructionBlock visit(Get e) {

        return null;
    }

    @Override
    public InstructionBlock visit(Put e) {

        return null;
    }

    @Override
    public InstructionBlock visit(Nop e) {

        return null;
    }

    @Override
    public InstructionBlock visit(Fun e) {

        return null;
    }

    @Override
    public InstructionBlock visit(AppClosure e) {

        return null;
    }

    @Override
    public InstructionBlock visit(FunDefs e) {
        InstructionBlock result = new InstructionBlock();
        for (Exp exp: e.funs) {
            result.chain(exp.accept(this).endWithNewline());
        }
        return result;
    }
}