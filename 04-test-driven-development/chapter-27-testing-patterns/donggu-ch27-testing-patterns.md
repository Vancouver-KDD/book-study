# Chapter 27. Testing Patterns

## Child Test
- Q: How do you get a test case running that turns out to be too big?
- A: Write a smaller test case that represents the broken part of the bigger test case.

## Mock Object
- Q: How do you test an object that relies on an expensive or complicated resources?
- A: Create a fake version of the resource that answers constants.
```
public void testOrderLookup() {
    Database db = new MockDatabase();
    db.expectQuery("select order_no_from Order where_cust_no is 123");
    db.retrunResult(new String[] {"Order 2", "Order 3"});
}
```
- If the MockDatabase does not get the query it expects, then it throws an exceptino.
- Mock Objects encourage we down the path of carefully considering the visibility of every object, reducing the coupling in your designs.

## Self Shunt
- Q: How do you test one object communicates correctly with another?
- A: Have the object under test commnunicate with the test case instead of with the object it expects.
- Tests written with Self Shunt tend to read better than tests written without it.

## Log String
- Q: How do you test that the sequence in which messages are called is corredct?
- A: Keep a log in a string, and append to the string  when a messae is called.
- Log Strings are particularly useful when you are implementing Observer and you expect notifications to come in a certain order.

## Crash Test Dummy
- Q: How do you test error code that is unlikely to be invoked? 
- A: Invoke it anyway with a special object that throws an exception instead of doing real work.
- A Crash Test Dummy is like a Mock Object, except you don't need to mock up the whole object.

## Broken Test
- Q: How do you leave a programming session when you're programming alone?
- A: Leave the last test broken.
- A broken test doesn't make the program any less finished, it just makes the status of the program manifest. The ability to pick up a thread of development quickly after weeks of hiatus is worth that little twinge of walking away from a red bar.

## Clean Check-in
- Q: How do you leave a programming session when you're programming in a team?
- A: Leave all of the tests running.

