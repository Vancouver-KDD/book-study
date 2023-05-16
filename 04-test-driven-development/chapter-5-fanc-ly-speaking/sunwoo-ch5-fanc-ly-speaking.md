# Ch 5. Fanc-ly Speaking

TDD에서 각각의 단계는 다른 목적들이 있습니다.
각각의 단계에는 목적에 맞는 다른 스타일의 해결책이 필요하고, 다른 방식으로 문제를 바라보아야 합니다.

처음 세 단계에서는 다음과 같이 `Dollar`에 존재하던 코드를 `Franc`으로 복제하여서,
빠르게 진행 할 수 있지만, 마지막 단계에서는 확실히 복제된 코드를 없애야 합니다.

```Java
public void testFrancMultiplication() {
    Franc five= new Franc(5);
    assertEquals(new Franc(10), five.times(2));
    assertEquals(new Franc(15), five.times(3));
}
```

```Java
class Franc {
    private int amount;

    Franc(int amount) {
        this.amount= amount;
    }

    Franc times(int multiplier) {
        return new Franc(amount * multiplier);
    }

    public boolean equals(Object object) {
        Franc franc= (Franc) object;
        return amount == franc.amount;
    }
}
```
