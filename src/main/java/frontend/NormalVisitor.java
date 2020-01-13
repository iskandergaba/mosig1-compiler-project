package frontend;

import java.util.ArrayList;
import java.util.List;

class NormalVisitor implements ObjVisitor<Exp> {

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
        if(!(e.e instanceof Var)) {
            Var v1 = Var.gen();
            Exp bExp = new Not(e);
            return new Let(v1.id, Type.gen(), e.e.accept(this), bExp);
        }
        return e;
    }

    public Exp visit(Neg e) throws Exception {
        if(!(e.e instanceof Var)) {
            Var v1 = Var.gen();
            Exp aExp = new Neg(e);
            return new Let(v1.id, Type.gen(), e.e.accept(this), aExp);
        }
        return e;
    }

    public Exp visit(Add e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp aExp = new Add(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, aExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(Sub e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp aExp = new Sub(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, aExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(FNeg e) throws Exception {
        if(!(e.e instanceof Var)) {
            Var v1 = Var.gen();
            Exp aExp = new FNeg(e);
            return new Let(v1.id, Type.gen(), e.e.accept(this), aExp);
        }
        return e;
    }

    public Exp visit(FAdd e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp aExp = new FAdd(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, aExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(FSub e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp aExp = new FSub(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, aExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(FMul e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp aExp = new FMul(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, aExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(FDiv e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp aExp = new FDiv(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, aExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(Eq e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp bExp = new Eq(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, bExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(LE e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp bExp = new LE(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, bExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(If e) throws Exception {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

    public Exp visit(Let e) throws Exception {
        return new Let(e.id, e.t, e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Var e) throws Exception {
        return e;
    }

    public Exp visit(LetRec e) throws Exception {
        Exp exp = e.e.accept(this);
        FunDef fd = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        return new LetRec(fd, exp);
    }

    public Exp visit(App e) throws Exception {
        boolean normalize = false;
        for (Exp exp : e.es) {
            if (!(exp instanceof Var)) {
                normalize = true;
                break;
            }
        }

        //TODO
        /*if (normalize) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp exp = new LE(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, exp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;*/

        Exp applyExp = e.e.accept(this);
        List<Exp> exps = new ArrayList<>();
        for (Exp exp : e.es) {
            exps.add(exp.accept(this));
        }
        return new App(applyExp, exps);
    }

    public Exp visit(Tuple e) throws Exception {
        List<Exp> es = new ArrayList<>();
        for (Exp exp : e.es) {
            es.add(exp.accept(this));
        }
        return new Tuple(es);
    }

    public Exp visit(LetTuple e) throws Exception {
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
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
