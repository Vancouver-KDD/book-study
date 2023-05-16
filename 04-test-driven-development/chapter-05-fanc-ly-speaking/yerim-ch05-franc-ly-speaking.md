# Ch 05. Franc-ly Speaking

- TODO LIST
    - $5 + 10 CHF = $10 if rate is 2:1
    - ~~$5 * 2 = $10~~
    - ~~Make "amount" private~~
    - ~~Dollar side-effects?~~
    - Money rounding?
    - ~~equals()~~
    - hashCode()
    - Equal null
    - Equal object
    - **5 CHF * 2 = 10 CHF**

첫번째 테스트에 어떻게 접근할 것인가? 작은 스텝으로 구현하기엔 아직 너무 크다. 전제조건으로는 `Dollar`같은 오브젝트처럼 프랑을 나타내는 오브젝트를 가지고 있어야 한다. 그렇게 된다면, 우리는 혼합 추가 테스트를 작성하고 실행시킬 수 있을 것이다.

```
public void testfrancMultiplication() {
    Franc five = new Franc(5);
    assertEquals(new Franc(10), five.times(2));
    assertEquals(new Franc(15), five.times(3));
}
```

`Dollar` 코드를 복사해서 `Franc`으로 바꾸었더니 이렇게 빠르게 테스트를 작성할 수 있다. 

복사 붙여넣기로 클린한 코드를 해친다고 생각되는가? 기억하자. 우리의 주기에는 여러 단계가 있고, 그것들은 종종 몇 초 만에 빠르게 지나가지만 그래도 하나의 단계들이다. 

1. Write a test.
2. Make it compile.
3. Run it to see that it fails.
4. Make it run.
5. Remove duplication.

각각의 다른 단계들은 다른 목적을 가진다. 첫번째 세 단계들은 빠르게 지나가고, 새로운 기능으로 알려진 상태에 도착한다. 우리를 거기에 도달하기 위해 많은 잘못들(sins)을 저지를 수 있다. 왜냐면, 그 짧은 순간동안은 속도가 디자인보다 우선하기 때문이다.

하지만 주기가 아직 끝난 게 아니다. 처음 네번째 스텝은 다섯번째 없이는 작동할 수 없다. 좋은 타이밍에 좋은 디자인. 이것들을 실행하게 하고 올바르게 만들자.

다시 돌아와서, 지금 단계에서는 속도를 위해 좋은 디자인의 모든 원칙을 위반한다. 다음 챕터들에서 우리의 잘못에 대한 참회를 다룰 것이다. 

```
class Franc {
    private int amount;

    Franc(int amount) {
        this.amount = amount;
    }

    Franc times(int multiplier) {
        return new Franc(amount * multiplier);
    }

    public boolean equals(Object object) {
        Franc franc = (Franc) object;
        return amount == franc.amount;
    }
}
```

- TODO LIST
    - $5 + 10 CHF = $10 if rate is 2:1
    - ~~$5 * 2 = $10~~
    - ~~Make "amount" private~~
    - ~~Dollar side-effects?~~
    - Money rounding?
    - ~~equals()~~
    - hashCode()
    - Equal null
    - Equal object
    - ~~5 CHF * 2 = 10 CHF~~
    - Dollar/Franc duplication
    - Common equals
    - Common times

지금 우리는 많은 duplication을 가지고 있으니, 다음 테스트를 작성하기 전에 그것들을 제거할 것이다. `equals()`를 제너럴라이징하는 것부터 시작하자. 