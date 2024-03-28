# **Step 1 - Understand the problem and establish design scope**

- C: What are most important features?
- I: Upload/download files, file sync and notifications
- C: Mobile or web?
- I: Both
- C: What are the supported file formats?
- I: Any file type
- C: Do files need to be encrypted?
- I: Yes, files in storage need to be encrypted
- C: Is the a file size limit?
- I: Yes, files need to be 10 gb or smaller
- C: How many users does the app have?
- I: 10mil DAU

Features we'll focus on:

- Adding files
- Downloading files
- Sync files across devices
- See file revisions
- Share file with friends
- Send a notification when file is edited/deleted/shared

# Step 2 - Propose high level design and get buy -in

## **Move away from single server**

To scale storage and prevent data loss, consider implementing sharing and replication:

1. **Sharing**: Store each user's data on separate servers (sharding-example).
2. **Replication with Amazon S3**: Use Amazon S3 for file storage with built-in replication for data redundancy (amazon-s3).

Other improvements include:

- **Load Balancing**: Distribute network traffic evenly to web server replicas.
- **Scaling Web Servers**: Easily add more servers with a load balancer.
- **Metadata Database**: Move the database to avoid single points of failure, with replication and sharding for scalability.
- File storage

### **Sync conflicts**

To handle sync conflicts as the user base grows, implement a strategy where the first modification wins (sync-conflict). When a conflict occurs, generate a second version of the file representing the alternative, leaving it to the user to merge (sync-conflict-example).

### High level design

- **User**: Accesses the application via browser or mobile app.
- **Block Servers**: Upload blocks to cloud storage using block-level storage technology.
- **Cloud Storage**: Stores blocks of files, split for efficient storage.
- **Cold Storage**: Designed for storing inactive data.
- **Load Balancer**: Distributes requests evenly among API servers.
- **API Servers**: Responsible for user authentication, profiles, file metadata updates, etc.
- **Metadata Database**: Stores metadata for users, files, blocks, and versions.
- **Metadata Cache**: Caches some metadata for faster retrieval.
- **Notification Service**: Notifies clients of file changes (additions, edits, removals).
- **Offline Backup Queue**: Stores changes for offline clients to sync when online.

# Step 3 - Design deep dive

### Block servers

For large files updated regularly, two optimizations are proposed:

1. **Delta Sync**: Only modified blocks are synced, reducing network traffic.
2. **Compression**: Blocks are compressed based on file types to reduce data size.

In the system, block servers split, compress, and encrypt blocks before uploading to cloud storage. This approach minimizes bandwidth usage by transferring only modified blocks.

### High consistency requirement

The system requires strong consistency by default, ensuring that files appear the same across different clients simultaneously. Strong consistency is essential for the metadata cache and database layers.

To achieve strong consistency:

1. Ensure data consistency between cache replicas and the master.
2. Invalidate caches upon database writes to synchronize cache and database values.

Relational databases are chosen due to their native support for ACID (Atomicity, Consistency, Isolation, Durability) properties, making it easier to maintain strong consistency compared to NoSQL databases that require programmatically incorporating ACID properties.

### Upload flow

1. **Add File Metadata**:
    - Client 1 requests to add metadata for a new file.
    - New file metadata is stored in the metadata database, and file upload status changes to "pending."
    - Notification service is informed about the new file addition.
    - Notification service notifies relevant clients (e.g., Client 2) about the file upload process.
2. **Upload Files to Cloud Storage**:
    - Client 1 uploads the file content to block servers.
    - Block servers segment the file into blocks, compress and encrypt them, and upload to cloud storage.
    - Upon file upload completion, cloud storage triggers an upload completion callback to API servers.
    - File status is updated to "uploaded" in the metadata database.
    - Notification service is informed about the file status change to "uploaded."
    - Relevant clients (e.g., Client 2) are notified by the notification service about the successful file upload.

### Download flow

1. Notification service informs client 2 that a file is changed somewhere else.
2. Once client 2 knows that new updates are available, it sends a request to fetch metadata.
3. API servers call metadata DB to fetch metadata of the changes.
4. Metadata is returned to the API servers.
5. Client 2 gets the metadata.
6. Once the client receives the metadata, it sends requests to block servers to download
blocks.
7. Block servers first download blocks from cloud storage.
8. Cloud storage returns blocks to the block servers.
9. Client 2 downloads all the new blocks to reconstruct the file.

### Notification service

- Enables real-time communication of file changes to clients.
- Clients can communicate via long polling or web sockets.
- Long polling is chosen due to unidirectional communication and infrequent notifications in Google Drive.

### Save storage space

- Deduplicate data blocks to save storage space.
- Implement an intelligent backup strategy with version history limits and aggregation of frequent edits.
- Move infrequently accessed data to cold storage like Amazon S3 Glacier.

### Failure handling

- Load balancer failure: Secondary balancer takes over.
- Block server failure: Other replicas handle the traffic.
- Cloud storage failure: Traffic redirected to other regions.
- API server failure: Load balancer redirects traffic.
- Metadata cache failure: Multiple replicas ensure availability.
- Metadata DB failure: Promote slave to master or use another slave.
- Notification service failure: Clients reconnect to different service replicas; reconnection may take time for many clients.
- Offline backup queue failure: Queues are replicated; consumers resubscribe to backup queues if one fails.