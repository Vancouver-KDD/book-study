# Chapter 1. Multi-Currency Money

### TDD Cycle

1. Add a little test
2. Run all tests and fail
3. Make a little change
4. Run the tests and succeed
5. Refactor to remove duplication

### Todo List

- $5 + 10 CHF = $10 if rate is 2:1
- **$5 \* 2 = $10**
- Make "amount" private
- Dollar side-effects?
- Money rounding?

---

#### 1. Write a test for muliplication

```
public void testMultiplication() {
Dollar five= new Dollar(5);
five.times(2);
assertEquals(10, five.amount);
}
```

#### 2. Compile error

- No class Dollar
- No constructor
- No method times(int)
- No field amount

#### 3. Write a code to run the test

```
class Dollar
```

```
Dollar(int amount) {
}
```

```
void times(int multiplier) {
}
```

```
int amount;
```

#### 4. Test fails

#### 5. Make a change to pass the test

```
int amount = 10;
```

#### 6. Remove duplication

- TDD is about being able to take teeny-tiny steps
- Get rid of duplication between the test code and the working code

amount = 10은 test code에서 5 \* 2 에서 오는 거임 -> remove duplication

```
int amount;
void times(int multiplier) {
    amount = 5*2;
}
```

5는 어디서 온거지? -> Dollar의 constructor로 받는 숫자임 -> amount variable을 times 안에서 사용

```
Dollar(int amount) {
    this.amount = amount;
}
void times(int multiplier) {
    amount *= multiplier;
}
```
