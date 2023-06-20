## Chapter 28 Green Bar Patterns

1. **Fake It ('Til You Make It)**

A simple example occurred in our implementation of xUnit:

```java
**return "**1 **run, 0 failed"**
```

became:

```java
**return "**%d **run, 0 failed" % self.runCount**
```

became:

```java
**return "%d run, %d failed" % (self.runCount , self failureCount)**
```

1. **Triangulate**

```java
public void testSum() { assertEquals(4, plus(3, 1));}
private int plus(int augend, int addend) { 
	return 4;
}
```

If we are triangulating to the right design, we have to write:

```java
public void **testSum() {
	assertEquals(4, plus(3, 1));
	**assertEquals(7, plus(3,4));**
}
```

**When we have the second example, we can abstract the implementation of plus():**

```java
private int plus(int augend, int addend) { 
	return augend + addend;
}
```

1. Obvious Implementation 
    - Solving "clean code" at the same time that you solve "that works" can be too much to do at once. As soon as it is, go back to solving "that works," and then "clean code" at leisure.