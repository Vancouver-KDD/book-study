# Chapter 12: Design a Chat System

## Step 1 - Understand the problem and establish design scope
In the chapter, we focus on designing a chat app like Facebook messenger, with an emphasis
on the following features:
- A one-on-one chat with low delivery latency
- Small group chat (max of 100 people)
- Online presence
- Multiple device support. The same account can be logged in to multiple accounts at the
same time.
- Push notifications
- 50 million DAU

## Step 2 - Propose high-level design and get buy-in
- Receive messages from other clients.
- Find the right recipients for each message and relay the message to the recipients.
- If a recipient is not online, hold the messages for that recipient on the server until she is
online.
### WebSocket
WebSocket is the most common solution for sending asynchronous updates from server to
client.
WebSocket connection is initiated by the client. It is bi-directional and persistent. It starts its
life as a HTTP connection and could be “upgraded” via some well-defined handshake to a
WebSocket connection. Through this persistent connection, a server could send updates to a
client. WebSocket connections generally work even if a firewall is in place. This is because
they use port 80 or 443 which are also used by HTTP/HTTPS connections.

### High-level design
Just now we mentioned that WebSocket was chosen as the main communication protocol
between the client and server for its bidirectional communication, it is important to note that
everything else does not have to be WebSocket. In fact, most features (sign up, login, user
profile, etc) of a chat application could use the traditional request/response method over
HTTP. Let us drill in a bit and look at the high-level components of the system.

#### Stateless Services
Stateless services are traditional public-facing request/response services, used to manage the
login, signup, user profile, etc. These are common features among many websites and apps.
Stateless services sit behind a load balancer whose job is to route requests to the correct
services based on the request paths. These services can be monolithic or individual
microservices. We do not need to build many of these stateless services by ourselves as there
are services in the market that can be integrated easily. The one service that we will discuss
more in deep dive is the service discovery. Its primary job is to give the client a list of DNS
host names of chat servers that the client could connect to.
#### Stateful Service
The only stateful service is the chat service. The service is stateful because each client
maintains a persistent network connection to a chat server. In this service, a client normally
does not switch to another chat server as long as the server is still available. The service
discovery coordinates closely with the chat service to avoid server overloading. We will go
into detail in deep dive.
#### Third-party integration
For a chat app, push notification is the most important third-party integration. It is a way to
inform users when new messages have arrived, even when the app is not running. Proper
integration of push notification is crucial. Refer to Chapter 10 Design a notification system
for more information.
#### Scalability
On a small scale, all services listed above could fit in one server. Even at the scale we design
for, it is in theory possible to fit all user connections in one modern cloud server. The number
of concurrent connections that a server can handle will most likely be the limiting factor. In
our scenario, at 1M concurrent users, assuming each user connection needs 10K of memory
on the server (this is a very rough figure and very dependent on the language choice), it only
needs about 10GB of memory to hold all the connections on one box.

### Data models
Just now, we talked about using key-value stores as our storage layer. The most important
data is message data.

#### Message table for 1 on 1 chat
The primary key is message_id, which
helps to decide message sequence. We cannot rely on created_at to decide the message
sequence because two messages can be created at the same time.

#### Message table for group chat
The composite primary key is
(channel_id, message_id). Channel and group represent the same meaning. channel_id
is the partition key because all queries in a group chat operate in a channel.

#### Message ID
How to generate message_id is an interesting topic worth exploring. Message_id carries the
responsibility of ensuring the order of messages. To ascertain the order of messages,
message_id must satisfy the following two requirements:
- IDs must be unique.
- IDs should be sortable by time, meaning new rows have higher IDs than old ones.

## Step 3 - Design deep dive
### Service discovery
The primary role of service discovery is to recommend the best chat server for a client based
on the criteria like geographical location, server capacity, etc.

### Message flows
#### 1 on 1 chat flow
1. User A sends a chat message to Chat server 1.
2. Chat server 1 obtains a message ID from the ID generator.
3. Chat server 1 sends the message to the message sync queue.
4. The message is stored in a key-value store.
5. If User B is online, the message is forwarded to Chat server 2 where User B is
connected.
6. If User B is offline, a push notification is sent from push notification (PN) servers.
7. Chat server 2 forwards the message to User B. There is a persistent WebSocket
connection between User B and Chat server 2.

Each device maintains a variable called cur_max_message_id, which keeps track of the latest
message ID on the device. Messages that satisfy the following two conditions are considered
as news messages:
- The recipient ID is equal to the currently logged-in user ID.
- Message ID in the key-value store is larger than cur_max_message_id .

#### Small group chat flow
- it simplifies message sync flow as each client only needs to check its own inbox to get
new messages.
- when the group number is small, storing a copy in each recipient’s inbox is not too
expensive.

### Online presence
An online presence indicator is an essential feature of many chat applications. Usually, you
can see a green dot next to a user’s profile picture or username.

#### User login
The user login flow is explained in the “Service Discovery” section. After a WebSocket
connection is built between the client and the real-time service, user A’s online status and
last_active_at timestamp are saved in the KV store. Presence indicator shows the user is
online after she logs in.

#### User logout
The
online status is changed to offline in the KV store. The presence indicator shows a user is
offline.

#### User disconnection
We introduce a heartbeat mechanism to solve this problem. Periodically, an online client
sends a heartbeat event to presence servers. If presence servers receive a heartbeat event
within a certain time, say x seconds from the client, a user is considered as online. Otherwise,
it is offline.

#### Online status fanout
Presence servers use a publish-subscribe model, in which each friend pair maintains a
channel. When User A’s online status changes, it publishes the event to three channels,
channel A-B, A-C, and A-D. Those three channels are subscribed by User B, C, and D,
respectively. Thus, it is easy for friends to get online status updates. The communication
between clients and servers is through real-time WebSocket.

## Step 4 - Wrap up
significantly larger than text in size. Compression, cloud storage, and thumbnails are
interesting topics to talk about.
- End-to-end encryption. Whatsapp supports end-to-end encryption for messages. Only the
sender and the recipient can read messages. Interested readers should refer to the article in
the reference materials.
- Caching messages on the client-side is effective to reduce the data transfer between the
client and server.
- Improve load time. Slack built a geographically distributed network to cache users’ data,
channels, etc. for better load time.
- Error handling.
- The chat server error. There might be hundreds of thousands, or even more persistent
connections to a chat server. If a chat server goes offline, service discovery
(Zookeeper) will provide a new chat server for clients to establish new connections
with.
- Message resent mechanism. Retry and queueing are common techniques for
resending messages.