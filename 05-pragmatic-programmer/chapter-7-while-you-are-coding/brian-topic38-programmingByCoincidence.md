# Programming by Coincidence
- As developers, we also work in minefields.
- We should avoid programming by coincidence—relying on luck and accidental successes—in favor of programming deliberately.

## HOW TO PROGRAM BY COINCIDENCE 
- Fred doesn’t know why the code is failing because he didn’t know why it worked in the first place.
- It seemed to work, given the limited “testing’’ that Fred did, but that was just a coincidence.

### Accidents of Implementation 
- But the author didn’t intend for the routine to work that way—it was never even considered.
- When the routine gets “fixed," your code may break.


- Here it looks like Fred is desperately trying to get something out on the screen using some particular GUI rendering framework
```
paint();
invalidate();
validate();
revalidate();
repaint();
paintImmediately();
```
- But these routines were never designed to be called this way; although they seem to work, that’s really just a coincidence.


- Why should you take the risk of messing with something that's working?
1. It may not really be working—it might just look like it is.
2. The boundary condition you rely on may be just an accident. In different circumstances (a different screen resolution, more CPU cores), it might behave differently. 
3. Undocumented behavior may change with the next release of the library. 
4. Additional and unnecessary calls make your code slower. 
5. Additional calls increase the risk of introducing new bugs of their own.

- For routines you call, rely only on documented behavior. If you can’t, for whatever reason, then document your assumption well.

### Close Enough Isn’t 
- As a result of conflicting time zone interpretations and inconsistencies in Daylight Savings Time policies, results were almost always wrong, but only off by one.
- Without a proper model of time handling, the entire large code base had devolved over time to an untenable mass of +1 and -1 statements.

### Phantom Patterns 
- Human beings are designed to see patterns and causes, even when it’s just a coincidence.
  - For example, Russian leaders always alternate between being bald and hairy: 
    - a bald (or obviously balding) state leader of Russia has succeeded a non- bald (“hairy”) one, and vice versa, for nearly 200 years
- Don’t assume it, prove it.

### Accidents of Context 
- When you copied code from the first answer you found on the net, are you sure your context is the same?

> Tip 62 - Don’t Program by Coincidence

### Implicit Assumptions 
- Coincidences can mislead at all levels—from generating requirements through to testing.
- Don’t assume it, prove it.
- At all levels, people operate with many assumptions in mind but these assumptions are 
  - rarely documented and are often in conflict between different developers.

## HOW TO PROGRAM DELIBERATELY 
1. Always be aware of what you are doing. Fred let things get slowly out of hand, until he ended up boiled, like the frog here.
2. Can you explain the code, in detail, to a more junior programmer? If not, perhaps you are relying on coincidences. 
3. Don’t code in the dark. Build an application you don’t fully grasp, or use a technology you don’t understand, and you’ll likely be bitten by coincidences. If you’re not sure why it works, you won’t know why it fails. 
4. Proceed from a plan, whether that plan is in your head, on the back of a cocktail napkin, or on a whiteboard.
5. Rely only on reliable things. Don’t depend on assumptions. If you can’t tell if something is reliable, assume the worst. 
6. Document your assumptions. Topic 23, Design by Contract, can help clarify your assumptions in your own mind, as well as help communicate them to others. 
7. Don’t just test your code, but test your assumptions as well. Don’t guess; actually try it. Write an assertion to test your assumptions (see Topic 25, Assertive Programming). If your assertion is right, you have improved the documentation in your code. If you discover your assumption is wrong, then count yourself lucky. 
8. Prioritize your effort. Spend time on the important aspects; more than likely, these are the hard parts. If you don’t have fundamentals or infrastructure correct, brilliant bells and whistles will be irrelevant. 
9. Don’t be a slave to history. Don’t let existing code dictate future code. All code can be replaced if it is no longer appropriate. Even within one program, don’t let what you’ve already done constrain what you do next—be ready to refactor (see Topic 40, Refactoring). This decision may impact the project schedule. The assumption is that the impact will be less than the cost of not making the change.

