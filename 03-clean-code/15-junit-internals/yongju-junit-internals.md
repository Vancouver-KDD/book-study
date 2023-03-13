# Chapter 15. JUnit Internals

#### The JUnit Framework
```java
// ComparisonCompactorTest.java

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
    String failure= new ComparisonCompactor(1, "ab", "cb").compact(null); 
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
    String failure= new ComparisonCompactor(1, "abcde", "abfde").compact(null); 
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
    String failure= new ComparisonCompactor(0, "abcdde", "abcde").compact(null); 
    assertEquals("expected:<...[d]...> but was:<...[]...>", failure); 
  }

  public void testComparisonErrorOverlapingMatches2Context() { 
    String failure= new ComparisonCompactor(2, "abcdde", "abcde").compact(null); 
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
    assertEquals("expected:<[S&P50]0> but was:<[]0>", failure); 
  }
}

// ComparisonCompactor.java
package junit.framework;

public class ComparisonCompactor {

  private static final String ELLIPSIS = "..."; 
  private static final String DELTA_END = "]"; 
  private static final String DELTA_START = "[";

  private int fContextLength; // -> contextLength;
  private String fExpected;  // -> expected;
  private String fActual; // -> actual;
  private int fPrefix; // -> prefix;
  private int fSuffix; // -> suffix;

  public ComparisonCompactor(int contextLength, String expected, String actual) {
    fContextLength = contextLength;
    fExpected = expected;
    fActual = actual; 
  }

  public String compact(String message) {
    if (fExpected == null || fActual == null || areStringsEqual()) // -> if (shouldNotCompact()) { ... }
     return Assert.format(message, fExpected, fActual);

//  private boolean shouldNotCompact() {
//    return expected == null || actual == null || areStringsEqual();
// }

    findCommonPrefix(); 
    findCommonSuffix(); 
    // [N4] Don't they represent something else?
    String expected = compactString(fExpected); // -> String compactExpected = compactString(expected);
    String actual = compactString(fActual); // -> String compactActual = compactString(actual);
    return Assert.format(message, expected, actual);
  }

/*
  Ver.1

  [G29] Negatives are slightly harder to understand than positives 
  public String compact(String message) {
    if (canBeCompacted()) {
      findCommonPrefix(); 
      findCommonSuffix(); 
      String compactExpected = compactString(expected);
      String compactActual = compactString(actual);
      return Assert.format(message, compactExpected, compactActual);
    }
    
    return Assert.format(message, expected, actual);
  }

  private boolean canBeCompacted() {
    return expected != null && actual != null && !areStringEqual();
  }
*/

/*
  Ver. 2

  private String compactExpected;
  private String compactActual;
  
  public String compact(String message) {
    if (canBeCompacted) {
      compactExpectedAndActual();
      return Assert.format(message, compactExpected, compactActual);   
    }
    return Assert.format(message, expected, actual);
  } 

  private void compactExpectedAndActual() {
    findCommonPrefix(); 
    findCommonSuffix(); 
    String compactExpected = compactString(expected);
    String compactActual = compactString(actual);
  }
*/

/*
  [G11] consistent conventions
  Ver. 3

  private void compactExpectedAndActual() {
    int prefixIndex = findCommonPrefix(); 
    int suffixIndex = findCommonSuffix(prefixIndex); 
    String compactExpected = compactString(expected);
    String compactActual = compactString(actual);
  }
*/ 

  private String compactString(String source) { // -> [N7] name of this function is strange 
// private String formatCompactedComparison(String message)
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

/*
  Ver. 1
  private int findCommonPrefix() { 
    int prefixIndex = 0; 
    int end = Math.min(expected.length(), actual.length()); 

    for (; prefixIndex < end; prefixIndex++) { 
      if (expected.charAt(prefixIndex) != actual.charAt(prefixIndex)) 
      break; 
    } 

    return prefixIndex; 
  }

  private int findCommonSuffix(int prefixIndex) { 
    int expectedSuffix = expected.length() - 1; 
    int actualSuffix = actual.length() - 1; 

    *** prefixIndex -> [G3] a hidden temporal coupling
    for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; actualSuffix--, expectedSuffix--) { 
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) 
      break; 
    } 
    
    return expected.length() - expectedSuffix;   
  }

  Ver. 2
  private void compactExpectedAndActual() { 
    findCommonPrefixAndSuffix(); 
    compactExpected = compactString(expected); 
    compactActual = compactString(actual); 
  }

  private void findCommonPrefixAndSuffix() {
    findCommonPrefix(); 
    
    int expectedSuffix = expected.length() - 1; 
    int actualSuffix = actual.length() - 1; 
    
    for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; actualSuffix--, expectedSuffix--) {
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

  Ver. 3
  private void findCommonPrefixAndSuffix() { 
    findCommonPrefix(); 
    int suffixLength = 1;

    for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) { 
      if (charFromEnd(expected, suffixLength) != charFromEnd(actual, suffixLength)) 
        break; 
    } 
    
    suffixIndex = suffixLength;
  }

  private char charFromEnd(String s, int i) { return s.charAt(s.length()-i); }

  private boolean suffixOverlapsPrefix(int suffixLength) { 
    return actual.length() - suffixLength < prefixLength || expected.length() - suffixLength < prefixLength; 
  }
*/

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
              fContextLength ? ELLIPSIS : ""); }

  private boolean areStringsEqual() { 
    return fExpected.equals(fActual); 
  }
}
```

```java
// Refactored ComparisonCompactor.java
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

  public ComparisonCompactor(int contextLength, String expected, String actual) { 
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

  private boolean shouldBeCompacted() { return !shouldNotBeCompacted(); }

  private boolean shouldNotBeCompacted() { 
    return expected == null || actual == null || expected.equals(actual); 
  }

  private void findCommonPrefixAndSuffix() {
    findCommonPrefix(); 
    suffixLength = 0; 
    
    for (; !suffixOverlapsPrefix(); suffixLength++) {
      if (charFromEnd(expected, suffixLength) != charFromEnd(actual, suffixLength))
        break;
    }
  }

  private char charFromEnd(String s, int i) { return s.charAt(s.length() - i - 1); }

  private boolean suffixOverlapsPrefix() { 
    return actual.length() - suffixLength <= prefixLength || expected.length() - suffixLength <= prefixLength;
  }

  private void findCommonPrefix() { 
    prefixLength = 0; 
    int end = Math.min(expected.length(), actual.length()); 
    for (; prefixLength < end; prefixLength++) 
      if (expected.charAt(prefixLength) != actual.charAt(prefixLength)) 
        break; 
    }

  private String compact(String s) { 
    return new StringBuilder().append(startingEllipsis()).append(startingContext()).append(DELTA_START).append(delta(s)).append(DELTA_END).append(endingContext()).append(endingEllipsis()).toString();
  }

  private String startingEllipsis() { return prefixLength > contextLength ? ELLIPSIS : ""; }

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
    int contextEnd = Math.min(contextStart + contextLength, expected.length()); 
    return expected.substring(contextStart, contextEnd); 
  }

  private String endingEllipsis() { return (suffixLength > contextLength ? ELLIPSIS : ""); }
}
```

위의 리팩토링 과정 처럼 중간 중간 만든 결정을 번복하고 다른 방법으로 다시 코드를 적는 경우가 허다하다. 리팩토링은 한 번에 끝나는 과정이 아니라 많은 시행착오를 거치는 iterative 과정이기 때문이다.

#### Conclusion
리팩토링을 통해 우리는 Boy Scout Rule을 지켰고, 우리가 처음 본 코드보다 더 깔끔하게 만들었다. 처음 코드를 적은 개발자들이 더러운 코드를 썼다는 의미가 아니라, 어떤 코드도 발전을 따라갈 수 없기 떄문에 우리는 코드를 조금씩 조금씩 더 깨끗하고 좋게 유지해야 하는 책임을 가지고 있다는 의미이다.