# Chapter 26. Red Bar Patterns

이러한 패턴은 테스트를 작성하는 시기, 테스트를 작성하는 위치 및 테스트 작성을 중지하는 시기에 관한 것입니다

## One Step Test
목록에서 한 단계를 나타내는 테스트를 찾지 못한 경우 해당 항목에 대한 진행 상황을 나타내는 몇 가지 새 테스트를 추가합니다
테스트에서 성장한 프로그램은 작은 조각으로 시작하여 점점 더 크게 집계하기 때문에 상향식으로 작성된 것처럼 보일 수도 있습니다.
방향성은 known-to-unknown의 흐름으로 경험한 바(know)로 부터 배우는 바(unknown)의 방향으로 간다.

## Starter Test
어떤 테스트부터 시작해야 할까요? 아무것도 하지 않는 작업의 변형을 테스트하여 시작합니다.

The first question you have to ask with a new operation is, "Where does it belong?" 

* Where does the operation belong? 
* What are the correct inputs?
* What is the correct output given those inputs?


Starter Test provides an answer:
*  The output should be the same as the input. Some configurations of polygons are already 
normalized, incapable of further reduction.
*  The input should be as small as possible, like a single polygon, or even an empty list of polygons.

My Starter Test looked like this:
```java
Reducer r= new Reducer(new Polygon());
assertEquals(0, reducer.result().npoints);

```

 Pick a Starter Test that will teach you something but that you are certain you can quickly get to work. If you are implementing an application for the nth time, then pick a test that will require an operation or two. You will be justifiably confident you can get it working. If you are implementing something hairy and complicated for the first time, then you need a little courage pill immediately.


## Explanation Test

you will notice fewer integration problems and defect reports in tested code, and the designs will be simpler and easier to explain. It can even happen that folks get downright enthusiastic about testing, and testing first.


### Learning Test


When do you write tests for externally produced software? Before the first time you are going to use a new facility in the package.

Let's say we are going to develop something on top of the Mobile Information Device Profile 
library for Java.

 we write a little test that verifies that the API works as expected. 
 
 ```java
RecordStore store; 
public void setUp() {
    store= RecordStore.openRecordStore("testing", true); 
}
public void tearDown() {
    RecordStore.deleteRecordStore("testing"); 
}
public void testStore() {
    int id= store.addRecord(new byte[] {5, 6}, 0, 2); 
    assertEquals(2, store.getRecordSize(id)); 
    byte[] buffer= new byte[2];
    assertEquals(2, store.getRecord(id, buffer, 0));
    assertEquals(5, buffer[0]);
    assertEquals(6, buffer[1]); 
}
 ```

When new releases of the package arrived, first the tests were run (and fixed, if necessary). If the tests didn't run, then there was no sense running the application because it certainly wouldn't run. Once the tests ran, the application ran every time.

## Another Test

How do you keep a technical discussion from straying off topic? When a tangential idea arises, add a test to the list and go back to the topic.

## Regression Test

What's the first thing you do when a defect is reported? Write the smallest possible test that fails and that, once run, will be repaired.

Regression tests for the application give your users a chance to speak concretely to you about what is wrong and what they expect. 

## Break

If you know what to type, type the Obvious Implementation. If you don't know what to type, then Fake It. If the right design still isn't clear, then Triangulate.

You're getting tired, so you're less capable of realizing that you're tired, so you keep going and get more tired.

The way out of this loop is to introduce an additional outside element.
* At the scale of hours, keep a water bottle by your keyboard so that biology provides the 
motivation for regular breaks.
* At the scale of a day, commitments after regular work hours can help you to stop when you 
need sleep before progress.
* At the scale of a week, weekend commitments help get your conscious, energy-sucking 
thoughts off work. (My wife swears I get my best ideas on Friday evening.)
* At the scale of a year, mandatory vacation policies help you refresh yourself completely. 

## Do Over

What do you do when you are feeling lost? Throw away the code and start over.

You're lost. You've taken the break, rinsed your hands in the brook, sounded the Tibetan 
temple bell, and still you're lost. The code that was going so well an hour ago is now a mess, you can't think of how to get the next test case working, and you've thought of 20 more tests that you really should implement.

If you pair program, switching partners is a good way to motivate productive do-overs. You'll try to explain the complicated mess you made for a few minutes when your new partner,
completely uninvested in the mistakes you've made, will gently take the keyboard and say, 
"I'm terribly sorry for being so dense, but what if we started like this ."
