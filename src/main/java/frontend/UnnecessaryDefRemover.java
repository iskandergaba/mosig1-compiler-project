package frontend;

import java.util.*;

/**
 * Visitor used for removing unnecessary definitions
 */
public class UnnecessaryDefRemover implements ObjVisitor<Exp> {

    List<String> usedVars = new ArrayList<>();

    public Exp visit(Unit e) {
        return e;
    }

    public Exp visit(Bool e) {
        return e;
    }

    public Exp visit(Int e) {
        return e;
    }

    public Exp visit(Float e) {
        return e;
    }

    public Exp visit(Not e) throws Exception {
        e.e.accept(this);
        return e;
    }

    public Exp visit(Neg e) throws Exception {
        e.e.accept(this);
        return e;
    }

    public Exp visit(Add e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(Sub e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(FNeg e) throws Exception {
        e.e.accept(this);
        return e;
    }

    public Exp visit(FAdd e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(FSub e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(FMul e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(FDiv e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(Eq e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(LE e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(If e) throws Exception {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

    public Exp visit(Let e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        if (e.e1 instanceof App) {
            usedVars.add(e.id.id);
        }
        if (usedVars.contains(e.id.id)) {
            return new Let(e.id, e.t, res1, res2);
        } else {
            return res2;
        }
    }

    public Exp visit(Var e) throws Exception {
        if (!usedVars.contains(e.id.id)) {
            usedVars.add(e.id.id);
        }
        return e;
    }

    public Exp visit(LetRec e) throws Exception {
        return new LetRec(new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this)), e.e.accept(this));
    }

    public Exp visit(App e) throws Exception {
        e.e.accept(this);
        for (Exp arg : e.es) {
            arg.accept(this);
        }
        return e;
    }

    public Exp visit(Tuple e) throws Exception {
        for (Exp exp : e.es) {
            exp.accept(this);
        }
        return e;
    }

    public Exp visit(LetTuple e) throws Exception {
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Array e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(Get e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
    }

    public Exp visit(Put e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
        return e;
    }
}
