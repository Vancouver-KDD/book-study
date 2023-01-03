# Global Data

- Global data는 어떤 코드 안에서든지 변경할 수 있고, 어떤 코드가 해당 data를 access하는 지 찾기 어려운 단점이 있다

- 흔하게는 global variable 형태이지만, class variable이나 singleton 형태도 있다

### Encapsulate Variable

Function을 리팩토링 할 때는 쉽게 이름을 변경하거나 옮길 수 있지만 data는 그렇지 않다. 한 function안에 있는 temporary variable이라면 문제가 없겠지만 scope가 커질 수록 특히, global data는 리팩토링하기 쉽지 않다

```java 
  // before
  public class ShouldNotRevealVariables {
    public boolean isRevealed;
    public String  status;
  }

  public static void main(String[] args) {
    ShouldNotRevealVariables snrv = new ShouldNotRevealVariables();
    System.out.println(snrv.isRevealed);
    System.out.println(snrv.status);
  }

  // after
  public class ShouldNotRevealVariables {
    private boolean isRevealed;
    private String  status;

    public boolean getIsRevealed() {
      return this.isRevealed;
    }

    public String getStatus() {
      return this.status;
    }

    public void setIsRevealed(boolean isRevealed) {
      this.isRevealed = isRevealed;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }

  public static void main(String[] args) {
    ShouldNotRevealVariables snrv = new ShouldNotRevealVariables();
    
    // not allowed
    System.out.println(snrv.isRevealed);
    System.out.println(snrv.information);

    // allowed
    snrv.setIsRevealed(false);
    snrv.setStatus("Good");

    System.out.println(snrv.getIsRevealed());
    System.out.println(snrv.getStatus());
  }

```
