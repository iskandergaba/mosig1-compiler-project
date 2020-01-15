package backend;

import java.util.*;

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
    
    public void setIndent(int indent) {
        this.indent = indent; 
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addArgument(String arg) {
        this.args.add(arg);
    }

    public void setLabel(String label) {
        this.label = label;
    }
}