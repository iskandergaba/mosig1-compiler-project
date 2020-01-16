package frontend;

class LetReducer implements ObjVisitor<Exp> {

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
        return e;
    }

    public Exp visit(Neg e) throws Exception {
        return e;
    }

    public Exp visit(Add e) throws Exception {
        return e;
    }

    public Exp visit(Sub e) throws Exception {
        return e;
    }

    public Exp visit(FNeg e) throws Exception {
        return e;
    }

    public Exp visit(FAdd e) throws Exception {
        return e;
    }

    public Exp visit(FSub e) throws Exception {
        return e;
    }

    public Exp visit(FMul e) throws Exception {
        return e;
    }

    public Exp visit(FDiv e) throws Exception {
        return e;
    }

    public Exp visit(Eq e) throws Exception {
        return e;
    }

    public Exp visit(LE e) throws Exception {
        return e;
    }

    public Exp visit(If e) throws Exception {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

    public Exp visit(Let e) throws Exception {
        return insert(e, e.e1.accept(this));
    }

    public Exp visit(Var e) throws Exception {
        return e;
    }

    public Exp visit(LetRec e) throws Exception {
        FunDef fun = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        return new LetRec(fun, e.e.accept(this));
    }

    public Exp visit(App e) throws Exception {
        return e;
    }

    public Exp visit(Tuple e) throws Exception {
        return e;
    }

    public Exp visit(LetTuple e) throws Exception {
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    public Exp visit(Array e) throws Exception {
        return e;
    }

    public Exp visit(Get e) throws Exception {
        return e;
    }

    public Exp visit(Put e) throws Exception {
        return e;
    }

    private Exp insert(Let outerLet, Exp e) throws Exception {
        if (e instanceof Let) {
            Let let = (Let) e;
            return new Let(let.id, let.t, let.e1, insert(outerLet, let.e2));
        } else if (e instanceof LetRec) {
            LetRec letRec = (LetRec) e;
            return new LetRec(letRec.fd, insert(outerLet, letRec.e));
        } else if (e instanceof LetTuple) {
            LetTuple letTuple = (LetTuple) e;
            return new LetTuple(letTuple.ids, letTuple.ts, letTuple.e1, insert(outerLet, letTuple.e2));
        }
        return new Let(outerLet.id, outerLet.t, e, outerLet.e2.accept(this));
    }
}
