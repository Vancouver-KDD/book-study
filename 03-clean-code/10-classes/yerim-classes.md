# Classes
### Class Organization
1. Public static constants
2. Private static variables
3. Private instance variables
4. Public functions
5. Private utilities
- This follows the stepdown rule and helps the program read like a **newspaper article**.

##### Encapsulation
- Sometimes we need to make a variable or utility function protected so that it can be accessed by a test.

### Classes Should Be Small!
- As with functions, smaller is the primary rule when it comes to designing classes.
- With classes we measured size by counting **responsibilities**.
- Too Many Responsibilities
  ```java
  public class SuperDashboard extends JFrame implements MetaDataUser {
    public String getCustomizerLanguagesPath()
    public void setSystemConfigPath(String systemConfigPath)
    public String getSystemConfigDocument()
    // ... 70 public methods
  }
  ```
- Small Enough? 👉 No
  ```java
  public class SuperDashboard extends JFrame implements MetaDataUser {
    public Component getLastFocusedComponent()
    public void setLastFocused(Component lastFocused)
    public int getMajorVersionNumber()
    public int getMinorVersionNumber()
    public int getBuildNumber()
  }
  ```
  - Despite its mall number of methods, `SuperDashboard` has too many responsibilities.
  - The `SuperDashboard` provides access to the component that last held the focus, **and** it also allows us to track the version and build numbers.

##### The Single Responsibility Principle (SRP)
- Classes should have one responsibility - one reason to change.
- The small `SuperDashboard` class
  1. It tracks version information that would seemingly need to be updated every time the software gets shipped.
  2. It manages Java Swing components.
- A single-responsibility class
  ```java
  public class Version {
    public int getMajorVersionNumber()
    public int getMinorVersionNumber()
    public int getBuildNumber()
  }
  ```
- We regularly encounter classes that do far too many things. Why?
  - Getting software to work and making software clean are two very different activities.
  - The problem is that too many of us think that we are done once the program works.
  - Many developers fear that a large number of small, single-purpose classes makes it more difficult to understand the bigger picture.
    - _Many small drawers each containing well-defined and well-labeled components._
      vs
      _A few drawers that you just toss everything into._
- We want our systems to be composed of many small classes, not a few large ones. Each small class encapsulates a single responsibility, has a single reason to change, and collaborates with a few others to achieve the desired system behaviors.

##### Cohesion
- In general the more variables a method manipulates the more cohesive that method is to its class.
```java
public class Stack {
  private int topOfStack = 0;
  List<Integer> elements = new LinkedList<Integer>();

  // Only this fails to use both the variables.
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
- We should try to separate the variables and methods into two or more classes such that the new classes are more cohesive.

##### Maintaining Cohesion Results in Many Small Classes
- Before
  ```java
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
        PAGEOFFET = 1;
        while (PAGEOFFSET <= M) {
          System.out.println("The First " + M + " Prime Numbers --- Page " + PAGENUMBER);

          System.out.println("");
          for (ROWOFFSET = PAGEOFFSET; ROWOFFSET < PAGEOFFSET + RR; ROWOFFSET++) {
            for (C = 0; C< CC; C++)
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
  - Problems
    - Deeply indented structure
    - Plethora of odd variables
    - Tightly coupled structure
- After
  - The change was made by writing a test suite that verified the precise behavior of the first program.
  - PrimePrinter.java
    ```java
    package literatePrimes;

    public class PrimePrinter {
      public static void main(String[] args) {
        final int NUMBER_OF_PRIMES = 1000;
        int[] primes = PrimeGenerator.generate(NUMBER_OF_PRIMES);

        final int ROW_PER_PAGE = 50;
        final int COLUMNS_PER_PAGE = 4;
        RowColumnPagePrinter tablePrinter = new RowColumnPagePrinter(ROWS_PER_PAGE, COLUMNS_PER_PAGE, "The First " + NUMBER_OF_PRIMES + " Prime Numbers");
        tablePrinter.print(primes);
      }
    }
    ```
    - The main program is contained in the `PrimePrinter` class all by itself.
    - Its responsibility is to handle the execution environment.
  <br>
  - RowColumnPagePrinter.java
    ```java
    package literatePrimes;

    import java.io.PrintStream;

    public class RowColumnPagePrinter {
      private int rowsPerPage;
      private int columnsPerPage;
      private int numbersPerPage;
      private String pageHeader;
      private PrintStream printStream;

      public RowColumnPagePrinter(int rowsPerPage, int columnsPerPage, String pageHeader) {
        this.rowsPerPage = rowsPerPage;
        this.columnsPerPage = columnsPerPage;
        this.pageHeader = pageHeader;
        numbersPerPage = rowsPerPage * columnsPerPage;
        printStream = System.out;
      }

      public void print(int data[]) {
        int pageNumber = 1;
        for (int firstIndexOnPage = 0; firstIndexOnPage < data.length; firstIndexOnPage += numbersPerPage) {
          int lastIndexOnPage = Math.min(firstIndexOnPage + numbersPerPage - 1, data.length - 1);
          printPageHeader(pageHeader, pageNumber);
          printPage(firstIndexOnPage, lastIndexOnPage, data);
          printStream.println("\f");
          pageNumber++;
        }
      }

      private ovid printPage(int firstIndexOnPage, int lastIndexOnPage, int[] data) {
        int firstIndexOfLastRowOnPage = firstIndexOnPage + rowsPerPage - 1;
        for (int firstIndexInRow = first IndexOnPage; firstIndexInRow <= firstIndexOfLastRowOnPage; firstIndexInRow++) {
          printRow(firstIndexInRow, lastIndexOnPage, data);
          printStream.println("");
        }
      }

      private void printRow(int firstIndexInRow, int lastIndexOnPage, int[] data) {
        for (int column = 0; column < columnsPerPage; column++) {
          int index = firstIndexInRow + column * rowsPerPage;
          if (index <= lastIndexOnPage)
            printStream.format("%10d", data[index]);
        }
      }

      private void printPageHeader(String pageHeader, int pageNumber) {
        printStream.println(pageHeader + " --- Page " + pageNumber);
        printStream.println("");
      }

      public void setOutput(PrintStream printStream) {
        this.printStream = printStream;
      }
    }
    ```
    - This class knows all about how to format a list of numbers into pages with a certain number of rows and columns.
    - If the formatting of the output needed changing, then this is the class that would be affected.
    <br>
  - PrimeGenerator.java
    ```java
    package literatePrimes;

    import java.util.ArrayList;

    public class PrimeGenerator {
      private static int[] primes;
      private static ArrayList<Integer> multiplesOfPrimeFactors;

      protected static int[] generate(int n) {
        primes = new int[n];
        multiplesOfPrimeFactors = new ArrayList<Integer>();
        set2AsFirstPrime();
        checkOddNumbersForSubsequentPrimes();
        return primes;
      }

      private static void set2AsFirstPrime() {
        primes[0] = 2;
        multiplesOfPrimeFactors.add(2);
      }

      private static void checkOddNumbersForSubsequentPrimes() {
        int primeIndex = 1;
        for (int candidate = 3; primeIndex < primes.length; candidate += 2) {
          if (isPrime(candidate))
            primes[primeIndex++] = candidate;
        }
      }

      private static boolean isPrime(int candidate) {
        if (isLeastRelevantMultipleOfNextLargerPrimeFactor(candidate)) {
          multiplesOfPrimeFactors.add(candidate);
          return false;
        }
        return isNotMultipleOfAnyPreviousPrimeFactor(candidate);
      }

      private static boolean isLeastRelevantMultipleOfNextLargerPrimeFactor(int candidate) {
        int nextLargerPrimeFactor = primes[multiplesOfPrimeFactors.size()];
        int leastRelevantMultiple = nextLargerPrimeFactor * nextLargerPrimeFactor;
        return candidate == leastRelevantMultiple;
      }

      private static boolean isNotMultipleOfAnyPreviousPrimeFactor(int candidate) {
        for (int n = 1; n < multiplesOfPrimeFactors.size(); n++) {
          if (isMultipleOfNthPrimeFactor(candidate, n))
            return false;
        }
        return true;
      }

      private static boolean isMultipleOfNthPrimeFactor(int candidate, int n) {
        return candidate == smallestOddNthMultipleNotLessThanCandidate(candidate, n);
      }

      private static int smallestOddNthMultipleNotLessThanCandidate(int candidate, int n) {
        int multiple = multiplesOfPrimeFactors.get(n);
        while (multiple < candidate)
          multiple += 2 * primes[n];
        multiplesOfPrimeFactors.set(n, multiple);
        return multiple;
      }
    }
    ```
    - This class knows how to generate a list prime numbers.
    - The class is just a useful scope in which its variables can be declared and kept hidden.
    - This class will change if the algorithm for computing prime numbers changes.
    <br>
  - Reasons for this growth
    - uses longer, more descriptive variable names.
    - uses function and class declarations as a way to add commentary to the code.
    - uses whitespace and formatting techniques to keep the program readable.

### Organizing for Change
- In a clean system we organize our classes so as to reduce the risk of change.
- Before
  ```java
  public class Sql {
    public Sql(String table, Column[] Columns)
    public String create()
    public String insert(Object[] fields)
    public String selectAll()
    //...
    private String ColumnList(Column[] columns)
    private String valuesList(Object[] fields, final Column[] columns)
    private String selectWithCriteria(String criteria)
    private String placeholderList(Columns[] columns)
  }
  ```
  - SRP violation
    - The `Sql` class must change when we add a new type of statement.
    - It also must change when we alter the details of a single statement type.
  - Private method behavior that applies only to a small subset of a class can be a useful heuristic for spotting potential areas for improvement.
  - However, the primary spur for taking action should be system change (`update` functionality) itself.
    - _If the `Sql` class is deemed logically complete, then we need not worry about separating the responsibilities._
  <br>
- After
  ```java
  abstract public class Sql {
    public Sql(String table, Column[] columns)
    abstract public String generate();
  }

  public class CreateSql extends Sql {
    public CreateSql(String table, Column[] columns)
    @override public String generate()
  }

  //...

  public class Where {
    public Where(String criteria)
    public String generate()
  }

  public class ColumnList {
    public ColumnList(Column[] columns)
    public String generate()
  }
  ```
  - High readability
  - The risk that one function could break another becomes vanishingly small.
  - Easy to test
  - When adding the `update` statements, none of the existing classes need change.
  - This class is open for extension but closed for modification. (OCP)
  <br>
- In an ideal system, we incorporate new features **by extending** the system, **not by making modifications** to existing code.

##### Isolating from Change
- A client class depending upon _concrete details_ is at risk when those details change.
- **Interfaces** and **abstract classes** can help isolate the impact of those details.
- If we're building a `Portfolio` class and it depends upon an external `TokyoStockExchange` API to derive the portfolio's value, we would get a different answer every five minutes.
  ```java
  public interface StockExchange {
    Money currentPrice(String symbol);
  }
  ```
- Testable implementation
  ```java
  public class Portfolio {
    private StockExchange exchange;
    public Portfolio(StockExchange exchange) {
      this.exchange = exchange;
    }
    //...
  }

  public class FixedStockExchangeStub implements StockExchange {
    //...
    public void fix(String symbol, int price) {
      //save fixed price of symbol
    }

    Money currentPrice(String symbol) {
      //return fixed price of symbol
    }
  }

  public class PortfolioTest {
    private FixedStockExchangeStub exchange;
    private Portfolio portfolio;

    @Before
    protected void setUp() throws Exception {
      exchange = new FixedStockExchangeStub();
      exchange.fix("MSFT", 100);
      portfolio = new Portfolio(exchange);
    }

    @Test
    public void GivenFiveMSFTTotalShouldBe500() throws Exception {
      portfolio.add(5, "MSFT");
      Assert.assertEquals(500, portfolio.value())
    }
  }
  ```
- By minimizing coupling in this way, our classes adhere to another class design principle known as the Dependency Inversion Principle (DIP)
  - _In essence, the DIP says that our classes should depend upon abstractions, not on concrete details._
- Instead of being dependent upon the implementation details of the `TokyoStockExchange` class, our `Portfolio` class is now dependent upon the `StockExchange` interface
- The `StockExchange` interface represents the abstract concept of asking for the current price of a symbol.
- This abstraction isolates all of the specific details of obtaining such a price, including from where that price is obtained.
