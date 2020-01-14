package backend;

public class InstructionFactory {
    private int indent;
    private boolean printLabel;
    private String nextLabel;

    public InstructionFactory() {
        this.indent = 0;
        this.printLabel = false;
        this.nextLabel = "";
    }

    public void setIndent(int indent) {
        this.indent = indent;
        this.printLabel = true;
    }

    public void setLabel(String label) {
        this.nextLabel = label;
        this.setIndent(label.length() + 2);
    }

    private int computeIndent() {
        if (printLabel) {
            return 1;
        } else {
            return indent;
        }
    }

    private CommaSeparatedInstruction createCSInstruction() {
        CommaSeparatedInstruction result = new CommaSeparatedInstruction();
        result.setIndent(computeIndent());
        if (printLabel) {
            result.setLabel(nextLabel);
        }
        printLabel = false;
        return result;
    }

    private ArgumentListInstruction createALInstruction() {
        ArgumentListInstruction result = new ArgumentListInstruction();
        result.setIndent(computeIndent());
        if (printLabel) {
            result.setLabel(nextLabel);
        }
        printLabel = false;
        return result;
    }

    public Instruction add(String dest, String op1, String op2) {
        Instruction result = createCSInstruction();

        result.setName("ADD");
        result.addArgument(dest);
        result.addArgument(op1);
        result.addArgument(op2);
        return result;
    }

    public Instruction sub(String dest, String op1, String op2) {
        Instruction result = createCSInstruction();
        
        result.setName("ADD");
        result.addArgument(dest);
        result.addArgument(op1);
        result.addArgument(op2);
        return result;
    }

    public Instruction rsb(String dest, String op1, String op2) {
        return sub(dest, op2, op1);
    }

    public Instruction push(String registers) {
        Instruction result = createALInstruction();

        result.setName("PUSH");
        result.addArgument(registers);
        return result;
    }

    public Instruction pop(String registers) {
        Instruction result = createALInstruction();

        result.setName("POP");
        result.addArgument(registers);
        return result;
    }

    public Instruction mov(String dest, String src) {
        Instruction result = createCSInstruction();

        result.setName("MOV");
        result.addArgument(dest);
        result.addArgument(src);
        return result;
    }

    public Instruction cmp(String reg1, String reg2) {
        Instruction result = createCSInstruction();

        result.setName("CMP");
        result.addArgument(reg1);
        result.addArgument(reg2);
        return result;
    }

    public Instruction branch(String condition, String label) {
        Instruction result = createCSInstruction();

        result.setName("B" + condition);
        result.addArgument(label);
        return result;
    }

    public Instruction swi(String syscall) {
        Instruction result = createCSInstruction();

        result.setName("SWI");
        result.addArgument(syscall);
        return result;
    }

    public Instruction load(String reg, String mem) {
        Instruction result = createCSInstruction();

        result.setName("LDR");
        result.addArgument(reg);
        result.addArgument(mem);
        return result;
    }

    public Instruction store(String reg, String mem) {
        Instruction result = createCSInstruction();

        result.setName("STR");
        result.addArgument(reg);
        result.addArgument(mem);
        return result;
    }
}