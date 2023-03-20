# Refactoring SerialDate
`SerialDate`는 날짜를 표시하는 자바 클래스입니다.
이 오픈소스를 refactoring하면서 지금까지 책에서 배웠던 내용을 적용하보겠습니다.

## First, Make It Work
테스트들은 모든 컨디션들, 그리고 break될 수 있는 모든 가능성들을 모두 테스트해야 합니다.
테스트들이 시스템을 얼마나 cover하고 있는지 느낌으로 아는 것보다 coverage tool들을 통해서 어느 부분이 테스트 되지 않았는지, 
비교적 쉽게 알아볼 수 있습니다.

시스템내에서 호출되지 않는 코드는 삭제해야 합니다. 코드 관리 시스템내에서 관리되고 있기때문에, dead code를 삭제하는 것을 두려워 할 필요는 없습니다.

예를 들어, `MonthCodeToQuarter`는 사용되지 않고, 또한 사용되지 않으므로 테스트 케이스또한 존재하지 않습니다.

```java
assertEquals(-1, stringToMonthCode("0"));
assertEquals(-1, stringToMonthCode("13"));
```
이 테스트 코드는 comment out 되어있고, 테스트 코드는 성공적으로 패스하지 않습니다.
이 테스트 코드가 가리키는 것은 명확하고, unimplment되어 있는 부분을 구현합니다.

```Java
if ((result < 1) || (result > 12)) {
    result = -1;
    for (int i = 0; i < monthNames.length; i++) {
        if (s.equalsIgnoreCase(shortMonthNames[i])) {
            result = i + 1;
            break;
        }
        if (s.equalsIgnoreCase(monthNames[i])) {
            result = i + 1;
            break;
        }
    }
}
```

또한 test coverage를 통해서 의도하지 않은 것들이 밝혀질 수도 있습니다.
예를 들어 `if (adjust >= 4)`이 코드는 항상 false를 반환한다는 것을 coverage test를 통해서 알아낼수 있습니다.
코드 안에서 `adjust`의 밸류는 항상 negative이기 때문에 이 알고리즘의 구현이 잘못 되어 있다는 것을 알아냈습니다.
이 테스트 코드를 통해서 다음과 같이 소스코드를 수정할수 있습니다.
```Java
 int delta = targetDOW - base.getDayOfWeek();
 int positiveDelta = delta + 7;
 int adjust = positiveDelta % 7;
 if (adjust > 3)
 adjust -= 7;
 return SerialDate.addDays(adjust, base);
 ```

이런식으로 테스트코드를 통해서, 구현 의도를 파악하고, 잘못되어 있는 부분이 있다면 그러한 부분을 고쳐서 코드가 work할 수 있게 합니다.

## Then Make It Right
이렇게 코드가 의도되록 움직이도록 작성을 한 후, 다음은 코드를 올바르게 작성하는 refactoring을 진행하겠습니다.

코드의 처음은 license information, copyrights, authors, 그리고 change history들의 comment들로 구성이 되어있습니다.
change history는 더이상 소스 코드에 저장하지 않습니다. 그렇게 때문에 그것들을 삭제합니다.

또한 같은 패캐지에서 다른 모듈들이 import 되어있는 것은 읽는 이로하여금 피곤하게 합니다.
따라서 import를 shorten합니다.

```java
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

// refactored
import java.text.*;
import java.util.*;
```

클래스 네임은 어떨까요? `SerialDate`의 `Serial`은 `Serializable`에서 온 것이 아닌,
클래스에서 date를 serial number로 구현을 했기때문입니다.
ordinal이 더 어떠한 클래스인지 설명할수 있습니다.
```java
/** The serial number for 1 January 1900. */
public static final int SERIAL_LOWER_BOUND = 2;

/** The serial number for 31 December 9999. */
public static final int SERIAL_UPPER_BOUND = 2958465;
```
또한, `SerialDate`는 어떻게 Date를 구현했는지 이름으로 표현하고 있고, 
구현 방식에 대해서는 숨기는 것이 더 좋습니다. 그렇기 때문에 `SerialDate`대신 `Date`를 사용하는 것이 더 적절합니다.
하지만 자바에서 Date라는 이름을 사용하는 라이브러리가 많으므로 `DayDate`으로 하겠습니다.

또한 `MonthConstants`를 사용하는 것보다 enum으로 Month를 표현하는것이 constant를 inherit하는 것보다 좋습니다.
이렇게 함으로써 많은 client 코드나 내부 코드를 고쳐야하겠지만, 이렇게 함으로써, month의 int값을 validation하는 필요가 없어집니다.
이러한 constants를 enum으로 변경하는 것은 또한 week에도 적용시킬수 있습니다.
```java
public abstract class DayDate implements Comparable,
                                        Serializable {
    public static enum Month {
        JANUARY(1),
        FEBRUARY(2),
        MARCH(3),
        APRIL(4),
        MAY(5),
        JUNE(6),
        JULY(7),
        AUGUST(8),
        SEPTEMBER(9),
        OCTOBER(10),
        NOVEMBER(11),
        DECEMBER(12);
        
        Month(int index) {
            this.index = index;
        }
        
        public static Month make(int monthIndex) {
            for (Month m : Month.values()) {
                if (m.index == monthIndex)
                    return m;
            }
            
            throw new IllegalArgumentException("Invalid month index " + monthIndex);
        }
        
        public final int index;
    }
}
 ```

 우리는 또한 abstract factory 패턴을 사용하여 베이스 클래스의 종속성을 제거할수 있습니다.
 팩토리는 `DayDateFactory`는 `DayDate` 인스턴스를 생성합니다.

 팩토리 코드는 다음과 같이 보여줄수 있습니다.
 ```java
public abstract class DayDateFactory {
    private static DayDateFactory factory = new SpreadsheetDateFactory();
    public static void setInstance(DayDateFactory factory) {
        DayDateFactory.factory = factory;
    }

    protected abstract DayDate _makeDate(int ordinal);
    protected abstract DayDate _makeDate(int day, DayDate.Month month, int year);
    protected abstract DayDate _makeDate(int day, int month, int year);
    protected abstract DayDate _makeDate(java.util.Date date);
    protected abstract int _getMinimumYear();
    protected abstract int _getMaximumYear();
    
    public static DayDate makeDate(int ordinal) {
        return factory._makeDate(ordinal);
    }
    
    public static DayDate makeDate(int day, DayDate.Month month, int year) {
        return factory._makeDate(day, month, year);
    }
    
    public static DayDate makeDate(int day, int month, int year) {
        return factory._makeDate(day, month, year);
    }

    public static DayDate makeDate(java.util.Date date) {
        return factory._makeDate(date);
    }

    public static int getMinimumYear() {
        return factory._getMinimumYear();
    }

    public static int getMaximumYear() {
        return factory._getMaximumYear();
    }
}
 ```
 이 abstract class의 concrete 클래스는 `_makeDate`의 메소드들을 교체하여서 구현할수 있습니다.
 또한 이렇게 `getMaximumYear`과 `getMinimumYear`로 client에는 정보를 알려주고, base case에는 숨길수 있습니다.

 또한 INCLUDE_NONE, INCLUDE_FIRST, INCLUDE_SECOND 및 INCLUDE_BOTH 상수는 날짜 범위의 정의되는 시작과 끝 날짜에 대한 설명이다. 이러한 상수들은 가독성을 높이기 위해서, 이름을 바꾸고, enum으로 변환하고, 불필요한 코드와 코멘트들을 코드에서 정리함으로서 가독성을 높였다.

또한 이렇게 enum으로 빼내던 것이 크기가 커지면, 그것을 클래스 밖으로 꺼내 관리하는 것이 좋다. 그리고 static 으로 선언되어 있던 메소스들을 인스턴스 메소드로 변환시켜, 메소드를 간결하도록 수정했습니다.

```java
public DayDate addDays(int days) {
    return DayDateFactory.makeDate(toOrdinal() + days);
}
```

DayDate의 마지막 내용을 요약하면, 전체 클래스를 다시 한 번더 확인후 개선을 했다. 첫번째로 오래된 코멘트를 짧게 수정하거나 삭제를 했다.
또한, 남아있는 enum들을 자체 파일들로 이동을 시켰습니다. 그리고 static variables과 static method를 DateUtil이라는 클래스로 이동시켰습니다.
열겨형의 모든 `toInt()`를 만들고 인덱스를 private으로 처리했습니다.
그리고 또한 `plusYears`과 `plusMonths` 중복되는 코드를 새로운 메소드로 추출을 했습니다.
그 결과 코드 커버리지가 84%으로 감소했지만, 이는 클래스 자체가 축소되어 커버하지 않는 코드가 생긴것 입니다.
 ## Conclusion
우리는 코드를 refactoring 했을때는 처음보다 더 클린해야 합니다.
그러므로써 다음으로 코드를 보는 사람은 더 이해하기 쉽고, 이용하기 쉬워질것입니다.