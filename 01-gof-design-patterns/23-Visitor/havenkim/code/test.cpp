#include <iostream>
#include "test.h"

void SpaceShip::CollideWith(Asteroid& inAsteroid) {
    inAsteroid.CollideWith(*this);
}
void ApolloSpacecraft::CollideWith(Asteroid& inAsteroid) {
    inAsteroid.CollideWith(*this);
}

void Asteroid::CollideWith(SpaceShip&) {
    std::cout << "Asteroida hit a SpaceShip\n";
}
void Asteroid::CollideWith(ApolloSpacecraft&) {
    std::cout << "Asteroid hit an ApolloSpacecraft\n";
}

void ExplodingAsteroid::CollideWith(SpaceShip&) {
    std::cout << "ExplodingAsteroid hit a SpaceShip\n";
}
void ExplodingAsteroid::CollideWith(ApolloSpacecraft&) {
    std::cout << "ExplodingAsteroid hit an ApolloSpacecraft\n";
}

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