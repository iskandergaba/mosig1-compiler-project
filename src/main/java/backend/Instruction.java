package backend;

import java.util.*;

/**
 * Represents an ARM instruction, with eventually a label before it.
 */
public class Instruction {
    protected String name;
    protected List<String> args;
    protected int indent;
    protected String label;

    protected static final int instrSize = 6;

    public Instruction() {
        this.args = new ArrayList<String>();
        this.label = null;
    }

    @Override
    public String toString() {
        String result = "";
        if (label != null) {
            result += label + ":";
        }
        String format = "%" + indent + "s%-" + instrSize + "s ";
        result += String.format(format, " ", name);
        result += String.join(", ", args);
        return result + "\n";
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
}