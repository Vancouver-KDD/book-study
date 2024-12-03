- News feed is the constantly updating list of stories in the middle of your home page. News Feed includes status updates, photos, videos, links, app activity, etc
- Questions: design Facebook news feed, Instagram feed, Twitter timeline, etc.

## Understanding the problem
- mobile or web?
- important features? ie: user can publish a post and see friends' post
- Order? chronological or friends?
- Number of max friends
- Traffic volume
- Can feed contain images videos, only texts

# Newsfeed APIs
- the primary ways for client to communicate with servers - HTTP based
- Feed publishing and newsfeed retrieval

## Feed publishing API
- To publish a post
- params: content, auth_token

<img width="694" alt="Screenshot 2024-12-02 at 8 08 27 PM" src="https://github.com/user-attachments/assets/8ac66dae-a09b-4dbc-8b04-0bfe348500f1">

### Web servers
- enforce authentication and rate-limiting.
- limits the number of posts a user can make within a certain period, to prevent spam and abusive content.

### Fanout service
- push new content to friends’ news feed. Newsfeed data is stored in the cache for fast retrieval.
- use hybrid of fanout on write and read
  - For most users, do fanout on write for fast experience
  - For celebrities with many friends/followers, do fanout on read (on demand)
    - Consistent hashing to mitigate the hotkey problem
<img width="518" alt="Screenshot 2024-12-02 at 8 38 24 PM" src="https://github.com/user-attachments/assets/b1189ae3-a6be-4133-a5e0-87b75e49de5b">


#### Fanout on write
- news feed is pre-computed during write time. A new post is delivered to friends’ cache immediately after it is published.
- Pros: updated in real-time, fetching is fast
- Cons
  - Fetching the friend list and generating the news feeds for many friends are slow - hotkey problem
  - For inactive/not too active users, precomputing news feed is waste
#### Fanout on read
- news feed is generated during read time. This is an on-demand model
- Pros
  - For inactive/not too active users, it doesn't waste resource
  - No hotkey issue since data is not published to friends
- Cons
  - fetching the news feed is slower

### Notification service
- inform friends that new content is available and send out push notifications.

## Newsfeed retrieval API
- To retrieve news feed, with param of autho_token
- Newsfeed service: news feed service fetches news feed from the cache.
- Newsfeed cache: store news feed IDs needed to render the news feed.

<img width="601" alt="Screenshot 2024-12-02 at 8 38 54 PM" src="https://github.com/user-attachments/assets/3072131c-0f60-48c5-879a-5bee730a4091">

4. News feed service gets a list post IDs from the news feed cache.
5. news feed service fetches the complete user and post objects from user cache and post cache to construct the fully hydrated news feed.
6. The fully hydrated news feed is returned in JSON format back to the client for rendering.

## Cache architecture
Divide the cache tier into 5 layers
- News Feed: It stores IDs of news feeds.
- Content: It stores every post data. Popular content is stored in hot cache.
- Social Graph: It stores user relationship data.
- Action: It stores info about whether a user liked a post, replied a post, or took other actions on a post.
- Counters: It stores counters for like, reply, follower, following, etc.

## Additional talking points
### Scalability
- Vertical scaling vs Horizontal scaling
- SQL vs NoSQL
- Master-slave replication
- Read replicas
- Consistency models
- Database sharding

### Other
- Keep web tier stateless
- Cache data as much as you can
- Support multiple data centers
- Lose couple components with message queues
- Monitor key metrics - QPS during peak hours and latency while users refreshing their news feed
