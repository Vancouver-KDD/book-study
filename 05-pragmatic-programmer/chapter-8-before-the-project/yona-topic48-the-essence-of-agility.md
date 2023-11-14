# The Essence of Agility

* Agile: albe to move quickly and easily.
* It's how you do something.
* Agility is your style, not you.

> Tip 83:  Agile Is Not a Noun; Agile is How You Do Things

* Another side of agility: teams and companies are often eager for off-the-shelf solutions: Agile-in-a-Box.
* Many people may have lost sight of the true meaning of agility.

## Manifesto for Agile Software Development
* __Individuals and Interactions__ over processes and tools
* __Working Software__ over comprehensive documentation
* __Customer collaboration__ over contract negotiation
* __Responding to change__ over following a plan

* While there is value in the items on the right, we value the items on the left more.

## There Can Never be an Agile Process

* Agility is all about responding to change, responding to unknowns you encounter after you set out. 
* There is no single plan you can follow when you develop software. They're all about gathering and responding to feedback.

* The values don't tell you what to do. THey tell you what to look for when you decide for yourself what to do.

* Decisions are contextual: they depend on who you are, the nature of your team, your application, you tooling, your company, your customer, and the outside world. 

## So What Do We Do?

* No one can tell you _what_ to do.
* The manifesto suggests that you do this by gathering and acting on feedback.

* Recipe of working in an agile way:
    1. Work out where you are
    2. Make the smallest meaningful step towards where you want to be.
    3. Evaluate where you end up, and fix anything you broke.

* Repeat these until done.
* Sometimes even the most trivial-seeming decision becomes important when you gather feedback.
    * My code needs to get the account owner
    ```
    let user = accountOwner(accountID);
    ```

    * user is a useless name. Make it owner.
    ```
    let owner = accountOwner(accountID);
    ```

    * Looks redundant. I only need email address.
    ```
    let email = emailOfAccountOwner(accountID);
    ```

    * Even applying the feedback loop at a really low level improved the design of the overall system - reduced coupling between this code and the code that deals with accounts.

* Feedback loop also applies at the highest level of a project.
    * Work on a client's requirements, figure out the best solution.
* Feedback loop also applies outside the scope of a project.
    * Review their process and how well it worked.
    * A team that doesn't continuously experiement with their process is not an agile team.

## And This Drives Design
* A good design produces something that's easier to change than a bad design.
* Agility: to make our feedback loop efficient, the fix has to be as pinless as possible. If not, it's easy to shrug it off and leave it unfixed.

## Challenges
The simple feedback loop isn't just for software. Think of other decisions you've made recently. Could any of them have been improved by thinking about how you might be able to undo them if things didn't take you in the direction you were going? Can you think of ways you can improve what you do by gathering and acting on feedback?