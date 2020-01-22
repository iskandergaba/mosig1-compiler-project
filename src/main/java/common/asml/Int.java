package common.asml;

import common.visitor.*;

public class Int extends Exp {
    public final int i;

    public Int(int i) {
        this.i = i;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}