package common.visitor;

import common.asml.*;
import common.asml.Float;

/**
 * Provides an interface for visiting an AST.
 */
public interface Visitor {
    void visit(Int e);
    void visit(Var e);
    void visit(Nop e);
    void visit(Fun e);
    void visit(Neg e);
    void visit(FNeg e);
    void visit(FAdd e);
    void visit(FSub e);
    void visit(FMul e);
    void visit(FDiv e);
    void visit(New e);
    void visit(Add e);
    void visit(Sub e);
    void visit(Get e);
    void visit(Put e);
    void visit(If e);
    void visit(Eq e);
    void visit(LE e);
    void visit(GE e);
    void visit(FEq e);
    void visit(FLE e);
    void visit(Call e);
    void visit(AppClosure e);
    void visit(FunDefs e);
    void visit(Let e);
    void visit(LetRec e);
    void visit(Float e);
}
