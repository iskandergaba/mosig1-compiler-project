package frontend;

import java.util.*;

/**
 * Visitor used for closure conversion
 */
public class ClosureConverter implements ObjVisitor<Exp> {

    private Hashtable<String, List<Id>> free;
    private List<String> retClosure = new ArrayList<>();
    private List<String> isClosure = new ArrayList<>();

    private String currentFun;

    /**
     * Function definitions generated by the visitor
     */
    public List<FunDef> funs = new ArrayList<>();

    private Var apply = new Var(new Id("_apply_direct_"));
    private Var app_closure = new Var(new Id("_apply_closure_"));
    private Var mk_closure = new Var(new Id("_make_closure_"));

    public ClosureConverter(FreeVarVisitor v) {
        free = v.free;
    }

    /**
     * Adds the generated function definitions as let rec statements at the
     * beginning of body
     * 
     * @param body an Exp
     * @return body with the previously generated function definitions as let rec
     *         statements added at the beginning
     */
    public Exp join(Exp body) {
        Exp top = body;
        for (int i = funs.size() - 1; i >= 0; i--) {
            top = new LetRec(funs.get(i), top);
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
        Eq exp = new Eq(res1, res2);
        exp.t = e.t;
        return exp;
    }

    public Exp visit(LE e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        LE exp = new LE(res1, res2);
        exp.t = e.t;
        return exp;
    }

    public Exp visit(If e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        Exp res3 = e.e3.accept(this);
        return new If(res1, res2, res3);
    }

    public Exp visit(Let e) throws Exception {
        Exp res1 = e.e1.accept(this);
        if (res1.retClosureFlag) {
            retClosure.add(e.id.id);
        }
        if (res1.isClosureFlag) {
            isClosure.add(e.id.id);
        }
        Exp res2 = e.e2.accept(this);
        Let l = new Let(e.id, e.t, res1, res2);
        l.retClosureFlag = res2.retClosureFlag;
        l.isClosureFlag = res2.isClosureFlag;
        return l;
    }

    public Exp visit(Var e) {
        if (e.id.id.equals(currentFun)) {
            e.id.id += "_self_clos";
        }
        if (retClosure.contains(e.id.id)) {
            e.retClosureFlag = true;
        }
        if (isClosure.contains(e.id.id)) {
            e.isClosureFlag = true;
        }
        /*
         * if (directFuns.contains(e.id.id)) { e.id.id = "_" + e.id.id; }
         */
        return e;
    }

    public Exp visit(LetRec e) throws Exception {
        String oldFun = currentFun;
        String clos = e.fd.id.id + "_self_clos";
        currentFun = e.fd.id.id;
        if (free.get(e.fd.id.id).size() == 0) {
            // directFuns.add(e.fd.id.id);
        }
        isClosure.add(clos);
        isClosure.add(e.fd.id.id);
        Exp res1 = e.fd.e.accept(this);
        currentFun = oldFun;
        if (res1.isClosureFlag || res1.retClosureFlag) {
            retClosure.add(e.fd.id.id);
        }
        Id label = Id.gen();
        label.id = "_" + e.fd.id.id;
        List<Id> args_ = e.fd.args;
        args_.add(new Id(clos));
        FunDef fun = new FunDef(label, e.fd.type, args_, res1);
        fun.free = free.get(e.fd.id.id);
        funs.add(fun);
        List<Exp> args = new ArrayList<>();
        args.add(new Var(fun.id));
        for (Id id : fun.free) {
            if (id.id.equals(currentFun)) {
                id.id += "_self_clos";
            }
            args.add(new Var(id));
        }
        App app = new App(mk_closure, args);
        app.isClosureFlag = true;
        isClosure.add(e.fd.id.id);
        Exp res2 = e.e.accept(this);
        Exp closure = new Let(e.fd.id, fun.type, app, res2);
        closure.isClosureFlag = res2.isClosureFlag;
        closure.retClosureFlag = res2.retClosureFlag;
        return closure;
    }

    public Exp visit(App e) throws Exception {
        List<Exp> args = new ArrayList<>();
        for (Exp exp : e.es) {
            args.add(exp.accept(this));
        }
        if (e.e instanceof Var && ((Var) e.e).id.id.equals(currentFun)) {
            ((Var) e.e).id.id = ((Var) e.e).id.id + "_self_clos";
        }
        Exp exp = e.e.accept(this);
        args.add(0, exp);
        if (exp.isClosureFlag || (exp instanceof Var && ((Var) exp).id.id.startsWith("arg"))) {
            args.add(exp);
            App a = new App(app_closure, args);
            if (exp.retClosureFlag) {
                a.isClosureFlag = true;
            }
            return a;
        }
        App a = new App(apply, args);
        if (exp.retClosureFlag) {
            a.isClosureFlag = true;
        }
        return a;
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
        if (res1 instanceof Tuple) {
            int i = 0;
            for (Exp exp : ((Tuple) res1).es) {
                if (exp.retClosureFlag) {
                    retClosure.add(e.ids.get(i).id);
                }
                if (exp.isClosureFlag) {
                    isClosure.add(e.ids.get(i).id);
                }
                i++;
            }
        }
        Exp res2 = e.e2.accept(this);
        return new LetTuple(e.ids, e.ts, res1, res2);
    }

    public Exp visit(Array e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        Array a = new Array(res1, res2);
        a.isClosureFlag = res2.isClosureFlag;
        a.retClosureFlag = res2.retClosureFlag;
        return a;
    }

    public Exp visit(Get e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        Get g = new Get(res1, res2);
        if (res1 instanceof Var && isClosure.contains(((Var) res1).id.id)) {
            g.isClosureFlag = true;
        }
        if (res1 instanceof Var && retClosure.contains(((Var) res1).id.id)) {
            g.retClosureFlag = true;
        }
        return g;
    }

    public Exp visit(Put e) throws Exception {
        Exp res1 = e.e1.accept(this);
        Exp res2 = e.e2.accept(this);
        Exp res3 = e.e3.accept(this);
        return new Put(res1, res2, res3);
    }
}
