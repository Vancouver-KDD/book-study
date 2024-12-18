## First step
**Questions to ask**
- Matching at the beginning of a search query or middle too?
- How many autocomplete suggestions to return?
- Spell check, auto correct?
- which language?
- capitalization and special chars?
- How many users

**Requirements**
- Fast response time
- relevance
- sorted by popularity or other ranking
- high availability

**Back of the envelope estimation**
- 10 million DAU
- on average, a person searches 10 times per day
- 20 bytes of data per query string
  - ASCII char encoding
  - a query with 4 words, and each word with 5 chars on average = 4*5 = 20 bytes
- For every chars entered into the search box, the request to BE is sent for autocomplete suggestions
- ~24,000 query per second = 10 million DAU * 10 queries / day * 20 chars / 24h/ 3600 sec
- Peak QPS = ~48,000 = QPS * 2
- 20 % of daily queries are new - 10 million * 10 queries / day * 20 bytes per query * 20% = 0.4 GB
  - This is added to storage daily

## High level design and get buy in
**Two services**
- Data gathering service: gathers user input queries and aggregates them in real time (real-time processing is not practical for large data, but as a starting point)
- Query service: given a query or prefix, return 5 most frequent searched terms

### Data gathering service
- Frequency table is kept per each query with frequency number

### Query service
- As a user types a query in the search box, top 5 frequently searched queries are fetched from the table

### Bottleneck
This only works when the data set is small. When it gets large, accessing the DB becomes a problem

# Design deep dive
• Trie data structure • Data gathering service • Query service • Scale the storage • Trie operations

## Trie (prefix tree) data structure
<img width="685" alt="Screenshot 2024-12-17 at 6 16 59 PM" src="https://github.com/user-attachments/assets/28c3f46f-1a2d-4193-bd0e-26c41f5dba02" />

- a tree-like data structure.
- The root represents an empty string.
- Each node stores a character and has 26 children, one for each possible character. To save space, we do not draw empty links.
- Each tree node represents a single word or a prefix string.

### some definitions
- p: length of a prefix
- n: total number of nodes in a trie
- c: number of children of a given node
  
### example with `tr`
- Find the prefix node “tr”. - O (p) = O(2)
- Traverse the subtree to get all valid children. In this case, nodes [tree: 10], [true: 35], [try: 29] are valid. - O(c) = O(3)
- Sort the children and get top 2. [true: 35] and [try: 29] are the top 2 queries with prefix “tr”. = O(c*logc) = O(3*log3)

### Optimizations
- Overall time complexity O(p) + O(c) + O(clogc)
- how to optimize? Limite the max length of a prefix & cache top search queries at each node

#### Limit the max length of a prefix
- p is likely a small integer number, say 50. Then O(p) becomes O(constant) = O(1)

#### Cache top search queries at each node
- store top k most frequently used queries at each node.
- Since 5 to 10 autocomplete suggestions are enough for users, k is a relatively small number.
- This significantly reduce the time complexity to retrieve the top 5 queries
  - **this design requires a lot of space to store top queries at every node.**
  - Know the trade off between the response time and space
- This makes the time complexity to O(1)

## Data gathering service
- Real-time data update is not realistic
  - Users enter billions of queries per day
  - Top suggestions may not change much once trie is built, unnecessary to update trie frequently except app like Twitter
<img width="685" alt="Screenshot 2024-12-17 at 6 35 53 PM" src="https://github.com/user-attachments/assets/49bd3931-c144-4513-b972-47a5a6db1aa3" />

### Analytic logs
- stores raw data about search queries. Logs are append-only and are not indexed.

### Aggregators
- The size of analytics logs is usually very large, and data is not in the right format. aggregate data so it can be easily processed by our system.
- For real time app, aggregate more often, otherwise like once a week

### Workers
- a set of servers that perform asynchronous jobs at regular intervals. They build the trie data structure and store it in Trie DB.

### Trie Cache
- a distributed cache system that keeps trie in memory for fast read. It takes a weekly snapshot of the DB.

### Trie DB
- the persistent storage
- Option: Document stores like MongoDB or Key-value store as a hash table
  - Key value store: Every prefix in the trie is mapped to a key in the hash table. Data on each trie node is mapped to a value in a hash table

## Query service
### optimizations
Query service requires lightning-fast speed
- AJAX request: sending/receiving a request/response does not refresh the whole web page.
- Browser caching: autocomplete suggestions can be saved in browser cache to allow subsequent requests to get results from the cache directly - Google search engine uses it
- Data sampling: logging every search query requires a lot of processing power and storage. - i.e. only 1 out of every N requests is logged

### Basic steps
- A search query is sent to the load balancer and it routes the request to API servers.
- API servers get trie data from Trie Cache and construct autocomplete suggestions for the client.
- In case the data is not in Trie Cache (or cache miss with out of memory or offline), fetch data back to the cache. Then all subsequent requests for the same prefix are returned from the cache.

## Trie operations
### Create
- created by workers using aggregated data. The source of data is from Analytics Log/DB.

### Update
1. Update the trie regularly like weekly. the new trie replaces the old one
2. Update individual trie node directly - acceptable only when the size of the trie is small

### Delete
- have to remove hateful, violent, sexually explicit, or dangerous autocomplete suggestions.
- a filter layer can be added in front of the Trie Cache to filter out unwanted suggestions.

## Scale the storage 

### Sharding
- By alphabet character? - 26 servers
- But there's **data imbalance** between the number of words starting from each char

### Shard manager
- analyze historical data distribution pattern and apply smarter sharding logic
- shard map manager maintains a lookup database for identifying where rows should be stored.
- i.e. queries starting with `s` and for u,v,w,x,y, and z combined are stored in 
 two shards

## Wrap up
- How to extend to support multiple languages?
  - Unicode characters in trie nodes, which covers all chars in the world
- What if top search queries are different by region/countries
  - build different tries for different countries.
  - To improve the response time, store tries in CDNs (content delivery network)
- How to support trending (real-time) search queries? as a news event breaks out
  - reduce the working data set by sharding
  - change the ranking model and assign more weight to recent search queries
  - Process streaming data - Hadoop MapReduce, Spark Streaming etc
