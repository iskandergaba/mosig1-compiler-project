package frontend;

import java.util.*;

/**
 * Visitor used for scope checking
 */
public class ScopeVisitor implements ObjVisitor<Void> {

    private static final String[] STD_FUNS = { "print_int", "print_newline", "truncate", "int_of_float", "float_of_int",
            "sin", "cos", "sqrt", "abs_float" };
    private List<String> env = new ArrayList<>();

    public ScopeVisitor() {
    }

    public ScopeVisitor(List<String> env) {
        this.env = env;
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
        List<String> env_ = new ArrayList<String>(env);
        env_.add(e.id.id);
        e.e1.accept(this);
        e.e2.accept(new ScopeVisitor(env_));
        return null;
    }

    public Void visit(Var e) throws Exception {
        if (!env.contains(e.id.id) && !isStandard(e.id.id))
            throw new EnvironmentException("VAR error : " + e.id.id + " is undeclared in this scope");
        return null;
    }

    public Void visit(LetRec e) throws Exception {
        env.add(e.fd.id.id);
        List<String> env_ = new ArrayList<String>(env);
        for(Id id : e.fd.args){
            env_.add(id.id);
        }
        e.e.accept(this);
        e.fd.e.accept(new ScopeVisitor(env_));
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
        List<String> env_ = new ArrayList<String>(env);
        for (Id id : e.ids) {
            env_.add(id.id);
        }
        e.e1.accept(this);
        e.e2.accept(new ScopeVisitor(env_));
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

    private boolean isStandard(String name) {
        for (String s : STD_FUNS) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
