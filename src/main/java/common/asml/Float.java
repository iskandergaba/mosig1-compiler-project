package common.asml;

import common.visitor.*;

public class Float extends Exp {
    float f;
    Label l;

    Float(float f, Label l) {
        this.f = f;
        this.l = l;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}