### What is Web Crawler - a robot or spider
- used by search engines to discover new or updated content on the web - a web page, an image, a video, a PDF file, etc.
- A web crawler starts by collecting a few web pages and then follows links on those pages to collect new content.
- The complexity depends on the scale to support.
- Basic algorithms
  - 1. Given a set of URLs, download all the web pages addressed by the URLs.
  - 2. Extract URLs from these web pages
  - 3. Add new URLs to the list of URLs to be downloaded. Repeat these 3 steps.

#### Usages
- Search engine indexing: collects web pages to create a local index for search engines.
    - i.e. Googlebot is the web crawler behind the Google search engine.
- Web archiving: collecting information from the web to preserve data for future uses.
    - i.e.  national libraries run crawlers to archive web sites
- Web mining: helps to discover useful knowledge from the internet.
    - i.e. financial firms use crawlers to download shareholder meetings and annual reports to learn key company initiatives.
- Web monitoring: monitor copyright and trademark infringements over the Internet.

## Understand the problem and establish design scope
**Functionality to clarify **
- main purpose, search engine indexing, data mining, or something else?
- How many web pages to collect?
- Content types? HTML or else
- How long to store the content
- how to handle duplicates

**Non-functional characteristics**
- Scalability: should be extremely efficient using parallelization, as the web is very large.
- Robustness: The web is full of traps. Bad HTML, unresponsive servers, crashes, malicious links, etc. are all common. The crawler must handle all those edge cases.
- Politeness: The crawler should not make too many requests to a website within a short time interval.
- Extensibility: The system is flexible so minimal changes are needed to support new content types.

## High-level design and get buy-in
![Screenshot 2024-11-25 192411](https://github.com/user-attachments/assets/145b1e3d-702b-4fea-9fd1-086506d6d19a)

#### Seed URL
- starting point that a crawler can utilize to traverse as many links as possible.
- Strategy: divide the entire URL space into smaller ones.
    - The first proposal is based on locality as different countries may have different popular websites.
    - Another is to choose seed URLs based on topics; shopping, sports etc

#### URL Frontier
- Web crawlers states split into two: to be downloaded and already downloaded.
- URL Frontier stores URLs to be downloaded as FIFO

#### HTML Downloader
- downloads web pages from the internet. Those URLs are provided by the URL Frontier.

#### DNS Resolver
- DNS Resolver gets the corresponding IP address for the URL. To download a web page, a URL must be translated into an IP address.
- i.e. URL www.wikipedia.org is converted to IP address 198.35.26.96

#### Content Parser
- After a web page is downloaded, it must be parsed and validated to avoid problems from malformed data and waste
- the content parser is separated from crawling server for low latency
  
#### Content Seen?
- To eliminate data redundancy and shorten processing time, it helps to detect new content previously stored in the system.
- Compare the hash values of the two web pages - comparing chars by char is too slow

#### Content Storage
- a storage system for storing HTML content.
- depends on data type, data size, access frequency, life span, etc. 
- Popular content is in memory, and most content is on disk to reduce latency.

#### URL Extractor
- parses and extracts links from HTML pages.
- Relative paths are converted to absolute URLs by adding a prefix.

#### The URL filter
- excludes certain content types, file extensions, error links and URLs in “blacklisted” sites.

#### URL Seen?
-# keeps track of URLs that are visited before or already in the Frontier not to increase server load and cause potential infinite loops.
- Bloom filter and hash table are common techniques to implement it

#### URL Storage
- stores already visited URLs.

#### workflow
- Step 6: “Content Seen” component checks if a HTML page is already in the storage.
  - If it is in the storage, this means the same content in a different URL has already been processed. In this case, the HTML page is discarded.
  - If it is not in the storage, the system has not processed the same content before. The content is passed to Link Extractor.
- Step 7: Link extractor extracts links from HTML pages.
- Step 8: Extracted links are passed to the URL filter.
- Step 9: After links are filtered, they are passed to the “URL Seen?” component.
- Step 10: “URL Seen” component checks if a URL is already in the storage, if yes, it is processed before, and nothing needs to be done.
- Step 11: If a URL has not been processed before, it is added to the URL Frontier.

## Design deep dive
• Depth-first search (DFS) vs Breadth-first search (BFS)
• URL frontier
• HTML Downloader
• Robustness
• Extensibility
• Detect and avoid problematic content

### DFS vs BFS
- Web: as a directed graph where web pages serve as nodes and hyperlinks (URLs) as edges.
- The crawl process can be seen as traversing a directed graph from one web page to others.
- BFS is commonly used and is implemented by a FIFO queue.
  - DFS can go too deep

**Problem with BFS**
- Impolite: Most links from the same web page are linked back to the same host. When the crawler tries to download web pages in parallel, the host servers will be flooded with requests. Or even considered as DOS attack
- Standard BFS does not take the priority of a URL into consideration. We want to prioritize URLs according to their page ranks, web traffic, update frequency, etc.

### URL frontier
- ensure politeness, URL prioritization, and freshness.

#### Politeness
- download one page at a time from the same host. A delay can be added between two download tasks.
- maintain a mapping from website hostnames to download (worker) threads.
- Each downloader thread has a separate FIFO queue and only downloads URLs obtained from that queue.
- Queue router: It ensures that each queue (b1, b2, … bn) only contains URLs from the same host.
- Mapping table: It maps each host to a queue.
- FIFO queues b1, b2 to bn: Each queue contains URLs from the same host.
- Queue selector: Each worker thread is mapped to a FIFO queue, and it only downloads URLs from that queue. The queue selection logic is done by the Queue selector.
- Worker thread 1 to N. A worker thread downloads web pages one by one from the same host. A delay can be added between two download tasks.

#### Priority
- prioritize URLs based on usefulness, which can be measured by PageRank, website traffic, update frequency, etc.
- Queue f1 to fn: Each queue has an assigned priority.
- Queue selector: Randomly choose a queue with a bias towards queues with higher priority.

#### Freshness
- A web crawler must periodically recrawl downloaded pages to keep our data set fresh.
- Recrawl based on web pages’ update history.
- Prioritize URLs and recrawl important pages first and more frequently.

#### Storage for URL Frontier
- The majority of URLs are stored on disk
- To reduce the cost of reading from the disk and writing to the disk, maintain buffers in memory for enqueue/dequeue operations.
- Data in the buffer is periodically written to the disk.

### HTML Downloader
- downloads web pages from the internet using the HTTP protocol.

#### Robots Exclusion Protocol
- a standard used by websites to communicate with crawlers.
- specifies what pages crawlers are allowed to download.
- a crawler should check its corresponding robots.txt first and follow its rules.
- robots.txts file is downloaded and cached

### Performance optimization for HTML downloader
#### Distributed crawl
- For high performance, crawl jobs are distributed into multiple servers, and each server runs multiple threads.

#### Cache DNS Resolver
- DNS Resolver is a bottleneck for crawlers because DNS requests might take time due to the synchronous nature of many DNS interfaces.
- Maintain DNS cache to avoid calling DNS frequently and update periodically

#### Locality
- Distribute crawl servers geographically. crawl servers, cache, queue, storage, etc.

#### Short timeout
- Set a maximal wait time

### Approaches for Robustness
- Consistent hashing: to distribute loads among downloaders. A new downloader server can be added or removed using consistent hashing.
- Save crawl states and data: To guard against failures, crawl states and data are written to a storage system. A disrupted crawl can be restarted easily by loading saved states and
data.
- Exception handling: Errors are inevitable and common in a large-scale system. The crawler must handle exceptions gracefully without crashing the system.
- Data validation: This is an important measure to prevent system errors.

### Extensibility
- PNG Downloader module is plugged-in to download PNG files.
- Web Monitor module is added to monitor the web and prevent copyright and trademark infringements.
![Screenshot 2024-11-25 203038](https://github.com/user-attachments/assets/4fcd83d4-eaf9-4397-897b-5be8eec91345)

### Detect and avoid problematic content
#### Redundant content
- Hashes or checksums help to detect duplication

#### Spider traps
- a web page that causes a crawler in an infinite loop.
  - i.e. www.spidertrapexample.com/foo/bar/foo/bar/foo/bar/…
- can be avoided by setting a maximal length for URLs, no perfect solution though

#### Data noise
Some of the contents have little or no value, such as advertisements, code snippets, spam URLs, etc

## More talking points
- Server-side rendering
  - Numerous websites use scripts like JavaScript, AJAX, etc to generate links on the fly. If we download and parse web pages directly, we will not be able to retrieve dynamically generated links. So perform server-side rendering (also called dynamic rendering) first before parsing a page

- Filter out unwanted pages: With finite storage capacity and crawl resources, an anti-spam component is beneficial in filtering out low quality and spam pages

- Availability, consistency, and reliability
  - Database replication and sharding: Techniques like replication and sharding are used to improve the data layer availability, scalability, and reliability.
  - Horizontal scaling: hundreds or even thousands of servers are needed
- Analytics: Collecting and analyzing data
