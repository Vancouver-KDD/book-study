# Global Data

### The difference between a poison and something benign is the does.
![](image/poisonVSmedicine.png)

* Global Data, Class Variables, Singletons 는 관리가 어렵다.
  * 아무곳에서나 변경 될 수 있음
  * 어떤 코드에 의해서 변경이 되었는지 파악이 어려움 

```java
class Example {
    public static int globalDataCount;
    public int classVariableCount;
}

class Singleton {
    private Singleton() {}
    
    // Singleton 의 다른 더 좋은 여러가지 구현방법이 있음
    private static Singleton instance = new Singleton();
    
    public static Singleton getInstance() {
        return instance;
    }
}

class AppClient {
    public static void main(String[] args) {
        GlobalDataExample.count++;
        Example example = new Example();
        example.classVariableCount++;
    } 
}
```

## Encapsulate Variable
### 변수를 함수화 하기(Getter/Setter)
* Variable 은 Refactoring 할때 한번에 모두 변경해야 한다
* Function 변경이 더 Variable 변경 보다 쉽다
  * Ex) We can easily rename or move a function while keeping a old function intact as a forwarding function.
* 데이터 Scape 가 클수록 Encapsulate 가 중요하다
* Function 은 검증 작업 혹은 후속 작업이 가능하다

```java
class Example {
    private static volatile int globalDataCount;
    private Notification notification;
    
    public static int increaseGlobalDataCount(int count) {
        // Verification
        if(count < 0) throw new IlligalArgumentException("The count must be grater then 0.");
        synchronized (this) {
            Example.globalDataCount += count;
        }
        // Notification
        if(globalDataCount > 100) {
            notification.send(globalDataCount);
        }
    }
}
```

### Immutable data
* **_Immutable data_** VS encapsulated data
* It doesn't need
  * a place to put in validation
  * or other logic hooks before updates.
* It can be copied rather than being moved.
  * No references from old location
  * No worry about sections of code getting stale data