package backend;

/**
 * Generates and formats ARM assembly instructions
 */
public class InstructionFactory {
    private int indent;
    private boolean printLabel;
    private String nextLabel;

    public InstructionFactory() {
        this.indent = 0;
        this.printLabel = false;
        this.nextLabel = "";
    }

    /**
     * Adds a label on the next instruction
     * @param label The label (without the semicolon)
     */
    public void setLabel(String label) {
        this.nextLabel = label;
        this.indent = label.length() + 2;
        this.printLabel = true;
    }

    private int computeIndent() {
        if (printLabel) {
            return 1;
        } else {
            return indent;
        }
    }

    /**
     * Creates an instruction and returns it
     * @param name The operator of the instruction (ADD, BL, ...)
     * @param args The arguments of the instruction, for example "r0", "r1", "r3" in "ADD r0, r1, r3"
     * @return The new Instruction
     */
    public Instruction instr(String name, String... args) {
        Instruction result = new Instruction();
        result.setIndent(computeIndent());
        if (printLabel) {
            result.setLabel(nextLabel);
        }
        printLabel = false;

        result.setName(name);

        if (args.length > 0 && args[0] == "$") {
            result.setArgumentToReplace(0);
        }

        for (String arg: args) {
            result.addArgument(arg);
        }

        return result;
    }
}