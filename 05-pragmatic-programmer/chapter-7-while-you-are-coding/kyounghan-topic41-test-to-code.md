# Test to Code
- Compared with past, many developers are writing test and are thinking test is important. 
- Even though that, following questions are important.
  - Why we shoud write tests?
  - What do we think is important about testing?
  - How do we think you should go about it?
  
> Tip 66 : Testing Is Not About Finding Bugs
-  
  

## Thinking About Tests
- Write new code
  - Queries the database to return a list of people who watch more than 10 videos a week on you "world's funniest dishwashing videos" site
  - Start to code like following
```
def return_avid_viewers do
# ... hmmm ...
end
```
### Stop!
  - How do you know that what you're about to do is a good thing?
  - You can't know that
  - No one can know
  - But thinking about tests can make it more likely.

### Start by imagining to test code
- Finish writing the function and have to test it
- For proper test, you need a database instance you can control rather than using some global one, as that allows to change it while testing

```
def return_avid_users(db) do
```
- How we'd populate the test data?
- The requirement
  - list of people who watch more than 10 videos a week
  - So we look at the database schema for fields that might help
  - Two likely fieds in a table
    - who-watched
    - waht : opened_video and completed_video
  - Let's just cheat and pass in the name of field (not exactaly name of field)
```
def return_avid_users(db, qualifying_field_name) do
```
- Before writing a line of code, two discoveries are made already by thinking about the tests

## Tests Drive Coding
- Thinking about testing made two decision
  - Reduce coupling - by passing in a database connection rather than using a global one
  - Increase flexibility - by making the name of the field we test a parameter
- Thinking about writing a test make us look at it from the outside
  - As if we were a client of the code
  - Not its author

> Tip 67: A Test Is The First User Of Your Code

#### Biggest benefit offered by testing
- A test is the first user of your code
- Testing is vital feedback that guides your coding

- A function or method that is tightly coupled to other code is hard to test
  - Have to set up all that environment before run method
  - Making code testable also redueces its coupling

#### Another things to be considered one by one during during coding
- All all the code to support the boundary conditions
- Error handling
- Performance
  - Code ends up five times longer than it should because it's full of conditional logic and special cases

#### With test , things become clearer
- By thinking the testing boundary conditions and how that will work before start coding, the possibility to find out the patterns in the logic that'll simplify the function will go up
- If you think about the error conditions you'll need to test, you'll structure your function accordingly.

### Test-Driven Development
#### The basic cyle of TDD
1. Decie on a small piece of functionality you want to add.
2. Write a test that will pass once that functionality is implemented
3. Run all tests. Verify that the only failure is the one you just wrote.
4. Write the smallest amount of code needed to get the test to pass, and verify that the tests now run cleanly
5. Refactor you code: see if there is a way to improve on what you just wrote(te test or the function). Make sure the tests will pass when you're done

- The idea is that this cycle should be very short (a matter of minutes)
- Constanlty writing tests and then getting them to work

#### Good impact from TDD
- If you follow the TDD workflow, you'll guarantee that you always have tests for your code

#### Bad impact from TDD
- The people who become salves to TDD
  - Spend inordinate amounts of time ensuring that they always have 100% test coverage
  - Have lots of redundant tests   
  - Their designs tend to start at the bottom and work their way up

> Tip 68: Build End-to-End, Not Top-Down or Buttom Up


## TDD: You Need To Know Where You're Going
### Old Joke
- How do you eat an elephant?
  - Punchline - One bite at a time
  - It is often touted as a benefit of TDD

### Just doing TDD cannot solve the problem
- Consider when you can't comprehend the whole problem, take small steps one test at a time
- However, this approache can mislead you
- Encouraging you to focus on and endlessly polish the easy problems while ignoring the real reason you're coding

### Interesting Example, Ron Jeffries
- A leading figure in the gaility movement, started a series of blog posts which documented his test-driven coding of a Sudoko solver in 2006
- After five posts, he'd refined the representation of the underlying board, refactoring a number of times until he was happy.
- But then he abandoned the project
- You can watch how a clever person can get sidetracked by the minutia, reinforced by the glow of passing tests.

### Another exmple, Peter Norvig
- Describes an alternative approach which feels very different in character: rather than being driven by tests, he starts with a basic understanding of how these kinds of problems are traditionally solved.
- Focuses on refining this algorithm

#### Tests can definitely help drive development. But, as with every drive, unless you have a destination in mind, you can end up going in circles.

## Back to The Code
#### Component-based development 
- The idea is that generic software components should be available and combined just as easily as common integrated circuits(ICs) are combined.
- But this works only if the components you are using are known to be reliable, and if you have common voltages, interconnect standards, timing and so on.

#### Hardware test
- Chips are designed to be tested
- BIST(Built-I  Self Test)
  - More complex chips and systems may have
  - Runs some base-level diagnostics internally, or a Test Access Mechanism(TAM) that provides a test harness that allows the external environment to provide stimuli and collect responses from the chip
  
Adapt this concept to Software development

## Unit Testing
- Chip-level testing for hardware is roughly equivalent to unit testing in software
  - Testing done on each module, in isolation, to verify its behavior
- A software unit test is code that exercises a module
- Later, when we assemble a 'software ICs' into a complete system
- We'll have confidence that the individual parts work as expected, and then we can use the same unit test facilities to test the system as a whole
- Need to decide what to test at the unit level

## Testing Against Contract
- We like to hink of unit testing as testing against contract.
- This means two things
  - Whether the code meets the contract
  - Whether the contract means what we think it menas
- We want to test that the module delivers the functionality it promises
  - Over a wide range of test cases
  - Boundary conditions

### Practical meaning of promising the contract
#### Numerical example : a squre root routine
- Documented contract is simple
```
pre-conditions:
    argument >= 0;

post-considitons:
    ((result * result) - argument).abs <= epsilon * argument;
```

#### Wat to test for this contract
- Pass in a negative argument and ensure that it is rejected
- Pass in an argument of zero to ensure that it is accepted (this is the boundary value)
- Pass in values between zero and maximum expressible argument and verify that the difference between the square of the result and the original argument is less than some small fraction of the argument(epsilon)


#### Basic test script to exercise the square root function
```
assertWithinEpsilon(my_sqrt(0),0)
assertWithinEpsilon(my_sqrt(2.0), 1.4142135624)
assertWithinEpsilon(my_sqrt(64,0), 8.0)
assertWithinEpsilon(my_sqrt(1.0e7), 3162.2776602)
assertRaisesException fn=> my_sqrt(-4.0) end
```

#### Another aspect to consider
- This is pretty simple test
- But in the real world, any nontrivial module is likeply to be dependent on a number of other modules. Then how do we go about testing the combination?
- Suppose we have a module A that uses a DataFeed and a LinearRegression. We would test
  - DataFeed's contract, in full
  - LinearRegression's contract, in full
  - A's contract, which relies on the other contracts but does not directly expose them
- This style of testing requires you to test subcomponents of a moudle first.
  - One the subcomponents have been verified, then the module itself can be tested. 

> Tip 69: Design To Test

## Ad Hoc Testing
- Ad-hoc testing is when we run poke at our code manually.
- This may be as simple as a console.log(), or a piece of code entered interactively in a debugger, IDE environment, or REPL.
- At the end of the debugging session, you need to formalize this ad hoc test to run again if the code broken

## Build A Test Window
- Even the best sets of tests are unlikely to find all the bugs
- Test is needed to run often, it has beeen deployed - with real-world data flowing though its veins
  
### Using log file
- Log messages should be in a regular, consistent format
  - You may want to parse them automatically to deduce processing time or logic paths 

### Hot-key sequence or magic URL
- When this particular combination of keys is pressed, or the URL is accessed, a diagnostic control window pops up with status mesages and so on
- This isn't for end users but for help desk
  
## A Culture Of Testing
- All software you write will be tested
  - you, your team and user in the end
- A little forethought can go a long way toward minimizing maintenance costs and help-desk calls

### Choices for test
- Test First
  - Best choice in most circumstances
- Test During
  - When test first is not convenient or userful
  - Test During coding can be a good fallback
- Test Never
  - The worst choice

### All the test pass all the time
- Ignore a spew of tests that 'always fail' makes it easier to ignore all the tests and the vicious spiral begins

### Test code & production code
- Treat test code with the same care as any production code
- Keep it decoupled, clean, and robust
- Don't rely on unrealiable things
  - The absolute position of widgets in a GUI system.
  - Exact timestamps in a server log
  - Exact wording of error messages

> Tip 70: Test Your Software, or Your Users Will

- Make no mistake, testing is part of programming.
- It's not something left to other departments or staff
- Testing, desing, coding -it's all programming

