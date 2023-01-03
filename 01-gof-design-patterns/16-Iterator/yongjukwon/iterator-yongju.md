# Iterator pattern example

```java
/*
  refrence: https://www.youtube.com/watch?v=uNTNEfwYXhI
*/

// Iterator interface
public interface ItemInventoryIterator {

    boolean isDone();
    void next();
    Item current();
}

// Concrete Iterator
public class HandHeldInventoryIterator implements ItemInventoryIterator {

    private final int MAX_INDEX = 2;
    private HandHeldInventory inventory;
    private int index;

    public HandHeldInventoryIterator(HandHeldInventory handHeldInventory) {
        this.inventory = handHeldInventory;
    }

    @Override
    public boolean isDone() {
        return this.index < MAX_INDEX;
    }

    @Override
    public void next() {
        this.index += 1;
    }

    @Override
    public Item current() {
        switch(this.index) {
            case 0:
                return this.inventory.getRight();
            case 1:
                return this.inventory.getLeft();
            default:
                return null;
        }
    }
}

// Aggregate
public interface ItemInventory {
    ItemInventoryIterator getIterator();
}

// Concrete Aggregate
public class HandHeldInventory implements ItemInventory {

    public Item right;
    public Item left;

    public HandHeldInventory(Item right, Item left) {
        this.right = right;
        this.left  = left;
    }

    @Override
    public ItemInventoryIterator getIterator() {
        return new HandHeldInventoryIterator(this);
    }

    public Item getRight() {
        return right;
    }

    public Item getLeft() {
        return left;
    }
}

// Item (placeholder)
public class Item {
}

```