# Chapter 2: Meaningful Names

#### Use Intention-Revealing Names

아래의 코드 샘플을 보자.
```java
public List<int[]> getThem() {
  List<int[]> list1 = new ArrayList<int[]>();
  for (int[] x: theList)
    if (x[0] == 4) 
      list1.add(x);
  return list1;
}
```
왜 위 코드는 읽기가 어려울까? 복잡한 연산도 없고, 스페이스나 들여쓰기(indentation)도 모자람이 없다. 게다가 오직 변수 3개와 상수 2개만이 존재한다. Array 1개만 있을 뿐 그럴듯한 클래스나 다형성(polymorphism)을 가진 메소드도 없다.

문제는 코드의 간단함이 아니라 코드가 내재 하고 있는 내용이다.
위 코드는 우리가 아래의 문제들에 대한 답을 이미 알고 있다고 생각한다.
1. theList 안에 무엇이 들어있는가?
2. theList의 0번째 인덱스는 어떤 의미를 가지고 있는가?
3. value 4가 가지는 의미는 무엇인가?
4. 반환되는 list1은 어떻게 쓰일것인가?

이 질문들에 대한 답은 위 코드에 없지만, 존재할 수는 있었다. 
예를 들어, 우리가 지뢰찾기 게임을 하고 있다고 생각해보자. 
theList는 지뢰찾기의 게임판이다. 각 cell은 하나의 array로 표현되고 0번째 인덱스는 해당 cell의 상태(status)를 나타낸다. 그리고 value 4의 의미는 해당 cell이 깃발로 표시되어있다고 보자. 
이 내용에 해당되도록 이름을 바꾸어주기만 해도 좋은 코드로 만들 수 있다.

```java
public List<int[]> getFlaggedCells() {
  List<int[]> flaggedCells = new ArrayList<int[]>();
  for (int[] cell: gameBoard) 
    if (cell[STATUS_VALUE] == FLAGGED)
      flaggedCells.add(cell);
  return flaggedCells;
}
```

코드의 간단함은 전혀 바뀌지 않았다는 것에 주목하자. 여전히 같은 수의 변수, 상수 그리고 연산이 존재한다.
한번 더 나아가면, int[] 대신 Cell이라는 클래스를 만들 수 있고, 그 안에서 isFlagged 라는 intention-revealing function을 만들어서 magic numbers를 없앨 수 있다.

```java
public List<Cell> getFlaggedCells() {
  List<Cell> flaggedCells = new ArrayList<Cells>();
  for (Cell cell: gameBoard)
    if (cell.isFlagged)
      flaggedCells.add(cell);
  return flaggedCells;
}
```

#### Make Meaningful Distinctions

**Number-series naming**
- Number-series naming은 잘못된 정보를 주는 것이 아니고, 정보가 존재하지 않는 것이다.
아래의 예를 보자.

```java
public static void copyChars(char a1[], char a2[]) {
  for (int i = 0; i < a1.length; i++) {
    a2[i] = a1[i];
  }
}
```
a1 대신 source, a2 대신 destination이라는 이름을 썼으면 훨씬 더 좋게 읽혔을 것이다.

**Noise words**
- Noise words는 의미없는 구분이다. Product 클래스가 있고, ProductInfo 혹은 ProduceData라는 다른 클래스를 만들었다고 생각해보자. Info와 Data는 a,an,그리고 the와 같이 깔끔하게 구분되지 않는 noise words다.

- a, an, 그리고 the같은 prefix를 쓰는 것은 문제가 없다. 하지만 이미 zork라는 변수가 있는데 theZork라는 변수를 또 만들 때 문제가 될 수 있다.

**Use Pronounceable Names**
- 발음할 수 있는 이름을 사용해라. Programming은 사회적 행동(Social activity)이다.

```java
// Not pronounceable
class DtaRcrd102 {
  private Date genymdhms;
  private Date modymdhms;
  private final String pszqint = "102";
}

// Pronounceable
class Customer {
  private Date generationTimestamp;
  private Date modificationTimestamp;
  private final String recordId = "102";
}
```

**Use Searchable Names**

```java
// Before
for (int j = 0; j < 34; j++) {
  s += (t[j]*4)/5;
}

// After
int realDaysPerIdealDay = 4;
const int WORK_DAYS_PER_WEEK = 5;
int sum = 0;
for (int j = 0; j < NUMBER_OF_TASKS; j++) {
  int realTaskDays = taskEstimate[j] * realDaysPerIdealDay;
  int realTaskWeeks = (realdays / WORK_DAYS_PER_WEEK);
  sum += realTaskWeeks;
}
```

**Avoid Encodings**
```java
PhoneNumber phoneString;
// Name not changed when type changed
```

**Member Prefixes**
```java
// with m_ (prefix member variable)
public class Part {
  private String m_dsc; // The textual description
  void setName(String name) {
    m_dsc = name;
  }
}

// without m_
public class Part {
  String description;
  void setDescription(String description) {
    this.description = description;
  }
}
```

**Interfaces and Implementation**
- 만약 interface나 implementation 둘 중 하나를 꼭 encode해야 한다면 interface보다는 implementation을 선택해라. IShapeFactory 대신 ShapeFactory를 사용하고, Implementation에 ShapeFactoryImp 혹은 CShapeFactory를 더 사용하는 걸 선호한다.

**Avoid Mental Mapping**

- i, j 혹은 k를 loop counter로 사용하는 것은 프로그래머들 사이에서는 암묵적인 규칙이다. 그 외에 single-letter를 사용하는 것은 코드를 읽는 사람에게 의미를 유추하게끔 한다. 
똑똑한 프로그래머와 프로페셔널한 프로그래머의 차이는 후자는 명료함이 최고라는 걸 아는 것이다.

**Class Names**
- Class와 Object는 반드시 명사여야 한다(e.g. Customer, Wikipage, Account, and AddressParser). Manager, Processor, Data, 혹은 Info 같은 이름은 피하도록 하자

**Method Names**
- Method는 반드시 동사여야 한다(e.g. postPayment, deletePage, or save). Accessor, mutators, 그리고 predicates의 이름들은 해당 value와 짝을 이루어야 하고  javabean standard에 따라서 get, set, 그리고 is가 항상 앞에 붙어야 한다.
```java
String name = employee.getName();
customer.setName("Mike");
if (paycheck.isPosted()) {...}
```

Constructor가 overload될 때는, arguments를 설명할 수 있는 이름을 가진 static factory method를 사용해라.
```java
Complex fulcrumPoint = new Complex(23.0);
대신에
Complex fulcrumPoint = Complex.FromRealNumber(23.0);
가 낫다
```

**Pick One Word per Concept**

e.g. 
- fetch, retrieve & get
- controller & manager

**Don't Pun**
- `Pick One Word per Concept` 규칙을 따르다보면 많은 클래스가 add라는 같은 이름을 가진 메소드가 가지고 있을 수 있다. 이 add 메소드는 한 argument를 받아서 array에 추가하는 기능을 가졌다고 생각해보자.
새로운 클래스를 만들고, 두 arguments를 받아서 concatenate하는 메소드를 만든다. 이 메소드의 이름도 add 여야 할까? 이미 다른 클래스에 존재하는 add 메소드와는 하는 일이 다르기 때문에 이런 경우에는 append 같이 다른 이름을 사용하는 것이 좋다.

**Use Solution Domain/Problem Domain Names**
- 우리의 코드를 읽는 사람들도 programmer라는 것을 잊지 말고, 컴퓨터 관련 용어를 쓰는 것을 주저하지 말자. (e.g. 알고리즘, 패턴, 수학 용어 등등)
- 만약 우리가 사용하려는 이름들이 컴퓨터 관련 용어와 전혀 관련이 없다면, 코드를 읽는 사람이 적어도 domain expert에게 어떤 의미인지 물어볼 수 있도록 최대한 problem domain 용어를 사용하자

**Add Meaningful Context**
```java
// Variables with unclear context
private void printGuessStatistics(char candidate, int count) {
  String number;
  String verb;
  String pluralModifier;

  if (count == 0) {
    number = "no";
    verb = "are";
    pluralModifier = "s";
  } else if (count == 1) {
    number = "1";
    verb = "is";
    pluralModifier = "";
  } else {
    number = Integer.toString(count);
    verb = "are";
    pluralModifier = "s";
  }

  String guessMessage = String.format(
    "There %s %s %s%s", verb, number, candidate, pluralModifier
  );
  print(guessMessage)
}

// Variables have a context
public class GuessStatisticsMessage {
  private String number;
  private String verb;
  private String pluralModifier;
  
  public String make(char candidate, int count) {
    createPluralDependentMessageParts(count);
    return String.format(
      "There %s %s %s%s", verb, number, candidate, pluralModifier
    );
  }

  private void createPluralDependentMessageParts(int count) {
    if (count == 0) {
     thereAreNoLetters();
    } else if (count == 1) {
      thereIsOneLetter();
    } else {
      thereAreManyLetters(count);
    }
  }

  private void thereAreManyLetters(int count) {
    number = Integer.toString(count);
    verb = "are";
    pluralModifier = "s";
  }

  private void thereIsOneLetter() {
    number = "1";
    verb = "are";
    pluralModifier = "s";
  }
}
```

**Don't Add Gratuitous Context**
- 필요없는 문맥은 더하지 말자. 예를 들어, Gas Station Deluxe라는 어플리케이션이 있다. 모든 클래스에 GSD라는 prefix를 붙이지는 말자. 정말 필요없는 일이다

**Final Words**
- 좋은 이름을 고르는 것이 어려운 이유는 뛰어난 설명하는 능력과 공통적인 문화적 배경을 필요로 하기 때문이다.
또한, 다른 개발자들이 콕 찝을까봐 renaming을 겁내하기도 한다. 하지만 우리는 오히려 더 좋은 이름으로 바꿔주는 것에 감사한다. 결국 더 좋은 코드를 만들어내는 방법이고, 우리에게도 코드를 읽기 더 쉽게 바꿔주기 때문이다.