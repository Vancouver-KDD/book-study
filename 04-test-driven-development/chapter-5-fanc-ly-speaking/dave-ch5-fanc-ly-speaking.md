## Chapter 5. Franc-ly Speaking


```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
//Make "amount" private 
//Dollar side effects? 
Money rounding? 
//equals()
hashCode()
Equal null
Equal object
5 CHF * 2 = 10 CHF <--
```

A prerequisite seems to be having an object like Dollar, but to represent francs. If we can get the object Franc to work the way that the object Dollar works now, we'll be closer to being able to write and run the mixed addition test.

Let's utilize the Dollar test.
```java
public void testFrancMultiplication() { 
    Franc five= new Franc(5);
    assertEquals(new Franc(10), five.times(2)); 
    assertEquals(new Franc(15), five.times(3));
}
```

Copying the Dollar code and replacing Dollar with Franc.
The first three phases need to go by quickly, so we get to a known state with the new functionality.
Good design at good times. Make it run, make it right.

```java
//Franc
class Franc {
    private int amount; 
    Franc(int amount) {
        this.amount= amount; 
    }
    Franc times(int multiplier) {
        return new Franc(amount * multiplier); 
    }
    public boolean equals(Object object) { 
        Franc franc= (Franc) object; 
        return amount == franc.amount;
    } 
}
```

```
<To-do List>
$5 + 10 CHF = $10 if rate is 2:1 
//$5 * 2 = $10
//Make "amount" private 
//Dollar side effects? 
Money rounding? 
//equals()
hashCode()
Equal null
Equal object
//5 CHF * 2 = 10 CHF
Dollar/Franc duplication 
Common equals 
Common times
```
Now we have duplication galore, and we have to eliminate it before writing our next test. 
We'll start by generalizing equals(). However, we can cross an item off our to-do list, even though we have to add two more.

<What we've done>
* Couldn't tackle a big test, so we invented a small test that represented progress 
* Wrote the test by shamelessly duplicating and editing
* Even worse, made the test work by copying and editing model code wholesale 
* Promised ourselves we wouldn't go home until the duplication was gone
