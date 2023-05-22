### CH. 9

Implementing currency steps;

1. First we declare currency() in Money:
2. Then we implement it in both subclasses:
3. We could store the currency in an instance variable and just return the variable.
4. Franc.times() should call the factory method instead of the contructor

```java
Dollar(int amount, String currency) { 
	this.amount = amount; 
	this.currency = currency;
}
Money times(int multiplier) {
	return Money.dollar(amount * multiplier); 
}
```