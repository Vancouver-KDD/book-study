# Naming Things

> The beginining of wisdom is to call things by their proper name.
> - Confucius

- What’s in a name? -> Everything!
- “what is my motivation to create this?”


- It turns out that the brain can read and understand words really fast: faster than many other activities.
![white-gray-black.jpg](images%2Fwhite-gray-black.jpg)

```
// user?? -> customer or buyer
let user = authenticate(credentials) 
```

```java
public void deductPercent(double amount)
// =>
public void applyDiscount(Percentage discount)
```

```
Fib.fib(n)
// =>
Fib.of(n) // or
Fib.nth(n)
```

## HONOR THE CULTURE 
- In the C programming language, i, j, and k are traditionally used as loop increment variables, 
- s is used for a character string, and so on.
- => We think they’re wrong. Sort of.


- Some language communities prefer camelCase, with embedded capital letters, 
- while others prefer snake_case with embedded underscores to separate words.
- =>  Honor the local culture.

## CONSISTENCY 
- It’s important that everyone on the team knows what these words mean, and that they use them consistently.
  - Pair program
  - project glossary, listing the terms that have special meaning to the team
-  you’ll be able to use the jargon as a shorthand, expressing a lot of meaning accurately and concisely. 
- (This is exactly what a pattern language is.)

## RENAMING IS EVEN HARDER
- Misleading names
> Tip 74 - Name well; Rename When Needed

## CHALLENGES
- When you find a function or method with an overly generic name, try and rename it to express all the things it really does. Now it’s an easier target for refactoring.
- In our examples, we suggested using more specific names such as buyer instead of the more traditional and generic user. What other names do you habitually use that could be better?
- Are the names in your system congruent with user terms from the domain? If not, why? Does this cause a Stroop-effect style cognitive dissonance for the team? 
- Are names in your system hard to change? What can you do to fix that particular broken window?