# Large Class

This has a similar problem as **Long function**.

If a class is a huge fat class, it needs to lose its weight for maintainability.

Usually too many fields indicate Large Class smell since this means the class has lots of responsibilities and is managing multiple states or resources.

Also, if there are specific features get along with together, they can be extracted as a subclass or delegate to be separated out.

## Extract Class & Superclass, Replace Type code with subclasses

These techniques are all related to splitting a fat class into small thin classes.
