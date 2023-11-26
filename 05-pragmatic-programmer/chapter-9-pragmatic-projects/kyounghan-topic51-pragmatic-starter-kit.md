# Pragmatic Starter Kit


### Model-T & modern cars
- Model-T
  - instruction for starting a Model-T Ford were more than two pages long
- Modern cars
  - Just push a button
  - The starting procedure is automatic and foolproof
  - Easy to start
  - The automatic starter won't flood the engine

### What we expect about software development these days
- Software development is still at Model-T stage
  - But we can't afford to go through two pages of instructions again and again for some common operation
- Automatic & repeatable
  - Automatic
    - It has to be automatic and repeatable on any capable machine.
    - Build, release procedure, testing, project paperwork, ...
  - Repeatable
    - We want to ensure consistency and repeatability on the project
    - Manual procedures leave consistency up to chance
      - Repeatablility isn't guaranteed
      - Especially, Manual precedure is open to interpretation by different people

### What are the most basic, most important element that every team needs regardless of methodology, language, or technology stak
- Version Control
- Regression Testing
- Full Automation

## Drive With Version Control
- Keep everything needed to build your project under version control
  - This idea becomes even more important in the context of the project itself

### Version control allows build machine to be ephemeral
- Build machines and/or clusters are created on demand as spot instances in the cloud
- Deployment configuration is under version control as well.
- Releasing to production can be handled automatically.
- At the project level, version control drives the build and release process

> Tip89: Use Version Control to Drive Builds, Tests, and Releases

- Build, test, and deployment are tigerred via commits or pushes to version control.
  - Built in container in the cloud
- Release to staging or production is specified by using a tag in version control system.
- Release then become a much more low cost
- Makes true continuous delivery, not tied to any one build machine or developer's machine

## Ruthless and Continuous Testing

### Test by who
- Test by developer
  - Many developers test gently, subconsciously knowing where the code will break and avoiding the weak spots
- Test by Pragmatic Programmers
  - Find gus now, so do not have to endure the shame of others finding the bugs later

### Finding bugs
- Somewhat like fishing with a net
- Fine, small nets (unit test) to catch the minnows
- Big, coarse nets(integration tests) to catch the killer sharks
- Sometimes the fish manage to escape, so we patch any holes that we find, in hopes of catching more and more slippery detects.
- Tiny minnows have a nasty habit of becoming giant, man-eating sharks pretty fast, and catching a shark is quite a bit harder.
- So we write unit tests. A lot of unit tests

> Tip90: Test Early, Test Often, Test Automatically

### Project & Test
- Good project may well have more test code than production code.
- The time it takes to produce this test code is worth the effort
- It ends up being much cheaper in the long run
- You actually stand a chance of producing a product with close to zero defects
- Test gives you a high degree of confidence that a piece of code is done

> Tip91: Coding Ain't Done 'Til All the Tests Run

### Build & Test
- Automatic build runs all available tests
- Important : Test for real
  - Test environment should match the production environment closely
  - Any gaps are where bugs breed
- Build may cover several major types of software testing
  - Unit 
  - Integration
  - Validation and verification
  - Performance 
  - This is by no means complete, some specialized projects will require various other types of testing as well.

### Unit testing
- Code that exercises a module
- The foundation of all the other forms of testing 
- All of the modules you are using must pass their own unit test before you can proceed
- Once all the pertinent modules have passed their individual test, ready for the next stage

### Integration Testing
- Test how all the modules are and interact with each other throughout the system
- Shows that the major subsystems that make up the project work and play well with each other
- With good contracts in place and well tested, any integration issues can be detected easily.
- Just an extension of the unit testing
  - Just testing how entire subsystems honor their contracts

### Validation and Verification
- Important questions
  - Users told you what they wanted, but is it what they needed?
- Does it meet the functional requirements of the system?
  - Need te tested
- Be conscious of end-user access patterns and how they differ from developer test data

### Performance Testing
- Performance or stress testing
- Ask yourself if the softare meets the performance requirements under real-world conditions-with the expected number of users, or connections, or transactions per second
- Is it scalable?
- Specialized testing hardward or software to simulate hte load realistically.

### Testing the Tests
- Because we can't write perfect software, it follows that we can't write perfect test software either.
- We need to test the tests
- After you have writeen a test to detect a particular bug, cause the bug deliberately and make sure the test complains

> Tip92: Use Saboteurs to Test Your Testing

### Testing Thoroughly
- How do you know if you have tested the code base thoroughly enough?
- Short answer : You Don't, and you never will
- Coverage analysis tools
  - Can see which lines of code have been executed during test
- The number of states that your program may have
  - States are not equivalent to lines of code
  - Suppose a function that takes two integers, each number can be 0 to 999
```
int test(int a, int b) {
    return a / (a+b);
}
```
  - In theory, this three-line function has 1,000,000 logical states
    - 999,999 will work correctly
    - One will not, when a + b == 0

> Tip93: Test State Coverage, Not Code Coverage

### Property-Based Testing
- How your code handles unexpected states 
  - To have a Computer generate those states
- Property-based testing 
  - To generate test data according to the contracts and invariants of the code under test

## Tightening The Net
- If a bug slips through the net of existing tests, you need to add a new test to trap it next time
  - The single most important concept in testing.
  - The automated tests should be modified to check for that particular bug from then on
    - Every time
    - With no exceptions
    - No matter how trivial
    - No matter how much the developer complains and says, "Oh, that will never happend again."
    - Because it will happend again
> Tip 94: Find Bugs Once

## Full Automation
- Whether you use something as simple as shell scripts with rsync and ssh, or full-featured solutions such as Ansible, Puppet, Chef, or Salt
- Just Don't Rely On Any Manual Intervention
- People just are not as repeatable as computers are
- Nor should we expect them to be.
- A shell script or program will execute the same instructions, in the same order, time after time. 
- Everything depends on automation.
- You can't build the project on an anonymous cloud server unless the build is fully automatic. 
- You can't deploy automatically if there are manual steps involved. 
- And once you introduce manual steps ("just for this one part...") you've broken a very large window. 

> Tip 95: Don't Use Manual Procedures

> With version control, ruthless testing, and full automation, your project will have the firm foundation you need so you can concentrate on the hard part: delighting users

## Challenges
- Are your nightly or continuous builds automatic, but deploying to production isn’t? Why? What’s special about that server?
- Can you automatically test your project completely? Many teams are forced to answer “no.” Why? Is it too hard to define the acceptable results? Won’t this make it hard to prove to the sponsors that the project is “done”?
- Is it too hard to test the application logic independent of the GUI?  What does this say about the GUI? About coupling?