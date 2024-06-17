# CH 3. A Framework for System Design Interviews

### What is system design interview is for?

The final design is less important compared to the work you put in the design process. This allows you to demonstrate your design skill, defend your design choices, and respond to feedback in a constructive manner

### System design interview red flags are;

- over-engineering, narrow mindedness, stubborness, etc.

<aside>
💡 What is “over-engineering”? 
**Unnecessary abstractions, Over use DRY, Over use design patterns, Wrap a library,** 
https://jamiewen00.medium.com/over-engineering-examples-in-code-21c365ae4ecc

</aside>

## A 4-step process for effective system design interview

Although every interview is different, there are some steps to cover in any interview.

### Step 1 - understand the problem and establish design scope

Don't jump to solutions. Ask questions to clarify requirements and assumptions.

<aside>
💡 Tips: 
When you get your answers (or you're asked to make assumptions yourself), write them down on the whiteboard to not forget.

</aside>

### What kind of questions to ask? Example questions;

- What specific features do we need to design?
- How many users does the product have?
- How fast is the company anticipated to scale up? - in 3, 6, 12 months?
- What is the company's tech stack? What existing services can you leverage to simplify the design?

### Design a news feed interview clarification questions example;

- Candidate: Is it mobile, web, both?
- Interviewer: both.
- C: What's the most important features?
- I: Ability to make posts & see friends' news feed.
- C: How is the feed sorted? Just chronologically or based on eg some weight to posts from close friends.
- I: To keep things simple, assume posts are sorted chronologically.
- C: Max friends on a user?
- I: 5000
- C: What's the traffic volume?
- I: 10mil daily active users (DAU)
- C: Is there any media in the feed? - images, video?
- I: It can contain media files, including video & images.

### Step 2 - Propose high-level design and get buy-in

The goal of this step is to develop a high-level design, while collaborating with the interviewer.

- Come up with a blueprint, **ask for feedback.** Many good interviewers involve the interviewer.
- Draw boxes on the whiteboard with key components - **clients, APIs, web servers, data stores, cache, cdn, message queue,** etc.
- Do **back-of-the-envelope calculations** to evaluate if the blueprint fits the scale constraints. Communicate with interviewer if this type of estimation is required beforehand.

### Example - designing a news feed

High-level features:

- feed publishing - user creates a post and that post is written in cache/database, after which it gets populated in other news feeds.
- news feed building - news feed is built by aggregating friends' posts in chronological order.

### Example diagram - feed publishing:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter04/images/feed-publishing.png

<aside>
💡 Post service 
- creation of new posts

Fanout service 
- When a message is published to a topic, the fanout service delivers copies of the message to all subscribers that have subscribed to that topic.

Questions❓❓❓
- what these “Post cache” and “News Feed Cache” will be used for?

</aside>

### Example diagram - news feed building:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter04/images/news-feed-building.png

## Step 3 - Design deep dive

You shall work with the interviewer to identify and prioritize components in the architecture.
It is worth stressing that every interview is different. 

Example things you might have to focus on:

- High-level design.
- System performance characteristics.
- In most cases, dig into the details of some system component.

What details could you dig into? Some examples:

- For URL shortener - the hash function which converts long URLs into small ones
- For Chat system - reducing latency and supporting online/offline status

## Step 4 - Wrap up

At this stage, the interviewer might ask you some follow-up questions or give you the freedom to discuss anything you want.

A few directions to follow:

- The interviewer might want you to identify the system bottlenecks and discuss potential
improvements.
    - Never say your design is perfect and nothing can be improved. There is always something to improve upon.
    - This is a great opportunity to show your critical thinking and leave a good final impression.
- Give recap of your design. Refreshing your interviewer’s memory.
- Error cases (server failure, network loss, etc.) are interesting to talk about.
- Operation issues are worth mentioning.
    - How do you monitor metrics and error logs? How to roll out the system?
- How to handle the next scale curve is also an interesting topic.
    - For example, if your
    current design supports 1 million users, what changes do you need to make to support 10
    million users?
- Propose other refinements you need if you had more time.

Dos

- Always ask for clarification. Do not assume your assumption is correct.
- Understand the requirements of the problem.
- There is neither the right answer nor the best answer. A solution designed to solve the
problems of a young startup is different from that of an established company with millions
of users. Make sure you understand the requirements.
- Let the interviewer know what you are thinking. Communicate with your interview.
- Suggest multiple approaches if possible.
- Once you agree with your interviewer on the blueprint, go into details on each
component. Design the most critical components first.
- Bounce ideas off the interviewer. A good interviewer works with you as a teammate.
- Never give up.

Don’ts

- Don't be unprepared for typical interview questions.
- Don’t jump into a solution without clarifying the requirements and assumptions.
- Don’t go into too much detail on a single component in the beginning. Give the highlevel design first then drills down.
- If you get stuck, don't hesitate to ask for hints.
- Again, communicate. Don't think in silence.
- Don’t think your interview is done once you give the design. You are not done until your
interviewer says you are done. Ask for feedback early and often.

## Time allocation on each step

45 minutes are not enough to cover any design into sufficient detail. Here's a rough guide on how much time you should spend on each step:

- Understand problem & establish design scope - 3-10m
- Propose high-level design & get buy-in - 10-15m
- Design deep dive - 10-25m
- Wrap-up - 3-5m