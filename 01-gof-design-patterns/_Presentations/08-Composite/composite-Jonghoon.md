# Composite Pattern

## 1. Composite Pattern

### 1.1 Definition:
* Composite(n):  a thing made up of several parts or elements.
* 복합 설계(Composite design): 복잡한 시스템을 기능단위가 작은 모듈로 분할하는 Design pattern.

### 1.2 Intent - why Composite?
- The intent of a composite is to "compose" objects into tree structures to represent part-whole hierarchies
- Example: File systems - a file and a folder are types of data (parent abstract class), but a folder can contain many files and folders

![image](https://user-images.githubusercontent.com/77429796/192131337-0698b640-1c01-4acc-a3c9-8a8bf15dde86.png)

### 1.3 When to use?
- Motivation
  * When dealing with Tree-structured data, programmers often have to discriminate between a leaf-node and a branch. This makes code more complex, 
    and therefore, more error prone. The solution is an interface that allows treating complex and primitive objects uniformly
  * A part-whole hierarchy should be represented so that clients can treat part and whole objects uniformly.
  * A part-whole hierarchy should be represented as tree structure.
  * Clients don’t need to distinguish between the nodes and leaves of the tree

## 2. Implementation
- UML
  * ![image](https://user-images.githubusercontent.com/77429796/192131467-fd0edd0b-247c-44d6-8138-3a63c04735cb.png)
- Component
  * is the abstraction for all components, including composite ones
  * declares the interface for objects in the composition
- Leaf
  * represents leaf objects in the composition
  * implements all Component methods
- Composite
  * represents a composite Component (component having children)
  * implements methods to manipulate children
  * implements all Component methods, generally by delegating them to its children

## 3. Example

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

## 4. Pros and Cons
- Pros
  * Great expandability
  * Easy to add additional composite and primitive classes
  * As long as these new classes follow the existing interface they will work with
any existing client code without the client code needing to change
- Cons
  * Implementation of component interfaces is very challenging
  * Because all classes in the hierarchy must follow the abstract interface it can lead to overly general classes. 
  * For example you cannot restrict the children of a certain composition class at compile time and instead must rely on runtime checks to achieve this behaviour

