# Iterator

### In One Liner

A Pointer lets you iterate the container and hides the implementation of the container at the same time.

Active (External) Iterator : Our common iterator that has ability to control iteration state.

Passive (Internal) Iterator : ex) map, filter, sum, or any lambda like functional programming features that iterate itself without a handle from outside. 

### Pros 

- Abstract the implementation of the container structure so that it's easy to change the container type.

### Cons

- Like an simple array or continuous memory block container, using iterator sometimes goes overkill.
- Since the iterator is still a reference to the element, depends on implementation, there is a chance of creating a bug when add & delete have been made to the container while other pointer is alive. See below c++ examples.

### Example

#### Other languages

In Python, it's much easier to use iterator with extended functionality.

```python
xs = [1, 2, 3, 4, 5, 6, 7, 8]

# Typical iterator
for x in xs:
    print(x)

# Get index and the element at the sametime
for idx, x in enumerate(xs):
    print(idx, x)
```

In CPP, const_iterator prohibits any modification to the element

```c++
#include <vector>

int main()
{
    std::vector<int> l = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    std::vector<int>::const_iterator iter = l.begin();
    // auto will assign normal iterator
    // auto iter = l.begin();   // std::vector<int>::iterator iter = l.begin();

    for (; iter != l.end(); l++)
    {
        // Compile error
        *iter = 10;    
    }
}
```

From C++11, C++ also supports Range-based for loop

```c++
#include <vector>

int main()
{
    std::vector<int> l = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    // Internally, it is integrated using iterator class.
    // https://en.cppreference.com/w/cpp/language/range-for
    for (int& data : l)
    {
        std::cout << data << std::endl; 
    }
    
    // const iterator
    for (const int& data : l)
    {
        std::cout << data << std::endl; 

        // Compile error
        data = 10;
    }
}
```

The possible exploit point of the iterator is that since it is a pointer (or cursor) of an element, storing the reference of the element will likely generate a potential behavior bug.

```c++
#include <vector>
#include <iostream>

int main(void)
{
    std::vector<int> l = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    // Have a pointer of the element at 5th. 
    int& target = &(l.begin() + 4);

    std::cout << target << std::endl;

    // Remove 3rd element
    l.erase(l.begin() + 2);

    std::cout << target << std::endl;
    return 0;
}
```

Output is 

>5
>6


In Rust, since the language design stricts mutation of the elements unless it is needed, it supports three variations. [Reference](https://doc.rust-lang.org/rust-by-example/flow_control/for.html)

```rust
let names = vec!["Haven", "Ken", "Kyle"];

// 1. Normal iterator
for name in names.iter() {
    match name {
        &"Haven" => println!("There is a raacker among us!"),
        _ => println!("Hello {}", name),
    }
}
println!("names: {:?}", names);
// Hello Bob
// Hello Frank
// There is a rustacean among us!
// names: ["Bob", "Frank", "Ferris"]

// 2. move the list into the loop and it is no longer available onward.
for name in names.into_iter() {
    match name {
        &"Haven" => println!("There is a raacker among us!"),
        _ => println!("Hello {}", name),
    }
}
// Compile Error!
//println!("names: {:?}", names);


let mut names = vec!["Haven", "Ken", "Kyle"];

// 3. Let you mutate the list through iterator
for name in names.iter_mut() {
    *name = match name {
        &mut "Haven" => println!("There is a raacker among us!"),
        _ => "Hello",
    }
}

println!("names: {:?}", names);
//names: ["Hello", "Hello", "There is a raacker among us!"]
```