package common.asml;

import common.visitor.*;

public class Int extends Exp {
    final int i;

    Int(int i) {
        this.i = i;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}