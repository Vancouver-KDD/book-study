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
  - 301: permanently redirected. The subsequeut requests with the same URL won't be sent to the URL shortening service but redirected to the long URL server directly
  - 302: temprarily moved to the long URL. so the subsequent requests will be sent to the URL shortening service
  - Use 301 if reducing the server load is important.
  - Use 302 if analytics is important so it can track source of click better
- Using hash tables to redirect URL, by key as shortURL

### URL shortening
- Each longURL must be hashed to one hashValue.
- Each hashValue can be mapped back to the longURL.

## 3 - Design deep dive
