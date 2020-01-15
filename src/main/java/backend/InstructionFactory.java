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

    public Instruction instr(String name, String... args) {
        Instruction result = new Instruction();
        result.setIndent(computeIndent());
        if (printLabel) {
            result.setLabel(nextLabel);
        }
        printLabel = false;

        result.setName(name);

        for (String arg: args) {
            result.addArgument(arg);
        }

        return result;
    }
}