# Chapter 10. Notification system 

## What is it?

This books assumes a scenario we design a internal (micro) service to provide a easy-to-use notification feature. 
Another assumption can be : SaaS 


```
                                                                                                
                                                                                    
                                                                                    
                                            ┌───────────────────┐                   
                                            │                   │                   
                                            │                   │                   
                                            │                   │      ┌────────┐   
                                            │                   ├─────►│user    │   
                                            │                   │      └────────┘   
 ┌──────────┐                               │                   │                   
 │Service 1 ├────┐  ┌────────────────┐      │                   │      ┌────────┐   
 └──────────┘    │  │                │      │                   ├─────►│user    │   
                 └─►│                │      │                   │      └────────┘   
 ┌──────────┐       │ Our service    │      │                   │                   
 │Service 2 ├──────►│ (Yet another   ├─────►│                   │      ┌────────┐   
 └──────────┘       │    service)    │      │  3-rd party       ├─────►│user    │   
                 ┌─►│                │      │  Vendors          │      └────────┘   
 ┌──────────┐    │  └────────────────┘      │  For messaging    │                   
 │Service N ├────┘                          │                   │      ┌────────┐   
 └──────────┘                               │                   ├─────►│user    │   
                                            │                   │      └────────┘   
                                            │                   │                   
                                            │                   │                   
                                            │                   │                   
                                            │                   │                   
                                            │                   │                   
                                            │                   │                   
                                            │                   │                   
                                            │                   │                   
                                            │                   │                   
                                            └───────────────────┘                   
                                                                                    
                                                                                    
                                                                                    
                                                                                         
```


## Design walkthrough
[Link](https://excalidraw.com/#json=Fe1UC5Cd_7yUwihLPUDzc,Cfb223yLB43T3As8mg4NYg)


## ERD
```
                                                               
                                                               
                                                               
                                           ┌─────────────┐     
                                           │(ios)        │     
┌─────────┐                                │user_id      │     
│ User    │                                │APNS_id      │     
│         │                                │created_at   │     
└─────────┘                                └─────────────┘     
                                                               
                                           ┌─────────────┐     
                                           │(android)    │     
                ┌────────────────┐         │user_id      │     
                │                │         │gcm_id       │     
                │ Notifiable     │         └─────────────┘     
                │                │                             
                │                │                             
                └────────────────┘         ┌─────────────┐     
                                           │(Mobile)     │     
                                           │user_id      │     
                                           │phone_number │     
                                           └─────────────┘     
                                                               
                                           ┌─────────────┐     
                                           │(Email)      │     
                                           │user_id      │     
                                           │email        │     
                                           └─────────────┘     
                                                               
                                                               
                                                               
```


## Consolidating scenario
- Let's clarify who rembmers user's contact information
- Do requesters (our users) provide user's device/email information?
- Or, do we remember all the information and requesters just let us know the 'user id'?
- Targeted messaging vs bulk messaging? 

## Version 1 : HLD with synchronous arrows
- Explaining components
  - Service user(Other microservices)
  - OUR system 
  - 3rd party vendors
  - End users
- Problem with this
  - OUR service takes 3 responsibility:
    - receive user requests
    - Identify email/phone number/device ids to send requests to
    - Send request to 3rd party vendors
      - All the fallbacks, fail overs  -> NOT easy to scale. 
         - WHY? 
## Version 2 : HLD with async : Introducing queues
- Introducing queues makes it possible to OUR service to delegate fall-back logic into another serivce (task server)
- DLQ pattern


## Deep dive & non-functional requirement
### Reliability
- Reliability has numerous aspects. In the most of interviews, addressing fail-over scenarios and durability typically suffices.
- De-duplication (User experience)

