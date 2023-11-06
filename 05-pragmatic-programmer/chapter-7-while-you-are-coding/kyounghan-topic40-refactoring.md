# Refactoring

#### Evolution of Program
- It will become necessary to rethink earlier decisions and rework portions of the code
- It is perfectly natural
- Code needs to evolve

#### Building construction
- Unfortunately, the most common metaphor for software development is bulding construction.
- Object-Oriented Software Construction[Mey97] - Bertrand Meyer's
  - Software Construction
- Software Construction column for IEEE Software in the early 2000s
- Construction means follow steps
    1. Anarchitect draws up blueprints
    2. Contractors dig the foundation, build the superstructure, wire and plumb, and apply finishing touches
    3. The tenants move in and live happily ever after, calling building maintenance to fix any problems

#### Software is different compared with consctruction
- Software doesn't quite work like construction
- Software is more like gardening, more organic than concrete
  - Plant many things in a garden according to an initial plan and conditions
  - Move platings relative to each other to take advantage of the interplay of light and shadow, wind and rain
  - Overgrown plants get split or pruned
  - Colors that clash may get moved to more aesthetically pleasing location
  - Pull weeds
  - Fertilize planting that are in need of some extra help.
  - Constantly monitor the health of the garden.
  - Make adjustments as needed

#### Comfortable with the metaphor of building construction
- Business people are like the metaphor of building construction
- More scientific
- Repeatable
- Rigid reporting hierarchy for management

#### Gardening Metaphor
- The Gardening metaphor is much closer to the realities of software development
- Split into two depends on the size of software
- Need to be weeded or pruned for softwares which do not work out as planned

#### Refactoring's feature
- A subset of Restructuring(Rewriting, reworking and re-architecting)
- Defined by Martin Fowler
- Disciplined technique for restructuring an existing body of code, altering its internal structure without changing its external behavior.
  - The activity is disciplined, not a free-for-all
  - External behavior does not change: this is not the time to add features

#### Refactoring is not intended to be
- Special 
- high-ceremony
- once-in-a-while activity, like plowing under the whole garden in order to replant

#### Refactoring is 
- Day-to-day activity
- taking low-risk small steps
- More like weeding and raking
- Wholesale rewrite the codebase, its'a targeted, precision approach to help keep the code easy to change

In order to guarantee that the external behavior hasn't changed, you need good, automated unit testing that validates the behavior of the code.

## When Should You Refactor?
- You refactor when you've learned something
- When you understand something better than you did last year, yesterday, or even just ten minutes ago
- You've com across a stumbling block because the code doesn't quite fit anymore
- You notice two things that should really be merged

#### Duplication
- You've discoved a violation of the DRY principle.

#### Nonorthogonal design
- You've discovered something that could be made more orthogonal

#### Outdated knowledge
- Things change, requirements drift, and your knowledge of the problem increases.
- Code needs to keep up

#### Usage
- As the system gets used by real people under real circumstances, you realize some features are now more important than previously thought, and 'must have' featurs perhpas weren't.


#### Performance
- You need to move functionality from one area of the system to another to imporve performance

#### The Tests Pass
- Refactoring should be a small scale activity, backed up by good tests.
- When add a small amount of code and it is passed the test, it is good opportunity to dive in and tidy up

Refactoring is really an exercise in pain management. 

### Real-World Complications
- It might hard to explain the needs of refactoring to teammates or client
- Time pressure is often used as an excuse for not refactoring
- It better to start when problem is small rather than it grows and spreads
- It will be more expensive and more dangerous if refactoring is done later

> Tip 65 : Refactor Early, Refactor Often
- Collateral damage in code can be just as deadly over time, Topic3 : Software Entropy
- Refactoring is easier to do while the issues are small, as an ongoing activiy while coding
- If the level of disruption is necessary, then you might well not be able to do it immediately. Instead make sure that is gets placed on the schedule. 

## How Do You Refactor?
- Refactoring is an activity that needs to be undertaken slowly, deliberately, and carefully.
- Martin Fowler offers the simple tips on how to refactor without doing more harm tnat good
  1. Don't try to refactor and add functionality at the same time
  2. Make sure you have good tests before you being refactoring : Run the tests as often as possible. 
  3. Take short, deliberate steps
       - move a field from one class to another
       - Split a method
       - Rename a variable
  - Maintaining good regression tests is the key to refactoring safely.
 

