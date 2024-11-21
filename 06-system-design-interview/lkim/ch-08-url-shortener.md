# URL shortener

## 1. Understand the problem and establish design scope
- What is the traffic volume? 100 million URLs are generated per day.
- How long? As short as possible.
- What characters are allowed? a combination of numbers (0-9) and characters (a-z, A- Z).
- Can be deleted or updated? assume cannot be deleted or updated.

### Back of the envelope estimation
- Write operation: 100 million URLs are generated per day.
- Write operation per second: 100 million / 24 /3600 = 1160
- Read operation: Assuming ratio of read operation to write operation is 10:1, read operation per second: 1160 * 10 = 11,600
- Assuming the URL shortener service will run for 10 years, this means we must support 100 million * 365 * 10 = 365 billion records.
- Assume average URL length is 100.
- Storage requirement over 10 years: 365 billion * 100 bytes * 10 years = 365 TB

## 2 - Propose high-level design and get buy-in
### API Endpoints
- facilitate the communication between clients and servers
- two endpoints for shortening the url and redirecting the short url to the long url

### URL redirecting
- Get request from client with the short URL, and with 301 status code, it returns the long URL
- 301 vs 302
  - 301: permanently redirected. The subsequent requests with the same URL won't be sent to the URL shortening service but redirected to the long URL server directly
  - 302: temprarily moved to the long URL. so the subsequent requests will be sent to the URL shortening service
  - Use 301 if reducing the server load is important.
  - Use 302 if analytics is important so it can track source of click better
- The intuition is to use hash tables to redirect URL, by key as shortURL, but...

### URL shortening
- Each longURL must be hashed to one hashValue.
- Each hashValue can be mapped back to the longURL.

## 3 - Design deep dive
### Data model
- Relational DB with id, short url, and long url
  - Hash table is a good starting point but not scalable

### Hash function 
#### Hash value length
- hashValue consists of characters from [0-9, a-z, A-Z], 62 possible characters.
- the length of hashValue = 7 -> find the smallest n such that 62^n ≥ 365 billion, since the system must support up to 365 billion URLs

#### Hash + collision resolution
- Well-known hash functions like CRC32, MD5, SHA-1: but still too long (more than the requirement 7)
- collect first 7 chars then recursively append a new string until no collision, but this is too expensive querying DB for every shortURL request.
- Bloom filter can improve

#### Base 62 conversion
- helps to convert the same number between its different number representation systems. Base 62 conversion is used as there are 62 possible characters for hashValue.

### URL shortening deep dive
- Assuming the input longURL is: https://en.wikipedia.org/wiki/Systems_design
- Unique ID generator returns a new ID: 2009215674938.
- Convert the ID to shortURL using the base 62 conversion. ID (2009215674938) is converted to “zn9edcu”.
- Save ID, shortURL, and longURL to the database

### URL redirecting deep dive
A user clicks a short URL link: https://tinyurl.com/zn9edcu
--> load balancer directs the request to the server
--> the server checks cache, if not in the cache, fetch from the DB
--> return the long URL to the user

## Additional talking points
- Rate limiter
  - A potential security problem - malicious users send an overwhelmingly large number of URL shortening requests.
  - Rate limiter helps to filter out requests based on IP address or other filtering rules.

- Web server scaling
  - Since the web tier is stateless, it is easy to scale the web tier by adding or removing web servers.

- Database scaling: Database replication and sharding are common techniques.

- Analytics
  - Integrating an analytics solution to the URL shortener could help to answer important questions for business

- Availability, consistency, and reliability: the core of any large system
