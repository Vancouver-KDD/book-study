# Design A Notification System

There are 3 types of notification formats:

- Mobile push notification
- SMS Message
- Email

## Step 1. Understand the problem and establish design scope

The requirements:

- The system support: Push notification, SMS message, and email
- Soft real-time system : A slight delay is acceptable
- The supported devices: iOS devices, android devices, and laptop/ desktop
- Triggered by client applications.
- Able to opt-out? Yes
- 10 million mobile push notifications, 1 million SMS messages, and 5 million emails

## Step 2. Propose high-level design and get buy-in

### 2.1. Different types of notification

#### 2.1.1. iOS push notification

- 3 components to send an iOS push notification
  - Provider : It builds and sends notification requests to Apple Push Notification Service(APNS).  
    - Device token: A unique identifier used for sending push notifications
    - Payload: A JSON dictionary that contains a notification's payload.
    - APNS: A remote service provided by Apple.
    - iOS Device: It is the end client, which receives push notifications.

#### 2.1.2. Android push notification

- Firebase Cloud Messaging (FCM) is commonly used to send push notifications to android devies.

#### 2.1.3. SMS message

- Third party SMS services like Twilio, Nexmo, and many others are commonly used. Most of them are commercial services.

#### 2.1.4. Email

- Although companies can set up their own email servers, many of them opt for commercial email services.

### 2.2. Contact info gathering flow

To send notifications, we need to gather mobile device tokens, phone numbers, or email addresses.

- Simplified database tables:
  - Email addresses and phone numbers are stored in the user table, whereas device tokens are stored in the device table
  - A user can have multiple devices.

### 2.3. Notification sending/receiving flow

#### 2.3.1. High-level design

##### 2.3.1.1. Service 1 to N

- A service can be a micro-service, a cron job, or a distributed system that triggers notification sending events.
- Ex, A billing service sends emails to remind customers of their due payment.

##### 2.3.1.2. Notification system

- The notification system is the centerpiece of sending/receiving notifications.
- Starting with something simple, only one notification server is used.

##### 2.3.1.3. Third-party services

- It is responsible for delivering notifications to users.
- Need to pay extra attention
  - Good extensibility : A flexible system that can easily plugging or unplugging of a third-party service.
  - A third-party service might be unavailable in new markets or in the future

##### 2.3.1.4. iOS, Android, SMS, Email

Three problems

- Single point of failure (SPOF): A single notification server means SPOF
- Hard to scale:
  - One notification server -> challenging to scale database, caches, and different notification processing components independently
- Performance bottleneck: 
  - Handling everything in one system can result in the system overload.

#### 2.3.2. High-level design (improved)

- Move the database and cache out of the notification server
- Add more notification servers and set up automatic horizontal scaling
- Introduce message queues to decouple the system components.

##### 2.3.2.1. Service 1 to N

They represent different services that send notifications via APIs provided by notification servers.

##### 2.3.2.2. Notification servers

- Provide APIs for services to send notifications.
- Carry out basic validations to verify emails, phone numbers, etc.
- Query the database or cache to fetch data needed to render a notification
- Put notification data to message queues for parallel processing

##### 2.3.2.3. Cache

- User info, device info, notification templates are cached

##### 2.3.2.4. DB

- It stores data about user, notification, settings, etc.

##### 2.3.2.5. Message queues

- They remove dependencies between components.
- Each notification type is assigned with a distinct message queue.

##### 2.3.2.6. Workers

- Workers are a list of servers that pull notification events from message queues and send them to the corresponding third-party services.

##### 2.3.2.7. Third-party services

- Already explained in the initial design

##### 2.3.2.8. iOS, Android, SMS, Email

- Already explained in the initial design

## Step 3. Design deep dive

### 3.1. Reliability

- How to prevent data loss?
  - The notification system persists notification data in a database and implements a retry mechanism.
  - The notification log database is included for data persistence.
- Will recipient receive a notification exactly once?
  - The short answer is no.
  - To reduce the duplication occurrence,
    - We introduce a dedupe mechanism and handle each failure case carefully.

### 3.2. Additional components and considerations

- Template reusing, notification settings, event tracking, system monitoring, rate limiting

#### 3.2.1. Notification template

- Notification templates are introduced to avoid building every notification from scratch.
- A notification template is a preformatted notification to create your unique notification by customizing parameters, styling, tracking links, etc.

#### 3.2.2. Notification setting

- Many websites and apps give users fine-grained control over notification settings.
- This information is stored in the notification setting table, with the following fields:
  - user_id
  - channel
  - opt_in

#### 3.2.3. Rate limiting

- To avoid overwhelming users with too many notifications, we can limit the number of notification a user can receive.

#### 3.2.4. Retry mechanism

- When a third-party service fails to send a notification, the notification will be added to the message queue for retrying. If the problem persists, an alert will be sent out to developers.

#### 3.2.5. Security in push notifications

- For iOS or Android apps, appKey and appSecret are used to secure push notification APIs.
- Only authenticated or verified clients are allowed to send push notifications using our APIs.

#### 3.2.6. Monitor queued notifications

- A key metric to monitor is the total number of queued notifications.
- If the number is large, the notification events are not processed fast enough by workers.

#### 3.2.7. Events tracking

- Notification metrics, such as open rate, click rate, and engagement are important in understanding customer behaviors.

### 3.3. Updated design

- The notification servers are equipped with two more critical features: authentication and rate-limiting
- Add a retry mechanism
- Notification templates provide a consistent and efficient notification creation process.
- Monitoring and tracking systems are added.

## Step 4. Wrap up

- We described the design of a scalable notification system.
- We dug deep into more components and optimizations.
  - Reliability
  - Security
  - Tracking and monitoring
  - Respect user settings
  - Rate limiting
