# Speculative Generality
Sometimes code is created “just in case” to support anticipated future features that never get implemented. As a result, code becomes hard to understand and support.

### 1. Collapse Hierarchy (380)

### 2. Inline Function (115)

### 3. Inline Class (186)

### 4. Change Function Declaration (124)

### 5. Remove Dead Code (237)
##### Example
```typescript
//Before
if (false) {
  doSomethingThatUsedToMatter();
}

//After
//Removed
```

### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
