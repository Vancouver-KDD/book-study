Many different kinds - extremely important to clarify the feature requirements

# First step - establish the design scope
- One-on-one chat or group chat app? Max number of people in a group?
- Mobile or web?
- Scale, how many users? 50 million DAU
- Attachment support? msg size limit?
- End to end encryption required?
- Storing the chat history?

# high level design
- Sender - chat service - receiver
- choice of network protocols is important
### client to server is rather simple
- Requests are initiated by the client for most client/server applications.
- HTTP protocol, the client opens a HTTP connection, keep-alive

## Techniques for server initiated connection to receivers
### Polling
- the client periodically asks the server if there are messages available. 
- Drawback: depending on the frequency, could be costly - often no answers most time

### Long Polling
- client holds the connection open until there are actually new messages available or a timeout threshold
- Once received new messages, it restarts the polling
- Drawback
    - Sender and receiver may not connect to the same chat server.
    - A server has no good way to tell if a client is disconnected.
    - inefficient. If a user does not chat much, long polling still makes periodic connections after timeouts

### WebSocket
- most common solution for sending asynchronous updates from server to client.
- initiated by the client - bi-directional and persistent.
- Also used for sending and receivers both - simplifies the design

## High level components of the system
- Broken into three major categories: Stateless, stateful, and third party

### Stateless Service
- public-facing request/response services, used to manage the login, signup, user profile, etc.
- Sit behind a load balancer routing requests to the correct microservices

### Stateful Service - chat service
- each client maintains a persistent network connection to a chat server, not changing the server
- The service discovery coordinates with the chat service to avoid server overloading

### Third-party integration
- push notification is the most important third-party integration - Ch 10

### Scalability
- The number of concurrent connections that a server can handle will most likely be the limiting factor.
- Single server design is a deal breaker - The single point of failure and else

<img width="648" alt="Screenshot 2024-12-09 at 3 59 33 PM" src="https://github.com/user-attachments/assets/38e26ddc-11d9-46ce-a054-a998c31a73a3">

- client maintains a persistent WebSocket connection to a chat server for real-time messaging.
- Chat servers facilitate message sending/receiving.
- Presence servers manage online/offline status.
- API servers handle user login, signup, change profile, etc.
- Notification servers send push notifications.
- Finally, the key-value store is used to store chat history. When an offline user comes online, she will see all her previous chat history.

### Storage
- What is the right type of database to use, relational DB or NoSQL DB?
**Relational DB:** generic data - user profile, setting, user friends list

**Chat history data: Key-value store is recommended**
- Read/write pattern
  - The amount of data is enormous - FB/Whatsapp: 60 billion msgs a day
  - Once recent chats are accessed frequently
  - users want random access of data, search, view, jump to specific msg
  - read/write is 1:1 ratio
- Why Key-value stores
  - allow easy horizontal scaling
  - low latency to access data
  - RMDB doesn't handle long tail data well, as the indexes grow large, random access is expensive
  - already adopted by other proven reliable chat apps, FB, Discord

### Data models

**Message table for 1:1 chat**
