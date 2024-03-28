# CHAPTER 14: DESIGN YOUTUBE

- Total number of monthly active users: 2 billion.
- Number of videos watched per day: 5 billion.
- 73% of US adults use YouTube.
- 50 million creators on YouTube.
- YouTube’s Ad revenue was $15.1 billion for the full year 2019, up 36% from 2018.
- YouTube is responsible for 37% of all mobile internet traffic.
- YouTube is available in 80 different languages.

## Step 1 - Understand the problem and establish design scope

- Features: upload, watch video, comment, share, like/dislike, save to playlist, subscribe to a channel
- Clients: mobile, web, smart tv
- Daily active users: 5mil
- Average daily screen time: 30 min
- Support international users
- Support video resolutions
- Encryption
- File size = 1gb
- Use existing cloud services

### Back of the envelope estimation

- 5 million daily active users
- 5 videos per day.
- 10% of users upload 1 video per day.
- the average video size is 300 MB.
- Total daily storage space needed: 5 million _ 10% _ 300 MB = 150TB
- Amazon’s CDN CloudFront for cost estimation.
- Assume 100% of traffic is served from the United States.
- The average cost per GB is $0.02
- 5 million _ 5 videos _ 0.3GB \* $0.02 = $150,000 per day.

## Step 2 - Propose high-level design and get buy-in

- choosing right tech is also important
- Videos stream from 3rd party cloud service CDN
- API servers are for everything else

### Upload flow

- Original storage: where original vidoes are stored
- Transcoding servers: video encoding, converting to different formats
- Transcoded storage: formatted videos stored for CDN
- CDN: cache videos for clients to play
- Completion queue for video message queue
- Update is either uploading actual video + file name, size, format

### Video streaming flow

- streaming protocol: MPEG–DASH (“Moving Picture Experts Group” and DASH stands for "Dynamic Adaptive Streaming over HTTP".)
- different streaming protocols support different video encodings

## Step 3 - Design deep dive

- why Video transcoding?
- Raw videos comsume a large space
- compatibility
- network condition
- encoding formats have two parts: Container (.avi, .mov, or .mp4.) and Codecs (H.264, VP9, and HEVC)

### Directed acyclic graph (DAG) model

- define tasks in stages so they can be executed sequentially or parallelly
- the original video is split into video, audio, and metadata

### Video transcoding architecture

- The architecture has six main components: preprocessor, DAG scheduler, resource manager, task workers, temporary storage, and encoded video as the output
- preprocessor: video splitting, splitting by GOD alignment for old clients, DAG generation, cache data for segmented videos
- DAG scheduler: splits DAG graph into stages of tasks and put in the queue in the resource manager
- Resource manager: managaing effeicency of resource allocation, task queue, worker queue, running queue, task scheduler and task scheduler assign highest priorty to an optimal worker

### Task workers

- run tasks in the queue from the DAG
- watermark, encoder, thumbnail, merger

### Temporary storage

- good for metadata

### Encoded video

- final output of the encoding pipeline

### Speed optimization

- parallelize video uploading, split videos in the chunks by GOP alignment, upload in parallel
- place upload centers close to users, setting up multiple upload centers across the globe, CDN as upload centers.
- parallelism everywhere, build a loosely coupled system and enable high parallelism.

### Safety optimization

- issue pre-signed upload URL to ensure only authorized users upload videos to the right location
- protect videos by Digital rights management, AES encryption, visual watermarking

### Cost-saving optimization

- only serve the most popular vids from CDN and rest from first party servers
- no encoding for less popular ones or short ones
- or ones that are popular in a region
- build your own CDN

### Error handling

- recoverable error: retry
- non-recoverable: return error code

- Upload, transcoding: retry
- split video error: old browser did not handle splitting, handlie on server
- Preprocessor error: regenerate DAG diagram
- DAG scheduler error: reschedule a task
- Resource manager queue down: use a replica
- Task worker down: retry the task on a new worker
- API server down: API servers are stateless so requests will be directed to a different API server.
- Metadata cache server down: data is replicated multiple times. a new cache server to replace the dead one.

## Step 4 - Wrap up

- Scale the API tier: easy to scale API tier horizontally
- Scale the database: database replication and sharding
- Live streaming:
  - live streaming and non-live streaming similarities: both require uploading, encoding, and streaming.
  - differences
    - Live streaming has a higher latency requirement, so it might need a different streaming protocol.
    - Live streaming has a lower requirement for parallelism because small chunks of data are already processed in real-time.
    - Live streaming requires different sets of error handling. Any error handling that takes too much time is not acceptable.
- Video takedowns: Videos that violate copyrights, pornography, or other illegal acts shall be removed
