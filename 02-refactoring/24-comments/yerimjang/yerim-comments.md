# Comments
![alt text](https://refactoring.guru/images/refactoring/content/smells/comments-01-2x.png "Comment")
A method is filled with explanatory comments. When you feel the need to write a comment, first try to refactor the code so that any comment becomes superfluous.
>The best comment is a good name for a method or class.

### 1. Extract Function (106)

### 2. Change Function Declaration (124)

### 3. Introduce Assertion (302)
##### Example
```typescript
//Before
if (this.discountRate) {
  base = base - (this.discountRate * base);
}

//After
assert(this.discountRate >= 0);
if (this.discountRate) {
  base = base - (this.discountRate * base);
}
```
##### Benefit
If an assumption isn’t true and the code therefore gives the wrong result, it’s better to stop execution before this causes fatal consequences and data corruption. This also means that you neglected to write a necessary test when devising ways to perform testing of the program.
##### Drawbacks
Sometimes an exception is more appropriate than a simple assertion. You can select the necessary class of the exception and let the remaining code handle it correctly.
- *When is an exception better than a simple assertion? If the exception can be caused by actions of the user or system and you can handle the exception. On the other hand, ordinary unnamed and unhandled exceptions are basically equivalent to simple assertions—you don’t handle them and they’re caused exclusively as the result of a program bug that never should have occurred.*
##### When to use
Don’t overdo it with use of assertions for everything in your code. Check for only the conditions that are necessary for correct functioning of the code. If your code is working normally even when a particular assertion is false, you can safely remove the assertion.

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/smells/comments
- https://refactoring.guru/introduce-assertion
