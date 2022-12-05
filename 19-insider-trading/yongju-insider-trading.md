# Insider Trading

Modules (classes or objects) 사이에서 일어나는 data transfer는 coupling을 증가시키기 때문에 프로그래머들이 상당한 주의를 기울여 피하려 한다.

기능적으로 function이나 field가 분리되어야 한다면 `Move function`이나 `Move field`를 사용할 수 있다.\
여러 모듈이 공통적인 부분을 가지고 있다면 새로운 모듈을 만들어 그 안에 공통적인 부분을 넣어주거나 `Hide Delegate`를 사용해서 역시 다른 모듈을 만들어 이용할 수 있다.\
Inheritance를 사용할 때에도 parent-child 간에 필요 외로 데이터가 오간다면 `Replace Subclass with Delegate` 혹은 `Replace Superclass with Delegate`를 이용해 리팩토링을 진행할 수 있다.

```java
  // Before
  public class Alarm {
    private List<Time> alarmTimes;
    private boolean isGPUOverheated;
    private boolean isCPUOverheated;
    private boolean doesMessageComeIn;

    public void ring() {
      ...
    }

    public void turnOff() {
      ...
    }

    public void ringWhenGPUoverheated() { ... }
    public void ringWhenCPUoverheated() { ... }
    public void ringWhenMessageCameIn() { ... }
  }

  // After
  public class Alarm {
    ...
  }

  public class GPUAlarm extends Alarm {
    ...
  }

  public class CPUAlarm extends Alarm {
    ...
  }

  public class MessageAlarm extends Alarm {
    ...
  }
```