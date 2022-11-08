#Duplicated Code
Duplication means that every time you read these copies, you need to read them carefully to see if there's **any difference**. If you need to change the duplicated code, you have to find and catch each duplication.

###1. Extract Function (106)

###2. Slide Statements (223)
#####Example
```typescript
//Before
if (isSpecialDeal()) {
  total = price * 0.95;
  send();
}
else {
  total = price * 0.98;
  send();
}

//After
if (isSpecialDeal()) {
  total = price * 0.95;
}
else {
  total = price * 0.98;
}
send();
```

###3. Pull Up Method (350)
#####Example
```typescript
//Before
class Unit {}
class Soldier extends Unit {
  getHealth(): Health {
    return this.health;
  }
}
class Tank extends Unit {
  getHealth(): Health {
    return this.health;
  }
}

//After
class Unit {
  getHealth(): Health {
    return this.health;
  }
}
class Soldier extends Unit {}
class Tank extends Unit {}
```

###Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
- https://refactoring.guru/smells/long-method
