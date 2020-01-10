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

        TFun t2=new TFun();
        t2.extern=true;
        t2.extern_args=new ArrayList<Type>();
        t2.extern_args.add(new TUnit());
        t2.extern_ret=new TUnit();
        env.put("print_newline",t2);

        TFun t3=new TFun();
        t3.extern=true;
        t3.extern_args=new ArrayList<Type>();
        t3.extern_args.add(new TFloat());
        t3.extern_ret=new TInt();
        env.put("truncate",t3);

        TFun t4=new TFun();
        t4.extern=true;
        t4.extern_args=new ArrayList<Type>();
        t4.extern_args.add(new TFloat());
        t4.extern_ret=new TInt();
        env.put("int_of_float",t4);

        TFun t5=new TFun();
        t5.extern=true;
        t5.extern_args=new ArrayList<Type>();
        t5.extern_args.add(new TFloat());
        t5.extern_ret=new TFloat();
        env.put("sin",t5);

        TFun t6=new TFun();
        t6.extern=true;
        t6.extern_args=new ArrayList<Type>();
        t6.extern_args.add(new TFloat());
        t6.extern_ret=new TFloat();
        env.put("cos",t6);

        TFun t7=new TFun();
        t7.extern=true;
        t7.extern_args=new ArrayList<Type>();
        t7.extern_args.add(new TFloat());
        t7.extern_ret=new TFloat();
        env.put("sqrt",t7);

        TFun t8=new TFun();
        t8.extern=true;
        t8.extern_args=new ArrayList<Type>();
        t8.extern_args.add(new TFloat());
        t8.extern_ret=new TFloat();
        env.put("abs_float",t8);

        TFun t9=new TFun();
        t9.extern=true;
        t9.extern_args=new ArrayList<Type>();
        t9.extern_args.add(new TInt());
        t9.extern_ret=new TFloat();
        env.put("float_of_int",t9);
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

    public Type visit(Not e) throws Exception {
        Type res = e.e.accept(this);
        if(res instanceof TBool || res instanceof TAssumeOK){
            return new TBool();
        }
        throw new TypingException("NOT error : wrong type");
    }

    public Type visit(Neg e) throws Exception {
        Type res = e.e.accept(this);
        if(res instanceof TInt  || res instanceof TAssumeOK){
            return new TInt();
        }
        throw new TypingException("NEG error : wrong type");
    }

    public Type visit(Add e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if((res1 instanceof TInt || res1 instanceof TAssumeOK) 
        && (res2 instanceof TInt || res2 instanceof TAssumeOK)) {
            return new TInt();
        }
        throw new TypingException("ADD error : wrong type");   
    }

    public Type visit(Sub e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if((res1 instanceof TInt || res1 instanceof TAssumeOK) 
        && (res2 instanceof TInt || res2 instanceof TAssumeOK)){
            return new TInt();
        }
        throw new TypingException("SUB error : wrong type");
   }

    public Type visit(FNeg e) throws Exception {
        Type res = e.e.accept(this);
        if(res instanceof TFloat || res instanceof TAssumeOK){
            return new TFloat();
        }
        throw new TypingException("FLOAT NEG error : wrong type");
    }

    public Type visit(FAdd e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if((res1 instanceof TFloat || res1 instanceof TAssumeOK) 
        && (res2 instanceof TFloat || res2 instanceof TAssumeOK)) {
            return new TFloat();
        }
        throw new TypingException("FLOAT ADD error : wrong type");
    }

    public Type visit(FSub e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if((res1 instanceof TFloat || res1 instanceof TAssumeOK) 
        && (res2 instanceof TFloat || res2 instanceof TAssumeOK)) {
            return new TFloat();
        }
        throw new TypingException("FLOAT SUB error : wrong type");
    }

    public Type visit(FMul e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if((res1 instanceof TFloat || res1 instanceof TAssumeOK) 
        && (res2 instanceof TFloat || res2 instanceof TAssumeOK)) {
            return new TFloat();
        }
        throw new TypingException("FLOAT MUL error : wrong type");    
    }

    public Type visit(FDiv e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if((res1 instanceof TFloat || res1 instanceof TAssumeOK) 
        && (res2 instanceof TFloat || res2 instanceof TAssumeOK)) {
            return new TFloat();
        }
        throw new TypingException("FLOAT DIV error : wrong type");
    }

    public Type visit(Eq e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if((res1!=null && res2!=null 
        && res1.getClass().getName().equals(res2.getClass().getName())) 
        || res1 instanceof TAssumeOK || res2 instanceof TAssumeOK) {
            return new TBool();
        }
        throw new TypingException("EQ error : type mismatch");
    }

    public Type visit(LE e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if(((res1 instanceof TInt || res1 instanceof TAssumeOK) 
        && (res2 instanceof TInt || res2 instanceof TAssumeOK))
        || ((res1 instanceof TFloat || res1 instanceof TAssumeOK) 
        && (res2 instanceof TFloat || res2 instanceof TAssumeOK))) {
            return new TBool();
        }
        throw new TypingException("LE error : wrong type");
    }

    public Type visit(If e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        Type res3 = e.e3.accept(this);
        if(res1 instanceof TBool
        && (res2.getClass().getName().equals(res3.getClass().getName()) 
        || res2 instanceof TAssumeOK || res3 instanceof TAssumeOK)) {
            return res2;
        } else {
            throw new TypingException("IF error : wrong type");
        }
    }

    public Type visit(Let e) throws Exception {
        Type res1 = e.e1.accept(this);
        env.put(e.id.id, res1);
        Type res2 = e.e2.accept(this);
        return res2;
    }

    public Type visit(Var e) throws Exception{
        Type res=env.get(e.id.id);
        if(res==null)
            throw new TypingException("VAR exception : "+e.id.id+" is undeclared");
        return res;
    }

    public Type visit(LetRec e) throws Exception {
        TFun t=new TFun();
        t.body=e.fd.e;
        t.args=e.fd.args;
        env.put(e.fd.id.id, t);
        Type res2 = e.e.accept(this);
        return res2;
    }

    public Type visit(App e) throws Exception {
        Type res = e.e.accept(this);
        if (res instanceof TFun) {
            if(((TFun)res).extern){
                int i=0;
                for(Exp exp : e.es){
                    Type res_=exp.accept(this);
                    if(e.es.size()!=((TFun)res).extern_args.size())
                        throw new TypingException("APP error : wrong number of arguments");
                    if(res_==null || !((res_ instanceof TAssumeOK) 
                        || res_.getClass().getName().equals(((TFun)res).extern_args.get(i).getClass().getName()))){
                        throw new TypingException("APP error : "+((Var)e.e).id.id
                            +" : wrong argument type " +"(has "+res_.getClass().getName()
                            +", expected "+((TFun)res).extern_args.get(i).getClass().getName()
                            +")");
                    }
                    i++;
                }
                return ((TFun)res).extern_ret;
            }
            int i=0;
            for (Exp exp : e.es) {
                Type res_ = exp.accept(this);
                env.put(((TFun)res).args.get(i).id,res_);
                i++;
            }
            Type res__;
            if(((TFun)res).rec_calls<2){
                ((TFun)res).rec_calls++;
                res__=((TFun)res).body.accept(this);
                ((TFun)res).rec_calls--;
            } else {
                res__=new TAssumeOK();
            }
            return res__;
        }
        throw new TypingException("APP error : not a function");
    }

    public Type visit(Tuple e) throws Exception {
        List<Type> types = new ArrayList<>();
        for (Exp exp : e.es) {
            Type res = exp.accept(this);
            types.add(res);
        }
        return new TTuple(types);
    }

    public Type visit(LetTuple e) throws Exception{
        Type res1 = e.e1.accept(this);
        if (res1 instanceof TTuple) {
            for (int i = 0; i < e.ids.size(); i++) {
                Type res3=((TTuple)res1).types.get(i);
                env.put(e.ids.get(i).id, res3);
            }
            Type res2 = e.e2.accept(this);
            return res2;
        }
        throw new TypingException("LET TUPLE error : not a tuple");
    }

    public Type visit(Array e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if(res1 instanceof TInt) {
            return new TArray(res2);
        }
        throw new TypingException("ARRAY error : size is not int");
    }

    public Type visit(Get e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if(res1 instanceof TArray && res2 instanceof TInt) {
            return ((TArray)res1).type;
        }
        if(!(res1 instanceof TArray))
            throw new TypingException("ARRAY GET error : not an array");
        throw new TypingException("ARRAY GET error : index is not int");
    }

    public Type visit(Put e) throws Exception {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        Type res3 = e.e3.accept(this);
        if(res1 instanceof TArray 
        && res2 instanceof TInt
        && res3.getClass().equals(((TArray)res1).type.getClass())) {
            return new TUnit();
        }
        if(!(res1 instanceof TArray))
            throw new TypingException("ARRAY PUT error : not an array");
        if(!(res2 instanceof TInt))
            throw new TypingException("ARRAY PUT error : index is not int");
        throw new TypingException("ARRAY PUT error : wrong type (has "
            +res3.getClass().getName()+", expected "
            +((TArray)res1).type.getClass().getName()+")");
    }
}
