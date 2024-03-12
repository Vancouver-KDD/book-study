# Chapter 11: Design a News Feed System

## Step 1 - Understand the problem and establish design scope

Candidate: Is this a mobile app? Or a web app? Or both?</br>
Interviewer: Both

Candidate: What are the important features?</br>
Interview: A user can publish a post and see her friends’ posts on the news feed page.

Candidate: Is the news feed sorted by reverse chronological order or any particular order
such as topic scores? For instance, posts from your close friends have higher scores.</br>
Interviewer: To keep things simple, let us assume the feed is sorted by reverse chronological
order.

Candidate: How many friends can a user have?</br>
Interviewer: 5000

Candidate: What is the traffic volume?</br>
Interviewer: 10 million DAU

Candidate: Can feed contain images, videos, or just text?</br>
Interviewer: It can contain media files, including both images and videos.

## Step 2 - Propose high-level design and get buy-in
The design is divided into two flows: feed publishing and news feed building.
- Feed publishing: when a user publishes a post, corresponding data is written into cache
and database. A post is populated to her friends’ news feed.
- Newsfeed building: for simplicity, let us assume the news feed is built by aggregating
friends’ posts in reverse chronological order.

### Newsfeed APIs
The news feed APIs are the primary ways for clients to communicate with servers.
- Feed publishing API
  - To publish a post, a HTTP POST request will be sent to the server.
  - content: content is the text of the post.
  - auth_token: it is used to authenticate API requests.
- Newsfeed retrieval API
  - To retrieve a post, a HTTP GET request will be sent to the server.
  - auth_token: it is used to authenticate API requests.

### Feed publishing
- User: a user can view news feeds on a browser or mobile app. A user makes a post with
content “Hello” through API:
/v1/me/feed?content=Hello&auth_token={auth_token}
- Load balancer: distribute traffic to web servers.
- Web servers: web servers redirect traffic to different internal services.
- Post service: persist post in the database and cache.
- Fanout service: push new content to friends’ news feed. Newsfeed data is stored in the
cache for fast retrieval.
- Notification service: inform friends that new content is available and send out push
notifications.

### Newsfeed building
- User: a user sends a request to retrieve her news feed. The request looks like this:
/ v1/me/feed.
- Load balancer: load balancer redirects traffic to web servers.
- Web servers: web servers route requests to newsfeed service.
- Newsfeed service: news feed service fetches news feed from the cache.
- Newsfeed cache: store news feed IDs needed to render the news feed.

## Step 3 - Design deep dive

### Feed publishing deep dive
#### Web servers
Besides communicating with clients, web servers enforce authentication and rate-limiting.
Only users signed in with valid auth_token are allowed to make posts. The system limits the
number of posts a user can make within a certain period, vital to prevent spam and abusive
content.
#### Fanout service
Fanout is the process of delivering a post to all friends. Two types of fanout models are:
fanout on write (also called push model) and fanout on read (also called pull model). Both
models have pros and cons. We explain their workflows and explore the best approach to
support our system.
#### Fanout on write.
With this approach, news feed is pre-computed during write time. A new
post is delivered to friends’ cache immediately after it is published.
Pros:
- The news feed is generated in real-time and can be pushed to friends immediately.
- Fetching news feed is fast because the news feed is pre-computed during write time.
Cons:
- If a user has many friends, fetching the friend list and generating news feeds for all of
them are slow and time consuming. It is called hotkey problem.
- For inactive users or those rarely log in, pre-computing news feeds waste computing
resources.
#### Fanout on read. The news feed is generated during read time. This is an on-demand model.
Recent posts are pulled when a user loads her home page.
- Pros:
  - For inactive users or those who rarely log in, fanout on read works better because it will
not waste computing resources on them.
  - Data is not pushed to friends so there is no hotkey problem.
- Cons:
  - Fetching the news feed is slow as the news feed is not pre-computed.

The fanout service works as follows:
1. Fetch friend IDs from the graph database.
2. Get friends info from the user cache.
3. Send friends list and new post ID to the message queue.
4. Fanout workers fetch data from the message queue and store news feed data in the news
feed cache.
5. Store <post_id, user_id > in news feed cache.

### Newsfeed retrieval deep dive
1. A user sends a request to retrieve her news feed. The request looks like this: /v1/me/feed
2. The load balancer redistributes requests to web servers.
3. Web servers call the news feed service to fetch news feeds.
4. News feed service gets a list post IDs from the news feed cache.
5. A user’s news feed is more than just a list of feed IDs. It contains username, profile
picture, post content, post image, etc. Thus, the news feed service fetches the complete
user and post objects from caches (user cache and post cache) to construct the fully
hydrated news feed.
6. The fully hydrated news feed is returned in JSON format back to the client for
rendering.

### Cache architecture
- News Feed: It stores IDs of news feeds.
- Content: It stores every post data. Popular content is stored in hot cache.
- Social Graph: It stores user relationship data.
- Action: It stores info about whether a user liked a post, replied a post, or took other
actions on a post.
- Counters: It stores counters for like, reply, follower, following, etc.

## Step 4 - Wrap up
Scaling the database:
- Vertical scaling vs Horizontal scaling
- SQL vs NoSQL
- Master-slave replication
- Read replicas
- Consistency models
- Database sharding

Other talking points:
- Keep web tier stateless
- Cache data as much as you can
- Support multiple data centers
- Lose couple components with message queues
- Monitor key metrics. For instance, QPS during peak hours and latency while users
refreshing their news feed are interesting to monitor.
