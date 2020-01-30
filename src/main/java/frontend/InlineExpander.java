package frontend;

import java.util.*;

/**
 * Visitor used for Inline Expansion
 */
public class InlineExpander implements ObjVisitor<Exp> {

    private int threshold;
    private static Hashtable<String, FunDef> inlineFuncs = new Hashtable<String, FunDef>();;

    public InlineExpander(int t) {
        threshold = t;
    }

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
        if (e.e1 instanceof App) {
            App a = (App) e.e1;
            if (a.e instanceof Var) {
                FunDef fd = inlineFuncs.get(((Var) a.e).id.id);
                if (fd != null) {
                    Exp res;
                    if (fd.e instanceof Let || fd.e instanceof LetTuple) {
                        InlineInsertionVisitor v = new InlineInsertionVisitor(e.id, e.e2.accept(this));
                        res = fd.e.accept(v);
                    } else {
                        res = new Let(new Id(e.id.id), e.t, fd.e, e.e2.accept(this));
                    }
                    int i = 0;
                    for (Id id : fd.args) {
                        res = new Let(new Id(id.id), common.type.Type.gen(), a.es.get(i), res);
                        i++;
                    }
                    return res;
                }
            }
        }
        return new Let(e.id, e.t, e.e1, e.e2.accept(this));
    }

    public Exp visit(Var e) throws Exception {
        return e;
    }

    public Exp visit(LetRec e) throws Exception {
        Exp res = e.fd.e.accept(this);
        int size = res.accept(new InlineHeightVisitor());
        FunDef fun = new FunDef(e.fd.id, e.fd.type, e.fd.args, res);
        if (size <= threshold) {
            inlineFuncs.put(e.fd.id.toString(), fun);
        }
        return new LetRec(fun, e.e.accept(this));
    }

    public Exp visit(App e) throws Exception {
        if (e.e instanceof Var) {
            FunDef fd = inlineFuncs.get(((Var) e.e).id.id);
            if (fd != null) {
                Id res = Id.gen();
                InlineInsertionVisitor v = new InlineInsertionVisitor(res, new Var(res));
                Exp insert = fd.e.accept(v);
                int i = 0;
                for (Id id : fd.args) {
                    insert = new Let(new Id(id.id), common.type.Type.gen(), e.es.get(i), insert);
                    i++;
                }
                return insert;
            } else {
                return e;
            }
        } else {
            return e;
        }
    }

    public Exp visit(Tuple e) throws Exception {
        return e;
    }

    public Exp visit(LetTuple e) throws Exception {
        e.e1.accept(this);
        e.e2.accept(this);
        return e;
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
