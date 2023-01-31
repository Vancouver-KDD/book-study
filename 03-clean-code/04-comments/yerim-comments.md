# Comments
> The proper use of comments is to compensate for our failure to express ourself in code.

### Comments Do Not Make Up for Bad Code
- Rather than your time writing the comments that explain the mess you've made, spend it cleaning that mess.

### Explain Yourself in Code
```java
//before
// Check to see if the employee is eligible for full benefits
if ((employee.flags & HOURLY_FLAG) && (employee.age >  65))

//after
if (employee.isEligibleForFullBenefits())
```

### Good Comments
- The only truly good comment is the comment you found a way **not to write**.

##### Legal Comments
- Copyright and authorship statements
```java
// Copyright (C) 2003, 2004, 2005 by Object Mentor, Inc. All rights reserved.
// Release under the terms of the GNU General Public License version 2 or later.
```

##### Informative Comments
```java
// Returns an instance of the Responder being tested.
protected abstract Responder responderInstance();

// format matched kk:mm:ss EEE, MMM dd, yyyy
Pattern timeMatcher = Pattern.compile(
  "\\d*:\\d*:\\d* \\w*, \\w* \\d*, \\d*"
);
```
- Still, it might have been better, and clearer, if this code had been moved to a special class that converted the formats of dates and time. Then the comment would likely have been superfluous.

##### Explanation of Intent
```java
public int compareTo(Object o) {
  if (o instanceof WikiPagePath) {
    WikiPagePath p = (WikiPagePath) o;
    String compressedName = StringUtil.join(names, "");
    String compressedArgumentName = StringUtil.join(p.names, "");
    return compressedName.compareTo(compressedArgumentName);
  }
  return 1; // we are greater because we are the right type.
}
```
- You might not agree with the programmer's solution to the problem, but **at least you know what he was trying to do**.

##### Clarification
```java
assertTrue(a.compareTo(a) == 0);        // a == a
assertTrue(a.compareTo(b) != 0);        // a != b
assertTrue(ab.compareTo(ab) == 0);      // ab == ab
assertTrue(a.compareTo(b) == -1);       // a < b
assertTrue(aa.compareTo(ab) == -1);     // aa < ab
assertTrue(ba.compareTo(bb) == -1);     // ba > bb
assertTrue(b.compareTo(a) == 1);        // b > a
assertTrue(ab.compareTo(aa) == 1);      // ab > aa
assertTrue(bb.compareTo(ba) == 1);      // bb > ba
```

##### Warning of Consequences
```java
public static SimpleDateFormat makeStandardHttpDateFormat() {
  //SimpleDateFormat is not thread safe,
  //so we need to create each instance independently.
  SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
  df.setTimeZone(TimeZone.getTimeZone("GMT"));
  return df;
}
```

##### TODO Comments
```java
// TODO-MdM these are not needed
// We expect this to go away when we do the checkout model
protected VersionInfo makeVersion() throws Exception {
  return null;
}
```

##### Amplification
```java
String listItemContent = match.group(3).trim();
// the trim is real important. It removes the starting
// spaces that could cause the item to be recognized
// as another list.
new ListItemWidget(this, listItemContent, this.level + 1);
return buildList(text,substring(match.end()));
```

##### Javadocs in Public APIs
- If you are writing a public API, then you should certainly write good javadocs for it.
- Javadocs can be just as misleading, nonlocal, and dishonest as any other kind of comment.

### Bad Comments
##### Mumbling
- Any comment that forces you to look in another module for the meaning of that comment has failed to communicate to you and is not worth the bits if consumes.
```java
public void loadProperties() {
  try {
    String propertiesPath = propertiesLocation +  "/" + PROPERTIES_FILE;
    FileInputStream propertiesStream = new FileInputStream(propertiesPath);
    loadedProperties.load(propertiesStream);
  } catch(IOException e) {
    // No properties files means all defaults are loaded.
  }
}
```

##### Redundant Comments
- The comment that is completely redundant.

##### Misleading Comments
- The comment that isn't precise enough to be accurate.

##### Mandated Comments
```java
/**
 *
 * @param title The title of the CD
 * @param author The author of the CD
 * @param tracks The number of tracks on the CD
 * @param durationInMinutes The duration of the CD in minutes
 */
public void addCD(String title, String author, int tracks, int durationInMinutes) {
  CD cd = new CD();
  ...
}
```

##### Journal Comments
```java
/**
 * Changes (from 11-Oct-2001)
 * --------------------------
 * 11-Oct-2001 : Re-organized the class and moved it to new package
 *               com.jrefinery.date (DG);
 * ...
 */
```

##### Noise Comments
- They restate the obvious and provide no new information
```java
/**
 * Default constructor.
 */
protected AnnualDateRule() {
}
```

##### Scary Noise
- Javadocs can also be noisy.
```java
/** The name. */
private String name;

/** The version. */
private String version;
```

##### Don't Use a Comment When You Can Use a Function or a Variable
- Before:
```java
// does the module from the global list <mod> depend on the
// subsystem we are part of?
if (smodule.getDependSubsystems().contains(subSysMod.getSubSystem()))
```
- After:
```java
ArrayList moduleDependees = smodule.getDependSubsystems();
String ourSubSystem = subSysMod.getSubSystem();
if (moduleDependees.contains(ourSubSystem))
```

##### Position Markers
- If you overuse banners, they'll fail into the background noise and be ignored.
```java
// Actions ////////////////////
```

##### Closing Brace Comments
- The comment on closing braces.
```java
// ...
  try {
    while ((line = in.readLine()) != null) {
      // ...
    } //while
  } //try
  catch (IOException e) {
    System.err.println("Error:" + e.getMessage());
  } //catch
// ...
```

##### Attributions and Bylines
```java
/* Added by Rick */
```

##### Commented-Out Code
```java
this.bytePos = writeBytes(pngIdBytes, 0);
//hdrPos = bytePos;
writeHeader();
writeResolution();
//dataPost = bytePost;
if (writeImageData()) {
  writeEnd();
  this.pngBytes = resizeByteArray(this.pngBytes, this.maxPos);
} else {
  this.pngBytes = null;
}
return this.pngBytes;
```

##### HTML Comments
```java
/**
 * Task to run fit test.
 * This task runs fitness tests and published the results.
 * <p/>
 * <pre>
 * ...
 * </pre>
 */
```

##### Nonlocal Information
- Don't offer systemwide information in the context of a local comment.
```java
/**
 * Port on which fitness would run. Default to <b>8082</b>
 *
 * @param fitnessPort
 */
public void setFitnessPort(int fitnessPort) {
  this.fitnessPort = fitnessPort;
}
```

##### Too Much Information
- Don't put interesting historical discussions or irrelevant descriptions of details into your comments.

##### Inobvious Connection
- The connection between a comment and the code it describes should be obvious.

##### Function Headers
- Short functions don't need much description.

##### Java in Nonpublic Code
- They are anathema to code that is not intended for public consumption.
