# Template method pattern example

```java
public abstract class HouseBuilder {

    // TemplateMethod
    public void build() {
        System.out.println("[Default] Ready to build house, let's get started!");

        makeWall();
        installDoor();
        installRoof();
        hangWallPaper();
        installWindows(); 
        specialInterior(); // hook

        System.out.println("[Default] All done. What a nice house!");
    }

    private void installDoor() {
        System.out.println("[Default] Installing doors. Every house has 2 doors only!");
    }

    private void installRoof() {
        System.out.println("[Default] Installing roof. Every house has paper roof 😁");
    }

    public abstract void makeWall();
    public abstract void hangWallPaper();
    public abstract void installWindows();

    // Hook Method
    public void specialInterior() {
//        System.out.println("[Default, Hook] I'm not doing any special interior"); // Optional
    }
}

// Concrete class (with hook)
public class PaperHouseBuilder extends HouseBuilder {

    @Override
    public void makeWall() {
        System.out.println("[PaperHouseBuilder] This house's wall is made of paper📄");
    }

    @Override
    public void hangWallPaper() {
        System.out.println("[PaperHouseBuilder] The wallpaper is of course paper, blue though 🟦️");
    }

    @Override
    public void installWindows() {
        System.out.println("[PaperHouseBuilder] Windows? Of course paper 📄");
    }

    @Override
    public void specialInterior() {
        System.out.println("[PaperHouseBuilder, Hook] I want to put a fireplace in my living room. Let's do it!!");
    }
}

// Concrete class (without hook)
public class BrickHouseBuilder extends HouseBuilder {
    @Override
    public void makeWall() {
        System.out.println("[BrickHouseBuilder] This is the house I want, the wall is made of bricks 🧱");
    }

    @Override
    public void hangWallPaper() {
        System.out.println("[BrickHouseBuilder] I like to feel cozy, wall paper's color is brown 🟫");
    }

    @Override
    public void installWindows() {
        System.out.println("[BrickHouseBuilder] 🪟🪟🪟🪟");
    }
}

public class Client {
    public static void main(String[] args) {
        HouseBuilder brickHouse = new BrickHouseBuilder();
        HouseBuilder paperHouse = new PaperHouseBuilder();

        System.out.println("------------ Building BrickHouse ------------");
        brickHouse.build();
        System.out.println("------------ Building BrickHouse has been built ------------\n");

        System.out.println("------------ Building PaperHouse ------------");
        paperHouse.build();
        System.out.println("------------ Building PaperHouse has been built ------------");
    }
}

/*
------------ Building BrickHouse ------------
[Default] Ready to build house, let's get started!
[BrickHouseBuilder] This is the house I want, the wall is made of bricks 🧱
[Default] Installing doors. Every house has 2 doors only!
[Default] Installing roof. Every house has paper roof 😁
[BrickHouseBuilder] I like to feel cozy, wall paper's color is brown 🟫
[BrickHouseBuilder] 🪟🪟🪟🪟
[Default] All done. What a nice house!
------------ Building BrickHouse has been built ------------

------------ Building PaperHouse ------------
[Default] Ready to build house, let's get started!
[PaperHouseBuilder] This house's wall is made of paper📄
[Default] Installing doors. Every house has 2 doors only!
[Default] Installing roof. Every house has paper roof 😁
[PaperHouseBuilder] The wallpaper is of course paper, blue though 🟦️
[PaperHouseBuilder] Windows? Of course paper 📄
[PaperHouseBuilder, Hook] I want to put a fireplace in my living room. Let's do it!!
[Default] All done. What a nice house!
------------ Building PaperHouse has been built ------------
*/

```