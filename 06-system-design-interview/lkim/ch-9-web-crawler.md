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
![Screenshot 2024-11-25 164748](https://github.com/user-attachments/assets/ba826549-4f4f-4b2d-bab5-7756a326cd55)

### Seed URL
- starting point that a crawler can utilize to traverse as many links as possible.
- Strategy: divide the entire URL space into smaller ones.
    - The first proposal is based on locality as different countries may have different popular websites.
    - Another is to choose seed URLs based on topics; shopping, sports etc

### URL Frontier
- Web crawlers states split into two: to be downloaded and already downloaded.
- URL Frontier stores URLs to be downloaded as FIFO

### HTML Downloader
- downloads web pages from the internet. Those URLs are provided by the URL Frontier.

### DNS Resolver
- DNS Resolver gets the corresponding IP address for the URL. To download a web page, a URL must be translated into an IP address.
- i.e. URL www.wikipedia.org is converted to IP address 198.35.26.96

### Content Parser
- After a web page is downloaded, it must be parsed and validated to avoid problems from malformed data and waste
- the content parser is separated from crawling server for low latency
  
### Content Seen?
- To eliminate data redundancy and shorten processing time, it helps to detect new content previously stored in the system.
- Compare the hash values of the two web pages - comparing chars by char is too slow

### Content Storage
- a storage system for storing HTML content.
- depends on data type, data size, access frequency, life span, etc. 
- Popular content is in memory, and most content is on disk to reduce latency.

### URL Extractor

