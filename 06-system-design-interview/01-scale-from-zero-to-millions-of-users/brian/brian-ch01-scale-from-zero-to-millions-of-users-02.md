# CHAPTER 1: SCALE FROM ZERO TO MILLIONS OF USERS
## 7 Stateless web tier
- Stateful Architecture

![statefulArchitecture.png](pictures%2FstatefulArchitecture.png)
- Stateless Architecture

![statelessArchitecture.png](pictures%2FstatelessArchitecture.png)

![statelessWebTier.png](pictures%2FstatelessWebTier.png)

## 8 Data centers
![dataCenters.png](pictures%2FdataCenters.png)
### Failover
![dataCenterFailover.png](pictures%2FdataCenterFailover.png)
### Challenges
1. Traffic redirection
2. DAta synchronization
3. Test and deployment

## 9 Message queue
![MQ.png](pictures%2FMQ.png)

![PubSub.png](pictures%2FPubSub.png)

Types: https://rabbitmq.com/getstarted.html

## 10 Logging, metrics, automation
![loggingMetricsMonitoringAutomation.png](pictures%2FloggingMetricsMonitoringAutomation.png)

## 11 Database scaling
![VerticalVSHorizontalScaling.png](pictures%2FVerticalVSHorizontalScaling.png)

![Sharding.png](pictures%2FSharding.png)

![ShardingData.png](pictures%2FShardingData.png)

### Challenges
1. Resharding data
2. Celebrity problem
3. Join and de-normalization

![AddingSharding.png](pictures%2FAddingSharding.png)

## 12 Millions of users and beyond
1. Keep web tier stateless
2. Build redundancy at every tier
3. Cache data as much as you can
4. Support multiple data centers
5. Host static assets in CDN
6. Scale your data tier by sharding
7. Split tiers into individual services
8. Monitor your system and use automation tools