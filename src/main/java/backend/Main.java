package backend;

import java.io.*;
import frontend.*;

public class Main {
  static public void main(String argv[]) {
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);
      RegAllocVisitor v = new RegAllocVisitor();
      expression.accept(v);

      System.out.println(expression.accept(new ARMVisitor(v.memory)));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
