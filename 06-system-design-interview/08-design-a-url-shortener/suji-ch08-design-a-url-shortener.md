# Chapter 8. **Design of a URL shortener**

### **Design a URL Shortener**

Design a service like https://tinyurl.com/app 

**Step 1 - Understand the problem and establish design scope**

- C: Can you give an example of how a URL shortening service works?
    - I: Given URL `https://www.systeminterview.com/q=chatsystem&c=loggedin&v=v3&l=long` and alias `https://tinyurl.com/y7keocwj`. You open the alias and get to the original URL.
- C: What is the traffic volume?
    - I: 100 million URLs are generated per day.
- C: How long is the shortened URL?
    - I: As short as possible
- C: What characters are allowed?
    - I: numbers and letters
- C: Can shortened URLs be updated or deleted?
    - I: For simplicity, let's assume they can't.

Other functional requirements - high availability, scalability, fault tolerance.

**Back of the envelope calculation**

- Write operation: 100 million URLs are generated per day.
    - rite operation per second: 100 million / 24 /3600 = 1160
- Read operation: Assuming ratio of read operation to write operation is 10:1, read
operation per second: 1160 * 10 = 11,600
- Storage requirement 365 TB:
- Assuming the URL shortener service will run for 10 years, this means we must support
100 million * 365 * 10 = 365 billion records.
    - Assume average URL length is 100.
    - Storage requirement over 10 years: 365 billion * 100 bytes * 10 years = 365 TB

### **Step 2 - Propose high-level design and get buy-in**

**API Endpoints**

A URL shortening service needs two endpoints:

`POST api/v1/data/shorten` URL Shortening 

- request parameter: {longUrl: longURLString}
- return shortURL

`GET api/v1/shortUrl` URL redirecting 

- Return longURL for HTTP redirection

**URL Redirecting**

How it works:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter09/images/tinyurl-example.png

***301 redirect vs. 302 redirect***

- 301 (Permanently moved) - indicates that the URL **permanently** points to the new URL. the browser caches the response; **subsequence requests are redirected to the long URL server directly.**
- 302 (Temporarily moved) - indicates that the URL is **temporarily** moved to the new URL. **Subsequent requests for the same URL will be sent to the URL shortening service first.**

Choose 301 if you want **to avoid extra server load**. Choose 302 if **tracking analytics** is important.

Easiest way to implement the URL redirection is to store the `<shortURL, longURL>` pair in an in-memory hash-table.

**URL Shortening**

To support the URL shortening use case, we must find a hash function fx that maps a long URL to the hashValue.

### **Step 3 - Design deep dive**

Storing `<shortURL, longURL>` pairs in hash table is not feasible; as memory resources are limited and expensive. A better option is to store `<shortURL, longURL>` mapping in a relational
database. 

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter09/images/url-table.png

**Hash function**

- Length:
    - When n = 7, 62 ^ n = ~3.5 trillion, 3.5 trillion is more than enough to hold 365 billion URLs,
    so the length of hashValue is 7.

<aside>
💡 62 menas: `[0-9a-zA-Z]`

</aside>

- Method
    - `base62 conversion` or `hash + collision detection`.

**Hash + Collision Detection**

In the latter case, we can use something like MD-5 or SHA256, but only taking the first 7 characters; since even CRC32 the length is 8. To resolve collisions, we can reiterate \w an some padding to input string until there is no collision:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter09/images/hash-collision-mechanism.png

The problem with this method is that we have to query the database to detect collision. Bloom filters could help in this case.

**Base62 Conversion**

1. **Generate a unique identifier**: This could be a numeric ID or hash value that uniquely identifies the webpage "chat.openai.com".
2. **Convert the identifier to base 62**: Let's say the unique identifier is the numeric value "100". We convert this number to base 62, resulting in a shorter string, for example, "M1".
    - 100 ÷ 62 = 1 with remainder 38 (character 'M')
    - 1 ÷ 62 = 0 with remainder 1 (character '1')
    
    So, the base 62 representation of 100 is "M1".
    
3. **Use the shortened string in the URL**: We append the shortened string to a base URL, such as "https://openai.com/chat/", resulting in the shortened URL "[https://openai.com/chat/](https://openai.com/chat/1k8p0t)M1".

**Comparison between the two approaches:**

| Hash + collision resolution | Base 62 conversion |
| --- | --- |
| Fixed short URL length. | Short URL length is not fixed. It goes up with the ID. |
| Does not need a unique ID generator. | This option depends on a unique ID generator. |
| Collision is possible and needs to be resolved. | Collision is not possible because ID is unique. |
| It’s not possible to figure out the next available short URL because it doesn’t depend on ID. | It is easy to figure out what is the next available short URL if ID increments by 1 for a new entry. This can be a security concern. |

### **URL shortening deep dive**

To keep our service simple, we'll use base62 encoding for the URL shortening.

Here's the whole workflow:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter09/images/url-shortening-deep-dive.png

1. longURL is the input.
2. The system checks if the longURL is in the database.
3. If it is, it means the longURL was converted to shortURL before. In this case, fetch the
shortURL from the database and return it to the client.
4. If not, the longURL is new. A new unique ID (primary key) Is generated by the unique
ID generator.
5. Convert the ID to shortURL with base 62 conversion.
6. Create a new database row with the ID, shortURL, and longURL.

### **URL redirection deep dive**

We've introduced a cache as there are more reads than writes, in order to improve read performance:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter09/images/url-redirection-deep-dive.png

1. A user clicks a short URL link: https://tinyurl.com/zn9edcu
2. The load balancer forwards the request to web servers.
3. If a shortURL is already in the cache, return the longURL directly.
4. If a shortURL is not in the cache, fetch the longURL from the database. If it is not in the
database, it is likely a user entered an invalid shortURL.
5. The longURL is returned to the user.

**Step 4 - Wrap up**

We discussed:

- API design
- data model
- hash function
- URL shortening
- URL redirecting

Additional talking points:

- Rate limiter - We can introduce a rate limiter to protect us against malicious actors, trying to make too many URL shortening requests.
- Web server scaling - We can easily scale the web tier by introducing more service instances as it's stateless.
- Database scaling - Replication and sharding are a common approach to scale the data layer.
- Analytics - Integrating analytics tracking in our URL shortener service can reap some business insights for clients such as "how many users clicked the link".
- Availability, consistency, reliability - At the core of every distributed systems.