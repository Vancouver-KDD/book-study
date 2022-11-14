# Mysterious Name

추리 소설를 읽을 때와 코드를 읽을 때 다른 점이 무엇일까?
우리는 추리 소설을 읽으면서 글을 나름대로 해석하고 어떤 내용이 펼쳐질 지 상상하고 퍼즐을 푸듯이 읽는다. 하지만 코드는 그렇게 읽혀지면 안된다. 우리의 코드는 다른 사람으로 하여금 호기심을 일으키거나 흥미를 유발해서는 안되고(mundane) 깔끔해야 한다. 즉, 읽는 사람이 의문을 가지지 않고 해당 코드가 무엇을 하는지, 어떻게 사용되는 지 명확히 알아야 한다.

사람들은 종종 나쁜 이름이 문제가 되지 않는다고 생각해서 다시 이름 짓기(Rename)을 좋아하지 않는다. 하지만 좋은 이름은 미래에 다른 사람들이 해당 코드를 읽고 이해할 때, 많은 시간을 줄여줄 수 있다.

다시 이름을 짓는 것은 그냥 이름을 바꾸는 연습을 하는 것이 아니다. 우리가 해당 variable, function, class 등을 만들 때 좋은 이름을 생각하지 못한다면 그 것은 종종 잘못된 디자인으로부터 오는 힌트일 수도 있다. 

애매한 이름을 좋은 이름으로 바꿔내는 것은 우리의 코드를 간단하게 바꿀 수 있게 도와준다.

Rename의 가장 보편적인 리팩토링 방법은 다음과 같을 것이다:

### 1. Change Function Declaration 

##### 1.1 Motivation

좋은 function 이름은 해당 function 안의 implementation을 보지 않고도 무엇을 하는 function인지 알게 해 준다. 좋은 이름을 짓는 것은 어렵고 우리는 대충 만들고 넘어가려는 유혹에 빠지기 쉽다. 나쁜 이름을 봤을 때 좋은 이름으로 바로 고치는 것은 중요한데, 우리가 그 function이 무엇을 하는지 다시 들여다 보지 않아도 되기 때문이다.

Parameters 도 비슷하게 접근해야 한다. Parameter는 function이 어떻게 다른 function, object와 상호작용하는 지 나타낸다.

Tip: 좋은 이름이 생각이 나지 않을 때, 해당 function이 하는 일이 무엇인 지 커멘트를 달아보자. 그리고 그 커멘트를 이름으로 바꾸어보자.

##### 1.2 Mechanics

**Simple Mechanics**

- Parameter를 제거하려면, function 안에 reference 가 없는 지 확인한다
- method가 사용 되고 있는 곳을 모두 찾아 새로운 method name으로 변경한다
- 테스트한다
- 최대한 작은 단위로 수정한다(e.g. 만약 method 이름과 parameter를 동시에 바꾸고 싶다면, 한 번에 바꾸는 것이 아니라 두 번에 나눠서 각각 수정한다)

**Migration Mechanics**

- 다음의 extraction step을 더 쉽게 만들 수 있다면 function의 body를 리팩토링한다 
- 새로운 function을 만들기 위해 **Extract Function**을 적용한다
  만약 새로운 function이 원래의 function과 같은 이름을 가지게 된다면, 새로운 function에 임시로 이름을 지어준다
- Extracted function이 새로운 parameter를 더 필요로 한다면 **Simple Mechanics**를 사용해서 만들어준다
- 테스트한다
- 원래의 function에 **Inline Function**을 적용한다
- 임시로 이름을 지어줬다면, **Change Function Declaration**을 사용해 원래의 이름을 돌려준다
- 테스트한다


##### 1.3 Examples  

**Renaming a Function (Simple Mechanics)**

```java
public int calWidth(int side) {
  return Math.pow(side, 2);
}

/* 1. Declaration을 변경한다 */
public int calculateWidth(int side) {
  return Math.pow(side, 2);
}

/*
  2. calWidth의 caller를 모두 찾아 calculateWidth로 변경한다
     (다양한 환경은 이 작업을 쉽게 만들 수 있다. 예를 들어, 대부분의 좋은 IDE들은 실수 하나 만들지 않고 function 이름을 바꿀 수 있도록 도와준다)
  3. Parameter를 수정할 때에도 같은 순서로 작업한다 
     (만약 이름과 parameter 모두 수정할 경우에는 각각의 작업으로 나눠주는 것이 좋다. 이름 변경 > 테스트 > Parameter 수정 > 테스트)
*/
```

단점:
 - 수정할 부분이 적거나, 자동화 된 refactoring 도구(e.g. IDE)가 있으면 굉장히 합리적이지만 그렇지 않고 양이 많다면 복잡해질 수 있다
 - 다른 object들이 같은 function name을 가지고 있을 때에는 자동화 도구를 쓸 수 없기 때문에 복잡해질 수 있다. 이런 경우에는 Migration Mechanics를 사용해보자

**Renaming a Function (Migration Mechanics)**

```java
public int calWidth(int side) {
  return Math.pow(side, 2);
}

/*
  1. Extract Function을 적용해서 새로운 function을 만든다
*/
public int calWidth(int side) {
  return calculateWidth(side);
}
public int calculateWidth(int side) {
  return Math.pow(side, 2);
}
/*
  2. 매 수정 때 마다 하나씩 테스트 하고 모두 완료 했을 때, 원래의 function (calWidth)를 제거한다
  
  Tip: 대부분의 리팩토링은 우리가 수정할 수 있는 코드를 변경하지만, 위 방법은 API에서도 잘 사용될 수 있다.
       새로운 function을 만드는 데까지 진행한 후, 원래의 function을 deprecated 처리 해 놓으면 사용자들이 새로운 function을 사용 할 수 있도록 유도할 수 있다
*/
```
