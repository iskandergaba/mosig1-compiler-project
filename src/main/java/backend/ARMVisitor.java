package backend;

import java.util.*;

class ARMVisitor implements ObjVisitor<String> {
    private LabelGenerator labelGenerator;
    private String currentFunction;
    private Memory memory;
    private boolean intImmediate = false; // TODO: find a way to do this better
    private Map<String, String> functionLabels;
    private InstructionFactory factory;

    private String getRegister(Id id) {
        return memory.finalMap.get(this.currentFunction + "." + id.id);
    }

    private String prologue() {
        return factory.push("{r0-r3,r14}").toString();
    }

    private String exit() {
        return factory.mov("r0", "#0") + "\n"
            + factory.mov("r7", "#1") + "\n"
            + factory.swi("#0");
    }

    private String epilogue() {
        return factory.pop("{r0-r3,r14}").toString();
    }

    private String header() {
        return ".text\n.global _start\n\n";
    }

    public ARMVisitor(Memory memory) {
        this.memory = memory;
        this.labelGenerator = new LabelGenerator();
        this.currentFunction = "";
        this.functionLabels = new HashMap<String, String>();
        this.factory = new InstructionFactory();
    }

    @Override
    public String visit(Int e) {
        if (intImmediate) {
            return String.format("#%d", e.i);
        } else {
            String instr = factory.mov("%%s", "#%d").toString();
            return String.format(instr, e.i);
        }
    }

    @Override
    public String visit(Float e) {
        return String.format("%f", e.f);
    }

    @Override
    public String visit(Neg e) {
        return factory.rsb("%s", "#0", getRegister(e.id)).toString();
    }

    @Override
    public String visit(Add e) {
        intImmediate = true;
        String reg = getRegister(e.id);
        String op = e.e.accept(this);
        String result = factory.add("%s", reg, op).toString();
        intImmediate = false;
        return result;
    }

    @Override
    public String visit(Sub e) {
        intImmediate = true;
        String reg = getRegister(e.id);
        String op = e.e.accept(this);
        String result = factory.sub("%s", reg, op).toString();
        intImmediate = false;
        return result;
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
        String result = factory.cmp("%s") + "\n"
            + factory.branch("NE", "%%s") + "\n";
        return String.format(result, reg1, reg2);
    }

    @Override
    public String visit(LE e) {
        intImmediate = true;
        String reg1 = getRegister(e.id);
        String reg2 = e.e.accept(this);
        intImmediate = false;
        String result = factory.cmp("%s") + "\n"
            + factory.branch("GE", "%%s") + "\n";
        return String.format(result, reg1, reg2);
    }

    @Override
    public String visit(GE e) {
        intImmediate = true;
        String reg1 = getRegister(e.id);
        String reg2 = e.e.accept(this);
        intImmediate = false;
        String result = factory.cmp("%s") + "\n"
            + factory.branch("LE", "%%s") + "\n";
        return String.format(result, reg1, reg2);
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
        String labelEnd = labelGenerator.getLabel();

        String condition = String.format(e.cond.accept(this), labelElse);
        String then = e.e1.accept(this);

        factory.setLabel(labelElse);
        String other = e.e2.accept(this);
        String branchToEnd = factory.branch("", labelEnd).toString();

        factory.setLabel(labelEnd);

        return condition
             + then + "\n"
             + branchToEnd + "\n"
             + other;
    }

    @Override
    public String visit(Let e) {
        String register = getRegister(e.id);
        String result1 = e.e1.accept(this);
        String result2 = e.e2.accept(this);

        return String.format(result1, register) + "\n" + result2;
    }

    @Override
    public String visit(Var e) {
        return getRegister(e.id);
    }

    @Override
    public String visit(LetRec e) {
        String result = "";
        this.currentFunction = e.fd.fun.l.label;

        String functionLabel;
        if (e.fd.fun.l.label == "_") {
            functionLabel = "_start";
        } else {
            functionLabel = this.labelGenerator.getLabel();
        }

        factory.setLabel(functionLabel);
        this.functionLabels.put(currentFunction, functionLabel);

        result += prologue() + "\n";
        result += e.e.accept(this) + "\n";
        result += epilogue() + "\n";
        if (e.fd.fun.l.label == "_") {
            result += exit();
        }
        result += "\n";
        return String.format(result, "r0");
    }

    @Override
    public String visit(Call e) {
        if (e.args.size() > 4) {
            System.err.println("Warning: too many arguments in syscall.");
        }
        String result = "";
        int paramReg = 0;
        for (Id id: e.args) {
            if (paramReg < 4) {
                String reg = getRegister(id);
                result += factory.mov("r" + (paramReg++), reg) + "\n";
            }
        }
        String functionLabel = functionLabels.get(e.f.label);
        if (functionLabel == null) {
            functionLabel = e.f.label;
        }
        result += factory.branch("L", functionLabel);
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
        String result = header();
        for (Exp exp: e.funs) {
            result += exp.accept(this);
        }
        return result;
    }
}