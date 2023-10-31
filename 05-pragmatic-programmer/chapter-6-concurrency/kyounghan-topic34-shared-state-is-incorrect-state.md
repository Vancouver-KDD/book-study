# Shared State is Incorrect State
### Short story in diner
- In diner, after main course, you ask your server if there's any apple pie left.
- The server check it in the display case and says yes
- You order it.
- At the same time, on the otehr side of the restaurant, another customer, also asks same question to another server.
- The server also check it in the display case and say yes.
- The cusotmer order
- One of the customers is going to be disappointed. 

### Shared state
- The problem is the shared state
- It adapted many cases like bank, sales and so on.

> Tip 57 : Shared State Is Incorrect State

## Nonatomic updates
#### Code for the diner example.
- The two waiters operate concurrently (and in real life, in parallel)
```
if display_case.pie_count > 0
    promise_pie_to_customer()
    display_case.take_pie()
    give_pie_to_customer()
end
```
- Waiter 1 get the current pie count, and finds that it one
- He promises the pie to the customer
- Waiter 2 get the current pie count, and finds that it one
- He promises the pie to the customer
- One of the two then grabs the last piece of pie, and the other waiter enters some kind of error state.

#### The problem for this example
- The problem here is not that two processes can write to the same memory
- The problem is that neither process can gurantee that its view of that memory is consistent.
- display_case.pie_count() -> For watier
  - the copy of the value from the display case into their own memory
  - If the value in the display case changes, their memory is now out of date
  - *This is all because the fetching and then updating the pie count is not an atomic operation*

*How can we make it atomic?*

### Semaphores and Other Forms of Mutual Exclustion
#### Semaphore
  - Semaphore is simply a thing that only one person can own at a time
  - Create a semaphore and then use it to control access to some other resource.
  - Example
    - Create a semaphore to control access to the pie case
    - Adopt convetion that anyone who wants to update the pie case contents can only do so if they are holding that semaphore.

#### Code for example with Semaphore
- Classically
  - The operation to grab the semaphore was called P (lock, claim)
  - The operation to release it was called V (unlock, release)
```
case_semaphore.lock()

if display_case.pie_count > 0
    promise_pie_to_customer()
    display_case.take_pie()
    give_pie_to_customer()
end

case_semaphore.unlock()
```
- Code assumes that a semaphore has already been created and stored in the variable *case_semaphore*.
- Problems
  - Still some problems with this approach
  - The most significant is that it only works becasue everyone who accesses the pie case agrees on the convention of using the semaphore. 
  - If someone forgots (does not follow the convention), then back in chaos.

### Make the Resource Transactional 
- The current design is poor because it delegates responsibility for protecting access to the pie case to the people who use it.

#### Solution
- Change it to centralize that control
- Have to change the API so that watiers can check the count and also take a slice of pie in a single call

#### Improvement #1
```
slice = display_case.get_pie_if_available()
if slice
    give_pie_to_customer()
end
```
```
def get_pie_if_available()      ####
    if @slices.size > 0         #
        update_sales_data(:pie) #
        return @slices.shift    #
    else        # incorrect code!
        false   #
    end         #
end             #####
```
- Above code still have a common misconception
- Resource access is moved to a central place, but the method still can be called from multiple concurrent threads. So need to proect it with a semaphore.

#### Improvement #2
```
def get_pie_if_available()
  @case_semaphore.lock()

  if @slices.size > 0
    update_sales_data(:pie)
    return @slices.shift
  else
    false
  end
  
  @case_semaphore.unlock()
end
```
- Even this code might not be correct
- If update_sales_data raises an exeception, the semaphore will never get unlock
- All future access to the pie case will hanging indefinitely.

#### Improvement #3

```
def get_pie_if_available()
  @case_semaphore.lock()
  try {
    if @slices.size > 0
      update_sales_data(:pie)
      return @slices.shift
    else
      false
    end
  }
  ensure {
    @case_semaphore.unlock()
  }
end
```
- Also many langauges provide libraries that handle semaphore like below. 

```
def get_pie_if_available()
  @case_semaphore.protect() {
  if @slices.size > 0
    update_sales_data(:pie)
    return @slices.shift
  else
    false
  end
}
end
```

## Multiple Resource Transactions
- Order a new pie : *a la mode*
  - Consist of pie and ice bream

```
slice = display_case.get_pie_if_available()
scoop = freezer.get_ice_cream_if_available()

if slice && scoop
  give_order_to_customer()
end
```
#### Possible situation
- This won't work
- Having a pie but there might be no scoop of ice cream
- Then we're left holding some pie that we can't do anything with(because we also must have a ice cream)
- It also blocking the order of customer who just want a pie 

#### Solution #1
- Fix this by adding a method to the case that let us return a slice of pie
- Need to add exception handling to ensure we don't keep resource if something failes

```
slice = display_case.get_pie_if_available()
if slice
try {
    scoop = freezer.get_ice_cream_if_available()
    if scoop
    try {
        give_order_to_customer()
    }
    rescue {
        freezer.give_back(scoop)
    }
    end
}
rescue {
  display_case.give_back(slice)
}
end
```
- Still, this is less than ideal
- Code is now really ulgy
  - Working out what it actually does is difficult
  - The business logic is buried in all the housekeeping.

##### Cause of this issue
- Previously we fixed this by moving the resource handling code into the resource itself
- Here, we have two resources.
- Which place is best one to put the resource handling code, display case or freezer?
  - Both 'No'
- Might be need to create "New Resource"
  - apple pie a la mode
- Of course, in the real world there are likely to be many composite dishes like this.

## Non-Transactional updates
- A lot of attention is given to shared memory as a source of concurrency problem.
- But the problem can pop up anywhere where mutable resources are used
  - Files
  - Databases
  - External services

### Example of another case
- While writing the book we updated the toolchain to do more work in parallel using thread.
- One thread in tool are trying to access file and directoy which is modifed by another thread.
- Exceptional cases are raised.


> Tip 58 : Random Failures Are Often Concurrency Issues


## Other Kinds of Exclusive Access
- Most language have library support for some kind of exclusive access to shared resources. 
  - mutexes(for mutual exclusion), monitors, semaphores
- Some languages have concurrency support built into the language itself.
  - Rust
- Functional languages
  - Its tendency to make all data immutable, make concurrency simpler
  - However, they still face the same challenges, because at some point they are forced to step into the real, mutable world 

## Doctor, It Hurts...
- Concurrency in a shared resource environment is difficult
- Managing it yourself is graught with challenges
- Next sections, alternative ways of getting the benefits of concurrency without the pain


