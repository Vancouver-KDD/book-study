Design a Notification System
- Types of notification: push notification/ sms message/ email
- Real time? Soft real-time
- Device supported: iOS devices, android devices, laptop/desktop
- Trigger notification - by client applications (also scheduled on server side)
- Opt-out? Yes, user can opt-out
- 10 million mobile push notifications, 1 million sms messages, 5 million emails


1. Different Types of Notifications
* iOS push = (provider -> Apple push notification service (APNs) -> iOS)
    * provider: provider builds and sends notifications to APNS.
		device token - unique identifier for sending push notifications 
		payload - json dictionary that contains a notification’s payload
 * apns: REMOVE SERVICE PROVIDED BY APPLE TO PROPAGATE PUSH NOTI TO I
