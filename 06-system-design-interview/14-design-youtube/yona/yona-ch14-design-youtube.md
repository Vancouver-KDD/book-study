# Chapter 14: Design Youtube
- You are asked to design YouTube.
    - Can be applied to other questions like designing a video sharing platform (i.e. Netflix, Hulu)
- Looks simple - content creators upload videos and viewers click play. However, there are lots of complex technologies underneath the simplicity.

- Fun facts of YouTube 2023/2024:
    - Total number of monthly active users: Over 2.5 billion.
    - Number of videos watched per day: Over 6 billion.
    - Approximately 75% of US adults use YouTube.
    - There are now over 60 million creators on YouTube.
    - YouTube's Ad revenue reached $20.5 billion for the full year 2023, showing continued growth.
    - YouTube is responsible for approximately 40% of all mobile internet traffic.
    - YouTube is available in over 100 different languages.

## Step 1: Understand the Problem and Establish Design Scope
- Some features: 
    - watch a video
    - comment
    - share
    - like
    - save a video to playlists
    - subscribe to a channel
- Impossible to design everything within 45-60 min interview. Thus very important to ask questions to narrow down the scope.

- Q: What are important features?
- A: Ability to upload a video and watch a video

- Q: What clients do we need to support?
- A: Mobile apps, web browsers, and smart TV

- Q: How many daily active users do we have?
- A: 5 million

- Q: What is the average daily time spent on the product?
- A: 30 minutes

- Q: Do we need to support international users?
- A: Yes, a large percentage of users are international users

- Q: What are the supported video resolutions?
- A: The system accepts most of the video resolutions and formats

- Q: Is encryption required?
- A: Yes

- Q: Any file size requirement for videos?
- A: Our platform focuses on small and medium-sized videos. Max allowed video size is 1GB.

- Q: Can we leverage some of the existing cloud infrastructures provided by Amazon, Google, or Microsoft?
- A: Building everything from scratch is unrealistic for most companies, it is recommended to leverage some of the existing cloud services.

- In summary:
    - Ability to upload videos fast
    - Smooth video streaming
    - Ability to change video quality
    - Low infrastructure cost
    - High availability, scalability, and reliability requirements
    - Clients supported: mobile apps, web browser, and smart TV

## Back of the Envelope Estimation
- Based on many assumptions, so very important to communicate with the interviewer to make sure they are on the same page.
    - Assume the product has 5 million daily active users (DAU). • Users watch 5 videos per day.
    - 10% of users upload 1 video per day.
    - Assume the average video size is 300 MB.
    - Total daily storage space needed: 5 million * 10% * 300 MB = 150TB
    - CDN cost.
        - When cloud CDN serves a video, you are charged for data transferred out of the CDN.
        - Let us use Amazon’s CDN CloudFront for cost estimation (Figure 14-2) [3]. Assume 100% of traffic is served from the United States. The average cost per GB is $0.02. For simplicity, we only calculate the cost of video streaming.
        - 5 million * 5 videos * 0.3GB * $0.02 = $150,000 per day.
    
- From this rough cost estimation, we know serving videos from the CDN is expensive.

- On-demand pricing for Data Transfer to the Internet (/GB):

<img title="On-demand pricing for Data Transfer to the Internet (/GB)" src="./resources/on-demand-pricing.png">
