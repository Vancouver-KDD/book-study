# MediatorPattern
한클래스에서의 이벤트가 연결된 다른 클래스의 객체에 영향을 미칠때 mediator패턴을 사용할 수 있다

```java
public class MediatorPattern {
    public static void main(String[] args) {
  
      ModeSwitch modeSwitch = new ModeSwitch();
      ModeMediator modeMediator = new ModeMediator();
  
      modeSwitch.setModeMediator(modeMediator);
  
      modeMediator.addListener(new ListView());
      modeMediator.addListener(new GalleryView());
      modeMediator.addListener(new DataDownloader());
  
      modeSwitch.toggleMode();
      // 리스트뷰 감춤
      // 갤러리뷰 보여줌
      // 갤러리뷰용 데이터 다운로드
  
      modeSwitch.toggleMode();
      // 리스트뷰 보여줌
      // 갤러리뷰 감춤
      // 리스트뷰용 데이터 다운로드
    }
  }

public class ModeSwitch {
Mode mode = Mode.LIST;  

ModeMediator modeMediator;

public void setModeMediator (ModeMediator _modeMediator) {
    modeMediator = _modeMediator;
}

public void toggleMode () {
    mode = mode == Mode.LIST ? Mode.GALLERY : Mode.LIST;

    if (modeMediator != null) {
    modeMediator.onModeChange(mode);
    }
}
}

enum Mode { LIST, GALLERY }

public interface ModeListener {
  public void onModeChange (Mode mode);
}

class ListView implements ModeListener {
  @Override
  public void onModeChange(Mode mode) {
    System.out.println(
      "리스트뷰 " + (mode == Mode.LIST ? "보여줌" : "감춤")
      );
  }
}

class GalleryView implements ModeListener {
  @Override
  public void onModeChange(Mode mode) {
    System.out.println(
      "갤러리뷰 " + (mode == Mode.GALLERY ? "보여줌" : "감춤")
      );
  }
}
  
class DataDownloader implements ModeListener {
  @Override
  public void onModeChange(Mode mode) {
    System.out.println(
      (mode == Mode.LIST ? "리스트" : "갤러리")
    + "뷰용 데이터 다운로드");
  }
}

import java.util.ArrayList;

public class ModeMediator {
  ArrayList<ModeListener> listeners = new ArrayList<>();

  public void addListener(ModeListener listener) {
    listeners.add(listener);
  }
  public void onModeChange (Mode mode) {
    for (ModeListener listener : listeners) {
      listener.onModeChange(mode);
    }
  }
}
```