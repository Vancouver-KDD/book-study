# Data Class

Data class is the class only having fields and their getter and setter.
The data class is a simple container to hold the data used by other classes.

There are a few problems we possibly can make while using data class

## Public Fields
Solution: Encapsulate Record

Before
```java
class User {
    public String user_id;
    // .. other fields and methods
}    

// client
User user = new User();
String id = user.user_id;
```

After
```java
class User {
    private String user_id;
    public String get_id() {
        return user_id;
    }
    public void set_id(String user_id) {
        this.user_id = user_id;
    }
    // .. other fields and methods
}    

    User user = new User();
    String id = user.get_id();
```

## Mutability for Immutable Field
Solution: Remove Setting Method

Before
```java
class User {
    private String user_id; // the user_id should not be changed
    public String get_id() {
        return user_id;
    }
    public void set_id(String user_id) {
        this.user_id = user_id;
    }
    // .. other fields and methods
}    
    User user = new User();
    String id = user.set_id("new id");
```

After
```java
class User {
    private String user_id; // the user_id should not be changed
    
    public User(String user_id) {
        this.user_id = user_id;
    }
    
    public String get_id() {
        return user_id;
    }
    // .. other fields and methods
}    
    User user = new User("user_id");
```

## Find Frequently Used Method
If you find some common behaviours in multiple times, move function into data class.

Before
```java
class User {
    private String user_id; // the user_id should not be changed
    
    public User(String user_id) {
        this.user_id = user_id;
    }
    
    public String get_id() {
        return user_id;
    }
    // .. other fields and methods
}    
    User user = new User("user_id");
    // assume id comparison occurs over the code 
    if (user.get_id() == target_user_id) {
        System.out.println("found the user!");
    }
```

After
```java
class User {
    private String user_id; // the user_id should not be changed
    
    public User(String user_id) {
        this.user_id = user_id;
    }
    
    public String get_id() {
        return user_id;
    }

    public boolean is_target_user(String target_id) {
        return this.get_id() == target_id;
    }
    // .. other fields and methods
}    
    User user = new User("user_id");
    // assume id comparison occurs over the code 
    if (user.is_target_user(target_user_id)) {
        System.out.println("found the user!");
    }
```

## Exception
A record that is created by a result of exectuion from a distinct function.
The main characteristic of a record is a immutability.
Therefore, instead of using getter method or any other encapsulation, directly access to the field.