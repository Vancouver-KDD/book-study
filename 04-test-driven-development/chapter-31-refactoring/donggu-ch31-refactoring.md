# Chapter 31. Refactoring

## Reconcile Differences
- Q: How do you unify two similar looking pieces of code?
- A: Gradually bring them closer. Unify them only when they are absolutely identical.

## Isolate Change
- Q: How do you change one part of a multi-part method or object?
- A: First, isolate the part that has to change.
- Some possible ways to isolate change are Extract Method (the most common), Extract Object, and Method Object.

## Migrate Data
- Q: How do you move from one representation?
- A: Temporarily duplicate the data.

## Extract Method
- Q: How do you make a long, complicated method easier to read?
- A: Turn a small part of it into a separate method and call the new method.

1. Find a region of the method that would make sense as its own method. Bodies of loop, whole loops, and branches of conditionals are common candidates for extraction.
2. Make sure that there are no assignments to temporary variables declared outside the scope of the region to be extracted.
3. Copy the code from the old method to the new method. Compile it.
4. For each temporary variable or parameter of the original method used in the new method,
add a parameter to the new method.
5. Call the new method from the original method.

## Inline Method
- Q: How do you simplify control flows that have become too twisted or scattered?
- A: Replace a method invocation with the method itself.

1. Copy the method.
2. Paste the method over the method invocation.
3. Replace all formal parameters with actual parameters. If, for example, you pass reader.getNext() (an expression causing side effects), then be careful to assign it to a local variable.

## Extract Interface
- Q: How do you introduce a second implementation of operations in Java?
- A: Create an interface containing the shared operations.

1. Declare an interface. Sometimes the name of the existing class should be the name of the interface, in which case you should first rename the class.
2. Have the existing class implement the interface.
3. Add the necessary methods to the interface, expanding the visibility of the methods in the
class if necessary.
4. Change type declarations from the class to the interface where possible.

## Move Method
- Q: How do you move a method to the place where it belongs?
- A: Add it to the class where it belongs, then invoke it.

1. Copy the method.
2. Paste the method, suitably named, into the target class. Compile it.
3. If the original object is referenced in the method, then add a parameter to pass the original object. If variables of the original object are referenced, then pass them as parameters. If variables of the original object are set, then you should give up.
4. Replace the body of the original method with an invocation of the new method.

## Method Object
- Q: How do you represent a complicated method that requires several parameters and local variables?
- A: Make an object out of the method.

1. Create an object with the same parameters as the method.
2. Make the local variables also instance variables of the object.
3. Create one method called run(), whose body is the same as the body of the original method.
4. In the original method, create a new object and invoke run().

## Add Parameter
- Q: How do you add a parameter to a method?

1. If the method is in an interface, add the parameter to the interface first.
2. Add the parameter.
3. Use the compiler errors to tell you what calling code you need to change.

## Method Parameter to Constructor Parameter
- Q: How do you move a parameter from a method or methods to the constructor?

1. Add a parameter to the constructor.
2. Add an instance variable with the same name as the parameter.
3. Set the variable in the constructor.
4. One by one, convert references to "parameter" to "this.parameter".
5. When no more references exist to the parameter, delete the parameter from the method and all caller.
6. Remove the now-superfluous "this." from references.
7. Rename the variable correctly.
