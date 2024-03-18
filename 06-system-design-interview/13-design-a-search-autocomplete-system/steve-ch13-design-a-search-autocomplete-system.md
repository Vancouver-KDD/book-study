# Chapter 12: Design a Search Autocomplete System

## Step 1 - Understand the problem and establish design scope
- Fast response time: As a user types a search query, autocomplete suggestions must show
up fast enough. An article about Facebook’s autocomplete system [1] reveals that the
system needs to return results within 100 milliseconds. Otherwise it will cause stuttering.
- Relevant: Autocomplete suggestions should be relevant to the search term.
- Sorted: Results returned by the system must be sorted by popularity or other ranking
models.
- Scalable: The system can handle high traffic volume.
- Highly available: The system should remain available and accessible when part of the
system is offline, slows down, or experiences unexpected network errors.
- Assume 20% of the daily queries are new. 10 million * 10 queries / day * 20 byte per
query * 20% = 0.4 GB. This means 0.4GB of new data is added to storage daily.

## Step 2 - Propose high-level design and get buy-in
### Data gathering service
- It gathers user input queries and aggregates them in real-time.
Real-time processing is not practical for large data sets; however, it is a good starting
point. We will explore a more realistic solution in deep dive.
### Query service
- Given a search query or prefix, return 5 most frequently searched terms.

## Step 3 - Design deep dive
### Trie data structure
Relational databases are used for storage in the high-level design. However, fetching the top
5 search queries from a relational database is inefficient. The data structure trie (prefix tree) is
used to overcome the problem. As trie data structure is crucial for the system, we will
dedicate significant time to design a customized trie.

### Data gathering service
- Users may enter billions of queries per day. Updating the trie on every query
significantly slows down the query service.
- Top suggestions may not change much once the trie is built. Thus, it is unnecessary to
update the trie frequently.

### Query service
1. A search query is sent to the load balancer.
2. The load balancer routes the request to API servers.
3. API servers get trie data from Trie Cache and construct autocomplete suggestions for
the client.
4. In case the data is not in Trie Cache, we replenish data back to the cache. This way, all
subsequent requests for the same prefix are returned from the cache. A cache miss can
happen when a cache server is out of memory or offline.

### Trie operations
Trie is a core component of the autocomplete system. Let us look at how trie operations
(create, update, and delete) work.
#### Create
Trie is created by workers using aggregated data. The source of data is from Analytics
Log/DB.
#### Update
There are two ways to update the trie.
- Option 1: Update the trie weekly. Once a new trie is created, the new trie replaces the old
one.
- Option 2: Update individual trie node directly.
#### Delete
We have to remove hateful, violent, sexually explicit, or dangerous autocomplete
suggestions.

### Scale the storage
Now that we have developed a system to bring autocomplete queries to users, it is time to
solve the scalability issue when the trie grows too large to fit in one server.
Since English is the only supported language, a naive way to shard is based on the first
character. Here are some examples.
- If we need two servers for storage, we can store queries starting with ‘a’ to ‘m’ on the
first server, and ‘n’ to ‘z’ on the second server.
- If we need three servers, we can split queries into ‘a’ to ‘i’, ‘j’ to ‘r’ and ‘s’ to ‘z’.

## Step 4 - Wrap up
- Offline workers are not scheduled to update the trie yet because this is scheduled to run
on weekly basis.
- Even if it is scheduled, it takes too long to build the trie.