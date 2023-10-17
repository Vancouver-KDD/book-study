# Assertive Programming

## Use Assertions to Prevent the Impossible
* Never say: "This can never happen". Add code to check it. Easiest way is with assertions.
    * _assert_ that checks a Boolean condition.
    * Add a descriptive string if possible (i.e. Java)
    * Assertions are useful check on an algorithm's operation.
```
assert result != null && result.size() > 0 : "Empty result from XYZ";
```

```
books = my_sort(find("scifi"))
assert(is_sorted?(books))
```
* Don't use assertions in place of real error handling. Assertions only check for things that should never happen.
```
puts("Enter 'Y' or 'N': ")
ans = gets[0] # Grab first character of response
assert((ch == 'Y') || (ch == 'N')) # Very bad idea!
```

* If you need to free resources, catch the assertion's exception or trap the exit, and run your own error handler.

## Assertions and Side Effects
* If evaluating the conditions has side effects, assertions can end up creating new errors.
* Heinsenbug - debugging that changes the behaviour of the system being debugged.
```
while (iter.hasMoreElements()) { 
    assert(iter.nextElement() != null); 
    Object obj = iter.nextElement();
    // ....
}
```
* Do this instead:
```
while (iter.hasMoreElements()) { 
    Object obj = iter.nextElement();
    assert(obj != null);
    // ....
}
```

## Leave Assertions Turned On
* Assertions can add some overhead to code. 
* However, testing does not find all bugs, and your program runs in a dangerous world (prod environment).
    * user won't exhaust memory
    * log files won't fill the storage partition

* Your first line of defense is checking for any possible error, and your second is using assertions to try to detect those you've missed.

* Even if you _do_ have performance issues, turn off only those assertions that really hit you.

## Exercise
A quick reality check. Which of these “impossible” things can
happen?
* A month with fewer than 28 days
* Error code from a system call: can’t access the current directory In C++: a = 2; b = 3; but (a + b) does not equal 5
* A triangle with an interior angle sum ≠ 180°
* A minute that doesn’t have 60 seconds
* (a + 1) <= a


