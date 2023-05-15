### CH. 6 - Process of remove duplication

Refactoring the `Franc` object:

Create a superclass `Money` 

- subclass: `Dollar` and `Franc`
- method `equals`:  will belong to superclass `Money`
- variable `amount` : `private` → `protected` so that subclass can see it.  **define see?