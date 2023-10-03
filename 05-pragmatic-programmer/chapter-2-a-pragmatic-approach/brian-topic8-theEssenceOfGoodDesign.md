# The Essence of Good Design

## Tip 14 - Good Design Is Easier to Change Than Bad Design
- ETC: Easier to Change
  - Every design principle out there is a special case of ETC.
    - Decoupling, Single Responsibility, Naming etc.
- ETC IS A VALUE, NOT A TRUE
  - ETC is a guide, helping you choose between paths.
    - “did the thing I just did make the overall system easier or harder to change?”
  - When you won’t have a clue.
    - First, try to make what you write replaceable
      - It’s really just thinking about keeping code decoupled and cohesive.
    - Second, note the choices you have, and some guesses about change
      - Then, later, when this code has to change, you’ll be able to look back and give yourself feedback.

### Law of Demeter
- Each unit should have only limited knowledge about other units: only units "closely" related to the current unit.
- Each unit should only talk to its friends; don't talk to strangers.
- Only talk to your immediate friends.

## Challenges
- Think about a design principle you use regularly. Is it intended to make things easy-to-change?
- Also think about languages and programming paradigms (OO, FP, Reactive, and so on). 
  - Do any have either big positives or big negatives when it comes to helping you write ETC code? 
  - Do any have both?
  - When coding, what can you do to eliminate the negatives and accentuate the positives?
- Many editors have support (either built-in or via extensions) to run commands when you save a file. 
  - Get your editor to popup an ETC?
  - message every time you save and use it as a cue to think about the code you just wrote. 
  - Is it easy to change?
