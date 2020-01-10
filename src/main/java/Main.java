import java_cup.runtime.*;
import java.io.*;
import java.util.*;

public class Main {
  static public void main(String argv[]) {    
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;      
      assert (expression != null);

      System.out.println("------ AST ------");
      expression.accept(new PrintVisitor());
      System.out.println();

      ObjVisitor<Integer> v1 = new HeightVisitor();
      int height = expression.accept(v1);
      System.out.println("using HeightVisitor: " + height);

      System.out.println("------ Type visiting ----");
      ObjVisitor<Type> v2 = new TypeVisitor();
      Type result = expression.accept(v2);
      if(result !=null) {
        System.out.println("YEAAAAAAAAAAAH!");
      } else if (result == null) {
        System.out.println("Fuck UGA");
      } else {
        System.out.println("Fuck UGA anyways");
        System.out.println(result.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

