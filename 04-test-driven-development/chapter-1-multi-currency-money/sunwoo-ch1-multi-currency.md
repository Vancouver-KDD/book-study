Test Driven Devleopment는 프로그래밍에 대한 두려움을 매니징하는 하나의 방법론입니다.
software engineering은 program의 시간의 변화를 더한것이고, 항상 변화하는 것입니다.
엔지니어는 항상 requirements가 구현 가능한 것인지, 얼마만큼의 resource가 필요한 것인지 알아야 합니다.
TDD는 이러한 결정을 하는데 도움을 줄 수 있습니다.

# The Steps of TDD

1. Quickly add a test.
2. Run all tests and see the new one fail.
3. Make a little change.
4. Run all tests and see them all succeed.
5. Refactor to remove duplication.

# Chapter 1. Multi-Currency Money

우리는 가장 먼저 시스템의 변경이 필요한때, 어떠한 테스트 셋이 그 변경점을 표현할지 고려해야 합니다.
예제에서는, 다른 currency를 사용 할 수 있게 시스템에 변경이 필요하다면, 다음과 같은 requirements를 고려해 볼 수 있습니다.

- We need to be able to add amounts in two different currencies and convert the result given a
  set of exchange rates.
- We need to be able to multiply an amount (price per share) by a number (number of shares)
  and receive an amount.

가장 먼저 multiplication에 대한 것을 implementation 할 때, 그 코드가 어떤것을 할지를 무엇보다 먼저 테스트 코드를 통해 먼저 작성합니다.

```Java
public void testMultiplication() {
    Dollar five= new Dollar(5);
    five.times(2);
    assertEquals(10, five.amount);
}
```

테스트 코드의 `Dollar`는 side-effect, public field 등 많은 문제점이 존재하지만 간단하게 note만 하고, 직접적으로 테스트 코드가 fail하는 이유에 먼저 집중합니다. 이유들은 다음과 같습니다.

- No class Dollar
- No constructor
- No method times(int)
- No field amount

테스트 코드가 실패한 이유들에 대한 최소한의 코드를 작성합니다.

```Java
class Dollar {
    int amount = 10;
    Dollar(int amount) {}
    void time(int multiplier) {}
}
```

이제 refactoring을 통해 코드의 반복되는 부븐을 제거해야 합니다. Steve Freeman은 테스트와 코드의 문제가 중복이 아닌 의존성이라는 점을 지적합니다. 현재 구현에서 코드를 변경하지 않고 더 많은 테스트를 작성할 수 있는 것이 목표입니다. 의존성은 소프트웨어 개발에서 주요한 문제이며, 중복을 제거함으로써 의존성을 감소시킬 수 있습니다.

그렇다면 예제의 코드에서 중복이 일어나는 곳은 어디일까요?
10이라는 constant가 테스트 코드와 Dollar 소스코드에서 중복적으로 일어나고 있습니다.
10이라는 값은 5*2라는 연산을 통해 나온 값이므로 `amount = 5*2`로 변경을 합니다 (?)
그리고 10이라는 값은 `times`라는 메소드의 호출의 결과이므로 `amount = 5\*2`를 `times`메소드 안으로 위치를 변경합니다.

```Java
class Dollar {
    int amount;
    Dollar(int amount) {}
    void time(int multiplier) {
        amount = 5*2;
    }
}
```

처음에 이렇게 아주 작은 단위로 변경하는 것은 귀찮을수 있습니다. 하지만 TDD는 이렇게 작은 단위로 쪼개는 것을 가능하게 하는 연습입니다. 만약 이렇게 작은 단위로 쪼개어 step을 구성할 수 있다면, 더 큰 단위로 쪼개는 것또한 가능하지만, 반대는 어렵습니다.
이렇게 작은 단위로 계속 쪼개어서 generalize하는 것을 반복합니다.

1.

```java
Dollar(int amount) {
    this.amount= amount;
}
```

2.

```Java
void times(int multiplier) {
    amount= amount * 2;
}
```

3.

```Java
void times(int multiplier) {
    amount= amount * multiplier;
}
```

4.

```Java
void times(int multiplier) {
    amount *= multiplier;
}
```
