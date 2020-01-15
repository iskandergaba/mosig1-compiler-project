package frontend;

import java.util.*;

class CCVisitor implements ObjVisitor<Exp> {

    public List<FunDef> funs = new ArrayList<>();
    public Var apply = new Var(new Id("_apply_direct_"));
    public Var app_closure = new Var(new Id("_apply_closure_"));
    public Var mk_closure = new Var(new Id("_make_closure_"));

    public Exp join(Exp body) {
        Exp top = body;
        for (FunDef fun : funs) {
            top = new LetRec(fun, top);
        }
        return top;
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
        return new Not(e.e.accept(this));
    }

    public Exp visit(Neg e) throws Exception {
        return new Neg(e.e.accept(this));
    }

    public Exp visit(Add e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new Add(res1, res2);
    }

    public Exp visit(Sub e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new Sub(res1, res2);
    }

    public Exp visit(FNeg e) throws Exception {
        return new FNeg(e.e.accept(this));
    }

    public Exp visit(FAdd e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new FAdd(res1, res2);
    }

    public Exp visit(FSub e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new FSub(res1, res2);
    }

    public Exp visit(FMul e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new FMul(res1, res2);
    }

    public Exp visit(FDiv e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new FDiv(res1, res2);
    }

    public Exp visit(Eq e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new Eq(res1, res2);
    }

    public Exp visit(LE e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new LE(res1, res2);
    }

    public Exp visit(If e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        Exp res3 = e.e3.accept(this);
        return new If(res1, res2, res3);
    }

    public Exp visit(Let e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new Let(e.id, e.t, res1, res2);
    }

    public Exp visit(Var e) {
        return e;
    }

    public Exp visit(LetRec e) throws Exception {
        Exp res1 = e.fd.e.accept(this);
        Exp res2 = e.e.accept(this);
        funs.add(new FunDef(e.fd.id, e.fd.type, e.fd.args, res1));
        return res2;
    }

    public Exp visit(App e) throws Exception {
        List<Exp> args = e.es;
        args.add(0, e.e);
        return new App(apply, args);
    }

    public Exp visit(Tuple e) throws Exception {
        List<Exp> exp = new ArrayList<>();
        for (Exp e_ : e.es) {
            exp.add(e_.accept(this));
        }
        return new Tuple(exp);
    }

    public Exp visit(LetTuple e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new LetTuple(e.ids, e.ts, res1, res2);
    }

    public Exp visit(Array e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new Array(res1, res2);
    }

    public Exp visit(Get e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        return new Get(res1, res2);
    }

    public Exp visit(Put e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        Exp res3 = e.e3.accept(this);
        return new Put(res1, res2, res3);
    }
}
