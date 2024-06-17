# Design a Chat System

## Step 1 - Understand the problem and establish design scope

### Chat App Scope

- Supports both one-on-one convos and group chats.
- Available on mobile and web platforms.
- Scalable to handle 50 million DAU.
- Group chat member limit of 100 people.
- Chat history stored indefinitely.

## Step 2 - Propose high-level design and get buy-in

### Client-Server Communication Methods

1. **Polling**
- Clients make periodic requests to the server.
- Easy to implement but can lead to high server load.
- Resource-intensive due to frequent requests.
1. **Long Polling**
- Clients maintain an open connection.
- Reduces requests compared to polling.
1. **WebSockets**
- Provides bidirectional communication.
- Most efficient for real-time communication.

### High-Level Design

### Stateless Services vs. Stateful Service

Stateless Service

- Handle public-facing request/response operations like login, signup, and user profile management.
- Utilize load balancers to distribute requests among service replicas.
- Not inherently stateful, making them scalable and easy to manage.

Stateful Service (Chat Service):

- Maintains persistent connections with clients for real-time communication.
- Clients stay connected to the same chat service as long as it remains active.
- Requires coordination with service discovery to prevent overload and ensure efficient connection management.

### Overall flow

- clients maintain a persistent web socket connection with a chat server for real-time messaging
- The chat servers facilitate message sending/receiving
- Presense servers manage online/offline status
- API servers handle traditional request/response-based responsibilities - login, sign up, change profile, etc.
- Notification servers manage push notifications
- Key-value store is used for storing chat history. When offline user goes online, they will see their chat history and missed messages.

### DB (SQL vs. NoSQL)

**SQL:**

Traditional data like user profiles and settings can be stored in relational databases, using techniques like replication and sharding for scalability.

**NoSQL:**

Chat history data, however, with its enormous volume and specific read/write pattern, is recommended to be stored in a key-value store due to:

1. Easy horizontal scaling and low latency access.
2. Key-value stores are better suited for long-tail data access, which is common in chat histories.
3. Relational databases struggle with large indexes and random access, making key-value stores like HBase and Cassandra preferred choices for chat systems used by platforms like Facebook and Discord.

## Step 3 - Design deep-dive

### Service Discovery

- Service discovery is to choose the best server based on some criteria - eg geographic location, server capacity, etc.
- Apache Zookeeper for server selection based on criteria.

### Message Flows

1. 1 on 1 chat flow 
2. message synchronization across devices 
3. small group chat flow 

### Online Presence

The online presence system in our chat application manages user statuses as "online" when they log in and "offline" when they log out or disconnect.

To handle frequent disconnects and ensure accurate status updates, we implement a heartbeat mechanism where clients send periodic heartbeats to indicate online status. If a heartbeat is not received within a set time frame, the user is marked as offline.

User presence status is communicated to friends using a fanout mechanism, where each friend pair has a queue for status updates. This approach is effective for small group chats, similar to WeChat's model with a user group cap of 500. For larger groups, presence status is fetched only when a user enters the group or refreshes the members list.

## Step 4 - Wrap up

In conclusion, the chat system design prioritizes real-time messaging, scalability, and efficient user experience. Key components include WebSocket communication, service discovery for server selection, and robust message handling flows. Additional considerations like media support, encryption, and error handling enhance the system's functionality and reliability.