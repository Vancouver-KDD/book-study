# Lazy element 

## Quick preview
재사용이나 확장성을 위해 메서드 나 클래스를 생성했지만 실질적인 역할을 많이 하지 않는 경우가 있다.   
예를 들어, 메서드 내부 로직이 너무 단순해서 굳이 따로 빼는 의미가 없는 경우나 클래스 내에 실질적인 역할을 하는 메서드가 하나 밖에 없는 경우가 있다.  
이런 경우는 inline function 이나 inline class 통해서 불필요한 건 없애는 방향으로 리팩토링한다.    
inheritance 이용한 경우는 Collapse Hierarchy 통해 제거한다.      

## Collapse Hierarchy

### Motivation
parent - child 간에 의미없는 차이가 없다고 보면 그냥 합쳐버린다.   

### Mechanics 
- 어느 요소를 없앨지 고른다.  
- parent class 없앤다면 push down field, push down method 이용 /  child class 없애면 pull up field, pull down method 사용 
- 제거할 class 를 참조하던게 있다면 남길 class 참조하도록 수정한다. 
- 빈 클래스 없앤다.
- 테스트한다.

### Example

```java
// parent class 없애는 경우 
// 여러 국가의 메뉴를 할 생각이었으나 (확장성 고려)  그럴 필요 없어져 parent class 의 의미가 사라졌다.  
class Menu {
    private String name;
    private double price;
    //...getter, setter
}

class KoreanRestaurant extends Menu {
    private String type = "Korean";
    ...
}

//after 
class KoreanRestaurant {
    private String name;
    private double price;
    private String type = "Korean";

    public String getName() {
        return name;
    }
    //...
}
```

```java
// child class를 없애는 경우 
// child class 가 하는 일이 너무 미미해서 따로 떨어져 있어야할 필요가 없다. 
// before
class Semester {
    private String type; 
    private int duration; 
    ...
}

class SummerSession extends Semester{
    private boolean isMandatory = false; 
}

// after
class Semester {
    private String type;
    private int duration;
    private boolean isMandatory;
    ...
}
```

