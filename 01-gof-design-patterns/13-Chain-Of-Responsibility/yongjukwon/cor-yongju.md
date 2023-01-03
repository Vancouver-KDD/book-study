#Chain Of Responsibility example

```java
// Client
public class Client {
    public static void main(String[] args) {
        // Chain of dispensers: 50 -> 20 -> 5
        FiveDollarDispenser fiveDollarDispenser = new FiveDollarDispenser();
        TwentyDollarDispenser twentyDollarDispenser = new TwentyDollarDispenser(fiveDollarDispenser);
        FiftyDollarDispenser fiftyDollarDispenser = new FiftyDollarDispenser(twentyDollarDispenser);

        fiftyDollarDispenser.dispense(173);

        /* 
          Dispensing 3 fifty dollars  (from FiftyDollarDispenser)
          Dispensing 1 twenty dollars (from TwentyDollarDispenser)
          We do not have a smaller bill for $3 (from FiveDollarDispenser)
        */
    }
}

// Dispenser (common interface)
public interface Dispenser {
    void dispense(int amount);
    int limit();
}

// FiftyDollarDispenser(the first filter of the chain)
public class FiftyDollarDispenser implements Dispenser {

    private final int FIFTY = 50;
    private Dispenser next;

    // be given the next dispenser
    public FiftyDollarDispenser(Dispenser nextDispenser) {
        this.next  = nextDispenser;
    }
    
    // dispense fifty dollar bills if the given amount is over 50
    @Override
    public void dispense(int amount) {
        int fiftyDollar = amount / FIFTY;
        int leftover = amount % FIFTY;

        if (fiftyDollar > 0) {
            System.out.printf("Dispensing %d fifty dollars\n", fiftyDollar);
        }

        if (leftover >= next.limit()) {
            next.dispense(leftover);
        } else {
            System.out.printf("We do not have a smaller bill for $%d", leftover);
        }
    }

    // Return the limit amount of this dispenser
    @Override
    public int limit() {
        return FIFTY;
    }
}

// TwentyDollarDispenser(the second filter of the chain)
public class TwentyDollarDispenser implements Dispenser {

    private final int TWENTY = 20;

    private Dispenser next;

    public TwentyDollarDispenser(Dispenser nextDispenser) {
        this.next = nextDispenser;
    }


    @Override
    public void dispense(int amount) {
        int twentyDollar = amount / TWENTY;
        int leftover = amount % TWENTY;

        if (twentyDollar > 0) {
            System.out.printf("Dispensing %d twenty dollars\n", twentyDollar);
        }

        if (leftover >= next.limit()) {
            next.dispense(leftover);
        } else {
            System.out.printf("We do not have a smaller bill for $%d", leftover);
        }
    }

    @Override
    public int limit() {
        return TWENTY;
    }
}

// FiveDollarDispenser(the last filter of the chain)
public class FiveDollarDispenser implements Dispenser {

    private final int FIVE = 5;

    // Does not get the next dispenser as this is the last piece of the chain

    @Override
    public void dispense(int amount) {
        int fiveDollar = amount / FIVE;
        int leftover = amount % FIVE;

        if (fiveDollar > 0) {
            System.out.printf("Dispensing %d five dollars\n", fiveDollar);
        }

        if (leftover > 0) {
            System.out.printf("We do not have a smaller bill for $%d", leftover);
        }
    }

    @Override
    public int limit() {
        return FIVE;
    }
}
```