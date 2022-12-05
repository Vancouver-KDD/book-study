# Large Class

`Extract Class`: 여러 variable들을 한 곳으로 모을 수 있다
`Extract Superclass`/`Replace Type Code with Subclasses`: inheritance를 이용 해 class를 분리해준다


##### Extract Class
```java
  // Before
  public class BankAccount {
    enum AccountType {
      CHECKING,
      SAVING,
      INVESTMENT
    }
    private String nameOfOwner;
    private AccountType accountType;
    private String openedYear;
    private String openedDate;
    private String openedTime;
    private String city;
    private String streetAddress;
    private String postalCode;
    private Double balance;
    private Double overDraftLimit;
    private Double withdrawalLimit;
    private String connectedCardNum;
    
    // methods
    ...
  } 

  // After
  public class BankAccount {
    private String nameOfOwner;
    private Double balance;
    private Date openedDate;
    ...
  }

  public class CheckingAccount extends BankAccount {
    ...
  }

  public class SavingAccount extends BankAccount {
    ...
  }

  public class Address {
    private String city;
    private String streetAddress;
    private String postalCode;
    ...
  }
```