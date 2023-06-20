# Chapter 28. Green Bar Patterns

Once you have a broken test, you need to fix it. If you treat a red bar as a condition to be
fixed as quickly as possible, then you will discover that you can get to green quickly. Use 
these patterns to make the code pass (even if the result isn't something you want to live with for even an hour).


## Fake It ('Til You Make It)
What is your first implementation once you have a broken test? Return a constant. Once you 
have the test running, gradually transform the constant into an expression using variables.


xUnit 구현에서 간단한 예
```
return "1 run, 0 failed"
```
became:
```
return "%d run, 0 failed" % self.runCount
```
became:
```
return "%d run, %d failed" % (self.runCount , self failureCount)
```
Fake It really rubs some people the wrong way. Why would you do something that you know 
you will have to rip out? Because having something running is better than not having 
something running, especially if you have the tests to prove it.


Fake It을 강력하게 만드는 몇 가지 효과

* Psychological - Having a green bar feels completely different from having a red bar. When the bar is green, you know where you stand. You can refactor from there with confidence.
* Scope control - Programmers are good at imagining all sorts of future problems. Starting with one concrete example and generalizing from there prevents you from prematurely confusing yourself with extraneous concerns. You can do a better job of solving the immediate problem because you are focused. When you go to implement the next test case, you can focus on that one, too, knowing that the previous test is guaranteed to work.


Fake It은 필요하지 않은 코드는 작성하지 않는다는 규칙을 위반? No, 리팩토링 단계에서 테스트 사례와 코드 간의 데이터 중복을 제거하게됨. 

```
assertEquals(new MyDate("28.2.02"), new MyDate("1.3.02").yesterday());
```

```java
//MyDate
public MyDate yesterday() { 
    return new MyDate("28.2.02");
}
```
코드와 테스트 사이의 중복제거
```java
//MyDate
public MyDate yesterday() {
    return new MyDate(new MyDate("31.3.02").days()-1); 
}
```
또 다른 중복도 제거해보자
```java
//MyDate
public MyDate yesterday() {
    return new MyDate(this.days()-1); 
}
```


## Triangulate
How do you most conservatively drive abstraction with tests? Abstract only when you have 
`two or more examples`.
Suppose we want to write a function that will return the sum of two integers. We write:

```java
public void testSum() { 
    assertEquals(4, plus(3, 1));
}
private int plus(int augend, int addend) { 
    return 4;
}

```
triangulating to the right design

```java
public void testSum() { 
    assertEquals(4, plus(3, 1)); 
    assertEquals(7, plus(3,4));
}

```
I only use Triangulation when I'm really, really unsure about the correct abstraction for the calculation. Otherwise I rely on either Obvious Implementation or Fake It.

## Obvious Implementation

간단한 작업을 어떻게 구현합니까? 그냥 구현~~!!

Fake It and Triangulation are teensy-weensy tiny steps. Sometimes you are sure you know how to implement an operation. Go ahead. For example, would I really use Fake It to implement something as simple as plus()? Not usually. I would just type in the Obvious Implementation. If I noticed I was getting surprised by red bars, then I would go to smaller steps.


Fake It과 Triangulation의 중간 특성에는 특별한 미덕이 없습니다. 무엇을 입력해야 하는지 알고 있고 빠르게 입력할 수 있다면 실행하십시오.


## One to Many
How do you implement an operation that works with collections of objects? Implement it 
without the collections first, then make it work with collections.
```java
public void testSum() { 
    assertEquals(5, sum(5));
}
private int sum(int value) { 
    return value;
}
```

(I am implementing sum() in the TestCase class to avoid writing a new class just for one 
method.)
We want to test sum(new int[] {5, 7}) next. First we add a parameter to sum(), taking an 
array of values:


