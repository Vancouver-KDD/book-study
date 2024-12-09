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
- Once received new messages, it restart the polling
- Drawback
    - Sender and receiver may not connect to the same chat server.
    - A server has no good way to tell if a client is disconnected.
    - inefficient. If a user does not chat much, long polling still makes periodic connections after timeouts

### WebSocket
- most common solution for sending asynchronous updates from server to client.
- initiated by the client - bi-directional and persistent.
- Also used for sending and receiver both - simplifies the design
