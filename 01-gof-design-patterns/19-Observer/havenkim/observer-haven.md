# Observer

### In One Liner

If you want to subscribe a youtuber for their new contents, you are now an observer. The youtuber is a subject that will notify you when they upload a new video.  

### Point

The main problem you need to concern is thread-safety of Resolve or Notify function. Depends on the design of notification or sequence of calls, it likely to create a bottleneck or modified memory while being accessed problem.

1. If the notify callback is called right away after the state changes

This is a common simple design of observer pattern but it is also a common mistake if the thread-safetyness has to be cared. The problem can be fixed by reordering notification handler outside of the scope.

2. Modified memory while being accessed.

This is a core design problem if it happens. Observers can change their states itself (ex. UI text or something independent) but should not write to the memory. If it is needed (should not), consider using semaphore. Keep in mind, giving lots of power to observers will be a sequence of a massive disaster. Their notification interfaces should remain as an "observer", not an "actor".

### Pros 

- Without multi-directional references Observer pattern enables abstracted communication channel in between subscribers and broadcasters. 

### Cons

- [refactoring.guru](https://refactoring.guru/design-patterns/observer) addresses that notification order is not consistent but if the sequence of observer notification is a concern, it is not a good design in my opinion. Notifications and regarding actions must follow Open/Closed Principle and no subsequential dependencies. If it is required, they should have an adapter to compound them into one object that intercept the notification and delegate handles.

### Example

```c++
#pragma once
#include <unordered_map>
#include <unordered_set>

namespace KDDExamples
{
    enum class EventType : int
    {
        EntityListUpdate,
        NewEntitySelection,
    };

    typedef std::pair<void*, unsigned int> EventPayload;
    typedef std::pair<ProjectEvent, EventPayload> ProjectEventWithPayload;
    class ProjectEventListener
    {
    public:
        virtual void Resolve(ProjectEventWithPayload e) = 0;
    };

    class ProjectEventManager
    {
    public:
        ProjectEventManager();
        ~ProjectEventManager();
        void Register(ProjectEvent e, ProjectEventListener* listener);
        void Unregister(ProjectEvent e, ProjectEventListener* listener);
        void Invoke(ProjectEvent e);
        void Invoke(ProjectEvent e, void* payload, unsigned int payloadSize);
        bool Process();

    private:
        typedef ProjectEventListener* Listener;
        typedef std::unordered_set<ProjectEventListener*> Listeners;
        std::unordered_map<ProjectEvent, Listeners> mEventPack;
        std::list<ProjectEventWithPayload> mEventWithPayloads;

        static int NumEvents;
    };
}
```

```c++
#include "ProjectEventManager.h"
using namespace KDDExamples;

int ProjectEventManager::NumEvents = 0;

ProjectEventManager::ProjectEventManager()
{
    mEventPack.clear();
}

ProjectEventManager::~ProjectEventManager()
{
    std::cout << "ProjectEventManager Released. The Number of Unregistered EventListener Instance Left: " << NumEvents << std::endl;
    mEventPack.clear();
}

void ProjectEventManager::Register(ProjectEvent e, ProjectEventListener* listener)
{
    NumEvents++;
    mEventPack[e].insert(listener);
}

void ProjectEventManager::Unregister(ProjectEvent e, ProjectEventListener* listener)
{
    auto found = mEventPack[e].find(listener);
    if (found != mEventPack[e].end())
    {
        NumEvents--;
        mEventPack[e].erase(found);
    }
}

// NULL payload
void ProjectEventManager::Invoke(ProjectEvent e)
{
    mEventWithPayloads.push_back(std::make_pair<>(e, std::make_pair<>((void*)NULL, 0)));
}

void ProjectEventManager::Invoke(ProjectEvent e, void* payload, unsigned int payloadSize)
{
    mEventWithPayloads.push_back(std::make_pair<>(e, std::make_pair<>(payload, payloadSize)));
}

bool ProjectEventManager::Process()
{
    bool ret = mEventWithPayloads.size() > 0;
    for (const ProjectEventWithPayload& e : mEventWithPayloads)
    {
        Listeners& listeners = mEventPack[e.first];
        for (Listener l : listeners)
        {
            if (l != NULL)
                l->Resolve(e);
        }
    }
    mEventWithPayloads.clear();

    return ret;
}
```