# Feature Envy
A classic case of Feature Eny occurs when a function in one module spends more time **communicating with functions or data** inside another module than it does within its own module. We've lost count of the times we've seen a function invoking half-a-dozen getter methods on another object to calculate some value.

### 1. Move Function (198)
##### Example
```typescript
//Before
class ClassA {
  aMethod(): void {
    //...
  }
}

class ClassB {
  //...
}

//After
class ClassA {
  //...
}

class ClassB {
  aMethod(): void {
    //...
  }
}
```
### 2. Extract Function (106)

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
