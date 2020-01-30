package frontend;

import java.util.*;

/**
 * Visitor used for Inline Expansion : inserts function body
 */
public class InlineInsertionVisitor implements ObjVisitor<Exp> {

    private Exp next;
    private Id ret;

    public InlineInsertionVisitor(Id v, Exp e) {
        next = e;
        ret = v;
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
        return e;
    }

    public Exp visit(Neg e) throws Exception {
        return new Neg(e.e.accept(this));
    }

    public Exp visit(Add e) throws Exception {
        return new Add(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Sub e) throws Exception {
        return new Sub(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(FNeg e) throws Exception {
        return new FNeg(e.e.accept(this));
    }

    public Exp visit(FAdd e) throws Exception {
        return new FAdd(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(FSub e) throws Exception {
        return new FSub(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(FMul e) throws Exception {
        return new FMul(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(FDiv e) throws Exception {
        return new FDiv(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Eq e) throws Exception {
        return e;
    }

    public Exp visit(LE e) throws Exception {
        return e;
    }

    public Exp visit(If e) throws Exception {
        return e;
    }

    public Exp visit(Let e) throws Exception {
        if (e.e2 instanceof Let || e.e2 instanceof LetTuple) {
            return new Let(new Id(e.id.id), e.t, e.e1.accept(this), e.e2.accept(this));
        } else {
            Let l = new Let(ret, e.t, e.e2.accept(this), next);
            return new Let(new Id(e.id.id), e.t, e.e1.accept(this), l);
        }
    }

    public Exp visit(Var e) throws Exception {
        return new Var(new Id(e.id.id));
    }

    public Exp visit(LetRec e) throws Exception {
        return e;
    }

    public Exp visit(App e) throws Exception {
        return e;
    }

    public Exp visit(Tuple e) throws Exception {
        List<Exp> exps = new ArrayList<>();
        for (Exp exp : e.es) {
            exps.add(exp.accept(this));
        }
        return new Tuple(exps);
    }

    public Exp visit(LetTuple e) throws Exception {
        if (e.e2 instanceof Let || e.e2 instanceof LetTuple) {
            List<Id> ids = new ArrayList<>();
            for (Id id : e.ids) {
                ids.add(new Id(id.id));
            }
            return new LetTuple(ids, e.ts, e.e1.accept(this), e.e2.accept(this));
        } else {
            Let l = new Let(ret, common.type.Type.gen(), e.e2.accept(this), next);
            List<Id> ids = new ArrayList<>();
            for (Id id : e.ids) {
                ids.add(new Id(id.id));
            }
            return new LetTuple(ids, e.ts, e.e1.accept(this), l);
        }
    }

    public Exp visit(Array e) throws Exception {
        return new Array(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Get e) throws Exception {
        return new Get(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Put e) throws Exception {
        return new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }
}
