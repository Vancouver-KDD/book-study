# Don’t Outrun Your Headlights
- It’s tough to make predictions, especially about the future.
- In software development, our “headlights” are similarly limited. 
- We can’t see too far ahead into the future, and the further off- axis you look, the darker it gets. So Pragmatic Programmers have a firm rule:
## Tip 42 - Take Small Steps—Always
- Always take small, deliberate steps, checking for feedback and adjusting before proceeding. 
- Consider that the rate of feedback is your speed limit. 
- You never take on a step or a task that’s “too big.”


- Feedbacks
1. Results in a REPL provide feedback on your understanding of APIs and algorithms
2. Unit tests provide feedback on your last code change
3. User demo and conversation provide feedback on features and usability


- What's a task that's too big?
  - you can quickly get past educated guess and into wild speculation. 
  - You might find yourself slipping into fortune telling when you have to:
    1. Estimate completion dates months in the future
    2. Plan a design for future maintenance or extendability
    3. Guess user’s future needs
    4. Guess future tech availability


- Instead of wasting effort designing for an uncertain future, you can always fall back on designing your code to be replaceable.
- Making code replaceable will also help with cohesion, coupling, and DRY, leading to a better design overall.

## BLACK SWANS 
- Even though you may feel confident of the future, there’s always the chance of a black swan around the corner.

## Tip 43 - Avoid Fortune-Telling
- Much of the time, tomorrow looks a lot like today. But don’t count on it.