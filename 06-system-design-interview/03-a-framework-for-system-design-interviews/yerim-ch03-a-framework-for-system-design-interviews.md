# 3. A FRAMEWORK FOR SYSTEM DESIGN INTERVIEWS
- The system design interview simulates real-life problem solving where **two co-workers collaborate on an ambiguous problem and come up with a solution** that meets their goals.
- This allows you to **demonstrate** your design skill, **defend** your design choices, and **respond** to feedback in a constructive manner.
- An effective system design interview gives strong signals about a person's ability to **collaborate**, to **work under pressure**, and to *resolve ambiguity constructively**. 
- The ability to **ask good questions** is also an essential skill, and many interviewers specifically look for this skill.
- Red flags:
    - **Over-engineering**: Many companies pay a high price for that ignorance.
    - **Narrow mindedness**
    - **Stubbornness**


### A 4-step process for effective system design interview
- A great system design interview is open-ended and there is no one-size-fits-all solution.

### Step 1 - Understand the problem and establish design scope
- Answering without a thorough understanding of the requirements is a huge red flag as the interview is not a trivia contest. **Slow down.** Think deeply and ask questions to clarify requirements and assumptions.
- One of the most important skills as an engineer is to **ask the right questions**, make the proper assumptions, and gather all the information needed to build a system.
- What kind of questions to ask?
    - _What specific features are we going to build?_
    - _How many users does the product have?_
    - _How fast does the company anticipate to scale up? What are the anticipated scales in 3 months, 6 months, and a year?_
    - _What is the company’s technology stack? What existing services you might leverage to simplify the design?_
- Example:
    - Is this a mobile app? Or a web app? Or both?
    - What are the most important features for the product?
    - Is the news feed sorted in reverse chronological order or a particular order? The particular order means each post is given a different weight. For instance, posts from your close friends are more important than posts from a group.
    - How many friends can a user have?
    - What is the traffic volume? (DAU)
    - Can feed contain images, videos, or just text?

### Step 2 - Propose high-level design and get buy-in
- Come up with an initial blueprint for the design. **Ask for feedback.** Treat your interviewer as a teammate and work together. Many good interviewers love to talk and get involved.
- **Draw box diagrams with key components** on the whiteboard or paper. This might include clients (mobile/web), APIs, web servers, data stores, cache, CDN, message queue, etc.
- Do back-of-the-envelope calculations to evaluate if your blueprint fits the scale constraints. **Think out loud.** Communicate with your interviewer if back-of-the-envelope is necessary before diving into it.
- Example:
    - At the high level, the design is divided into two flows: feed publishing and news feed building.

### Step 3 - Design deep dive
- Sometimes, the interviewer may give off hints that she likes focusing on high-level design.
- Sometimes, for a senior candidate interview, the discussion could be on the system performance characteristics, likely focusing on the bottlenecks and resource estimations.
- For URL shortener, it is interesting to dive into the hash function design that converts a long URL to a short one.
- For a chat system, how to reduce latency and how to support online/offline status are two interesting topics.
- Try not to get into unnecessary details.

### Step 4 - Wrap up
- The interviewer might want you to identify the system bottlenecks and discuss potential improvements. **Never say your design is perfect and nothing can be improved.**
- It could be useful to **give the interviewer a recap of your design.** This is particularly important if you suggested a few solutions. Refreshing your interviewer’s memory can be helpful after a long session.
- **Error cases** (server failure, network loss, etc.) are interesting to talk about.
- **Operation issues** are worth mentioning. How do you monitor metrics and error logs? How to roll out the system?
- **How to handle the next scale curve** is also an interesting topic.
- **Propose other refinements** you need if you had more time.
- **Dos:**
    - Always ask for clarification. Do not assume your assumption is correct.
    - Understand the requirements of the problem.
    - There is neither the right answer nor the best answer. A solution designed to solve the problems of a young startup is different from that of an established company with millions of users. Make sure you understand the requirements.
    - Let the interviewer know what you are thinking. Communicate with your interview.
    - Suggest multiple approaches if possible.
    - Once you agree with your interviewer on the blueprint, go into details on each component. Design the most critical components first.
    - Bounce ideas off the interviewer. A good interviewer works with you as a teammate.
    - Never give up.

- **Don't:**
    - Don't be unprepared for typical interview questions.
    - Don’t jump into a solution without clarifying the requirements and assumptions.
    - Don’t go into too much detail on a single component in the beginning. Give the high- level design first then drills down.
    - If you get stuck, don't hesitate to ask for hints.
    - Again, communicate. Don't think in silence.
    - Don’t think your interview is done once you give the design. You are not done until your interviewer says you are done. Ask for feedback early and often.

### Time allocation on each step
- Step 1 Understand the problem and establish design scope: **3 - 10 minutes**
- Step 2 Propose high-level design and get buy-in: **10 - 15 minutes**
- Step 3 Design deep dive: **10 - 25 minutes**
- Step 4 Wrap: **3 - 5 minutes**