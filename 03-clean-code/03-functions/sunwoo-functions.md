# Functions

Function은 프로그램을 구성하는 가장 작은 단위중에 하나이다.
Clean한 function은 어떤 것일까라는 질문에 대해서 책에서는 어떠한 function이 읽고 이해하는데 어려운지를 예를 통해서 설명하고 있다.

* Long Function

긴코드의 function은 개발자가 function을 하나의 덩어리로 이해하는데 어려움을 준다. 
예를 들어서 function 시작부분에서 declare된 variable가 나중에 다시 사용 된다면, 그 variable를 기억하고 있지 않는한, 다시 위쪽으로 올라가 그 variable이 어떤 데이터를 저장하고 있는지 다시 확인해야하는 절차가 추가가 된다.

* Duplicated code

만약 똑같은 code 혹은 logic이 같은 function안에서 반복적으로 나타난다면, 그것 또한 같은 부분을 이해하기 위한 추가적인 시간이 소요가 된다. 그리고 각각의 duplicated code들이 같은 행동을 한다는 추가적인 검증이 필요하다.

* Strange and inobivious data type

만약 variable 대신 String 값이나 다른 값들이 직접적으로 사용이 되어 진다면, 그 String 값이 어떤 의미를 내포하고 있는지 불분명해진다.

```js
const filteredUsers = users.filter((user) => {return user.type === 1});

const VIP_TYPE = 1
const filteredUsers = users.filter((user) => {return user.type === VIP_TYPE});
```
* Different levels of abstraction

한 function이 두가지의 subtask A와 B가 있다고 가정을 했을때, 만약 A는 byte를 핸들링하고, B는 UI단에서 rendering 한다고 가정 했을때, 이러한 코드는 읽는 이로 하여금 function의 기능에 대해 혼돈을 줄 수 있다.

## Small!
책에서 function에 대해 주장하는 것중에 하나는 function은 작아야 한다는 것이다. 필자는 자신의 경험을 근거로 function은 20줄 이상 넘어가지 않는것을 추천을 하고 있다. function의 크기가 작아지면 작아질수록, 코드가 명시하고 있는 것들은 transeparency해지고, 목적성이 명확해지고, function의 execution에 순서에 대해 설득력이 생긴다.

### Blocks and Indenting
`if`, `else`, `while` 등의 statements안에 있는 것들은 statements 안에서 복잡한 구조를 가지어서는 안된다. 대신 descriptive한 이름을 가진 메소드를 호출하는 것으로 이것을 해결한다.

## Do One Thing
*FUNCTIONS SHOULD DO ONE THING. THEY SHOULD DO IT WIELL. THEY SHOULD DO IT ONLY.*
function을 작성할때 가장 핵심적인 것을 function은 한가지의 일을 한다라는 룰을 지키는 것이다. 문제접은 한 가지라는 것을 어떻게 정의 할것인지이다. 이것의 핵심은 layers of abstraction이라고 생각한다. 한가지의 requirement에 대한 function을 작성한다고 가정을 했을때, 그 requirement를 충족하기 위한 일련의 과정을 다음 abstraction level에서 정의한다. 그것을 또 다음 abstraction level에서 분해하고, 이 과정을 통해, 위의 룰을 지키고, 더 명확한 abstraction level을 정의 할 수 있다.

### Sections within Functions
만약 지금 사용하고 있는 function에서 하는 일을 두가지 이상으로 간단하게 관찰 할 수 있다면, 그것은 refactoring이 필요하다.

## One Level of Abstraction per Function
한 function에서 다른 level의 abstraction을 사용하는 것은, 읽는 사람으로 하여금 불필요한 정보를 제공 할 뿐만 아니라, function의 핵심으로 부터 멀어질수 있다. 예를 들어 한 function안에서 서버로부터 받아온 bytes를 encoding하고, 그것을 화면에 rendering하는 것은 function의 핵심이 무엇인지 읽는 이로 하여금 혼란을 야기한다.

### Reading Code from Top to Bottom: The *Stepdown* Rule
우리는 code를 읽을 때, 위에서부터 아래로 읽는다. 그리고 코드를 읽을때, 그 코드을 어떻게 구현되었는지를 abstraction 레벨에 따라 분류해서 읽고 싶다. *TO* paragraph는 현재의 level of abstraction에서 function이 하는 일을 설명하고, 다음 레벨의 abraction *TO* paragraph들을 reference한다.

예시

1 To include the setups and teardowns, we include <span style="color:red"><strong>setups</strong></span>, then we include the test page content, and then we include the teardowns.

1-1 To include the setups, we include the <span style="color:red"><strong>suite setup </strong></span>if this is a suite, then we include the regular setup.

1-1-1 To include the suite setup, we <span style="color:red"><strong>search the parent </strong></span> hierarchy for the “SuiteSetUp” page and add an include statement with the path of that page.

1-1-1-1 To search the parent, we <span style="color:red"><strong>. . . </strong></span>

## Switch Statements
Switch statment는 statement 갯수 만큼의 일을 하도록 디자인이 되어 있기 때문에, switch statemt가 하는 일을 줄이는 것은 쉽지 않다. 하지만 몇은 polymorphism을 통해서 switch statement를 대채 할 수 있다.

## Use Descriptive Names
function의 이름은 그 function의 어떤 일을 하는지 명확하게 설명해야 한다. 함수가 어떠한 일을 하는지 정의하기 위해서는 function을 작은 function들로 쪼개고, 각각 function에게 또한 descriptive한 이름을 정의해야한다.
function이 작으면 작을수록, 그 function이 어떤 일을 하는지 명확해지고, 이름을 선택할때에도 편리해진다. 또한 descriptive한 이름은 모듈이나 시스템의 디자인을 이해하는데도 도움을 줄 수 있다.

긴 이름의 descriptive한 function을 사용 하는 것이, 정체를 알 수 없는 약어를 사용하는 function name이나 코멘트란에 추가적인 설명보다 효과적이다. 또한 팀적으로는 단어를 선택할때 consistent하게 이름을 가져가야 한다. (retrieve, fetch, get) -> get

또한 IDE를 사용할 때, 이름을 자동으로 타이핑 해주거나, 이름을 refactoring하는 것에 도움을 받을 수 있기 때문에, 여러가지 이름을 사용해보거나 긴 이름을 사용하는 것에 대한 부담도 많이 줄어 들 수 있다.

## Function Arguments
function의 arguments를 정의할때 3개 이상의 arguments를 받는 function을 작성하는 것은 피해야 한다.
또한 function과 맞지 않는 레벨의 abraction layer에 있는 argument는 읽는 이로 하여금 이해하여야 하는 코드의 양을 증가 시킨다.

`render(UserPage userPage)` function이 `render(ByteBuffer bytes)` function보다 훨씬 직관적이고 이해하기 쉽다.

또한 arguments가 많을 경우, 테스트해야 하는 input domain의 범위도 크게 증가를 한다.

### Common Monadic Forms
한개의 argument를 받는 function은 주로 다음과 같은 3가지 기능을 한다.
* argument에 대한 질문 `boolean fileExists("./file")`
* argument를 변환 `InputStream fileOpen("./file")`
* argument를 system의 state 변환으로 사용 `void passwordAttemptFailedNtimes(int attemps)`

### Flag Arugment
boolean variable를 function의 argument로 보내는 것은 function이 그 booelean 밸류에 따라 한가지 이상의 일을 한다는 것을 암시한다. 또한 그 boolean 밸류가 어떤 일을 하는지 function를 봐서는 인지하기 힘들다.

### Dyadic Function
두 개의 Arguments를 받는 function은 많이 있다. 예를 들어 cartesian coordiate 같은 경우에는 두개의 좌표를 필요로 한다. 이러한 function 들을 사용할 때에는 두 개의 argument들을 한개로 통합시킬수 있는지를 고려해보자.

### Triads
3개의 arguments를 사용 할 경우, function의 readability나 reliability등 많은 요소가 2개 이하의 function을 사용할때보다 떨어진다.

### Argument Objects
만약 function이 3개 이상의 arguments를 사용하게 된다면, 그 arguments들을 encapsulation 해서 function에 보내는 것도 고려해보자.

### Argument Lists
가끔 같은 타입의 arugments를 가변적으로 받는 경우가 있다. 이 경우에는 function이 한 타입의 list처럼 받기 때문에 한 개의 argument를 받는 것과 동일하다.
### Verbs and Keywords
function의 이름을 작성할때 verb를 사용해 function이 무엇을 하는지를 표기한다. `write(name)`
또, arugment의 이름들을 function 이름에 사용해, 읽는 사람에게 자연스럽게 순서를 알려줄수도 있다. `assertExpectedEqualsActual(expected, actual)`
### Have No Side Effects
만약 function이 dependecy가 없다면, function 레벨에서의 side effect는 function이 다른 의도되지 않은 다른 일들을 한다는 뜻이다. class 레벨에서는 method를 실행하는 도중 field값이 바뀌거나 하는 일이 있을 수 있다.

## Command Query Separation
function은 무엇 인가를 하거나 아니면 무엇 인가에 대해 답해야한다. 하지만 한 function 두 가지를 동시에 해서는 안된다.
`public boolean set(String attribute, String Value)`
->
`public void setAttribute(String attribute, String Value)`
`public boolean attributeExist(String attribute, String Value)`
## Prefer Exceptions to Returning Error Codes
어떠한 command의 결과에 대한 정보를 리턴할때, 에러코드를 반환하는 것보다는 exception을 던지는 것을 사용하는 것이, 코드를 간단하게 만들고 function에 역활을 clear하게 할 수 있다.
### Extract Try/Catch Blocks
무분별한 try/catch block은 exception을 handling하는 코드와 그렇지 않은 코드를 섞는다. try/catch block이 필요한 최하의 코드에만 적용을 하도록 노력하자.
### Error Handling Is One Thing
error를 핸들링 하는 것도 하나의 functionality이다. 위에 이야기 했던 룰 처럼, function이 error handling을 한다면 function은 하나의 일을 완수한 것이다.
### The Error.java Dependency Magnet
에러 값을 리턴 한다는 것은, error에 대한 class나 enum이 있다는 것을 의미한다. 만약 이 코드의 클래스를 바꿨을때, 이 error에 관련된 시스템의 변화가 요구된다.

## Don't Repeat Yourself
duplicated code가 존재 한다는 것은 같은 일에 대해 에러가 발생할 가능성이 duplicated code 수 만큼 존재 한다는 뜻이다. 그러한 duplicated code들을 통합해 한 곳에서 관리 할 수 있다면, 시스템에서 발생 할 수 있는 많은 문제들을 예방 할 수 있다.
## Structured Programming
structured programming 룰에 따르면 function에는 단 한개 만의 entry와 단 한개의 exit만 존재해야 한다. break, continue나 goto 같은 키워드들의 사용을 function을 작게 만듬으로서 최소화 할 수 있다.
## How Do You Write Functions Like This?
function을 작성하는 것을 글을 쓰는 것과 같다. 처음의 draft가 있고 feedback loop를 통해서 코드를 refine한다.
## Conclusion
function은 programming language의 동사이다. 우리는 이 언어를 통해서 시스템을 이야기로서 간결하고, 명확하게 이야기 할 수 있어야 한다.