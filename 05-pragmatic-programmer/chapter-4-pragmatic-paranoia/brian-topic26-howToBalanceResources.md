# How to Balance Resources
- We all manage resources whenever we code: memory, transactions, threads, network connections, files, timers—all kinds of things with limited availability.

## Tip 40 -  Finish What You Start
- Example of Tightly Coupled Functions
```
def read_customer
    @customer_file = File.open(@name + ".rec", "r+")
    @balance       = BigDecimal(@customer_file.gets)
end

def write_customer
    @customer_file.rewind
    @customer_file.puts @balance.to_s
    @customer_file.close   
end

def update_customer(transaction_amount)
    read_customer
    @balance = @balance.add(transaction_amount,2)
    write_customer   
end
```
- The problem
```
def update_customer(transaction_amount)
    read_customer
    if (transaction_amount >= 0.00)
      @balance = @balance.add(transaction_amount,2)
      write_customer
    end
end
```
- Bad solution - This is not balanced!
```
def update_customer(transaction_amount)
    read_customer
    if (transaction_amount >= 0.00)
        @balance += BigDecimal(transaction_amount, 2)
        write_customer
    else
      @customer_file.close # Bad idea!
    end
end
```
```
def read_customer(file)
    @balance=BigDecimal(file.gets)
end

def write_customer(file)
    file.rewind
    file.puts @balance.to_s
end

def update_customer(transaction_amount)
    file=File.open(@name + ".rec", "r+")            # >--
    read_customer(file)                             #    |
    @balance = @balance.add(transaction_amount,2)   #    |
    file.close                                      # <--   
end
```
```
def update_customer(transaction_amount)
    File.open(@name + ".rec", "r+") do |file|           # >--
        read_customer(file)                             #    |
        @balance = @balance.add(transaction_amount,2)   #    |
        write_customer(file)                            #    |
    end                                                 # <--   
end
```
## Act Locally
### Nest Allocations
- There are just two more suggestions:
  1. Deallocate resources in the opposite order to that in which you allocate them. That way you won’t orphan resources if one resource contains references to another.
     - Open A -> Open B -> Open C -> Close C -> Close B -> Close A 
  2. When allocating the same set of resources in different places in your code, always allocate them in the same order. This will reduce the possibility of deadlock. (If process A claims resource1 and is about to claim resource2, while process B has claimed resource2 and is trying to get resource1, the two processes will wait forever.)
     - Time 1: Thread A locks resource1 and Thread B locks resource2
     - Time 2: Thread A claims resource2 and Thread B claim resource1

## BALANCING AND EXCEPTIONS 
- If an exception is thrown, how do you guarantee that everything allocated prior to the exception is tidied up? The answer depends to some extent on the language support. You generally have two choices:
  1. Use variable scope (for example, stack variables in C++ or Rust)
  2. Use a finally clause in a try…catch block

- Example 1
```
{     
    let mut accounts = File::open("mydata.txt")?; // >--     
    // use 'accounts'                             //    |
    ...                                           //    |
}                                                 // <--   
// 'accounts' is now out of scope, and the file is  automatically closed
```
- Example 2
```
try
     // some dodgy stuff
catch
     // exception was raised
finally
     // clean up in either case
```

### An Exception Antipattern 
```
begin
    thing = allocate_resource()
    process(thing)
finally
    deallocate(thing)
end
```
VS
```
thing = allocate_resource()
begin
    process(thing)
finally
    deallocate(thing)
end
```

## WHEN YOU CAN’T BALANCE RESOURCES
- You need to decide who is responsible for data in an aggregate data structure.
    1. The top-level structure is also responsible for freeing any substructures that it contains. These structures then recursively delete data they contain, and so on.
    2. The top-level structure is simply deallocated. Any structures that it pointed to (that are not referenced elsewhere) are orphaned.
    3. The top-level structure refuses to deallocate itself if it contains any substructures.

- Implementing any of these options in a procedural language such as C can be a problem: data structures themselves are not active.
  - Our preference in these circumstances is to write a module for each major structure that provides standard allocation and deallocation facilities for that structure.

## CHECKING THE BALANCE 
1. Producing wrappers for each type of resource, and using these wrappers to keep track of all allocations and deallocations.
    - Services requests will probably have a single point at the top of its main processing loop where it waits for the next request to arrive.
2. 3rd party solutions

## CHALLENGES
- Although there are no guaranteed ways of ensuring that you always free resources, certain design techniques, when applied consistently, will help. 
- In the text we discussed how establishing **a semantic invariant** for major data structures could direct memory deallocation decisions. 
- Consider how Topic 23, Design by Contract, could help refine this idea.
