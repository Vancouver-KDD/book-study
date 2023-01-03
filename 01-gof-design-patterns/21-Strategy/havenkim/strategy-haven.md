# Strategy

### In One Liner

Decide which Tool / Method / Algorithm you desire to use in runtime.

### Point

Strategy pattern has very similar structures and approaches as other patterns such as Bridge, Command, State or Template Method.

But the core intents of the pattern are focused on
- Describe different ways of **achieving the same goal**
- Change the ways in **runtime** depends on users(clients)' needs
- Change the ways at **object level with composition** (Template Method : at class level, inheritance) 
- It handles **any requirements itself**. But they might need to be handled by the context (State: Context has states and State Pattern refers to them to decide which state should be followed)
- Each strategy **doesn't know each other's existence and Context also doesn't have to know any detail** of them (State: each state can have relationship for a state progression)

### Pros 

- Change the **algorithm** in runtime
- Open/Closed, Single Responsibility Principle

### Cons

- As always, it might be an overkill if the algorithms don't change often or they occasionally alter in runtime.
- lambda features can achieve the same goal without any interface or extra class structures (ex. [python filter](https://www.programiz.com/python-programming/methods/built-in/filter))
  >A lot of modern programming languages have functional type support that lets you implemenmt different versions of an algorithm inside a set of anonymous functions. Then you could use these functions exactly as you'd have used the strategy objects, but without bloating your code with extra classes and interfaces. [refactoring.guru](https://refactoring.guru/design-patterns/strategy)

### Example

Payment system demonstrates the advantage of strategy pattern very well but in a static main code, it cannot show the full profit. 

[Reference](https://www.digitalocean.com/community/tutorials/strategy-design-pattern-in-java-example-tutorial)

```c++
class PaymentStrategy
{
public:
    virtual bool pay(float amount) = 0;
};
```

```c++
class CreditCardStrategy : public PaymentStrategy
{
public:
    CreditCardStrategy(
        const std::string& name,
        const std::string& cardNumber,
        const int cvv,
        const int dateOfExpiry) :
        name(name), cardNumber(cardNumber), cvv(cvv), dateOfExpiry(dateOfExpiry)
    {
    }

    bool pay(float amount)
    {
        std::cout << "Credit Card payment has been made with amount of " << amount << std::endl;
        return true;
    }
private:
    std::string name;
    std::string cardNumber;
    int cvv;
    int dateOfExpiry;
};

class PaypalStrategy : public PaymentStrategy
{
public:
    PaypalStrategy(
        const std::string& paypalEmail,
        const std::string& paypalCredential) :
        paypalEmail(paypalEmail), paypalCredential(paypalCredential)
    {
    }
    
    bool pay(float amount)
    {
        std::cout << "Paypal payment has been made with amount of " << amount << std::endl;
        return true;
    }
private:
    std::string paypalEmail;
    std::string paypalCredential;
};

class CashStrategy : public PaymentStrategy
{
public:
    CashStrategy()
    {
    }
    
    bool pay(float amount)
    {
        std::cout << "Cash payment has been made with amount of " << amount << std::endl;
        return true;
    }
};
```

```c++
struct Item
{
    std::string mName;
    float mPrice;
    int mAmount;

    float getTotalPrice()
    {
        return mPrice * mAmount;
    }
};
// Context object
class ShoppingCart
{
public:
    void addNewItem(const Item& item) 
    {
        items.push_back(item);
    }
    void removeItem(Item& item)
    {
        items.remote(item);
    }
    float getTotalPrice()
    {
        float total = 0;
        for (Item& item : mItems)
        {
            total += item.getTotalPrice();
        }
        return total;
    }

    bool payment(const PaymentStrategy& paymentMethod)
    {
        return paymentMethod.pay(getTotalPrice());
    }
private:
    std::list<Item> mItems;
};
```

```c++
int main()
{
    ShoppingCard cart;

    cart.addItem({ "iPad 10th generation", 599, 1 });   
    cart.addItem({ "airpot pro 2nd generation", 329, 2 });   

    cart.payment(CreditCardStrategy("Haven Kim", "123456789000000", 111, 1212));

    return 0;
};
```