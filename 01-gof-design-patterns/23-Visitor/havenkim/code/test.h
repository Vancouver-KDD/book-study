class Asteroid;
class SpaceShip
{
public:
    virtual void CollideWith(Asteroid& inAsteroid);
};
class ApolloSpacecraft : public SpaceShip {
    void CollideWith(Asteroid& inAsteroid) override;
};

class Asteroid {
public:
  virtual void CollideWith(SpaceShip&);
  virtual void CollideWith(ApolloSpacecraft&);
};

class ExplodingAsteroid : public Asteroid {
public:
  void CollideWith(SpaceShip&) override;
  void CollideWith(ApolloSpacecraft&) override;
};
