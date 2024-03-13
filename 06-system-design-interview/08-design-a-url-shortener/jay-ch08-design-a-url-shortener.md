# CHAPTER 8: DESIGN A URL SHORTENER

## Step 1 - Understand the problem and establish design scope

- expected behaviour
- traffic volume
- length
- type of characters (alpha numeric)
- delete and update for url
- use cases: URL shortening, redirecting, high availability, scalability, fault tolerance

### Back of the envelop estimation

- write operation: 100 million URLs per day
- write operation per second: 100 million / 24/ 3600 = 1160
- read operation: 10:1 to write
- read operation per second: 100 million _ 365 _ 10 = 11,600
- running fo 10 years, 100 million _ 365 _ 10 = 365 billion records
- average URL length: 100
- storage requirement over 10 years: 365 billion _ 100 bytes _ 10 years = 365 TB

## Step 2 - Propose high-level design and get buy-in

### API endpoints

1. a client sends a POST request, which contains one parameter: the original long URL and return a shortened url
2. get request to the url redirects

### URL redirecting

- server recieves the shortned url, convert it to the original url with 301/302
- 301 redirect: permanentaly moved to the long url (subsequent request will not be made) by cacheing
  - reduce server load
  - not good for analytics
- 302 redirect: temporarily moved to the long url (subsequent request will be made)
  - light on server load
  - good for analytics

### URL shortening

- hash funciton (one to one)
- each long URL => hash value
- each hash value => long url

## Step 3 - Design deep dive

### Data model

- store shortURL, longURL pair in relational database

### Hash function

- a functioon to hash long url to a short url
- hash value length: 62 alphanumeric chars
- find the smallest n such that 62^n ≥ 365 billion (n being the length of the short url)

### Hash + collision resolution

- CRC32, MD5, or SHA-1
- get first 7 characters, check db, if exist, combine it with long url then hash again

### Base 62 conversion

- char value mapping
- 0-0, ..., 9-9, 10-a, 11-b, ..., 35-z, 36-A, ..., 61-Z, where ‘a’ stands for 10, ‘Z’ stands for 61,
- •1115710 =2x622 +55x621+59x620 =[2,55,59]->[2,T,X]inbase62

### URL shortening deep dive

- use base 64
- receive url, check if exist in db
- if exist return short url for it
- if not, generate id, covert id to base 62 conversion and save

### URL redirecting deep dive

1. get request fo a short URL link: https://tinyurl.com/zn9edcu
2. The load balancer forwards the request to web servers
3. If a shortURL is already in the cache, return the longURL directly.
4. If a shortURL is not in the cache, fetch the longURL from the database. If it is not in the database, it is likely a user entered an invalid shortURL.
5. The longURL is returned to the user.

## Step 4 - Wrap up

- Rate limiter: A potential security problem by malicious users with an overwhelmingly large number of URL shortening requests filter them based on IP address or other filtering rules.
- Web server scaling: the web tier is stateless, easy to scale the web tier by adding or removing web servers
- Database scaling: Database replication and sharding are common techniques
- Analytics: Integrating an analytics solution to the URL shortener (how many people click on a link)
- Availability, consistency, and reliability
