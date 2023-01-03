# Factory Method 예제

기본 팩토리를 추상 클래스나 인터페이스로 하나 만들고, 
이를 상속한, 테마마다의 팩토리를 둬서,
처음부터 어떤 팩토리를 세우냐에 따라 찍혀나오는 요소들의 테마가 달라지도록한다.


### CompFactory.java
```Java
interface CompFactory {
    public Component getComp (Usage usage) {
    }
}

class LightCompFactory implements CompFactory {

    @override
    public Component getComp (Usage usage) {
        if (usage == Usage.PRESS) {
            return new LightButton();
        } else if (usage == Usage.TOGGLE) {
            return new LightSwitch();
        } else {
            return new LightDropdown();
        }
    }
}

class DarkCompFactory implements CompFactory {

    @override
    public Component getComp (Usage usage) {
        if (usage == Usage.PRESS) {
            return new DarkButton();
        } else if (usage == Usage.TOGGLE) {
            return new DarkSwitch();
        } else {
            return new DarkDropdown();
        }
    }
}
```


### Console.java
```Java
class Console {

    private CompFactory lightCompFactory = new LightCompFactory();
    private CompFactory darkCompFactory = new DarkCompFactory();
    
    Component com1;
    Component com2;
    Component com3;

    void withFactory() {
        com1 = lightCompFactory.getComp(Usage.PRESS);
        com2 = lightCompFactory.getComp(Usage.TOGGLE);
        com3 = lightCompFactory.getComp(Usage.EXPAND);

        com1 = darkCompFactory.getComp(Usage.PRESS);
        com2 = darkCompFactory.getComp(Usage.TOGGLE);
        com3 = darkCompFactory.getComp(Usage.EXPAND);
    }
}

enum Usage {
    PRESS, TOGGLE, EXPAND
}
```

### Resources:
https://www.youtube.com/watch?v=q3_WXP9pPUQ&ab_channel=%EC%96%84%ED%8C%8D%ED%95%9C%EC%BD%94%EB%94%A9%EC%82%AC%EC%A0%84
