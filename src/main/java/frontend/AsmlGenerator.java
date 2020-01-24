package frontend;

import java.util.*;

/**
 * Visitor used for asml generation
 */
public class AsmlGenerator implements ObjVisitor<common.asml.Exp> {

    List<common.asml.Float> floatDefs = new ArrayList<>();
    List<common.asml.FunDef> funDefs = new ArrayList<>();
    int floatCount = 0;
    int tempCount = 0;
    int tupleCount = 0;

    public common.asml.FunDefs join(common.asml.Exp exp) {
        List<common.asml.Exp> defs = new ArrayList<>();
        for (common.asml.Float f : floatDefs) {
            defs.add(f);
        }
        for (common.asml.FunDef fd : funDefs) {
            defs.add(new common.asml.LetRec(fd, null));
        }
        common.asml.FunDef main = new common.asml.FunDef(new common.asml.Fun(new common.asml.Label("_")),
                common.type.Type.gen(), new ArrayList<common.asml.Id>(), exp);
        defs.add(new common.asml.LetRec(main, null));
        return new common.asml.FunDefs(defs);
    }

    public common.asml.Exp visit(Unit e) {
        return new common.asml.Nop();
    }

    public common.asml.Exp visit(Bool e) {
        if (e.b)
            return new common.asml.Int(1);
        else
            return new common.asml.Int(0);
    }

    public common.asml.Exp visit(Int e) {
        return new common.asml.Int(e.i);
    }

    public common.asml.Exp visit(Float e) {
        common.asml.Label l = new common.asml.Label("_float" + floatCount);
        floatDefs.add(new common.asml.Float(e.f, l));
        floatCount++;
        return new common.asml.Fun(l);
    }

    public common.asml.Exp visit(Not e) throws Exception {
        throw new AsmlTranslationException("error : Not encountered");
    }

    public common.asml.Exp visit(Neg e) throws Exception {
        if (e.e instanceof Var) {
            common.asml.Id id = new common.asml.Id(((Var) e.e).id.id);
            return new common.asml.Neg(id);
        }
        throw new AsmlTranslationException("error : expected Var as operand to Neg");
    }

    public common.asml.Exp visit(Add e) throws Exception {
        if (e.e1 instanceof Var) {
            common.asml.Exp res2 = e.e2.accept(this);
            return new common.asml.Add(new common.asml.Id(((Var) e.e1).id.id), res2);
        }
        throw new AsmlTranslationException("error : expected Var as first operand to Add");
    }

    public common.asml.Exp visit(Sub e) throws Exception {
        if (e.e1 instanceof Var) {
            common.asml.Exp res2 = e.e2.accept(this);
            return new common.asml.Sub(new common.asml.Id(((Var) e.e1).id.id), res2);
        }
        throw new AsmlTranslationException("error : expected Var as first operand to Sub");
    }

    public common.asml.Exp visit(FNeg e) throws Exception {
        if (e.e instanceof Var) {
            return new common.asml.FNeg(new common.asml.Id(((Var) e.e).id.id));
        }
        throw new AsmlTranslationException("error : expected Var as operand to FNeg");
    }

    public common.asml.Exp visit(FAdd e) throws Exception {
        if (e.e1 instanceof Var && e.e2 instanceof Var) {
            return new common.asml.FAdd(new common.asml.Id(((Var) e.e1).id.id), new common.asml.Id(((Var) e.e2).id.id));
        }
        throw new AsmlTranslationException("error : expected Vars operands to FAdd");
    }

    public common.asml.Exp visit(FSub e) throws Exception {
        if (e.e1 instanceof Var) {
            return new common.asml.FSub(new common.asml.Id(((Var) e.e1).id.id), new common.asml.Id(((Var) e.e2).id.id));
        }
        throw new AsmlTranslationException("error : expected Vars as operands to FSub");
    }

    public common.asml.Exp visit(FMul e) throws Exception {
        if (e.e1 instanceof Var) {
            return new common.asml.FMul(new common.asml.Id(((Var) e.e1).id.id), new common.asml.Id(((Var) e.e2).id.id));
        }
        throw new AsmlTranslationException("error : expected Vars as operands to FMul");
    }

    public common.asml.Exp visit(FDiv e) throws Exception {
        if (e.e1 instanceof Var) {
            return new common.asml.FDiv(new common.asml.Id(((Var) e.e1).id.id), new common.asml.Id(((Var) e.e2).id.id));
        }
        throw new AsmlTranslationException("error : expected Vars as operands to FDiv");
    }

    public common.asml.Exp visit(Eq e) throws Exception {
        if (e.e1 instanceof Var) {
            if (e.e2 instanceof Int) {
                common.asml.Exp res = e.e2.accept(this);
                return new common.asml.Eq(new common.asml.Id(((Var) e.e1).id.id), res);
            } else if (e.e2 instanceof Var) {
                return new common.asml.FEq(new common.asml.Id(((Var) e.e1).id.id),
                        new common.asml.Id(((Var) e.e2).id.id));
            } else {
                throw new AsmlTranslationException("error : expected Var as second operand of FEq");
            }
        }
        throw new AsmlTranslationException("error : expected Var as first operand of Eq");
    }

    public common.asml.Exp visit(LE e) throws Exception {
        if (e.e1 instanceof Var) {
            if (e.e2 instanceof Int) {
                common.asml.Exp res = e.e2.accept(this);
                return new common.asml.LE(new common.asml.Id(((Var) e.e1).id.id), res);
            } else if (e.e2 instanceof Var) {
                return new common.asml.FLE(new common.asml.Id(((Var) e.e1).id.id),
                        new common.asml.Id(((Var) e.e2).id.id));
            } else {
                throw new AsmlTranslationException("error : expected Var as second operand of FLE");
            }
        }
        throw new AsmlTranslationException("error : expected Var as first operand of LE");
    }

    public common.asml.Exp visit(If e) throws Exception {
        if (e.e1 instanceof Not) {
            return new If(((Not) e.e1).e, e.e3, e.e2).accept(this);
        } else {
            return new common.asml.If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
        }
    }

    public common.asml.Exp visit(Let e) throws Exception {
        if (e.e1 instanceof Tuple) {
            Tuple t = (Tuple) e.e1;
            common.asml.Exp exp = e.e2.accept(this);
            common.asml.Id tid = new common.asml.Id(e.id.id);
            common.asml.Var var = new common.asml.Var(tid);
            for (int i = t.es.size() - 1; i >= 0; i--) {
                if (t.es.get(i) instanceof Var) {
                    common.asml.Put p = new common.asml.Put(var, new common.asml.Int(i * 4),
                            new common.asml.Id(((Var) t.es.get(i)).id.id));
                    exp = new common.asml.Let(new common.asml.Id("tmp" + tempCount), common.type.Type.gen(), p, exp);
                    tempCount++;
                } else {
                    throw new AsmlTranslationException("error : expected Var in tuple");
                }
            }
            common.asml.New n = new common.asml.New(new common.asml.Int(4 * t.es.size()));
            return new common.asml.Let(tid, common.type.Type.gen(), n, exp);
        } else if (e.e1 instanceof Array) {
            Array a = (Array) e.e1;
            common.asml.Id array = new common.asml.Id(e.id.id);
            common.asml.Exp exp = e.e2.accept(this);
            /*
             * for (int i = ((Int) a.e1).i - 1; i >= 0; i--) { if (a.e2 instanceof Var) {
             * common.asml.Put p = new common.asml.Put(array, new common.asml.Int(i * 4),
             * new common.asml.Id(((Var) a.e2).id.id)); exp = new common.asml.Let(new
             * common.asml.Id("tmp" + tempCount), common.type.Type.gen(), p, exp); } else {
             * throw new
             * AsmlTranslationException("error : expected Var in array initialization"); } }
             * common.asml.New n = new common.asml.New(new common.asml.Int(((Int) a.e1).i *
             * 4)); exp = new common.asml.Let(array, common.type.Type.gen(), n, exp);
             */
            List<common.asml.Id> args = new ArrayList<>();
            args.add(new common.asml.Id(((Var) a.e1).id.id));
            args.add(new common.asml.Id(((Var) a.e2).id.id));
            common.asml.Call c = new common.asml.Call(new common.asml.Label("_min_caml_create_array"), args);
            return new common.asml.Let(array, common.type.Type.gen(), c, exp);
        } else if (e.e1 instanceof App && ((App) e.e1).e instanceof Var
                && ((Var) ((App) e.e1).e).id.id.equals("_make_closure_")) {
            common.asml.Id closure = new common.asml.Id(e.id.id);
            common.asml.Var closureVar = new common.asml.Var(closure);
            App a = ((App) e.e1);
            common.asml.Exp exp = e.e2.accept(this);
            for (int off = 4 * (a.es.size() - 1); off >= 0; off -= 4) {
                Exp arg = a.es.get(off / 4);
                if (!(arg instanceof Var)) {
                    throw new AsmlTranslationException("error : expected Var as argument to _make_closure_");
                }
                if (off == 0) {
                    common.asml.Id funId = new common.asml.Id("addr" + ((Var) arg).id.id);
                    common.asml.Put p = new common.asml.Put(closureVar, new common.asml.Int(off), funId);
                    exp = new common.asml.Let(new common.asml.Id("tmp" + tempCount), common.type.Type.gen(), p, exp);
                    tempCount++;
                    exp = new common.asml.Let(funId, common.type.Type.gen(),
                            new common.asml.Fun(new common.asml.Label(((Var) arg).id.id)), exp);
                    break;
                }
                common.asml.Put p = new common.asml.Put(closureVar, new common.asml.Int(off),
                        new common.asml.Id(((Var) arg).id.id));
                exp = new common.asml.Let(new common.asml.Id("tmp" + tempCount), common.type.Type.gen(), p, exp);
                tempCount++;
            }
            common.asml.New n = new common.asml.New(new common.asml.Int(4 * a.es.size()));
            exp = new common.asml.Let(closure, common.type.Type.gen(), n, exp);
            return exp;
        }
        return new common.asml.Let(new common.asml.Id(e.id.id), common.type.Type.gen(), e.e1.accept(this),
                e.e2.accept(this));
    }

    public common.asml.Exp visit(Var e) {
        return new common.asml.Var(new common.asml.Id(e.id.id));
    }

    public common.asml.Exp visit(LetRec e) throws Exception {
        common.asml.Label id = new common.asml.Label(e.fd.id.id);
        List<common.asml.Id> args = new ArrayList<>();
        for (Id id_ : e.fd.args) {
            args.add(new common.asml.Id(id_.id));
        }
        common.asml.Exp body = e.fd.e.accept(this);
        for (int i = e.fd.free.size() - 1; i >= 0; i--) {
            common.asml.Get g = new common.asml.Get(new common.asml.Self(), new common.asml.Int((i + 1) * 4));
            body = new common.asml.Let(new common.asml.Id(e.fd.free.get(i).id), common.type.Type.gen(), g, body);
        }
        common.asml.FunDef fd = new common.asml.FunDef(new common.asml.Fun(id), e.fd.type, args, body);
        funDefs.add(fd);
        return e.e.accept(this);
    }

    public common.asml.Exp visit(App e) throws Exception {
        if (e.e instanceof Var) {
            if (((Var) e.e).id.id.equals("_apply_direct_")) {
                common.asml.Label f;
                if (e.es.get(0) instanceof Var) {
                    f = new common.asml.Label(((Var) e.es.get(0)).id.id);
                } else {
                    throw new AsmlTranslationException("error : expected Var as function in Call");
                }
                List<common.asml.Id> args = new ArrayList<>();
                boolean first = true;
                for (Exp exp : e.es) {
                    if (first) {
                        first = false;
                        continue;
                    }
                    if (exp instanceof Var) {
                        args.add(new common.asml.Id(((Var) exp).id.id));
                    } else {
                        throw new AsmlTranslationException("error : expected Var as argument in Call");
                    }
                }
                return new common.asml.Call(f, args);
            } else if (((Var) e.e).id.id.equals("_apply_closure_")) {
                common.asml.Id closure;
                if (e.es.get(0) instanceof Var) {
                    closure = new common.asml.Id(((Var) e.es.get(0)).id.id);
                } else {
                    throw new AsmlTranslationException("error : expected Var as function in Call");
                }
                List<common.asml.Id> args = new ArrayList<>();
                boolean first = true;
                for (Exp exp : e.es) {
                    if (first) {
                        first = false;
                        continue;
                    }
                    if (exp instanceof Var) {
                        args.add(new common.asml.Id(((Var) exp).id.id));
                    } else {
                        throw new AsmlTranslationException("error : expected Var as argument in Call");
                    }
                }
                return new common.asml.AppClosure(closure, args);
            } else if (((Var) e.e).id.id.equals("_make_closure_")) {
                throw new AsmlTranslationException("error : closure creation was not dealt with at Let level");
            } else {
                throw new AsmlTranslationException("error : unrecognized function Id in App");
            }
        } else {
            throw new AsmlTranslationException("error : expected Var as function in App");
        }
    }

    public common.asml.Exp visit(Tuple e) throws Exception {
        common.asml.Id tid = new common.asml.Id("tuple" + tupleCount);
        tupleCount++;
        common.asml.Exp exp = new common.asml.Var(tid);
        common.asml.Var var = new common.asml.Var(tid);
        for (int i = e.es.size() - 1; i >= 0; i--) {
            if (e.es.get(i) instanceof Var) {
                common.asml.Put p = new common.asml.Put(var, new common.asml.Int(i * 4),
                        new common.asml.Id(((Var) e.es.get(i)).id.id));
                exp = new common.asml.Let(new common.asml.Id("tmp" + tempCount), common.type.Type.gen(), p, exp);
                tempCount++;
            } else {
                throw new AsmlTranslationException("error : expected Var in tuple");
            }
        }
        common.asml.New n = new common.asml.New(new common.asml.Int(4 * e.es.size()));
        exp = new common.asml.Let(tid, common.type.Type.gen(), n, exp);
        return exp;
    }

    public common.asml.Exp visit(LetTuple e) throws Exception {
        if (e.e1 instanceof Tuple) {
            common.asml.Exp exp = e.e2.accept(this);
            Tuple t = (Tuple) e.e1;
            for (int i = t.es.size() - 1; i >= 0; i--) {
                if (t.es.get(i) instanceof Var) {
                    exp = new common.asml.Let(new common.asml.Id(e.ids.get(i).id), common.type.Type.gen(),
                            t.es.get(i).accept(this), exp);
                } else {
                    throw new AsmlTranslationException("error : expected Var in tuple");
                }
            }
            return exp;
        } else if (e.e1 instanceof Var) {
            common.asml.Exp exp = e.e2.accept(this);
            Var t = (Var) e.e1;
            common.asml.Id id = new common.asml.Id(t.id.id);
            common.asml.Var var = new common.asml.Var(id);
            for (int i = e.ids.size() - 1; i >= 0; i--) {
                common.asml.Get g = new common.asml.Get(var, new common.asml.Int(i * 4));
                exp = new common.asml.Let(new common.asml.Id(e.ids.get(i).id), common.type.Type.gen(), g, exp);
            }
            return exp;
        } else {
            throw new AsmlTranslationException("error : expected Tuple or Var in LetTuple");
        }
    }

    public common.asml.Exp visit(Array e) throws Exception {
        throw new AsmlTranslationException("error : reached Array in visitor");
    }

    public common.asml.Exp visit(Get e) throws Exception {
        if (e.e1 instanceof Var) {
            return new common.asml.Get(e.e1.accept(this), e.e2.accept(this));
        } else {
            throw new AsmlTranslationException("error : expected Var for array in Get");
        }
    }

    public common.asml.Exp visit(Put e) throws Exception {
        if (e.e1 instanceof Var) {
            if (e.e3 instanceof Var) {
                return new common.asml.Put(e.e1.accept(this), e.e2.accept(this),
                        new common.asml.Id(((Var) e.e3).id.id));
            } else {
                throw new AsmlTranslationException("error : expected Var to Put in array");
            }
        } else {
            throw new AsmlTranslationException("error : expected Var for array in Get");
        }
    }
}
