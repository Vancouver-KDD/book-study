# CHAPTER 9: DESIGN A WEB CRAWLER

A web crawler is known as a robot or spider.  

It is widely used by search engines to discover new or updated content(a web page, an image, a video, a PDF file, etc) on the web. 

A web crawler starts by collecting a few web pages and then follows links on those pages to collect new content.(Figure 9-1)

![f9-1](img/fg9-1.jpg)

#### Purpose
```
• Search engine indexing:
   This is the most common use case. A crawler collects web pages to create a local index for search engines.
    (For example) Googlebot is the web crawler behind the Google search engine.
• Web archiving:
   This is the process of collecting information from the web to preserve data for future uses.
    (For example) many national libraries run crawlers to archive web sites. Notable examples are the US Library of Congress [1] and the EU web archive [2].
• Web mining:
   The explosive growth of the web presents an unprecedented opportunity for data mining.
   Web mining helps to discover useful knowledge from the internet.
    (For example) top financial firms use crawlers to download shareholder meetings and annual reports to learn key company initiatives.
• Web monitoring.
   The crawlers help to monitor copyright and trademark infringements over the Internet.
    (For example) Digimarc [3] utilizes crawlers to discover pirated works and reports.
```

The complexity of developing a web crawler depends on the scale we intend to support. 
It could be either a small school project, 
  which takes only a few hours to complete or a gigantic project that requires continuous improvement from a dedicated engineering team. 
Thus, we will explore the scale and features to support below.

## Step 1 - Understand the problem and establish design scope

The basic algorithm of a web crawler:
```
1. Given a set of URLs, download all the web pages addressed by the URLs.
2. Extract URLs from these web pages
3. Add new URLs to the list of URLs to be downloaded. Repeat these 3 steps.
```

To understand the requirements and establish design scope:
• Functionalities:
- The main purpose of the crawler? search engine indexing? data mining? something else?
- How many web pages does the web crawler collect per month?
- What content types? HTML only, PDFs and images.
- Newly added or edited web pages? 
- Do we need to store HTML pages crawled from the web?
- Duplicate content?

• Scalability: 
  The web is very large. There are billions of web pages out there. 
  Web crawling should be extremely efficient using parallelization.
• Robustness: 
  The web is full of traps. 
  Bad HTML, unresponsive servers, crashes, malicious links, etc. are all common. 
  The crawler must handle all those edge cases.
• Politeness: 
  The crawler should not make too many requests to a website within a short time interval.
• Extensibility: 
  The system is flexible so that minimal changes are needed to support new content types. 
    For example, if we want to crawl image files in the future, we should not need to redesign the entire system.  

### Back of the envelope estimation

• Estimations(assumptions)
  - Assume 1 billion web pages are downloaded every month.
  - QPS: 1,000,000,000 / 30 days / 24 hours / 3600 seconds = ~400 pages per second.
  - Peak QPS = 2 * QPS = 800
  - Assume the average web page size is 500k.
  - 1-billion-page x 500k = 500 TB storage per month. If you are unclear about digital storage units, go through “Power of 2” section in Chapter 2 again.
  - Assuming data are stored for five years, 500 TB * 12 months * 5 years = 30 PB. A 30 PB storage is needed to store five-year content.

## Step 2 - Propose high-level design and get buy-in
![f9-2](img/fg9-2.jpg)

#### Seed URLs
seed URLs = a starting point for the crawl process

The general strategy
1. locality : Different countries may have different popular websites.
2. topics : shopping, sports, healthcare, etc
   
#### URL Frontier
The component that stores URLs to be downloaded is called the URL Frontier.

#### HTML Downloader
The HTML downloader downloads web pages from the internet. Those URLs are provided by the URL Frontier.
#### DNS Resolver
To download a web page, a URL must be translated into an IP address. 
The HTML Downloader calls the DNS Resolver to get the corresponding IP address for the URL. 
  For instance, URL www.wikipedia.org is converted to IP address 198.35.26.96 as of 3/5/2019.
  
#### Content Parser
```
After a web page is downloaded, it must be parsed and validated
because malformed web pages could provoke problems and waste storage space. 
Implementing a content parser in a crawl server will slow down the crawling process.
Thus, the content parser is a separate component.
```
#### Content Seen?
```
Online research [6] reveals that 29% of the web pages are duplicated contents,
which may cause the same content to be stored multiple times. 
We introduce the “Content Seen?” data structure to eliminate data redundancy and shorten processing time. 
It helps to detect new content previously stored in the system. 
To compare two HTML documents, we can compare them character by character. 
However, this method is slow and time-consuming, especially when billions of web pages are involved. 
An efficient way to accomplish this task is to compare the hash values of the two web pages [7].
```
#### Content Storage
```
It is a storage system for storing HTML content. 
The choice of storage system depends on factors such as data type, data size, access frequency, life span, etc. 
Both disk and memory are used.
• Most of the content is stored on disk because the data set is too big to fit in memory.
• Popular content is kept in memory to reduce latency.
```
#### URL Extractor
```
URL Extractor parses and extracts links from HTML pages.
Figure 9-3 shows an example of a link extraction process.
Relative paths are converted to absolute URLs by adding the “https://en.wikipedia.org” prefix.
```
![f9-3](img/fg9-3.jpg)
#### URL Filter
The URL filter excludes certain content types, file extensions, error links and URLs in “blacklisted” sites.
#### URL Seen?
```
“URL Seen?” is a data structure that keeps track of URLs that are visited before or already in the Frontier.
“URL Seen?” helps to avoid adding the same URL multiple times as this can increase server load and cause potential infinite loops.
Bloom filter and hash table are common techniques to implement the “URL Seen?” component.
We will not cover the detailed implementation of the bloom filter and hash table here.
For more information, refer to the reference materials [4] [8].
```
#### URL Storage
```
URL Storage stores already visited URLs. So far, we have discussed every system component. 
Next, we put them together to explain the workflow.
```
#### Web crawler workflow
```
To better explain the workflow step-by-step, sequence numbers are added in the design diagram as shown in Figure 9-4.
```
![f9-4](img/fg9-4.jpg)
```
Step 1: Add seed URLs to the URL Frontier
Step 2: HTML Downloader fetches a list of URLs from URL Frontier.
Step 3: HTML Downloader gets IP addresses of URLs from DNS resolver and starts downloading.
Step 4: Content Parser parses HTML pages and checks if pages are malformed.
Step 5: After content is parsed and validated, it is passed to the “Content Seen?” component.
Step 6: “Content Seen” component checks if a HTML page is already in the storage.
  • If it is in the storage, this means the same content in a different URL has already been processed.
        In this case, the HTML page is discarded.
  • If it is not in the storage, the system has not processed the same content before. T
        he content is passed to Link Extractor.
Step 7: Link extractor extracts links from HTML pages.
Step 8: Extracted links are passed to the URL filter.
Step 9: After links are filtered, they are passed to the “URL Seen?” component.
Step 10: “URL Seen” component checks if a URL is already in the storage,
        if yes, it is processed before, and nothing needs to be done.
Step 11: If a URL has not been processed before, it is added to the URL Frontier.
```

## Step 3 - Design deep dive
- Depth-first search (DFS) vs Breadth-first search (BFS)
- URL frontier
- HTML Downloader
- Robustness
- Extensibility
- Detect and avoid problematic content

  
### (1) DFS vs BFS

웹 페이지가 노드 역할을 하고 하이퍼링크(URL)가 가장자리(edge) 역할을 하는 방향성 그래프(directed graph)
```
Two common graph traversal algorithms 
- DFS(Not good choice because of the deep depth)
- BFS(Common and is implemented by a first-in-first-out (FIFO)

<Two problems of BFS>
• Most links from the same web page are linked back to the same host.
In Figure 9-5, all the links in wikipedia.com are internal links, making the crawler busy processing URLs from the same host (wikipedia.com).
When the crawler tries to download web pages in parallel, Wikipedia servers will be flooded with requests.
This is considered as “impolite”.

• Standard BFS does not take the priority of a URL into consideration.
The web is large and not every page has the same level of quality and importance.
Therefore, we may want to prioritize URLs according to their page ranks, web traffic, update frequency, etc.
```

![f9-5](img/fg9-5.jpg)
### (2) URL frontier
```
URL frontier helps to address these problems
(The web is large and not every page has the same level of quality and importance. 
  Therefore, we may want to prioritize URLs according to their page ranks, web traffic, update frequency, etc). 
A URL frontier is a data structure that stores URLs to be downloaded. 
The URL frontier is an important component to ensure politeness, URL prioritization, and freshness.
```
##### Politeness
```
Generally, a web crawler should avoid sending too many requests to the same hosting server within a short period.
Sending too many requests is considered as “impolite” or even treated as denial-of-service (DOS) attack.
   For example, without any constraint, the crawler can send thousands of requests every second to the same website.
   This can overwhelm the web servers.
The general idea of enforcing politeness is to dow   nload one page at a time from the same host.
A delay can be added between two download tasks.
The politeness constraint is implemented by maintain a mapping from website hostnames to download (worker) threads. 
Each downloader thread has a separate FIFO queue and only downloads URLs obtained from that queue.
Figure 9-6 shows the design that manages politeness.
```
![f9-6](img/fg9-6.jpg)
```
• Queue router: It ensures that each queue (b1, b2, … bn) only contains URLs from the same host.
• Mapping table: It maps each host to a queue.
• FIFO queues b1, b2 to bn: Each queue contains URLs from the same host.
• Queue selector: Each worker thread is mapped to a FIFO queue,
   and it only downloads URLs from that queue. The queue selection logic is done by the Queue selector.
• Worker thread 1 to N. A worker thread downloads web pages one by one from the same host.
   A delay can be added between two download tasks.
```
![t9-1](img/t9-1.jpg)
##### Priority
```
A random post from a discussion forum about Apple products carries very different weight 
than posts on the Apple home page. Even though they both have the “Apple” keyword, it is 
sensible for a crawler to crawl the Apple home page first.
We prioritize URLs based on usefulness, which can be measured by PageRank [10], website 
traffic, update frequency, etc. “Prioritizer” is the component that handles URL prioritization. 
Refer to the reference materials [5] [10] for in-depth information about this concept. 
Figure 9-7 shows the design that manages URL priority.

• Prioritizer: It takes URLs as input and computes the priorities.
• Queue f1 to fn: Each queue has an assigned priority. Queues with high priority are selected with higher probability.
• Queue selector: Randomly choose a queue with a bias towards queues with higher priority.
```
![f9-7](img/fg9-7.jpg)

Figure 9-8 presents the URL frontier design, and it contains two modules:
```
• Front queues: manage prioritization
• Back queues: manage politeness
```

![f9-8](img/fg9-8.jpg)


##### Freshness
```
Web pages are constantly being added, deleted, and edited. A web crawler must periodically 
recrawl downloaded pages to keep our data set fresh.
Recrawl all the URLs is time-consuming and resource intensive.

Few strategies to optimize freshness are listed as follows:
• Recrawl based on web pages’ update history.
• Prioritize URLs and recrawl important pages first and more frequently.
```
##### Storage for URL Frontier
```
In real-world crawl for search engines, the number of URLs in the frontier could be hundreds of millions [4].
Putting everything in memory is neither durable nor scalable.
Keeping everything in the disk is undesirable neither because the disk is slow; and it can easily become a bottleneck for the crawl.
We adopted a hybrid approach.
The majority of URLs are stored on disk, so the storage space is not a problem.
To reduce the cost of reading from the disk and writing to the disk, we maintain buffers in memory for enqueue/dequeue operations.
Data in the buffer is periodically written to the disk.
```
### (3) HTML Downloader
```
The HTML Downloader downloads web pages from the internet using the HTTP protocol. 
Before discussing the HTML Downloader, we look at Robots Exclusion Protocol first.
```
#### Robots.txt
```
Robots.txt, called Robots Exclusion Protocol, is a standard used by websites to communicate with crawlers.
It specifies what pages crawlers are allowed to download. Before attempting to crawl a web site, a crawler should check its corresponding robots.txt first and follow its rules. 
To avoid repeat downloads of robots.txt file, we cache the results of the file.
The file is downloaded and saved to cache periodically.
Here is a piece of robots.txt file taken from https://www.amazon.com/robots.txt.
Some of the directories like creatorhub are disallowed for Google bot.
User-agent: Googlebot
Disallow: /creatorhub/*
Disallow: /rss/people/*/reviews
Disallow: /gp/pdp/rss/*/reviews
Disallow: /gp/cdp/member-reviews/
Disallow: /gp/aw/cr/
Besides robots.txt, performance optimization is another important concept we will cover for the HTML downloader.
```

#### Performance optimization
1. Distributed crawl
```
To achieve high performance, crawl jobs are distributed into multiple servers, and each server runs multiple threads.
The URL space is partitioned into smaller pieces; so, each downloader is responsible for a subset of the URLs.
Figure 9-9 shows an example of a distributed crawl.
```
2. Cache DNS Resolver
![f9-9](img/fg9-9.jpg)
```
DNS Resolver is a bottleneck for crawlers because DNS requests might take time due to the synchronous nature of many DNS interfaces.
DNS response time ranges from 10ms to 200ms. Once a request to DNS is carried out by a crawler thread, other threads are blocked until the first request is completed.
Maintaining our DNS cache to avoid calling DNS frequently is an effective technique for speed optimization.
Our DNS cache keeps the domain name to IP address mapping and is updated periodically by cron jobs.
```
3. Locality
```
Distribute crawl servers geographically. When crawl servers are closer to website hosts, 
crawlers experience faster download time. Design locality applies to most of the system 
components: crawl servers, cache, queue, storage, etc.
```
4. Short timeout
```
Some web servers respond slowly or may not respond at all. To avoid long wait time, a maximal wait time is specified.
If a host does not respond within a predefined time, the crawler will stop the job and crawl some other pages.
```
### (4) Robustness
```
Besides performance optimization, robustness is also an important consideration.
We present a few approaches to improve the system robustness:
• Consistent hashing: This helps to distribute loads among downloaders.
   A new downloader server can be added or removed using consistent hashing.
   Refer to Chapter 5: Design consistent hashing for more details.
• Save crawl states and data: To guard against failures, crawl states and data are written to a storage system.
   A disrupted crawl can be restarted easily by loading saved states and data.
• Exception handling: Errors are inevitable and common in a large-scale system.
   The crawler must handle exceptions gracefully without crashing the system.
• Data validation: This is an important measure to prevent system errors.
```
### (5) Extensibility
```
As almost every system evolves, one of the design goals is to make the system flexible enough to support new content types.
The crawler can be extended by plugging in new modules. Figure 9-10 shows how to add new modules.
```
![f9-10](img/fg9-10.jpg)
```
• PNG Downloader module is plugged-in to download PNG files.
• Web Monitor module is added to monitor the web and prevent copyright and trademark infringements.
```
### (6) Detect and avoid problematic content
```
This section discusses the detection and prevention of redundant, meaningless, or harmful 
content.
1. Redundant content
   As discussed previously, nearly 30% of the web pages are duplicates. Hashes or checksums help to detect duplication [11].
2. Spider traps
A spider trap is a web page that causes a crawler in an infinite loop. For instance, an infinite deep directory structure is listed as follows:
www.spidertrapexample.com/foo/bar/foo/bar/foo/bar/…
Such spider traps can be avoided by setting a maximal length for URLs. However, no one-size-fits-all solution exists to detect spider traps.
Websites containing spider traps are easy to identify due to an unusually large number of web pages discovered on such websites.
It is hard to develop automatic algorithms to avoid spider traps;
however, a user can manually verify and identify a spider trap, and either exclude those websites from the crawler or apply some customized URL filters.
3. Data noise
Some of the contents have little or no value, such as advertisements, code snippets, spam URLs, etc.
Those contents are not useful for crawlers and should be excluded if possible.
```
