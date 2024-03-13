# Chapter 10: Design a Notification System

- Notification system is a very popular feature for many apps in recent years.
    - breaking news
    - product updates
    - events
    - offerings
- more than just mobile push notification. There are three types of notification formats:
    - mobile push notification
    - SMS message
    - email

## Step 1: Understand the Problem and Establish Design Scope
- Building a scalable system that sends out millions of notifications is not an easy task
    - requires deep understanding of the notification ecosystem
    - Interview question is purposely designed to be open-ended & ambiguous
    - your responsibility to clarify the requirements

- Q: What types of notifications does the system support?
- A: Push notification, SMS message, and email

- Q: Is it a real-time system?
- A: Soft real-time system. We want a user to receive notifications asap. However, if the system is under a high workload, a slight delay is acceptable.

- Q: What are the supported devices?
- A: iOS devices, android devices, and laptop/desktop

- Q: What triggers notifications?
- A: Notifications can be triggered by client applications. They can also be scheduled on the server-side

- Q: Will users be able to opt-out?
- A: Yes, users who choose to opt-out will no longer receive notifications.

- Q: How many notifications are sent out each day?
- A: 10 million mobile push notifications, 1 million SMS messages, and 5 million emails

## Step 2: Propose High-Level Design and Get Buy-In
- Hight level design that supports various notification types:
    - iOS push notification
    - Android push notification
    - SMS message
    - Email

### Different Types of Notifications
#### iOS Push Notification
- Need three components:
    - Provider: builds and sends notification requests to Apple Push Notification Service (APNS). To construct a push notification, the provider provides following data:
        - Device token: unique identifier used for sending push notis
        - Payload: JSON dictionary that contains a notification's payload.
        - APNS: remote service provided by Apple to propagate push notis to iOS devices
        - iOS Device: end client which receives push notis

#### Android Push Notification
- Similar noti flow. Instead of APNs, Firebase Cloud Messaging (FCM) is commonly used to send push notifications to android devices

#### SMS message
- Third party SMS services like Twilio, Nexmo, and many others are commonly used. Most of them are commercial services

#### Email
- Although companies can set up their own email servers, many of them opt for commercial email services. 
    - Sendgrid and Mailchimp are most popular email services (better delivery rate and data analytics)

#### Contact Info Gathering Flow
- To send notifications, the system collects mobile device tokens, phone numbers, or email addresses during user app installation or sign-up. This information is stored in the database, with email addresses and phone numbers in the user table and device tokens in the device table.

#### Notification Sending/Receiving Flow - Initial Design
- The high-level design includes services triggering notifications, a notification system, third-party services delivering notifications to users (iOS, Android, SMS, Email). However, issues like a single point of failure, scalability challenges, and performance bottlenecks are identified.

#### Notification Sending/Receiving Flow - Improved Design
- Improvements involve moving the database and cache out of the notification server, introducing automatic horizontal scaling, and implementing message queues to decouple system components. Multiple notification servers handle APIs, validations, and data fetching, and message queues facilitate parallel processing.

## Step 3 - Design Deep Dive
### Reliability
- Preventing Data Loss: Notification system persists data in a database with a retry mechanism.
- Notification Delivery: Duplicate notifications are addressed through a dedupe mechanism based on event IDs.

### Additional Components and Considerations
- Notification Template: Templates avoid building every notification from scratch, ensuring a consistent format.
- Notification Setting: Users control notification settings to manage the frequency and types of notifications they receive.
- Rate Limiting: Limits on notifications per user to avoid overwhelming users.
- Retry Mechanism: Failed notifications are retried, and alerts are sent for persistent issues.
- Security in Push Notifications: AppKey/AppSecret secure push notification APIs.
- Monitor Queued Notifications: Monitoring queued notifications ensures timely processing.
- Event Tracking: Analytics service tracks metrics like open rate, click rate, and engagement for insights.

### Updated Design
The updated design adds features like authentication, rate-limiting, retry mechanism, notification templates, and monitoring for system health checks.

## Step 4 - Wrap Up
Notifications are crucial for keeping users informed. The scalable notification system supports push notifications, SMS messages, and emails. Components like reliability mechanisms, security measures, tracking, and monitoring are crucial for a robust notification system. Respect for user settings and rate limiting ensure a positive user experience. 