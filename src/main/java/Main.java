import java.io.*;

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
      System.out.println("------ Type checking ------");
      ObjVisitor<Type> v2 = new TypeVisitor();
      Type result = expression.accept(v2);
      if (result != null) {
        System.out.println("------ Type checking DONE ------");
      } else {
        System.out.println("------ Typing error ------");
      }
    } catch (TypingException e) {
      System.out.print("(TYPING ERROR) ");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
