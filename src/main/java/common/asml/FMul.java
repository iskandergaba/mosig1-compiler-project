package common.asml;

import common.visitor.*;

public class FMul extends Exp {
    final Id id1;
    final Id id2;

    FMul(Id id1, Id id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}