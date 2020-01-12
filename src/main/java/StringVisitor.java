import java.util.*;

class StringVisitor implements ObjVisitor<String> {
    public String visit(Unit e) {
        return "()";
    }

    public String visit(Bool e) {
        if(e.b)
            return "True";
        else
            return "False";
    }

    public String visit(Int e) {
        return ""+e.i;
    }

    public String visit(Float e) {
        String s = String.format("%.2f", e.f);
        return s;
    }

    public String visit(Not e) throws Exception {
        String res="(not ";
        res+=e.e.accept(this);
        return res+")";
    }

    public String visit(Neg e) throws Exception {
        String res="(- ";
        res+=e.e.accept(this);
        return res+")";
    }

    public String visit(Add e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=" + ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(Sub e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=" - ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(FNeg e) throws Exception {
        String res="(-. ";
        res+=e.e.accept(this);
        return res+")";
    }

    public String visit(FAdd e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=" +. ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(FSub e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=" -. ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(FMul e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=" *. ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(FDiv e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=" /. ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(Eq e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=" = ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(LE e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=" <= ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(If e) throws Exception {
        String res="(if ";
        res+=e.e1.accept(this);
        res+=" then ";
        res+=e.e2.accept(this);
        res+=" else ";
        res+=e.e3.accept(this);
        return res+")";
    }

    public String visit(Let e) throws Exception {
        String res="(let ";
        res+=e.id;
        res+=" = ";
        res+=e.e1.accept(this);
        res+=" in ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(Var e){
        return e.id.id;
    }


    // print sequence of identifiers 
    static <E> String printInfix(List<E> l, String op) {
        String res="";
        if (l.isEmpty()) {
            return res;
        }
        Iterator<E> it = l.iterator();
        res+=it.next();
        while (it.hasNext()) {
            res+= op + it.next();
        }
        return res;
    }

    // print sequence of Exp
    String printInfix2(List<Exp> l, String op) throws Exception {
        String res="";
        if (l.isEmpty()) {
            return res;
        }
        Iterator<Exp> it = l.iterator();
        res+=it.next().accept(this);
        while (it.hasNext()) {
            res+=op;
            res+=it.next().accept(this);
        }
        return res;
    }

    public String visit(LetRec e) throws Exception {
        String res="(let rec " + e.fd.id + " ";
        res+=printInfix(e.fd.args, " ");
        res+=" = ";
        res+=e.fd.e.accept(this);
        res+=" in ";
        res+=e.e.accept(this);
        return res+")";
    }

    public String visit(App e) throws Exception {
        String res="(";
        res+=e.e.accept(this);
        res+=" ";
        res+=printInfix2(e.es, " ");
        return res+")";
    }

    public String visit(Tuple e) throws Exception {
        String res="(";
        res+=printInfix2(e.es, ", ");
        return res+")";
    }

    public String visit(LetTuple e) throws Exception {
        String res="(let (";
        res+=printInfix(e.ids, ", ");
        res+=") = ";
        res+=e.e1.accept(this);
        res+=" in ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(Array e) throws Exception {
        String res="(Array.create ";
        res+=e.e1.accept(this);
        res+=" ";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(Get e) throws Exception {
        String res=e.e1.accept(this);
        res+=".(";
        res+=e.e2.accept(this);
        return res+")";
    }

    public String visit(Put e) throws Exception {
        String res="(";
        res+=e.e1.accept(this);
        res+=".(";
        res+=e.e2.accept(this);
        res+=") <- ";
        res+=e.e3.accept(this);
        return res+")";
    }
}


