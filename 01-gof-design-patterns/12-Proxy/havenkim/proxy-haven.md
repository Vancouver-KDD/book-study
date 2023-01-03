# Proxy

### In One Liner

Surrogate, deputy, replacement.

Manager of **Actual ConcreteObject** that restricts or provides plentiful features.

Proxy is more like an *Object's personalized manager*.

### Pros 

- Provides restriction or features to the original object by re-routing interfaces. It means Proxy Object must have same interface as its reference Object.

### Cons

- The response from the service might get delayed
- The code may become more complicated since you need to introduce a lot of new classes.
[Reference](https://refactorying.guru/design-patterns/proxy)

### IRL Example

DLL boundaries in between program is very important when the program is composited by multiple 3rd party libraries. 

The core difference of developing C++ program and C++ libraries is that you must be able to debug the code without DLL Boundary problem. It is caused by Memory alignment problem, virtual memory address dereferencing problems, etcs.

Most of private company level libraries support only Release mode dll since they do not want to reveal the actual codes by providing pdb files (program debug database)

One of the solutions to solve dll boundary problem is creating a wrapper or proxy object that hides internal Object but provide the external interfaces.

<br/>
This GeometryProxy class is a proxy object that hide the implementation of the Geometry & avoid header expose problem by declaring it as void pointer.

And also, it overrides new, delete operator to make seamless api in between Geometry and the Proxy Object, GeometryProxy
```c++
#pragma once

namespace KDDLib
{
    class KDDLIB_DLL GeometryProxy
    {
    public:
        ~GeometryProxy();
        static GeometryProxy* create();
        void* get();

        void operator delete(void*);

        const Eigen::Vector3d& vertex(uint64_t index) const;
        Eigen::Vector3d& vertex(uint64_t index);
        uint64_t nVertices();
    private:
        GeometryProxy();
        void* operator new(size_t);
        void* geometry = NULL;
    };
}
```

Inside of the GeometryProxy.cpp, it references the actual object, Geometry.

A bit difference in GeometryProxy is that it provides **create()** function to avoid constructor call from the outside of dll. Thanks to the Factory Method, it will be able to restrict the GeometryProxy Object's creation and deletion in memory without Memory conflicts.

```c++
#include "GeometryProxy.h"

#include "Geometry.h"

using namespace KDDLib;

Geometry* conv(void* ptr)
{
    return (Geometry*)ptr;
}


GeometryProxy::GeometryProxy()
{
    
}
GeometryProxy::~GeometryProxy()
{
    
}

GeometryProxy* GeometryProxy::create()
{
    GeometryProxy* newProxy = new GeometryProxy();

    // This proxy contains the concrete object (real object) inside.
    newProxy->geometry = new Geometry();

    return newProxy;
}

void* GeometryProxy::operator new(size_t size)
{
    return ::operator new(size);
}

void GeometryProxy::operator delete(void* ptr)
{
    // Delete my object
    delete conv(ptr->geometry);

    // Delete myself as well.
    ::operator delete(ptr);
}

const Eigen::Vector3d& GeometryProxy::vertex(uint64_t index) const
{
    // Re-route interface of vertex(index) to actual object
    return conv(ptr->geometry)->vertex(index);
}
Eigen::Vector3d& GeometryProxy::vertex(uint64_t index)
{
    return conv(ptr->geometry)->vertex(index);
}
uint64_t GeometryProxy::nVertices()
{
    return conv(ptr->geometry)->nVertices();
}
```

In conclusion, this library code will generate **Release Mode DLL** only. But it will manage its own memory blocks (list of Eigen::Vector3d, Geometry class itself, and GeometryProxy) and will successfully hide the implementation of the code.

No matter when this dll is being used, any creation, access or deletion are delegated to libraries DLL. 