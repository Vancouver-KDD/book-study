# Chapter 14: Design Youtube

- Assumetion
  - Total number of monthly active users: 2 billion.
  - Number of videos watched per day: 5 billion.
  - 73% of US adults use YouTube.
  - 50 million creators on YouTube.
  - YouTube’s Ad revenue was $15.1 billion for the full year 2019, up 36% from 2018.
  - YouTube is responsible for 37% of all mobile internet traffic.
  - YouTube is available in 80 different languages.

## Step 1 - Understand the problem and establish design scope

Ask questions to narrow down the scope.
- Key features
  - Ability to upload videos fast
  - Smooth video streaming
  - Ability to change video quality
  - Low infrastructure cost
  - High availability, scalability, and reliability requirements
  - Clients supported: mobile apps, web browser, and smart TV

- Back of the envelope estimation
  - Assume the product has 5 million daily active users (DAU).
  - Users watch 5 videos per day.
  - 10% of users upload 1 video per day.
  - Assume the average video size is 300 MB.
  - Total daily storage space needed: 5 million * 10% * 300 MB = 150TB
  - CDN cost.
    - When cloud CDN serves a video, you are charged for data transferred out of the CDN.
    - Let us use Amazon’s CDN CloudFront for cost estimation. Assume 100% of traffic is served from the United States. The average cost per GB is $0.02. For simplicity, we only calculate the cost of video streaming.
    - 5 million * 5 videos * 0.3GB * $0.02 = $150,000 per day.

## Step 2 - Propose high-level design and get buy-in
At the high-level, the system comprises three components
### Client: You can watch YouTube on your computer, mobile phone, and smartTV.
### CDN: Videos are stored in CDN. When you press play, a video is streamed from the CDN.
### API servers: Everything else except video streaming goes through API servers. This includes
feed recommendation, generating video upload URL, updating metadata database and cache,
user signup, etc.
In the question/answer session, the interviewer showed interests in two flows:
- Video uploading flow
  - User: A user watches YouTube on devices such as a computer, mobile phone, or smart
TV.
  - Load balancer: A load balancer evenly distributes requests among API servers.
  - API servers: All user requests go through API servers except video streaming.
  - Metadata DB: Video metadata are stored in Metadata DB. It is sharded and replicated to
meet performance and high availability requirements.
  - Metadata cache: For better performance, video metadata and user objects are cached.
  - Original storage: A blob storage system is used to store original videos. A quotation in
Wikipedia regarding blob storage shows that: “A Binary Large Object (BLOB) is a
collection of binary data stored as a single entity in a database management system” [6].
  - Transcoding servers: Video transcoding is also called video encoding. It is the process of
converting a video format to other formats (MPEG, HLS, etc), which provide the best
video streams possible for different devices and bandwidth capabilities.
  - Transcoded storage: It is a blob storage that stores transcoded video files.
  - CDN: Videos are cached in CDN. When you click the play button, a video is streamed
from the CDN.
  - Completion queue: It is a message queue that stores information about video transcoding
completion events.
  - Completion handler: This consists of a list of workers that pull event data from the
completion queue and update metadata cache and database.

#### Upload the actual video.
1. Videos are uploaded to the original storage.
2. Transcoding servers fetch videos from the original storage and start transcoding.
3. Once transcoding is complete, the following two steps are executed in parallel:
4. Transcoded videos are sent to transcoded storage.
5. Transcoding completion events are queued in the completion queue.
6. Transcoded videos are distributed to CDN.
7. Completion handler contains a bunch of workers that continuously pull event data
from the queue.
8. 1.a. and 3b.1.b. Completion handler updates the metadata database and cache when
video transcoding is complete.
9. API servers inform the client that the video is successfully uploaded and is ready for
streaming.

#### Update the metadata
While a file is being uploaded to the original storage, the client in parallel sends a request to
update the video metadata.

- Video streaming flow
Popular streaming protocols are:
  - MPEG–DASH. MPEG stands for “Moving Picture Experts Group” and DASH stands for
"Dynamic Adaptive Streaming over HTTP".
  - Apple HLS. HLS stands for “HTTP Live Streaming”.
  - Microsoft Smooth Streaming.
  - Adobe HTTP Dynamic Streaming (HDS).

## Step 3 - Design deep dive
### Video transcoding
Video transcoding is important for the following reasons:
  - Raw video consumes large amounts of storage space. An hour-long high definition video
recorded at 60 frames per second can take up a few hundred GB of space.
  - Many devices and browsers only support certain types of video formats. Thus, it is
important to encode a video to different formats for compatibility reasons.
  - To ensure users watch high-quality videos while maintaining smooth playback, it is a
good idea to deliver higher resolution video to users who have high network bandwidth
and lower resolution video to users who have low bandwidth.
  - Network conditions can change, especially on mobile devices. To ensure a video is
played continuously, switching video quality automatically or manually based on network
conditions is essential for smooth user experience.
Many types of encoding formats are available; however, most of them contain two parts:
  - Container: This is like a basket that contains the video file, audio, and metadata. You can
tell the container format by the file extension, such as .avi, .mov, or .mp4.
  - Codecs: These are compression and decompression algorithms aim to reduce the video
size while preserving the video quality. The most used video codecs are H.264, VP9, and
HEVC.

### Directed acyclic graph (DAG) model
- Inspection: Make sure videos have good quality and are not malformed.
- Video encodings: Videos are converted to support different resolutions, codec, bitrates,
etc. Figure 14-9 shows an example of video encoded files.
- Thumbnail. Thumbnails can either be uploaded by a user or automatically generated by
the system.
- Watermark: An image overlay on top of your video contains identifying information
about your video.

### Video transcoding architecture
The architecture has six main components: preprocessor, DAG scheduler, resource manager,
task workers, temporary storage, and encoded video as the output.

- Preprocessor
1. Video splitting. Video stream is split or further split into smaller Group of Pictures (GOP)
alignment. GOP is a group/chunk of frames arranged in a specific order. Each chunk is an
independently playable unit, usually a few seconds in length.
2. Some old mobile devices or browsers might not support video splitting. Preprocessor split
videos by GOP alignment for old clients.
3. DAG generation. The processor generates DAG based on configuration files client
programmers write. Figure 14-12 is a simplified DAG representation which has 2 nodes and
1 edge:
This DAG representation is generated from the two configuration files below (Figure 14-13):
4. Cache data. The preprocessor is a cache for segmented videos. For better reliability, the
preprocessor stores GOPs and metadata in temporary storage. If video encoding fails, the
system could use persisted data for retry operations.
- DAG scheduler
Stage 1: video, audio,
and metadata. The video file is further split into two tasks in stage 2: video encoding and
thumbnail. The audio file requires audio encoding as part of the stage 2 tasks.
- Resource manager
  - Task queue: It is a priority queue that contains tasks to be executed.
  - Worker queue: It is a priority queue that contains worker utilization info.
  - Running queue: It contains info about the currently running tasks and workers running
the tasks.
  - Task scheduler: It picks the optimal task/worker, and instructs the chosen task worker to
execute the job.
- Task workers
Different task workers may run different tasks
- Temporary storage
Multiple storage systems are used here. The choice of storage system depends on factors like
data type, data size, access frequency, data life span, etc. For instance, metadata is frequently
accessed by workers, and the data size is usually small. Thus, caching metadata in memory is
a good idea. For video or audio data, we put them in blob storage. Data in temporary storage
is freed up once the corresponding video processing is complete.
- Encoded video
Encoded video is the final output of the encoding pipeline.

### System optimizations
#### Speed optimization: parallelize video uploading
- We can split a video into smaller chunks by GOP alignment.

#### Speed optimization: place upload centers close to users
- Another way to improve the upload speed is by setting up multiple upload centers across the globe

#### Speed optimization: parallelism everywhere
- Achieving low latency requires serious efforts. Another optimization is to build a loosely coupled system and enable high parallelism.
  - Before the message queue is introduced, the encoding module must wait for the output of
the download module.
  - After the message queue is introduced, the encoding module does not need to wait for the
output of the download module anymore. If there are events in the message queue, the
encoding module can execute those jobs in parallel.

#### Safety optimization: pre-signed upload URL
1. The client makes a HTTP request to API servers to fetch the pre-signed URL, which
gives the access permission to the object identified in the URL. The term pre-signed URL
is used by uploading files to Amazon S3. Other cloud service providers might use a
different name. For instance, Microsoft Azure blob storage supports the same feature, but
call it “Shared Access Signature”.
2. API servers respond with a pre-signed URL.
3. Once the client receives the response, it uploads the video using the pre-signed URL.

#### Safety optimization: protect your videos
- Digital rights management (DRM) systems: Three major DRM systems are Apple
FairPlay, Google Widevine, and Microsoft PlayReady.
- AES encryption: You can encrypt a video and configure an authorization policy. The
encrypted video will be decrypted upon playback. This ensures that only authorized users
can watch an encrypted video.
- Visual watermarking: This is an image overlay on top of your video that contains
identifying information for your video. It can be your company logo or company name.

#### Cost-saving optimization
1. Only serve the most popular videos from CDN and other videos from our high capacity
storage video servers (Figure 14-28).
2. For less popular content, we may not need to store many encoded video versions. Short
videos can be encoded on-demand.
3. Some videos are popular only in certain regions. There is no need to distribute these
videos to other regions.
4. Build your own CDN like Netflix and partner with Internet Service Providers (ISPs).
Building your CDN is a giant project; however, this could make sense for large streaming
companies. An ISP can be Comcast, AT&T, Verizon, or other internet providers. ISPs are
located all around the world and are close to users. By partnering with ISPs, you can
improve the viewing experience and reduce the bandwidth charges.

### Error handling
- Recoverable error. For recoverable errors such as video segment fails to transcode, the
general idea is to retry the operation a few times. If the task continues to fail and the
system believes it is not recoverable, it returns a proper error code to the client.
- Non-recoverable error. For non-recoverable errors such as malformed video format, the
system stops the running tasks associated with the video and returns the proper error code
to the client.

### Typical errors
- Upload error: retry a few times.
- Split video error: if older versions of clients cannot split videos by GOP alignment, the
entire video is passed to the server. The job of splitting videos is done on the server-side.
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
up another database server to replace the dead one.

## Step 4 - Wrap up
- Scale the API tier: Because API servers are stateless, it is easy to scale API tier
horizontally.
- Scale the database: You can talk about database replication and sharding.
- Live streaming: It refers to the process of how a video is recorded and broadcasted in real
time. Although our system is not designed specifically for live streaming, live streaming
and non-live streaming have some similarities: both require uploading, encoding, and
streaming. The notable differences are:
- Live streaming has a higher latency requirement, so it might need a different
streaming protocol.
- Live streaming has a lower requirement for parallelism because small chunks of data
are already processed in real-time.
- Live streaming requires different sets of error handling. Any error handling that
takes too much time is not acceptable.
- Video takedowns: Videos that violate copyrights, pornography, or other illegal acts shall
be removed. Some can be discovered by the system during the upload process, while
others might be discovered through user flagging.