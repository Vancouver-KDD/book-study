package designpattern.interpreterPattern;

import java.io.BufferedReader;
import java.io.FileReader;


// interpreter 패턴은 어떠한 형식으로 쓰여진 파일의 내용을 통역하는 역할을 하는 프로그램 표현방식이다
public class InterpreterPattern {

    public class Main {
        public static void main(String[] args) {
            ClassLoader loader = Main.class.getClassLoader();
            String file = loader.getResource("interpreter/program.txt").getFile();
    
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String text;
                while ((text = reader.readLine()) != null) {
                    System.out.println("text = \"" + text + "\"");
                    Node node = new ProgramNode();
                    node.parse(new Context(text));
                    System.out.println("node = " + node);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}