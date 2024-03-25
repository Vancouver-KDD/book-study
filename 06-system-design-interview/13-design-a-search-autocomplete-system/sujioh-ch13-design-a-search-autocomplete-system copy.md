# Design A Search Autocomplete System

## Step 1 - Understand the problem and establish design scope

### Search Autocomplete Scope

- Supports autocomplete at the beginning of search terms only.
- Returns top 5 suggestions based on popularity.
- Does not support spell check or auto-correct.
- Assumes lowercase characters for queries.
- English language support initially, with potential for multi-language in the future.
- Handles 10 million DAU with an average of 10 searches per user per day.

## Step 2 - Propose high-level design and get buy-in

### Components

- Data gathering service: for real-time query aggregation.
- Query service: to retrieve top suggestions.

## Step 3 - Design deep dive

### Trie Data Structure

- Trie for fast string prefix retrieval.
- Tree structure with nodes for characters.
- Root for empty string, 26 children per node.
- Max prefix length limit for optimization.
- Caching top searches at nodes for faster access.

### Data Gathering Service

### Query Service

- Retrieves suggestions from trie cache or DB.
- Utilizes AJAX requests, data sampling, and browser caching for performance.
- Data sampling
- Browser caching

### Trie Operations

- Create, update, and delete operations managed by workers.

### Scale the storage

- To handle large trie data that can't fit on a single server, we can shard it based on alphabet letters.
- However, this method can lead to uneven data distribution. To address this, a dedicated shard mapper can create a sharding algorithm considering the varying frequency of search terms.

## Step 4 - Wrap up

Other considerations:

- Multi-language support
- Differ search queries across countries
- Trending search queries