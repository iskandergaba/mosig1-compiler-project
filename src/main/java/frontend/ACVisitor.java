package frontend;

import java.util.*;

/**
 * Visitor used for alpha conversion (also checks scope)
 */
public class ACVisitor implements ObjVisitor<Void> {

    private Hashtable<String, String> changes;
    private static int varCount = 0;
    private static int funCount = 0;
    private static int argCount = 0;
    private static String[] standardFuns = { "print_int", "print_newline", "truncate", "int_of_float", "float_of_int",
            "sin", "cos", "sqrt", "abs_float" };

    public ACVisitor() {
        changes = new Hashtable<String, String>();
    }

    public ACVisitor(Hashtable<String, String> changes) {
        this.changes = changes;
    }

    public Void visit(Unit e) {
        return null;
    }

    public Void visit(Bool e) {
        return null;
    }

    public Void visit(Int e) {
        return null;
    }

    public Void visit(Float e) {
        return null;
    }

    public Void visit(Not e) throws Exception {
        e.e.accept(this);
        return null;
    }

    public Void visit(Neg e) throws Exception {
        e.e.accept(this);
        return null;
    }

    public Void visit(Add e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(Sub e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(FNeg e) throws Exception {
        e.e.accept(this);
        return null;
    }

    public Void visit(FAdd e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(FSub e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(FMul e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(FDiv e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(Eq e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(LE e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(If e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
        return null;
    }

    public Void visit(Let e) throws Exception {
        Hashtable<String, String> newChanges = new Hashtable<String, String>(changes);
        newChanges.put(e.id.id, "var" + varCount);
        e.id.old = e.id.id;
        e.id.id = "var" + varCount;
        varCount++;
        ACVisitor v = new ACVisitor(newChanges);
        e.e1.accept(this);
        e.e2.accept(v);
        return null;
    }

    private boolean isStandard(String name) {
        for (String s : standardFuns) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Void visit(Var e) throws Exception {
        String name = changes.get(e.id.id);
        if (name != null)
            e.id.id = name;
        else if (e.id.old == null && !isStandard(e.id.id))
            throw new EnvironmentException("VAR error : " + e.id.id + " is undeclared in this scope");
        return null;
    }

    public Void visit(LetRec e) throws Exception {
        Hashtable<String, String> newChanges = new Hashtable<String, String>(changes);
        newChanges.put(e.fd.id.id, "fun" + funCount);
        e.fd.id.old = e.fd.id.id;
        e.fd.id.id = "fun" + funCount;
        funCount++;
        Hashtable<String, String> newChangesFun = new Hashtable<String, String>(newChanges);
        for (Id arg : e.fd.args) {
            newChangesFun.put(arg.id, "arg" + argCount);
            arg.old = arg.id;
            arg.id = "arg" + argCount;
            argCount++;
        }
        e.fd.e.accept(new ACVisitor(newChangesFun));
        e.e.accept(new ACVisitor(newChanges));
        return null;
    }

    public Void visit(App e) throws Exception {
        e.e.accept(this);
        for (Exp e_ : e.es) {
            e_.accept(this);
        }
        return null;
    }

    public Void visit(Tuple e) throws Exception {
        for (Exp e_ : e.es) {
            e_.accept(this);
        }
        return null;
    }

    public Void visit(LetTuple e) throws Exception {
        Hashtable<String, String> newChanges = new Hashtable<String, String>(changes);
        for (Id v : e.ids) {
            newChanges.put(v.id, "var" + varCount);
            v.id = "var" + varCount;
            varCount++;
        }
        e.e1.accept(this);
        e.e2.accept(new ACVisitor(newChanges));
        return null;
    }

    public Void visit(Array e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(Get e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return null;
    }

    public Void visit(Put e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
        return null;
    }
}
