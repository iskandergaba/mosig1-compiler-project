package common.asml;

import common.visitor.*;
/**
 * Describes an expression in the ASML AST.
 */
public abstract class Exp {
    public abstract void accept(Visitor v);

    abstract <E> E accept(ObjVisitor<E> v);
}
