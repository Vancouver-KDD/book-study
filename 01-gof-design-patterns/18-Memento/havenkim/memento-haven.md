# Memento

m. an object kept as a reminder or souvenir of a person or event.
- "You can purchase a mememnto of your visit"

### In One Liner

Snapshot(State Payload) of an object to keep "required" attributes and retrieve them in the future use while hiding all details of the object.

### Point

Memento class is not inheriting Originator but it still needs to access private attributes. It means the pattern's implementation relies on supported language features. 

[refactoring.guru](https://refactoring.guru/design-patterns/memento) covered different implementations of C++/C#, PHP, or even more abstracted version. The simplest and most ideal solution (if applicable) will still be the C++/C# version.

### Pros 

- Encapsulate Originator's attributes and save history of states of the object. Originators can jump back to the previous state by appling a specific memento at any time.

### Cons

- The pattern couldn't applicable depends on language designs.
- Ensuring memento immutable could be hard depends on language designs (ex. JavaScript).
- Revealing & Hiding of interfaces could be very harsh depends on object's responsibility or behaviors.
- The history (so called Caretaker in the book) is not managed well since mementos are hidden to Caretaker. It produces RAM explosion if the history is not managed in a good manner.

### Example

[refactoring.guru](https://refactoring.guru/design-patterns/memento/cpp/example#lang-features)

```c++
#include <string>

class Memento
{
public:
    virtual ~Memento() {}
    virtual std::string GetName() const = 0;
    virtual std::string date() const = 0;
    virtual std::string state() const = 0;
};

class ConcreteMemento : public Memento
{
public:
    ConcreteMemento(std::string state)
        : mState(state)
    {
        std::time_t now = std::time(0);
        mDate = std::ctime(&now);
    }
    std::string state() const override
    {
        return mState;
    }
    std::string GetName() const override
    {
        return mDate + " / (" + mState.substr(0, 9) + "...)";
    }
    std::string date() const override
    {
        return mDate;
    }
private:
    std::string mState;
    std::string mDate;
};

class Originator
{
private:
    std::string GenerateRandomString(int length);

public:
    Originator(std::string state) : mState(state)
    {
    }

    void MutateString(int length)
    {
        mState = GenerateRandomString(length);
    }

    Memento* Save()
    {
        return new ConcreteMemento(mState);
    }
    void Restore(Memento* memento)
    {
        mState = memento->mState;
    }
private:
    std::string GenerateRandomString(int length) 
    {
        const char alphanum[] =
        "0123456789"
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        "abcdefghijklmnopqrstuvwxyz";

        int stringLength = sizeof(alphanum) - 1;

        std::string random_string;
        for (int i = 0; i < length; i++)
        {
            random_string += alphanum[std::rand() % stringLength];
        }
        return random_string;
    }

    std::string mState;
};

class History
{
public:
    History(Originator* originator) : mOriginator(originator)
    {
    }

    ~History()
    {
        for (Memento* m : mHistories)
        {
            delete m;
        }
        mHistories.clear();
    }

    void StoreSnapshot()
    {
        mHistories.push_back(mOriginator->Save());
    }
    bool Undo()
    {
        if (mHistories.empty())
            return false;

        Memento* prevMemento = mHistories.back();
        mHistories.pop_back();

        mOriginator->Restore(prevMemento);
    }
private:
    Originator* mOriginator;
    std::vector<Memento*> mHistories;
};

int main()
{
    Originator* newOriginator = new Originator("Hello KDD World!");
    History* history = new History(newOriginator);

    history->StoreSnapshot();
    newOriginator->MutateString(5);
    history->StoreSnapshot();
    newOriginator->MutateString(10);
    history->StoreSnapshot();
    newOriginator->MutateString(35);

    delete newOriginator;
    delete history;

    return 0;
}
```