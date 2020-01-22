package common.asml;

import java.util.List;

import common.type.Type;

public class FunDef {
    final Fun fun;
    final Type type;
    final List<Id> args;
    final Exp e;

    public FunDef(Fun fun, Type t, List<Id> args, Exp e) {
        this.fun = fun;
        this.type = t;
        this.args = args;
        this.e = e;
    }

}