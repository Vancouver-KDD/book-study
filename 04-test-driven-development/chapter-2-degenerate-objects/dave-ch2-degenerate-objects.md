
## Chapter 2. Degenerate Objects

### General TDD cycle

#### 1. Write a test. 
Think about how you would like the operation in your mind to appear in your code. Invent the interface you wish you had. Include all of the elements in the story that you imagine will be necessary to calculate the right answers.


#### 2. Make it run. 
Quickly getting that bar to go to green dominates everything else. If a clean, simple solution is obvious, then type it in. If the clean, simple solution is obvious but it will take you a minute, then make a note of it and get back to the main problem, which is getting the bar green in seconds. 


#### 3. Make it right. 
Step back onto the straight and narrow path of software righteousness. Remove the duplication that you have introduced, and get to green quickly.

#### The goal is clean code that works.
First we'll solve the "that works" part of the problem. Then we'll solve the "clean code" part. 이는 클린코드 후 작동을 고려하며 디자인에 통합하는 순서의 해당 아키텍쳐 기반의 순서와 반대되는 걔념이다.

```
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
Make "amount" private 
Dollar side effects? <-- 
Money rounding?
```
Dollar object에서의 작업으로 Dollar의 값이 계속 바뀌는 side effect를 다룹니다.

```java
public void testMultiplication() { 
	Dollar five= new Dollar(5); 
	five.times(2);
	assertEquals(10, five.amount); 
	five.times(3);
	assertEquals(15, five.amount); 
}
```

위와 같이 testMultiplication 호출하면 두번째 five.times(3);에 대해 assertEquals은 무조건 실패할 수 밖에 없습니다. 그러므로 times()를 return type을 지정해서 반환된 값을 new object에 새롭게 반환시키도록 아래와 같이 변경합니다. 
```java
public void testMultiplication() { 
    Dollar five= new Dollar(5); 
    Dollar product= five.times(2); 
    assertEquals(10, product.amount); 
    product= five.times(3); 
    assertEquals(15, product.amount);
}
```

그렇게 되면 Dollar.times()는 컴파일 될리가 없으므로 변경을 하게 되고, 아래와 같이 됩니다.
```java
Dollar times(int multiplier) {
    return new Dollar(amount * multiplier); 
}
```
```
//To-do List
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
Make "amount" private 
//Dollar side effects? 
Money rounding?
```

1장에 가짜 구현으로 일단 실행이 되도록 한 후 점차 올바른 구현을 작성하면서 정상적인 구현을 하는 방식으로 바꾸어 갔습니다. 
이를 정리하면 아래와 같습니다. 

### How to get to green quickly

#### 1. Fake It
Return a constant and gradually replace constants with variables until you have the real code.


#### 2. Use Obvious Implementation
Type in the real implementation.


### Third style of TDD(Triangulation)

1. Translated a design objection (side effects) into a test case that failed because of the objection
1. Got the code to compile quickly with a stub implementation 
1. Made the test work by typing in what seemed to be the right cod


부작용에 대한 혐오감???을 테스트로 변경해 가는 것 예를 들면 하나의 동일한 Dollar object를 두번 연달아 곱하기해서 에러를 도출하는 방식 이를 통해 테스트 코드의 미적인 부분이 발달할 수 있다. 
