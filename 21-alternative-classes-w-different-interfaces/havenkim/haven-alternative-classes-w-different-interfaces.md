# Alternative Classes with Different interfaces

This smell can be found commonly when integrating Strategy pattern or visitor pattern on top of existing algorithm codes.

It's simply because that there is a chance to have different parameters, call sequence in similar algorithms. Until each subclass gets stabilized, mismatched interface can be found frequently.

## Change Function Declaration, Move Function, Extract Superclass

They all can be used since it is all about matching function interfaces but also tier the logics apart into several functions.
