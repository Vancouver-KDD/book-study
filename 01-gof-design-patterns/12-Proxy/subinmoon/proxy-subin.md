**Internet.java**
```
public interface Internet {
    public void connectTo(String serverhost) throws Exception;
}
```

**RealInternet.java**
```
public class RealInternet implements Internet {
    @Override
    public void connecTo(String serverhost) {
        System.out.println("Connecting to " + serverhost);
    }
}
```


**ProxyInternet.java**
```
public class ProxyInternet implements Internet {
    private Internet internet = new RealInternet();
    private static List<String> bannedSites;

    static {
        bannedSites = new ArrayList<String>();
        bannedSites.add("abc.com");
        bannedSites.add("def.com");
        bannedSites.add("ijk.com");
        bannedSites.add("lnm.com");
    }

    @Override
    public void connecTo(String serverhost) throws Exception {
        if (bannedSites.contains(serverhost.toLowercase())) {
            throw new Exception("Access Denied");
        }
        internet.connectTo(serverhost);
    }
}
```

**Client.java**
```
public class Client {
    public static void main(String[] args) {
        Internet internet = new ProxyInternet();

        try {
            internet.connectTo("bcit.org");
            internet.connectTo("abc.com");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
```
Output:
```
Connecting to bcit.org
Access Denied
```