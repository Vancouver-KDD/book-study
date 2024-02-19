# Actors and Processes
Actor and processes offer interesting ways of implementing concurrency without the burden of synchronizing access the shared memory

### Key concepts
#### Actor
- An independent virtual processor with its own local state(private)
- Each actor has a mailbox
- When a message appears in the mailbox and actor is idle, it kicks into lef and process the message
- When it finished processing it process another message in the mailbox, or, if the mailbox is empty, it goes back to sleep

#### Process
- Typically a more general-purpose virtual processor
- Often implemented by the operating system to facilitate concurrency.
- Processes can be constrained (by convention) to behave like actors, and that's the type of process we mean here.

## Actors can only be concurrent

### Addtional information for actors
- There's no single thing that's in control
- The only state in the system is held in messages and in the local state of each actor
- All messages are one way
- An actor processes each message to completion, and only processes one message at a time

### Feature of Actor
- Actor execute concurrently, asynchronously, and share nothing
- If you had enough physical processors
  - You could run an actor on each
- If you have a single processor
  - Some runtime can handle the swtiching of ocntext between them

> Tip 59: Use Actors For Concurrency Without Shared State

## A Simple Actor (Diner example using actors)

### Three actor
  - The customers
  - The watier
  - The pie case

### Messages
- Trigger message - to customer, they are hungry
- in response, they'll ask the watier for pie
- The watier will ask the pie case to get some pie to the customer
- If the pie case has a slice available, it will send it to customer and also notify the watier to add it to the bill
- If there is no pie, the case tells the watier, and the watier apologizes to the customer

### Implementation
- JavaScript using Nact library
- Simple Wrapper with
  - key : message types that it receives
  - values : function  to run when that particular message is received

### Customer
- The customer can receive three messages
  - You're hungry (sent by the external context)
  - There's pie on the table (sent by the pie case)
  - Sorry, ther's no pie(sent by the watier)

```
const customerActor = {
    'hungry for pie': (msg, ctx, state) => {
        return dispatch(state.waiter,
            { type: "order", customer: ctx.self, wants: 'pie' })
    },

    'put on table': (msg, ctx, _state) =>
        console.log(`${ctx.self.name} sees "${msg.food}" appear on the table`),

    'no pie left': (_msg, ctx, _state) =>
        console.log(`${ctx.self.name} sulks…`)
}
```
- When receive a "hungry for pie", send a message off to the waiter


### Waiter
```
const waiterActor = {
    "order": (msg, ctx, state) => {
    
        if (msg.wants == "pie") {
            dispatch(state.pieCase,
                { type: "get slice", customer: msg.customer, waiter: ctx.self })
        }
        else {
            console.dir(`Don't know how to order ${msg.wants}`);
        }
    },

    "add to order": (msg, ctx) =>
        console.log(`Waiter adds ${msg.food} to ${msg.customer.name}'s order`
    ),

    "error": (msg, ctx) => {
        dispatch(msg.customer, { type: 'no pie left', msg: msg.msg });
            console.log(`\nThe waiter apologizes to ${msg.customer.name}:
        ${msg.msg}`)
    }
};
```
- When it receives the 'order' message from the customer, it checks to see if the request if for pie. 
  - If so, it send a request to the pie case, passing the reference both to itself and the customer

### Pie case
- Pie case has state
  - an array of all the slices of pie it hold
- When it receives a 'get slice' message from the waiter, it see if it has any slice left.
  - If it does, it passes the slice to the customer, tells the waiter to update the order and finally returns an updated state
```
const pieCaseActor = {
    'get slice': (msg, context, state) => {
        if (state.slices.length == 0) {
            dispatch(msg.waiter,
                { type: 'error', msg: "no pie left", customer: msg.customer })
            return state
        }
        else {
            var slice = state.slices.shift() + " pie slice";
            dispatch(msg.customer,
                { type: 'put on table', food: slice });
            dispatch(msg.waiter,
                { type: 'add to order', food: slice, customer: msg.customer });
            return state;
        }
    }
}
```

### Manual Setting for initial state
- The pie case get the initial list of pie slices it contains
- Waiter get a reference to the pie case
- Customers get a reference to the waiter

```
const actorSystem = start();

let pieCase = start_actor(
    actorSystem,
    'pie-case',
    pieCaseActor,
    { slices: ["apple", "peach", "cherry"] });

let waiter = start_actor(
    actorSystem,
    'waiter',
    waiterActor,
    { pieCase: pieCase });

let c1 = start_actor(actorSystem, 'customer1', customerActor, { waiter: waiter });
let c2 = start_actor(actorSystem, 'customer2', customerActor, { waiter: waiter });
```

### Final Steps to start
- Our customer are greedy
  - Customer 1 asks for three slices of pie
  - Customer 2 asks for two

```
dispatch(c1, { type: 'hungry for pie', waiter: waiter });
dispatch(c2, { type: 'hungry for pie', waiter: waiter });
dispatch(c1, { type: 'hungry for pie', waiter: waiter });
dispatch(c2, { type: 'hungry for pie', waiter: waiter });
dispatch(c1, { type: 'hungry for pie', waiter: waiter });
sleep(500)
    .then(() => {
        stop(actorSystem);
    })
```

### Result
- Actors communication
```
$ node index.js

customer1 sees "apple pie slice" appear on the table
customer2 sees "peach pie slice" appear on the table
Waiter adds apple pie slice to customer1's order
Waiter adds peach pie slice to customer2's order
customer1 sees "cherry pie slice" appear on the table
Waiter adds cherry pie slice to customer1's order

The waiter apologizes to customer1: no pie left
customer1 sulks…

The waiter apologizes to customer2: no pie left
customer2 sulks…
```

## No Explicit Concurrency
- In the actor model, there's no needed to write any code to handle concurrency as there is no shared state
- There's also no logic to code in explicit end-to-end "do this, do that" logic
- Actors work it out for themselves based on the message they receive
- This set of componenets work equally well on a single processor, on multiple cores or on multiple networked machines.

## Erlang sets the stage
- The Erlang language and runtime are great examples of an actor implementation
- Lightweight process communicate by sending messages.
- Each process isloated from the others, so there is no sharing of state.

## Challenges
- Do you currently have code that uses mutual exclusion to protect shared data. Why not try a prototype of the same code written using actors?
- The actor code for the diner only supports ordering slices of pie. Extend it to let customers order pie a la mode, with separate agents managing the pie slice and the scoops of ice cream. Arrange things so that it handles the situation where one or the others runs out.