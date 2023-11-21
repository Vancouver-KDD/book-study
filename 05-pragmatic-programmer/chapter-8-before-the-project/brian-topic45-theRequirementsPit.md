# The Requirements Pit
- Requirements rarely lie on the surface. 
- Normally, they’re buried deep beneath layers of assumptions, misconceptions, and politics. 
- Even worse, often they don’t really exist at all.

> Tip 75 - No One Knows Exactly What They Want

## THE REQUIREMENTS MYTH
- it was actually possible to understand the whole problem before you started.
- But that is not the real world. The real world is messy, conflicted, and unknown.

> Tip 76 - Programmers Help People Understand What They Want

## PROGRAMMING AS THERAPY 
- In our experience, this initial statement of need is not an absolute requirement. 
- The client may not realize this, but it is really an invitation to explore.


- Example: Shipping should be free on all orders costing $50 or more.
- Possible Questions
  1. Does the $50 include tax? 
  2. Does the $50 include current shipping charges? 
  3. Does the $50 have to be for paper books, or can the order also include ebooks? What kind of shipping is offered? Priority? Ground? 
  4. What about international orders? 
  5. How often will the $50 limit change in the future?


## REQUIREMENTS ARE A PROCESS
- During that exploration, you are likely to come up with more feedback as the client plays with different solutions.
- This is the reality of all requirements gathering:
> Tip 77 - Requirements Are Learned in a Feedback Loop

- Your job is to help the client understand the consequences of their stated requirements. 
- You do that by generating feedback, and letting them use that feedback to refine their thinking.

> Ask “is this what you meant?"

- Even at the end of a project we’re still interpreting what our client wants. 
- In fact, by that point we’re likely to have more clients: 
  - the QA people, operations, marketing, and maybe even test groups of customers.

## WALK IN YOUR CLIENT’S SHOES
- Are you writing a system for the help desk?
    - Spend a couple of days monitoring the phones with an experienced support person.
- Are you automating a manual stock control system?
    - Work in the warehouse for a week.

> Tip 78 - Work with a User to Think Like a User

## REQUIREMENTS VS. POLICY
> Tip 79 - Policy Is Metadata

## REQUIREMENTS VS. REALITY 
- The interface to the new music mixing board didn’t leverage off those abilities. 
- Instead, it forced its users to type on a keyboard or click a mouse.

## DOCUMENTING REQUIREMENTS 
- We believe that the best requirements documentation, perhaps the only requirements documentation, is working code.
- They are not something that you give to a client to sign off on. 
  - Instead, they are simply mileposts to help guide the implementation process.

### Requirements Documents Are Not for Clients 
- Perfect documentation is nothing.
  - First, as we’ve discussed, the client doesn’t really know what they want up front.
  - Second, the client never reads them.
- The requirements document is written for developers, and contains information and subtleties that are sometimes incomprehensible and frequently boring to the client.

### Requirements Documents Are for Planning 
- What form does this take? 
- We favor something that can fit on a real (or virtual) index card. 
- These short descriptions are often called user stories. 
- They describe what a small portion of the application should do from the perspective of a user of that functionality.

## OVERSPECIFICATION
- Another big danger in producing a requirements document is being too specific.
- Requirements are not architecture. Requirements are not design, nor are they the user interface. 
  - Requirements are need.

## JUST ONE MORE WAFER-THIN MINT...
- "Here is one last little bit of information which I am trying to squeeze in; I hope it's not too much...."

- Many project failures are blamed on an increase in scope
- The answer (again) is feedback.
-  the client will experience first-hand the impact of “just one more feature.” 
  - They’ll see another story card go up on the board, 
    - and they’ll get to help choose another card to move into the next iteration to make room.

## MAINTAIN A GLOSSARY
- Create and maintain a project glossary—one place that defines all the specific terms and vocabulary used in a project.
> Tip 80 - Use a Project Glossary

## CHALLENGES
1. Can you use the software you are writing? Is it possible to have a good feel for requirements without being able to use the software yourself?
2. Pick a non-computer-related problem you currently need to solve. Generate requirements for a noncomputer solution.
