# CHAPTER 3: A FRAMEWORK FOR SYSTEM DESIGN INTERVIEWS

- System design interview is to examine ability to
  - collaborate
  - work under pressure
  - resolve ambiguity
  - ask good questions
- Red flags
  - over-engineering
    - adding features and functionality to where it is not needed or it will not be used
    - follow Rule of three
- Narrow mindedness & Stubbornness (not listening to suggestions)

reference: https://en.wikipedia.org/wiki/Rule_of_three_(computer_programming)

## 4-step process for effective system design interview

### Step 1 - Understand the problem and establish design scope

- Take time to analyze the requirement and assumptions and do not rush to answer
- Most important skills of an engineer

  1. Ask the right questions
  2. Make proper assumptions
  3. Gather all information neede to build a system

- Question examples

  - Mobile vs web app?
  - What specific features are we going to build?
  - How many users does the product have?
  - How fast does the company anticipate to scale up?
  - What are the anticipated scales in 3 months, 6 months, and a year?
  - What is the company’s technology stack?
  - What existing services you might leverage to simplify the design?

- Ex) News feed app
- how to order feed
- number of friends a user can have
- traffic volume
- feed can contain image or video

### Step 2 - Propose high-level design and get buy-in

- Collaborate with the interviewer like a teammate
- Come up with a blueprint and get feedback
- Draw diagram with key components of a system (clients, API, servers, data store, cache, CDN, message queue)
- Back-of-the-envelope calculations and see if blueprint fits

Ex) High level design

- Feed publishing
- Newsfeed building

### Step 3 - Design deep dive

- Will be asked to dig into some of the important component
- Such as bottlenecks and resource estimation
- URL shortner functions
- How to reduce latency
- How to support online/offline status
- Time management (do not waste time on things that you will not show ur abilities with)

### Step 4 - Wrap up

- follow-up questions
- discuss other additional points
- identify the system bottlenecks
- potential improvements
- Never say your design is perfect and nothing can be improved
- show your critical thinking
- do a recap over the process
- Error cases
- metrics and error logs (how to achieve observability)
- how to handle scaling

### Dos

- Take time to analyze and understand the requirement
- Ask for clarification to make clear assumptions
- Collaborate with interviewer & treat as a team member
- Let them know what you are thinking
- Get blueprint done and then go into details

### Don’ts

- Dont be unprepared for typical questions
- Do not rush to solutions
- Do not waste time on details that do not show ur abilities
- Do not be afraid of asking questions or hints if stuck
- Do not stay silent keep communicating
- Do not stop finishing desinging keep thinking and sharing new improvements

### Time allocation

1. Understand the problem and establish design scope: 3 - 10 minutes
2. Propose high-level design and get buy-in: 10 - 15 minutes
3. Design deep dive: 10 - 25 minutes
4. Wrap: 3 - 5 minutes
