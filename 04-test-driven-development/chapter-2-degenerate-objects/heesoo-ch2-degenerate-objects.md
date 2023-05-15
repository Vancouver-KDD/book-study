# Chapter 2. Degenerate Objects

### General TDD

1. Write a test : 올바른 답이 나오게 하기 위한 모든 요소를 포함해서 테스트 작성
2. Make it run : 테스트가 통과될 수 있도록 제일 단순한 코드를 작성
3. Make it right : remove duplication

### Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- ~~$5 \* 2 = $10~~
- Make "amount" private
- **Dollar side-effects?**
- Money rounding?

### Test code

```
public void testMultiplication() {
    Dollar five = new Dollar(5);
    five.times(2)
    assertEquals(10, five.amount);
    five.times(3)
    assertEquals(15, five.amount);
}
```

five.times(2) 후에는 amount = 10 이 되기때문에 five.tiems(3) 테스트가 실패하게 됨 -> times() 함수가 새로운 인스턴스를 리턴하면 됨

```
Dollar times(int multiplier) {
return new Dollar(amount * multiplier);
}
```

- Use Obvious Implementation -> Type in the real implementation when I know what to type
- Fake it : when a test fials -> return a constant -> replace them with variables
  -> back and forth between thses two
