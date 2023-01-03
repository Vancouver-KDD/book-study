public class MyProgram {
    public static void main(final String[] args) {
        
        final ModeSwitch modeSwitch = new ModeSwitch();

        modeSwitch.onSwitch();
        modeSwitch.onSwitch();
        modeSwitch.onSwitch();
        modeSwitch.onSwitch();
    }
}

public class ModeSwitch {
    private ModeState modeState = new ModeStateLight();

    public void setState(ModeState _modeState) {
        modeState = _modeState;
    }

    public void onSwitch () {
        modeState.toggle(this);
    }

}


public interface ModeState {
    public void toggle (ModeSwitch modeSwitch);
}

class ModeStateLight implements ModeState {
    public void toggle(ModeSwitch modeSwitch) {
        System.out.println("FROM LIGHT TO DARK");
        // 화면을 어둡게 하는 코드
        modeSwitch.setState(new ModeStateDark());
    }
} 

class ModeStateDark implements ModeState {
    public void toggle(ModeSwitch modeSwitch) {
        System.out.println("FROM DARK TO LIGHT");
        // 화면을 밝게 하는 코드
        modeSwitch.setState(new ModeStateLight());
    }
} 