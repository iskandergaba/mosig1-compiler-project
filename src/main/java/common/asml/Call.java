package common.asml;

import java.util.List;

import common.visitor.*;

public class Call extends Exp {
    final Label f;
    final List<Id> args;

    Call(Label f, List<Id> args) {
        this.f = f;
        this.args = args;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}