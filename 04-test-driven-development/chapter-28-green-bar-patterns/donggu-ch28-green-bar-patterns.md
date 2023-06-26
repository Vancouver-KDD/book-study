# Chapter 28. Green Bar Patterns

## Fake it (Until You Make it)
- Q: What is your first implementation once you have a broken test?
- A: Return a constant. Once you have the test running, gradually transform the constant into an expression using variables.
```
    return "1 run, 0 failed"
    -->
    return "%d run, 0 failed" % self.runCount
    -->
    return "%d run, %d failed" % (self.runCount , self failureCount)
```

## Triangulate
- Q: How do you most conservatively drive abstraction with tests?
- A: Abstract only when you have two or more examples.
```
    public void testSum() { 
        assertEquals(4, plus(3, 1));
    }
    private int plus(int augend, int addend) {
        return 4;
    }
    -->
    public void testSum() {
        assertEquals(4, plus(3, 1)); 
        assertEquals(7, plus(3,4));
    }
    -->
    private int plus(int augend, int addend) {
        return augend + addend;
    }
```

## Obvious Implementation
- Q: How do you implement simple operations?
- A: Just implement them.

## One to Many
- Q: How do you implement an operation that works with collections of objects?
- A: Implement it without the collections first, then make it work with collections.