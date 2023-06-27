# Chapter 29. xUnit Patterns

## Assertion
- Q: How do you check that tests worked correctly?
- A: Write boolean expressions that automate your judgment about whether the code worked.
- The decisions have to be boolean - True generally means everything is okay, and false means something unexpected happened.

## Fixture
- Q: How do you create common objects needed by several tests? 
- A: Convert the local variables in the tests into instance variables. Override setUp() and initialize those variables.

## External Fixture
- Q: How do you release external resources in the fixture?
- A: Override tearDown() and release the resources. Remember that the goal of each test is to leave the world in exactly the same state as before it ran.
```
    setUp(self):
        self.file= File("foobar").open()
    testMethod(self): 
        ...run the test...
    tearDown(self): 
        self.file.close()
```

## Test Method
- Q: How do you represent a single test case?
- A: As a method whose name begins with "test."
- By convention, the name of the method begins with "test." Tools can look for this pattern to automatically create suites of tests given a class.
- Test methods should be easy to read, pretty much straightline code.

## Exception Test
- Q: How do you test for expected exceptions? 
- A: Catch expected exceptions and ignore them, failing only if the exception isn't thrown.
```
public void testMissingRate() { 
    try {
        exchange.findRate("USD", "GBP");
        fail();
    } catch (IllegalArgumentException expected) { }
}
```

## All Tests
- Q: How do you run all tests together?
- A: Make a suite of all the suites - one for each package, and one aggregating the package tests for the whole application.