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
