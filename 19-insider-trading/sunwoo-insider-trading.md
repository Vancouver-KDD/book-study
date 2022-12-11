# Insider Trading

When we program, we like to have independent modules. However, trading data between those module increase the coupling between them.
If a component know too much information for another component, we need to reduce it.

## Solution
### Replace Subclass with Delegate
To change the logic or extend the functionality of classes, we inherit the parent class and modify the children class.
But inheritance has downside. The child class is only for one specific behaviour. The child class can't handle logic which is sperated by two behaviours.
Also, parent-child classes have close relationship, so if we change the parent class's logic, it can cause the error in the child class.
If we use delegation, we can free from above restriction. It is similar to strategy pattern and state pattern in gof.

```ts
// we have 4 different inherited classes for two behaviours
class Trip {
    _location = 'Default'
    _type = 'Defauly'
    
    public getPlan() {
        console.log(`Location: ${this._location}`)
        console.log(`Type ${this._type}`)
    }
}

class ActivityCanadaTrip extends Trip {
    _location = 'Canada'
    _type = 'Activity'
}

class SightseeingCanadaTrip extends Trip {
    _location = 'Canada'
    _type = 'Activity'
}

class ActivityKoreaTrip extends Trip {
    _location = 'Canada'
    _type = 'Activity'
}

class SightseeingKoreaTrip extends Trip {
    _location = 'Canada'
    _type = 'Activity'
}
```

```ts
class TripCreator {
    _type: TripType
    _location: TripLocation
    constructor(type: TripType, location: TripLocation ) {
        this._type = type
        this._location = location
    }

   public getPlan() {
        console.log(`Location: ${this._location._location}`)
        console.log(`Type ${this._type._type}`)
   }
}

class TripType {
    _type = 'Default'
}
class Sightseeing extends TripType {
    _type = 'Sightseeing'
}
class Activity extends TripType {
    _type = 'Activity'
}

class TripLocation {
    _location = 'Default'
}
class CanadaTrip extends TripLocation {
    _location = 'Canada'
}
class KoreaTrip extends TripLocation {
    _location = 'Korea'
}
```

### Replace Superclass with Delegate
To extend the functionality, using inheritance usually good idea. However, sometime some interface which is not required in the subclass can be exposed to the client code.
To handle these problem, we can have an object of delegated object for super class and use the interface to extend the functionality.
```ts
class BSTTree {
    val: number
    left: BSTTree | null
    right: BSTTree | null
    
    constructor(val: number) {
        this.val = val
    }

    public insertNode(node: BSTTree) {
        this.insert(node, this)
    }

    public insert(node: BSTTree, root: BSTTree | null ) {
        if (root === null) {
            root = node
        }
        if (root.val >= node.val) {
            this.insert(node, root.left)
        }
        else {
            this.insert(node, root.right)
        }
    }
}

class BSTTreeTraversal extends BSTTree {
    left: BSTTreeTraversal | null
    right: BSTTreeTraversal | null

    public inorderPrint() {
        this.left?.inorderPrint()
        console.log(this.val)
        this.right?.inorderPrint()
    }
}

class BSTTreeTraversalDele {
    root: BSTTreeTraversal
    constructor(root: BSTTreeTraversal) {
        this.root = root
    }
    
    public inorderPrint() {
        this.root.left?.inorderPrint()
        console.log(this.root.val)
        this.root.left?.inorderPrint()
    }
}
```