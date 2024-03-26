# Chapter 14: **Design YouTube**

Design YouTube. The solution to this question can be applied
to other interview questions like designing a video sharing platform such as Netflix and Hulu.

# **Step 1 - Understand the problem and establish design scope**

- C: What features are important?
- I: Upload video + watch video
- C: What clients do we need to support?
- I: Mobile apps, web apps, smart TV
- C: How many DAUs do we have?
- I: 5mil
- C: Average time per day spend on YouTube?
- I: 30m
- C: Do we need to support international users?
- I: Yes
- C: What video resolutions do we need to support?
- I: Most of them
- C: Is encryption required?
- I: Yes
- C: File size requirement for videos?
- I: Max file size is 1GB
- C: Can we leverage existing cloud infra from Google, Amazon, Microsoft?
- I: Yes, building everything from scratch is not a good idea.

### Features, we'll focus on:

- Upload videos fast
- Smooth video streaming
- Ability to change video quality
- Low infrastructure cost
- High availability, scalability, reliability
- Supported clients - web, mobile, smart TV

### **Back of the envelope estimation**

- Assume product has 5M DAU
- Users watch 5 videos per day
- 10% of users upload 1 video per day
- Average video size is 300mb
- Daily storage cost needed - 5mil * 10% * 300mb = 150TB
- CDN Cost, assuming 0.02$ per GB - 5mil * 5 videos * 0.3GB * 0.02$ = USD 150k per day

# **Step 2 - Propose high-level design and get buy-in**

CDN and blob storage are the cloud services we will leverage.

### Here's our system design at a high-level:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/high-level-sys-design.png

- Client - you can watch youtube on web, mobile and TV.
- CDN - videos are stored in CDN.
- API Servers - Everything else, except video streaming goes through the API servers.
    - Feed recommendation, generating video URL, updating metadata db and cache, user signup.

### Let's explore high-level design of video streaming and uploading.

### **Video uploading flow**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/video-uploading-flow.png

- Users watch videos on a supported client
- Load balancer evenly distributes requests across API servers
- All user requests go through API servers, except video streaming
- Metadata DB - sharded and replicated to meet performance and availability requirements

<aside>
💡 metadata? 
This metadata provides details about the video content, such as its title, description, duration, resolution, format, creation date, tags or keywords, …

</aside>

- Metadata cache - for better performance, video metadata and user objects are cached
- A blob storage system is used to store the actual videos
- Transcoding/encoding servers - transform videos to various formats (eg MPEG, HLS, etc) which are suitable for different devices and bandwidth
- Transcoded storage stores result files from transcoding
- Videos are cached in CDN
- Completion queue - stores events about video transcoding results
- Completion handler - a set of workers which pull event data from completion queue and update metadata cache and database

Let's now explore the flow of uploading videos and video metadata. Metadata includes info about video URL, size, resolution, format, etc.

### Video uploading flow:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/video-uploading-flow.png

1. Videos are uploaded to original storage
2. Transcoding servers fetch videos from storage and start transcoding
3. Once transcoding is complete, two steps are executed in parallel:
    - Transcoded videos are sent to transcoded storage and distributed to CDN
    - Transcoding completion events are queued in completion queue, workers pick up the events and update metadata database & cache
4. API servers inform user that uploading is complete

### Metadata update flow:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/metadata-update-flow.png

- While video file is being uploaded, user sends a request to update the video metadata - file name, size, format, etc.
- API servers update metadata database & cache

## **Video streaming flow**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/video-streaming-flow.png

Whenever user watches video it is streaming not downloading; they download a little and start watching it while downloading the rest. 

You don't need to understand those protocols in detail, but some popular streaming protocols are MPEG-DASH, Apple HLS, Microsoft Smooth Streaming, and so on. It is important to understand, though, that different streaming protocols support different video encodings and playback players.

# **Step 3 - Design deep dive**

We will refine two main flows; video uploading flow and video streaming flow with optimization and error handling.

## **Video transcoding**

Video transcoding is important for a few reasons:

- Storage space: Raw video consumes a lot of storage space.
- Browser compatibility: Many browsers have constraints on the type of videos they can support. It is important to encode a video for compatibility reasons.
- To deliver higher resolution video to users with high network bandwidth; 
and lower resolution video to users with low bandwidth

## **Directed Acyclic Graph (DAG) model**

Facebook’s streaming video engine uses a directed acyclic graph (DAG) programming model, which defines tasks in stages so they can be executed sequentially or parallelly. In our design, we adopt a similar DAG model to achieve flexibility and parallelism.

### Parallel tasks

- Inspection: Make sure videos have good quality and are not malformed.
- Video encodings: Videos are converted to support different resolutions, codec, bitrates,
etc. Figure 14-9 shows an example of video encoded files.
- Thumbnail. Thumbnails can either be uploaded by a user or automatically generated by
the system.
- Watermark: An image overlay on top of your video contains identifying information
about your video.

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/dag-model.png

## **Video transcoding architecture**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/video-transcoding-architecture.png

**Preprocessor**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/preprocessor.png

The preprocessor's responsibilities:

- Video splitting - video is split in group of pictures (GOP) alignment, ie arranged groups of chunks which can be played independently
- Cache - intermediary steps are stored in persistent storage in order to retry on failure.
- DAG generation - DAG is generated based on config files specified by programmers.

Example DAG configuration with two steps:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/dag-config-example.png

**DAG Scheduler**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/dag-scheduler.png

DAG scheduler splits a DAG into stages of tasks and puts them in a task queue, managed by a resource manager:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/dag-split-example.png

In this example, a video is split into video, audio and metadata stages which are processed in parallel.

**Resource manager**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/resource-manager.png

Resource manager is responsible for optimizing resource allocation.

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/resource-manager-deep-dive.png

- Task queue is a priority queue of tasks to be executed
- Worker queue is a queue of available workers and worker utilization info
- Running queue contains info about currently running tasks and which workers they're assigned to

How it works:

- task scheduler gets highest-priority task from queue
- task scheduler gets optimal task worker to run the task
- task scheduler instructs worker to start working on the task
- task scheduler binds worker to task & puts task/worker info in running queue
- task scheduler removes the job from the running queue once the job is done

**Task workers**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/task-workers.png

The workers execute the tasks in the DAG. Different workers are responsible for different tasks and can be scaled independently.

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/task-workers-example.png

**Temporary storage**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/temporary-storage.png

Multiple storage systems are used for different types of data. Eg temporary images/video/audio is put in blob storage. Metadata is put in an in-memory cache as data size is small.

Data is freed up once processing is complete.

**Encoded video**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/encoded-video.png

Final output of the DAG. Example output - `funny_720p.mp4`.

### **System Optimizations**

Optimizations for speed, safety, cost-saving.

### **Speed optimization - parallelize video uploading**

We can split video uploading into separate units via GOP alignment:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/video-uploading-optimization.png

This enables fast resumable uploads if something goes wrong. Splitting the video file is done by the client.

### **Speed optimization - place upload centers close to users**

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/upload-centers.png

This can be achieved by leveraging CDNs.

### **Speed optimization - parallelism everywhere**

We can build a loosely coupled system and enable high parallelism.

Currently, components rely on inputs from previous components in order to produce outputs:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/no-parralelism-components.png

We can introduce message queues so that components can start doing their task independently of previous one once events are available:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/parralelism-components.png

### **Safety optimization - pre-signed upload URL**

To avoid unauthorized users from uploading videos, we introduce pre-signed upload URLs:

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/presigned-upload-url.png

How it works:

- client makes request to API server to fetch upload URL
- API servers generate the URL and return it to the client
- Client uploads the video using the URL

### **Safety optimization - protect your videos**

To protect creators from having their original content stolen, we can introduce some safety options:

- Digital right management (DRM) systems - Apple FairPlay, Google Widevine, Microsoft PlayReady
- AES encryption - you can encrypt a video and configure an authorization policy. It is decrypted on playback.
- Visual watermarking - image overlay on top of video which contains your identifying information, eg company name.

### **Cost-saving optimization**

CDN is expensive, as we've seen in our back of the envelope estimation. CDN optimizations:

1. Only serve the most popular videos from CDN and other videos from our high capacity storage video servers (Figure 14-28).
2. For less popular content, we may not need to store many encoded video versions. Short
videos can be encoded on-demand.
3. Some videos are popular only in certain regions. There is no need to distribute these
videos to other regions.
4. Build your own CDN like Netflix and partner with Internet Service Providers (ISPs).

!https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter15/images/cdn-optimization.png

### **Error Handling**

For a large-scale system, errors are unavoidable. To make a fault-tolerant system, we need to handle errors gracefully and recover from them.

There are two types of errors:

- Recoverable error - can be mitigated by retrying a few times. If retrying fails, a proper error code is returned to the client.
- Non-recoverable error - system stops running related tasks and returns proper error code to the client.

Typical errors for each system component are covered by the following playbook:

- Upload error: retry a few times.• Split video error: if older versions of clients cannot split videos by GOP alignment, the entire video is passed to the server. The job of splitting videos is done on the server-side.
- Transcoding error: retry.
- Preprocessor error: regenerate DAG diagram.
- DAG scheduler error: reschedule a task.
- Resource manager queue down: use a replica.
- Task worker down: retry the task on a new worker.
- API server down: API servers are stateless so requests will be directed to a different API
server.
- Metadata cache server down: data is replicated multiple times. If one node goes down,
you can still access other nodes to fetch data. We can bring up a new cache server to
replace the dead one.
    - Metadata DB server down:
    - Master is down. If the master is down, promote one of the slaves to act as the new
    master.
    - Slave is down. If a slave goes down, you can use another slave for reads and bring
    up another database server to replace the dead one

# **Step 4 - Wrap up**

Additional talking points:

- Scaling the API layer - easy to scale horizontally as API layer is stateless
- Scale the database - replication and sharding
- Live streaming - our system is not designed for live streams, but it shares some similarities, eg uploading, encoding, streaming. Notable differences:
    - Live streaming has higher latency requirements so it might demand a different streaming protocol
    - Lower requirement for parallelism as small chunks of data are already processed in real time
    - different error handling, as there is a timeout after which we need to stop retrying
- Video takedowns - videos that violate copyrights, pornography, any other illegal acts need to be removed either during upload flow or based on user flagging.