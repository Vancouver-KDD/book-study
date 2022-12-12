# Speculative Generality

Similarly to Lazy Element, it is a smell of failure of Dream Big.

Well, we don't know how the structure should end until we encounter the problems. That's why we do planning, UML diagrams, etc. But even though we spend lots of time on planning, the planning might go wrong or we are kinda obsessed with something minor. Once they are revealed as *Speculative Generality*, they should get killed with dignity.

## Collapse Hierarchy, Inline Function, Inline Class

Merge them into one.

## Change Function Declaration

After changing features over time, we might end up removing features or extracting them into several functions. Keep an eye on related parameters because they will remain unused.

## Remove Dead Code

> Speculative Generality can be spotted when the only users of a function or class are test cases.

Test case is not code. If they are the only user because of the possibility of *Future usage*, they should go.