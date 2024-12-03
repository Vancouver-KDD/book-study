**Three types of notification formats**
- mobile push notification, SMS message, and Email

# Step 1 - things to ask
- types of notifications
- real-time system? as soon as possible or delay
- supported devices? mobile type, laptop/desktop
- what is the trigger? by client app, server-side schedule
- users can unsubscribe?
- how many notifications are sent daily? 10 million mobile, 1 mil sms, 5 mil emails

# Step 2 - high level design and get buy-in

## Different types of notifications

### iOS push notification
- Provider: builds and sends notification requests to Apple Push Notification Service - with device token, payload (the content)
- APNS: remote service by Appple, propagating push notification to devices

### Android push notification
- FCM - Firebase Cloud Messaging: Same functionality as APNS

### SMS messages 
- Third party SMS services like Twilio etc
### Email
- Own email servers or commercial email services like Sendgrid, MailChimp

## Contact info gathering flow
- When a user installs the app or sign ups for the first time, the mobile device token, phone number, or email is stored in the DB

## Notification sending/receiving flow
### High level design
#### Third party services responsible for delivering notifications, 
  - good extensibility is important: easily plugging or unplugging of a third-party service
  - Considering a third party service might be unavailable in new markets or in the future, so always alternatives in mind

### Potential problems 
- Single point of failure from a single notification server
- Hard to scale - every notification handled in one server
- Performance bottleneck
  - Processing and sending notifications can be resource intensive - Handling everything in one system can result in the system overload, especially during peak hours

### Improved high level design
- Move the database and cache out of the notification server.
- Add more notification servers and set up automatic horizontal scaling.
- Introduce message queues to decouple the system components.

<img width="680" alt="Screenshot 2024-12-02 at 7 31 30 PM" src="https://github.com/user-attachments/assets/9d367c68-da25-4180-bfd0-126515429260">

#### Notification servers
- Provide internal APIs for services to send notifications. 
- basic validations to verify emails, phone numbers, etc.
- Query the database or cache to fetch data needed to render a notification.
- Put notification data to message queues for parallel processing.

#### Message queues
- removes dependencies between components.
- serve as buffers when high volumes of notifications are to be sent out.
- Each notification type is assigned with a distinct message queue so an outage in one third-party service will not affect other notification types.

#### Workflow
1. A service calls APIs provided by notification servers to send notifications.
2. Notification servers fetch metadata such as user info, device token, and notification setting from the cache or database.
3. A notification event is sent to the corresponding queue for processing. For instance, an iOS push notification event is sent to the iOS PN queue.
4. Workers pull notification events from message queues.
5. Workers send notifications to third party services.
6. Third-party services send notifications to user devices.

# Design deep dive
## Reliability
### How to prevent data loss?
- Notifications can usually be delayed or re-ordered, but never lost.
- Retry mechanism: persisting notification log

### Will recipients receive a notification exactly once?
- Notifications can be duplicated due to the distributed nature
- To reduce it, dedupe mechanism: check the event ID to check if it was delivered before

## Additional components and considerations
### Notification template
- preformatted notification to create your unique notification by customizing parameters, styling, tracking links, etc.
- a consistent format, reducing the margin error, and saving time.

### Notification setting
- many websites and apps give users fine-grained control over notification settings and store in the DB notification setting table

### Rate limiting
- limit the number of notifications a user can receive - receivers could turn off notifications completely if we send too often.

### Retry mechanism
- failed notification is added to the message queue for retrying
- If the issue persists, an alert will be sent out

### Security in push notification
- Only authenticated or verified clients are allowed to send push notifications using APIs.

### Monitor queued notifications
- Total number of queued notifications; depending on that add or reduce # of workers

### Event tracking
- Notification metrics, such as open rate, click rate, and engagement are important in understanding customer behaviors.
