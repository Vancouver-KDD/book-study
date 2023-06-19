## Chapter 25 **Test-Driven Development Patterns**

1. Isolated  Test - decoupling 
    - test one thing at a time
    - test should not depend on other tests - failing domino
2. Test List
- what we put on the list are the tests we want to implement.
    - First, put on the list examples of every operation that you know you need to implement.
    - Next, for those operations that don't already exist, put the null version of that operation on the list.
    - Finally, list all of the refactorings that you think you will have to do in order to have clean code at the end of this session.
1. Test First
    - When should you write your tests? Before you write the code that is to be tested.
2. **Assert First**

    - When I test assert-first, I find it has a powerful simplifying effect. When you are writing a test, **you are solving several problems at once,** even if you no longer have to think about the implementation.
3. **Test Data**

    - One trick in Test Data is to try never to use the same constant to mean more than one thing.
        - If we use 2 for the first argument, for example, then we should use 3 for the second argument.
    
    > The alternative to Test Data is Realistic Data, in which you use data from the real world. Realistic Data is useful when:
    You are testing real-time systems using traces of external events gathered from the actual execution
    You are matching the output of the current system with the output of a previous system (parallel testing)
    You are refactoring a simulation and expect precisely the same answers when you are finished, particularly if floating point accuracy may be a problem
    > 
4. **Evident Data**

```java
Bank bank= new Bank().
bank.addRate("USD", "GBP", STANDARD_RATE); 
bank.commission(STANDARD_COMMISSION);
Money result= bank.convert(new Note(100, "USD"), "GBP"); 
assertEquals(new Note(***49.25***, "GBP"), result);
```

```java
Bank bank= new Bank();
bank.addRate("USD", "GBP", ***2***);
bank.commission(***0.015***);
Money result= bank.convert(new Note(100, "USD"), "GBP"); 
assertEquals(new Note(***100 / 2 * (1 - 0.015)***, "GBP"), result);
```

You are writing tests for a reader, not just the computer.