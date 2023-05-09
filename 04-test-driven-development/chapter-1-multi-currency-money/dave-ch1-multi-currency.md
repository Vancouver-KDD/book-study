## Part I: The Money Example

we will develop typical model code driven completely by tests
Goal: to see the rhythm of Test-Driven Development (TDD)

>1.  Quickly add a test.
>2.  Run all tests and see the new one fail.
>3.  Make a little change.
>4.  Run all tests and see them all succeed.
>5.  Refactor to remove duplication.

* How each test can cover a small increment of functionality
* How small and ugly the changes can be to make the new tests run
* How often the tests are run
* How many teensy-weensy steps make up the refactorings


## Chapter 1. Multi-Currency Money
Ward가 WyCash에 만든 다중통화화폐 오브젝트로 부터 설명을 시작한다.
아래와 같은 report가 통화단위가 추가되어진 상태로 있다고 가정하자.

|Instrument| Shares |Price  | Total|
|----------|----------|----------|----------|
|IBM |1000 |25  |25000 |
|GE |400 |150  | 40000  |
|   |       |Total| 65000 |

헌데, 아래와 같은 수정이 필요적용되었다고 하자.

|Instrument| Shares |Price  | Total|
|----------|----------|----------|----------|
|IBM |1000 |25 USD |25000 USD|
|Novartis |400 |150 CHF | 40000 CHF |
|   |       |Total| 65000 USD|

또한 아래와 같은 환율지정도 필요했다고 하자.

|From| To |Rate  |
|----------|----------|----------|
|CHF |USD |1.5 |


```
//To-do List
$5 + 10 CHF = $10 if rate is 2:1 
$5 * 2 = $10  <--
```


수정된 보고서가 생성되기 위해서 필요한 동작? 즉, 테스트를 통과 했을 때 이는 정상적인 보고서라고 확신할 수 있는 테스트는 무엇일까?

* 첫번째는 두가지 통화로 금액을 추가 그리고 적용된 환율의 세트가 결과로 보여져야한다.
* 두번째는 multiply an amount (price per share) by a number (number of shares)를 곱한 금액을 얻을 수 있어야 한다.

'<--'가 현재 해결코자하는 대상을 표시하며, 위 리스트와 같이 곱셈작업에 대해 먼저 다룰 것이다. 먼저 어떤 객체가 필요한가?
사실 객체가 아닌 테스트로 시작해야하는 것을 인식하고 있자.

그리고, 테스트를 작성할때 복잡하고 현실적인 테스트를 짜내야한다는 생각에서 시작하기보단 먼저 현재 가장 가능한 API에서 출발하면됩니다.

다음은 곱셈작업에 대한 예이다.
```java
public void testMultiplication() { 
    Dollar five= new Dollar(5); 
    five.times(2);
    assertEquals(10, five.amount); 
}
```
public access type, side-effect, inegers for monetary amount는 작은 단계이다.
현재 위의 코드로는 빨간색 즉, 오류 상태일 것이다.

```
//To-do List
$5 + 10 CHF = $10 if rate is 2:1 
$5 * 2 = $10  <--
Make "amount" private 
Dollar side-effects? 
Money rounding?
```

위에 코드는 컴파일 되지 않는다. 여기서 실행되기 전 컴파일을 위한 조치는 무었을까? 
아래 4가지 오류와 오류에 대한 각각의 수정이다. 

* No class Dollar
```java
class Dollar
```
* No constructor
```java
Dollar(int amount) { 
}
```
* No method times(int) 
```java
//컴파일을 위한 최소한의 작업만 우선 진행한다.
void times(int multiplier) { 
}
```
* No field amoun
```java
int amount;
```

Junit이 정상실행되는 것을 볼 수 있으나 빨간색 테스트 실패 표시와 코드결과가 10을 예상 했으나 0이라는 결과 메세지를 확인할 수 있다.

현재의 상태에 대해서 저자는 Fail에 대해 모호하게 아는 것 보다 실패에 대한 구체적인 척도 (Concrete meaure)를 갖게 되었음을 진보한 것이라는 점을 알려준다.

아래와 같이 코드가 추가가 되면,
```java
int amount= 10;
```
일단은 테스틀 통과하는 작은 단위가 작동되게된다.
하지만 이와 같이 제한적이고 Naive한 구현을 통과 시키는 실제 테스트는 없을 것이다.
그래서 일반화된 cycle이 필요하며 다음과 같다. 
1.  Add a little test.
2.  Run all tests and fail.
3.  Make a little change.
4.  Run the tests and succeed.
5. Refactor to remove duplication.


>테스트와 코드의 문제는 Duplication이 아니고 Dependency의 문제이다. 하나를 변경하지않고는 다른 하나를 변경할 수 없는 것이 문제점이라는 것이다.

>If dependency is the problem, duplication is the symptom. Objects are excellent for abstracting away the duplication of logic. duplication을 제거하면 dependency을 줄 일 수 있다. 여기서 TDD의 두번째 규칙 Duplication 제거가 소개된다.

자 그럼 중복을 제거해보자.

```java
//아래 object initialization을 method안으로 옮긴다. 
//int amount= 10;

int amount;
void times(int multiplier) { 
    amount= 5 * 2;
}
```

그 다음 '5'를 얻을 수 있는 것을 생각해보면 constructor를 통해서 얻을 수 있고, 얻어진 값을 메소드에 적용하게 되면 아래와 같다. 

```java
Dollar(int amount) { 
    this.amount= amount;
}
void times(int multiplier) { 
    amount *= multiplier;
}
```


이로써 첫번째 목표였던 곱셈 연산에 대한 to-do를 완료함.

```
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
Make "amount" private 
Dollar side effects? 
Money rounding
```

#### What we`ve done
* Made a list of the tests we knew we needed to have working
* Told a story with a snippet of code about how we wanted to view one operation 
* Ignored the details of JUnit for the moment
* Made the test compile with stubs
* Made the test run by committing horrible sins
* Gradually generalized the working code, replacing constants with variables 
* Added items to our to-do list rather than addressing them all at once
