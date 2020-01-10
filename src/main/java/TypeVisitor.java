import java.util.*;

class TypeVisitor implements ObjVisitor<Type> {

    public Hashtable<String, Type> env = new Hashtable<>();

    public TypeVisitor(){
        TFun t=new TFun();
        t.extern=true;
        t.extern_args=new ArrayList<Type>();
        t.extern_args.add(new TInt());
        t.extern_ret=new TUnit();
        env.put("print_int",t);
    }

    public Type visit(Unit e) {
        return new TUnit();
    }

    public Type visit(Bool e) {
        return new TBool();
    }

    public Type visit(Int e) {
        return new TInt();
    }

    public Type visit(Float e) { 
        return new TFloat();
    }

    public Type visit(Not e) {
        Type res = e.e.accept(this);
        return res instanceof TBool || res instanceof TAssumeOK ? new TBool() : null;
    }

    public Type visit(Neg e) {
        Type res = e.e.accept(this);
        return res instanceof TInt  || res instanceof TAssumeOK ? new TInt() : null;
    }

    public Type visit(Add e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return (res1 instanceof TInt || res1 instanceof TAssumeOK) && (res2 instanceof TInt || res2 instanceof TAssumeOK) ? new TInt() : null;
    }

    public Type visit(Sub e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return (res1 instanceof TInt || res1 instanceof TAssumeOK) && (res2 instanceof TInt || res2 instanceof TAssumeOK) ? new TInt() : null;
   }

    public Type visit(FNeg e){
        Type res = e.e.accept(this);
        return res instanceof TFloat || res instanceof TAssumeOK ? new TFloat() : null;
    }

    public Type visit(FAdd e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return (res1 instanceof TFloat || res1 instanceof TAssumeOK) && (res2 instanceof TFloat || res2 instanceof TAssumeOK) ? new TFloat() : null;
    }

    public Type visit(FSub e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return (res1 instanceof TFloat || res1 instanceof TAssumeOK) && (res2 instanceof TFloat || res2 instanceof TAssumeOK) ? new TFloat() : null;
    }

    public Type visit(FMul e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return (res1 instanceof TFloat || res1 instanceof TAssumeOK) && (res2 instanceof TFloat || res2 instanceof TAssumeOK) ? new TFloat() : null;
     }

    public Type visit(FDiv e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return (res1 instanceof TFloat || res1 instanceof TAssumeOK) && (res2 instanceof TFloat || res2 instanceof TAssumeOK) ? new TFloat() : null;
    }

    public Type visit(Eq e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1.getClass().getName().equals(res2.getClass().getName()) || res1 instanceof TAssumeOK || res2 instanceof TAssumeOK ? new TBool() : null;
    }

    public Type visit(LE e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return ((res1 instanceof TInt || res1 instanceof TAssumeOK) && (res2 instanceof TInt || res2 instanceof TAssumeOK))
            || ((res1 instanceof TFloat || res1 instanceof TAssumeOK) && (res2 instanceof TFloat || res2 instanceof TAssumeOK))
                ? new TBool()
                : null;
    }

    public Type visit(If e){
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        Type res3 = e.e3.accept(this);
        return res1 instanceof TBool && res2!=null && res3!=null 
            && (res2.getClass().getName().equals(res3.getClass().getName()) || res2 instanceof TAssumeOK || res3 instanceof TAssumeOK)
            ? res2
            : null;
    }

    public Type visit(Let e) {
        Type res1 = e.e1.accept(this);
        if(res1!=null){
            env.put(e.id.id, res1);
        }
        Type res2 = e.e2.accept(this);
        if (res2!=null) {
            return res2;
        }
        System.out.println("let : "+e.id.id);
        return null;
    }

    public Type visit(Var e){
        return env.get(e.id.id);
    }

    // TODO
    public Type visit(LetRec e){
        //Type res1 = e.fd.e.accept(this);
        TFun t=new TFun();
        t.body=e.fd.e;
        t.args=e.fd.args;
        env.put(e.fd.id.id, t);
        Type res2 = e.e.accept(this);
        if (res2 != null) {
            return res2;
        }
        System.out.println("letrec : "+e.fd.id.id);
        return null;
    }

    public Type visit(App e) {
        Type res = e.e.accept(this);
        if (res instanceof TFun) {
            if(((TFun)res).extern){
                int i=0;
                for(Exp exp : e.es){
                    Type res_=exp.accept(this);
                    if(res_==null || !((res_ instanceof TAssumeOK) || res_.getClass().getName().equals(((TFun)res).extern_args.get(i).getClass().getName()))){
                        System.out.print("app bad arg : ");
                        e.e.accept(new PrintVisitor());
                        System.out.println("");
                        return null;
                    }
                    i++;
                }
                return ((TFun)res).extern_ret;
            }
            int i=0;
            for (Exp exp : e.es) {
                Type res_ = exp.accept(this);
                if (res_ == null) {
                    System.out.print("app bad arg : ");
                    e.e.accept(new PrintVisitor());
                    System.out.println("");
                    return null;
                }
                env.put(((TFun)res).args.get(i).id,res_);
                i++;
            }
            Type res__;
            if(((TFun)res).rec_calls<2){
                ((TFun)res).rec_calls++;
                res__=((TFun)res).body.accept(this);
                ((TFun)res).rec_calls--;
                if(res__==null){
                    System.out.print("bad call : ");
                    e.e.accept(new PrintVisitor());
                    System.out.println("");
                }
            } else {
                res__=new TAssumeOK();
            }
            return res__!=null ? res__ : null;
        }
        System.out.print("app not func : ");
        e.e.accept(new PrintVisitor());
        System.out.println("");
        return null;
    }

    public Type visit(Tuple e) {
        List<Type> types = new ArrayList<>();
        for (Exp exp : e.es) {
            Type res = exp.accept(this);
            if (res == null) {
                return null;
            }
            types.add(res);
        }
        return new TTuple(types);
    }

    public Type visit(LetTuple e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if (res1 instanceof TTuple && res2 instanceof TUnit) {
            for (int i = 0; i < e.ids.size(); i++) {
                env.put(e.ids.get(i).id, e.ts.get(i));
            }
            return new TUnit();
        }
        return null;
    }

    public Type visit(Array e){
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1 instanceof TInt ? new TArray(res2) : null;
    }

    public Type visit(Get e){
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1 instanceof TArray && res2 instanceof TInt ? ((TArray)res1).type : null;
    }

    public Type visit(Put e){
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        Type res3 = e.e3.accept(this);
        return res1 instanceof TArray 
            && res2 instanceof TInt
            && res3.getClass().equals(((TArray)res1).type.getClass()) 
                ? new TUnit()
                : null;
    }
}
