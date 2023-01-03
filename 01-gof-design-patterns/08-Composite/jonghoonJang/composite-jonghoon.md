### Composite patter

- 컴퓨터의 폴더 시스템(폴더, 파일)
  폴더, 파일 둘다 이름바꾸기, 삭제하기, 복사하기등 같은 기능을 가지고 있음 
  포함하는 것들과 포함되는 것들이 같은 방식으로 다뤄질 때
 
 #### FileSystem.java
```Java
public interface FileSystem {
    int getSize();
    void remove();
}
```

 #### Folder.java
```Java
import java.util.ArrayList;

class Folder implements FileSystem {
    private String name;
    private ArrayList<FileSystem> files = new ArrayList<FileSystem>();

    public Folder(String name) {
        this.name = name;
    }

    public void add(FileSystem file) {
        files.add(file);
    }

    @Override
    public int getSize() {
        int size = 0;
        for (FileSystem file : files) {
            size += file.getSize();
        }
        System.out.println(name + " size:" + size );
        return size;
    }

    @Override
    public void remove() {
        for (FileSystem file : files) {
            file.remove();
        }
        System.out.println("Removing folder " + name);
    }
}
```

 #### File.java
```Java
class File implements FileSystem {
    private String name;
    private int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public int getSize() {
        System.out.println(name + " size:" + size );
        return size;
    }

    @Override
    public void remove() {
        System.out.println("Removing file " + name);
    }
}
```

 #### Client.java
```Java
public class Client {

    public static void main(String[] args) {
        Folder schoolFolder = new Folder("School");

        Folder mathFolder = new Folder("Math");
        Folder englishFolder = new Folder("English");

        schoolFolder.add(mathFolder);
        schoolFolder.add(englishFolder);

        File mathFile = new File("math_constants", 100);
        mathFolder.add(mathFile);

        Folder calculus1Folder = new Folder("Calculus1");
        Folder calculus2Folder = new Folder("Calculus2");

        mathFolder.add(calculus1Folder);
        mathFolder.add(calculus2Folder);

        File calculus1File = new File("Calculus1_notes", 200);
        calculus1Folder.add(calculus1File);
        File calculus2File = new File("Calculus2_notes", 300);
        calculus2Folder.add(calculus2File);

        Folder basicEnglishFolder = new Folder("BasicEnglish");
        englishFolder.add(basicEnglishFolder);
        Folder grammarFolder = new Folder("Grammar");
        basicEnglishFolder.add(grammarFolder);

        File verbNotes = new File("verb_notes", 400);
        grammarFolder.add(verbNotes);

        schoolFolder.getSize();
        schoolFolder.remove();
    }
}

```

