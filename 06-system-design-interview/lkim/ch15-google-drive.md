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
- process files passed from clients by splitting a file into blocks, compressing each block, and
encrypting them. Instead of uploading the whole file to the storage system, only modified
blocks are transferred.

![Screenshot 2025-01-11 160414](https://github.com/user-attachments/assets/26a6007a-26fc-428f-a930-208a2840ea3b)

- Sending large files updated regularly consumes a lot of bandwidth. Two optimizations
1. **Delta sync**: Only modified blocks are synced instead of the whole file 
2. **Compression**: Applying compression on blocks can significantly reduce the data size, using compression algorithms depending on file types.

## High consistency requirement
- It is unacceptable for a file to be shown differently by different clients at the same time.
- The system needs to provide **strong consistency (not eventual consistency) for metadata cache and database layers**.

### What to ensure
- Data in cache replicas and the master is consistent.
- Invalidate caches on database write to ensure cache and database hold the same value.

### Relational DB and strong conssitency
- Easy for relational DB maintaining the ACID (Atomicity, Consistency, Isolation, Durability) properties
- NoSQL DB must programmatically support the ACD

## Metadata DB tables
- User basic info
- Device info: including push_id for notifications
- Namespace: root directory of a user
- File: name, path, latest version, if is directory, timestamp
- File_version: version history of a file, read-only for integrity
- Block: everything related to a file block. A file of any version can be reconstructed by joining all the blocks in the correct order.

## Upload flow (edit too)
![Screenshot 2025-01-13 213824](https://github.com/user-attachments/assets/ba8fc40d-c724-4b4d-bd69-787bd8c6fcea)

## Download flow
![Screenshot 2025-01-13 214334](https://github.com/user-attachments/assets/234233f3-9332-4306-9056-4b18f9eab6e0)

- If client A is offline while a file is changed by another client, data will be saved to the cache. When the offline client is online again, it pulls the latest changes.

## Notification service
- To maintain file consistency, any mutation of a file performed locally needs to be informed to other clients to reduce conflicts.

### Options 
**- Long polling** - Dropbox
    - Chosen as communication for notification service is one direction, server to the client
    - each client keeps a long poll connection to the notification service 
    - after a response is received or connection timeout is reached, a client immediately sends a new request to keep the connection open.

- WebSocket - persistent bi-directional, better suited for real-time bi-directional communication such as a chat app. G Drive notifications are infrequent, no burst of data

## Save storage space
Storage space can be filled up quickly with frequent backups of all file revisions. 3 techniques to reduce storage costs

#### De-duplicate data blocks
- Eliminating redundant blocks at the account level to save space, for the blocks with the same hash value

#### Adopt an intelligent data backup strategy
• Set a limit: We can set a limit for the number of versions to store. If the limit is
reached, the oldest version will be replaced with the new version.
• Keep valuable versions only: Some files might be edited frequently. For example,
saving every edited version for a heavily modified document could mean the file is
saved over 1000 times within a short period. To avoid unnecessary copies, we could
limit the number of saved versions. We give more weight to recent versions.
Experimentation is helpful to figure out the optimal number of versions to save.
• Moving infrequently used data to cold storage. Cold data is the data that has not been
active for months or years. Cold storage like Amazon S3 glacier [11] is much cheaper than
S3.
