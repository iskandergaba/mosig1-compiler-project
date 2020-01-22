package common.asml;

import java.util.List;

import common.visitor.*;

public class AppClosure extends Exp {
    final Id id;
    final List<Id> args;

    public AppClosure(Id id, List<Id> args) {
        this.id = id;
        this.args = args;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}