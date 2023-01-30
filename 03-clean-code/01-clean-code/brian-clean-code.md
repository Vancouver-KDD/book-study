# Clean code

## What is Clean Code?
### Bjarne Stroustrup, inventor of C++ and author of The C++ Programming Language
I like my code to be **elegant** and **efficient**. The logic should be straightforward to make it hard for bugs to hide, the dependencies minimal to ease maintenance, error handling complete according to an articulated strategy, and performance close to optimal so as not to **tempt** people to make the code messy with unprincipled optimizations. Clean code does one thing well.
- elegant = clean code is pleasing to read
- tempt = Bad code temps the mess to grow

### Grady Booch, author of Object Oriented Analysis and Design with Applications
Clean code is simple and direct. Clean code reads like well-written prose. Clean code never obscures the designer’s intent but rather is full of **crisp abstractions** and straightforward lines of control.
- crisp <-> concrete

### “Big” Dave Thomas, founder of OTI, godfather of the Eclipse strategy
Clean code can be read, and enhanced by a developer other than its original author. It has unit and acceptance tests. It has meaningful names. It provides one way rather than many ways for doing one thing. It has **minimal** dependencies, which are explicitly defined, and provides a clear and **minimal** API. Code should be **literate** since depending on the language, not all necessary information can be expressed clearly in code alone.
- easy to change = TDD
- Smaller is better
- make it readable by humans

### Michael Feathers, author of Working Effectively with Legacy Code
I could list all of the qualities that I notice in clean code, but there is one overarching quality that leads to all of them. Clean code always looks like it was written by someone who cares. There is nothing obvious that you can do to make it better. All of those things were thought about by the code’s author, and if you try to imagine improvements, you’re led back to where you are, sitting in appreciation of the code someone left for you—code left by someone who cares deeply about the craft.

### Ron Jeffries, author of Extreme Programming Installed and Extreme Programming Adventures in C#
- Runs all the tests;
- Contains no duplication;
- Expresses all the design ideas that are in the system;
- Minimizes the number of entities such as classes, methods, functions, and the like.

= No duplication, one thing, expressiveness, tiny abstractions

### Ward Cunningham, inventor of Wiki, inventor of Fit, coinventor of eXtreme Programming. Motive force behind Design Patterns. Smalltalk and OO thought leader. The godfather of all those who care about code.
You know you are working on clean code when each routine you read turns out to be pretty much what you expected. You can call it beautiful code when the code also makes it look like the language was made for the problem.

## We are authors
- Authors are responsible for communicating well with their readers. 
- Reading VS Writing = 10 : 1

## The boy scout rule 
- The boy scout rule = Leave the campground cleaner than you found it.
- The programmer rule = The code has to be kept clean over time

