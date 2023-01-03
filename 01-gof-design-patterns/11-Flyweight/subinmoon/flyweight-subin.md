**Player.java**
```
public interface Player {
    public void assignWeapon(String weapon);
    public void mission();
}
```

**Terrorist.java**
```
class Terrorist implements Player {
    private final String TASK;

    private String weapon;
    
    public Terrorist() {
        TASK = "PLANT A BOMB";
    }

    public void assignWeapon(String weapon) {
        this.weapon = weapon;
    }

    public void mission() {
        System.out.println("Terrorist with weapon " + weapon + "|" + " Task is " + TASK);
    }
}
```

**CounterTerrorist.java**
```
public class CounterTerrorist implements Player {
    private final String TASK;

    private String weapon;
    
    public CounterTerrorist() {
        TASK = "DIFFUSE BOMB";
    }

    public void assignWeapon(String weapon) {
        this.weapon = weapon;
    }

    public void mission() {
        System.out.println("Counter Terrorist with weapon " + weapon + "|" + " Task is " + TASK);
    }
}
```
**PlayerFactory.java**
```
public class PlayerFactory {
    private static HashMap<String, Player> hm = new HashMap<>();

    public static Player getPlayer(String type) {
        Player p = null;

        if (hm.containKey(type)) {
            p = hm.get(type);
        } else {
            switch(type) {
                case "Terrorist":
                    System.out.println("Terrorist Created");
                    p = new Terrorist();
                    break;
                case "CounterTerrorist"
                    System.out.println("Counter Terrorist Created");
                    p = new CounterTerrorist();
                    break;
                default:
                    System.out.println("Unreachable code!");
            }
            hm.put(type, p);
        }
        return p;
    }
}
```

**CounterStrike.java**
```
public class CounterStrike {
    Random r = new Random();
    private static String[] playerType = {"Terrorist", "CounterTerrorist"};
    private static String[] weapons = {"AK-47", "Maverick", "Gut Knife", "Desert Eagle"};

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Player p = PlayerFactory.getPlayer(getRandPlayerType());
            p.assignWeapon(getRandWeapon());
            p.mission();
        }
    }

    public static String getRandPlayerType() {
        int randInt = r.nextInt(playerType.length);
        return playerType[randInt];
    }

    public static String getRandWeapon() {
        int randInt = r.nextInt(weapons.length);
        return weapons[randInt];
    }
}
```
Output:
```
Counter Terrorist Created
Counter Terrorist with weapon Gut Knife| Task is DIFFUSE BOMB
Counter Terrorist with weapon Desert Eagle| Task is DIFFUSE BOMB
Terrorist Created
Terrorist with weapon AK-47| Task is PLANT A BOMB
Terrorist with weapon Gut Knife| Task is PLANT A BOMB
Terrorist with weapon Gut Knife| Task is PLANT A BOMB
Terrorist with weapon Desert Eagle| Task is PLANT A BOMB
Terrorist with weapon AK-47| Task is PLANT A BOMB
Counter Terrorist with weapon Desert Eagle| Task is DIFFUSE BOMB
Counter Terrorist with weapon Gut Knife| Task is DIFFUSE BOMB
Counter Terrorist with weapon Desert Eagle| Task is DIFFUSE BOMB
```