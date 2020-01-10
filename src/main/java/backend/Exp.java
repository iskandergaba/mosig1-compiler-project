package backend;

/**
 * Describes an expression in the ASML AST.
 */
public abstract class Exp {
    abstract void accept(Visitor v);

    abstract <E> E accept(ObjVisitor<E> v);
}