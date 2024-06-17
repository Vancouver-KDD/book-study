# Chapter 11. Design a news feed system
## Functional Requirement
- Functional requirement is quite simple.  Uploading posts, viewing posts. 
- Let's solely focus on this requirement.


## Rephrase the requirement
- Upload a post : Simple, very usual case of CRUD. 
- Reading a post : 
- Reading posts:
  - What 'reading posts' entail? 
    - Imagine you open the instagram or facebook app. 'Feed' is pre-generated and ready-to-be-read.
    - This simple means:
        ```SQL
        SELECT content, created_at 
        FROM posts
        WHERE 
            posts.writer is user's friend -- ???
        ```
        ```SQL
        SELECT content, created_at 
        FROM posts p
        LEFT JOIN friendships f
            p.writer_id = f.followed
        WHERE 
            f.following = reader_id
        LIMIT 10
        ```
    And this can be problematic if it gets scaled. Denoting number of user as N, Retrieving time complexity is proportional to N^2 (bottleneck : Friendship, given post is properly indexed.)


### Solution 1 : Fan-out on write (Fanning out 'write' notification)

### Solution 2 : Fan-out on read (Fanning out 'read' request)

- Pivoting POV is difficult. At some point you need to think from Writer's perspective, and you need to switch to reader's perspective.


### Real solution : Combination of 1 & 2
- Although it sounds too trivial...
- There's no silver bullet-  There's no elegant solution that fits into all scenarios. 
  - Finding sweet spot - is the gist of the problem. (and requires a good design for the experiement)