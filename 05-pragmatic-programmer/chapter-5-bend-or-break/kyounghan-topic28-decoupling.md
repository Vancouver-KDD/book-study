# Decoupling

- Good design principles will make the code you write easy to change. (Topic 8, The Essence of Good Design)
- Coupling is the enemy of change, because it links together things that must change in parallel.
  - Either you spend time tracking down all the parts that need changing
  - You spend time wondering why things broke when you changed "just one thing" and not the other things to which it was coupled.

| Designing brideges | Designing software |
|--------------------|--------------------|
| hold its shape     | changable          |
| rigid              | flexible           |

- Coupling is transitive
  - if A is coupled to B and C, and B is coupled to M and N, and C to X and Y
  - then A is actually coupled to B, C, M, N, X and Y

> Tip 44 : Decoupled Code is Easier to Change

#### 3 stories to explain the meaning of decouple code
- Train wrecks - chains of method calls
- Globalization - the dangers of static things
- Inheritance - why subclassing is dangerous

#### The symptoms of coupling
- Wacky dependencies between unrelated modules or libraries
- 'Simple' changes to one module that propagate through unrelated modules in the system or break stuff elsewhere in the system.
- Developers who are afraid to change code because they aren't sure what might be affected
- Meetings where everyone has to attend because no one is sure who will be affected by a change

## Train Wrecks
```
public void applyDiscound(customer, order_id, discount) {
    totals = customer
                .orders
                .find(order_id)
                .getTotals();
    totals.grandTotal = totals.grandTotal - discount;
    totals.discount = discount;
}
```
- Traversing five levels of abstraction, from customer to total amount
- Top-level code has to know that
  - a customer object exposes orders
  - the orders have a find method that takes an order id and returns an order
  - the order object has a total object which has getters and setters for grand totals and discount
- A lot of implicit knowledge
- Worse, that's a lot of things that cannot change in the future if this code is to continue to work.

### Scenario
- The business decides that no order can have a discount of more than 40%
- Where would we put the code that enforces that rule?

#### Answer #1
- fix applyDiscount function
- That's certainly part of the answer
- But it is not the whole answer
- Any piece of code, anywhere, could set fields in the total object, and if the maintainer of that code didn't get the memo, it wouldn't be checking against the new policy

> Tip 45 : Tell, Don't Ask

#### Improvement #1
- One way to look at this 
  - To think about responsibilities
  - The *total* objects should be responseible for managing the totals
  - Currently, it isn't : it's really just a container for a bunch of fields that anyone can query and update.

- Tell, Don't Ask(tip 45)
  - Shouldn't make decision based on the internal state of an object and then update that object.
  - It totally destroys the benefits of encapsulation and, spreads the knowledge of the implementation throughout the code.
  - *Do not update the value from outside of the class, just tell what we want not do something based on the value or interal state*
  - First fix is to delegate the discounting to the total object

```
public void applyDiscount(customer, order_id, discount) {
    customer
        .orders
        .find(order_id)
        .getTotals()
        .applyDiscount(discount);
}
```
#### Improvement #2
- Still have issue, a kind of tell-don't-ask(TDA), related with customer object and its order
- Shouldn't fetch its list of order and search them.
- Instead of that, get the order we want directly from the customer
```
public void applyDiscount(customer, order_id, discount) {
    customer
        .findOrder(order_id)
        .getTotals()
        .applyDiscount(discount);
}
```

#### Improvment #3
- Same thing applies to order object and its total
- Why should the outside world have to know that the implementation of an order uses a separate object to store its totals?
  
```
public void applyDiscount(customer, order_id, discount) {
    customer
        .findOrder(order_id)
        .applyDiscount(discount);
}
```

#### applyDiscountToOrder(order_id) ??
- If followed slavishly, it would.
- TDA is not a law of nature
- It's just a pattern to help us recognize problems.
- In this case, we're comfortable exposing the fact that a customer has orders and that we can find one of those orders by asking the customer object for it. 
- In every application there are certain top-level concepts that are universal, like customers and orders. It makes no sense to hide orders totally inside customer object. They have an existence of their own.

### The Law of Demeter (LoD)
- LoD is a set of guidelines writeen in the late '80s by Ian Holland 
- To help developers on the Demeter Project keep their functions cleaner and decoupled.
- LoD says that a method defined in a class C should only call
  - Other instance methods in C
  - Its parameters
  - Methods in objects that it creates, both on the stack and in the heap
  - Global variables 
- Not many used comparing with 20 years ago.
  - Difficult to use in practice
  - A little like having to parse a legal document
  - But the principle is still sound
  - Simpler way - Don't Cahin Method Calls

> Tip 46 : Don't Chain Method Calls

  - Try not to have more than one "." when you access something.
  - And access something also covers cases where you use intermediate variables, as in the following code :

```
# this is pretty poor style
amount = customer.orders.last().totals().amount;

# and so is this...
orders = customer.orders;
last = orders.last();
totals = last.totals();
amount = totals.amount;
```
- Exception to the one-dot rule
  - The rule doesn't apply if the things you're chaining are really, really unlikely to change.
  - If libraries which your application uses are changed, your application should be changed.
  - But Libraries that come with the language are probably pretty stable, so below code is ok
```
people
    .sort_by {|person | person.age}
    .first(10)
    .map{|person | person.name}
```

### Chains and Pipelines
- In topic 20, *Transforming Programming*, composing functions into pipelines is introduced.
- These pipelines transform data, passing it from one function to the next.
- This is not the same as a train wrect of method calls
- This is far less a barrier to changing the code than form introduced by train wrecks.

## The Evils of Globalization
- Globally accessible data is an insidious source of coupling between application components.
- Each piece of global data acts as if every method in your application suddenly gained an additional parameter : after all, that global data is available inside every method

#### Globals couple code
- A change to the implementation of the global potentially affects all the code.
- If your code uses global data, then it becomes difficult to split it out from the rest.
  - It becomes hard doing refactoring and making resuable code
- Writing unit tests for code that uses global data
  - A bunch of setup code is needed to create a global environment just to allow test to run

> Tip 47 : Avoid Global Data

### Global Data Includes Singletons
- Global data & Global variables
- If all you have is a singleton with a bunch of exported instance variables, then it's still just global data. It just has a longer name
- Method is better
  - Config.log_level() or Config.getLogLevel() is better Config.log_level
  - But still have only the one set of configuration data.

### Global Data Includes External Resources
- Any mutuable external resource is global data
- If your application uses a database, datastore, file system, service API, and so on, it risks falling into the globalization trap. 
- The solution is to make sure you always wrap these resources behind code that you control

> Tip 48: If It's Important Enough to Be Global, Wrap It in an API

## Inheritance adds coupling
- The misuse of subclassing, where a class inherits state and behavior from another class, is so important that we discuss it in its own section, Topic 31, Ineritance Tax

## Again, It's All About Change
- Coupled code is hard to change
  - alterations in one place can have secondary effects elsewhere in the code, and often in hard-to-find places that only come to light a month later in production
- Keeping your code shy
  - having it only deal with things it directly knows about


## No Challenge