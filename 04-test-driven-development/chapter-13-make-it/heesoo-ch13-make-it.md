# Chapter 13. Make it

### Addition Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- **$5 + $5 = $10**
- Return Money from $5 + $5

모든 duplication을 제거하기 전까지 우리는 테스트를 통과했다고 여길 수 없음 -> 지금 우리는 코드 중복은 없지만 데이터 중복이 있음

1. Money.plus() 는 `Money` 가 아니라 real Expression 인 `Sum`을 리턴해야함 -> 두 Money의 합은 Sum 이여야함

```
public void testPlusReturnsSum() {
    Money five = Money.dollar(5);
    Expression result = five.plus(5);
    Sum sum = (Sum) result;
    assertEquals(five, sum.augend);
    assertEquals(five, sum.addend);
}
```

2. 이 테스트코드를 compile 하기 위해서는 Sum class 에 두개의 필드가 필요함 : augend, addend

```
class Sum {
    Money augend;
    Money addend;
}
```
