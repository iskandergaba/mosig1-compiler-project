package backend;

import java.io.*;

public class Main {
  static public void main(String argv[]) {
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);

      InstructionBlock text = expression.accept(new CodeGenerationVisitor());
      Program prog = new Program(text);
      prog.generateHeapAllocationCode();
      System.out.println(prog);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
