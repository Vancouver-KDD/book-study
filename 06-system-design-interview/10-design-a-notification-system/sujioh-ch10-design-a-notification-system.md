# **Chapter 10. Design a Notification System**

Notification systems are a popular feature in many applications - it alerts a user for important news, product updates, events, etc.

There are multiple flavors of a notification:

- Mobile push notification
- SMS
- Email

### **Step 1 - Understand the problem and establish design scope**

- C: What types of notifications does the system support?
    - I: Push notifications, SMS, Email
- C: Is it a real-time system?
    - I: Soft real-time. We want user to receive notification as soon as possible, but delays are okay if system is under high load.
- C: What are the supported devices?
    - I: iOS devices, android devices, laptop/desktop.
- C: What triggers notifications?
    - I: Notifications can be triggered by client applications or on the server-side.
- C: Will users be able to opt-out?
    - I: Yes
- C: How many notifications per day?
    - I: 10mil mobile push, 1mil SMS, 5mil email

### **Step 2 - Propose high-level design and get buy-in**

1. **Different Types of Notifications:**
    - iOS Push Notification: Utilizes a provider to construct and send notifications to Apple Push Notification Service (APNS).
    - Android Push Notification: Similar to iOS, it uses Firebase Cloud Messaging (FCM) to send notifications.
    - SMS Message: Relies on third-party SMS services like Twilio or Nexmo.
    - Email: Employs commercial email services such as Sendgrid or Mailchimp.
2. **Contact Info Gathering Flow:**
    - Upon app installation or user sign-up, API servers collect user contact info and store it in the database.
    - Email addresses and phone numbers are stored in the user table, while device tokens are stored in the device table.
3. **Notification Sending/Receiving Flow:**
    - Initial Design:
        - A single notification server is used, which serves as a central point for sending/receiving notifications.
        - Third-party services deliver notifications to users' devices.
        - Identified problems include single point of failure, scalability issues, and performance bottlenecks.
    - Improved Design:
        - Database and cache are moved out of the notification server.
        - Multiple notification servers are introduced with automatic horizontal scaling.
        - Message queues are added to decouple system components, providing buffers during high volumes of notifications.
        - Workers pull notification events from message queues and send them to corresponding third-party services.
        - Each notification type has its own message queue to prevent outages in one service from affecting others.
4. Example lifecycle of a notification**:**
    - Service makes a call to make a notification.
    - Notification servers fetch metadata (user info, …) from cache or database.
    - Notification event is sent to corresponding queue for processing for each third-party provider.
    - Workers pull events from queues and send notifications to third-party services.
    - Third-party services deliver notifications to user devices.

### **Step 3 - Design deep dive**

The deep dive into the notification system design encompasses several crucial aspects including reliability, additional components, and considerations:

1. **Reliability:**
    - **Preventing Data Loss:** Data persistence in a notification log database and implementation of a retry mechanism ensure no data loss.
    - **Handling Duplicate Notifications:** A dedupe mechanism is introduced to mitigate the occurrence of duplicate notifications by checking event IDs.
2. **Additional Components and Considerations:**
    - **Notification Template:** Templates are introduced to streamline the creation of notifications, ensuring consistency and saving time.
    - **Notification Settings:** Users are given control over their notification preferences through fine-grained settings, stored in a dedicated table.
    - **Rate Limiting:** Limits on the number of notifications a user can receive are implemented to prevent overwhelming users and potential opt-outs.
    - **Retry Mechanism:** Failed notifications are retried through a mechanism integrated into the message queue system, with alerts sent to developers if problems persist.
    - **Security in Push Notifications:** Authentication via appKey and appSecret is enforced to ensure only authenticated clients can send notifications.
    - **Monitoring Queued Notifications:** The total number of queued notifications is monitored to determine system performance and scalability needs.
    - **Event Tracking:** Metrics such as open rate and click rate are tracked to understand user engagement, requiring integration with analytics services.
3. **Updated Design:**
    - The updated design incorporates additional components and features:
        - Authentication and rate-limiting functionalities are added to notification servers.
        - A retry mechanism is implemented to handle notification failures, ensuring robustness.
        - Notification templates streamline notification creation.
        - Monitoring and tracking systems are introduced for system health checks and analytics.

Overall, the deep dive into the design emphasizes reliability, scalability, and user control, while also addressing potential issues and introducing mechanisms for handling failures and monitoring system performance.