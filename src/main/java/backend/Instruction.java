package backend;

import java.util.*;

abstract class Instruction {
    protected String name;
    protected List<String> args;
    protected int indent;
    protected String label;

    protected static int instrSize = 6;

    public Instruction() {
        this.args = new ArrayList<String>();
        this.label = null;
    }

    public abstract String toString();
    
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

class CommaSeparatedInstruction extends Instruction {
    public String toString() {
        String result = "";
        if (label != null) {
            result += label + ":";
        }
        String format = "%" + indent + "s%-" + instrSize + "s ";
        result += String.format(format, " ", name);
        result += String.join(", ", args);
        return result;
    }
}

class ArgumentListInstruction extends Instruction {
    public String toString() {
        String result = "";
        if (label != null) {
            result += label + ":";
        }
        String format = "%" + indent + "s%-" + instrSize + "s ";

        result += String.format(format, " ", name);
        result += args.get(0);
        return result;
    }
}