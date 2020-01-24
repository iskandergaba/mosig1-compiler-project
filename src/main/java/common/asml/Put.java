package common.asml;

import common.visitor.*;

public class Put extends Exp {
    public final Id base;
    public final Exp offset;
    public final Id dest;

    public Put(Id base, Exp offset, Id dest) {
        this.base = base;
        this.offset = offset;
        this.dest = dest;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}