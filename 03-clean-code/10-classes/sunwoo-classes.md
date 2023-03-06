# Classes
지금까지 function 레벨에서의 clean한 코드를 작성하는 법을 알아보았다.
하지만 더 높은 code organization level에서도(어떻게 그 함수들이 composition하는지에서도) 주의를 기울어야 한다.

## Class Organization
Java의 Standard는 variables를 선언하는 것으로 부터 클래스가 시작된다.
이 variables들을 public static constanct부터, private static variables, private instance variables를 각각 선언한다.

### Encapsulation
우리는 항상 variable을 private하게 유지하고, encapsulation 해야 하지만, 
테스트를 위해 protected를 사용하는 것도 고려해 볼 수 있다.

## Classes Should Be Small!
class는 우리가 생각하는 것보다 더 작은 단위로 유지되어야 한다.
만약 class가 너무 크다면, 그것은 하나의 class가 너무 많은 responsibilities들을 가지고 있는 것을 의미한다.
class의 naming은 그 class의 사이즈를 결정하는 하나의 척도로 이용 될수있다.
예를 들어 `processor`, `Manager` 혹은 `Super`의 이름들은 한 개의 class에 여러 responsibilities들을 내포하고 있다는 것을 암시한다.

## The Single Responsibility Principle
SRP(Single Responsubility Principle)는 클래스나 모듈이 하나의 변경 이유만들 가져야 한다는 원칙이다. SRP는 클래스의 정의와 크기를 결정하는데 가이드라인을 제공한다.
만약 클래스가 한 개 이상의 이유로 변경이 되어야 한다면, 그것은 SRP를 위배한다는 의미이고, 또 refactoring의 대상이 된다는 것을 의미한다.

```Java
public class SuperDashboard extends JFrame implements MetaDataUser {
    public Component getLastFocusedComponent()
    public void setLastFocused(Component lastFocused)
    public int getMajorVersionNumber()
    public int getMinorVersionNumber()
    public int getBuildNumber()
}
```
위의 코드는 `Component`에 관한 변경이 있을때, 혹은 `Version`에 관한 변경이 있을때 총 두가지의 변경점을 가지고 있다.
class의 크기(number of lines)이 작을지라도 이것은 작은 크기의 클래스가 아니다.

```Java
public class Version {
 public int getMajorVersionNumber()
 public int getMinorVersionNumber()
 public int getBuildNumber()
}
```
다음과 같이 `Version`을 다른 클래스에서 관리함으로써, `Version`에 관련된 변경은 `Version`하나의 클래스에서 변경되도록 할 수 있다.
이렇게 SRP를 적용함으로서 코드의 abstraction을 개선시킬수 있고, 책임을 단위로 더 작게 class들을 관리할수 있다.

## Cohesion
클래스는 적은 숫자의 instance variable를 가져야하고, 클래스 안에 메소드들은 한개 이상의 그 변수들을 조작해야한다. 
일반적으로 많은 variables을 조작을 할수록 그 메소드는 클래스의 cohesion이 증가한다. 높은 cohesion는 클래스와 메소드들을 co-independent하고 한 개의 logic으로 묶을수 있도록 한다.

```Java
public class Stack {
    private int topOfStack = 0;
    List<Integer> elements = new LinkedList<Integer>();
    
    public int size() {
        return topOfStack;
    }
    
    public void push(int element) {
        topOfStack++;
        elements.add(element);
    }

    public int pop() throws PoppedWhenEmpty {
        if (topOfStack == 0)
            throw new PoppedWhenEmpty();
        int element = elements.get(--topOfStack);
        elements.remove(topOfStack);
        return element;
    }
}
```
`Stack`은 높은 cohesion을 가지는 클래스이다. `size`를 제외한 모든 메소드들은 모든 variables들을 사용하고 있다.
만약 몇 몇의 variables들이 한 subset의 function들에서 사용이 된다면, 
그것들을 한개의 class로 분리시키는 식으로 class의 크기를 작게 유지 할 수 있다.

## Maintaining Cohesion Results in Many Small Classes
만약 큰 클래스에서 한 부분을 작은 클래스로 추출할때, 공통적으로 사용되는 variables들을 하나의 instance로 바꾸게되면 코드를 쉽게 추출 할 수 있다. 하지만 이 경우에는 클래스의 cohesion을 잃게 된다. 만약 클래스 내의 메소드가 그 instance에 관해서만 관련이 되어있다면, 그 메소드들은 instance의 메소드들로 옮길수 있다. 이렇게 refactoring한다면 클래스내의 높은 cohesion을 유지할수 있게된다.

```Java
package literatePrimes;

public class PrintPrimes {
 public static void main(String[] args) {
 final int M = 1000;
 final int RR = 50;
 final int CC = 4;
 final int WW = 10;
 final int ORDMAX = 30;
 int P[] = new int[M + 1];
 int PAGENUMBER;
 int PAGEOFFSET;
 int ROWOFFSET;
 int C;
 int J;
 int K;
 boolean JPRIME;
 int ORD;
 int SQUARE;
 int N;
 int MULT[] = new int[ORDMAX + 1];

 J = 1;
 K = 1;
 P[1] = 2;
 ORD = 2;
 SQUARE = 9;

 while (K < M) {
    do {
        J = J + 2;
        if (J == SQUARE) {
            ORD = ORD + 1;
            SQUARE = P[ORD] * P[ORD];
            MULT[ORD - 1] = J;
        }
        N = 2;
        JPRIME = true;
        while (N < ORD && JPRIME) {
            while (MULT[N] < J)
                MULT[N] = MULT[N] + P[N] + P[N];
                if (MULT[N] == J)
                    JPRIME = false;
                N = N + 1;
        }
    } while (!JPRIME);
    K = K + 1;
    P[K] = J;
 }
 {
    PAGENUMBER = 1;
    PAGEOFFSET = 1;
    while (PAGEOFFSET <= M) {
        System.out.println("The First " + M +
           " Prime Numbers --- Page " + PAGENUMBER);
        System.out.println("");
        for (ROWOFFSET = PAGEOFFSET; ROWOFFSET < PAGEOFFSET + RR; ROWOFFSET++){
            for (C = 0; C < CC;C++)
                if (ROWOFFSET + C * RR <= M)
                    System.out.format("%10d", P[ROWOFFSET + C * RR]);
            System.out.println("");
        }
        System.out.println("\f");
        PAGENUMBER = PAGENUMBER + 1;
        PAGEOFFSET = PAGEOFFSET + RR * CC;
    }
 }
}
}
```
위의 프로그램은 하나의 function이 모든 일을 하고 있고, 이것들은 다음과 같이 여러개의 클래스들로 나눌수 있다.
```Java
package literatePrimes;
public class PrimePrinter {
 public static void main(String[] args) {
    final int NUMBER_OF_PRIMES = 1000;
    int[] primes = PrimeGenerator.generate(NUMBER_OF_PRIMES);

    final int ROWS_PER_PAGE = 50;
    final int COLUMNS_PER_PAGE = 4;
    RowColumnPagePrinter tablePrinter =
        new RowColumnPagePrinter(ROWS_PER_PAGE,
                                COLUMNS_PER_PAGE,
                                "The First " + NUMBER_OF_PRIMES +
                                " Prime Numbers");
    tablePrinter.print(primes);
 }
}
```
```Java
package literatePrimes;

import java.io.PrintStream;

public class RowColumnPagePrinter {
    private int rowsPerPage;
    private int columnsPerPage;
    private int numbersPerPage;
    private String pageHeader;
    private PrintStream printStream;

    public RowColumnPagePrinter(int rowsPerPage,
                                int columnsPerPage,
                                String pageHeader) {
        this.rowsPerPage = rowsPerPage;
        this.columnsPerPage = columnsPerPage;
        this.pageHeader = pageHeader;
        numbersPerPage = rowsPerPage * columnsPerPage;
        printStream = System.out;
    }

    public void print(int data[]) {
        int pageNumber = 1;
        for (int firstIndexOnPage = 0;
            firstIndexOnPage < data.length;
            firstIndexOnPage += numbersPerPage) {
            int lastIndexOnPage =
                Math.min(firstIndexOnPage + numbersPerPage - 1,
                        data.length - 1);
            printPageHeader(pageHeader, pageNumber);
            printPage(firstIndexOnPage, lastIndexOnPage, data);
            printStream.println("\f");
            pageNumber++;
        }
    }

    private void printPage(int firstIndexOnPage,
                            int lastIndexOnPage,
                            int[] data) {
        int firstIndexOfLastRowOnPage =
        firstIndexOnPage + rowsPerPage - 1;
        for (int firstIndexInRow = firstIndexOnPage;
            firstIndexInRow <= firstIndexOfLastRowOnPage;
            firstIndexInRow++) {
            printRow(firstIndexInRow, lastIndexOnPage, data);
            printStream.println("");
        }
    }

    private void printRow(int firstIndexInRow,
                        int lastIndexOnPage,
                        int[] data) {
        for (int column = 0; column < columnsPerPage; column++) {
            int index = firstIndexInRow + column * rowsPerPage;
            if (index <= lastIndexOnPage)
                printStream.format("%10d", data[index]);
        }
    }

    private void printPageHeader(String pageHeader,
                                int pageNumber) {
        printStream.println(pageHeader + " --- Page " + pageNumber);
        printStream.println("");
    }

    public void setOutput(PrintStream printStream) {
        this.printStream = printStream;
    }
}
// ... other classes as well
```
다음과 같이 refactoring 했을때 눈에 띄는 변경점은 코드의 양이 많아졌다는 것이다.
이 이유로는 좀더 descriptive한 변수, 메소드 그리고 클래스들을 사용했기 때문이고 또 white space를 사용해서 formatting 했기 때문이다.

## Organizing for Change
시스템은 지속적으로 변화하고, 이 변화들은 시스템이 예상대로 작동하지 않을 리스크를 높인다. 우리는 classes들을 구성한 clean한 시스템은 이러한 리스크를 줄일수 있다.

```Java
public class Sql {
    public Sql(String table, Column[] columns)
    public String create()
    public String insert(Object[] fields)
    public String selectAll()
    public String findByKey(String keyColumn, String keyValue)
    public String select(Column column, String pattern)
    public String select(Criteria criteria)
    public String preparedInsert()
    private String columnList(Column[] columns)
    private String valuesList(Object[] fields, final Column[] columns)
    private String selectWithCriteria(String criteria)
    private String placeholderList(Column[] columns)
}
```
위의 클래스는 아직도 변경해야 할 점이 있고(update sql), 이러한 추가(변경)은 클래스의 행동을 변경시킬수 있는 위험성이 있다.

```Java
abstract public class Sql {
    public Sql(String table, Column[] columns)
    abstract public String generate();
}
public class CreateSql extends Sql {
    public CreateSql(String table, Column[] columns)
    @Override public String generate()
}
```
이렇게 refactoring하게 된다면, 구조화된 각각의 sql로직은 확장에는 열려있고 (새로운 클래스를 추가), 수정에는 닫혀있다. 이렇게 기존의 코드를 수정하는 것이 아닌, `Sql`를 확장 시킴으로서 시스템의 변경에 관해 최소한으로 개입 할 수 있게 된다.

## Isolating from Change
client 클래스가 만약 concrete dependencu class에 의존을 하고 있다면, dependency class의 내부 detail의 변경될때, 의도대로 작동하지 않을 위험이 있다. 이러한 위험을 interface나 abstract를 중간에 위치 시킴으로서 의존하고 있는 클래스 내부 변경으로부터 isolate시킬수 있다.
