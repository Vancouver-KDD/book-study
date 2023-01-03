# Command (Transaction)

### In One Liner

m. an exchange or transfer of goods, srevices, or funds.

Add a transaction layer in between **action provider** and **action receiver**. Each transaction contains any regarding information about the action such as method name, parameter, execution result, etc.

### Discussion Point

>You might have noticed one missing piece of the puzzle, which is the request parameters. A GUI object might have supplied the business-layer object with some parameters. Since the command execution method doesn’t have any parameters, how would we pass the request details to the receiver? It turns out the command should be either pre-configured with this data, or capable of getting it on its own.
[refactoring.guru](https://refactoring.guru/design-patterns/command)

[Command Pattern : How to pass parameters to a command?](https://stackoverflow.com/questions/104918/command-pattern-how-to-pass-parameters-to-a-command)

The principal problem that would occur is *how can we pass parameters needed for command execution*. 

From the pattern definition and advantage approach, Execute() should not take extra parameter but command itself must contain any regarding information. It is very important to keep the Execute method simple since the power of the pattern is flexibility, undo and redo. 

But in real system, it seems like there are a lot more problems followed by keeping this pattern principle strictly. It even could generate more complicated or spaguetti structure in between commands to solve. 

One of the suggested alternative is Executed Routed Event system as introduced in [this article](https://en.wikipedia.org/wiki/Command_pattern#Terminology). It combines command pattern and the event processing. As a result, there is no composition in between senders and receivers. And the queued events are being handled during **Event execution phase** with payloads.  

### Pros 

- Single Responsibility Principle & Open/Close Principle. Very flexible and easily maintained.
- If the implementation can be done with simple Execute() method, undo and redo is possible.
- It enables to define sequential command operations and increase reusability of each feature(command)

### Cons

- Extra complexity of each layer and since the Command objects can be dynamically assigned, business logic could get highly tangled.

### Example

```c++
class Command
{
public:
    virtual ~Command();
    virtual void Execute() const = 0;
};
```

```c++
#include <string>
#include "portable-file-dialog.h"

class OpenFileCommand : public Command
{
public:
    OpenFileCommand(DocumentManager* manager) :
        mpRefManager(manager)
    {
    }

    void Execute() const override
    {
        std::string path = pfd::open_file::open_file("Open Document", ".", { "All Files", "*" }).result();
        if (!path.empty())
        {
            Document* newDocument = new Document();
            newDocument->read(path);
            mpRefManager->addDocument(newDocument);
        }
    }
private:
    DocumentManager* mpRefManager;
}
```
```c++
class SaveFileCommand : public Command
{
public:
    SaveFileCommand(Document* document) :
        mpRefDocument(document)
    {
    }

    void Execute() const override
    {
        std::string path = pfd::save_file::save_file("Save Document", ".", { "All Files", "*" }).result();
        if (!path.empty())
        {
            mpRefDocument->save(path);
        }
    }
private:
    Document* mpRefDocument;
}
```

```c++
#include "Command.h"
#include "OpenFileCommand.h"
#include "SaveFileCommand.h"

int main()
{
    DocumentHandler* handler = new DocumentHandler;

    Command* cmd = new OpenFileCommand(handler);
    cmd->Execute();
    delete cmd;

    Document* doc = handler->getLastDocument();
    cmd = new SaveFileDocument(doc);
    cmd->Execute();
    delete cmd;
}
```