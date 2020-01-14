package backend;

import java.io.*;

public class Main {
  static public void main(String argv[]) {
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);

      System.out.println(expression.accept(new CodeGenerationVisitor()));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
