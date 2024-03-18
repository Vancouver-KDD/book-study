# CHAPTER 13: DESIGN A SEARCH AUTOCOMPLETE SYSTEM

**The design of a chat system**

## Step 1 - Understand the problem and establish design scope

Clarification Questions 
```
Candidate: What kind of chat app shall we design? 1 on 1 or group based?
Interviewer: It should support both 1 on 1 and group chat. 
Candidate: Is this a mobile app? Or a web app? Or both? 
Interviewer: Both.
Candidate: What is the scale of this app? A startup app or massive scale? 
Interviewer: It should support 50 million daily active users (DAU). 
Candidate: For group chat, what is the group member limit?
Interviewer: A maximum of 100 people
Candidate: What features are important for the chat app? Can it support attachment? 
Interviewer: 1 on 1 chat, group chat, online indicator. The system only supports text messages.
Candidate: Is there a message size limit?
Interviewer: Yes, text length should be less than 100,000 characters long. 
Candidate: Is end-to-end encryption required?
Interviewer: Not required for now but we will discuss that if time allows. 
Candidate: How long shall we store the chat history?
Interviewer: Forever.
```

Chat App with the following features
```
• A one-on-one chat with low delivery latency
• Small group chat (max of 100 people)
• Online presence
• Multiple device support. The same account can be logged in to multiple accounts at the same time.
• Push notifications
```

## Step 2 - Propose high-level design and get buy-in
```
Each client connects to a chat service, which supports all the features mentioned above.
Let us focus on fundamental operations. The chat service must support the following functions:
```
![fg12-2](img/fg12-2)

