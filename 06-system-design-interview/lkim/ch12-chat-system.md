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
- The primary key is message_id: helps to decide message sequence.

**Message table for group chat**
- The composite primary key: (channel_id, message_id) - channel represent the group

**How to generate message ID**
- carries the responsibility of ensuring the order of messages.
  - IDs must be unique
  - IDs should be sortable by time, meaning new rows have higher IDs than old ones.
- How?
  - auto-increment keyword MySQL, but not in NoSQL DB
  - global 64-bit sequence number generator like Snowflake
  - local sequence number generator - IDs are only unique within a group

# Design deep dive
- Go in-depth for service discovery, messaging flows, and online/offline indicators

## Service discovery
- Role: recommend the best chat server for a client based on criteria like geographical location, server capacity, etc.
- After the service discovery finds the best chat server as server 2, the server info is returned back to User A.
  - User A connects to chat server 2 through WebSocket.

## Message flows
### 1 on 1 chat flow
<img width="613" alt="Screenshot 2024-12-10 at 4 11 35 AM" src="https://github.com/user-attachments/assets/2a6b7e10-ccd2-4cd2-a111-29553ed0f69d">

- 4. The message is stored in a key-value store.
- 5.a. If User B is online, the message is forwarded to Chat server 2 where User B is connected.
- 5.b. If User B is offline, a push notification is sent from push notification (PN) servers.
- 6. Chat server 2 forwards the message to User B. There is a persistent WebSocket connection between User B and Chat server 2.

### Message synchronization across multiple devices
- When users have multiple devices
- Each device maintains `cur_max_message_id`, which keeps track of the latest message ID on the device.
- New messages
  - The recipient ID is equal to the currently logged-in user ID.
  - Message ID in the key-value store is larger than `cur_max_message_id`.
- message synchronization is easy as each device can get new messages from the KV store.

### Small group chat flow
**When User A sends a message in a group chat, 3 members in the group**
- the message from User A is copied to each group member’s message sync queue
  - the message sync queue is an inbox for a recipient.
- This design choice is good for small group chat
  - simplifies message sync flow as each client only needs to check its own inbox to get new messages.
  - with a small group, storing a copy in each recipient’s inbox is not too expensive.

**A recipient can receive messages from multiple users.**
- Each recipient has an inbox (message sync queue) which contains messages from different senders.

## Online presence
- presence servers are responsible for managing online status and communicating with clients through WebSocket

### User login/logout
<img width="615" alt="Screenshot 2024-12-10 at 4 36 21 AM" src="https://github.com/user-attachments/assets/888e53c7-e8b1-43e2-a752-f43b128ef945">
- After a WebSocket connection is built between the client and the real time service, user A’s online status and `last_active_at` timestamp are saved in the KV store. 
- Presence indicator shows the user is online after she logs in.
- As user logout, it just adds a request to the API server between presence servers

### User disconnection
-  It is common for users to disconnect and reconnect to the internet frequently in a short time, with internet connection unreliable/changing

#### a heartbeat mechanism
- Periodically, an online client sends a heartbeat event to presence servers
  - i.e. every 5 seconds, client sends a heartbeat event and after an arbitrarily number (30 seconds) if the heartbeat is not sent, then it's changed to offline

#### Online status fanout
**How do user A’s friends know about the status changes?**
- Presence servers use a publish-subscribe model, in which each friend pair maintains a channel.
- When User A’s online status changes, it publishes the event to each channel
- The comm between clients and servers is via real-time Websocket.

**For larger groups**
- the above approach is expensive and time consuming for larger groups
- thus to fetch online status only when a user enters a group or manually refreshes the friend list.

# Wrap up / Additional points
### Summary
- WebSocket for real-time communication between the client and server. - - The chat system contains
  - chat servers for real-time messaging,
  - presence servers for managing online presence,
  - push notification servers for sending push notifications,
  - key-value stores for chat history persistence and API servers for other functionalities.

### Additionally
- Extend the chat app to support media files such as photos and videos.
  - Media files are larger than text in size. Compression, cloud storage, and thumbnails

- End-to-end encryption so only the sender and the recipient can read messages
 
- Caching messages on the client-side to reduce the data transfer between the client and server.

- Improve load time. Slack built a geographically distributed network to cache users’ data, channels, etc

- Error handling.
  - The chat server error. There might be hundreds of thousands, or even more persistent connections to a chat server. If a chat server goes offline, service discovery (Zookeeper) will provide a new chat server for clients to establish new connections with.
  - Message resent mechanism. Retry and queueing are common techniques for resending messages.
