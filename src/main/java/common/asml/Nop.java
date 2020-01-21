package common.asml;

import common.visitor.*;

public class Nop extends Exp {
    Nop() {

    }
    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}