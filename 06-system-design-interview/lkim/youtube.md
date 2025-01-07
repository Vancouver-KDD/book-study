# Step 1 - understand the problem and design scope
- features: upload and watch a video
- clients: mobile apps, web, and smart TV
- DAU: 5 million
- average daily time spent: 30 mins
- international users
- supported video resolutions
- encyrption?
- file size requirements - 1GB
- Using the existing cloud infrastructure by Amazon, Google, Microsoft

### Back of the envelope estimation
- 5 million DAU, watch 5 videos per day.
- 10% of users upload 1 video per day, the average video size is 300 MB.
- Total daily storage space needed: 5 million * 10% * 300 MB = 150TB
- Content delivery network cost (CDN)
  - charged for data transferred out of the CDN
  - Amazon CDN CloudFront, average cost per GB is $0.02 = $150,000 per day
  - Find a way to reduce CDN costs later

# High level design and get buy in
### Why using the existing CDN and blob storages?
- Within the limited time frame, choosing the right technology and mentioning them is better than explaining how the technology works in detail.
- building scalable blob storage or CDN is too complex for interview

## Video uploading flow
### Uploading the actual video
![Screenshot 2025-01-06 201821](https://github.com/user-attachments/assets/17f5a470-c151-4c19-bce5-d8e5014c6873)

2. Transcoding servers fetch videos from the original storage and start transcoding.
3. Once transcoding is complete, the following two steps are executed in parallel:
  3a. Transcoded videos are sent to transcoded storage.
  3b. Transcoding completion events are queued in the completion queue.
3a.1. Transcoded videos are distributed to CDN.
3b.1. Completion handler contains a bunch of workers that continuously pull event data from the queue.
3b.1.a. and 3b.1.b. Completion handler updates the metadata database and cache when video transcoding is complete.
4. API servers inform the client that the video is successfully uploaded and is ready for streaming.

### Updating the metadata flow
- While a file is being uploaded to the original storage, the client in parallel sends a request to update the video metadata.
- The request contains video metadata, including file name, size, format, etc. API servers update the metadata cache and database.

## Video streaming flow
- Streaming: your client loads a little bit of data at a time so you can watch videos immediately and continuously.

### Streaming protocol
- a standardized way to control data transfer for video streaming
- MPEG–DASH, Apple HLS, Microsoft Smooth Streaming, Adobe HTTP Dynamic Streaming (HDS)
- Choose the right streaming protocol to support our use cases, as different streaming protocols support different video encodings and playback players

- Videos are streamed from CDN directly, delivered from the closest server

# Step 3 - design deep dive
## Video transcoding
- the video must be encoded into compatible bitrates and formats, from the recorded format
   - Bitrate: the rate at which bits are processed over time.
   - A higher bitrate generally means higher video quality, needing more processing power

### Why transcoding is important
- Raw video consumes large amounts of storage space
- Many devices and browsers only support certain types of video formats
- For smooth playback, adjust resolution depending on users' network bandwidths
  - Network conditions can change, especially on mobile devices

### Encoding formats consist of
**- Container**
  - contains the video file, audio, and metadata.
  - the format by the file extension, such as .avi, .mov, or .mp4.
**- Codecs**
  - compression and decompression algorithms aim to reduce the video size while preserving the video quality
  - H.264, VP9, and HEVC

## Directed acyclic graph (DAG) model
- To support different video processing pipelines and maintain high parallelism, add some level of abstraction and let client programmers define what tasks to execute.
  - Transcoding a video is computationally expensive and time-consuming
  - Also different content creators may have different video processing requirements

- Facebook’s streaming video engine uses DAG programming model, which defines tasks in stages so they can be executed sequentially or parallelly
![Screenshot 2025-01-06 210026](https://github.com/user-attachments/assets/7b6de8c5-0e23-4928-97ec-34113d0aec98)

## Video transcoding architecture
![Screenshot 2025-01-06 210621](https://github.com/user-attachments/assets/aa922073-f1ba-4104-8db7-55d7ec21be5e)

### Preprocessor's responsibilities
1. Video splitting
  - Video stream is split into smaller Group of Pictures
  - GOP: group/chunk of frames arranged in a specific order. Each chunk is playable
2. For old clients not supporting video splitting, the preprocessor does it
3. DAG generation
  - based on config files that client programmers write.
  - DAG has 2 nodes (download -> transcode) and 1 edge
4. Cache data
  - The preprocessor is a cache for segmented videos.
  - For better reliability, the preprocessor stores GOPs and metadata in temporary storage which can be used for retry

### DAG scheduler
- this splits a DAG graph into stages of tasks and puts them in the task queue in the resource manager.
- the original video is split into three stages
![Screenshot 2025-01-06 212020](https://github.com/user-attachments/assets/331bcce7-d9b2-435e-9b3e-5f2d579833a7)

### Resource manager
- responsible for managing the efficiency of resource allocation.
- It contains 3 queues and a task scheduler
  - Task queue: a priority queue that contains tasks to be executed.
  - Worker queue: a priority queue that contains worker utilization info.
  - Running queue: contains info about the currently running tasks and workers running the tasks.
  - Task scheduler: picks the optimal task/worker, and instructs the chosen task worker to execute the job.
