import java.io.*;
import java.util.*;

public class Runner {

    private static String data;

    private static String className;
    private static ArrayList<String> imports;
    private static ArrayList<String> vars;
    private static ArrayList<String> conVars;
    private static ArrayList<String> funcs;
    private static ArrayList<String> cons;
    private static int lc;

    public static void main(String[] args) {

        data = "";

        className = "";
        imports = new ArrayList<String>();
        vars = new ArrayList<String>();
        conVars = new ArrayList<String>();
        funcs = new ArrayList<String>();
        cons = new ArrayList<String>();
        
        String line = "";
        String defArg = " ";
        String defType = ",";
        String defData = " ";

        //TODO
        //automatic get() and set() for private variables
        //custom pathing
        //special cases: screen (multiple types? i.e. actionlistener), runner, comparable, etc?
        //options? maybe choose if private variables should have get, set, both, or none?
        //maybe change categorization? i.e. specific places to put data
        //polish
        
        try {

            BufferedReader br = new BufferedReader(new FileReader("./input.txt"));
            className = br.readLine();

            lc = 1;

            while ((line = br.readLine()) != null) {
                lc++;

                if(line.equals("") || (line.charAt(0) == '/' && line.charAt(1) == '/')) continue;

                String arg = line.split(defArg)[0];

                line = line.substring(arg.length());

                String[] split = line.split(defType);

                if(arg.equals("imp")) { //imports
                    for(int i=0; i<split.length; i++) {
                        if(split[i].charAt(0) == ' ') split[i] = split[i].substring(1);
                        imports.add("import java." + split[i] + ";");
                    }
                } else if(arg.equals("var")) { //variables
                    for(int i=0; i<split.length; i++) {
                        if(split[i].charAt(0) == ' ') split[i] = split[i].substring(1);
                        String[] data = split[i].split(defData);
                        char access = data[0].charAt(0);
                        if(access == '-') {
                            data[0] = data[0].substring(1);
                            vars.add("private " + data[0] + " " + data[1] + ";");
                        } else if(access == '+') {
                            data[0] = data[0].substring(1);
                            vars.add("public " + data[0] + " " + data[1] + ";");
                        } else {
                            vars.add("public " + data[0] + " " + data[1] + ";");
                        }
                        if(data.length < 3) continue;
                        if(data[2].equals("c")) {
                            conVars.add(data[0] + " " + data[1]);
                        }
                    }
                } else if(arg.equals("func")) { //functions
                    if(line.charAt(0) == ' ') line = line.substring(1);
                    String[] data = line.split(defData);
                    char access = data[0].charAt(0);
                    if(access == '-') {
                        data[0] = data[0].substring(1);
                        String params = line.substring(data[0].length() + data[1].length() + 2);
                        if(params.length() != 0 && params.charAt(0) == ' ') {
                            params = params.substring(1);
                        } else {
                            params = "";
                        }
                        funcs.add("private " + data[0] + " " + data[1] + "(" + params + ") {}");
                    } else if(access == '+') {
                        data[0] = data[0].substring(1);
                        String params = line.substring(data[0].length() + data[1].length() + 2);
                        if(params.length() != 0 && params.charAt(0) == ' ') {
                            params = params.substring(1);
                        } else {
                            params = "";
                        }
                        funcs.add("public " + data[0] + " " + data[1] + "(" + params + ") {}");
                    } else {
                        String params = line.substring(data[0].length() + data[1].length() + 1); //idk why this is +1 not +2
                        if(params.length() != 0 && params.charAt(0) == ' ') {
                            params = params.substring(1);
                        } else {
                            params = "";
                        }
                        funcs.add(data[0] + " " + data[1] + "(" + params + ") {}");
                    }
                } else if(arg.equals("con")) {
                    if(line.charAt(0) == ' ') line = line.substring(1);
                    cons.add("public " + className + "(" + line + ")" + "{}"); //automatically instatiate?
                }
            }

            String defaultConstructor = "public " + className + "(";
            for(int i=0; i<conVars.size(); i++) {
                defaultConstructor += conVars.get(i);
                if(i < conVars.size()-1) defaultConstructor += ", ";
            }
            defaultConstructor += ") {}";

            br.close();

            for(int i=0; i<imports.size(); i++) {
                data += imports.get(i) + "\n";
            }

            data += "\npublic class " + className + " {\n"; //remember to close the bracket and account for implements/extends

            for(int i=0; i<vars.size(); i++) {
                data += "\n\t" + vars.get(i);
            }

            data += "\n\n\t" + defaultConstructor;

            for(int i=0; i<cons.size(); i++) {
                data += "\n\n\t" + cons.get(i);
            }

            for(int i=0; i<funcs.size(); i++) {
                data += "\n\n\t" + funcs.get(i);
            }

            data += "\n\n";

            data += "}";

            FileWriter fw = new FileWriter(className + ".java", false); //figure out pathing here?
            fw.write(data);
            fw.close();

        } catch (IOException e) { System.out.println(e.getClass() + "\n" + e.getMessage() + "\n\nError reading line " + lc); }

    }
}