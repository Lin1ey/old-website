import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;  // Import the Scanner class
import java.util.spi.ToolProvider;

class ScalaH {

    private static String name;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("format is: ScalaH <scala.classfile>\n");
        }
        
        name = args[0].split("\\.")[0];
        
        
        //create javapOutput.dsk
        ToolProvider javap = ToolProvider.findFirst("javap").orElseThrow();

        File javapOutput = new File("javapOutput.dsk");
        PrintWriter out = null;
        try {
            out = new PrintWriter(javapOutput);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        //run javap into the .dsk file
        javap.run(out, out, args[0]);
        try {
            out.close();
        } catch (Exception e) {
        }
        

        
        //make new java source file with native functions only from javap output
        
        File nativeJava = new File(name + ".java");
        FileWriter fileWriter = null;
        Scanner scanner = null;
        try {
            nativeJava.createNewFile(); //create file if doesn't exist
            fileWriter = new FileWriter(nativeJava);
            scanner = new Scanner(javapOutput);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
        

        //skip "compiled from x" line and set up class
        scanner.nextLine();
        try {
            String line = scanner.nextLine();
            fileWriter.write(line);
            fileWriter.write("\n");
        } catch (Exception e) {
        }

        
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine().trim();
            String[] split = nextLine.split(" ");

            if (split.length > 1 && split[1].equalsIgnoreCase("native")) {
                try {
                    String first = nextLine.replaceAll("java.lang.String", "String");
                    String[] paramSplit = first.split("\\(");
                    String[] param = first.substring(first.indexOf("(")+1, first.indexOf(")")).split(",");
                    fileWriter.write("\t");
                    fileWriter.write(paramSplit[0]);
                    fileWriter.write("(");
                    for (int i = 0; i < param.length; i++) {
                        fileWriter.write(param[i].trim() + " a" + i);
                        if (i != param.length - 1) {
                            fileWriter.write(", ");
                        }

                   }
                    fileWriter.write(");\n");
                } catch (Exception e) {
                    System.out.println(e);
                    System.exit(1);
                }
            }
            
        }

        try {
            fileWriter.write("}");
            fileWriter.close();
            scanner.close();
        } catch (Exception e) {

        }
        
        
        File javacOutput = new File(name + ".h");
        PrintWriter javacOut = null;
        try {
            javacOutput.createNewFile();
            javacOut = new PrintWriter(javacOutput);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        ToolProvider javac = ToolProvider.findFirst("javac").get();
        String[] javacArgs = {"-h", ".", name + ".java"};
        javac.run(javacOut, javacOut, javacArgs);

        //need to remove javapOutput.dsk and nativeJava
        //javapOutput.delete();
        //nativeJava.delete();
        
        try {
            javacOut.close();
        } catch (Exception e) {
        }

    }

}

