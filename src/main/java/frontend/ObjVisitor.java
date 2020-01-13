package frontend;

interface ObjVisitor<E> {
    E visit(Unit e) throws Exception;

    E visit(Bool e) throws Exception;

    E visit(Int e) throws Exception;

    E visit(Float e) throws Exception;

    E visit(Not e) throws Exception;

    E visit(Neg e) throws Exception;

    E visit(Add e) throws Exception;

    E visit(Sub e) throws Exception;

    E visit(FNeg e) throws Exception;

    E visit(FAdd e) throws Exception;

    E visit(FSub e) throws Exception;

    E visit(FMul e) throws Exception;

    E visit(FDiv e) throws Exception;

    E visit(Eq e) throws Exception;

    E visit(LE e) throws Exception;

    E visit(If e) throws Exception;

    E visit(Let e) throws Exception;

    E visit(Var e) throws Exception;

    E visit(LetRec e) throws Exception;

    E visit(App e) throws Exception;

    E visit(Tuple e) throws Exception;

    E visit(LetTuple e) throws Exception;

    E visit(Array e) throws Exception;

    E visit(Get e) throws Exception;

    E visit(Put e) throws Exception;
}
