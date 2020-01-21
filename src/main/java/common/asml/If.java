package common.asml;

import common.visitor.*;

public class If extends Exp {
    final Exp cond;
    final Exp e1;
    final Exp e2;

    If(Exp cond, Exp e1, Exp e2) {
        this.cond = cond;
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}