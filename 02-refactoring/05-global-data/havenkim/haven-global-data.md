# Global Data

Unless they can be declared as *constant*, try not to use Global variable unless you are willing to bite the bullet in the future.

### 1. Encapsulate Variable (132p) ###

The mechanic introduced in this example is important since IDE cannot guarantee finding all global variable accesses.

This is even another proof that why we should avoid using Global Variables ☺️

##### Examples

1. Make getter/setter of the Global variable.

2. Try finding all references of the varialbe and then change the variable manipulation statements into function calls.

3. **Rename variable to random** so that it breaks any hidden references.

```ts
const gFrameCounter = 0;

// You shouldn't do this!
function foo() {
    gFrameCounter = 500;
}
function goo() {
    gFrameCounter = -200;
}
```

will turn into 

```ts
const gFrameCounter: number = 0;

export function getFrameCounter(): number { return gFrameCounter; }
export function setFrameCounter(counter: number) { gFrameCounter = counter; }

//...

function foo() {
    setFrameCounter(500);
}
function goo() {
    setFrameCounter(-200);
}
```

Now you can easily remove & refactor Global variable accessings. 

In Javascript, returning data will pass the reference of the actual object. It leads modifying the returned data without *setter*.

Hence, return values with a new object by *Object.assign({}, value)* is recommended as following.

```ts
const gFrameCounter: number = 0;

export function getFrameCounter(): number { return Object.assign({}, gFrameCounter); }
export function setFrameCounter(counter: number) { gFrameCounter = counter; }
```