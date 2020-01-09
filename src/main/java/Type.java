import java.util.List;

abstract class Type {
    private static int x = 0;
    static Type gen() {
        return new TVar("?" + x++);
    }
    
}

class TUnit extends Type { }

class TBool extends Type { }

class TInt extends Type { }

class TFloat extends Type { }

class TFun extends Type {}

class TTuple extends Type {
    List<Type> types;
    TTuple(List<Type> types) {
        this.types = types;
    }
    
}

class TArray extends Type {
    Type type;
    TArray(Type type) {
        this.type = type;
    }
}

class TVar extends Type {
    String v;
    TVar(String v) {
        this.v = v;
    }
    @Override
    public String toString() {
        return v; 
    }
}

