## Chapter 27 **Testing Patterns**

1. **Child Test**

    - How do you get a test case running that turns out to be too big? Write a smaller test case that represents the broken part of the bigger test case. Get the smaller test case running. Reintroduce the larger test case.
2. Mock Object
    - How do you test an object that relies on an expensive or complicated resource? Create a fake version of the resource that answers constants.
    - Another value of mocks, aside from performance and reliability, is readability. You can read the preceding test from one end to another.

```java
public void testOrderLookup() {
Database db= new MockDatabase();
db.expectQuery("select order_no from Order where cust_no is 123"); db.returnResult(new String[] {"Order 2" ,"Order 3"});
...
}
```

1. Self Shunt
2. **Log String**

    - How do you test that the sequence in which messages are called is correct? Keep a log in a string, and append to the string when a message is called.
3. **Crash Test Dummy**

4. Broken Test
    - Finish a solo session by writing a test case and running it to be sure it doesn't pass. When you come back to the code, you then have an obvious place to start.
5. Clean Check-in