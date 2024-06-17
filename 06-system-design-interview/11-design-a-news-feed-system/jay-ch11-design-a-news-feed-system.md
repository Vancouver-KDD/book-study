# CHAPTER 11: DESIGN A NEWS FEED SYSTEM

- "News feed is the constantly updating list of stories in the middle of your home page"

## Understand scope

- mobile vs web?
- required features
- sort
- how many friends
- traffic volume
- contain media content

## Feed publishing API

- HTTP POST
- Make feed with a content
- Post service: persist post in db and cache
- Fanout service: push new content to friends' news feed stored in cache for fast retrieval
- Notification serice: inform friends with push notification

## Newsfeed retrieval API

- HTTP GET
- Newsfeed service: fetches new feed from the cache
- Newsfeed cache: store new feed IDs needed to render the news feed

## Web servers

- communicating wiht clients
- authentication and rate-limiting

## Fanout service

- delivering a post to all friends
- fanout on write vs fanout on read

### Fanout on write

- pros: real-time, fast because of pre-computing during write
- con: can be slow when a user has too many users, pre-computing can be waste for irregular user

### Fanout on read

- compute on-demand
- pros: better for irregular user, no hotkey problem
- cons: fetching newsfeed is slow because not pre-computed

## Newsfeed retrival

- A user’s news feed fetches feed ids, and all related username, profile picture, post content, post image, etc
- the news feed service fetches the complete user and post objects from caches

## Cache architecture

- News feed: store ids of feed
- Content: stores every post data
- Social graph: user relationship data
- Action: like, dislike, reply
- Counters: counts of actions
