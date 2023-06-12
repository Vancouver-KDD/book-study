# Chapter 24. xUnit Retrospective


If the time comes for you to implement your own testing framework, then the sequence presented in Part II of this book can serve as your guide. The details of the implementation
are not nearly as important as the test cases. If you can support a set of test cases like the ones given here, then you can write tests that are isolated and can be composed, and you will be on your way to being able to develop test-first.

There are a couple of reasons for implementing xUnit yourself, even if there is a version already available:

* Mastery - The spirit of xUnit is simplicity. Rolling your own will give you a tool over which you have a feeling of mastery.

* Exploration - When I'm faced with a new programming language, I implement xUnit.

When you begin using xUnit, you will discover a big difference between assertions that fail 
and other kinds of errors while running tests: assertion failures consistently take much longer to debug.

JUnit declares a simple Test interface that both TestCase  and TestSuite implement. If you 
want JUnit tools to be able to run your tests, then you can implement the Test interface, too.


```python
public interface Test {
    public abstract int countTestCases(); 
    public abstract void run(TestResult result);
}
```
