#  Singleton

### In One Liner

Create once, use anywhere.

But the primitive way of creating Singleton might be not enough to cover most of problems it would encounter.

### Pros 

- Secure lifecycle of the Object to be only once. Prohibit any secondary instances so that one unique object manages related attributes and logics.  

### Cons

- Over-use of singleton instance might lead to chaotic spaguetti logic since the access to each method is open to global and is out of scope that the instance can handle. 

### Sample

The most important aspect of Singleton is having an unique object. But the time of memory allocation & initialization could be a problem, depends on languages, compiler or OS.

NaiveSingleton.h cannot provide thread-safe access because the creation time of the object is not certain.

**NaiveSingleton.h**
```c++
class FooSingleton{
public:
  static FooSingleton* I();
private:
  FooSingleton()= default;
  ~FooSingleton()= default;
  FooSingleton(const FooSingleton&)= delete;
  FooSingleton& operator=(const FooSingleton&)= delete;

  static SingleInstance* mpSingleInstance = NULL;
};
```

**NaiveSingleton.cpp**
```c++
FooSingleton* FooSingleton::I()
{
    if (mpSingleInstance == NULL)
        mpSingleInstance = new FooSingleton();
    return mpSingleInstance;
}
```

In C++11, internal static variable can assure the creation time of the object to be program on-memory time and it can secure thread-safetyness.

**MeyerSingletonCPP11.h**
```c++
class FooSingleton{
public:
  static FooSingleton& I();
private:
  FooSingleton()= default;
  ~FooSingleton()= default;
  FooSingleton(const FooSingleton&)= delete;
  FooSingleton& operator=(const FooSingleton&)= delete;
};
```

**MeyerSingletonCPP11.cpp**
```c++
FooSingleton& FooSingleton::I()
{
    static FooSingleton instance;
    return instance;
}
```

[Reference 1](https://www.modernescpp.com/index.php/thread-safe-initialization-of-a-singleton)
[Reference 2](https://programmer.ink/think/summary-of-c-thread-safety-singleton-patterns.html)