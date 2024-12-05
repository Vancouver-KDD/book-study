Design a News Feed System

Functional requirements:
- Supported by both mobile app + web app
- publish post
- view friends' posts
- news feed sorted by reverse chronological order / any particular order ?
- max 5000 friends, 10 million DAU
- Feed contains images/videos/txts

Two main flows:
1. Feed Publishing - POST /v1/me/feed (content + auth token)
Main components: User -> Load balancer -> web servers -> (post service + Fanout service + Notification service)
* Post service: persist post in the db and cache
* fancout service: push new content to friends' news feed (cache for fast retrieval)
* notification service: inform friends with new contents

2. Newsfeed bulding - Newsfeed Retrieval API: GET /v1/me/feed (auth token)
Main components: User -> Load balancer -> web servers -> News feed service -> Newsfeed cache

Check diagram

Fanout service: process of delivering a post to all friends
Two types:
1. fanout on write (push model) - new post is delivered to friends immediately after publishing (Fast, but slow and time consuming (hotkey problem), wasts computing resources)
2. fanout on read (pull model) - on-demand model where feed is generated during read time (saves resources for offline users and no hotkey problem, fetching is slow as not pre-computed)

Fanout service Hybrid approach:
1. get friends ids from graph DB
2. get friends data from cache/db
3. send friend list and new post ID to MQ
4. Fanout workers
5. News Feed Cache (post_id : user_id)

NewsFeed Retrieval:
1. usr sends request to retrieve news feed (/v1/me/feed)
2. load balancer / web servers / get postID from cache
3. news feed service fetches the complete user and post objects from cache and construct fully fully hydrated news feed
4. returned as JSON format for rendering

Cache architecture:
- News Feed (news feed)
- Content (hot cache / normal)
- Social graph (follower / following)
- Action (liked / replied / others)
- Counters (like counter / reply counter / other counters)


Scaling the database:
• Vertical scaling vs Horizontal scaling • SQL vs NoSQL
• Master-slave replication
• Read replicas
• Consistency models
• Database sharding

Other talking points:
• Keep web tier stateless
• Cache data as much as you can
• Support multiple data centers
• Lose couple components with message queues
• Monitor key metrics. For instance, QPS during peak hours and latency while users refreshing their news feed are interesting to monitor.