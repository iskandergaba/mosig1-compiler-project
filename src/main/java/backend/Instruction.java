package backend;

import java.util.*;

/**
 * Represents an ARM instruction, with eventually a label before it.
 */
public class Instruction {
    private String name;
    public List<String> args;
    private int indent;
    public String label;
    private String comment;
    private boolean finalInstruction;
    private Optional<Integer> argumentToReplace;

    private static final int instrSize = 6;
    private static final int argsSize = 24;

    public Instruction() {
        this.args = new ArrayList<String>();
        this.label = null;
        this.comment = null;
        this.finalInstruction = false;
        this.argumentToReplace = Optional.empty();
    }

    @Override
    public String toString() {
        String result = "";
        if (label != null) {
            result += label + ":";
        }
        if (indent == 0) {
            indent = 1;
        }
        String format = "%" + indent + "s%-" + instrSize + "s %-" + argsSize + "s";

        String formattedArgs = String.join(", ", args);
        result += String.format(format, " ", name, formattedArgs);

        if (comment != null) {
            result += " @ " + comment;
        }

        return result + (finalInstruction ? "\n\n" : "\n");
    }

    /**
     * Sets the indentation of the instruction.
     * @param indent The number of spaces before the instruction
     */
    public void setIndent(int indent) {
        this.indent = indent;
    }

    /**
     * Sets the name (operator) of the instruction
     * @param name The name of the instruction
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds an argument to the instruction
     * @param arg The new argument
     */
    public void addArgument(String arg) {
        this.args.add(arg);
    }

    /**
     * Adds a label before the instruction
     * @param label The label to be printed before the instruction
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Adds a comment at the end of the instruction
     * @param comment the comment to add
     * @return this instruction
     */
    public Instruction comment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Replaces a placeholder argument with its real value
     * @param value The replacement for the placeholder
     * @return this instruction
     */
    public Instruction replaceArgument(String value) {
        if (this.argumentToReplace.isPresent()) {
            this.args.set(this.argumentToReplace.get(), value);
            this.argumentToReplace = Optional.empty();
        }
        return this;
    }

    public boolean hasPlaceholder() {
        return this.argumentToReplace.isPresent();
    }

    /**
     * Sets the position of the argument placeholder in the instruction
     * @param instruction The position of the placeholder
     * @return this instruction
     */
    public Instruction setArgumentToReplace(int instruction) {
        this.argumentToReplace = Optional.of(instruction);
        return this;
    }

    /**
     * Sets the instruction as "final", which will add an extra newline
     * character at the end of the instruction, used to separate functions
     * in the program. This is not necessary, but it makes the generated
     * code more readable
     * @return This instruction
     */
    public Instruction setFinal() {
        this.finalInstruction = true;
        return this;
    }
}