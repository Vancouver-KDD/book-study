# Chapter 3: A Framework for System Design Interviews

## Framework : 
### 1. Understand the problem, establish design scope
- There's no right answer
  - However there are [wrong answers](https://twitter.com/lazysnowdark/status/1090244498129485824/photo/1)
- Do not answer immediately - premature conclusion is a big red flag. 
  - c.f. : However - there are *canonical path* for most of the   popular system design questions. Stick to the guide answer- if you can explain well **why** you picked those components than you are good. 
- Ask adequate and relevant questions. Start with general questions. Never jump into nitty-gritty details or some feature you personally admire. 

### 2. HLD & buy-in
- There are numerous methods for diagramming. Just be consistent on the direction of arrows. 
- Get comfortable with sketching boxes. (Excalidraw!)
- Remember: it's an iterative process. Begin with a simple design and evolve it by replacing one component with two new ones. 
- Implementing CQRS/microservice patterns is beneficial, but don't jump to this conclusion immediately. Include an intermediate step. (Depends on your level - if you're L3, your final goal would be seperating services/CQRS. If you are L6, interviewerse would expect CQRS in the initial drawing-  who knows, I'm not L6)
- Validate your system with basic scenarios. Happy path #1, #2, error case #1, #2. 

### 3. Design deep dive 
- Interviewers will really put you through your paces; they'll question every single thing you put on that diagram.
- Vague system or data flows are easy targets. Ensure you understand the connections represented by your arrows.
- Question on Database is a must. **Start with an RDB, and introduce NoSQL when you face with a specific challenge.**

### 4. Wrap up 
- Time to shine. If you aced #1, #2, #3, now's your chance to pour out all the keywords from your past experience.
  - But still -stick to *general* software engineering components. Security, bottleneck, deployment, i18n, etc.
  - Scalability should alreday have been discussed. 
- template : Hey if I had more time I could have discussed X, Y, Z. 