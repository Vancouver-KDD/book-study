# Chapter 13: Design a Search Authocomplete System
- Autocomplete, typeahead, search-as-you-type, incremental search
- important feature of many products
- Design a search autocomplete system question: aka "design top k" or "design top k most searched queries"

## Step 1: Understand the problem and establish design scope
- clarify requirements
- our requirements:
    - mathich only supported at the beginning of a search query
    - 5 autocomplete suggestions returned by system
    - 5 suggestions determined by popularity, decided by the historical query frequency
    - spell check or autocorrect not supported
    - in english
    - assume all search queries have lowecase alphabetic characters
    - 10 million DAU
- summary of requirements:
    - fast response time
    - relevant
    - sorted
    - scalable
    - highly available

### Back of the envelope estimation
- Assume 10 million daily active users (DAU).
- An average person performs 10 searches per day. 
- 20 bytes of data per query string:
- Assume we use ASCII character encoding. 1 character = 1 byte
- Assume a query contains 4 words, and each word contains 5 characters on average. 
- That is 4 x 5 = 20 bytes per query.
- For every character entered into the search box, a client sends a request to the backend for autocomplete suggestions. 

- On average, 20 requests are sent for each search query. For example, the following 6 requests are sent to the backend by the time you finish typing “dinner”.
    - search?q=d
    - search?q=di
    - search?q=din
    - search?q=dinn
    - search?q=dinne
    - search?q=dinner

- ~24,000 query per second (QPS) = 10,000,000 users * 10 queries / day * 20 characters / 24 hours / 3600 seconds.
- Peak QPS = QPS * 2 = ~48,000
- Assume 20% of the daily queries are new. 10 million * 10 queries / day * 20 byte per query * 20% = 0.4 GB. This means 0.4GB of new data is added to storage daily.

## Step 2: Propose high-level design and get buy-in
- high level- system is broken down in two:
    - Data gathering service
    - Query service

### Data gathering service
    - gathers user input queries and aggregates them in real-time
    - real-time processing not practical for large data sets
        - good starting point
### Query service
- assume frequency table has two fields
    - Query: stores the query string
    - Frequency: represents the number of times a query has been searched

- When data is large, accessing the db becomes a bottleneck

## Step 3: Design deep dive
- Few components to explore optimizations:
    - Trie data structure
    - Data gathering service
    - Query service
    - Scale the storage
    - Trie operations

### Trie data structure
- Relational databases are used but fetching top 5 search queries is inefficient.
- Trie (prefix tree) data structure is used to overcome inefficiency.
- Customized trie design is crucial for the system.
- Basic understanding of trie data structure is essential.
- Trie is a tree-like data structure for compactly storing strings.
- Root represents an empty string, each node stores a character, and has 26 children.
- Each node represents a single word or prefix string.

- How does autocomplete work with trie?
    - p: length of prefix
    - n: total number of nodes in a trie
    - c: number of children of a given node

1. Find the prefix (O(p)).
2. Traverse the subtree to find valid children (O(c)).
3. Sort the children and get the top k (O(clogc)).
    - i.e. User types "tr" -> Find prefix node -> Traverse subtree -> Sort and get top 2 queries.

- Time complexity of ^ algorithm: O(p) + O(c) + O(clogc)

- straightforward, but slow. Optimizations:
    - limit the max length of a prefix
    - cache top search queries at each node

### Data gathering service
- Real-time updates in previous design impractical due to:
    - High volume of user queries
    - Limited change in top suggestions over time
- Scalable data gathering service design focuses on:
    - Origin and usage of data
    - Requirement for up-to-date autocomplete suggestions in real-time apps like Twitter
    - Minimal change in autocomplete suggestions for frequently searched keywords
- Despite use case differences, data gathering foundation remains consistent:
    - Data sourced from analytics or logging services to build trie

#### Analytic logs
- Stores raw data about search queries
- append-only and are not indexed
#### Aggregators
- The size of analytics logs is usually very large
- data usually not in the right format
- thus need to aggregate data so it can be easily processed by our system
#### Aggregated Data
- i.e. "time" field represents the start time of a week, "frequency" field is the sum of occurences for the corresponding query in that week
#### Workers
- set of servers that perform asynchronous jobs at regular intervals
- build the trie data structure and store it in Trie DB
#### Trie Cache
- distributed cache system that keeps trie in memory for fast read
- takes weekly snapshot of the DB
#### Trie DB
- persistent storage
    - Document store: i.e. MongoDB
    - Key-value store
        - every prefix in the trie is mapped to a key in a hash table
        - data on each trie node is mapped to a value in a hash table
### Query Service
- high-level: calls the db directly to fetch the top 5 results.
- improved design:
    1. A search query is sent to the load balancer.
    2. The load balancer routes the request to API servers.
    3. API servers get trie data from Trie Cache and construct autocomplete suggestions for the client.
    4. In case the data is not in Trie Cache, we replenish data back to the cache. This way, all subsequent requests for the same prefix are returned from the cache. A cache miss can happen when a cache server is out of memory or offline.

- Query services require lightning-fast speed. Can proposed following optimizations:
    - AJAX request
    - Browser caching

### Trie operations
- core component of the autocomplete system
#### Create
- Trie is created by workers using aggregated data. Source of data is from Analytics Log/DB
#### Update
- Two ways to update in trie:
    1. Update the trie weekly. Once new trie is created, the new trie replaces the old one
    2. Update individual trie node directly
        - slow
        - if size of trie is small, acceptable solution
#### Delete
- remove hateful, violent, sexually explicit, or dangerous autocomplete suggestions
- add filter layer in front of the trie cache to filter out unwanted suggestions
- gives the flexibility of removing results bbased on different filter rules
- unwanted suggestions are removed physically from the db asynchronically so the correct data set will be used to build trie in the next update cycle

### Scale the storage
- Scalability issue arises when trie grows too large for single server
- Naive sharding based on first character:
    - Split queries from 'a' to 'm' on one server, 'n' to 'z' on another for two servers
    - For three servers: 'a' to 'i', 'j' to 'r', 's' to 'z'
- First level sharding limited to 26 servers due to English alphabet
- Smarter sharding needed to address data imbalance:
    - Historical data distribution analyzed
    - Shard map manager maintains lookup database
    i.e. 's' and 'u' to 'z' combined for similar historical query numbers would have two shards

## Step 4: Wrap up
- Some follow up questions
    - how to extend the design to support multiple languages
        - store Unicode characters in trie nodes
    - what if top search queries in one country different from others?
        - build different tries for different countries
        - can store tries in CDNs to improve response time
    - Support trending (real-time) search queries?
        - Original design limitations:
            - Offline workers not scheduled for trie updates
            - uilding trie takes too long
        - Ideas for real-time search autocomplete:
            - Reduce working data set by sharding
            - Adjust ranking model, prioritize recent queries
            - Streaming data requires different processing systems:
                - Apache Hadoop MapReduce
                - Apache Spark Streaming
                - Apache Storm
                - Apache Kafka