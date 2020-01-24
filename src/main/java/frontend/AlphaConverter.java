package frontend;

import java.util.*;

/**
 * Visitor used for alpha conversion (also checks scope)
 */
public class AlphaConverter implements Visitor {

    private Hashtable<String, String> changes;
    private static int varCount = 0;
    private static int funCount = 0;
    private static int argCount = 0;

    public AlphaConverter() {
        changes = new Hashtable<String, String>();
    }

    public AlphaConverter(Hashtable<String, String> changes) {
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
        Hashtable<String, String> newChanges = new Hashtable<String, String>(changes);
        newChanges.put(e.id.id, "var" + varCount);
        e.id.id = "var" + varCount;
        varCount++;
        AlphaConverter v = new AlphaConverter(newChanges);
        e.e1.accept(this);
        e.e2.accept(v);
    }

    public void visit(Var e) {
        String name = changes.get(e.id.id);
        if (name != null)
            e.id.id = name;
    }

    public void visit(LetRec e) {
        Hashtable<String, String> newChanges = new Hashtable<String, String>(changes);
        newChanges.put(e.fd.id.id, "fun" + funCount);
        e.fd.id.id = "fun" + funCount;
        funCount++;
        Hashtable<String, String> newChangesFun = new Hashtable<String, String>(newChanges);
        for (Id arg : e.fd.args) {
            newChangesFun.put(arg.id, "arg" + argCount);
            arg.id = "arg" + argCount;
            argCount++;
        }
        e.fd.e.accept(new AlphaConverter(newChangesFun));
        e.e.accept(new AlphaConverter(newChanges));
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
        Hashtable<String, String> newChanges = new Hashtable<String, String>(changes);
        for (Id v : e.ids) {
            newChanges.put(v.id, "var" + varCount);
            v.id = "var" + varCount;
            varCount++;
        }
        e.e1.accept(this);
        e.e2.accept(new AlphaConverter(newChanges));
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
