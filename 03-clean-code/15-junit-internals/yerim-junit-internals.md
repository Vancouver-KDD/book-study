# JUnit Internals
- JUnit은 유명한 자바 프레임워크들 중 하나다. 그래서 프레임워크의 코드들은 개념이 단순하고, 정의가 명확하며, 구현이 우아하다. 이 챕터에서는 JUnit 프레임워크 코드 예제를 보며 비평해보자!

### The JUnit Framework
- JUnit은 자바를 배우고 싶은 Ken Beck과 Small-talk 테스팅 프레임워크를 배우고 싶은 Eric Gamma가 만나서 세시간 동안의 고도의 작업으로 시작되었다.
- 우리가 볼 모듈은 `ComparisonCompactor`이라 불리는 문자열 비교 오류를 식별하는 데 도움이 되는 똑똑한 코드이다. 예를 들면, `ABCDE`와 `ABXDE` 같은 두 개의 다른 스트링이 주어졌을때,  이 모듈은 `<...B[X]D...>`와 같은 문자열을 생성하여 차이를 나타낸다.
- 이 모듈의 테스트 코드를 보며 더 간단해질 수 있는지, 더 명확해질 수 있는지 비평해보자.
- ComparisonCompactorTest.java
  ```java
  package junit.tests.framework;

  import junit.framework.ComparisonCompactor;
  import junit.framework.TestCase;

  public class ComparisonCompactorTest extends TestCase {

      public void testMessage() {
          String failure = new ComparisonCompactor(0, "b", "c").compact("a");
          assertTrue("a expected:<[b]> but was:<[c]>".equals(failure));
      }

      public void testStartSame() {
          String failure = new ComparisonCompactor(1, "ba", "bc").compact(null);
          assertEquals("expected:<b[a]> but was:<b[c]>", failure);
      }

      public void testEndSame() {
          String failure = new ComparisonCompactor(1, "ab", "cb").compact(null);
          assertEquals("expected:<[a]b> but was:<[c]b>", failure);
      }

      public void testSame() {
          String failure = new ComparisonCompactor(1, "ab", "ab").compact(null);
          assertEquals("expected:<ab> but was:<ab>", failure);
      }

      public void testNoContextStartAndEndSame() {
          String failure = new ComparisonCompactor(0, "abc", "adc").compact(null);
          assertEquals("expected:<...[b]...> but was:<...[d]...>", failure);
      }

      public void testStartAndEndContext() {
          String failure = new ComparisonCompactor(1, "abc", "adc").compact(null);
          assertEquals("expected:<a[b]c> but was:<a[d]c>", failure);
      }

      public void testStartAndEndContextWithEllipses() {
          String failure = new ComparisonCompactor(1, "abcde", "abfde").compact(null);
          assertEquals("expected:<...b[c]d...> but was:<...b[f]d...>", failure);
      }

      public void testComparisonErrorStartSameComplete() {
          String failure = new ComparisonCompactor(2, "ab", "abc").compact(null);
          assertEquals("expected:<ab[]> but was:<ab[c]>", failure);
      }

      public void testComparisonErrorEndSameComplete() {
          String failure = new ComparisonCompactor(0, "bc", "abc").compact(null);
          assertEquals("expected:<[]...> but was:<[a]...>", failure);
      }

      public void testComparisonErrorEndSameCompleteContext() {
          String failure = new ComparisonCompactor(2, "bc", "abc").compact(null);
          assertEquals("expected:<[]bc> but was:<[a]bc>", failure);
      }

      public void testComparisonErrorOverlappingMatches() {
          String failure = new ComparisonCompactor(0, "abc", "abbc").compact(null);
          assertEquals("expected:<...[]...> but was:<...[b]...>", failure);
      }

      public void testComparisonErrorOverlappingMatchesContext() {
          String failure = new ComparisonCompactor(2, "abc", "abbc").compact(null);
          assertEquals("expected:<ab[]c> but was:<ab[b]c>", failure);
      }

      public void testComparisonErrorOverlappingMatches2() {
          String failure = new ComparisonCompactor(0, "abcdde", "abcde").compact(null);
          assertEquals("expected:<...[d]...> but was:<...[]...>", failure);
      }

      public void testComparisonErrorOverlappingMatches2Context() {
          String failure = new ComparisonCompactor(2, "abcdde", "abcde").compact(null);
          assertEquals("expected:<...cd[d]e> but was:<...cd[]e>", failure);
      }

      public void testComparisonErrorWithActualNull() {
          String failure = new ComparisonCompactor(0, "a", null).compact(null);
          assertEquals("expected:<a> but was:<null>", failure);
      }

      public void testComparisonErrorWithActualNullContext() {
          String failure = new ComparisonCompactor(2, "a", null).compact(null);
          assertEquals("expected:<a> but was:<null>", failure);
      }

      public void testComparisonErrorWithExpectedNull() {
          String failure = new ComparisonCompactor(0, null, "a").compact(null);
          assertEquals("expected:<null> but was:<a>", failure);
      }

      public void testComparisonErrorWithExpectedNullContext() {
          String failure = new ComparisonCompactor(2, null, "a").compact(null);
          assertEquals("expected:<null> but was:<a>", failure);
      }

      public void testBug609972() {
          String failure = new ComparisonCompactor(10, "S&P500", "0").compact(null);
          assertEquals("expected:<[S&P50]0> but was:<[]0>", failure);
      }
  }
  ```
  * 이 테스트 코드는 100% 커버되었고, 이것은 코드가 잘 작동한다는 높은 자신감과 저자들의 장인정신에 대한 높은 리스펙을 가져다준다.

- ComparisonCompactor.java (Original)
  ```java
  package junit.framework;

  public class ComparisonCompactor {

      private static final String ELLIPSIS = "...";
      private static final String DELTA_END = "]";
      private static final String DELTA_START = "[";

      private int fContextLength;
      private String fExpected;
      private String fActual;
      private int fPrefix;
      private int fSuffix;

      public ComparisonCompactor(int contextLength, String expected, String actual) {
          fContextLength = contextLength;
          fExpected = expected;
          fActual = actual;
      }

      public String compact(String message) {
          if (fExpected == null || fActual == null || areStringsEqual()) {
              return Assert.format(message, fExpected, fActual);
          }

          findCommonPrefix();
          findCommonSuffix();
          String expected = compactString(fExpected);
          String actual = compactString(fActual);
          return Assert.format(message, expected, actual);
      }

      private String compactString(String source) {
          String result = DELTA_START + source.substring(fPrefix, source.length() - fSuffix + 1) + DELTA_END;
          if (fPrefix > 0) {
              result = computeCommonPrefix() + result;
          }
          if (fSuffix > 0) {
              result = result + computeCommonSuffix();
          }
          return result;
      }

      private void findCommonPrefix() {
          fPrefix = 0;
          int end = Math.min(fExpected.length(), fActual.length());
          for (; fPrefix < end; fPrefix++) {
              if (fExpected.charAt(fPrefix) != fActual.charAt(fPrefix)) {
                  break;
              }
          }
      }

      private void findCommonSuffix() {
          int expectedSuffix = fExpected.length() - 1;
          int actualSuffix = fActual.length() - 1;
          for (; actualSuffix >= fPrefix && expectedSuffix >= fPrefix; actualSuffix--, expectedSuffix--) {
              if (fExpected.charAt(expectedSuffix) != fActual.charAt(actualSuffix)) {
                  break;
              }
          }
          fSuffix = fExpected.length() - expectedSuffix;
      }

      private String computeCommonPrefix() {
          return (fPrefix > fContextLength ? ELLIPSIS : "") + fExpected.substring(Math.max(0, fPrefix - fContextLength), fPrefix);
      }

      private String computeCommonSuffix() {
          int end = Math.min(fExpected.length() - fSuffix + 1 + fContextLength, fExpected.length());
          return fExpected.substring(fExpected.length() - fSuffix + 1, end) + (fExpected.length() - fSuffix + 1 < fExpected.length() - fContextLength ? ELLIPSIS : "");
      }

      private boolean areStringsEqual() {
          return fExpected.equals(fActual);
      }
  }
  ```
  * 이 모듈을 보면, 긴 이름과 `+1`같은 이상한 부분에 대해서 불만이 있을 수도 있지만, 전체적으로 보기 좋다. 이 코드는 원래 아래와 같은 코드처럼 생겼을 수도 있다.

- ComparisonCompactor.java (defactored)
  ```java
  package junit.framework;

  public class ComparisonCompactor {
    private int ctxt;
    private String s1;
    private String s2;
    private int pfx;
    private int sfx;
    public ComparisonCompactor(int ctxt, String s1, String s2) {
      this.ctxt = ctxt;
      this.s1 = s1;
      this.s2 = s2;
    }

    public String compact(String msg) {
      if (s1 == null || s2 == null || s1.equals(s2))
        return Assert.format(msg, s1, s2);

      pfx = 0;
      for (; pfx < Math.min(s1.length(), s2.length()); pfx++) {
        if (s1.charAt(pfx) != s2.charAt(pfx))
          break;
      }
      int sfx1 = s1.length() - 1;
      int sfx2 = s2.length() - 1;
      for (; sfx2 >= pfx && sfx1 >= pfx; sfx2--, sfx1--) {
        if (s1.charAt(sfx1) != s2.charAt(sfx2))
          break;
      }
      sfx = s1.length() - sfx1;
      String cmp1 = compactString(s1);
      String cmp2 = compactString(s2);
      return Assert.format(msg, cmp1, cmp2);
    }

    private String compactString(String s) {
      String result = "[" + s.substring(pfx, s.length() - sfx + 1) + "]";
      if (pfx > 0)
        result = (pfx > ctxt ? "..." : "") + s1.substring(Math.max(0, pfx - ctxt), pfx) + result;
      if (sfx > 0) {
        int end = Math.min(s1.length() - sfx + 1 + ctxt, s1.length());
        result = result + (s1.substring(s1.length() - sfx + 1, end) + (s1.length() - sfx + 1 < s1.length() - ctxt ? "..." : ""));
      }
      return result;
    }
  }
  ```

- 저자가 이 모듈을 매우 좋은 상태로 남겼지만, Boy Scout Rule에 의하면 우리는 이걸 발견했을 때보다 더 클린하게 남겨둬야 한다. 그러면 우리는 어떻게 Original 코드를 개선할 수 있을까?
- `f` prefix 제거하기. 저자는 신경쓰지 않지만, 요즘 개발 환경에서는 코딩 중복으로 만든다.
  ```java
  private int contextLength;
  private String expected;
  private String actual;
  private int prefix;
  private int suffix;
  ```
- `if`문의 조건절 메서드로 캡슐화하기.
  ```java
  //Before
  public String compact(String message) {
    if (expected == null || actual == null || areStringsEqual())
      return Assert.format(message, expected, actual);
    findCommonPrefix();
    findCommonSuffix();
    String expected = compactString(this.expected);
    String actual = compactString(this.actual);
    return Assert.format(message, expected, actual);
  }

  //After
  public String compact(String message) {
    if (shouldNotCompact())
      return Assert.format(message, expected, actual);
    findCommonPrefix();
    findCommonSuffix();
    String expected = compactString(this.expected);
    String actual = compactString(this.actual);
    return Assert.format(message, expected, actual);
  }

  private boolean shouldNotCompact() {
    return expected == null || actual == null || areStringsEqual();
  }
  ```
- 이름을 모호하게 짓지 않기. 우리가 prefix를 제거하기로 했기 때문에, 멤버 변수와 함수 안의 변수의 이름이 같은 경우가 생겼다. 저자는 `this` notation을 사용하는 것 보다는, 모호하지 않게 이름을 나타내도록 이름을 짓는 걸 선호한다.
  ```java
  //Before 1
  String expected = compactString(fExpected);
  String actual = compactString(fActual);

  //Before 2
  String expected = compactString(this.expected);
  String actual = compactString(this.actual);

  //After
  String compactExpected = compactString(expected);
  String compactActual = compactString(actual);
  ```
- Negatives 보다는 Positives 사용하기.
  ```java
  //Before
  public String compact(String message) {
    if (shouldNotCompact())
      return Assert.format(message, expected, actual);
    findCommonPrefix();
    //...
  }

  private boolean shouldNotCompact() {
    return expected == null || actual == null || areStringsEqual();
  }

  //After
  public String compact(String message) {
    if (canBeCompacted()) {
      findCommonPrefix();
      //...
    } else {
      return Assert.format(message, expected, actual);
    }
  }

  private boolean canBeCompacted() {
    return expected != null && actual != null && !areStringsEqual();
  }
  ```
- 함수의 이름을 실제 동작과 맞추어서 짓자. `compact` 함수는 실제 동작이 수행되지 않을 경우의 사이드 이펙트를 이름에서 숨기고 있다.
  ```java
  //Before
  public String compact(String message) {
    //...
  }

  //After
  public String formatCompactedComparison(String message) {
    //...
  }
  ```
- 실제 compacting 하는 부분을 메서드로 분리하고, format 하는 함수와 compact 하는 함수의 역할을 분리하자.
  ```java
  //...
  private String compactExpected;
  private String compactActual;
  //...

  public String formatCompactedComparison(String message) {
    if (canBeCompacted()) {
      compactExpectedAndActual();
      return Assert.format(message, compactExpected, compactActual);
    } else {
      return Assert.format(message, expected, actual);
    }
  }

  private void compactExpectedAndActual() {
    findCommonPrefix();
    findCommonSuffix();
    compactExpected = compactString(expected);
    compactActual = compactString(actual);
  }
  ```

- `compactExpectedAndActual()` 함수에서는 일관된 convention을 사용하고 있지 않다. 그래서 `findCommonPrefix()` 와 `findCommonSuffix()`에서도 해당 값들을 리턴해주는 형태로 바꿔보자.
  ```java
  //...
  private int prefixIndex;
  private int suffixIndex;
  //...

  private void compactExpectedAndActual() {
    prefixIndex = findCommonPrefix();
    suffixIndex = findCommonSuffix();
    compactExpected = compactString(expected);
    compactActual = compactString(actual);
  }

  private int findCommonPrefix() {
    int prefixIndex = 0;
    int end = Math.min(expected.length(), actual.length());
    for (; prefixIndex < end; prefixIndex++) {
      if (expected.charAt(prefixIndex) != actual.charAt(prefixIndex))
        break;
    }
    return prefixIndex;
  }

  private int findCommonSuffix() {
    int expectedSuffix = expected.length() - 1;
    int actualSuffix = actual.length() - 1;
    for (; actualSuffix >= prefixIndex&& expectedSuffix >= prefixIndex; actualSuffix--, expectedSuffix--) {
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix))
        break;
    }
    return expected.length() - expectedSuffix;
  }
  ```

- `findCommonSuffix`를 주의깊게 검사해보니, `findCommonPrefix`에 의해서 계산되는 `prefixIndex`에 의존한다는 사실을 통해 숨겨진 일시적인 coupling이 드러났다. 만약 두 함수가 순서대로 호출되지 않으면, 디버깅하기가 어려울 것이다. 이 문제를 해결하기 위해서 `prefixIndex`를 `findCommonSuffix`에 argument로 넣어주자.
  ```java
  private void compactExpectedAndActual() {
    prefixIndex = findCommonPrefix();
    suffixIndex = findCommonSuffix(prefixIndex);
    compactExpected = compactString(expected);
    compactActual = compactString(actual);
  }

  private int findCommonSuffix(int prefixIndex) {
    int expectedSuffix = expected.length() - 1;
    int actualSuffix = actual.length() - 1;
    for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex;actualSuffix--, expectedSuffix--) {
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix))
        break;
      }
    return expected.length() - expectedSuffix;
  }
  ```

- 하지만 저자는 `prefixIndex`를 argument로 넣은 것에 대하여 독단적이라며 아직 불만이 있다. 이것은 순서를 정하는 역할을 하지만, 이에 대한 설명이 없기 떄문에 다른 개발자가 다시 원래대로 돌려놓을 수도 있다.
  ```java
  private void compactExpectedAndActual() {
    findCommonPrefixAndSuffix();
    compactExpected = compactString(expected);
    compactActual = compactString(actual);
  }

  private void findCommonPrefixAndSuffix() {
    findCommonPrefix();
    int expectedSuffix = expected.length() - 1;
    int actualSuffix = actual.length() - 1;
    for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; actualSuffix--, expectedSuffix-- ) {
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix))
        break;
    }
    suffixIndex = expected.length() - expectedSuffix;
  }

  private void findCommonPrefix() {
    prefixIndex = 0;
    int end = Math.min(expected.length(), actual.length());
    for (; prefixIndex < end; prefixIndex++)
      if (expected.charAt(prefixIndex) != actual.charAt(prefixIndex))
        break;
  }
  ```

- 우리는 `findCommonPrefixAndSuffix`로 변경하면서, `findCommonPrefix`와 `findCommonSuffix`를 원래대로 돌려놓았다. 이렇게 함으로써, 이전보다 더 극적으로 시간적 특성을 설정하였다. 이제 `findCommonPrefixAndSuffix`를 클린하게 해보자.
  ```java
  private void findCommonPrefixAndSuffix() {
    findCommonPrefix();
    int suffixLength = 1;
    for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) {
      if (charFromEnd(expected, suffixLength) !=charFromEnd(actual, suffixLength))
        break;
    }
    suffixIndex = suffixLength;
  }

  private char charFromEnd(String s, int i) {
    return s.charAt(s.length()-i);
  }

  private boolean suffixOverlapsPrefix(int suffixLength) {
    return actual.length() - suffixLength < prefixLength || expected.length() - suffixLength < prefixLength;
  }
  ```

- 위 코드에서는 `suffixIndex`가 실제로는 suffix의 length인데도 이름을 제대로 짓지 못했다. length와 index는 동의어임에도 불구하고 여기서는 length를 쓰는 것이 더 일관적이다. 문제는 `suffixIndex`가 zero 베이스가 아니라는 것이다. `suffixIndex`는 1 베이스이며 실제 길이도 아니다. 이것은 `computeCommonSuffix`에서 `+1`을 더한 이유이다.
  ```java
  private String compactString(String source) {
    String result = DELTA_START + source.substring(prefixLength, source.length() - suffixLength) + DELTA_END;
    if (prefixLength > 0)
      result = computeCommonPrefix() + result;

    if (suffixLength > 0)
      result = result + computeCommonSuffix();

    return result;
  }
  ```
- `suffixLength`가 이전보다 1만큼 줄었으니 operator가 `>`에서 `>=`로 바뀌어야한다. 말이 안되는 것 같지만, 이제는 말이 된다. 이제는 if문에서 zero length suffix를 붙이는 걸 방지하기 때문이다. 이전에는 작동하지 않았었다.
  ```java
  private String compactString(String source) {
    return computeCommonPrefix() + DELTA_START + source.substring(prefixLength, source.length() - suffixLength) + DELTA_END + computeCommonSuffix();
  }
  ```

- ComparisonCompactor.java (final)
  ```java
  package junit.framework;
  public class ComparisonCompactor {
    private static final String ELLIPSIS = "...";
    private static final String DELTA_END = "]";
    private static final String DELTA_START = "[";
    private int contextLength;
    private String expected;
    private String actual;
    private int prefixLength;
    private int suffixLength;

    public ComparisonCompactor( int contextLength, String expected, String actual ) {
      this.contextLength = contextLength;
      this.expected = expected;
      this.actual = actual;
    }

    public String formatCompactedComparison(String message) {
      String compactExpected = expected;
      String compactActual = actual;

      if (shouldBeCompacted()) {
        findCommonPrefixAndSuffix();
        compactExpected = compact(expected);
        compactActual = compact(actual);
      }
      return Assert.format(message, compactExpected, compactActual);
    }

    private boolean shouldBeCompacted() {
      return !shouldNotBeCompacted();
    }

    private boolean shouldNotBeCompacted() {
      return expected == null || actual == null || expected.equals(actual);
      } private void findCommonPrefixAndSuffix() { findCommonPrefix(); suffixLength = 0; for (; !suffixOverlapsPrefix(); suffixLength++) { if (charFromEnd(expected, suffixLength) != charFromEnd(actual, suffixLength) )break; } } private char charFromEnd(String s, int i) { return s.charAt(s.length() - i - 1); } private boolean suffixOverlapsPrefix() { return actual.length() - suffixLength <= prefixLength || expected.length() - suffixLength <= prefixLength; } private void findCommonPrefix() { prefixLength = 0; int end = Math.min(expected.length(), actual.length()); for (; prefixLength < end; prefixLength++) if (expected.charAt(prefixLength) != actual.charAt(prefixLength)) break; } private String compact(String s) { return new StringBuilder() .append(startingEllipsis()) .append(startingContext()) .append(DELTA_START) .append(delta(s)) .append(DELTA_END) .append(endingContext()) .append(endingEllipsis()) .toString(); } private String startingEllipsis() { return prefixLength > contextLength ? ELLIPSIS : ""; } private String startingContext() { int contextStart = Math.max(0, prefixLength - contextLength); int contextEnd = prefixLength; return expected.substring(contextStart, contextEnd); } private String delta(String s) { int deltaStart = prefixLength; int deltaEnd = s.length() - suffixLength; return s.substring(deltaStart, deltaEnd); } private String endingContext() { int contextStart = expected.length() - suffixLength; int contextEnd = Math.min(contextStart + contextLength, expected.length()); return expected.substring(contextStart, contextEnd); }private String endingEllipsis() { return (suffixLength > contextLength ? ELLIPSIS : ""); }}
  ```

### Conclusion
> 우리 각자는 우리가 코드를 발견했을 때보다 조금 더 나은 상태로 남겨둘 책임이 있다!
