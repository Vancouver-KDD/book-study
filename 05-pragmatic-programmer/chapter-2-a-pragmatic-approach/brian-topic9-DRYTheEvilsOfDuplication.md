# The Evils of Duplication

### Unfortunately, knowledge isn’t stable. Requirements change—often rapidly. 
  - Maintenance is not a discrete activity, but a routine part of the entire development process.
  - Only way to develop software reliably is to follow DRY principle:
    - Every piece of knowledge must have a single, unambiguous, authoritative representation within a system.

## Tip 15 - DRY—Don’t Repeat Yourself
### DRY IS MORE THAN CODE
- DRY doesn't mean only “don’t copy-and-paste lines of source.”
- DRY is about the duplication of knowledge, of intent.
- when some single facet of the code has to change, 
  - do you find yourself making that change in multiple places.
    - The code isn't DRY.
### DUPLICATION IN CODE
```
def print_balance(account)     
    printf "Debits:  %10.2f\n", account.debits
    printf "Credits: %10.2f\n", account.credits
    if account.fees < 0
        printf "Fees:    %10.2f-\n", -account.fees
    else
        printf "Fees:    %10.2f\n", account.fees
    end
    printf "         ———-\n"
    if account.balance < 0
        printf "Balance: %10.2f-\n", -account.balance
    else
        printf "Balance: %10.2f\n", account.balance
    end
end
```
```
def format_amount(value)
    result = sprintf("%10.2f", value.abs)
    if value < 0
        result + "-"     
    else       
        result + " "     
    end   
end

def print_balance(account)
    printf "Debits:  %10.2f\n", account.debits
    printf "Credits: %10.2f\n", account.credits
    printf "Fees:    %s\n",
    format_amount(account.fees)
    printf "         ———-\n"
    printf "Balance: %s\n",
    format_amount(account.balance)
end
```
```
def format_amount(value)
    result = sprintf("%10.2f", value.abs)
    if value < 0
        result + "-"     
    else       
        result + " "     
    end   
end

def print_balance(account)
    printf "Debits:  %10.2f\n", format_amount(account.debits)
    printf "Credits: %10.2f\n", format_amount(account.credits)
    printf "Fees:    %s\n", format_amount(account.fees)
    printf "         ———-\n"
    printf "Balance: %s\n", format_amount(account.balance)
end
```
```
def format_amount(value)
    result = sprintf("%10.2f", value.abs)
    if value < 0
        result + "-"     
    else       
        result + " "     
    end   
end

def print_line(label, value)
    printf "%-9s%s\n", label, value
end

def report_line(label, amount)
    print_line(label + ":", format_amount(amount))
end

def print_balance(account)
    report_line("Debits",  account.debits)
    report_line("Credits", account.credits)
    report_line("Fees",    account.fees)
    print_line("",         "———-")
    report_line("Balance", account.balance) 
end
```
#### Not All Code Duplication Is Knowledge Duplication 
```
def validate_age(value):
    validate_type(value, :integer)
    validate_min_integer(value, 0)
  
def validate_quantity(value):
    validate_type(value, :integer)
    validate_min_integer(value, 0)

```
### DUPLICATION IN DOCUMENTATION
```
# Calculate the fees for this account.
#
# * Each returned check costs $20
# * If the account is in overdraft for more than 3 days,
#   charge $10 for each day
# * If the average account balance is greater that $2,000
#   reduce the fees by 50%

def fees(a)
    f = 0
    if a.returned_check_count > 0
        f += 20 * a.returned_check_count
    end
    if a.overdraft_days > 3
        f += 10*a.overdraft_days
    end
    if a.average_balance > 2_000
        f /= 2
    end
    f
end
```
```
def calculate_account_fees(account)
    fees  = 20 * account.returned_check_count
    fees += 10 * account.overdraft_days  
    if account.overdraft_days > 3
        fees /= 2
    if account.average_balance > 2_000
        fees
end
```
#### DRY Violations in Data
```
class Line {
    Point  start;
    Point  end;
    double length;
};
```
```
class Line {
    Point  start;
    Point  end;
    double length() { 
        return start.distanceTo(end); 
    }
};
```
```
class Line {
    private double length;
    private Point  start;
    private Point  end;
    
    public Line(Point start, Point end) {
        this.start = start;
        this.end   = end;
        calculateLength();
    }
    
    // public
    void setStart(Point p) { this.start = p; calculateLength(); }
    void setEnd(Point p)   { this.end   = p; calculateLength(); }
    
    Point getStart()       { return start; }
    Point getEnd()         { return end;   }
    double getLength()     { return length; }
    
    private void calculateLength() {
           this.length  = start.distanceTo(end);
    }   
};

```
### REPRESENTATIONAL DUPLICATION 
#### Duplication Across Internal APIs
- Ideally the tool will store all your APIs in a central repository, 
  - allowing them to be shared across teams.

#### Duplication Across External APIs
- OpenAPI allows you to import the API spec into your local API tools 
  - and integrate more reliably with the service.

#### Duplication with Data Sources 
- Rather than writing code that represents external data in a fixed structure (an instance of a struct or class, for example),
  - YES!!!
- just stick it into a key/value data structure (your language might call it a map, hash, dictionary, or even object).
  - No!!!!

### INTERDEVELOPER DUPLICATION
- We feel that the best way to deal with this is 
  - to encourage active and frequent communication between developers.

## Tip 16 - Make It Easy to Reuse
- What you’re trying to do is foster an environment 
  - where it’s easier to find and reuse existing stuff than to write it yourself