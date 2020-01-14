package frontend;

import java.util.*;

class AlphaConversionVisitor implements Visitor {

    public Hashtable<String, String> changes;
    public static int varCount = 0;
    public static int funCount = 0;
    public static int argCount = 0;

    public AlphaConversionVisitor() {
        changes = new Hashtable<String, String>();
    }

    public AlphaConversionVisitor(Hashtable<String, String> changes) {
        this.changes = changes;
    }

    public void visit(Unit e) {
    }

    public void visit(Bool e) {
    }

    public void visit(Int e) {
    }

    public void visit(Float e) {
    }

    public void visit(Not e) {
        e.e.accept(this);
    }

    public void visit(Neg e) {
        e.e.accept(this);
    }

    public void visit(Add e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(Sub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(FNeg e) {
        e.e.accept(this);
    }

    public void visit(FAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(FSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(FMul e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(FDiv e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(Eq e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(LE e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(If e) {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
    }

    public void visit(Let e) {
        Hashtable<String, String> newChanges = (Hashtable<String, String>) changes.clone();
        newChanges.put(e.id.id, "var" + varCount);
        e.id.id = "var" + varCount;
        varCount++;
        AlphaConversionVisitor v = new AlphaConversionVisitor(newChanges);
        e.e1.accept(v);
        e.e2.accept(v);
    }

    public void visit(Var e) {
        String name = changes.get(e.id.id);
        if (name != null)
            e.id.id = name;
    }

    public void visit(LetRec e) {
        Hashtable<String, String> newChanges = (Hashtable<String, String>) changes.clone();
        newChanges.put(e.fd.id.id, "fun" + funCount);
        e.fd.id.id = "fun" + funCount;
        funCount++;
        Hashtable<String, String> newChangesFun = (Hashtable<String, String>) newChanges.clone();
        for (Id arg : e.fd.args) {
            newChangesFun.put(arg.id, "arg" + argCount);
            arg.id = "arg" + argCount;
            argCount++;
        }
        e.fd.e.accept(new AlphaConversionVisitor(newChangesFun));
        e.e.accept(new AlphaConversionVisitor(newChanges));
    }

    public void visit(App e) {
        e.e.accept(this);
        for (Exp e_ : e.es) {
            e_.accept(this);
        }
    }

    public void visit(Tuple e) {
        for (Exp e_ : e.es) {
            e_.accept(this);
        }
    }

    public void visit(LetTuple e) {
        Hashtable<String, String> newChanges = (Hashtable<String, String>) changes.clone();
        for (Id v : e.ids) {
            newChanges.put(v.id, "var" + varCount);
            v.id = "var" + varCount;
            varCount++;
        }
        e.e1.accept(this);
        e.e2.accept(new AlphaConversionVisitor(newChanges));
    }

    public void visit(Array e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(Get e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(Put e) {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
    }
}
