# Builder

- Client
```java
  public class Client {
    public static void main(String[] args) {
        Director director = new Director();
        CheckingAccountBuilder checkingAccountBuilder = new CheckingAccountBuilder();
        SavingAccountBuilder savingAccountBuilder = new SavingAccountBuilder();

        Account checkingAccount = director.createAccount(checkingAccountBuilder);
        Account savingAccount = director.createAccount(savingAccountBuilder);

        System.out.println(checkingAccount);
        System.out.println(savingAccount);
    }
  }
```

- Director
```java
  public class Director {
    public Account createAccount(IAccountBuilder builder) {
        Account account = null;

        if (builder instanceof CheckingAccountBuilder) {
            account = builder
            .addOwner("Checking Owner")
            .addBalance(0.0)
            .addInterestRate(0.0)
            .addOpenDate("20220101")
            .addOpenedBranch(Branch.DOWNTOWN)
            .addAccountNumber("22220001")
            .createAccount();
        } else if (builder instanceof SavingAccountBuilder) {
            account = builder
            .addOwner("Saving Owner")
            .addBalance(0.0)
            .addInterestRate(3.2)
            .addOpenDate("20220101")
            .addOpenedBranch(Branch.DOWNTOWN)
            .addAccountNumber("55550001")
            .createAccount();
        } else {
            // more logic
        }

        return account;
    }
  }
```

- IAccountBuilder
```java
 public interface IAccountBuilder {

    IAccountBuilder addOwner(String owner);
    IAccountBuilder addBalance(Double balance);
    IAccountBuilder addInterestRate(Double interestRate);
    IAccountBuilder addOpenDate(String date); // String for convenience
    IAccountBuilder addOpenedBranch(Branch branch);
    IAccountBuilder addAccountNumber(String accountNum);

    // build method
    Account createAccount();
 }
```

- CheckingAccountBuilder
```java
public class CheckingAccountBuilder implements IAccountBuilder {

    private String owner;
    private Double balance;
    private Double interestRate;
    private String openDate;
    private Branch branch;
    private String accountNum;

    private Double requiredMinBalance;

    @Override
    public IAccountBuilder addOwner(String owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public IAccountBuilder addBalance(Double balance) {
        this.balance = balance;
        return this;
    }

    @Override
    public IAccountBuilder addInterestRate(Double interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    @Override
    public IAccountBuilder addOpenDate(String openDate) {
        this.openDate = openDate;
        return this;
    }

    @Override
    public IAccountBuilder addOpenedBranch(Branch branch) {
        this.branch = branch;
        return this;
    }

    @Override
    public IAccountBuilder addAccountNumber(String accountNum) {
        this.accountNum = accountNum;
        return this;
    }

    @Override
    public Account createAccount() {
        CheckingAccount checkingAccount = new CheckingAccount(this.owner, this.balance, this.interestRate, this.openDate, this.branch, this.accountNum);

        return checkingAccount;
    }
  }
```

- SavingAccountBuilder
```java
public class SavingAccountBuilder implements IAccountBuilder {

    private String owner;
    private Double balance;
    private Double interestRate;
    private String openDate;
    private Branch branch;
    private String accountNum;

    public SavingAccountBuilder() {}

    @Override
    public IAccountBuilder addOwner(String owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public IAccountBuilder addBalance(Double balance) {
        this.balance = balance;
        return this;
    }

    @Override
    public IAccountBuilder addInterestRate(Double interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    @Override
    public IAccountBuilder addOpenDate(String openDate) {
        this.openDate = openDate;
        return this;
    }

    @Override
    public IAccountBuilder addOpenedBranch(Branch branch) {
        this.branch = branch;
        return this;
    }

    @Override
    public IAccountBuilder addAccountNumber(String accountNum) {
        this.accountNum = accountNum;
        return this;
    }

    @Override
    public Account createAccount() {
        SavingAccount savingAccount = new SavingAccount(this.owner, this.balance, this.interestRate, this.openDate, this.branch, this.accountNum);

        return savingAccount;
    }
  }
```

- Account 
```java
  public class Account {}
```

- SavingAccount
```java
  public class SavingAccount extends Account{

    private final String owner;
    private final Double balance;
    private final Double interestRate;
    private final String openDate;
    private final Branch branch;
    private final String accountNum;

    protected SavingAccount(String owner, Double balance, Double interestRate, String openDate, Branch branch, String accountNum) {
        this.owner = owner;
        this.balance = balance;
        this.interestRate = interestRate;
        this.openDate = openDate;
        this.branch = branch;
        this.accountNum = accountNum;
    }

    public String getOwner() {
        return owner;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public Branch getBranch() {
        return branch;
    }

    public String getAccountNum() {
        return accountNum;
    }

    @Override
    public String toString() {
        return "SavingAccount{" +
                "owner='" + owner + '\'' +
                ", balance=" + balance +
                ", interestRate=" + interestRate +
                ", openDate='" + openDate + '\'' +
                ", branch=" + branch +
                ", accountNum='" + accountNum + '\'' +
                '}';
    }
 }
```

- CheckingAccount
```java
  public class CheckingAccount extends Account {

    private final String owner;
    private final Double balance;
    private final Double interestRate;
    private final String openDate;
    private final Branch branch;
    private final String accountNum;

    public CheckingAccount(String owner, Double balance, Double interestRate, String openDate, Branch branch, String accountNum) {
        this.owner = owner;
        this.balance = balance;
        this.interestRate = interestRate;
        this.openDate = openDate;
        this.branch = branch;
        this.accountNum = accountNum;
    }

    public String getOwner() {
        return owner;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public Branch getBranch() {
        return branch;
    }

    public String getAccountNum() {
        return accountNum;
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "owner='" + owner + '\'' +
                ", balance=" + balance +
                ", interestRate=" + interestRate +
                ", openDate='" + openDate + '\'' +
                ", branch=" + branch +
                ", accountNum='" + accountNum + '\'' +
                '}';
    }
  }
```

- Branch
```java
  public enum Branch {

    DOWNTOWN,
    BURNABY,
    RICHMOND
  }
```
