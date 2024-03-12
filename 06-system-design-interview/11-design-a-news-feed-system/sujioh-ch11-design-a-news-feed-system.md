# **Chapter 11. Design a News Feed System**

**What is news feed?** 

- constantly updating list of stories on your home page.
- It includes status updates, photos, videos, links, etc.

### **Step 1 - Understand the problem and establish design scope**

First step is to clarify what the interviewer has in mind exactly:

- C: Mobile, web app?
    - I: Both
- C: What are the important **features**?
    - I: User can publish posts and see friends' posts on news feed.
- C: Is news feed **sorted** in reverse chronological order or based on rank, eg best friends' posts first.
    - I: To keep it simple, let's assume reverse chrono order
- C: **Max number of friends**?
    - I: 5000
- C: **Traffic volume**?
    - I: 10mil DAU
- C: Can the feed contain **media**?
    - I: It can contain images and video

### **Step 2 - Propose high-level design and get buy-in**

**Two flows in the design:**

- Feed publishing
    - when a user publishes a post, corresponding data is written into cache and database. A post is populated to her friends’ news feed.
- Newsfeed building
    - Newsfeed building: for simplicity, let us assume the news feed is built by aggregating friends’ posts in reverse chronological order.

**Newsfeed API**

Those APIs are HTTP based that allow clients to perform actions; i.e. posting a status, retrieving news feed, adding friends, etc. 

We discuss two most important APIs: 

- feed publishing: `POST /v1/me/feed`
    - Params:
        - content: content is the text of the post.
        - auth_token:
- news feed retrieval API: `GET /v1/me/feed`
    - Params
        - uth_token

**Feed publishing** 

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter12/images/feed-publishign.png

- User: a user can view news feeds on a browser or mobile app. A user **makes a post** with
content “Hello” through API: *`/v1/me/feed?content=Hello&auth_token={auth_token}`*
- Load balancer: distribute traffic to web servers.
- Web servers: web servers redirect traffic to different internal services.
- Post service: persist post in the database and **cache**.

<aside>
💡 what can be cached in `Post Cache`?

- User makes a post ("Hello") via API.
- Post service persists post in the database.
- Post service also caches the post data.
- **When the user who made the post refreshes their feed, the post is quickly retrieved from the cache instead of the database.**
</aside>

- Fanout service: push new content to friends’ news feed. **Newsfeed data is stored in the
cache for fast retrieval.**

<aside>
💡 what can be cached in `newsfeed cache` ?

- When someone posts new content, like a photo, it's sent to their friends' news feeds by a service called "fanout."
- This new content, along with updates from friends, is stored in a shared cache called the "newsfeed cache."
- When any user checks their own news feed, they see the latest content, including updates from friends, quickly retrieved from this shared newsfeed cache.
</aside>

- Notification service: inform friends that new content is available and send out push
notifications.

**Newsfeed building**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter12/images/newsfeed-building.png

- User: a user sends a request to retrieve her news feed. The request looks like this:
`/ v1/me/feed`.
- Load balancer: load balancer redirects traffic to web servers.
- Web servers: web servers route requests to newsfeed service.
- Newsfeed service: news feed service fetches news feed from the cache.
- Newsfeed cache: store news feed IDs needed to render the news feed.

### **Step 3 - Design deep dive**

Let's discuss the two flows we covered in more depth.

**Feed publishing deep dive**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter12/images/feed-publishing-deep-dive.png

**Web servers**

- besides a gateway to the internal services, these do **authentication** and apply **rate limits**, in order to prevent spam.

**Fanout service - Fanout on write (push model) vs. fanout on read (pull model).**

**Fanout on write (push model)** 

- posts are **pre-computed** during post publishing.

Pros:

- The news feed is generated in real-time and can be pushed to friends immediately.
- **Fetching news feed is fast because the news feed is pre-computed during write time.**

Cons:

- **If a user has many friends**, fetching the friend list and generating news feeds for all of them are slow and time consuming. It is called **hotkey** problem.
- For inactive users or those rarely log in, pre-computing news feeds **waste computing
resources.**

**Fanout on read (pull model) - news feed is generated during read time.**

Pros:

- Works better for inactive users, as news feeds are not generated for them.
- Data is not pushed to friends, hence, no hotkey problem.

Cons:

- Fetching the news feed is slow as it's not pre-computed.

We'll adopt a hybrid approach - we'll **pre-compute the news feed for people without many friends** and **use the pull model for celebrities and users with many friends/followers.**

System diagram of fanout service:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter12/images/fanout-service.png

- Fetch friend IDs from **graph** database. They're suited for managing friend relationships and recommendations.
- Get friends info from user cache. Filtering is applied here for eg muted/blocked friends.
- Send friends list and post ID to the message queue.
- Fanout workers fetch the messages and store the news feed data in a cache. They store a `<post_id, user_id>` mappings** in it which can later be retrieved.
- * I think there is some kind of error in this part of the book. It doesn't make sense to store a `<post_id, user_id>` mapping in the cache. Instead, it should be a `<user_id, post_id>` mapping as that allows one to quickly fetch all posts for a given user, which are part of their news feed. In addition to that, the example in the book shows that you can store multiple user_ids or post_ids as keys in the cache, which is typically not supported in eg a hashmap, but it is actually supported when you use the `Redis Sets` feature, but that is not explicitly mentioned in the chapter.

**News feed retrieval deep dive**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter12/images/news-feed-retrieval-deep-dive.png

- user sends request to retrieve news feed.
- Load balancer distributes request to a set of web servers.
- Web servers call news feed service.
- News feed service gets a list of `post_id` from the news feed cache.
- Then, the posts in the news feed are hydrated with usernames, content, media files, etc.
- Fully hydrated news feed is returned as a JSON to the user.
- Media files are also stored in CDN and fetched from there for better user experience.

**Cache architecture**

Cache is very important for a news feed service. We divided it into 5 layers:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter12/images/cache-layer.png

- news feed - stores ids of news feeds
- content - stores every post data. Popular content is stored in hot cache.
- social graph - store user relationship data.
- action - store info about whether a user liked, replied or took actions on a post.
- counters - counters for replies, likes, followers, following, etc.

### **Step 4 - wrap up**

In this chapter, we designed a news feed system and we covered two main use-cases - feed publishing and feed retrieval.

Talking points, related to scalability:

- vertical vs. horizontal database scaling
- SQL vs. NoSQL
- Master-slave replication
- Read replicas
- Consistency models
- Database sharding

Other talking points:

- keep web tier stateless
- cache data as much as possible
- multiple data center setup
- Loose coupling components via message queues
- Monitoring key metrics - QPS and latency.