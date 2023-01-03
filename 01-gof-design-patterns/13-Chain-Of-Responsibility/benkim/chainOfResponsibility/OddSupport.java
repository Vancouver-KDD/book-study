package leetcode.chainOfResponsibility;

public class OddSupport extends Support {

    public OddSupport(String name) {
        super(name);
    }

    // 홀수 번호라면 해결 가능
    protected boolean resolve(Trouble trouble) {
        if (trouble.getNumber() % 2 == 1) {
            return true;
        } else {
            return false;
        }
    }
}