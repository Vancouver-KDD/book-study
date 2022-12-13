# Refused Bequest
Sometimes, subclass does not want to use common fields or methods from the parent class.
This can be occured by wrongly inherited structure.

The first solution is that you create a sibling class instead of inheriting from parent class.
Then move the logic and field into a sibling class.

Instead of creating sibling clas, we can throw exception and overriding some unused methods from child class

## Push Down Method & Push Down Field

```java
class Ship {
    // for battle ship
    private int total_ammo;
    public reload() {
        this.total_ammo - 300;
    }
}

class CargoShip extends Ship {}
class BattleShip extends Ship {}
```

```java
class Ship {}

class CargoShip extends Ship {}
class BattleShip extends Ship {
    private int total_ammo;
    public reload() {
        this.total_ammo - 300;
    }
}
```