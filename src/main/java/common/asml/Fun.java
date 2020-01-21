package common.asml;

import common.visitor.*;

public class Fun extends Exp {
    final Label l;

    Fun(Label l) {
        this.l = l;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}