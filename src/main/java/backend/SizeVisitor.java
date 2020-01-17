package backend;

import java.util.*;

public class SizeVisitor implements ObjVisitor<Integer> {

    @Override
    public Integer visit(Int e) {
        return 0;
    }

    @Override
    public Integer visit(Float e) {
        return 0;
    }

    @Override
    public Integer visit(Neg e) {
        return 0;
    }

    @Override
    public Integer visit(Add e) {
        return e.e.accept(this);
    }

    @Override
    public Integer visit(Sub e) {
        return e.e.accept(this);
    }

    @Override
    public Integer visit(FNeg e) {
        return 0;
    }

    @Override
    public Integer visit(FAdd e) {
        return 0;
    }

    @Override
    public Integer visit(FSub e) {
        return 0;
    }

    @Override
    public Integer visit(FMul e) {
        return 0;
    }

    @Override
    public Integer visit(FDiv e) {
        return 0;
    }

    @Override
    public Integer visit(Eq e) {
        return e.e.accept(this);
    }

    @Override
    public Integer visit(LE e) {     
        return e.e.accept(this);
    }

    @Override
    public Integer visit(GE e) {
        return e.e.accept(this);
    }

    @Override
    public Integer visit(FEq e) {
        return 0;
    }

    @Override
    public Integer visit(FLE e) {
        return 0;
    }

    @Override
    public Integer visit(If e) {
        return e.cond.accept(this) +
        e.e1.accept(this) +
        e.e2.accept(this);
    }

    @Override
    public Integer visit(Let e) {
        return 1 +
        e.e1.accept(this) +
        e.e2.accept(this);
    }

    @Override
    public Integer visit(Var e) {
        return 0;
    }

    @Override
    public Integer visit(LetRec e) {
        return e.e.accept(this);
    }

    @Override
    public Integer visit(Call e) {
        return 0;
    }

    @Override
    public Integer visit(New e) {
        return e.size.accept(this);
    }

    @Override
    public Integer visit(Get e) {
        return 0;
    }

    @Override
    public Integer visit(Put e) {
        return e.offset.accept(this);
    }

    @Override
    public Integer visit(Nop e) {
        return 0;
    }

    @Override
    public Integer visit(Fun e) {
        return 0;
    }

    @Override
    public Integer visit(AppClosure e) {
        return 0;
    }

    @Override
    public Integer visit(FunDefs e) {
        int result = 0;
        for (Exp exp: e.funs) {
            result += exp.accept(this);
        }
        return result;
    }
}