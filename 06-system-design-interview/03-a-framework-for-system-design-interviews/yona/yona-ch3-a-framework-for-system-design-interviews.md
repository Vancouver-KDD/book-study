# Chapter 3: A Framework for System Design Interviews

- System design interviews are often intimidating
- It could be vague ("design a well known product X")
- It could be ambiguous and seem unreasonably broad
- How could anyone design a popular product in an hour that has taken hundrends/thousands of engineers to build?

- Simple: no one expects you to. Real-world system design is extremely complicated.
    - Google search engine

- The system design interview simulates real-life problem solving where two co-workers collaborate on an ambiguous problem and come up with a solution that meets their goals. 
    - Open-ended, no perfect answer
    - The final design is less important than the work you put in the design process
    - Demonstrate your design skill, defend your design choices, and respond to feedback in a constructive manner

- The primary goal of an interviewer is to accurately assess your abilities
- Much more than technical design skills
    - Show signals of your ability to collaborate, to work under pressure, and to resolve ambiguity constructively
    - Ask good questions
- A good interviewer also looks for red flags
    - Over-engineering (delight in purity and ignore tradeoffs)
    - Be aware of the compounding costs of over-engineered systems (many companies pay a high price for that ignorance)
    - Narrow mindness
    - Stubbornness

## A 4-Step Process for Effective System Design Interview

- A great system design interview is open-ended and there is no one-size-fits-all solution
- However, there are some steps and common ground to cover in every system design interview

### Step 1: Understand the Problem and Establish Design Scope
- Giving out an answer quickly without thinking gives you no bonus points
- Answering withought a thorough understanding of the requirements is a huge red flag as the interview is not a trivia contest. 
- There is no right answer. So don't jump right into giving a solution
    - __Think deeply and ask questions to clarify requirements and assumptions__
- Ask the right questions, make the proper assumptions, and gather all the information needed to build a system. 
    - Do not be afraid to ask questions!!
    - Understand the requirements and clarify ambiguities
    - When you ask a question, interviewer either answers your question directly or asks you to make your assumptions
        - If the latter, write down your assumptions on paper. You might need them later
    - Some good questions:
        - What specific features are we going to build?
        - How many users does the product have?
        - How fast does the company anticipate to scale up? What are the anticipated scales in 3 months, 6 months, and a year?
        - What is the company's technology stack? What existing services you might leverage to simplify the design?
        - Is this a mobile app? Or web app? Or both?
#### Example
- refer textbook

### Step 2: Propose High-Level Design and Get Buy-In
- Aim to develop a high-level design and reach an agreement with the interviewer on the desing, Collaborate with the interviewer during the process. 
    - Come up with an initial blueprint for the design
        - Ask for feedback
        - Treat your interviewer as a teammate and work together (many interviewers love to talk and get involved)
    - Draw box diagrams with key components on paper
        - Might include clients (mobile/web), APIs, web servers, data stores, cache, CDN, message queue, etc
    - Do back-of-the-envelope calculations to evaluate if your blueprint fits the scale constraints
        - Think out loud
        - Communicate with your interviewer if back-of-the-envelope is necessary before diving into it
- If possible, go through a few concrete use cases
    - Frame the high-level design
    - Discover edge cases
- Should we include API endpoints and database schema?
    - Communicate with your interviewer

#### Example
- refer textbook

### Step 3: Design and deep dive
- At this step these should have already been achieved:
    - Agreed on the overall goals and feature scope
    - Sketched out a high-level blueprint for the overall design
    - Obtained feedback from your interviewer on the high-level design
    - Had some initial ideas about areas to focus on in deep dive based on their feedback

- Work with interviewer to identify and prioritize components in the architecture
    - Every interviewer is different:
        - May prefer high-level design
        - May prefer system performance characteristics, focusing on bottlenecks and resource estimations
        - Dig into details of some system components
- Time management is essential
    - Try not to get into unnecessary details

#### Example
- refer textbook

### Step 4: Wrap Up
- Interviewer might ask a few follow-up questions or give you the freedom to discuss other additional points
    - Never say your design is perfect and nothing can be improved. Identify the system bottlenecks and discuss potential improvements. Show your critical thinking and leave a good final impression
    - Maybe give the interviewer a recap of your design (especially if you suggested a few solutions). Refresh their memory if it was a long session.
    - Error cases (server failure, network loss, etc)
    - Operation issues (How do you monitor metrics and error logs? How to roll out the system?)
    - How to handle the next scale curve
    - Propose other refinements you need if you had more time

#### Dos:
    - Ask for clarification. Do NOT assume
    - Understand the requirements of the problem
    - There is neither the right answer nor the best answer
        - Solution designed to solve the problems of a young startup is different from that of a big company
    - Communicate with your interviewer. Let them know what you're thinking
    - Once agreed on the blueprint, go into details on each component. Design the most critical components first
    - Bounce ideas off the interviewer. Work like ateammate
    - Never give up
    - Ask for feedback early and often

#### Dont's:
    - Don't be unprepared for typical interview questions
    - Don't jump into a solution without clarifying requirements/assumptions
    - Don't go into too much detail on a single component in the beginning. Give a high level design first then drill down
    - If stuck, don't hesitate to ask for hints
    - Don't think in silence

### Time Allocation on Each Step
- A rough guide on distributing your time in a 45 min interview session (actual time distribution depends on the scope of the problem and the requirements from the interviewer):
    - Step 1: 3~10 mins
    - Step 2: 10~15 mins
    - Step 3: 10~20 mins
    - Step 4: 3~5 mins



