# Visitor

### In One Liner

Let **complex, remain untouched** classes to be used in external objects by passing themselves.

The responsibility of fetching data and using the object is **delegated to the visitor** class so that the original object can remain pure and untouched.

### Point

The whole intent of using Visitor pattern is to determine and invoke class-specific behaviors while avoiding various polymorphism problems caused by complex inheritance. The method overloading and overriding are not the ultimate answer when the object has very complicated or deep depth inheritance.

Visitor pattern tries to solve this problem by [**Double dispatching**](https://en.wikipedia.org/wiki/Double_dispatch) technique.

>The problem is that, while virtual functions (Overrided methods) are dispatched dynamically, function overloading is done statically.

This means looking up vtable is done in runtime(fetch **instance-oriented** function body), compiling overloaded functions is static.

```c++
class SpaceShip {};
class ApolloSpacecraft : public SpaceShip {};

class Asteroid {
public:
  virtual void CollideWith(SpaceShip&) {
    std::cout << "Asteroid hit a SpaceShip\n";
  }
  virtual void CollideWith(ApolloSpacecraft&) {
    std::cout << "Asteroid hit an ApolloSpacecraft\n";
  }
};

class ExplodingAsteroid : public Asteroid {
public:
  void CollideWith(SpaceShip&) override {
    std::cout << "ExplodingAsteroid hit a SpaceShip\n";
  }
  void CollideWith(ApolloSpacecraft&) override {
    std::cout << "ExplodingAsteroid hit an ApolloSpacecraft\n";
  }
};
```

```c++
int main()
{
    SpaceShip theSpaceShip;
    ApolloSpacecraft theApolloSpacecraft;

    ExplodingAsteroid theExplodingAsteroid;
    Asteroid& theAsteroidReference = theExplodingAsteroid;
    theAsteroidReference.CollideWith(theSpaceShip); 
    theAsteroidReference.CollideWith(theApolloSpacecraft);
}
```
> ExplodingAsteroid hit a SpaceShip<br/>
> ExplodingAsteroid hit an ApolloSpacecraft

Since we are passing the typed objects (SpaceShip and ApolloSpacecraft), looking up vtable will be successful.

```c++
int main()
{
    ExplodingAsteroid theExplodingAsteroid;

    // Have Base Class type variables 
    Asteroid theAsteroid;
    Asteroid& theAsteroidReference = theExplodingAsteroid;

    // Have Base Class type variables
    ApolloSpacecraft theApolloSpacecraft;
    SpaceShip& theSpaceShipReference = theApolloSpacecraft;
    
    theAsteroid.CollideWith(theApolloSpacecraft);
    theAsteroidReference.CollideWith(theApolloSpacecraft);

    theAsteroid.CollideWith(theSpaceShipReference);
    theAsteroidReference.CollideWith(theSpaceShipReference);
}
```                                                                                       
> Asteroid hit an ApolloSpacecraft<br/>
> ExplodingAsteroid hit an ApolloSpacecraft<br/>
> Asteroid hit a SpaceShip<br/>
> ExplodingAsteroid hit a SpaceShip

But in this case, fetching correct method fails because even though the actual memory of *theSpaceShipReference* is ApolloSpaceCraft, the visible scope is limited at *SpaceShip*

```c++
virtual void CollideWith(SpaceShip&) {
    std::cout << "Asteroid hit a SpaceShip\n";
}
virtual void CollideWith(ApolloSpacecraft&) {
    std::cout << "Asteroid hit an ApolloSpacecraft\n";
}
```

When determining which overloaded function to choose is done in static compile time but our desired output only can be done in runtime.

But using double dispatch technique, we are going to look up vtable twice and decide the correct method body at compile time as well.

```c++
void SpaceShip::CollideWith(Asteroid& inAsteroid) {
    inAsteroid.CollideWith(*this);
}
void ApolloSpacecraft::CollideWith(Asteroid& inAsteroid) {
    inAsteroid.CollideWith(*this);
}
```
The CollideWith function must implemented in both classes because if the base class *SpaceShip* only has the body, *this will remain at *SpaceShip* scope.

We can finally get a desired result with a reversed call for double dispatching.

```c++
int main()
{
    ExplodingAsteroid theExplodingAsteroid;

    // Have Base Class type variables 
    Asteroid theAsteroid;
    Asteroid& theAsteroidReference = theExplodingAsteroid;

    // Have Base Class type variables
    ApolloSpacecraft theApolloSpacecraft;
    SpaceShip& theSpaceShipReference = theApolloSpacecraft;
    
    theAsteroid.CollideWith(theApolloSpacecraft);
    theAsteroidReference.CollideWith(theApolloSpacecraft);

    theSpaceShipReference.CollideWith(theAsteroid);
    theSpaceShipReference.CollideWith(theAsteroidReference);
}
```     
> Asteroid hit an ApolloSpacecraft<br/>
> ExplodingAsteroid hit an ApolloSpacecraft<br/>
> Asteroid hit an ApolloSpacecraft<br/>
> ExplodingAsteroid hit an ApolloSpacecraft

*theSpaceShipReference* will look up the vtable for SpaceShip's CollideWith function and then when it passes ***this**, Asteroid class will look up its vtable again for CollideWith.

### Pros 

- Open/Closed & Single Responsibility Principle. 

### Cons

- Visitors must know all variations of the parameter classes, hence they must update or introduce new function schema heavily coupled with the parameters.
- Sometimes lack of private attributes or method accessing can cause a encapsulation break. But it's a very minor problem because each class can define a proper handling method anyway.