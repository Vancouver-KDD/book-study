#Mysterious Name

One of the most important parts of clear code is **good names**, so we put a lot of thought into naming functions, modules, variables, classes, so they clearly communicate what they do and how to use them.

###1. Change Function Declaration (124)
#####Example
```typescript
//Before
function sendFN(String message) {
  //Sending Firebase Notification message
}

//After
function sendFirebaseNotification(String message) {
  //Sending Firebase Notification message
}
```

###2. Rename Variable (137)

###3. Rename Field (244)

###Reference
- Refactoring (Improving the Design of Existing Code) - Martin Fowler with contributions by Kent Beck
