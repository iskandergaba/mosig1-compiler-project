package backend;

import java_cup.runtime.*;
import java.io.*;
<<<<<<< HEAD
import java.util.*;
import frontend.*;
=======
>>>>>>> First version of ARM code generation

public class Main {
  static public void main(String argv[]) {
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);
<<<<<<< HEAD
      // expression.accept(new PrintVisitor());
      expression.accept(new RegAllocVisitor());
=======

      System.out.println("# Register allocation");
      RegAllocVisitor v = new RegAllocVisitor();
      expression.accept(v);

      System.out.println("# ARM generation");
      System.out.println(expression.accept(new ARMVisitor(v.memory)));

>>>>>>> First version of ARM code generation
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
