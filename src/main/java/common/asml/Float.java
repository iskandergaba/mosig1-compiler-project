package common.asml;

import common.visitor.*;

public class Float extends Exp {
    public float f;
    public Label l;

    public Float(float f, Label l) {
        this.f = f;
        this.l = l;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}