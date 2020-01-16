package frontend;

import java.util.ArrayList;
import java.util.List;

class KNVisitor implements ObjVisitor<Exp> {

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
        if (!(e.e instanceof Var)) {
            Var v = Var.gen();
            Exp bExp = new Not(v);
            return new Let(v.id, Type.gen(), e.e.accept(this), bExp);
        }
        return e;
    }

    public Exp visit(Neg e) throws Exception {
        if (!(e.e instanceof Var)) {
            Var v = Var.gen();
            Exp aExp = new Neg(v);
            return new Let(v.id, Type.gen(), e.e.accept(this), aExp);
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
        if (!(e.e instanceof Var)) {
            Var v = Var.gen();
            Exp aExp = new FNeg(v);
            return new Let(v.id, Type.gen(), e.e.accept(this), aExp);
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
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var) || !(e.e3 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Var v3 = Var.gen();
            Exp exp = new If(v1, v2, v3);
            Let let2 = new Let(v3.id, Type.gen(), e.e3, exp);
            Let let1 = new Let(v2.id, Type.gen(), e.e2, let2);
            Exp e1 = e.e1.accept(this);
            Exp e2 = let1.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
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
        if(!(e.e instanceof Var)) {
            normalize = true;
        }
        for (Exp exp : e.es) {
            if (normalize) {
                break;
            }
            if (!(exp instanceof Var)) {
                normalize = true;
            }
        }
        if (normalize) {
            Exp applyExp = e.e.accept(this);
            List<Exp> vars = new ArrayList<>();
            Var fun = Var.gen();
            for (int i = 0; i < e.es.size(); i++) {
                vars.add(Var.gen());
            }

            Exp exp = new App(applyExp, vars);
            exp = new Let(fun.id, Type.gen(), e.e.accept(this), new App(fun, vars));

            for (int i = e.es.size() - 1; i >= 0; i--) {
                exp = new Let(((Var) (vars.get(i))).id, Type.gen(), e.es.get(i).accept(this), exp);
            }
            return exp;
        }
        return e;
    }

    public Exp visit(Tuple e) throws Exception {
        boolean normalize = false;
        for (Exp exp : e.es) {
            if (!(exp instanceof Var)) {
                normalize = true;
                break;
            }
        }
        if (normalize) {
            List<Exp> vars = new ArrayList<>();
            for (int i = 0; i < e.es.size(); i++) {
                vars.add(Var.gen());
            }

            Exp exp = new Tuple(vars);

            for (int i = e.es.size() - 1; i >= 0; i--) {
                exp = new Let(((Var) (vars.get(i))).id, Type.gen(), e.es.get(i).accept(this), exp);
            }
            return exp;
        }
        return e;
    }

    public Exp visit(LetTuple e) throws Exception {
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Array e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp bExp = new Array(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, bExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(Get e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Exp bExp = new Get(v1, v2);
            Let letExp = new Let(v2.id, Type.gen(), e.e2, bExp);
            Exp e1 = e.e1.accept(this);
            Exp e2 = letExp.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }

    public Exp visit(Put e) throws Exception {
        if (!(e.e1 instanceof Var) || !(e.e2 instanceof Var) || !(e.e3 instanceof Var)) {
            Var v1 = Var.gen();
            Var v2 = Var.gen();
            Var v3 = Var.gen();
            Exp exp = new Put(v1, v2, v3);
            Let let2 = new Let(v3.id, Type.gen(), e.e3, exp);
            Let let1 = new Let(v2.id, Type.gen(), e.e2, let2);
            Exp e1 = e.e1.accept(this);
            Exp e2 = let1.accept(this);
            return new Let(v1.id, Type.gen(), e1, e2);
        }
        return e;
    }
}
