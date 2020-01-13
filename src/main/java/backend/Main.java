package backend;

import java.io.*;
import frontend.*;

public class Main {
  static public void main(String argv[]) {    
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);
      expression.accept(new PrintVisitor());

      ARMWriter writer = new ARMWriter("output.s", ARMVerboseLevel.ALL);
      writer.fileHeader();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

