# Orthogonality

- critical concept if you want to produce systems that are easy to design, build, test and extend.
- Rarely taught directly
- Once learn, there would be immediate improvement in the quality of systems

## What is orthogonality?
- Orthgonality is a term borrowed from geometry

#### Geometry
- Two lines are orthogonal if they meet at right angles.
#### In Vector terms
- The two lines are independent.
#### In Computing
- A kind of independence or decoupling.
- Two or more things are orthogonal if changes in one do not affect any of the others.
- In well designed system, the database code will be orthogonal to the UI. These two do not affect each other. 

### A Nonorthogonal System
- Short story about helicopter
- Helicopter have four basic controls
    - cyclic : Move to direction
    - collective pitch lever : lift
    - throttle
    - two foot pedals : vary the amount of tail rotor thrust
- Each of change of controls affect all of the other controls again.
- Helicopter controls are decidedly not orthgonal


## Benefits of orthogonality
- Nonorthogonal systems are inherently more complex to change and control
> Tip 17 Eliminate Effects Between Unrelated Things

- Design the self-contained component
    - independent
    - single, well-defined purpose
- When components are isolated from on another, you can change one without having to worry about the rest
- As long as don't change that component's external interfaces, 
you can be confident that you won't cause problems that ripple thought the entire system.


### Gain productivity
- Time reduce 
    - Changes are localized, so development time and testing time are reduced.
    - Easier to write relatively small, self-contained components than a single large block of code.
- Reuse
    - Specific, well-defined component can be combined with new components in ways that 
    were not envisioned by their original implementors
    - The more loosely coupled system, the easier they are to reconfigure and reengineer.
- gain in productivity
    - fairly subtle gain in productivity when combine components
    - Less overlap

### Reduce risk
- Diseased sections of code are isolated.
- Less fragile
    - Make samll changes and fixes to a particular area
- Better tested
    - Easy to design and run tests 
- Don't need to tied with components
    - Particular vendor / product / platform 

# How to apply the principle of orthogonality 

## Design
- Modular / Component-base / Layered
- Layer
    - Abstractions provided by the layers below it
    - Have great flexibility in changing underlying implementations withouth affecting code
- Easy test for orthogonal design
    - If I dramatically change the requirements behind a particular function, how many 
    modules are affected?
    - In an orthogoanl system, the answer should be "one"
- Example
    - telephone number as a customer identifier
    - What happens when the phone company reassigns area code?
    - Postal codes, Social Security Numbers or government IDs, email addresses, and domain
    are all external identifiers that you have no control over 
    and could change at any time for any reason
    - Don't rely on the properties of things you can't control

## Toolkits and libraries
- Be careful to preserve the orthogonality of system as introduce third-party toolkits and libraries
- If an object persistence scheme is transparent, then it's orthogonal.
- If it requires you to create or access objects in a special way, then it's not
- Keeping such details isolated from your code
- EJB : Decorator Pattern - adding functionality to things without changing them.

## Coding
### Keep your code decoupled
- Write shy code
    - modules that don't reveal anything unnecessary to other modules and that don't rely on 
    other modules' implementations.

### Avoid global data
- Every time your code references global data, it ties itself into the other components that share that data.
- In general, your code is easier to understand and maintain if you explicitly pass any required context into your module.

### Avoid similar functions
- Duplicate code is a symption of structual problems
- Have a look at the Strategy pattern in Design Patterns for a better implementations.

## Testing
- An orthogonally desinged and implemented system is easier to test.
- Formalized and limited interfactions between the system's components make possible to test at individual module level.
- For unit test , how many modules are needed to import?
- Fixing Bug
    - Do you change just one module, or care the changes scattered throughout the entire system?

## Documentation
- Perhaps surprisingly, orthogonality also applies to documentation.
- With truly orthogonal documentation, the appearance can be changed dramatically without changing the content.

## Living with Orthogonality
- Orthogonality is closely related to the DRY principle.

# Challenges
- Consider the difference between tools : GUI vs CLI utilities
    - Which set is more orghogonal and why?
    - Which is easier to use for exactly the purpose for which is was intended?
    - Which set is easier to combine with other tools to meet new challenges?
    - Which set is easier to learn?
- Multiple inheritance 
    - C++ : multiple inheritance
    - Java : multiple interfaces
    - Ruby : mixins
    - What impact does using these facilities have on orthogonality?
    - Is there a difference in impact between using multiple inheritance and multiple interfaces?
    - Is ther a difference between using delegation and using inheritance?
