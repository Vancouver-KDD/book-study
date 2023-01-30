## 3. Functions	
	
### Small
첫번째 규칙은 Function은 작아야한다는 것.
두번재 규칙은 보다 적아져야 한다는 것.
Function이 나이진다는 말할 순 없지만, 수년간에 저자의 경험을 토대로 저자는 Function은 작아져야함을 강조한다.
	
```java
public static String renderPageWithSetupsAndTeardowns(PageData pageData, boolean isSuite ) throws Exception {    
     boolean isTestPage = pageData.hasAttribute("Test");    
	 if (isTestPage) {  
		 WikiPage testPage = pageData.getWikiPage();    
		 StringBuffer newPageContent = new StringBuffer();  
		 includeSetupPages(testPage, newPageContent, isSuite);  
		 newPageContent.append(pageData.getContent());  
		 includeTeardownPages(testPage, newPageContent, isSuite);   
		 pageData.setContent(newPageContent.toString());    
     }  
    return pageData.getHtml(); 
}  
```	
	
	
						
### Blocks and Indenting					
if statements, else statements, while statements 등에 대해 블럭을 sinlge화 하고,
이는 펑션 자체를 작게해주고 더불어 그 기능에 대해 잘 표현된 이름을 갖을 수 있기에 중요하다.
						
### Do One Thing					
						
FUNCTIONS SHOULD DO ONE THING. THEY SHOULD DO IT WELL.
THEY SHOULD DO IT ONLY.
우리가 함수를 쓰는 이유는 더 큰 개념(즉, 함수의 이름)을  그 다음 단계의 추상화 단계로 분해하기 위해서이다.
```java
public static String renderPageWithSetupsAndTeardowns(				
		PageData pageData, boolean isSuite) throws Exception {				
		if (isTestPage(pageData))				
		    includeSetupAndTeardownPages(pageData, isSuite);				
		return pageData.getHtml();				
}

```
				
위의 코드는 몇 차례  리팩토링을 거친 코드이다.
이는 더 이상 줄일 수 없어보인다. 물론 IF문을 IF문내의 함수에 포함시켜 진행 할 수 있지만.
이는 추상화 레벨을 변경한다기 보다는 코드 재작성이라 할 수 있다.

이와 같이 함수가 하나의 일 이상을 수행하고 있는 지를 판단할 수 있는 방법은 코드 재작성의 의미가 아닌 해당 함수에서
다른 함수를 또 추출 할 수 있느냐로 판단해 볼 수 있다. 그렇다면 축소함을 더 나은 판단이라고 저자는 말한다.
			
						
### Sections within Functions				
아래 코드가 한가지 이상을 하는 펑션(declarations, initializations, and sieve)의 명백한 예시이고, 만약 한가지 일을 한다면 섹션을 합리적으로 나눌 수 없다.	
```java
	public static int[] generatePrimes(int maxValue)			
	{			
		if (maxValue >= 2) // the only valid case			
		{			
			// declarations			
			int s = maxValue + 1; // size of array			
			boolean[] f = new boolean[s];			
			int i;			
			// initialize array to true.			
			for (i = 0; i < s; i++)			
				f[i] = true;			
			// get rid of known non-primes			
			f[0] = f[1] = false;			
			// sieve			
			int j;			
			for (i = 2; i < Math.sqrt(s) + 1; i++)			
			{			
				if (f[i]) // if i is uncrossed, cross its multiples.			
				{			
					for (j = 2 * i; j < s; j += i)			
					f[j] = false; // multiple is not prime			
				}			
			}			
			// how many primes are there?			
			int count = 0;			
			for (i = 0; i < s; i++)			
			{			
				if (f[i])			
				count++; // bump count.			
			}			
			int[] primes = new int[count];			
			// move the primes into the result			
			for (i = 0, j = 0; i < s; i++)			
			{			
				if (f[i]) // if prime			
				primes[j++] = i;			
			}			
			return primes; // return the primes			
		}			
		else // maxValue < 2			
			return new int[0]; // return null array if bad input.			
	}		

```
					
						
### One Level of Abstraction per Function					
						
함수 내에서 추상화 수준을 혼합하는 것은 혼란을 가중시키며 코드를 보는 사람들로 하여근 본질적인 내용을 파악하는데 혼란을 줄 수 있다.
세부사항이 필수적인 개념과 섞이게 되면서 펑션 내에 점점 더 많은 세부사항이 축적되는 경향이 있는 데 더 안좋은 케이스이다.
			
						
### Reading Code from Top to Bottom: The Stepdown Rule					
						
코드는 top-down 서술 방식으로 읽혀짐이 좋고, 각 추상화 수준으로 내용을 가려가면서 Funtion에 대한 목록을 읽어 내려가는 것이 좋다.
다르게 말하면 단락은 현재의 Abstraction을 설명하고 그에 속하는 문단들은 다음 단계를 설명하는 형식이다.
				
						
	To include the setups and teardowns, we include setups, then we include the test page content,			
	and then we include the teardowns.			
		To include the setups, we include the suite setup if this is a suite, then we include the		
		regular setup.		
		To include the suite setup, we search the parent hierarchy for the “SuiteSetUp” page		
		and add an include statement with the path of that page.		
		To search the parent. . .		
						
물론 프로그래머들이 이 규칙을 따르고 한개 수준의 추상화 수준으로 지켜가는 것은 어렵다.
하지만 이와 같은 skill도 매우 중요하므로 Function을 짧게 유지하고, 하나의 목적을 수행하도록 하는 것이 핵심이다.
### Switch Statements						
```java			
public Money calculatePay(Employee e) throws InvalidEmployeeType {				
	switch (e.type) {			
		case COMMISSIONED:		
				return calculateCommissionedPay(e);
		case HOURLY:		
				return calculateHourlyPay(e);
		case SALARIED:		
				return calculateSalariedPay(e);
		default:		
				throw new InvalidEmployeeType(e.type);
	}			
}				
```						
						
#### 문제점
1. 규모가 크고 유형이 추가가 될 수 있다 -> OCP(Open Closed Principle)에 어긋
2. 한가지 이상의 일을 한다. -> SRP(Single Responsibilty Principle) 어긋

같은 형태의 다른 Function들이 추가로 더 붙을 수 있음이 더 큰 문제로 보인다.


    isPayday(Employee e, Date date),				
    or				
    deliverPay(Employee e, Money pay),

#### 해결안
즉, Abstraction Factory 즉 Gang of Four Design Patterns중에 하나인 Abstract Factory를 제공함으로써
Concrete class가 아닌 Interface를 활용하여 관계되고 종속된 오브젝트를 생성하도록 하는 것이다.
switch statement를 Abstract Factory에 묻고, 이 Factory는 switch statement를 활용하여 Polymorphism에 의거하여 제공된 implemented Employee의 인스턴스를 생성하고 활용하게 된다.
calculatePay, isPayday, and deliverPay 즉 abstract method들은 다형화(polymorphically)된 형태로 interface에 의해 제공된다.
			
```java				
public abstract class Employee {			
	public abstract boolean isPayday();		
	public abstract Money calculatePay();		
	public abstract void deliverPay(Money pay);		
}			
			
public interface EmployeeFactory {			
	public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType;		
}			
			
public class EmployeeFactoryImpl implements EmployeeFactory {			
	public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType {		
		switch (r.type) {	
		case COMMISSIONED:	
			return new CommissionedEmployee(r) ;
		case HOURLY:	
			return new HourlyEmployee(r);
		case SALARIED:	
			return new SalariedEmploye(r);
		default:	
			throw new InvalidEmployeeType(r.type);
		}	
}	
```			
						
### Use Descriptive Names					
						
클린코드의 원칙을 달성하기 위한 절반의 성취는 
Do one thing을 하는 smaller Fuction을 위해 좋은 name을 먼저 고를때 달성된다.
				
Don’t be afraid to make a name long				
Don’t be afraid to spend time choosing a name				
Choosing descriptive names will clarify the design of the module				
Be consistent in your names				
						
						
### Function Arguments					
the ideal number of arguments for a funtion is zero				
						
### Common Monadic Forms					
일반적인 단일형식.
    
    Boolean의 경우 단일  input argument에 대해 물어볼 수 있다.   
    boolean fileExists(“MyFile”).   

    input argument를 변형 시키고 변형된 argument를 반환하는 형식
    InputStream fileOpen(“MyFile”)

    이벤트의 경우, 아래와 같이 비번의 연속적인 오류의 경우 input argument를 통해 시스템의  state를 바꿀 수 있다.
    void passwordAttemptFailedNtimes(int attempts).
    만약 Function 이 input argument를 변형시키게 되면, 그 변형은 리턴밸류에 나타내주어야 한다.
    즉, StringBuffer transform(StringBuffer in)이 void transform(StringBuffer out) 보다 나은 표현이다.
				
						
						
### Flag Arguments					
						
Flag Arguments는 피하도록하자
이러한 Function은 로직의 흐름을 따라 복수개의 Function으로 나누어  구성 할 수 있다.
				
    render(boolean isSuite)				
    renderForSuite() and renderForSingleTest().				
						
### Dyadic Functions			
						
    writeField(name)이 코드를 읽는 사람으로 하여금, writeField(output-Stream, name)보다 이해를 돕니다.
    Point p = new Point(0,0); 경우는 두개의 argument를 받는 것이 합리적으로 보인다.
    assertEquals(expected, actual) 경우는 문제가 있어보인다.
				
						
### Triads					
    assertEquals(message, expected, actual).
    예시로 여기 3개의 argument를 받는 오버로드 형식은 몇번이고 다시 보게 만들만큼 이해하기 어렵다.
				
						
### Argument Objects					
						
    Circle makeCircle(double x, double y, double radius);				
    Circle makeCircle(Point center, double radius);				
						
						
### Verbs and Keywords					
						
    verb/noun pair form				
    	writeField(name)			
    the keyword form				
    	Argument? ?? ??? Function?? ???.			
    	e.i. assertExpectedEqualsActual(expected, actual)			
						
						
						
### Have No Side Effects						
						
Function은 작동을 하거나 혹은 필요한 값을 반환하는 것 둘중에 한가지에 주목하고 두가지 한꺼번에 하는걸 삼가라 
이로인해 네이밍을 통해 유추할 수 없는 부수적인 동작들로 데이터 손실이 일어난다고 가정하자
이는 프로그래머로 하여금 혼란을 초래한다. 그러므로 명확하게 이름에 표시를 하거나 하나의 동작에 주목하고
Function을 나누는 것이 옳다.
					
						
### Don't Repeat Yourself						
						
Duplication may be the root of all evil in software					
