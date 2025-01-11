- a cloud file storage and synchronization service
- users can store documents, photos, videos, and other files in the cloud.
- users can access files from any computer, smartphone, and tablet and share those with others

# Step 1, understand the problem and design scope
- mobile, web both
- support any file times and encrypt those
- file size limit - 10GB
- 10M DAU

### feature requirements
  - adding and downloading files
  - file synchronization across multiple devices
  - notification if a file is edited/deleted/shared
  - see file revisions
  - share the file with others
  - (excluded here) G Doc - simultaneous editing/collaboration

### non-functional requirements
- reliability is very important - no data loss
- fast sync - can lead to user abandonment
- the economic bandwidth usage
- scalability with high volume
- high availability

# High-level design and get buy in
Start with something simple - build everything in a single server then scale it up
 - A web server to upload and download files.
 - A database to keep track of metadata like user data, login info, files info, etc.
 - A storage system to store files - 1TB of storage space

## APIs
- uploading and downloading a file, and getting file revisions

### Uploading
- simple upload and resumable upload for large files and network interruptions
- resumable API includes params indicating that - `uploadType=resumable`
- eg. https://api.example.com/files/upload?uploadType=resumable

### Downloading
- eg. https://api.example.com/files/download
- the param includes the path
  - eg. { "path": "/recipes/soup/best_soup.txt" }

### Getting file revisions
- eg. https://api.example.com/files/list_revisions
- params include the path to the file and max number of limit
    - eg. { "path": "/recipes/soup/best_soup.txt", "limit": 20 }

## Move away from single server
- Once the storage is close to being full, shard the data (based on user id or else), so it is stored on multiple storage servers.

### Preventing data loss from storage server outage
- “Amazon Simple Storage Service (Amazon S3) is an object storage service that offers
industry-leading scalability, data availability, security, and performance”

### More improvements
**Load balancer:** 
- Add a load balancer to distribute network traffic. A load balancer ensures evenly distributed traffic, and if a web server goes down, it will redistribute the traffic.
**Web servers:**
- After a load balancer is added, more web servers can be added/removed
easily, depending on the traffic load.
**Metadata database:**
- Move the database out of the server to avoid a single point of failure. In the meantime, set up data replication and sharding to meet the availability and
scalability requirements.
**File storage:**
- Amazon S3 is used for file storage. To ensure availability and durability, files are replicated in two separate geographical regions.

## Sync conflicts
If two users try to update the same file at the same time

### Strategy
- the first version that gets processed wins, and the version that gets processed later receives a conflict.
- The system presents both copies of the same file: user 2’s local copy and the latest version from the server (Figure 15-9). User 2 has the option to merge both files or override one version with the other.

## High level design
![Screenshot 2025-01-11 153552](https://github.com/user-attachments/assets/b06d23d0-11ad-4a12-a426-9f57aec3f52d)

### Block servers
- upload blocks to cloud storage.
- Block(-level) storage is a technology to store data files on cloud-based environments.     - A file can be split into several blocks, each with a unique hash value, stored in our metadata database.
   - Each block is treated as an independent object and stored in the storage system (S3).
   - To reconstruct a file, blocks are joined in a particular order.
   - Dropbox reference - the maximal size of a block to 4MB.

### Cold storage
- computer system designed for storing inactive data, meaning files are not accessed for a long time.

### API servers
- responsible for almost everything other than the uploading flow - user authentication, managing user profile, updating file metadata, etc.

### Metadata database
- stores metadata of users, files, blocks, versions, etc. 
- files are stored in the cloud and the metadata database only contains metadata.

### Notification service
- a publisher/subscriber system that allows data to be transferred from notification service to clients as certain events happen.
- notification service notifies relevant clients when a file is added/edited/removed elsewhere so they can pull the latest changes.

### Offline backup queue
- If a client is offline and cannot pull the latest file changes, the offline backup queue stores the info so changes will be synced when the client is online.

# Design deep dive
## Block servers
Sending large files updated regularly consumes a lot of bandwidth. Two optimizations
Delta sync**
Only modified blocks are synced instead of the whole file 

**
