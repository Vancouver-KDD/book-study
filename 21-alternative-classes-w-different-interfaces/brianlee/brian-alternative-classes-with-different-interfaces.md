# Alternative Classes with Different Interfaces
* 비슷한 기능을 가진 여러 곳에 여러 인터페이스들로 나누어져 있을 때 이를 한곳으로 모아야 한다.
* 기존 인터페이스 변경 없이 인터페이스 합치기
  1. 중복 기능을 가진 새로운 인터페이스 생성
  2. 흩어져있는 기능을 이 새로운 인터페이스로 다시 구현 하여 사용

```java
public interface EmailService {
  void send(Email email);
}

public interface AlertService {
  void add(Alert alert);
}

public interface NewNotificationService {
  void sendNoti(Noti noti);
}

public class EmailNotiService implements NewNotificationService {
  private EmailService emailService;

  @Override
  public void sendNoti(Noti noti) {
      Email email = new Email(noti.title());
      email.to(noti.getReceiver());
      ...
      emailService.send(email);
  }
}

public class AlertNotiService implements NewNotificationService {
  private AlertService alertService;

  @Override
  public void sendNoti(Noti noti) {
      Alert alert = new Alert(noti.title());
      alert.setFor(noti.getReceiver());
      ...
      alertService.add(alert);
  }
}
```

## Change Function Declaration (#Mysterious Name)
## Move Function (#Divergent Change)
## Extract Superclass (#Large Class)
* 중복된 기능으로 새로운 상위 클래스를 만들고 그 하위 클래스로 나머지 기능들을 넣는다 