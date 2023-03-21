# Refactoring SerialDate
SerialDate (Listing B-1, 349페이지)는 a date in Java 클래스로 챕터 진행.



## First, Make It Work
SerialDateTests라는 클래스에는 unit test가 존재하는데 테스트를 자세히 보면 모든 테스트를 진행하지 않습니다. 
물론 테스트 자체의 결과는 모두 패스합니다.
그래서 Clover라는 framework를 통해서 테스트되는 것과 그렇지않은 것을 구분했습니다.
185개중 91개만 실행한다고 보고합니다.
그래서 모든 클래스를 이해하고 refactor하는 것이 목표!
그래서 작성된 BobsSerialDateTest.java에 꽤 많은 부분이 주석처리가 되어져있다.
이들은 test를 pass하지 못했다. 하지만 90%이상이 정상적인 테스트가 이루어진다고 Clover가 보고했다.
그래서 모든 테스트가 통과하도록 Refactoring 될것이다. 
## Then Make It Right
Serial Date를 위에서부터 아래로 개선할것. 변경 될 때마다 SerialDate에 대한 improved된 unit테스트와 JCommon 장치 테스트를 실행. 
따라서 여기에 표시된 모든 변경 사항이 모든 JCommon에 적용될 것.

1. line 1, license information, copyrights, authors, and change history에서 합법적인 부분이 언급되어져야 하므로 license information과 copyrights는 존재해야한다.
다만, 변경 이력의 경우 현재는  source code control tools의 역할이므로 지우도록한다.

2. import의 각 기술된 종류들도 java.text.* and java.util.*을 통해 줄이도록한다.
```java
61 import java.io.Serializable;
62 import java.text.DateFormatSymbols;
63 import java.text.SimpleDateFormat;
64 import java.util.Calendar;
65 import java.util.GregorianCalendar;
```
3. line 67 HTML 포맷이 javadoc에 있을 필요는 없으므로 이 또한 수정이 필요하다.
```java
67 /**
68 * An abstract class that defines our requirements for manipulating dates,
69 * without tying down a particular implementation.
70 * <P>
71 * Requirement 1 : match at least what Excel does for dates;
72 * Requirement 2 : class is immutable;
73 * <P>
74 * Why not just use java.util.Date? We will, when it makes sense. At times,
75 * java.util.Date can be *too* precise - it represents an instant in time,
76 * accurate to 1/1000th of a second (with the date itself depending on the
77 * time-zone). Sometimes we just want to represent a particular day (e.g. 21
78 * January 2015) without concerning ourselves about the time of day, or the
79 * time-zone, or anything else. That's what we've defined SerialDate for.
80 * <P>
81 * You can call getInstance() to get a concrete subclass of SerialDate,
82 * without worrying about the exact implementation.
83 *
84 * @author David Gilbert
85 */
```

4. line 86 SerialDate 라는 클래스의 이름을 보자.  
```
86 public abstract class SerialDate implements Comparable,
87 						Serializable,
88 						MonthConstants {
```
   Serial이라는 의미가 일련번호의 의미를 암시하고 있음을 알 수 있다 여기 그러기에는 두가지의 문제점이 보인다. 
   첫번째, "일련 번호"라는 용어는 날짜보다는 제품 식별 표시와 더 관련이 있기에 조금은 동떨어져있는 느낌
   두번째, 이는 이미 implementation의 단계를 의미한다. 현재 클래스가 Abstract class임을 감안할때 이는 구현의 상위단계인 좀 더 넓은 의미여야한다. Date로 클래스 이름을 저자는 권유

    ==> DayDate로 최종결정. Date라는 이름의 클래스가 Java에 이미 존재하므로 

5. 위 구문에서 DayDate(SerialDate)는 왜 MonthConstants를 상속 받고 있는가?

   사실 class MonthConstants (Listing B-3, page 372) is just a bunch of static ﬁnal constants that deﬁne the months
   이를 MonthConstants.January 이런 식으로 활용하기 보다는 enum으로 가는 것이 옳으므로 수정대상이다.
   해당 수정으로 relavant class와 its의 사용처들이 모두 바꾸는데 저자는 한시간이 걸렸고 이제 int로 리턴값을 받던 함수는 이젠 enum타입 Month로 받게 된다.
   이로인해 isValidMonthCode, monthCodeToQuarter와 같은 확인을 위한 체킹로직은 줄일 수 있게된다.
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
```
6. line 91, 아래 javadoc의 설명에 의하면 자동으로 serialVersionUID를 컴파일러가 생성을 하지만 javadoc도 매뉴얼하게 생성할 것을 권장하고 있지만
```
90 /** For serialization. */
91 private static final long serialVersionUID = -293716040467423637L;
```

>> According to the docs for java.io.Serializable, if a serializable class does not explicitly declare a serialVersionUID, then the serialization runtime will calculate a default serialVersionUID value for that class based on various aspects of the class, as described in the Java(TM) Object Serialization Specification.   
   	
저자는 serialVersionUID를 변경치 않아 발생하는 에러를 Trace하는 것보다 InvalidClassException이 낫기 때문이다.

>> InvalidClassException 이란? receiver system이  the serialization process 중에 사용되어지고 있는 serialVersionUID의 클래스와 다른 serialVersionUID의 클래스를 load할 경우 발생 Exception

명확하게 왜 발생하는 이유를 확인하고 접근하는 에러수정방식이 좀더 빠르고 효율적이라는 생각이든다.

7. line 93, 클래스 이름과 중복된 주석 삭제한다.
```java
93 /** Date format symbols. */
94 public static final DateFormatSymbols
95 DATE_FORMAT_SYMBOLS = new SimpleDateFormat().getDateFormatSymbols();
```

8. line 97 and line 100, the earliest and latest possible dates의 serial numbers이며 이는 아래와 같이 간결해진다.
```java
//before
97 /** The serial number for 1 January 1900. */
98 public static final int SERIAL_LOWER_BOUND = 2;
99
100 /** The serial number for 31 December 9999. */
101 public static final int SERIAL_UPPER_BOUND = 2958465;
```
```java
//after
 public static final int EARLIEST_DATE_ORDINAL = 2;     // 1/1/1900 
 public static final int LATEST_DATE_ORDINAL = 2958465; // 12/31/9999
```
왜 EARLIEST_DATE_ORDINAL가 2일까? 이에 대한 해답은 SpreadsheetDate.java에서 찾는다.
```java
71 * Excel uses the convention that 1-Jan-1900 = 1. This class uses the
72 * convention 1-Jan-1900 = 2.
73 * The result is that the day number in this class will be different to the
74 * Excel figure for January and February 1900...but then Excel adds in an extra
75 * day (29-Feb-1900 which does not actually exist!) and from that point forward
76 * the day numbers will match.
```
머 결국 윤달에 대해 다루는 부분이고 윤달 이후에는 같아질 거라는 거다.
어쨌든 이에 대한 설명이 잘반영된 곳이 SpreadsheetDate.java라면 해당 두개의 variable은 DayDate클래스가 아닌 SpreadsheetDate.java에 속해야 한다는 결론에 저자는 이른다.

9. line 104,107  MINIMUM_YEAR_SUPPORTED, and MAXIMUM_YEAR_SUPPORTED는 DayDate가 추상클래스라는 전제하에 구현의 암시를 보여주면 안된다.
```
103 /** The lowest year value supported by this date format. */
104 public static final int MINIMUM_YEAR_SUPPORTED = 1900;
105
106 /** The highest year value supported by this date format. */
107 public static final int MAXIMUM_YEAR_SUPPORTED = 9999;
```
그런데, RelativeDayOfWeekRule.java에서 사용되는 것으로 확인이 된다. year 파라미터가 정상적인지 확인하기 위해서 말이다.
즉, 딜레마는 추상 클래스의 사용처인 RelativeDayOfWeekRule.java가 그것의 구현의 실제 info 값을 필요로 한다는 것이다.
"DayDate" 클래스 자체를 어지르지 않고 이 정보를 어떻게 제공?
```java
//RelativeDayOfWeekRule.java
174 public SerialDate getDate(final int year) {
175
176 // check argument...
177 if ((year < SerialDate.MINIMUM_YEAR_SUPPORTED)
178 	|| (year > SerialDate.MAXIMUM_YEAR_SUPPORTED)) {
179 	throw new IllegalArgumentException(
180 		"RelativeDayOfWeekRule.getDate(): year outside valid range.");
181 }
182
183 // calculate the date...
184 SerialDate result = null;
185 final SerialDate base = this.subrule.getDate(year);
186
187 	if (base != null) {
188 	switch (this.relative) {
189 		case(SerialDate.PRECEDING):
190 			result = SerialDate.getPreviousDayOfWeek(this.dayOfWeek,
191 				base);
192 			break;
193 		case(SerialDate.NEAREST):
194 			result = SerialDate.getNearestDayOfWeek(this.dayOfWeek,
195 				base);
196 			break;
197 		case(SerialDate.FOLLOWING):
198 			result = SerialDate.getFollowingDayOfWeek(this.dayOfWeek,
199 				base);
200 			break;
201 		default:
202 			break;
203 		}
204 	}
205 	return result;
206
207 }
208
209 }
```
DayDate는 결국 인스턴스를 반환해야하는데  GetPreviousDayOfWeek, GetNearDayOfWeek 또는 GetFollowingDayOfWeek의 세 가지 함수 중 하나로 생성되서 반환되어 제공하는데 
이는 결국  DayDate.Java(구 SerialDate.Java)내에 line 638-724에서 제공되며 이 함수들의 리턴밸류도한 addDays(line 808)를 통해 SerialDate의 인스턴스가 SpreadsheetDate까지 거쳐서 제공된다.
base 추상 클래스가 파생되는 것에 대해 인지하고 있는 형태는 옳바르지 않고 이를 ABSTRACT FACTORY pattern에 의해 재정비될 수 있고 
DayDate 인스턴스를 생성, 최대/최소날짜에 대해서도 정보를 제공할 수 있게 만듭니다.
추상 메소드에 delegate하는 정적 메소드는 싱글톤, 데코레이터 및 추상 팩토리 패턴의 조합을 사용한다.
```java
//ABSTRACT FACTORY pattern
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
MINIMUM_YEAR_SUPPORTED and MAXIMUM_YEAR_SUPPORTED variables가 해당 변수가 속한 SpreadsheetDate 이동
```java
//SpreadsheetDateFactory
public class SpreadsheetDateFactory extends DayDateFactory { 
  public DayDate _makeDate(int ordinal) {
    return new SpreadsheetDate(ordinal); 
  }
  public DayDate _makeDate(int day, DayDate.Month month, int year) { 
    return new SpreadsheetDate(day, month, year);
  }
  public DayDate _makeDate(int day, int month, int year) { 
    return new SpreadsheetDate(day, month, year);
  }
  public DayDate _makeDate(Date date) {
    final GregorianCalendar calendar = new GregorianCalendar(); 
    calendar.setTime(date);
    return new SpreadsheetDate( 
      calendar.get(Calendar.DATE),
      DayDate.Month.make(calendar.get(Calendar.MONTH) + 1), 
      calendar.get(Calendar.YEAR));
  }
  protected int _getMinimumYear() {
    return SpreadsheetDate.MINIMUM_YEAR_SUPPORTED; 
  }
  protected int _getMaximumYear() {
    return SpreadsheetDate.MAXIMUM_YEAR_SUPPORTED; 
  }
}
```
10. the day constants
```java
109 /** Useful constant for Monday. Equivalent to java.util.Calendar.MONDAY. */
110 public static final int MONDAY = Calendar.MONDAY;
```
아래와 같이 enum으로 변경활용
```java
//Day.java (Final)
1 package org.jfree.date;
2
3 import java.util.Calendar;
4 import java.text.DateFormatSymbols;
5
6 public enum Day {
7  MONDAY(Calendar.MONDAY),
8  TUESDAY(Calendar.TUESDAY),
9  WEDNESDAY(Calendar.WEDNESDAY),
10 THURSDAY(Calendar.THURSDAY),
11 FRIDAY(Calendar.FRIDAY),
12 SATURDAY(Calendar.SATURDAY),
13 SUNDAY(Calendar.SUNDAY);
14
15 private final int index;
16 private static DateFormatSymbols dateSymbols = new DateFormatSymbols();
17
18 Day(int day) {
19 	index = day;
20 }
21
22 public static Day fromInt(int index) throws IllegalArgumentException {
23 	for (Day d : Day.values())
24 		if (d.index == index)
25 		return d;
26 	throw new IllegalArgumentException(
27 		String.format("Illegal day index: %d.", index));
28 }
29
30 public static Day parse(String s) throws IllegalArgumentException {
31 	String[] shortWeekdayNames =
32 		dateSymbols.getShortWeekdays();
33 	String[] weekDayNames =
34 		dateSymbols.getWeekdays();
35
36 	s = s.trim();
37 	for (Day day : Day.values()) {
38 	if (s.equalsIgnoreCase(shortWeekdayNames[day.index]) ||
39 		s.equalsIgnoreCase(weekDayNames[day.index])) {
40 	return day;
41 	}
42 }
43 throw new IllegalArgumentException(
44 	String.format("%s is not a valid weekday string", s));
45 }
46
47 public String toString() {
48 	return dateSymbols.getWeekdays()[index];
49 }
50
51 public int toInt() {
52 	return index;
53 }
54 }
```

11. line 140, LAST_DAY_OF_MONTH
- comment가 필요없다 상수 이름으로 충분, 즉 중복임
- private일 필요가 없다 lastDayOfMonth라는 public method로 제공하고 있기때문에
```
//SerialDate.Java
139 /** The number of days in each month in non leap years. */
140 static final int[] LAST_DAY_OF_MONTH =
141 {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
``

12.  AGGREGATE_DAYS_TO_END_OF_MONTH & LEAP_YEAR_AGGREGATE_DAYS_TO_END_OF_MONTH는 JCommon framework에서 한차례도 쓰이지 않음 그래서 삭제
```java
143 /** The number of days in a (non-leap) year up to the end of each month. */
144 static final int[] AGGREGATE_DAYS_TO_END_OF_MONTH =
145 {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
146
147 /** The number of days in a year up to the end of the preceding month. */
148 static final int[] AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH =
149 {0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
```

13. AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH 는 단지 SpreadsheetDate.java에서만 쓰임.
만약 특정한 구현에만 한정되어져 있지 않다면 SpreadsheetDate.java로 옮기지않겠지만, SpreadsheetDate 단지 존재하는 것만이므로 이동.

14. 위 열거된 테이블들에 대해, 일관성을 유지하기 위해 테이블들을 private하게 만들고 하나의 펑션을 통해서 접근을 하도록 함이 옳다.

15. 다음으로 3개의 커다란 구성으로 나뉘는 constants는 모두 enums 헝식으로 변경한다.
```java
162 /** A useful constant for referring to the first week in a month. */
163 public static final int FIRST_WEEK_IN_MONTH = 1;
164
165 /** A useful constant for referring to the second week in a month. */
166 public static final int SECOND_WEEK_IN_MONTH = 2;
167
168 /** A useful constant for referring to the third week in a month. */
169 public static final int THIRD_WEEK_IN_MONTH = 3;
170
171 /** A useful constant for referring to the fourth week in a month. */
172 public static final int FOURTH_WEEK_IN_MONTH = 4;
173
174 /** A useful constant for referring to the last week in a month. */
175 public static final int LAST_WEEK_IN_MONTH = 0;
176
177 /** Useful range constant. */
178 public static final int INCLUDE_NONE = 0;
179
180 /** Useful range constant. */
181 public static final int INCLUDE_FIRST = 1;
182
183 /** Useful range constant. */
184 public static final int INCLUDE_SECOND = 2;
185
186 /** Useful range constant. */
187 public static final int INCLUDE_BOTH = 3;
188
189 /**
190 * Useful constant for specifying a day of the week relative to a fixed
191 * date.
192 */
193 public static final int PRECEDING = -1;
194
195 /**
196 * Useful constant for specifying a day of the week relative to a fixed
197 * date.
198 */
199 public static final int NEAREST = 0;
200
201 /**
202 * Useful constant for specifying a day of the week relative to a fixed
203 * date.
204 */
205 public static final int FOLLOWING = 1;
```
라인 161-175은 달 내에서 주를 선택하는 것으로 아래와 같이 수정된다.
```java
public enum WeekInMonth {
    FIRST(1), SECOND(2), THIRD(3), FOURTH(4), LAST(0); 
    public final int index;
    WeekInMonth(int index) { 
      this.index = index; 
    }
  }
```

The second set of constants (lines 177–187)는 범위의 끝날짜가 해당 범위에 포함되어야 하는지 여부를 설명하는 데 사용
이를 DateInterval 라는 enum으로 CLOSED, CLOSED_LEFT, CLOSED_RIGHT, and OPEN 구성토록 한다.

The third set of constants (lines 188–205)는 특정 요일에 대한 검색 결과가 마지막 instance 다음 instance 또는 가장 가까운 instance로 되어야 하는지 여부를 설명하는데
이는 enum WeekdayRange로 LAST, NEXT, and NEAREST로 구성케 한다.

이 세가지를 모두 eunm화 함으로써 IDE의 "change name"과 같은 기능을 통해서 변경을 할때 -1,2등이 누락되거나 또한 해당값에 대한 설명이 부족함에 대한 아쉬움을 덜 수 있습니다.

16. 208 라인에 description ﬁeld은 사용되지 않으므로 필드 자체와 accessor(getter) and mutator(setter) method를 모두 삭제한다.
```java
207 /** A description for the date. */
208 private String description;
209
210 /**
211 * Default constructor.
212 */
213 protected SerialDate() {
214 }
```
17. 213 라인에 SerialDate클래스의 Default constructor도 삭제한다. 컴파일러가 생성해줄테니 말이다.

18. 242라인에 자바닥은 값이 -1 반환에 대한 설명이나 우리는 Day enum을 생성하여 대체하였으므로이제는 옳지않은 정보를 제공하므로 삭제하도록 한다.
```java
242 /**
243 * Converts the supplied string to a day of the week.
244 *
245 * @param s a string representing the day of the week.
246 *
247 * @return <code>-1</code> if the string is not convertable, the day of
248 * the week otherwise.
249 */
250 public static int stringToWeekdayCode(String s) {
251
252 	final String[] shortWeekdayNames
253 	= DATE_FORMAT_SYMBOLS.getShortWeekdays();
254 	final String[] weekDayNames = DATE_FORMAT_SYMBOLS.getWeekdays();
255
256 	int result = -1;
257 	s = s.trim();
258 	for (int i = 0; i < weekDayNames.length; i++) {
259 		if (s.equals(shortWeekdayNames[i])) {
260 			result = i;
261 			break;
262 		}
263 		if (s.equals(weekDayNames[i])) {
264 			result = i;
265 			break;
266 		}
267	 }
268 	return result;
269
270 	}
271
272 /**
273 * Returns a string representing the supplied day-of-the-week.
274 * <P>
275 * Need to find a better approach.
276 *
277 * @param weekday the day of the week.
278 *
279 * @return a string representing the supplied day-of-the-week.
280 */
281 public static String weekdayCodeToString(final int weekday) {
282
283     final String[] weekdays = DATE_FORMAT_SYMBOLS.getWeekdays();
284     return weekdays[weekday];
285
286 }
287
288 /**
```

19. all the final keywords in arguments and variable declarations를 제거한다. 
    제거이유는 저자의 소스 분석 후 final keywords가 명확하게 필요에 의해 사용된것이 아니었고 이는 도리어 혼란을 가중시킬수있기때문이다.

20. 또한 위에 259, 263에 두개의 if문을  || 오퍼레이터로 연결하고, Day enumeration를 활용하여 for loop(Day.java의 30라인인듯)를 가리키는 형식으로 보기좋게 바꾸는 작업을 한다.
그래서 the parse function of Day로 적용하였고, 걔념적으로도 또한 해당 몸집이 커짐에 따라 Day.java로 분리하도록 결정함.

21. 또한 weekdayCodeToString (lines 272–286) 부분도 Day.java의 enumeration의 toString으로 옮긴다. 그래서 나온 Day.java는 아래와 같다.
```java
public enum Day {
  MONDAY(Calendar.MONDAY),
  TUESDAY(Calendar.TUESDAY),
  WEDNESDAY(Calendar.WEDNESDAY),s 
  THURSDAY(Calendar.THURSDAY), 
  FRIDAY(Calendar.FRIDAY),
  SATURDAY(Calendar.SATURDAY), 
  SUNDAY(Calendar.SUNDAY);
  public final int index;
  private static DateFormatSymbols dateSymbols = new DateFormatSymbols(); 
  Day(int day) {
    index = day; 
  }
  public static Day make(int index) throws IllegalArgumentException { 
    for (Day d : Day.values())
      if (d.index == index) 
        return d;
    throw new IllegalArgumentException(
      String.format("Illegal day index: %d.", index)); 
  }
  public static Day parse(String s) throws IllegalArgumentException { 
    String[] shortWeekdayNames =
      dateSymbols.getShortWeekdays(); 
    String[] weekDayNames =
      dateSymbols.getWeekdays(); 
    s = s.trim();
    for (Day day : Day.values()) {
      if (s.equalsIgnoreCase(shortWeekdayNames[day.index]) || 
          s.equalsIgnoreCase(weekDayNames[day.index])) { 
        return day;
      } 
    }
    throw new IllegalArgumentException(
      String.format("%s is not a valid weekday string", s)); 
  }
  public String toString() {
    return dateSymbols.getWeekdays()[index]; 
  }
}
```

22. 그다음 나오는 라인 289-316을 보면 두개의 펑션인데 첫번재펑션이 두번째펑션을 호출하는 형식
헌데, 사실 두번째 펑션은 다른 그누구에게도 호출되지 않는다 그래서 collapse를 두개를 하고 이름도 바꾸어준다.
```java
// Before
289 * Returns an array of month names.
290 *
291 * @return an array of month names.
292 */
293 public static String[] getMonths() {
294
295     return getMonths(false);
296
297 }
298
299 /**
300 * Returns an array of month names.
301 *
302 * @param shortened a flag indicating that shortened month names should
303 * be returned.
304 *
305 * @return an array of month names.
306 */
307 public static String[] getMonths(final boolean shortened) {
308
309     if (shortened) {
310         return DATE_FORMAT_SYMBOLS.getShortMonths();
311     }
312     else {
313        return DATE_FORMAT_SYMBOLS.getMonths();
314     }
315
316 }
```
```java
//After 
 public static String[] getMonthNames() { 
    return dateFormatSymbols.getMonths(); 
  }
```

23. 아래 isValidMonthCode 는 Month enum이 있으므로 삭제
```java
326 public static boolean isValidMonthCode(final int code) {
327
328 switch(code) {
329     case JANUARY:
330     case FEBRUARY:
331     case MARCH:
332     case APRIL:
333     case MAY:
334     case JUNE:
335     case JULY:
336     case AUGUST:
337     case SEPTEMBER:
338     case OCTOBER:
339     case NOVEMBER:
340     case DECEMBER:
341     return true;
342     default:
343         return false;
344     }   
345
346 }
```
24. monthCodeToQuarter 또한 Month enum에 quarter 메소드에 귀속되므로 삭제.
또한, Month enum 클래스화 하기에 충분히 비대해짐에 따라 DayDate로 부터 나와 독립적인 클래스로 만듬
```java
356 public static int monthCodeToQuarter(final int code) {
357
358 switch(code) {
359     case JANUARY:
360     case FEBRUARY:
361     case MARCH: return 1;
362     case APRIL:
363     case MAY:
364     case JUNE: return 2;
365     case JULY:
366     case AUGUST:
367     case SEPTEMBER: return 3;
368     case OCTOBER:
369     case NOVEMBER:
370     case DECEMBER: return 4;
371     default: throw new IllegalArgumentException(
372         "SerialDate.monthCodeToQuarter: invalid month code.");
373     }
374
375 }
376
377 /**
```
```java
 public int quarter() { 
      return 1 + (index-1)/3; 
    }
```
25. 그다음 라인 378-426을 보면 두개의 메소드가 있고, 출력의 형식 즉 month의 축약형으로 반환할지 아니면 기본형태로 반환할지에 대한 결정을 boolean flag을 통해 하는 것은 좋지않은 방식이다. 그러므로 두개의 용도에 따른 메소드로 나누어준다.
```java
//before
377 /**
378 * Returns a string representing the supplied month.
379 * <P>
380 * The string returned is the long form of the month name taken from the
381 * default locale.
382 *
383 * @param month the month.
384 *
385 * @return a string representing the supplied month.
386 */
387 public static String monthCodeToString(final int month) {
388
389     return monthCodeToString(month, false);
390
391 }
392
393 /**
394 * Returns a string representing the supplied month.
395 * <P>
396 * The string returned is the long or short form of the month name taken
397 * from the default locale.
398 *
399 * @param month the month.
400 * @param shortened if <code>true</code> return the abbreviation of the
401 * month.
402 *
403 * @return a string representing the supplied month.
404 * @throws java.lang.IllegalArgumentException
405 */
406 public static String monthCodeToString(final int month,
407                             final boolean shortened) {
408
409         // check arguments...
410         if (!isValidMonthCode(month)) {
411             throw new IllegalArgumentException(
412                 "SerialDate.monthCodeToString: month outside valid range.");
413         }
414     
415         final String[] months;
416     
417         if (shortened) {
418             months = DATE_FORMAT_SYMBOLS.getShortMonths();
419         }
420         else {
421             months = DATE_FORMAT_SYMBOLS.getMonths();
422         }
423     
424     return months[month - 1];
425
426 }
427
428 /**
```
```java
//After
public String toString() {
        return dateFormatSymbols.getMonths()[index - 1]; 
  }
  public String toShortString() {
        return dateFormatSymbols.getShortMonths()[index - 1]; 
  }
```
26. stringToMonthCode (lines 428–472)를 간소화하여 이름을 변경하고 Month enum으로 저자는 이동을 시킨다.
```java
428 /**
429 * Converts a string to a month code.
430 * <P>
431 * This method will return one of the constants JANUARY, FEBRUARY, ...,
432 * DECEMBER that corresponds to the string. If the string is not
433 * recognised, this method returns -1.
434 *
435 * @param s the string to parse.
436 *
437 * @return <code>-1</code> if the string is not parseable, the month of the
438 * year otherwise.
439 */
440 public static int stringToMonthCode(String s) {
441
442 final String[] shortMonthNames = DATE_FORMAT_SYMBOLS.getShortMonths();
443 final String[] monthNames = DATE_FORMAT_SYMBOLS.getMonths();
444
445 int result = -1;
446 s = s.trim();
447
448 // first try parsing the string as an integer (1-12)...
449 try {
450     result = Integer.parseInt(s);
451 }
452     catch (NumberFormatException e) {
453 // suppress
454 }
455
456 // now search through the month names...
457 if ((result < 1) || (result > 12)) {
458     for (int i = 0; i < monthNames.length; i++) {
459         if (s.equals(shortMonthNames[i])) {
460             result = i + 1;
461             break;
462         }
463         if (s.equals(monthNames[i])) {
464             result = i + 1;
465         break;
466         }
467     }
468 }
469
470     return result;
471
472 }
```
```java
public static Month parse(String s) { 
    s = s.trim();
    for (Month m : Month.values()) 
      if (m.matches(s))
        return m; 
    try {
      return make(Integer.parseInt(s)); 
    }
    catch (NumberFormatException e) {}
    throw new IllegalArgumentException("Invalid month " + s); 
  }
private boolean matches(String s) {
        return s.equalsIgnoreCase(toString()) || 
           s.equalsIgnoreCase(toShortString()); 
  }
```

27. isLeapYear method (lines 495–517) 는 좀 더 구체적으로 표현되어질 수 있다.
```java
495 /**
496 * Determines whether or not the specified year is a leap year.
497 *
498 * @param yyyy the year (in the range 1900 to 9999).
499 *
500 * @return <code>true</code> if the specified year is a leap year.
501 */
502 public static boolean isLeapYear(final int yyyy) {
503
504 if ((yyyy % 4) != 0) {
505     return false;
506 }
507 else if ((yyyy % 400) == 0) {
508     return true;
509 }
510 else if ((yyyy % 100) == 0) {
511     return false;
512 }
513 else {
514     return true;
515 }
516
517 }
```
```java
//After
 public static boolean isLeapYear(int year) {
    boolean fourth = year % 4 == 0;
    boolean hundredth = year % 100 == 0;
    boolean fourHundredth = year % 400 == 0;
    return fourth && (!hundredth || fourHundredth); 
  }
```

28. leapYearCount (lines 519–536)은 DayDate내에 누구에게도 호출되지않고 SpreadsheetDate내에 두개메소드에 호출되므로 push down하여 subclasses화 한다.
```java
495 /**
496 * Determines whether or not the specified year is a leap year.
497 *
498 * @param yyyy the year (in the range 1900 to 9999).
499 *
500 * @return <code>true</code> if the specified year is a leap year.
501 */
502 public static boolean isLeapYear(final int yyyy) {
503
504 if ((yyyy % 4) != 0) {
505     return false;
506 }
507 else if ((yyyy % 400) == 0) {
508     return true;
509 }
510 else if ((yyyy % 100) == 0) {
511     return false;
512 }
513 else {
514     return true;
515 }
516
517 }
```

29. lastDayOfMonth function (lines 538–560)은 the Month enum에 귀속되는 LAST_DAY_OF_MONTH array을 활용하므로 이를 Month.java로 옮기고, 동시에 간소화하고 좀더 표현적으로 바꾸어본다.
```java
538 /**
539 * Returns the number of the last day of the month, taking into account
540 * leap years.
541 *
542 * @param month the month.
543 * @param yyyy the year (in the range 1900 to 9999).
544 *
545 * @return the number of the last day of the month.
546 */
547 public static int lastDayOfMonth(final int month, final int yyyy) {
548
549 	final int result = LAST_DAY_OF_MONTH[month];
550 	if (month != FEBRUARY) {
551 		return result;
552 	}
553 	else if (isLeapYear(yyyy)) {
554 		return result + 1;
555 	}
556 	else {
557 		return result;
558 	}
559
560 }
```
```java
//After
public static int lastDayOfMonth(Month month, int year) { 
    if (month == Month.FEBRUARY && isLeapYear(year)) 
      return month.lastDay() + 1;
     else
      return month.lastDay(); 
  }
```
30. function addDays (lines 562–576)의 경우, DayDate의 변수상에서 작동하므로 static일 필요가 없으로 instance method로 바꾸어주고, 두번째로 toSerial의 이름 또한 알맞게 toOrdinal로 변경합니다. 
```java
/**
563 * Creates a new date by adding the specified number of days to the base
564 * date.
565 *
566 * @param days the number of days to add (can be negative).
567 * @param base the base date.
568 *
569 * @return a new date.
570 */
571 public static SerialDate addDays(final int days, final SerialDate base) {
572
573     final int serialDayNumber = base.toSerial() + days;
574         return SerialDate.createInstance(serialDayNumber);
575
576 }
```
```java
//After
public DayDate addDays(int days) {
    return DayDateFactory.makeDate(toOrdinal() + days); 
  }
```

31. addMonths (lines 578–602)도 상동합니다. 인스턴스 메소드로 바꾸어주고, 알고리즘이 좀 복잡하므로 EXPLAINING TEMPORARY VARIABLES을 이용하여
계산식을 중간자인 variable을 두어 한스텝씩 나누어서 readibility을 적용하는 방식을 적용합니다. 그리고 메소드 이름 하나도 getYYY ==> getYear로 변경
```java
578 /**
579 * Creates a new date by adding the specified number of months to the base
580 * date.
581 * <P>
582 * If the base date is close to the end of the month, the day on the result
583 * may be adjusted slightly: 31 May + 1 month = 30 June.
584 *
585 * @param months the number of months to add (can be negative).
586 * @param base the base date.
587 *
588 * @return a new date.
589 */
590 public static SerialDate addMonths(final int months,
591 					final SerialDate base) {
592
593 	final int yy = (12 * base.getYYYY() + base.getMonth() + months - 1)
594 			/ 12;
595 	final int mm = (12 * base.getYYYY() + base.getMonth() + months - 1)
596 			% 12 + 1;
597 	final int dd = Math.min(
598 		base.getDayOfMonth(), SerialDate.lastDayOfMonth(mm, yy)
599 	);
600 	return SerialDate.createInstance(dd, mm, yy);
601
602 }
```
```java
//After
 public DayDate addMonths(int months) {
	int thisMonthAsOrdinal = 12 * getYear() + getMonth().index - 1; 
    	int resultMonthAsOrdinal = thisMonthAsOrdinal + months; 
    	int resultYear = resultMonthAsOrdinal / 12;
	Month resultMonth = Month.make(resultMonthAsOrdinal % 12 + 1);
	int lastDayOfResultMonth = lastDayOfMonth(resultMonth, resultYear); 
    	int resultDay = Math.min(getDayOfMonth(), lastDayOfResultMonth); 
    	return DayDateFactory.makeDate(resultDay, resultMonth, resultYear); 
  }
```

32. 아래 addYears 또한 마찬가지로 static을 instance 메소드로 바꾸어줍니다.
```java
/**
605 * Creates a new date by adding the specified number of years to the base
606 * date.
607 *
608 * @param years the number of years to add (can be negative).
609 * @param base the base date.
610 *
611 * @return A new date.
612 */
613 public static SerialDate addYears(final int years, final SerialDate base) {
614
615 final int baseY = base.getYYYY();
616 final int baseM = base.getMonth();
617 final int baseD = base.getDayOfMonth();
618
619 final int targetY = baseY + years;
620 final int targetD = Math.min(
621 	baseD, SerialDate.lastDayOfMonth(baseM, targetY)
622 );
623
624 return SerialDate.createInstance(targetD, baseM, targetY);
625
626 }
```
>> 여기서 위 3개 모두 static을 instace methode화 했지만, addDay라는 이름으로 인해 new instance로 반환된다는 것이 그다지 유저에게 인지시키기 어렵고 해당 object를 변경하는 것으로 인지될 확률이 높다
그러므로 이름을 plusDay라는 형식으로 바꾼다.

33. getPreviousDayOfWeek메소드는 다소 복잡한 형태를 띄고 있습니다. 아래를 3가지를 적용해서 리팩터링을 실시함.
- 보다 간단하게 하기 위해 EXPLAINING TEMPORARY VARIABLES 를 사용했고
- static method ==> instance method
- 중복 instance method 제거 (lines 997–1008)
```java
628 /**
629 * Returns the latest date that falls on the specified day-of-the-week and
630 * is BEFORE the base date.
631 *
632 * @param targetWeekday a code for the target day-of-the-week.
633 * @param base the base date.
634 *
635 * @return the latest date that falls on the specified day-of-the-week and
636 * is BEFORE the base date.
637 */
638 public static SerialDate getPreviousDayOfWeek(final int targetWeekday,
639 final SerialDate base) {
640
641 	// check arguments...
642 	if (!SerialDate.isValidWeekdayCode(targetWeekday)) {
643 		throw new IllegalArgumentException(
644 		"Invalid day-of-the-week code."
645 		);
646 	}
647
648 	// find the date...
649 	final int adjust;
650 	final int baseDOW = base.getDayOfWeek();
651 	if (baseDOW > targetWeekday) {
652 		adjust = Math.min(0, targetWeekday - baseDOW);
653 	}
654 	else {
655 		adjust = -7 + Math.max(0, targetWeekday - baseDOW);
656 	}
657
658 	return SerialDate.addDays(adjust, base);
659
660 }
```
```java
997 /**
998 * Returns the latest date that falls on the specified day-of-the-week and
999 * 	is BEFORE this date.
1000 *
1001 * @param targetDOW a code for the target day-of-the-week.
1002 *
1003 * @return the latest date that falls on the specified day-of-the-week and
1004 * 	is BEFORE this date.
1005 */
1006 public SerialDate getPreviousDayOfWeek(final int targetDOW) {
1007 	return getPreviousDayOfWeek(targetDOW, this);
1008 }
```
```java
//After
public DayDate getPreviousDayOfWeek(Day targetDayOfWeek) {
    int offsetToTarget = targetDayOfWeek.index - getDayOfWeek().index; 
    if (offsetToTarget >= 0)
      offsetToTarget -= 7;
    return plusDays(offsetToTarget); 
  }
```

34. getFollowingDayOfWeek(), lines 662–693 역시 상동
- EXPLAINING TEMPORARY VARIABLES 를 사용
- change static to instace method
```java
/**
663 * Returns the earliest date that falls on the specified day-of-the-week
664 * and is AFTER the base date.
665 *
666 * @param targetWeekday a code for the target day-of-the-week.
667 * @param base the base date.
668 *
669 * @return the earliest date that falls on the specified day-of-the-week
670 * and is AFTER the base date.
671 */
672 public static SerialDate getFollowingDayOfWeek(final int targetWeekday,
673 							final SerialDate base) {
674
675 	// check arguments...
676 	if (!SerialDate.isValidWeekdayCode(targetWeekday)) {
677 		throw new IllegalArgumentException(
678 			"Invalid day-of-the-week code."
679 		);
680 }
681
682 	// find the date...
683 	final int adjust;
684 	final int baseDOW = base.getDayOfWeek();
685 	if (baseDOW > targetWeekday) {
686 		adjust = 7 + Math.min(0, targetWeekday - baseDOW);
687 	}
688 	else {
689 		adjust = Math.max(0, targetWeekday - baseDOW);
690 	}
691
692 	return SerialDate.addDays(adjust, base);
693 }
```
```java
//After
public DayDate getFollowingDayOfWeek(Day targetDayOfWeek) {
    int offsetToTarget = targetDayOfWeek.index - getDayOfWeek().index; 
    if (offsetToTarget <= 0)
	offsetToTarget += 7;
    return plusDays(offsetToTarget); 
  }
```
35.  getNearestDayOfWeek (lines 695–726)라인도 앞에 두 메서드와 마찬가지의 패턴을 적용한다.
- EXPLAINING TEMPORARY VARIABLES 를 사용
- change static to instace method
```java
public DayDate getNearestDayOfWeek(final Day targetDay) {
    	int offsetToThisWeeksTarget = targetDay.index - getDayOfWeek().index; 
	int offsetToFutureTarget = (offsetToThisWeeksTarget + 7) % 7;
    	int offsetToPreviousTarget = offsetToFutureTarget - 7; 
    	if (offsetToFutureTarget > 3)
      		return plusDays(offsetToPreviousTarget); 
    	else
      		return plusDays(offsetToFutureTarget); 
  }
```
36. getEndOfCurrentMonth method (lines 728–740) DayDate argument를 받아서 instance를 반환하는 형식이었는데
이를 true instance method화하여서 파라미터를 비우고 이름들을 보다 명확하게 수정을 하였다.
```java
728 /**
729 * Rolls the date forward to the last day of the month.
730 *
731 * @param base the base date.
732 *
733 * @return a new serial date.
734 */
735 public SerialDate getEndOfCurrentMonth(final SerialDate base) {
736 	final int last = SerialDate.lastDayOfMonth(
737 	base.getMonth(), base.getYYYY()
738 	);
739 	return SerialDate.createInstance(last, base.getMonth(), base.getYYYY());
740 }
```
```java
//After
public DayDate getEndOfMonth() { 
        Month month = getMonth(); 
        int year = getYear();
        int lastDay = lastDayOfMonth(month, year);
        return DayDateFactory.makeDate(lastDay, month, year); 
  }
```
37.  Refactoring weekInMonthToString (lines 742–761)
- the refactoring tools of IDE를 이용해서 method를 WeekInMonth enum로 옮기고 
- static method를 instance method로 바꾸었다. 여전히 Asserts가 모두 passed
- 그런데 그 다음에는 메서드 전체를 지우면 Asserts가 모두failed
- 그리곤 enumerators(FIRST,SECOND...)의 이름들을 이용해서 바꾸고 나니 All tests passed? 
- 왜인지 이해하나요? 왜 이 모든 절차가 순서대로 필요했는지 이해하나요?
```java
742 /**
743 * Returns a string corresponding to the week-in-the-month code.
744 * <P>
745 * Need to find a better approach.
746 *
747 * @param count an integer code representing the week-in-the-month.
748 *
749 * @return a string corresponding to the week-in-the-month code.
750 */
751 public static String weekInMonthToString(final int count) {
752
753 	switch (count) {
754 		case SerialDate.FIRST_WEEK_IN_MONTH : return "First";
755 		case SerialDate.SECOND_WEEK_IN_MONTH : return "Second";
756 		case SerialDate.THIRD_WEEK_IN_MONTH : return "Third";
757 		case SerialDate.FOURTH_WEEK_IN_MONTH : return "Fourth";
758 		case SerialDate.LAST_WEEK_IN_MONTH : return "Last";
759 		default :
760 	return "SerialDate.weekInMonthToString(): invalid code.";
761 }
```
```java
// 위에 언급된 Assert 구문 
410 public void testWeekInMonthToString() throws Exception {
411 assertEquals("First",weekInMonthToString(FIRST_WEEK_IN_MONTH));
412 assertEquals("Second",weekInMonthToString(SECOND_WEEK_IN_MONTH));
413 assertEquals("Third",weekInMonthToString(THIRD_WEEK_IN_MONTH));
414 assertEquals("Fourth",weekInMonthToString(FOURTH_WEEK_IN_MONTH));
415 assertEquals("Last",weekInMonthToString(LAST_WEEK_IN_MONTH));
416
417 //todo try {
418 // weekInMonthToString(-1);
419 // fail("Invalid week code should throw exception");
420 // } catch (IllegalArgumentException e) {
421 // }
422 }
```
38.  relativeToString() line 765-783는 테스트말고 호출하는 것이 없음을 확인하고 삭제했다.
```java
765 /**
766 * Returns a string representing the supplied 'relative'.
767 * <P>
768 * Need to find a better approach.
769 *
770 * @param relative a constant representing the 'relative'.
771 *
772 * @return a string representing the supplied 'relative'.
773 */
774 public static String relativeToString(final int relative) {
775
776 	switch (relative) {
777 		case SerialDate.PRECEDING : return "Preceding";
778 		case SerialDate.NEAREST : return "Nearest";
779 		case SerialDate.FOLLOWING : return "Following";
780 		default : return "ERROR : Relative To String";
781 	}
782
783 }
```

39.  toDate (lines 838–846)은 앞서서 ToSerial의 이름을 toOrdinal로 바꾸었듯 같은 맥락에서 getOrdinalDay로 바꾼다.
```java
838 /**
839 * Returns a java.util.Date. Since java.util.Date has more precision than
840 * SerialDate, we need to define a convention for the 'time of day'.
841 *
842 * @return this as <code>java.util.Date</code>.
843 */
844 public abstract java.util.Date toDate();
845
846 /**
```

40. abstract method toDate (lines 838–844)는 SpreadsheetDate에서 implementation되고
다른 dependency가 그 클래스 내에 존재하지 않는다 Abstract일 이유가 있을까? So, Push up한다.
```java
/**
839 * Returns a java.util.Date. Since java.util.Date has more precision than
840 * SerialDate, we need to define a convention for the 'time of day'.
841 *
842 * @return this as <code>java.util.Date</code>.
843 */
844 public abstract java.util.Date toDate();
845
846 /**
```

41. getYYYY, getMonth, and getDayOfMonth methods 의 추상화와 다르게 
getDayOfWeek는 SpreadsheetDate.java에 종속되지 않습니다. 그러므로 DayDate.java로 pull up.
그런데 자세히 //SpreadsheetDate.java 의 247 라인을 보면 로직상의 종속성이 표현되고 있는데
day의 순서의 시작점이 0이라는 것을 암묵적으로 나타내고 있다. 
이름 로직적 종속성을 감안하여서, 저자는 getDayOfWeekForOrdinalZero 추상 메서드를 DayDate에 만들어서
SpreadsheetDate내에서 implmented되게 하고 Day.SATURDAY를 리턴하게된다.
다음 getDayOfWeek 메서드를 DayDate로 이동하고 getOrdinalDay 와 getDayOfWeekForOrdinalZero를 호출하도록 변경했습니다
```java
874 /**
875 * Returns the year (assume a valid range of 1900 to 9999).
876 *
877 * @return the year.
878 */
879 public abstract int getYYYY();
880
881 /**
882 * Returns the month (January = 1, February = 2, March = 3).
883 *
884 * @return the month of the year.
885 */
886 public abstract int getMonth();
887
888 /**
889 * Returns the day of the month.
890 *
891 * @return the day of the month.
892 */
893 public abstract int getDayOfMonth();
894
895 /**
896 * Returns the day of the week.
897 *
898 * @return the day of the week.
899 */
900 public abstract int getDayOfWeek();
```
```java
//SpreadsheetDate.java
214 public int getYYYY() {
215 return this.year;
216 }
217
223 public int getMonth() {
224 return this.month;
225 }
226
232 public int getDayOfMonth() {
233 return this.day;
234 }
235
246 public int getDayOfWeek() {
247 return (this.serial + 6) % 7 + 1;
248 }
```
```java
public Day getDayOfWeek() {
        Day startingDay = getDayOfWeekForOrdinalZero();
        int startingOffset = startingDay.index - Day.SUNDAY.index; 
        return Day.make((getOrdinalDay() + startingOffset) % 7 + 1);
  }
```

42. 그리고 상단에 getDayOfWeek()에 대한 주석의 반복을 삭제합니다.
43. compare (lines 902–913) 는 추상이 되기엔 부적절하므로 pull up하고 리턴값도 사실상 차이가 나는 만큼의 Day의 수를 반환하므로 daysSince로 변경한다.
```java
909 * @param other the date being compared to.
910 *
911 * @return the difference between this and the other date.
912 */
913 public abstract int compare(SerialDate other);
```
44.  다음 six functions (lines 915–980) 모두 추상 메서드로 모두 DayDate에 implemented 되어져야한다.
So I pulled them all up from SpreadsheetDate.
```java
916 * Returns true if this SerialDate represents the same date as the
917 * specified SerialDate.
918 *
919 * @param other the date being compared to.
920 *
921 * @return <code>true</code> if this SerialDate represents the same date as
922 * the specified SerialDate.
923 */
924 public abstract boolean isOn(SerialDate other);
925
926 /**
927 * Returns true if this SerialDate represents an earlier date compared to
928 * the specified SerialDate.
929 *
930 * @param other The date being compared to.
931 *
932 * @return <code>true</code> if this SerialDate represents an earlier date
933 * compared to the specified SerialDate.
934 */
935 public abstract boolean isBefore(SerialDate other);
936
937 /**
938 * Returns true if this SerialDate represents the same date as the
939 * specified SerialDate.
940 *
941 * @param other the date being compared to.
942 *
943 * @return <code>true<code> if this SerialDate represents the same date
944 * as the specified SerialDate.
945 */
946 public abstract boolean isOnOrBefore(SerialDate other);
947
948 /**
949 * Returns true if this SerialDate represents the same date as the
950 * specified SerialDate.
951 *
952 * @param other the date being compared to.
953 *
954 * @return <code>true</code> if this SerialDate represents the same date
955 * as the specified SerialDate.
956 */
957 public abstract boolean isAfter(SerialDate other);
958
959 /**
960 * Returns true if this SerialDate represents the same date as the
961 * specified SerialDate.
962 *
963 * @param other the date being compared to.
964 *
965 * @return <code>true</code> if this SerialDate represents the same date
966 * as the specified SerialDate.
967 */
968 public abstract boolean isOnOrAfter(SerialDate other);
969
970 /**
971 * Returns <code>true</code> if this {@link SerialDate} is within the
972 * specified range (INCLUSIVE). The date order of d1 and d2 is not
973 * important.
974 *
975 * @param d1 a boundary date for the range.
976 * @param d2 the other boundary date for the range.
977 *
978 * @return A boolean.
979 */
980 public abstract boolean isInRange(SerialDate d1, SerialDate d2);
```

45. isInRange (lines 982–995) 또한 pulled up 그리고 refactored되어져야할 대상으로 switch구분(DateInterval)을 활용하여서 재구현한다.
```java
982 /**
983 * Returns <code>true</code> if this {@link SerialDate} is within the
984 * specified range (caller specifies whether or not the end-points are
985 * included). The date order of d1 and d2 is not important.
986 *
987 * @param d1 a boundary date for the range.
988 * @param d2 the other boundary date for the range.
989 * @param include a code that controls whether or not the start and end
990 * dates are included in the range.
991 *
992 * @return A boolean.
993 */
994 public abstract boolean isInRange(SerialDate d1, SerialDate d2,
995 int include);
996
```
```java
//After
public enum DateInterval { 
    OPEN {
      public boolean isIn(int d, int left, int right) { 
        return d > left && d < right;
      } 
    },
    CLOSED_LEFT {
      public boolean isIn(int d, int left, int right) { 
        return d >= left && d < right;
      } 
    },
    CLOSED_RIGHT {
      public boolean isIn(int d, int left, int right) { 
        return d > left && d <= right;
      } 
    },
    CLOSED {
      public boolean isIn(int d, int left, int right) { 
        return d >= left && d <= right;
      } 
    };
    public abstract boolean isIn(int d, int left, int right); 
  }
```
```java
public boolean isInRange(DayDate d1, DayDate d2, DateInterval interval) { 
        int left = Math.min(d1.getOrdinalDay(), d2.getOrdinalDay());
	    int right = Math.max(d1.getOrdinalDay(), d2.getOrdinalDay()); 
        return interval.isIn(getOrdinalDay(), left, right);
  }
```

### 전반적인 수정내용과 클래스의 흐름
- 긴 오프닝 주석을 단축 및 개선
- the remaining enums을 각 파일화함 
- static 변수(dateFormatSymbols)와 세 가지 static 메서드(getMonthNames, isLeap)를 신규 클래스 DateUtil 로 이동
- Abstract method가 소속된곳으로 이동시켰고
- Month.make를 Month.fromInt로 바꾸고 다른 enums들에 대해서도 같은 수정을 했으며
- toInt() accessor라는 accessor도 추가했고,  index ﬁeld를 private화 하였음
- plusYears and plusMonth상에 중복되는 부분을 발견해서 correctLastDayOfMonth로 새로 축출 생성하였으며
- 숫자로 표현된 것들을 Month.JANUARY.toInt() or Day.SUNDAY.toInt()로 올바르게 바꾸었으며
- SpreadsheetDate에 조금서 시간을 쓰고 알고리즘을 다듬어서 

>> Final files
- DayDate.java (Final)
- Month.java (Final)
- Day.java (Final)
- DateInterval.java (Final)
- WeekInMonth.java (Final)
- WeekdayRange.java (Final)
- DateUtil.java (Final)
- DayDateFactory.java (Final)
- SpreadsheetDateFactory.java (Final)
- SpreadsheetDate.java (Final)

### Conclusion
- 보이스카우트 규칙을 따랐고,
- 조금 더 깨끗하게 하려했고,
- 시간이 걸렸지만 가치가 있었고,
- 테스트의 적용범위가 증가하고 일부의 버그는 수정되었으며,
- 코드가 명확하고 축소되었다. 
- 다른 코드를 사용하는 이로하여금 조금더 수월하게 만들었다. 
