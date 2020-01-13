package frontend;

import java.util.*;

class HeightVisitor implements ObjVisitor<Integer> {

    public Integer visit(Unit e) {
        // This tree is of height 0
        return 0;
    }

    public Integer visit(Bool e) {
        return 0;
    }

    public Integer visit(Int e) {
        return 0;
    }

    public Integer visit(Float e) {
        return 0;
    }

    public Integer visit(Not e) throws Exception {
        return e.e.accept(this) + 1;
    }

    public Integer visit(Neg e) throws Exception {
        return e.e.accept(this) + 1;
    }

    public Integer visit(Add e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(Sub e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(FNeg e) throws Exception {
        return e.e.accept(this) + 1;
    }

    public Integer visit(FAdd e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(FSub e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(FMul e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(FDiv e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(Eq e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(LE e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(If e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        int res3 = e.e3.accept(this);
        return Math.max(res1, Math.max(res2, res3)) + 1;
    }

    public Integer visit(Let e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(Var e) {
        return 0;
    }

    public Integer visit(LetRec e) throws Exception {
        int res1 = e.e.accept(this);
        int res2 = e.fd.e.accept(this);
        return Math.max(res1, res2) + 1;
    }

    public Integer visit(App e) throws Exception {
        int res1 = e.e.accept(this);
        for (Exp exp : e.es) {
            res1 = Math.max(res1, exp.accept(this));
        }
        return res1 + 1;
    }

    public Integer visit(Tuple e) throws Exception {
        int res1 = 0;
        for (Exp exp : e.es) {
            int res = exp.accept(this);
            res1 = Math.max(res, res1);
        }
        return res1 + 1;
    }

    public Integer visit(LetTuple e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2);
    }

    public Integer visit(Array e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2);
    }

    public Integer visit(Get e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        return Math.max(res1, res2);
    }

    public Integer visit(Put e) throws Exception {
        int res1 = e.e1.accept(this);
        int res2 = e.e2.accept(this);
        int res3 = e.e3.accept(this);
        return Math.max(res1, Math.max(res2, res3)) + 1;
    }
}
