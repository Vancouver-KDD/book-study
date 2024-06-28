# CHAPTER 3 - Storage and Retrieval

Two families of storage engines: 
- Log-structured storage engines 
- Page-oriented storage engines such as B-trees.

## Data Structures That Power Your Database
```
#!/bin/bash
db_set () {
echo "$1,$2" >> database 
}
db_get () {
    grep "^$1," database | sed -e "s/^$1,//" | tail -n 1 
}
```
-  db_set key value : to store key and value in the database

```
$ db_set 123456 '{"name":"London","attractions":["Big Ben","London Eye"]}' 
$ db_set 42 '{"name":"San Francisco","attractions":["Golden Gate Bridge"]}' 
$ db_get 42
{"name":"San Francisco","attractions":["Golden Gate Bridge"]}
```
- a text file where each line contains a key-value pair, separated by a comma
- Every call to db_set appends to the end of the file, so if you update a key several times, the old versions of the value are not overwritten

```
$ db_set 42 '{"name":"San Francisco","attractions":["Exploratorium"]}' 
$ db_get 42
{"name":"San Francisco","attractions":["Exploratorium"]} 
$ cat database
123456,{"name":"London","attractions":["Big Ben","London Eye"]} 
42,{"name":"San Francisco","attractions":["Golden Gate Bridge"]} 
42,{"name":"San Francisco","attractions":["Exploratorium"]}
```
> db_set function
- It actually has pretty good performance for something that is so simple, because appending to a file is generally very efficient.
- Similarly to what db_set does, many databases internally use a log, which is an append-only data file.

> db_get function
- It has terrible performance if you have a large number of records in your database. 
- Every time you want to look up a key, db_get has to scan the entire database file from beginning to end, looking for occurrences of the key.
- The cost of a lookup is O(n)

> To make it efficient: **Index**
- An index is an additional structure that is derived from the primary data
- The general idea behind them is to keep some additional metadata on the side
- This doesn’t affect the contents of the database; it only affects the performance of queries.
- Maintaining additional structures incurs overhead, especially on writes.
- Any kind of index usually slows down writes, because the index also needs to be updated every time data is written.
- Trade-off: well-chosen indexes speed up read queries, but every index slows down writes.
- Not necessary, But the application developer or database administrator can then choose the indexes that give your application the greatest benefit, without introducing more overhead than necessary.

**Let's talk about Index!!**

## Hash Indexes
Let’s start with ind
