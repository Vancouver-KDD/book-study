Design a Notification System
- Types of notification: push notification/ sms message/ email
- Real time? Soft real-time
- Device supported: iOS devices, android devices, laptop/desktop
- Trigger notification - by client applications (also scheduled on server side)
- Opt-out? Yes, user can opt-out
- 10 million mobile push notifications, 1 million sms messages, 5 million emails


1. Different Types of Notifications
- iOS push notification (provider -> Apple push notification service (APNs) -> iOS)
    * provider: provider builds and sends notifications to APNS.
		device token - unique identifier for sending push notifications 
		payload - json dictionary that contains a notification’s payload
	* APNS: Remote services provided by Apple to propagate push noti to iOS devices
	* iOS Device: end client, receives push noti
- Android push notification (provider -> Firebase Cloud Messaging (FCM) -> android devices)
- SMS message (SMS -> Twillo/Nexmo/third party SMS services -> SMS) - most are commercial services
- Email (Commercial email services -> Sendgrid/Mailchimp -> email)

2. Contact Info gathering
- API servers collect user info - mobile device token, phone numbers, email addresses. API servers (user can have multiple devices)

3. Notification sending/receiving flow
- Service -> Notification System -> Third Party Services -> devices
	* Notification System: centerpiece of sending/receiving noti. 1 -> N
	* Third-party services: delivers noti to users (pay attention to extensibility such as pluggins) Ex. FCM is not available in China
	* iOS, Android, SMS, Email: devices
- Improvements: Avoid single point of failure, improve scalability, fix performance bottleneck
	* move database and cache out of the notification server
	* Set up automatic horizontal scaling
	* message queue to decouple system components

Service 1 to N: .
Notification servers: provide APIs to send notifications, only accessibly internally for verified clients. Carry out basic validations on user info, query db or cache for data needed, put notification data to MQ for parallel processing.
	POST https://api.example.com/v/sms/send

Reliability - Notification logs and retry mechanisms from queue
notification exactly once - dedupe mechanism - check event ID
Additional:
Notification template and customizing with parameters, stling tracking links, etc
Notification (user) setting for opt-in/out + channel + user id
rate limiting
retry mechanism
security in push notification - authenticate + AppKey/appSecret
monitor queue notification
Event tracking - Integration btw notification system and the analytics services is usually required
start -> pending -> sent -> deliver -> click/unsubscribe
            -> error <-

