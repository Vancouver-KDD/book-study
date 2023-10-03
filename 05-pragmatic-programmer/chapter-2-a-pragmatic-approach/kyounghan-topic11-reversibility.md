# Reversibility
## Changes in development
### Definition of reversibility
- The fact that a process, an action or a disease can be changed so that something returns to its original state of situation
- The ability to become the opposite in position, direction, order or character
- The ability to be restored or returned to a previous condition

- Engineers prefer simple, singular solutions to problems
- Management tends to agree with the engineers : singular, easy answers fit
nicely on spreadsheets and project plans.
- But Nothing is forever
- If you go into a project hampered by the myopic notion that there is only one way to do it, you may be in
for an unpleasant surprise.

### Real world
- "We are 85% done coding the project, we can't change now!"

## Reversibility
- Producing flexible, adaptable software.
    - DRY principle, decoupling, external configuration
    - Don't have to make an many critical, irreversible decisions
    - Don't always make the best decision the first time around
    - Requirements, users, and hardware change faster than software developed. 

- Flexibility in the project
    - to use relational database 
    - during performance testing, discover that the database is simply too slow
    - DB another vendor is faster
    - Depends on the abstraction for database, flexibility is different.

- App development
    - Project begins as a brower-based application
    - Marketing decides to move to mobile app.

- This mistakes lies in assuming that any decisions is cast in stone - and int not preparing
for the contigencies that might arise. 
    

## Flexible architecture
Not just coding part, many areas need flexibility - architecture, deployement and vendor integration

### Best Practice server-side architectures
- Big hunk of iron
- Federations fo big iron
- Load-balanced clusters of commodify hardward
- Cloud-based virtual machines running applications
- Cloud-based virtual machines running serviecs
- Containerzied version of the above
- Cloud-supported serverless applications
- And, inevitably, an apparent move back to big hunks of iron for some tasks

### How can you plan for this kind of architectural volatility?
- You can't
- Instead, make it easy to change
- Hide third-party APIs behind your own abstraction layers
- Break your code into components
- No one know what the future may hold.

> Tip 19: Forgo following Fads



## Challenges
- Schrodinger's cat
- Can see what happen only after open the box
- Every decisions results in a different version of the future.
- How many possible futures can your code support?
- Which ones are more likely? 
- How hard will it be to support them when the time comes?

