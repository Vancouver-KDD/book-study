# Lazy Element

It is a one of the example of "Overuse".

It has couter-smells of

- Extract Class vs Inline Class
- Extract Function vs Inline Function

But as the book described, there is no answer but the decision by the time goes.

> Sometimes, this reflects a function that was expected to grow and be popular later, but never realized its dreams.

Even though we thought a function or a class will be used very often in scalable environment, after a while we realize it is no longer needed or no longer scaled in any soon. Thus, even though we did a refactoring of "Extraction", now we are putting them back, "Inlining".

## Inline Function & Inline Class

Basically aims the same goal. Remove meaningless (it had meaning before but not anymore)  structural code and inline all the code into where they've been used. This means we are creating a coupling but at this moment of decision making, we certainly know this is redundant anymore.

## Collapse Hierarchy

If a child class is no longer different from a parent class, merge them.

Use *Pull Up & Push Down* Field, *Pull Up & Push Down* Method to merge them into one.
