## 4.Comments

### Do Not Make Up for Bad Code					
주석이 나쁜 코드를 보완하는 역할을 하진않는다.				
코드에 대해 구구절절히 설명하고 변경하는 것보다 정리하는 게 낫다!				
				
클리어하고 전달력있는 코드 + 짤막한 주석 >>>> 난해하고 복잡한 코드 + 긴 설명의 주석				
					
### Explain Yourself in Code					
다시 한번 저자는 언급한다. 코드에서 설명해라 클리어하고 전달력있는 코드를 짜라.				
코드가 설명을 위한 수단이 될 수 없다는 생각은 명백한 거짓말이다.
```java
// Check to see if the employee is eligible for full benefits			
if ((employee.flags & HOURLY_FLAG) &&			
(employee.age > 65))			
	Or this?				
if (employee.isEligibleForFullBenefits())			
```					
					
## Good Comments					
주석의 합당하는 몇 가지를 저자는 소개한다.				
하지만, 저자는 다시 한번 강조한다. 진정으로 좋은 주석은 주석을 적지 않을 방법을 찾는 것이다.				
					
### Legal Comments				
					
첫번째로 저작권 및 저작자에 대한 언급은 필요하고 합리적인 주석에 해당한다.				
					
### Informative Comments				
때때론 주석으로 기본 정보를 제공하는 것이 다음과 같이 유용하다.				
```java		
// Returns an instance of the Responder being tested.			
protected abstract Responder responderInstance();	
```		
Abstract method에 대한 설명이긴 하지만 저자는 다시 한번 강조한다.				
아래와 같이 네이밍을 이용해 코드 자체로 주석을 최소하여서 설명할 수 있다.	
```java
protected abstract Responder responderBeingTested();			
```					
```java					
// format matched kk:mm:ss EEE, MMM dd, yyyy			
Pattern timeMatcher = Pattern.compile(			
\\d*:\\d*:\\d* \\w*, \\w* \\d*, \\d*);	
```		
해당 예시를 보면 주석을 통해 regular expression이 time과 date에 맞춰 패턴이 포맷화됨을 알 수 있다.				
그래도, 날짜와 시간 형식을 변환하는 특정한 클래스화가 되면 더 좋고 명화하고 주석은 불필요하다고 저자는 설명한다.				
					
					
### Explanation of Intent				
또 하나의 주석의 목적은 구현된 코드를 설명하는 것을 넘어, 의사결정에 필요한 의도를 전달하는 역할을 합니다.				
					
### Clarification	
    assertTrue(a.compareTo(b) == -1); // a < b
    assertTrue(b.compareTo(a) == 1); // b > a
					
					
### TODO Comments	
때로는 ToDo 주석을 통해서 완료되어져야할 것 이후에 이어서 할 것등 주석을 통해 프로그래머가 할일을 리마인드하도록 유용하게 쓰이기도 합니다.

    //TODO-MdM these are not needed
    // We expect this to go away when we do the checkout model
    protected VersionInfo makeVersion() throws Exception
    {
        return null;
    }
### Amplification	
built-in function trim 이용 시 첫번째 space가 지워져 다른 리스트로 인식되어질 수 있음에 대한 주의점을 전달하고자 합니다.

    String listItemContent = match.group(3).trim();
    // the trim is real important. It removes the starting
    // spaces that could cause the item to be recognized
    // as another list.
    new ListItemWidget(this, listItemContent, this.level + 1);
    return buildList(text.substring(match.end()));		

## Bad Comnnets					
					
대부분의 코멘트는 이 카테고리에 속하지 않을까 싶습니다.				
일반적으로 불충분한 결정에 대한 자기 방어나 변명 혹은 혼잣말등이 주를 이룬다.				
					
### Mumbling				
단지 수동적고 회사의 코딩 스탠다드에 의해 적는 주석은 unprofeesional하고 crappy한 개발자이다.			
주석을 달기로 마음 먹었다면, 시간을 투자해서 명확하게 적는 것이 좋다.			
저자는 테스트 프레임워크인 FitNesse에서 다음과 같은 코드를 발견했다.			

```java	
public void loadProperties()
{
    try
    {
        String propertiesPath = propertiesLocation + "/" + PROPERTIES_FILE;
        FileInputStream propertiesStream = new FileInputStream(propertiesPath);
        loadedProperties.load(propertiesStream);
    }
    catch(IOException e)
    {
        // No properties files means all defaults are loaded
    }
}
```
분명히 I/Oexception이 일어나면, 기본파일이 업로드 된다는 것입니다. 
하지만, 누가 그리고 어느 시점에 어떻게 디폴트 파일이 업로드된다는 구체적인 정보가 없는 것은 문제가 됩니다.
더구나 만약에 개발자 catch 블럭을 비워둔 채로 마음이 편했다면 더 큰 문제라고 보입니다.
우리가 최종적으로 유일하게 의지할 수 있는 것은 다른 모듈에 가서 무슨일이 있는 지를 보는 것입니다.
허나, 주석이 불분명확하기 때문에 그로 인해 개발자에게 그 의미를 찾도록 강요하는 주석은 가치 없는 주석입니다.

### Redundant Comments					
					
간단한 funtion에 주석을 달아서 도리어 개발자에게 혼동을 주지마십시오				
예) Tomcat에 있는 불필요하고 중복된 javadocs이 하나의 예일 것입니다.				


```java	
/**
* The Manager implementation with which this Container is
* associated.
*/
protected Manager manager = null;
/**
* The cluster with which this Container is associated.
*/
protected Cluster cluster = null;
/**
* The human-readable name of this Container.
*/
protected String name = null;
/**
* The parent Container to which this Container is a child.
*/
protected Container parent = null;
/**
* The parent class loader to be configured when we install a
* Loader.
*/
protected ClassLoader parentClassLoader = null;
/**
* The Pipeline object with which this Container is
* associated.
*/
protected Pipeline pipeline = new StandardPipeline(this);
/**
* The Realm with which this Container is associated.
*/
protected Realm realm = null;
```
### Misleading Comments
때로는 주석이 작성자의 실수로 잘못된 의미를 전달해 다른 개발자로 하여금 혼동을 주기도합니다.
```java	
// Utility method that returns when this.closed is ???true???. Throws an exception
// if the timeout is reached.
public synchronized void waitForClose(final long timeoutMillis)
throws Exception
{
    if(!closed)
    {
        wait(timeoutMillis);
        if(!closed)
        throw new Exception("MockResponseSender could not be closed");
    }
}
```
### Mandated Comments					
주석 필수! 와 같은 규정을 같는 것은 어리석을 수 있습니다. 이는 주석이 도리어 코드를 어수선하게 만들고 혼동을 가져옵니다.
아래와 같은 주석은 좀 당황스러울 수 있겠네요				

```java	
/**
*
* @param title The title of the CD
* @param author The author of the CD
* @param tracks The number of tracks on the CD
* @param durationInMinutes The duration of the CD in minutes
*/
public void addCD(String title, String author,
                int tracks, int durationInMinutes) {
    CD cd = new CD();
    cd.title = title;
    cd.author = author;
    cd.tracks = tracks;
    cd.duration = duration;
    cdList.add(cd);
}
```
### Journal Comments		
수정로그를 모듈에 매번 남기는 것은 혼란스러울 수 있습니다.	
하지만, 과거에는 Source code control/management system이 없었으므로 존재의 이유가 있었으나	
오늘날에는 지양해야할 것으로 생각됩니다.	

```java	
* Changes (from 11-Oct-2001)
* --------------------------
* 11-Oct-2001 : Re-organised the class and moved it to new package
* com.jrefinery.date (DG);
* 05-Nov-2001 : Added a getDescription() method, and eliminated NotableDate
* class (DG);
* 12-Nov-2001 : IBD requires setDescription() method, now that NotableDate
* class is gone (DG); Changed getPreviousDayOfWeek(),
* getFollowingDayOfWeek() and getNearestDayOfWeek() to correct
* bugs (DG);
* 05-Dec-2001 : Fixed bug in SpreadsheetDate class (DG);
* 29-May-2002 : Moved the month constants into a separate interface
* (MonthConstants) (DG);
* 27-Aug-2002 : Fixed bug in addMonths() method, thanks to N???levka Petr (DG);
* 03-Oct-2002 : Fixed errors reported by Checkstyle (DG);
* 13-Mar-2003 : Implemented Serializable (DG);
* 29-May-2003 : Fixed bug in addMonths method (DG);
* 04-Sep-2003 : Implemented Comparable. Updated the isInRange javadocs (DG);
* 05-Jan-2005 : Fixed bug in addYears() method (1096282) (DG);
```
#### Noise Comments	
불필요한 주석은 어떠한 정보도 포함하고 있지않습니다.


```java	
	/**		
	* Default constructor.		
	*/		
	protected AnnualDateRule() {		
	}	
	
    //No, really? Or how about this:	
    
	/** The day of the month. */		
	private int dayOfMonth;		
	
    //And then there’s this paragon of redundancy:
    
	/**		
	* Returns the day of the month.		
	*		
	* @return the day of the month.		
	*/		
	public int getDayOfMonth() {		
	    return dayOfMonth;		
	}		

```
아래의 예시를 보면, 첫번째 주석은 catch블럭이 무시되는 이유를 설명하며 적절해 보입니다.				
하지만 두번째 주석은 프로그래머가 하고싶은 말이 있어보입니다. 이보다는 function으로 빼서 try/catch 블락을 별도록 가져가는 것에 집중했어야 하지않나 싶습니다.				

```java	
private void startSending()
{
    try
    {
        doSending();
    }
    catch(SocketException e)
    {
        // normal. someone stopped the request.
    }
    catch(Exception e)
    {
        try
        {
            response.add(ErrorResponder.makeExceptionString(e));
            response.closeAll();
        }
        catch(Exception e1)
        {
            //Give me a break!
        }
    }
}
```

```java	
private void startSending()
{
    try
    {
        doSending();
    }
    catch(SocketException e)
    {
        // normal. someone stopped the request.
    }
    catch(Exception e)
    {
        addExceptionAndCloseResponse(e);
    }
}
private void addExceptionAndCloseResponse(Exception e)
{
    try
    {
        response.add(ErrorResponder.makeExceptionString(e));
        response.closeAll();
    }
    catch(Exception e1)
    {
    }
}
```
#### Scary Noise		
불필요한 주석의 사용은 도리어 혼란과 잡음을 초라하기도 합니다.	
아래 예시를 보면 copy & paste의 실수가 보입니다. 불필요함과 동시에 실수가 더 혼란을 초래합니다.	
```java	
	/** The name. */	
	private String name;	
	/** The version. */	
	private String version;	
	/** The licenceName. */	
	private String licenceName;	
	/** The version. */	
	private String info;
```
#### Don’t Use a Comment When You Can Use a Function or a Variable											
										
Consider the following stretch of code:	
```java	
	// does the module from the global list <mod> depend on the									
	// subsystem we are part of?									
	if (smodule.getDependSubsystems().contains(subSysMod.getSubSystem()))	
```
This could be rephrased without the comment as			
```java	
	ArrayList moduleDependees = smodule.getDependSubsystems();									
	String ourSubSystem = subSysMod.getSubSystem();									
	if (moduleDependees.contains(ourSubSystem))									
```					
보통 코드를 간결하고 효율적으로 만들어 Readability를 올리는 데 주력하지만										
만약 그 간결함과 깔끔함 때문데 도리어 주석이 필요하다면,  차라리 주석을 지울 수 있도록  풀어서 코드를 작성함이 옳다.										
											
#### Position Markers											
											
프로그래머들이 가끔은 코드를 표기하고자 하는 위치를 표시하기를 원할 수 있습니다.										
하지만 과도한 사용은 도리어 혼란을 불러올 수 있으므로, 꼭 필요한 경우에 사용하도록 노력한다.										
이는 도리어 배너가 위치가 확인하게 눈에 들어오는 긍정적인 효과를 가져올 수 있습니다.										
```java											
	i.e.	// Actions //////////////////////////////////									
```											

#### Closing Brace Comments											
											
때때로 프로그래머들이 혼돈을 방지하기 위해 Closing curling bracket에 다음과 같이 주석을 표현하곤 합니다. 										
이런 표기가 보인다는 것은 함수를 줄이고 정리해서 복잡함을 완화해야 하는 신호가 아닐까 생각됩니다.								
```java	
public class wc {
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        int lineCount = 0;
        int charCount = 0;
        int wordCount = 0;
        try {
            while ((line = in.readLine()) != null) {
            lineCount++;
            charCount += line.length();
            String words[] = line.split("\\W");
            wordCount += words.length;
        } //while <---
            System.out.println("wordCount = " + wordCount);
            System.out.println("lineCount = " + lineCount);
            System.out.println("charCount = " + charCount);
        } // try <---
            catch (IOException e) {
            System.err.println("Error:" + e.getMessage());
        } //catch <---
    } //main
}
```
#### Attributions and Bylines							
							
		/* Added by Rick */					
코드 작성자를 기억하는 것은 Source code control systems이 해야 할 일입니다. 						
굳이 남길 필요가 없어 보입니다.						
							
#### Commented-Out Code							
							
							
		InputStreamResponse response = new InputStreamResponse();					
		response.setBody(formatter.getResultStream(), formatter.getByteCount());					
		// InputStream resultsStream = formatter.getResultStream();					
		// StreamReader reader = new StreamReader(resultsStream);					
		// response.setContent(reader.read(formatter.getByteCount()));					
							
저자는 주석처리된 코드를 남기는 것만큼 협오, 불쾌한(odious)것은 없으며 Don't do this!라고 말합니다.						
더욱 심각하고 공감가는 문제는 다른 프로그래머는 이것을 삭제할 용기가 없다는 것입니다.						
따라서 이는 찌꺼기 처럼 쌓이게 됩니다. 60년대는 코드를 주석처리를 해서 중요하거나 긴박한 수정에 대한 히스토리를 남겼을 수 있습니다. 하지만, 지금은 Source code control systems가 해야할 일 입니다.						
							
#### HTML Comments							
							
소스코드내에 주석에 HTML 코드가 섞여 있지않도록 해야겠습니다. 혼란스럽고 Editor 나 IDE에서 						
읽고 이해하는데 어렵게 만듭니다.						
							
	/**					
	* Task to run fit tests.					
	* This task runs fitnesse tests and publishes the results.					
	* <p/>					
	* <pre>					
	* Usage:					
	* &lt;taskdef name=&quot;execute-fitnesse-tests&quot;					
	* classname=&quot;fitnesse.ant.ExecuteFitnesseTestsTask&quot;					
	* classpathref=&quot;classpath&quot; /&gt;					
	* OR					
	* &lt;taskdef classpathref=&quot;classpath&quot;					
	* resource=&quot;tasks.properties&quot; /&gt;					
	* <p/>					
	* &lt;execute-fitnesse-tests					
	* suitepage=&quot;FitNesse.SuiteAcceptanceTests&quot;					
	* fitnesseport=&quot;8082&quot;					
	* resultsdir=&quot;${results.dir}&quot;					
	* resultshtmlpage=&quot;fit-results.html&quot;					
	* classpathref=&quot;classpath&quot; /&gt;					
	* </pre>					
	*/					
							
							
### Nonlocal Information							
							
아래 예시와 같이 주석이 특정한 기능에 대해 정보를 주는것이 아닌 시스템 전반에 걸친 설명을 하는 것은 지양해야겠습니다.

	/**					
	* Port on which fitnesse would run. Defaults to <b>8082</b>.					
	*					
	* @param fitnessePort					
	*/					
	public void setFitnessePort(int fitnessePort)					
	{					
	this.fitnessePort = fitnessePort;					
	}					
							
							
#### Too Much Information							
							
아래 예시와 같이 불필요한 정보를 장황하게 설명하지 않도록 합니다.						
아래 주석은 Funtion이 base64를 인코딩과 디코딩이 가능한지를 테스트하는 모듈에서 발췌된 예시입니다.						
RFC 번호외에 나머지는 불필요한 난해한 정보로 보이네요.	

		/*					
		RFC 2045 - Multipurpose Internet Mail Extensions (MIME)					
		Part One: Format of Internet Message Bodies					
		section 6.8. Base64 Content-Transfer-Encoding					
		The encoding process represents 24-bit groups of input bits as output					
		strings of 4 encoded characters. Proceeding from left to right, a					
		24-bit input group is formed by concatenating 3 8-bit input groups.					
		These 24 bits are then treated as 4 concatenated 6-bit groups, each					
		of which is translated into a single digit in the base64 alphabet.					
		When encoding a bit stream via the base64 encoding, the bit stream					
		must be presumed to be ordered with the most-significant-bit first.					
		That is, the first bit in the stream will be the high-order bit in					
		the first 8-bit byte, and the eighth bit will be the low-order bit in					
		the first 8-bit byte, and so on.					
		*/					
							
#### Inobvious Connection							
주석과 코드와의 연관성은 분명해야 합니다.						
예를 들어 아래 apache commons에서 발췌한 주석을 보면, filter byte가 먼지? 						
그리고 이게 작성된 +1을 의미하는 지? *3을 의미하는지? 둘다를 의미하는지?						
주석의 목적이 스스로를 설명할 수 없는 코드라는 친구를 설명하는 것인데 설명이 부족해보인다.						
							
    /*					
    * start with an array that is big enough to hold all the pixels					
    * (plus filter bytes), and an extra 200 bytes for header info					
    */					
    this.pngBytes = new byte[((this.width + 1) * this.height * 3) + 200];					

#### Function Headers							
	잘 지어진 funtion의 이름이 일반적으로 주석보다 낮다.


