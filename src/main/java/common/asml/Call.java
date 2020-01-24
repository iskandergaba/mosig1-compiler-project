package common.asml;

import java.util.List;

import common.visitor.*;

public class Call extends Exp {
    public final Label f;
    public final List<Id> args;

    public Call(Label f, List<Id> args) {
        this.f = f;
        this.args = args;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}