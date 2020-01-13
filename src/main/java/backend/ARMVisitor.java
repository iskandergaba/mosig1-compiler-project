package backend;

import java.util.*;

class ARMVisitor implements ObjVisitor<String> {
    private LabelGenerator labelGenerator;
    private String currentFunction;
    private Memory memory;
    private boolean intImmediate = false; // TODO: find a way to do this better
    private Map<String, String> functionLabels;

    private String getRegister(Id id) {
        return "r" + memory.varMap.get(/*this.currentFunction + "." + */id.id);
    }

    public ARMVisitor(Memory memory) {
        this.memory = memory;
        this.labelGenerator = new LabelGenerator();
        this.currentFunction = "";
        this.functionLabels = new HashMap<String, String>();
    }

    @Override
    public String visit(Int e) {
        if (intImmediate) {
            return String.format("#%d", e.i);
        } else {
            return String.format("MOV %%s, #%d\n", e.i);
        }
    }

    @Override
    public String visit(Float e) {
        return String.format("%f", e.f);
    }

    @Override
    public String visit(Neg e) {
        return "RSB %s, #0, " + getRegister(e.id);
    }

    @Override
    public String visit(Add e) {
        intImmediate = true;
        String result = "ADD %s, " + getRegister(e.id) + ", " + e.e.accept(this);
        intImmediate = false;
        return result + "\n";
    }

    @Override
    public String visit(Sub e) {
        intImmediate = true;
        String result = "SUB %s, " + getRegister(e.id) + ", " + e.e.accept(this);
        intImmediate = false;
        return result + "\n";
    }

    @Override
    public String visit(FNeg e) {

        return "";
    }

    @Override
    public String visit(FAdd e) {

        return "";
    }

    @Override
    public String visit(FSub e) {

        return "";
    }

    @Override
    public String visit(FMul e) {

        return "";
    }

    @Override
    public String visit(FDiv e) {

        return "";
    }

    @Override
    public String visit(Eq e) {
        intImmediate = true;
        String reg1 = getRegister(e.id);
        String reg2 = e.e.accept(this);
        intImmediate = false;
        return String.format("CMP %s, %s\nBNE %%s\n", reg1, reg2);
    }

    @Override
    public String visit(LE e) {
        intImmediate = true;
        String reg1 = getRegister(e.id);
        String reg2 = e.e.accept(this);
        intImmediate = false;
        return String.format("CMP %s, %s\nBGE %%s\n", reg1, reg2);
    }

    @Override
    public String visit(GE e) {
        intImmediate = true;
        String reg1 = getRegister(e.id);
        String reg2 = e.e.accept(this);
        intImmediate = false;
        return String.format("CMP %s, %s\nBLE %%s\n", reg1, reg2);
    }

    @Override
    public String visit(FEq e) {

        return "";
    }

    @Override
    public String visit(FLE e) {

        return "";
    }

    @Override
    public String visit(If e) {
        String labelElse = labelGenerator.getLabel();

        String condition = String.format(e.cond.accept(this), labelElse);
        String then = e.e1.accept(this);
        String other = e.e2.accept(this);
        return condition + then + other + "\n";
    }

    @Override
    public String visit(Let e) {
        String register = getRegister(e.id);
        String result1 = e.e1.accept(this);
        String result2 = e.e2.accept(this);

        return String.format(result1, register) + "\n" + result2 + "\n";     
    }

    @Override
    public String visit(Var e) {
        return getRegister(e.id);
    }

    @Override
    public String visit(LetRec e) {
        String result = "";
        this.currentFunction = e.fd.fun.l.label;
        String functionLabel = this.labelGenerator.getLabel();
        this.functionLabels.put(currentFunction, functionLabel);
        result += functionLabel + ":\n";
        result += e.e.accept(this);
        return String.format(result, "r0") + "\n";
    }

    @Override
    public String visit(Call e) {
        if (e.args.size() > 4) {
            System.err.println("Warning: too many arguments in syscall.");
        }
        String result = "";
        int reg = 0;
        for (Id id: e.args) {
            if (reg < 4) {
                result += "MOV r" + (reg++) + ", " + getRegister(id) + "\n";
            }
        }
        result += "B " + functionLabels.get(e.f.label) + "\n";
        return result;
    }

    @Override
    public String visit(New e) {

        return "";
    }

    @Override
    public String visit(Get e) {

        return "";
    }

    @Override
    public String visit(Put e) {

        return "";
    }

    @Override
    public String visit(Nop e) {

        return "";
    }

    @Override
    public String visit(Fun e) {

        return "";
    }

    @Override
    public String visit(AppClosure e) {

        return "";
    }

    @Override
    public String visit(FunDefs e) {
        String result = "";
        for (Exp exp: e.funs) {
            result += exp.accept(this);
        }
        return result;
    }
}