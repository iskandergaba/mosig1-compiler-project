package common.asml;

import java.util.List;

import common.type.Type;

public class FunDef {
    public final Fun fun;
    final Type type;
    public final List<Id> args;
    public final Exp e;

    public FunDef(Fun fun, Type t, List<Id> args, Exp e) {
        this.fun = fun;
        this.type = t;
        this.args = args;
        this.e = e;
    }

}