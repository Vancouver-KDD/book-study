# Chapter 10: Design a Notification System

## Step 1 - Understand the problem and establish design scope

Candidate: What types of notifications does the system support?<br/>
Interviewer: Push notification, SMS message, and email.


Candidate: Is it a real-time system?<br/>
Interviewer: Let us say it is a soft real-time system. We want a user to receive notifications
as soon as possible. However, if the system is under a high workload, a slight delay is
acceptable.


Candidate: What are the supported devices?<br/>
Interviewer: iOS devices, android devices, and laptop/desktop.


Candidate: What triggers notifications?<br/>
Interviewer: Notifications can be triggered by client applications. They can also be
scheduled on the server-side.


Candidate: Will users be able to opt-out?<br/>
Interviewer: Yes, users who choose to opt-out will no longer receive notifications.


Candidate: How many notifications are sent out each day?<br/>
Interviewer: 10 million mobile push notifications, 1 million SMS messages, and 5 million
emails.

## Step 2 - Propose high-level design and get buy-in

### Different types of notifications
#### iOS push notification
- Provider. A provider builds and sends notification requests to Apple Push Notification
Service (APNS). To construct a push notification, the provider provides the following
data:
  - Device token: This is a unique identifier used for sending push notifications.
  - Payload: This is a JSON dictionary that contains a notification’s payload.
- APNS: This is a remote service provided by Apple to propagate push notifications to iOS
devices.
- iOS Device: It is the end client, which receives push notifications.

#### Android push notification
Instead of using APNs, Firebase Cloud Messaging
(FCM) is commonly used to send push notifications to android devices.
- SMS message
  - For SMS messages, third party SMS services like Twilio, Nexmo, and many others are
commonly used. Most of them are commercial services.

- Email
  - Sendgrid and Mailchimp are among the most popular email services,
which offer a better delivery rate and data analytics.

#### Contact info gathering flow
To send notifications, we need to gather mobile device tokens, phone numbers, or email
addresses. Email addresses and phone
numbers are stored in the user table, whereas device tokens are stored in the device table. A
user can have multiple devices, indicating that a push notification can be sent to all the user
devices.

#### Notification sending/receiving flow
- Service 1 to N: They represent different services that send notifications via APIs provided by
notification servers.
- Notification servers: They provide the following functionalities:
  - Provide APIs for services to send notifications. Those APIs are only accessible internally
or by verified clients to prevent spams.
  - Carry out basic validations to verify emails, phone numbers, etc.
  - Query the database or cache to fetch data needed to render a notification.
  - Put notification data to message queues for parallel processing.
  Cache: User info, device info, notification templates are cached.
- DB: It stores data about user, notification, settings, etc.
- Message queues: They remove dependencies between components. Message queues serve as
buffers when high volumes of notifications are to be sent out. Each notification type is
assigned with a distinct message queue so an outage in one third-party service will not affect
other notification types.
- Workers: Workers are a list of servers that pull notification events from message queues and
send them to the corresponding third-party services.
- Third-party services: Already explained in the initial design.
- iOS, Android, SMS, Email: Already explained in the initial design.

## Step 3 - Design deep dive
### Reliability
- How to prevent data loss?
  - One of the most important requirements in a notification system is that it cannot lose data.
Notifications can usually be delayed or re-ordered, but never lost. To satisfy this requirement,
the notification system persists notification data in a database and implements a retry
mechanism.
  - When a notification event first arrives, we check if it is seen before by checking the event ID.
If it is seen before, it is discarded. Otherwise, we will send out the notification.

### Additional components and considerations
- Notification template
  - A large notification system sends out millions of notifications per day, and many of these
notifications follow a similar format. Notification templates are introduced to avoid building
every notification from scratch. A notification template is a preformatted notification to
create your unique notification by customizing parameters, styling, tracking links, etc.
- Notification setting
  - Users generally receive way too many notifications daily and they can easily feel
overwhelmed. Thus, many websites and apps give users fine-grained control over notification
settings.
- Rate limiting
  - To avoid overwhelming users with too many notifications, we can limit the number of
notifications a user can receive.
- Retry mechanism
  - When a third-party service fails to send a notification, the notification will be added to the
message queue for retrying.
- Security in push notifications
  - For iOS or Android apps, appKey and appSecret are used to secure push notification APIs. Only authenticated or verified clients are allowed to send push notifications using our APIs. Interested users should refer to the reference material.
- Monitor queued notifications
  - A key metric to monitor is the total number of queued notifications. If the number is large,
the notification events are not processed fast enough by workers. To avoid delay in the
notification delivery, more workers are needed.
- Events tracking
  - Notification metrics, such as open rate, click rate, and engagement are important in
understanding customer behaviors. Analytics service implements events tracking.

### Updated design
- The notification servers are equipped with two more critical features: authentication and
rate-limiting.
- We also add a retry mechanism to handle notification failures. If the system fails to send
notifications, they are put back in the messaging queue and the workers will retry for a
predefined number of times.
- Furthermore, notification templates provide a consistent and efficient notification
creation process.
- Finally, monitoring and tracking systems are added for system health checks and future
improvements.

## Step 4 - Wrap up
Notifications are indispensable because they keep us posted with important information. It
could be a push notification about your favorite movie on Netflix, an email about discounts
on new products, or a message about your online shopping payment confirmation.
### More components and optimizations
- Reliability: We proposed a robust retry mechanism to minimize the failure rate.
- Security: AppKey/appSecret pair is used to ensure only verified clients can send
notifications.
- Tracking and monitoring: These are implemented in any stage of a notification flow to
capture important stats.
- Respect user settings: Users may opt-out of receiving notifications. Our system checks
user settings first before sending notifications.
- Rate limiting: Users will appreciate a frequency capping on the number of notifications
they receive.