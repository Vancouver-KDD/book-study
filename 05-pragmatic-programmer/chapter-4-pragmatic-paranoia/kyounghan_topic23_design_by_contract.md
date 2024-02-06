# Design by Contract

- Dealing with computer systems is hard
- Dealing with people is even harder
- Contract can be use to resolve these hard

### contract
- A contract defines rights and responsibilities of one party, as well as those of the other party.
- There is an agreement concerning reprecussions if either party fails to abide by the contract
- Employment contract
  - work hours
  - rules of conduct to folllow
  - salary and other perks
  - Each party meets its obligations and everyone benefit.
- Can we use the this idea and concept to help software modules interact? 
  - Yes

## DBC (Design by Contract)
- Bertrand Meyer(Object-Oriented Software Construction[Mey97]) developed it for the language Eiffel
- Simple yet powerful technique that focuses on documenting the rights and responsibilities of software modules to ensure program correctness
- Documenting and verifying that claim is the heart of Design by Contract
  
> Correct program
>> Do no more and no less than it claims to do


### Function & Method in a software system
- Expectation : Before it starts, the function may have some expectation of the state of the world
- Claim : May be able to to make a statement about the state of the world when it concludes
- Meyer describes these expectations and cliams as follows
    - Preconditions
        - What must be true in order for the routine to be called
        - the routine's requirements
        - A routine should never get called when its preconditions would be violated
        - It is the caller's repsonsibility to pass good data
    - Postconditions
        - What the routine is guaranteed to do
        - the state of the world when the routine is done
        - It will conclude : infinite loops aren't allowed
    - Class invariants
        - A class ensures that this condition is always true from the perspective of a caller.
        - During internal processing of a routine, the invariant may not hold, but by the time the routine exists and control returns to the caller, the invariant must be true

- If all the routine's preconditions are met by the caller, the routine shall guarantee that all postconditions and invariants will be true when it completes
- If either party fail to live up to the terms of the contract, then remedy is invoked
    - Exception is raised
    - The program terminates


### Some language have better support for these concept than others
  - Clojure
    - Support pre- and post-conditions as well as the more comprehensive instrumentation provided by specs
    - Example of a banking function to make a deposit using simple pre- and post-conditions:

    ``` 
    (defn accept-deposit [account-id amount]
        {:pre[ (>amount 0.00)
            (account-open? account-id)] 
        :post [(contains? (account-transactions account-id) %)]} 
        "Accept a depoit and return the new transaction id"
        ;; Some other processing goes here..
        ;; Return the newly created transaction:

        (create-transaction account-id: deposit amount)) 
    ```

    - Two precondition for accept-deposit function
      - First : the amount is greater than zero
      - Second : the account is open and valid, as determined by some function named account-open?
    - Postcondition
      - the function guarantees that the new transcation can be found among the transactions for this account

    - If accept-deposit is called with a positive amount for the deposit and a valid account, it will proceed to create a transaction of the appropriate type and do whatever other processing it does.
  - Exceptional case
    - pass negative amount for the deposit 
    ```
    Exception in thread "main"...
    Caused by:java.lang.AssertionError: Assert failed: (>amount 0.0)
    ```

    - account is not open or not valid
    ```
    Exception in thread "main"...
    Caused by:java.lang.AssertionError: Assert failed: (account-open? account-id)
    ```

### Other language which support features
- while not DBC-specific, can still be used to good effect
- Elixir 
    - uses guard clauses to dispatch function 

    ```
    defmodule Deposits do
    def accept_deposit(account_id, amount) when (amount > 100000) do
        # Call the manager!
    end
    def accept_deposit(account_id, amount) when (amount > 10000) do
        # Extra Federal requirements for reporting
        # Some processing...
    end
    def accept_deposit(account_id, amount) when (amount > 0) do
        # Some processing...
    end
    end
    
    ```
    - calling accept_deposit with a large enough amount may trigger addiontal steps and processing.
    - Exceptional case
    - try to call it with an amount less than or equal to zero
    ```
        ** (FunctionClauseError) no function clause matching in
        Deposits.accept_deposit/2
    ```

    - This is a better approach than simply checking your inputs
    - Simply can not call function if your arguments are out of range

> Tip 37 : Design with Contracts

### "Lazy" code
- In topic 10, Orthogonality, writing "shy" code was recommended.
- Here, the emphasis is on "lazy" code
    - Be strict in what you will accept before you begin
    - Promise as little as possible in return
- In any programming language, whether it's functional, object-oriented, or procedural, DBC forces you to think 

### DBC and Test-Driven Development
Is Design by Contract needed in a world where developers practice unit testing, test-driven development(TDD), property-based testing, or defensive programming? -> Yes

- DBC and testing are different approaches to the broader topic of program correctness.
- They both have value and both have uses in different situation.
- DBC offers several advantages over specific testing approaches
  - DBC doesn't require any steup or mocking
  - DBC defines the parameters for success or failure in all cases, wheras testing can only target one specific case at a time
  - TDD and other testing happends only at "test time" within the build cycle. But DBC and assertions are forever:during design, development, deployment, and maintenance

- TDD is a great technique, but as with many techniques, but concentrate on the "happy path", and not the real world full of bad data, bad actors, bad versions, and bad sepcifications


### Class Invariants and Functional Languages
- Eiffel is an object-oriented language -> named as "class invariant"
- More general idea
- Really refers to is "state"
- In a functional language, typically state is passed to functions and received as a result

## IMPLEMENTING DBC
- Simply enumerating below before writing code is huge leap forward in writing better software
  - what the input domain range is
  - what the boundary conditions are
  - what the routine promises to deliver
  - what ist doesn't promise to deliver 

- There are languages that do not support DBC
  - DBC is design technique
  - It's possible to put the contract in the code even withough automatic checking
    - Comments
    - unit tests

### Assertions
- It's possible to emulate the contract partially in some language by using assertions
  - Runtime checks of logical conditions
- Why partially?
  - In object-oriented languages there probably is no support for progagating assertions down an inheritance hierarchy.
  - Overrided base class method that has a contract, the assertions that implement that contract will not be called correctly 
  - Must call the class invariant (base class invariants) manually before exit every method.
  - Basic problem is that the contract is not automatically enforced.

## DBC AND CRASHING EARLY
- DBC fits in nicely with concept of crashing early
- By using assert or DBC mechanism to validate the preconditions, postconditions, and invariants, you can crash early and report more accurate information about the problem.

### Example : sqrt method 
- Needs a DBC precondition that restricts the domain to positive numbers
- DBC support language, if negative parameter is passed, error will raise
  - sqrt_arg_must_be_positive
  - with stack trace
- Java, C and C++ will return NaN when negative parameter is passed
  - Unexpected result might be came
- It's much easier to find and diagnose the problem by crashing early , at the site of the problem

## SEMANTIC INVARIANTS
### Example : debit card transaction switch
- A major requirement : user of a debit card should never have the same transaction applied to their account twice
  - No : processing a duplicate transaction
  - Better : error should be on the side of not porcessing a transaction
- This simple law, driven directly from the requirements, proved to be very helpful in sorting out complex error recovery scenarios
- Do not confuse the requirment with some policies that might be changable
- Make a requirement clear and unambiguouse  

## DYNAMIC CONTRACTS AND AGENTS


## Challenges
- Point to ponder : If DBC is so powerful, why isn't it used more widely?
- It it hard to come up with the contract?
- Does it make you think about issues you'd rather ignore for now?
- DOes it force you to THINK!?
- Clearly, this is a dangerous tool!