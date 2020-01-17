package frontend;

import java.util.*;

class FreeVarVisitor implements Visitor {

    List<String> env = new ArrayList<>();
    Hashtable<String, List<Id>> free = new Hashtable<>();
    Id currentFun = null;

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
        e.e1.accept(this);
        env.add(e.id.id);
        e.e2.accept(this);
    }

    public void visit(Var e) {
        if (currentFun != null && !env.contains(e.id.id) && !free.get(currentFun.id).contains(e.id)) {
            free.get(currentFun.id).add(e.id);
        }
    }

    public void visit(LetRec e) {
        Id parentFun = currentFun;
        currentFun = e.fd.id;
        env.add(currentFun.id);
        List<String> parentEnv = env;
        env = new ArrayList<String>();
        env.add(currentFun.id);
        for (Id id : e.fd.args) {
            env.add(id.id);
        }
        free.put(currentFun.id, new ArrayList<Id>());
        e.fd.e.accept(this);
        currentFun = parentFun;
        env = parentEnv;
        for (Id var : free.get(e.fd.id.id)) {
            if (currentFun != null && !env.contains(var.id) && !free.get(currentFun.id).contains(var)) {
                free.get(currentFun.id).add(var);
            }
        }
        e.e.accept(this);
    }

    public void visit(App e) {
        e.e.accept(this);
        for (Exp exp : e.es) {
            exp.accept(this);
        }
    }

    public void visit(Tuple e) {
        for (Exp exp : e.es) {
            exp.accept(this);
        }
    }

    public void visit(LetTuple e) {
        for (Id id : e.ids) {
            env.add(id.id);
        }
        e.e1.accept(this);
        e.e2.accept(this);
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
