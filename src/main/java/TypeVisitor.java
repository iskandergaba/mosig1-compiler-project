import java.util.*;

class TypeVisitor implements ObjVisitor<Type> {

    public Hashtable<String, Type> env = new Hashtable<>();

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
        return res instanceof TBool ? new TBool() : null;
    }

    public Type visit(Neg e) {
        Type res = e.e.accept(this);
        return res instanceof TInt ? new TInt() : null;
    }

    public Type visit(Add e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1 instanceof TInt && res2 instanceof TInt ? new TInt() : null;
    }

    public Type visit(Sub e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1 instanceof TInt && res2 instanceof TInt ? new TInt() : null;
   }

    public Type visit(FNeg e){
        Type res = e.e.accept(this);
        return res instanceof TFloat ? new TFloat() : null;
    }

    public Type visit(FAdd e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1 instanceof TFloat && res2 instanceof TFloat ? new TFloat() : null;
    }

    public Type visit(FSub e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1 instanceof TFloat && res2 instanceof TFloat ? new TFloat() : null;
    }

    public Type visit(FMul e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1 instanceof TFloat && res2 instanceof TFloat ? new TFloat() : null;
     }

    public Type visit(FDiv e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1 instanceof TFloat && res2 instanceof TFloat ? new TFloat() : null;
    }

    public Type visit(Eq e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return res1.getClass().getName().equals(res2.getClass().getName()) ? new TBool() : null;
    }

    public Type visit(LE e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        return (res1 instanceof TInt && res2 instanceof TInt)
            || (res1 instanceof TFloat && res2 instanceof TFloat)
                ? new TBool()
                : null;
    }

    public Type visit(If e){
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        Type res3 = e.e3.accept(this);
        return res1 instanceof TBool && res2 instanceof TUnit && res3 instanceof TUnit 
            ? new TFloat()
            : null;
    }

    public Type visit(Let e) {
        Type res1 = e.e1.accept(this);
        Type res2 = e.e2.accept(this);
        if (e.t.getClass().getName().equals(res1.getClass().getName()) && res2 instanceof TUnit) {
            env.put(e.id.id, e.t);
            return new TUnit();
        }
        return null;
    }

    public Type visit(Var e){
        return new TVar(e.id.id);
    }

    public Type visit(LetRec e){
        Type res1 = e.fd.e.accept(this);
        Type res2 = e.e.accept(this);
        if (res1 instanceof TFun && res2 instanceof TUnit) {
            env.put(e.fd.id.id, e.fd.type);
            return new TUnit();
        }
        return null;
    }

    public Type visit(App e) {
        /*
        int res1 = e.e.accept(this);
        for (Exp exp : e.es) {
            res1 = Math.max(res1, exp.accept(this));
        }
        return res1 + 1;
        */
        return new TUnit();
    }

    public Type visit(Tuple e) {
        List<Type> types = new ArrayList<>();
        for (Exp exp : e.es) {
            types.add(exp.accept(this));
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
