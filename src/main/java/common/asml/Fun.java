package common.asml;

import common.visitor.*;

public class Fun extends Exp {
    public final Label l;

    public Fun(Label l) {
        this.l = l;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}