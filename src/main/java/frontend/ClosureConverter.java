package frontend;

import java.util.*;

/**
 * Visitor used for closure conversion
 */
public class ClosureConverter implements ObjVisitor<Exp> {

    private Hashtable<String, List<Id>> free;
    private List<String> retClosure = new ArrayList<>();
    private List<String> isClosure = new ArrayList<>();
    private List<String> directFuns = new ArrayList<>();

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
        if (directFuns.contains(e.id.id)) {
            e.id.id = "_" + e.id.id;
        }
        e.retClosureFlag = e.id.retClosureFlag;
        if (retClosure.contains(e.id.id)) {
            e.retClosureFlag = true;
        }
        if (isClosure.contains(e.id.id)) {
            e.isClosureFlag = true;
        }
        return e;
    }

    public Exp visit(LetRec e) throws Exception {
        if(free.get(e.fd.id.id).size()==0){
            directFuns.add(e.fd.id.id);
        }
        Exp res1 = e.fd.e.accept(this);
        if (res1.retClosureFlag) {
            retClosure.add(e.fd.id.id);
        }
        Exp res2 = e.e.accept(this);
        Id label = Id.gen();
        label.id = "_" + e.fd.id.id;
        FunDef fun = new FunDef(label, e.fd.type, e.fd.args, res1);
        fun.free = free.get(e.fd.id.id);
        funs.add(fun);
        if (fun.free.size() > 0) {
            List<Exp> args = new ArrayList<>();
            args.add(new Var(fun.id));
            for (Id id : fun.free) {
                args.add(new Var(id));
            }
            App app = new App(mk_closure, args);
            app.isClosureFlag = true;
            res2 = new Let(e.fd.id, fun.type, app, res2);
            res2.retClosureFlag = true;
            e.fd.id.retClosureFlag = true;
        }
        return res2;
    }

    public Exp visit(App e) throws Exception {
        List<Exp> args = new ArrayList<>();
        for (Exp exp : e.es) {
            args.add(exp.accept(this));
        }
        Exp exp = e.e.accept(this);
        if (exp.isClosureFlag) {
            args.add(0, exp);
            return new App(app_closure, args);
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
