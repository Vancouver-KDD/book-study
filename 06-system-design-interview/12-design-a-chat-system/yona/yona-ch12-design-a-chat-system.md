# Chapter 12: Design a Chat System

A chat app performs different functions for different people. It's important to nail down the exact requirements. 
    - i.e. you don't want to design a system that focuses on group chat when the interviewer has one-on-one chat in mind
    - important to explore the feature requirements

## Step 1: Understand the problem and establish design scope
- Agree on the type of chat app to design.
- one-on-one chat apps: Facebook Messenger, WeChat, WhatsApp, 
- office chat apps that focus on group chat: Slack, game chat apps (i.e. Discord)
    - large group interaction, low voice chat latency

- Our requirements:
    - Both 1:1 and group chat must be supported
    - Both mobile and web app
    - Scale: 50 million daily active users (DAU)
    - Group member limit: max 100 ppl
    - Important features: 1:1 chat, group chat, online indicator. The system only supports text messages (no attachments)
    - Message size limit: length < 100,000 chars long
    - End-to-end encryption not required, but will discuss if time allows
    - Should store chat history forever

- Thus we focus on designing a chat app like Facebook messenger
    - 1:1 chat with low delivery latency
    - small group chat (max 100 ppl)
    - online presence
    - multiple device support (same account can be logged in to multiple accounts at the same time)
    - push notifications
    - 50 million DAU

## Step 2: Propose high-level design and get buy-in
- Have a basic knowledge of how clients and servers communicate
    - Clients don't communitcate directly with each other. 
    - Each client connects to a chat service using one or more network protocols
    - Receive messages from other clients
    - Find the right recipients for each message and relay the message to the recipients
    - If a recipient is not online, hold the messages for that recipient on the server until she is online

- Requests are initiated by the client for most client/server applications (also true for the sender side of a chat app)
- When the sender sends a message to the receiver via the chat service, it uses the time-tested HTTP protocol (most common web protocol)
    - Client opens a HTTP connection with the chat service and sends the message, informing the service to send the message to the receiver
    - keep-alive is efficient b/c the keep-alive header allows a client to maintain a persistent connection with the chat service
    - reduces the number of TCP handshakes
    - Fine option on the sender side
- However, receiver side is more complicated
    - HTTP is client-initiated, so not trivial to send messages from the server
- Many techniques used to simulate a server-initiated connection
    - polling
    - long polling
    - WebSocket

### Polling
- A technique that the client periodically asks the server if there are messages available
- Depending on polling frequency, polling could be costly
- Could consume precious server resources to answer a question that offers no as an answer most of the time

### Long Polling
- Because polling could be inefficient, long polling
- A client holds the connection open until there are actually new messages available or a timeout threshold has been reached
- Once the client receives new messages, it immediately sends another request to the server, restarting the process
- Drawbacks:
    - Sender and receiver may not connect to the same chat server. 
        - HTTP servers are usually stateless.
        - If you use round robin for load balancing, the server that receives the message might not have a long-polling connection with the client who receives the message
    - a server has no good way to tell if a client is disconnected
    - inefficient. If a user does not chat much, long polling still makes periodic connections after timeouts

### WebSocket
- Most common solution for sending asynchronous updates from server to client
- Initiated by the client. 
- Bi-directional and persistent
- Starts its life as a HTTP connection and could be "upgraded" via some well-defined handshake to a WebSocket connection
- Through persistent connection, server can send updates to a client
- Works even if a firewall is in place ( they use port 80 or 443 which are also used by HTTP/HTTPS connections )
- Since bi-directionoal, no strong technical reason not to use it also for sending
- Simplifies the design and makes implementation on both client and server more straightforward
- Efficient management is critical on the server-side

## High-level Design
- Main communication protocol between client and server for its bidirectional communication = WebSocket
- Everything else does not have to be WebSocket
    - Most features (sign up, login, user profile, etc) could use traditional request/response method over HTTP

## Stateless Services
- Traditional public-facing request/response services
    - used to manage login, signup, user profile, etc
    - common features among many websites and apps
- Sit behind a load balancer whose job is to route requests to the correct services based on the request paths
    - These services can be monolithic or individual microservices
- There are many stateless services in the market that can be integrated easily
    - i.e. Service discovery - its primary job is to give the client a list of DNS host names of chat servers that the client could connect to

## Stateful Service
- Only stateful service is the chat service
- Client maintains a persistent network connection to a chat server
- Client normally does not switch to another chat server as long as the server is still available
- Service discovery coordinates closely with the chat service to avoid server overloading

## Third-party integration
- Push notification
- Inform users when new messages have arrived, even when the app is not running
- Proper integration of push notis is crucial

## Scalability
- On a small scale: all services ^ could fit in one server
- In theory possible to fit all user connections in one modern cloud server
- Number of concurrent connections that a server can handle will most likely be the limiting factor
- Design with one server - may raise a big red flag
    - no technologist would design such a scale in a single server
    - Deal breaker due to many factors (i.e. single point failure)
- Fine to start with a single server design. Just let the interviewer know this is the starting point

## Storage
- Data layer
- Requires some effort to get it correct
- Important decision to make: right type of db to use (relational or NoSQL?)
- Examine data types and read/write patterns

## Data Models
- Decided on using key-value stores as our storage layer
- Most important data: message data

### Message Table for 1:1 chat
- primary key: message_id
    - helps to decide message sequence
    - cannot rely on created_at because 2+ messages can be created at the same time

### Message table for group chat
- Composite primary key: channel_id, message_id
    - channel == group
    - channel_id is the partition key because all queries in a group chat operate in a channel

### Message_ID
- carries the responsibility of ensuring the order of messages
    - IDs must be unique
    - IDs should be sortable by time (new IDs > old IDs)

- Some ideas:
    - auto-increment in MySql
        - NoSQL dbs ususally don't provide this feature
    - global 64-bit sequence number generator like Snowflake
    - local sequence number generator
        - local = IDs are only unique within a group
        - maintaining message sequence within 1:1 channel or a group channel is sufficient
        - easier to implement
    
## Step 3: Design deep dive

### Service Descovery
- Recommends the best chat server for a client based on the criteria like geographical location, server capacity, etc
- Apache Xookeeper
    - registers all the available chat servers and picks the best chat server for a client based on predefined criteria

### Message Flows
- End-to-end flow of a chat system
    - 1:1 chat flow
### Message synchronization across multiple devices
- Explain how to sync messages across multiple devices
    - When user A logs in to the chat app with their phone, it establishes a WebSocket connection with Chat server 1
    - similarly, there is a connection between the laptop and Chat server 1
    - Each device maintains a variable called cur_max_message_id, to keep track of the latest message ID on the device
    - messages with below conditions are considered new messages:
        - recipient ID == currently logged-in user ID
        - message ID in the key-value store is larger than cur_max_message_id
### Small group chat flow
- logic of group chat is more complicated
- refer to Figure 12-14

### Online presence
- essential feature
- green dot next to a user's profile picture or username
- presence servers are responsible for managing online status and communicating with clients through WebSocket
- flows that will trigger online status change
    - user login
    - user logout
    - user disconnection
    - online status fanout

## Step 4: Wrap Up
- Summarize what we did so far
- If extra time:
    - extend the cat app to support media files (photos, videos)
        - larger than text in size
        - compression, cloud storage, thumbnails
    - end-to-end encryption
        - only sender and recipient can read messages
    - caching messages on the client-side to recude data transfer between client and server
    - improve load time
        - geographically distributed network to cache users' data, channels, etc
    - error handling
        - chat server error
            - service discovery provides a new chat server for clients to establish new connections with if a chat server goes offline
        - message resent mechanism
            - retry
            - queueing
        

