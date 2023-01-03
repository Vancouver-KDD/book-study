package designpattern.interpreterPattern.Iterator;

public class Main {
    public static void main(String[] args) {

        ClassRoom classRoom = new ClassRoom(4);
        classRoom.appendStudent(new Student("kim"));
        classRoom.appendStudent(new Student("lee"));
        classRoom.appendStudent(new Student("jung"));
        classRoom.appendStudent(new Student("ben"));

        Iterator iterator= classRoom.iterator();

        while (iterator.hasNext()) {
            Student student = (Student)iterator.next();
            System.out.println(student.getName());
        }
    }
}
