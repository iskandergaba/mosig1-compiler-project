package frontend;

import java.util.*;

/**
 * Visitor used for constant folding
 */
public class ConstantFolder implements ObjVisitor<Exp> {

    private Hashtable<String, Integer> intVars = new Hashtable<>();
    private Hashtable<String, java.lang.Float> floatVars = new Hashtable<>();
    private boolean replaceVars = false;

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
        replaceVars = false;
        return new Neg(e.e.accept(this));
    }

    public Exp visit(Add e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        if (res1 instanceof Int) {
            if (res2 instanceof Int) {
                return new Int(((Int) res1).i + ((Int) res2).i);
            } else {
                return new Add(res2, res1);
            }
        } else if (res2 instanceof Int) {
            return new Add(res1, res2);
        } else {
            return e;
        }
    }

    public Exp visit(Sub e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        if (res1 instanceof Int) {
            if (res2 instanceof Int) {
                return new Int(((Int) res1).i - ((Int) res2).i);
            } else {
                return new Sub(res2, res1);
            }
        } else if (res2 instanceof Int) {
            return new Sub(res1, res2);
        } else {
            return e;
        }
    }

    public Exp visit(FNeg e) throws Exception {
        replaceVars = false;
        return new FNeg(e.e.accept(this));
    }

    public Exp visit(FAdd e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        if (res1 instanceof Float && res2 instanceof Float) {
            return new Float(((Float) res1).f + ((Float) res2).f);
        } else {
            return e;
        }
    }

    public Exp visit(FSub e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        if (res1 instanceof Float && res2 instanceof Float) {
            return new Float(((Float) res1).f - ((Float) res2).f);
        } else {
            return e;
        }
    }

    public Exp visit(FMul e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        if (res1 instanceof Float && res2 instanceof Float) {
            return new Float(((Float) res1).f * ((Float) res2).f);
        } else {
            return e;
        }
    }

    public Exp visit(FDiv e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        if (res1 instanceof Float && res2 instanceof Float) {
            return new Float(((Float) res1).f / ((Float) res2).f);
        } else {
            return e;
        }
    }

    public Exp visit(Eq e) throws Exception {
        replaceVars = true;
        Exp res = e.e2.accept(this);
        replaceVars = false;
        if (res instanceof Float) {
            res = e.e2.accept(this);
        }
        return new Eq(e.e1.accept(this), res);
    }

    public Exp visit(LE e) throws Exception {
        replaceVars = true;
        Exp res = e.e2.accept(this);
        replaceVars = false;
        if (res instanceof Float) {
            res = e.e2.accept(this);
        }
        return new LE(e.e1.accept(this), res);
    }

    public Exp visit(If e) throws Exception {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

    public Exp visit(Let e) throws Exception {
        replaceVars = true;
        Exp res1 = e.e1.accept(this);
        replaceVars = false;
        if (res1 instanceof Int) {
            intVars.put(e.id.id, ((Int) res1).i);
        } else if (res1 instanceof Float) {
            floatVars.put(e.id.id, ((Float) res1).f);
        }
        return new Let(e.id, e.t, res1, e.e2.accept(this));
    }

    public Exp visit(Var e) throws Exception {
        if (replaceVars && intVars.get(e.id.id) != null) {
            return new Int(intVars.get(e.id.id));
        } else if (replaceVars && floatVars.get(e.id.id) != null) {
            return new Float(floatVars.get(e.id.id));
        } else {
            return e;
        }
    }

    public Exp visit(LetRec e) throws Exception {
        return new LetRec(new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this)), e.e.accept(this));
    }

    public Exp visit(App e) throws Exception {
        replaceVars = false;
        List<Exp> args = new ArrayList<>();
        for (Exp arg : e.es) {
            args.add(arg.accept(this));
        }
        return new App(e.e, args);
    }

    public Exp visit(Tuple e) throws Exception {
        replaceVars = false;
        List<Exp> Exps = new ArrayList<>();
        for (Exp exp : e.es) {
            Exp res = exp.accept(this);
            Exps.add(res);
        }
        return new Tuple(Exps);
    }

    public Exp visit(LetTuple e) throws Exception {
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Array e) throws Exception {
        replaceVars = false;
        return new Array(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Get e) throws Exception {
        replaceVars = true;
        Exp res = e.e2.accept(this);
        replaceVars = false;
        return new Get(e.e1.accept(this), res);
    }

    public Exp visit(Put e) throws Exception {
        replaceVars = true;
        Exp res = e.e2.accept(this);
        replaceVars = false;
        return new Put(e.e1, res, e.e3.accept(this));
    }
}
