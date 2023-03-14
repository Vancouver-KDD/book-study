# Concurrency

### Why Concurrency?
- Concurrency is a decoupling strategy. It helps us decouple _what_ gets done from _when_ it gets done.
- Decoupling _what_ from _when_ can dramatically improve both the throughput and structures of an application.

### Myths and Misconceptions
- Common myths and misconceptions:
  * _Concurrency always improves performance._
  * _Design does not change when writing concurrent programs._
  * _Understanding concurrency issues is not important when working with a container such as a Web of EJB container._
- Balanced sound bites
  * _Concurrency incurs some overhead_, both in performance as well as writing additional code.
  * _Correct concurrency is complex_, even for simple problems.
  * _Concurrency bugs aren't usually repeatable_, so they are often ignored as one-offs instead of the true defects they are.
  * _Concurrency often requires a fundamental change in design strategy._

### Challenges
```java
public class X {
  private int lastIdUsed;

  public int getNextId() {
    return ++lastIdUsed;
  }
}
```
- Thread one gets the value 43, thread two gets the value 44, `lastIdUsed` is 44.
- Thread one gets the value 44, thread two gets the value 43, `lastIdUsed` is 44.
- Thread one gets the value 43, thread two gets the value 43, `lastIdUsed` is 43.

### Concurrency Defense Principles
##### Single Responsibility Principle
- Keep your concurrency-related code separate from other code

##### Corollary: Limit the Scope of Data
- Take data encapsulation to heart: severely limit the access of any data that may be shared. (ie: `synchronized`)

##### Corollary: Use Copies of Data
- A good way to avoid shared data is to avoid sharing the data in ther first place.
- Copy objects and treat them as read-only.
- Copy objects, collect results from multiple threads in these copies and then merge the results in a single thread.

##### Corollary: Threads Should Be as Independent as Possible
- Attempt to partition data into independent subsets thatn can be operated on by independent threads, possibly in different processors.

### Know Your Library
- Java 5 offeres many improvements for concurrent development over previous versions.
  * Use the provided thread-safe collections.
  * Use the executor framework for executing unrelated tasks.
  * Use non-blocking solutions when possible.
  * Several library classes are not thread safe.

##### Thread-Safe Collections
- The collections in `java.util.concurrent` package are safe for multithreaded situations and they perform well.
- Review the classes available to you. In the case of Java, become familiar with `java.util.concurrent`, `java.util.concurrent.atomic`, `java.util.concurrent.locks`

### Know Your Execution Models
- Bound Resources
  * Resources of a fixed size or number used in a concurrent environment. Examples include database connections and fixed-size read/write buffers.
- Mutual Exclusion
  * Only one thread can access shared data or a shared resource at a time.
- Starvation
  * One Thread or a group of threads is prohibited from proceeding for an excessively long time of forever. For example, always letting fast-running threads through first could starve out longer running threads if there is no end to the fast-running threads.
- Deadlock
  * Two or more threads waiting for each other to finish. Each thread has a resource that the other thread requires and neither can finish until it gets the other resource.
- Livelock
  * Threads in lockstep, each trying to do work but finding another "in the way." Due to resonance, threads continue trying to make progress but are unable to for an excessively long time or forever.

##### Producer-Consumer
- One or more _producer_ threads create some work and place it in a buffer or queue. One or more _consumer_ threads acquire that work from the queue and complete it. The queue between the producers and consumers is a **bound resource.**
- Both potentially wait to be noticed when they can continue.

##### Readers-Writers
- Emphasizing throughput can cause starvation and the accumulation of stale information.
- Allowing updates can impact throughput.
- Finding that balance and avoiding concurrent update issues is what the problem addresses.

##### Dining Philosophers
- Unless carefully designed, systems that compete in this way can experience deadlock, livelock, throughput, and efficiency degradation.
- Learn these basic algorithms and understand their solutions.

### Beware Dependencies Between Synchronized Methods
- Avoid using more than one method on a shared object(`synchronized`).
- When you must use more than one method on a shared object:
  * **Client-Based Locking**: have the client lock the server before calling the first method and make sure the lock's extent includes code calling the last method.
  * **Server-Based Locking**: Within the server created a method that locks the server, calls all the methods, and then unlocks. Have the client call the new method.
  * **Adapted Server**: create an intermediary that performs the locking. This is an example of server-based locking, where the original server cannot be changed.

### Keep Synchronized Sections Small
- The `synchronized` keyword introduces a lock. Locks are expensive because they create delays and add overhead.
- On the other hand, critical sections must be guarded. So we want to design out code with as few critical sections as possible.

### Writing Correct Shut-Down Code Is Hard
- Graceful shutdown can be hard to get correct. Common problems involve deadlock, with threads waiting for a signal to continue that never come(Producer/consumer pair).
- Think about shut-down early and get it working early. It's going to take longer than you expect. Review existing algorithms because this is probably harder than you think.

### Testing Threaded Code
- Testing does not guarantee correctness. However, good testing can minimize risk.
- Write tests that have the potential to expose porblems and then run them frequently, with different programmatic configurations and system configurations and load. If test ever fail, track down the failure. Don't ignore a failure just because the tests pass on a subsequent run.
- Fine-grained recommendations:
  * Treat spurious failures as candidate threading issues.
  * Get your non-threaded code working first.
  * Make your threaded code pluggable.
  * Make your threaded code tunable.
  * Run with more threads than processors.
  * Run on different platforms.
  * Instrument your code to try and force failures.

##### Treat Spurious Failures as Candidate Threading Issues
- Do not ignore system failures as one-offs.

##### Get Your Non-threaded Code Working First
- Do not try to chase down nonthreading bugs and threading bugs at the same time. Make sure your code works outside of threads.

##### Make Your Threaded Code Pluggable
- Various configurations
  * One thread, several threads, varied as it executes
  * Threaded code interacts with something that can be both real or a test double.
  * Execute with test doubles that run quickly, slowly, variable
  * Configure tests so they can run for a number of iterations.
- Make your thread-based code especially pluggable so that you can run it in various configurations.

##### Make Your Threaded Code Tunable
- Getting the right balance of threads typically requires trial an error.
- Early on, find ways to time the performance of your system under different configurations.
- Allow the number of threads to be easily tuned.

##### Run with More Threads Than Processors
- Things happens when the system switches between tasks.
- The more frequently your tasks swap, the more likely you'll encounter code that is missing a critical sections or causes deadlock.

##### Run on Different Platforms
- Different operating systems have different threading policies, each of which impacts the code's execution.
- Multithreaded code behaves differently in different environments.
- Run your threaded code on all target platforms early and often.

##### Instrument Your Code to Try and Force Failures
- The reason that threading bugs can be infrequent, sporadic, and hard to repeat, is that only a very few pathways out of the many thousands of possible pathways through a vulnerable section actually fail. This makes detection and debugging very difficult.
- you can instrument your code and force it to run in different orderings by adding calls to methods like `Object.wait()`, `Object.sleep()`, `Object.yield()` and `Object.priority()`.
- There are two options for code instrumentation:
  * Hand-coded
  * Automated

##### Hand-Coded
```java
public synchronized String nextUrlOrNull() {
  if (hasNext()) {
    String url = urlGenerator.next();
    Thread.yield(); // inserted for testing
    updateHasNext();
    return url;
  }
  return null;
}
```
- The inserted call to `yield()` will change the execution pathways taken by the code and possibly cause the code to fail where it did not fail before.
- If the code does break, it was not because you added a call to `yield()`.
- Problems:
  * You have to manually find appropriate places to do this.
  * How do you know where to put the call and what kind of call to use?
  * Leaving such code in a production environment unnecessarily slows the code down.
  * It's a shotgun approach. You may or may not find flaws. Indeed, the odds aren't with you.
- What we need is a way to do this during testing but not in production.
- If we divide our system up into POJOs that know nothing of threading and classes that control the threading, it will be easier to find appropriate places to instrument the code.

##### Automated
- You could use tools like an Aspect-Oriented Framework, CGLIB, or ASM to programmatically instrument your code.

```java
public synchronized String nextUrlOrNull() {
  if (hasNext()) {
    ThreadJiglePoint.jiggle();
    String url = urlGenerator.next();
    ThreadJiglePoint.jiggle();
    updateHasNext();
    ThreadJiglePoint.jiggle();
    return url;
  }
  return null;
}
```
- Now you use a simple aspect that randomly selects among doing nothing, sleeping, or yielding.
- If you run your tests a thousand times with random jiggling, you may root out some flaws. If the tests pass, at least you can say you've done due diligence.
- The combination of well-written tests and jiggling can dramatically increase the change finding errors.

### Conclusion
- If you are faced with writing concurrent code, you need to write clean code with rigor or else face subtle and infrequent failures.
- First and foremost, follow the Single Responsibility Principle.
- Know the possible resources of concurrency issues
  * Multiple threads operating on shared data
  * Using a common resource pool
  * Shutting down cleanly
  * Finishing the iteration of a loop
- Learn your library and know the fundamental algorithms.
- Learn how to find regions of code that must be locked and lock them.
- You need to be able to run your thread-related code in many configurations on many platforms repeatedly and continuously.
- You will greatly improve your chances of finding erroneous code if you take the time to instrument your code.

> If you take a clean approach, your chances of getting it right increase drastically.
