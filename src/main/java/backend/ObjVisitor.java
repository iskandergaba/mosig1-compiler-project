package backend;

public interface ObjVisitor<E> {
    E visit(Int e);
    E visit(Var e);
    E visit(Nop e);
    E visit(Fun e);
    E visit(Neg e);
    E visit(FNeg e);
    E visit(FAdd e);
    E visit(FSub e);
    E visit(FMul e);
    E visit(FDiv e);
    E visit(New e);
    E visit(Add e);
    E visit(Sub e);
    E visit(Get e);
    E visit(Put e);
    E visit(If e);
    E visit(Eq e);
    E visit(LE e);
    E visit(GE e);
    E visit(FEq e);
    E visit(FLE e);
    E visit(Call e);
    E visit(AppClosure e);
    E visit(Let e);
    E visit(LetRec e);
    E visit(Float e);
}
