package frontend;

import java.io.*;

public class Main {
  static public void main(String argv[]) {
    try {

      FileReader file = null;

      byte terminate_point = 0;

      // Command Line Arguments
      if (argv.length == 0) {
        throw new Exception();
      } else if (argv.length == 1) {

        // Case for direct compilation
        if (argv[0].endsWith(".ml")) {
          file = new FileReader(argv[0]);
        }

        // Check if 1st arg is a flag
        if (argv[0].startsWith("-")) {
          char c = argv[0].charAt(1);
          switch (c) {
          case 'h':
            System.out.println(
                "Options :\n-o : output file\n-h : display help\n-v : display version\n-t : type check only\n-p : parse only\n-asml : output ASML");
            throw new Exception();
          case 'v':
            System.out.println("mincamlc 1.0");
            throw new Exception();
          case 'o':
          case 't':
          case 'p':
          case 'a':
            System.out.println("Error : Missing filename after '" + argv[0] + "'");
            throw new Exception();
          default:
            System.out.println("Error : Invalid flag: " + argv[0]);
            throw new Exception();
          }
        }

        // Check if invalid extension
        if (!argv[0].endsWith(".ml")) {
          System.out.println("Error: Only .ml file extensions supported");
          throw new Exception();
        }

      } else if (argv.length == 2) {

        // Check if 1st arg is a flag
        if (argv[0].startsWith("-")) {
          char c = argv[0].charAt(1);
          switch (c) {
          case 't':
            terminate_point = 2;
            break;
          case 'p':
            terminate_point = 1;
            break;
          case 'a':
            // Output ASML
            break;
          case 'o':
            System.out.println("Error : No input files");
            throw new Exception();
          default:
            System.out.println("Error : Invalid flag: " + argv[0]);
            throw new Exception();
          }
          file = new FileReader(argv[1]);
        }
      } else if (argv.length == 3) {

        // Check if 2nd arg is a flag
        if (argv[1].startsWith("-")) {
          char c = argv[1].charAt(1);
          switch (c) {
          case 'o':
            // Output to a specific file
            break;
          case 'h':
          case 'v':
          case 't':
          case 'p':
          case 'a':
            System.out.println("Error : Too many arguments ");
            throw new Exception();
          default:
            System.out.println("Error : Invalid flag: " + argv[0]);
            throw new Exception();
          }
        }
        // Check if 1st arg is a flag
        else if (argv[0].startsWith("-")) {
          System.out.println("Error : Too many arguments");
          throw new Exception();
        }
      } else if (argv.length > 3) {
        System.out.println("Error : Too many arguments");
        throw new Exception();
      }

      Parser p = new Parser(new Lexer(file));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);

      System.out.println("------ AST ------");
      expression.accept(new PrintVisitor());
      System.out.println();

      // End at Parsing
      assert(terminate_point != 1);

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

      // End at Typing
      assert(terminate_point != 2);

    } catch (TypingException e) {
      System.out.print("(TYPING ERROR) ");
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      System.out.println("Error: file not found: " + argv[0]);
    } catch (AssertionError e) {
      System.out.print("");
    }catch (Exception e) {
      System.out.println("Usage: ./mincamlc <options> <source files>");
      System.out.println("Compilation terminated");
    }
  }
}
