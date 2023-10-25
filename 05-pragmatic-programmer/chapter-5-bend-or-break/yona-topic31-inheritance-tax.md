# Don't Pay Inheritance Tax!

## Some Background

### Simula
* Suggests inheritance was a way of combining types.
* Continued in languages such as C++ and Java.
    * Simula approach : use _prefix classes_
    * 'link' is a prefix class that adds the functionality of linked lists. (like parent class)

    ```
    link CLASS car;
        ... implementation of car
    link CLASS bicycle;
        ... implementation of bicycle
    ```
* This gives a form of polymorphism: cars and bicycles both implemented the 'link' interface because they both contained the 'link' code.

### Smalltalk
* Allows experiments with "differentail programming" (various ways to accomplish "this is like that except")
* Inheritance is a dynamic organization of behaviours
* Continued in languages such as Ruby and JavaScript

* In general, OO developers use inheritance to :
    * add common functionality from a base class into child classes
    * i.e. class __User__ and class __Product__ are both subclasses of __ActiveRecord::Base__

    * express the relationship between classes
    * i.e. a __Car__ is-a-kind-of __Vehicle__

## Problems Using Inheritance to Share Code
* Inheritance is coupling. API changes break clients of classes.
```

class Vehicle 
    def initialize
        @speed = 0
    end
    def stop 
        @speed = 0
    end
    def move_at(speed) 
        @speed = speed
    end
end

class Car < Vehicle 
    def info
        "I'm car driving at #{@speed}" 
    end
end

    # top-level code
    my_ride = Car.new
    my_ride.move_at(30)
```

## Problems Using Inheritance to Build Types
* Some view inheritance as a way of defining new types with class hierarchies.
* This can lead to added complexity and can make the application more brittle.
* The multiple inheritance issue. Modelling correctly would need multiple inheritance.
    * A __Car__ may be a kind of __Vehicle__, but it can also be a kind of __Asset__, __InsuredItem__, etc.

## The Alternatives are Better
* Interfaces and protocols
* Delegation
* Mixins and traits

* Each of these methods may be better in different circumstances, depending on whether your goal is sharing type information, adding functionality, or sharing methods.

## Interfaces and Protocols
```
public class Car implements Drivable, Locatable {
    // Code for class Car. This code must include
    // the functionality of both Drivable
    // and Locatable
}
```

```
public interface Drivable {
    double getSpeed();
    void stop();
}

public interface Locatable() {
    Coordinate getLocation();
    boolean locationIsValid();
}
```

* class definition of __Car__ will only be valid if it includes all four of these methods.
* We can use interfaces and protocols as types, and any class implements the appropriate interface will become compatible with that type.

```
List<Locatable> items = new ArrayList<>();
items.add(new Car(...)); 
items.add(new Phone(...)); 
items.add(new Car(...));
// ...
```

```
void printLocation(Locatable item) { 
    print(item.getLocation().asString());
}
if (item.locationIsValid()) {
    print(item.getLocation().asString());
}
// ...
items.forEach(printLocation);
```
* We can process the list safely knowing that every item has __getLocation__ and __locationIsValid__.

* Tip 52: Prefer Interfaces to Express Polymorphism
    * Interfaces and protocols give us polymorphism without inheritance.

## Delegation
* many persistence and UI frameworks insist that application components subclass some supplied base class:
```
class Account < PersistenceBaseClass
end
```
* the __Account__ class carries all of the persistence class's API around with it (even if it only needs 2 out of 20)

```
class Account
    def initialize(. . .)
        @repo = Persister.for(self)
    end

    def save
        @repo.save()
    end
end
```
* This exposes non of the framework API to the clients of our __Account__ class.
* We're no longer constrained by the API of the framework we're using, we're free to create the API we need.

* A step further: __Account__ does not need to know how to persist itself. It's job is to know and enforce the account business rules.

```
class Account
    # nothing but account stuff
end

class AccountRecord
    #Wraps an account with the ability
    #to be fetched and stored
end
```
* We're really decoupled
* However, we need to write more code (some will be boilerplate)

## Mixins and Traits

* We want to be able to extend classes and objects with new functionality without using inheritance. So we create a set of these functions, give that set a name, and then somehow extend a class or object with them.
* Merging functionality between _existing things_ and _new things_.

```
mixin CommonFinders {
    def find(id) { ... }
    def findAll() { ... } 
}

class AccountRecord extends BasicRecord with CommonFinders 
class OrderRecord extends BasicRecord with CommonFinders
```

* Can also add validation code to prevent bad data from infiltrating our calculations.
    * Use mixins to create specialized classes for appropriate situations:

```
class AccountForCustomer extends Account
    with AccountValidations,AccountCustomerValidations

class AccountForAdmin extends Account
    with AccountValidations,AccountAdminValidations
```

    * Both derived classes include validations common to all account objects
    * Customer variant includes validations appropriate for the customer-facing APIs
    * Admin variant contains (presumably less restrictive) admin validations
    * Passing instances of __AccountForCustomer__ or __AccountForAdmin__ back and forth, our code _automatically_ ensures the correct validation is applied.

## Challenges
* The next time you find yourself subclassing, take a minute to examine the options. Can you achieve what you want with interfaces, delegation, and/or mixins? Can you reduce coupling by doing so?