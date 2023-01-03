# Speculative Generality

##### Collapse Hierarchy
- 클래스 hierarchy에 대한 리팩토링을 진행하면서 parent와 child의 관계를 나눠 놓은 것이 더 이상 worth하지 않다고 생각이 들면 merge하도록 한다
```java
  // before
  public class Parent { ... }
  public class ChildButNotWorth { ... }

  // after
  public class Parent { ... }
  // or
  public class NewName { ... }
```

##### Remove Dead Code
- 쓰지 않는 코드는 바로 지워주는 것이 좋다
- 혹여나 나중에 쓰여질 것 같은 코드여도 지금 당장 쓰지 않는다면 version control system을 믿고 지우도록 하자
