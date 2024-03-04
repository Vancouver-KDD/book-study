# Chapter 3 : A FRAMEWORK FOR SYSTEM DESIGN INTERVIEWS

## What is system design interview?

- System design interview 란 실제 상황에서의 문제 해결을 모방(intimidate) 하는 인터뷰이다.
- 한정적인(Limited) 인터뷰 시간 내에서 실제로 하나의 전체 product 를 design 하는 것은 불가능하다.
- Interview 를 통해서 알고자 하는 것은 'ability to collaborate, to work under pressure, and to resolve ambiguity
  constructively' 이다.
- Answer 이 정해져있지 않으며, Good Question 을 할 수 있는 능력 또한 필요하다.

## A 4-step process for effective system design interview

### Step 1 - Understand the problem and establish design scope

- 빠르게 답변을 하는 것보다 요구사항 (Requirements) 를 명확하는(clarify) 것이 중요하다.
- 이를 위해, Good questions 을 하도록 하자.
- Good questions 이란 모호한(vague) 요구사항 혹은 아직 정해지지 않은 디테일 들에 대한 질문이 될 수 있다.

### Step 2 - Propose high-level design and get buy-in

- High-level design 을 우선적으로 목표로 하고, interviewer 와 피드백을 주고 받는다.
- 디테일을 아직 논의하지 말고, clients side, APIs, web servers, data stores, cache, CDN, message queue 등에 대해서 어떻게 처리 할 지 큰 그림을 그리자.
- Whiteboard 에 Diagram 을 제공하는 것도 좋은 방법


### Step 3 - Design deep dive

#### 전체 디자인을 보완하고, 아래의 목표에 집중한다.

- Agreed on the overall goals and feature scope
- Sketched out a high-level blueprint for the overall design 
- Obtained feedback from your interviewer on the high-level design 
- Had some initial ideas about areas to focus on in deep dive based on her feedback

#### 이 과정에서 Interviewer 가 선호하는 방향을 파악하려고 해보자. 예를 들면, some interviewers 는 high-level design 에 우선순위(priority) 가 여전히 있을 수도 있지만, 다른 몇몇은 다른 부분을 건드는 것을 원할 수도 있다.

### Step 4 - Wrap up

- Potential improvements 등을 논의하면서 피드백을 받자.
- Never say 'your design is perfect and nothing can be improved'
- 너가 어필 할 부분이 있으면 다시 한번 interviewer 에게 강조를 해주자. (or refreshing)
- Error cases, Operation issues, the Next scale curve 도 마무리에 꺼내기 좋은 토픽이다.

### Do's & Don'ts

#### Do's

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

#### Don'ts

- Don't be unprepared for typical interview questions.
- Don’t jump into a solution without clarifying the requirements and assumptions. 
- Don’t go into too much detail on a single component in the beginning. Give the highlevel
design first then drills down. 
- If you get stuck, don't hesitate to ask for hints. 
- Again, communicate. Don't think in silence. 
- Don’t think your interview is done once you give the design. You are not
