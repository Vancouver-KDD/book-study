# Loops
We find pipeline operations, such as filter and map, help us quickly see the elements that are included in the processing and what is done with them.

### 1. Replace Loop with Pipeline (231)
##### Example
```typescript
//Before
const names = [];
for (const i in input) {
  if (i.job === 'programmer') {
    names.push(i.name);
  }
}

//After
const names = input.filter(i => i.job === 'programmer').map(i => i.name);
```
### Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
