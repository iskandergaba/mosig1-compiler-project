package backend;

import java.util.*;

public class CodeGenerationVisitor implements ObjVisitor<String> {
    private LabelGenerator labelGenerator;
    private String currentFunction;
    public Memory memory = new Memory();
    private boolean intImmediate = false; // TODO: find a way to do this better
    private Map<String, String> functionLabels;
    private InstructionFactory factory;
    private String varRetrieve; // TODO: ugly !!!!

    private int getOffset(Id id) {
        return memory.idOffMap.get(this.currentFunction + "." + (id.id));
    }

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

    private String retrieveVariable(Id id, int reg) {
        return factory.instr("LDR", "r" + reg, "[r13,#" + getOffset(id) + "]") + "\n";
    }

    private String spillVariable(Id id, int reg) {
        return factory.instr("STR", "r" + reg, "[r13,#" + getOffset(id) + "]") + "\n";
    }

    private String prologue(int size) {
        return factory.instr("PUSH", "{r0-r3,r14}").toString() + "\n"
            + factory.instr("ADD", "r13", "r13", "#-" + (4 * size));
    }

    private String exit() {
        return factory.instr("MOV", "r0", "#0") + "\n"
            + factory.instr("MOV", "r7", "#1") + "\n"
            + factory.instr("SWI", "#0");
    }

    private String epilogue(int size) {
        return factory.instr("POP", "{r0-r3,r14}").toString() + "\n"
            + factory.instr("ADD", "r13", "r13", "#" + (4 * size));
    }

    private String header() {
        return ".text\n.global _start\n\n";
    }

    public CodeGenerationVisitor() {
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
            String instr = factory.instr("MOV", "%%s", "#%d").toString();
            return String.format(instr, e.i);
        }
    }

    @Override
    public String visit(Float e) {
        return String.format("%f", e.f);   
    }

    @Override
    public String visit(Neg e) {
        memory.allocate(e.id);
        int reg = getNextAvailableRegister();
        String res = retrieveVariable(e.id, reg) 
            + factory.instr("RSB", "%s", "#0", "r" + reg);
        freeRegister(reg);
        return res;
    }

    @Override
    public String visit(Add e) {
        intImmediate = true;
        memory.allocate(e.id);

        int reg = getNextAvailableRegister();
        String op = e.e.accept(this);

        String result = retrieveVariable(e.id, reg);

        if (varRetrieve != null) {
            result += varRetrieve;
            varRetrieve = null;
        }

        result += factory.instr("ADD", "%s", "r"+ reg, op).toString();
        
        intImmediate = false;
        freeRegister(reg);
        
        return result;
    }

    @Override
    public String visit(Sub e) {
        intImmediate = true;
        memory.allocate(e.id);

        int reg = getNextAvailableRegister();
        String op = e.e.accept(this);
        String result = retrieveVariable(e.id, reg);
        
        if (varRetrieve != null) {
            result += varRetrieve;
            varRetrieve = null;
        }
        
        result += factory.instr("SUB", "%s", "r" + reg, op).toString();
        intImmediate = false;
        
        freeRegister(reg);
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
        memory.allocate(e.id);
        String reg2 = e.e.accept(this);
        intImmediate = false;

        int reg1 = getNextAvailableRegister();
        String result = retrieveVariable(e.id, reg1);
        if (varRetrieve != null) {
            result += varRetrieve;
            varRetrieve = null;
        }
        freeRegister(reg1);

        result += factory.instr("CMP", "%s", "%s") + "\n"
            + factory.instr("BNE", "%%s") + "\n";
        return String.format(result, "r" + reg1, reg2);
    }

    @Override
    public String visit(LE e) {
        intImmediate = true;
        memory.allocate(e.id);
        String reg2 = e.e.accept(this);
        intImmediate = false;

        int reg1 = getNextAvailableRegister();
        String result = retrieveVariable(e.id, reg1);
        if (varRetrieve != null) {
            result += varRetrieve;
            varRetrieve = null;
        }
        freeRegister(reg1);

        result += factory.instr("CMP", "%s", "%s") + "\n"
            + factory.instr("BGE", "%%s") + "\n";
        return String.format(result, "r" + reg1, reg2);
    }

    @Override
    public String visit(GE e) {
        intImmediate = true;
        memory.allocate(e.id);
        String reg2 = e.e.accept(this);
        intImmediate = false;

        int reg1 = getNextAvailableRegister();
        String result = retrieveVariable(e.id, reg1);
        if (varRetrieve != null) {
            result += varRetrieve;
            varRetrieve = null;
        }
        freeRegister(reg1);

        result += factory.instr("CMP", "%s", "%s") + "\n"
            + factory.instr("BLE", "%%s") + "\n";
        return String.format(result, "r" + reg1, reg2);
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
        String branchToEnd = factory.instr("B", labelEnd).toString();

        factory.setLabel(labelEnd);

        return condition
             + then + "\n"
             + branchToEnd + "\n"
             + other;
    }

    @Override
    public String visit(Let e) {
        memory.allocate(e.id);
        int reg = getNextAvailableRegister();
        String result1 = e.e1.accept(this);
        freeRegister(reg);
        String spill = spillVariable(e.id, reg);
        String result2 = e.e2.accept(this);
        return String.format(result1, "r" + reg) + "\n"
            + spill
            + result2;
    }

    @Override
    public String visit(Var e) {
        memory.allocate(e.id);
        int reg = getNextAvailableRegister();
        freeRegister(reg);
        varRetrieve = retrieveVariable(e.id, reg);
        return "r" + reg;
    }

    @Override
    public String visit(LetRec e) {
        int size = e.accept(new SizeVisitor());
        memory.memInit();
        memory.UpdateScope(e.fd.fun.l);
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

        result += prologue(size) + "\n";
        result += e.e.accept(this) + "\n";
        result += epilogue(size) + "\n";
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
                String location = memory.allocate(id);
                result += factory.instr("LDR", "r" + (paramReg++), location) + "\n";
            }
        }
        String functionLabel = functionLabels.get(e.f.label);
        if (functionLabel == null) {
            functionLabel = e.f.label;
        }
        result += factory.instr("BL", functionLabel);
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