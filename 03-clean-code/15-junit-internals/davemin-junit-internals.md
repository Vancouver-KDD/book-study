## 15. JUnit Internals

### The JUnit Framework

It is ComparisonCompactor to identify string comparison errors.

``` java
//ComparisonCompactorTest.java
package junit.tests.framework;
import junit.framework.ComparisonCompactor; 
import junit.framework.TestCase;
public class ComparisonCompactorTest extends TestCase {
public void testMessage() {
String failure= new ComparisonCompactor(0, "b", "c").compact("a"); 
assertTrue("a expected:<[b]> but was:<[c]>".equals(failure));
}
public void testStartSame() {
String failure= new ComparisonCompactor(1, "ba", "bc").compact(null); 
assertEquals("expected:<b[a]> but was:<b[c]>", failure);
}
public void testEndSame() {
String  failure= new ComparisonCompactor(1, "ab", "cb").compact(null); 
assertEquals("expected:<[a]b> but was:<[c]b>", failure);
}
public void testSame() {
String failure= new ComparisonCompactor(1, "ab", "ab").compact(null); 
assertEquals("expected:<ab> but was:<ab>", failure);
}
public void testNoContextStartAndEndSame() {
String failure= new ComparisonCompactor(0, "abc", "adc").compact(null); 
assertEquals("expected:<...[b]...> but was:<...[d]...>", failure);
}

public void testStartAndEndContext() {
String failure= new ComparisonCompactor(1, "abc", "adc").compact(null); 
assertEquals("expected:<a[b]c> but was:<a[d]c>", failure);
}
public void testStartAndEndContextWithEllipses() { 
String failure=
       new ComparisonCompactor(1, "abcde", "abfde").compact(null); 
assertEquals("expected:<...b[c]d...> but was:<...b[f]d...>", failure);
}
public void testComparisonErrorStartSameComplete() {
String failure= new ComparisonCompactor(2, "ab", "abc").compact(null); 
assertEquals("expected:<ab[]> but was:<ab[c]>", failure);
}
public void testComparisonErrorEndSameComplete() {
String failure= new ComparisonCompactor(0, "bc", "abc").compact(null); 
assertEquals("expected:<[]...> but was:<[a]...>", failure);
}
public void testComparisonErrorEndSameCompleteContext() {
String failure= new ComparisonCompactor(2, "bc", "abc").compact(null); 
assertEquals("expected:<[]bc> but was:<[a]bc>", failure);
}
public void testComparisonErrorOverlapingMatches() {
String failure= new ComparisonCompactor(0, "abc", "abbc").compact(null); 
assertEquals("expected:<...[]...> but was:<...[b]...>", failure);
}
public void testComparisonErrorOverlapingMatchesContext() {
String failure= new ComparisonCompactor(2, "abc", "abbc").compact(null); 
assertEquals("expected:<ab[]c> but was:<ab[b]c>", failure);
}
public void testComparisonErrorOverlapingMatches2() { 
String failure= new ComparisonCompactor(0, "abcdde",
"abcde").compact(null);
assertEquals("expected:<...[d]...> but was:<...[]...>", failure); 
}
public void testComparisonErrorOverlapingMatches2Context() { 
String failure=
       new ComparisonCompactor(2, "abcdde", "abcde").compact(null); 
assertEquals("expected:<...cd[d]e> but was:<...cd[]e>", failure);
}
public void testComparisonErrorWithActualNull() {
String failure= new ComparisonCompactor(0, "a", null).compact(null); 
assertEquals("expected:<a> but was:<null>", failure);
}
public void testComparisonErrorWithActualNullContext() {
String failure= new ComparisonCompactor(2, "a", null).compact(null);
assertEquals("expected:<a> but was:<null>", failure); 
}
public void testComparisonErrorWithExpectedNull() {
String failure= new ComparisonCompactor(0, null, "a").compact(null); 
assertEquals("expected:<null> but was:<a>", failure);
}
public void testComparisonErrorWithExpectedNullContext() {
String failure= new ComparisonCompactor(2, null, "a").compact(null); 
assertEquals("expected:<null> but was:<a>", failure);
}
public void testBug609972() {
String failure= new ComparisonCompactor(10, "S&P500", "0").compact(null); 
assertEquals("expected:<[S&P50]0> but was:<[]0>", failure);}
}
}

```

> nicely partitioned, reasonably expressive, and simple in structure. 

```java
//ComparisonCompactor.java (Original)
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
  public ComparisonCompactor(int contextLength, 
                             String expected, 
                               String actual) { 
    fContextLength = contextLength;
    fExpected = expected; 
    fActual = actual;
  }
 public String compact(String message) {
    if (fExpected == null || fActual == null || areStringsEqual()) 
      return Assert.format(message, fExpected, fActual);
    findCommonPrefix(); 
    findCommonSuffix();
    String expected = compactString(fExpected); 
    String actual = compactString(fActual);
    return Assert.format(message, expected, actual); 
  }
  private String compactString(String source) { 
    String result = DELTA_START +
                      source.substring(fPrefix, source.length() - 
                        fSuffix + 1) + DELTA_END;
    if (fPrefix > 0)
      result = computeCommonPrefix() + result; 
    if (fSuffix > 0)
      result = result + computeCommonSuffix(); 
    return result;
  }
  private void findCommonPrefix() { 
    fPrefix = 0;
    int end = Math.min(fExpected.length(), fActual.length()); 
    for (; fPrefix < end; fPrefix++) {
      if (fExpected.charAt(fPrefix) != fActual.charAt(fPrefix)) 
        break;
    } 
  }
  private void findCommonSuffix() {
    int expectedSuffix = fExpected.length() - 1; 
    int actualSuffix = fActual.length() - 1; 
    for (;
         actualSuffix >= fPrefix && expectedSuffix >= fPrefix; 
          actualSuffix--, expectedSuffix--) {
      if (fExpected.charAt(expectedSuffix) != fActual.charAt(actualSuffix)) 
        break;
    }
    fSuffix = fExpected.length() - expectedSuffix; 
  }
  private String computeCommonPrefix() {
    return (fPrefix > fContextLength ? ELLIPSIS : "") +
             fExpected.substring(Math.max(0, fPrefix - fContextLength), 
                                    fPrefix);
  }
  private String computeCommonSuffix() {
    int end = Math.min(fExpected.length() - fSuffix + 1 + fContextLength, 
                         fExpected.length());
    return fExpected.substring(fExpected.length() - fSuffix + 1, end) + 
           (fExpected.length() - fSuffix + 1 < fExpected.length() - 
            fContextLength ? ELLIPSIS : "");
  }
 private boolean areStringsEqual() { 
    return fExpected.equals(fActual); 
  }
}

```

> After defactoring

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
    String result =
      "[" + s.substring(pfx, s.length() - sfx + 1) + "]"; 
    if (pfx > 0)
      result = (pfx > ctxt ? "..." : "") +
        s1.substring(Math.max(0, pfx - ctxt), pfx) + result;if (sfx > 0) {
      int end = Math.min(s1.length() - sfx + 1 + ctxt, s1.length()); 
      result = result + (s1.substring(s1.length() - sfx + 1, end) + 
        (s1.length() - sfx + 1 < s1.length() - ctxt ? "..." : "")); 
    }
    return result; 
  }
}

```
위에 코드가 물론 보다 좋은 모습을 갖추었지만 보이스카트 규칙에 따라 즉 최초에 방문했을 때보다 보다 더 깨끗하게 유지한다.에 따라 이행해보자
1. 먼저 member variable들을 profix 접두어를 다듬어 보자 

```java

//before
  private int ctxt;
  private String s1;
  private String s2;
  private int pfx;
  private int sfx;
//after
  private int contextLength;
  private String expected;
  private String actual;
  private int prefix;
  private int suffix;
```

> 2. compact function에 capsulated condition으로 바꾸어보자

```java

//before
public String compact(String message) {
if (expected == null || actual == null || areStringsEqual()) //<-----
      return Assert.format(message, expected, actual);
    findCommonPrefix(); 
    findCommonSuffix();
    String expected = compactString(this.expected); 
    String actual = compactString(this.actual);
    return Assert.format(message, expected, actual); 
  }

//after
public String compact(String message) { 
    if (shouldNotCompact()) //<-----
      return Assert.format(message, expected, actual); 
    findCommonPrefix();
    findCommonSuffix();
    String expected = compactString(this.expected); 
    String actual = compactString(this.actual); 
    return Assert.format(message, expected, actual); 
  }

```

> 3. this.expected and this.actual notation in the compact function는 꽤 unambiguous하도록 수정이 필요함

```java
String compactExpected = compactString(expected); 
    String compactActual = compactString(actual);
```

> 4. 부정적인 구문이 긍정구문에 비해 이해하기 어려우므로 if statement를 긍정구문으로 바꾸어보자
```java
public String compact(String message) { 
    if (canBeCompacted()) { //<-----------
      findCommonPrefix(); 
      findCommonSuffix();
      String compactExpected = compactString(expected); 
      String compactActual = compactString(actual);
	return Assert.format(message, compactExpected, compactActual); 
    } else {
      	return Assert.format(message, expected, actual); 
    }
  }
  private boolean canBeCompacted() {
	return expected != null && actual != null && !areStringsEqual(); 
  }

```
> 5. canBeCompacted function명이 이상하다 왜? 
- false 반환 시 실제 문자열을 압축하지 않을 수돌 있다.
- compact라는 이름이 에러체크 시 예상치 못한 오류 초래가능
- 압축뿐만아니라 지정된 메세지를 반환 할 수 있기에
So, 이름은 다음처러 바꾸어 보자 formatCompactedComparison
```java
public String formatCompactedComparison(String message) {
```

> 6. compactExpectedActual method를 통해서 실제 compacting of the expected and actual strings

``` java
...
  private String compactExpected; 
  private String compactActual;
...
  public String formatCompactedComparison(String message) { 
    if (canBeCompacted()) {
      compactExpectedAndActual();
      return Assert.format(message, compactExpected, compactActual); 
    } else {
	return Assert.format(message, expected, actual); 
    }
  }
  private void compactExpectedAndActual() { 
    findCommonPrefix(); //멤버변수화함
    findCommonSuffix(); //멤버변수화함
    compactExpected = compactString(expected); 
    compactActual = compactString(actual); 
  }
```


> 7.  접미사와 접미사 값을 반환하도록 변경

```java
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
    for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; 
         actualSuffix--, expectedSuffix--) {
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) 
        break;
    }
return expected.length() - expectedSuffix; 
  }
```


8. findCommonPrefix의 prefixIndex를 활용해서 findCommonSuffix의 시간적 효율성 확보

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
    for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; 
         actualSuffix--, expectedSuffix--) {
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) 
        break;
    }
    return expected.length() - expectedSuffix; 
  }

9. prefixIndex parameter의 pass를 좀 더 연계적으로 바꾸면
   공통 접두사 찾기의 이름을 변경하여 공통 접두사 찾기 및 접미사 찾기를 호출합니다
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
    for (;
         actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; 
         actualSuffix--, expectedSuffix--
      ) {
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

10. 공통 접두사와 접미사 찾기를 좀 더 정리하면.(중간에 if구문)

```java
private void findCommonPrefixAndSuffix() { 
    findCommonPrefix();
    int suffixLength = 1;
    for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) { 
      if (charFromEnd(expected, suffixLength) !=
           charFromEnd(actual, suffixLength)) 
        break;
    }
    suffixIndex = suffixLength; 
  }
  private char charFromEnd(String s, int i) { 
    return s.charAt(s.length()-i);}
  private boolean suffixOverlapsPrefix(int suffixLength) { 
    return actual.length() - suffixLength < prefixLength || 
      expected.length() - suffixLength < prefixLength;
  }
```

11. suffixIndex prefixIndex 는 사실상 length다 바꾸자
```java
public class ComparisonCompactor {
...
  private int suffixLength;
...
  private void findCommonPrefixAndSuffix() { 
    findCommonPrefix();
    suffixLength = 0;
    for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) { 
      if (charFromEnd(expected, suffixLength) !=
          charFromEnd(actual, suffixLength)) 
        break;
    } 
  }
  private char charFromEnd(String s, int i) { 
    return s.charAt(s.length() - i - 1);
  }
  private boolean suffixOverlapsPrefix(int suffixLength) { 
    return actual.length() - suffixLength <= prefixLength || 
      expected.length() - suffixLength <= prefixLength;
  }
...
  private String compactString(String source) { 
    String result =
      DELTA_START +
      source.substring(prefixLength, source.length() - suffixLength) + 
      DELTA_END;
    if (prefixLength > 0)
      result = computeCommonPrefix() + result;
    if (suffixLength > 0)
      result = result + computeCommonSuffix(); 
    return result;
  }
...
  private String computeCommonSuffix() {
    int end = Math.min(expected.length() - suffixLength + 
      contextLength, expected.length()
    ); 
    return
      expected.substring(expected.length() - suffixLength, end) + 
      (expected.length() - suffixLength <
        expected.length() - contextLength ? 
        ELLIPSIS : "");
  }
```

12. the +1s를 제거하고나서 if (suffixLength > 0)에 operator 버그 예상
compactString로 테스트하고 재구성해보자

```java
private String compactString(String source) { 
    return
      computeCommonPrefix() + 
      DELTA_START +
      source.substring(prefixLength, source.length() - suffixLength) + 
      DELTA_END +
      computeCommonSuffix(); 
  }
```

> Final code
``` java

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
  public ComparisonCompactor(
    int contextLength, String expected, String actual 
  ) {
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
    return expected == null || 
           actual == null ||
           expected.equals(actual); 
  }
  private void findCommonPrefixAndSuffix() { 
    findCommonPrefix();
    suffixLength = 0;
    for (; !suffixOverlapsPrefix(); suffixLength++) { 
      if (charFromEnd(expected, suffixLength) != 
          charFromEnd(actual, suffixLength)
      )
break; 
    }
  }
  private char charFromEnd(String s, int i) { 
    return s.charAt(s.length() - i - 1);
  }
  private boolean suffixOverlapsPrefix() {
    return actual.length() - suffixLength <= prefixLength || 
      expected.length() - suffixLength <= prefixLength;
  }
  private void findCommonPrefix() { 
    prefixLength = 0;
    int end = Math.min(expected.length(), actual.length()); 
    for (; prefixLength < end; prefixLength++)
      if (expected.charAt(prefixLength) != actual.charAt(prefixLength)) 
        break;
  }
  private String compact(String s) { 
    return new StringBuilder()
      .append(startingEllipsis())
      .append(startingContext())
      .append(DELTA_START)
      .append(delta(s))
      .append(DELTA_END)
      .append(endingContext())
      .append(endingEllipsis())
      .toString(); 
  }
  private String startingEllipsis() {
    return prefixLength > contextLength ? ELLIPSIS : ""; 
  }
  private String startingContext() {
    int contextStart = Math.max(0, prefixLength - contextLength); 
    int contextEnd = prefixLength;
    return expected.substring(contextStart, contextEnd); 
  }
  private String delta(String s) { 
    int deltaStart = prefixLength;
    int deltaEnd = s.length() - suffixLength; 
    return s.substring(deltaStart, deltaEnd); 
  }
  private String endingContext() {
    int contextStart = expected.length() - suffixLength; 
    int contextEnd =
      Math.min(contextStart + contextLength, expected.length()); 
    return expected.substring(contextStart, contextEnd);
  }
private String endingEllipsis() {
    return (suffixLength > contextLength ? ELLIPSIS : ""); 
  }
}
```
