# Debugging

> It is a painful thing to look at your own trouble and know that you yourself and no one else has made it
> - Sophocles, Ajax

## Psychology of Debugging
### Tip 29 - Fix the Problem, Not the Blame
- Debugging is a sensitive, emotional subject for many developers.
- It doesn’t really matter whether the bug is your fault or someone else’s. It is still your problem.

## A Debugging Mindset
### Tip 30 - Don't Panic
- Always try to discover the root cause of a problem, not just this particular appearance of it.

## Where to Start
- You first need to be accurate in your observations rather than debugging tools.
- Accuracy in bug reports is further diminished when they come through a third party.
  - You may need to interview the user who reported the bug in order to gather more data than you were initially given.
  - Artificial tests don’t exercise enough of an application. You must brutally test both boundary conditions and realistic end-user usage patterns. You need to do this systematically.

## Debugging Strategies
### Reproducing Bugs
### Tip 31 - Failing Test Before Fixing Code
-  It’s a lot harder to fix a bug if you have to go through 15 steps to get to the point where the bug shows up.

## Coder in a Strange Land
### Tip 32 - Read the Damn Error Message
### Bad Results
- More often you have to look deeper to find out why the value is wrong in the first place.
- There’s often a quicker way to find the problem than examining each and every stack frame: use a binary chop.
### Sensitivity to Input Values
- Your program works fine with all the test data, and survives its first week in production with honor. 
  - Then it suddenly crashes when fed a particular dataset.
- Get a copy of the dataset and feed it through a locally running copy of the app, making sure it still crashes. 
- Then binary chop the data until you isolate exactly which input values are leading to the crash.

### Regressions Across Releases
- At some point a bug pops up in code that worked OK a week ago. 
- Wouldn’t it be nice if you could identify the specific change that introduced it? 
- Guess what? 
  - Binary chop time.

## The Binary Chop (sometimes called a binary search)
1. When you’re facing a massive stacktrace and you’re trying to find out exactly which function mangled the value in error, ~
2. If you find bugs that appear on certain datasets, ~
3. If your team has introduced a bug during a set of releases, ~

### Logging and/or Tracing
- Tracing statements are those little diagnostic messages you print to the screen or to a file that say things such as “got here” and “value of x = 2.”
- Tracing is invaluable in any system where time itself is a factor: concurrent processes, real-time systems, and event-based applications.

### Rubber Ducking
- A very simple but particularly useful technique for finding the cause of a problem is simply to explain it to someone else.

### Process of Elimination
### Tip 33 - "select" Isn't Broken
- If you “changed only one thing’’ and the system stopped working, that one thing was likely to be responsible, directly or indirectly, no matter how farfetched it seems.
- So keep a close eye on the schedule when considering an upgrade; you may want to wait until after the next release.

## The Element of Surprise
### Tip 34 - Don't Assume It - Prove It
- When you come across a surprise bug, beyond merely fixing it, you need to determine why this failure wasn’t caught earlier. 
  - Consider whether you need to amend the unit or other tests so that they would have caught it.
- Make sure that whatever happened, you’ll know if it happens again.
- If the bug is the result of someone’s wrong assumption, discuss the problem with the whole team.

## Debugging Checklist
- Is the problem being reported a direct result of the underlying bug, or merely a symptom? 
- Is the bug really in the framework you’re using? Is it in the OS? Or is it in your code? 
- If you explained this problem in detail to a coworker, what would you say? 
- If the suspect code passes its unit tests, are the tests complete enough? 
  - What happens if you run the tests with this data? 
- Do the conditions that caused this bug exist anywhere else in the system? 
  - Are there other bugs still in the larval stage, just waiting to hatch?



