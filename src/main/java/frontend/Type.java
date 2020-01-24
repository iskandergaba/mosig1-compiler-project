package frontend;

import java.util.List;

import common.type.Type;

class TUnit extends Type { }

class TBool extends Type { }

class TInt extends Type { }

class TFloat extends Type { }

class TFun extends Type {
    List<Id> args;
    Exp body;
    boolean extern=false;
    List<Type> extern_args;
    Type extern_ret;
    int rec_calls=0;
}

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

class TAssumeOK extends Type {
    
}

