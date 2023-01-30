# Comments
- Comments are always failures.
- Every time you write a comment, you should grimace and feel the failure of your ability of expression.
- Only the code can truly tell you what it does.
- we will expend significant energy to minimize them.

## Comments Do Not Make Up for Bad Code
## Explain Yourself in Code

## Good Comments
### Legal Comments
```java
// Copyright (C) 2003,2004,2005 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the GNU General Public License version 2 or later.
```
### Informative Comments
```java
// format matched kk:mm:ss EEE, MMM dd, yyyy 
    Pattern timeMatcher = 
            Pattern.compile("\\d*:\\d*:\\d* \\w*, \\w* \\d*, \\d*");
```
### Explanation of Intent
```java
public int compareTo(Object o) {
    ...
    return 1; // we are greater because we are the right type.
} 
```
```java
void testConcurrentAddWidgets() {
    ...
    //This is our best attempt to get a race condition 
    // by creating large number of threads.
    for(int i=0;i< 25000;i++){
        ...
    }
    ...
}
```
### Clarification
```java
assertTrue(a.compareTo(a) == 0);    // a == a
assertTrue(a.compareTo(b) != 0);    // a != b
assertTrue(ab.compareTo(ab) == 0);  // ab == ab
```
- There is a substantial risk, of course, that a clarifying comment is incorrect.
### Warning of Consequences
```java
// Don't run unless you
// have some time to kill.
public void _testWithReallyBigFile(){
    ...
}
```
```java
public static SimpleDateFormat makeStandardHttpDateFormat() {
    //SimpleDateFormat is not thread safe,
    //so we need to create each instance independently.
    SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM  yyyy HH:mm:ss z");
    df.setTimeZone(TimeZone.getTimeZone("GMT"));
    return df; 
}
```
### TODO Comments
- scan through them regularly and eliminate the ones you can.
### Amplification
```java
String listItemContent = match.group(3).trim(); 
// the trim is real important.  It removes the starting 
// spaces that could cause the item to be recognized 
// as another list. 
new ListItemWidget(this, listItemContent, this.level + 1); 
return buildList(text.substring(match.end()));
```
### Javadocs in Public APIs
- There is nothing quite so helpful and satisfying as a well-described **public** API.
- but Javadocs can be just as misleading, nonlocal, and dishonest as any other kind of comment.

## Bad Comments
### Mumbling
```java
public void loadProperties() {
    try {
      String propertiesPath = propertiesLocation + "/" + PROPERTIES_FILE;
      FileInputStream propertiesStream = new FileInputStream(propertiesPath);
      loadedProperties.load(propertiesStream);
    } catch(IOException e) {
        // No properties files means all defaults are loaded 
    }
} 
```
- What is all defaults??
### Redundant Comments (with Code)
### Misleading Comments
### Mandated Comments
```java
 /**
   *
   * @param title The title of the CD
   * @param author The author of the CD
   * @param tracks The number of tracks on the CD
   * @param durationInMinutes The duration of the CD in minutes
   */
  public void addCD(String title, String author, int tracks, int durationInMinutes) {
      ...
  }
```
### Journal Comments
```java
/*
 * Changes (from 11-Oct-2001
 * --------------------------  
 * 11-Oct-2001 : Re-organised the class and moved it to new package  
 *               com.jrefinery.date (DG);
 * 05-Nov-2001 : Added a getDescription() method, and eliminated NotableDate  
 *               class (DG);
 * ...
 */
```
### Noise Comments
```java
/**
 * Default constructor.
 */
protected AnnualDateRule() {
}
```
### Scary Noise
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
### Don’t Use a Comment When You Can Use a Function or a Variable
- the comment could be removed.
### Position Markers
```java
...
// Actions //////////////////////////////////
...
```
### Closing Brace Comments
- try to shorten your functions instead
```java
while ((line = in.readLine()) != null) {
    lineCount++;
    charCount += line.length();
    String words[] = line.split("\\W");
    wordCount += words.length;       
} //while 
```
### Attributions and Bylines
- the source code control system is a better place for this kind of information.
```java
/* Added by Rick */
```
### Commented-Out Code
```java
InputStreamResponse response = new InputStreamResponse(); 
response.setBody(formatter.getResultStream(), formatter.getByteCount());
// InputStream resultsStream = formatter.getResultStream(); 
// StreamReader reader = new StreamReader(resultsStream); 
// response.setContent(reader.read(formatter.getByteCount()));
```
### HTML Comments
```java
/**
 * Task to run fit tests.
 * This task runs fitnesse tests and publishes the results.
 * <p/>
 * <pre>
 * Usage:
 * &lt;taskdef name=&quot;execute-fitnesse-tests&quot;
 *     classname=&quot;fitnesse.ant.ExecuteFitnesseTestsTask&quot;
 *     classpathref=&quot;classpath&quot; /&gt;
 * OR
 * &lt;taskdef classpathref=&quot;classpath&quot;
 *             resource=&quot;tasks.properties&quot; /&gt;
 * <p/>
 * &lt;execute-fitnesse-tests
 *     suitepage=&quot;FitNesse.SuiteAcceptanceTests&quot;
 *     fitnesseport=&quot;8082&quot;
 *     resultsdir=&quot;${results.dir}&quot;
 *     resultshtmlpage=&quot;fit-results.html&quot;
 *     classpathref=&quot;classpath&quot; /&gt;
 * </pre>
 */

```
### Nonlocal Information
```java
/**
 * Port on which fitnesse would run. Defaults to <b>8082</b>.
 * @param fitnessePort  */
 */
public void setFitnessePort(int fitnessePort) {
    this.fitnessePort = fitnessePort;
}
```
- no control over what that default is
### Too Much Information
```java
/*
 RFC 2045 - Multipurpose Internet Mail Extensions (MIME)
 Part One: Format of Internet Message Bodies
 section 6.8.  Base64 Content-Transfer-Encoding
 The encoding process represents 24-bit groups of input bits as output  strings of 4 encoded characters. Proceeding from left to right, a
 24-bit input group is formed by concatenating 3 8-bit input groups.  These 24 bits are then treated as 4 concatenated 6-bit groups, each  of which is translated into a single digit in the base64 alphabet.
 When encoding a bit stream via the base64 encoding, the bit stream
 must be presumed to be ordered with the most-significant-bit first.  That is, the first bit in the stream will be the high-order bit in
 the first 8-bit byte, and the eighth bit will be the low-order bit in  the first 8-bit byte, and so on.
 */
```
### Inobvious Connection
- Inobvious <-> obvious
```java
/*
 * start with an array that is big enough to hold all the pixels
 * (plus filter bytes), and an extra 200 bytes for header info
*/
this.pngBytes = new byte[((this.width + 1) * this.height * 3) + 200];
```
### Function Headers
### Javadocs in Nonpublic Code