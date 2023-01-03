**Hotel.java**
```
public interface Hotel {
    public Menus getMenus();
}
```

**NonVegRestaurant.java**
```
publicl class NonVegRestaurant implement Hotel {
    public Menus getMenus() {
        NonVegMenu nv = new NonVegMenu();
        return nv;
    }
}
```

**VegRestaurant.java**
```
public class VegRestaurant implements Hotel {
    public Menus getMenus() {
        VegMenu v = new VegMenu();
        return v;
    }
}
```

**VegNonBothRestaurant.java**
```
public class VegNonBothRestaurant implements Hotel {
    public Menus getMenus() {
        Both b = new Both();
        return b;
    }
}
```

**HotelKeeper.java**
```
public class HotelKeeper {
    public VegMenu getVegMenu() {
        VegRestaurant v = new VegRestaurant();
        VegMenu vegMenu = (VegMenu)v.getMenus();
        return vegMenu;
    }

    public NonVegMenu getNonVegMenu() {
        NonVegRestaurant v = new NonVegRestaurant();
        NonVegMenu nonVegMenu = (NonVegMenu)v.getMenus();
        return nonVegMenu;
    }

    public Both getVegNonMenu() {
        VegNonBothRestaurant v = new VegNonBothRestaurant();
        Both bothMenu = (Both)v.getMenus();
        return bothMenu;
    }
}
```

**Client.java**
```
public class Client {
    public static void main(String[] args) {
        HotelKeeper keeper = new HotelKeeper();

        VegMenu v = keeper.getVegMenu();
        NonVegMenu nv = keeper.getNonVegMenu();
        Both = keeper.getVegNonMenu();
    }
}
```