### CH. 11

- The two subclasses, Dollar and Franc, have only their constructors. But because a constructor is not reason enough to have a subclass, we want to delete the subclasses.

```java
~~static Money dollar(int amount) { 
	return new **Dollar**(amount);
}~~
```

```java
static Money dollar(int amount) { 
	return new **Money**(amount, "USD");
}
```

- The test we wrote forcing us to compare currencies instead of classes makes sense only if there are multiple classes.
    - Therefore,  update **`testDifferentClassEquality`  `testEqualiry`**