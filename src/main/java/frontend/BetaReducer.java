package frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.type.Type;

/**
 * Visitor used for Beta Reduction
 */
public class BetaReducer implements ObjVisitor<Exp> {

    private Map<String, String> substitutions;

    public BetaReducer() {
        substitutions = new HashMap<>();
    }

    public BetaReducer(Map<String, String> substitutions) {
        this.substitutions = substitutions;
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
        Eq res = new Eq(e.e1.accept(this), e.e2.accept(this));
        res.t = e.t;
        return res;
    }

    public Exp visit(LE e) throws Exception {
        LE res = new LE(e.e1.accept(this), e.e2.accept(this));
        res.t = e.t;
        return res;
    }

    public Exp visit(If e) throws Exception {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

    public Exp visit(Let e) throws Exception {
        Exp e1 = e.e1.accept(this);
        if (e1 instanceof Var) {
            Var v = (Var) e.e1;
            Map<String, String> subs = new HashMap<>(substitutions);
            if (substitutions.containsKey(v.id.id)) {
                subs.put(e.id.id, substitutions.get(v.id.id));
            } else {
                subs.put(e.id.id, v.id.id);
            }
            if (!((Var) (e.e1)).id.id.startsWith("fun"))
                return e.e2.accept(new BetaReducer(subs));
            else
                return new Let(e.id, Type.gen(), e.e1, e.e2.accept(new BetaReducer(subs)));
        }
        return new Let(e.id, Type.gen(), e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Var e) throws Exception {
        if (substitutions.containsKey(e.id.id)) {
            return new Var(new Id(substitutions.get(e.id.id)));
        }
        return e;
    }

    public Exp visit(LetRec e) throws Exception {
        FunDef fun = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        return new LetRec(fun, e.e.accept(this));
    }

    public Exp visit(App e) throws Exception {
        List<Exp> es = new ArrayList<>();
        for (Exp exp : e.es) {
            Exp arg = exp.accept(this);
            if (arg instanceof Var && ((Var) arg).id.id.startsWith("fun")) {
                es.add(exp);
            } else {
                es.add(arg);
            }
        }
        return new App(e.e.accept(this), es);
    }

    public Exp visit(Tuple e) throws Exception {
        List<Exp> es = new ArrayList<>();
        for (Exp exp : e.es) {
            Exp arg = exp.accept(this);
            if (arg instanceof Var && ((Var) arg).id.id.startsWith("fun")) {
                es.add(exp);
            } else {
                es.add(arg);
            }
        }
        return new Tuple(es);
    }

    public Exp visit(LetTuple e) throws Exception {
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Array e) throws Exception {
        Exp init = e.e2.accept(this);
        if (init instanceof Var && ((Var) init).id.id.startsWith("fun")) {
            return new Array(e.e1.accept(this), e.e2);
        }
        return new Array(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Get e) throws Exception {
        return new Get(e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Put e) throws Exception {
        return new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }
}
