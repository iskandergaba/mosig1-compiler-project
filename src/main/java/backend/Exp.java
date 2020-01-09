package backend;

public abstract class Exp {
    abstract void accept(Visitor v);

    abstract <E> E accept(ObjVisitor<E> v);
}