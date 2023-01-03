# Factory Method 예제

### Component.java
```Java
abstract class Component {
    protected abstract String getCompName();
    public Component() {
        System.out.println(this.getCompName() + " created");
    }
}

class Button extends Component {
    @Override
    protected String getCompName() {
        return "Button";
    }
}

class Switch extends Component {
    @Override
    protected String getCompName() {
        return "Switch";
    }
}

class Dropdown extends Component {
    @Override
    protected String getCompName() {
        return "Dropdown";
    }
}
```

### CompFactory.java
```Java
class CompFactory {
    public Component getComp (Usage usage) {
        switch (usage) {
            case Usage.PRESS: return new Button();
            case Usage.TOGGLE: return new Switch();
            case Usage.EXPAND: return new Dropdown();
        }
    }
}
```

### FactoryMethod.java
```Java
class FactoryMethod {
    public static void main(String[] args) {
        new Console().withoutFactory();
        new Console().withFactory();
    }
}
```

### Console.java
```Java
class Console {

    private CompFactory compFactory = new CompFactory();

    Component com1;
    Component com2;
    Component com3;

    void withouthFactory() {
        com1 = new Button();
        com2 = new Switch();
        com3 = new Dropdown();
    }

    void withFactory() {
        com1 = compFactory.getComp(Usage.PRESS);
        com2 = compFactory.getComp(Usage.TOGGLE);
        com3 = compFactory.getComp(Usage.EXPAND);
    }
}

enum Usage {
    PRESS, TOGGLE, EXPAND
}
```

### Resources:
https://www.youtube.com/watch?v=q3_WXP9pPUQ&ab_channel=%EC%96%84%ED%8C%8D%ED%95%9C%EC%BD%94%EB%94%A9%EC%82%AC%EC%A0%84
