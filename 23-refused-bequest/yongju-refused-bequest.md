# Refused Bequest

Refused Bequest is a smell when child classes use only some of the methods and properties inherited from its parents. In this case, the hierarchy is likely to be wrong. We can fix this problems by applying these below refactoring methods.

##### Push Down Method
##### Push Down Field
##### Replace Subclass with Delegate
##### Replace Superclass with Delegate

We already repeatedly touched all these four refactoring methods through the previous code smell and its refactoring. These four methods will let classes have high coherence & cohesion and loosen coupling. That is, the modules in the program have its independency. It leads a easier maintenance for programmers.