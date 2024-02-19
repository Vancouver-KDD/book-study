# Chapter 1 : Scale from zero to millions of users

### Ground rule 
1. Client(Web browser/mobile app) **doesn't** want to know what's going on on the server side. Server component is a blackbox to clients. 

```plaintext
                                        ┌────────────────────┐
                                        │                    │
    ┌─────────────────┐   request       │    Web server      │
    │                 ├────────────────►│    Load balancer   │
    │    Client       │                 │    CDN             │
    │                 │◄────────────────┤    Database        │
    └─────────────────┘     response    │    Microservices   │
                                        │..Many more things..│
                                        │                    │
                                        └────────────────────┘
```


2. Why we decouple components? If two or more different software pieces are running on the same server,  it's not easy to pick the busy piece only and scale- we have to scale the whole server instead.  (And many other reasons to come)


```
Large server : 100$/month
Medium server : 50$/month


     ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
     │  Large server   │  │  Large server   │  │  Large server   │
     │  ┌───────────┐  │  │  ┌───────────┐  │  │  ┌───────────┐  │
     │  │           │  │  │  │           │  │  │  │           │  │
     │  │ service A │  │  │  │ service A │  │  │  │ service A │  │
     │  │           │  │  │  │           │  │  │  │           │  │
     │  └───────────┘  │  │  └───────────┘  │  │  └───────────┘  │
     │                 │  │                 │  │                 │
     │  ┌───────────┐  │  │  ┌───────────┐  │  │  ┌───────────┐  │
     │  │           │  │  │  │           │  │  │  │           │  │
     │  │ service B │  │  │  │ service B │  │  │  │ service B │  │
     │  │           │  │  │  │           │  │  │  │           │  │
     │  └───────────┘  │  │  └───────────┘  │  │  └───────────┘  │
     │                 │  │                 │  │                 │
     └─────────────────┘  └─────────────────┘  └─────────────────┘
                                              total cost : $300
   ────────────────────────────────────────────────────────────────────

     ┌─────────────────┐
     │  Medium server  │
     │  ┌───────────┐  │
     │  │           │  │
     │  │ service A │  │
     │  │           │  │
     │  └───────────┘  │
     │                 │
     └─────────────────┘

     ┌─────────────────┐   ┌─────────────────┐  ┌─────────────────┐
     │  Medium server  │   │  Medium server  │  │  Medium server  │
     │  ┌───────────┐  │   │  ┌───────────┐  │  │  ┌───────────┐  │
     │  │           │  │   │  │           │  │  │  │           │  │
     │  │ service B │  │   │  │ service B │  │  │  │ service B │  │
     │  │           │  │   │  │           │  │  │  │           │  │
     │  └───────────┘  │   │  └───────────┘  │  │  └───────────┘  │
     │                 │   │                 │  │                 │
     └─────────────────┘   └─────────────────┘  └─────────────────┘
                                              total cost : 
                                                    50 * 1 (A)
                                                    500 * 3 (B)
                                                    = $200
```

### How it goes

1. To begin with, We can host all softwares to handle client requests on a single server.
```
                        ┌─────────────────┐
                        │  Server         │
          ─────────────►│  ┌───────────┐  │
                        │  │           │  │
                        │  │ Web app   │  │
                        │  │           │  │
                        │  └───┬──▲────┘  │
                        │      │  │       │
                        │  ┌───▼──┴────┐  │
                        │  │           │  │
          ◄─────────────┤  │ Database  │  │
                        │  │           │  │
                        │  └───────────┘  │
                        │                 │
                        └─────────────────┘
``` 

2. Userbase grows so fast - we need more than 1 web server. To add multiple servers- Let's separate DB . 
```
                        ┌─────────────────┐
                        │  Server         │
          ─────────────►│  ┌───────────┐  │
                        │  │           │  │
                        │  │ Web app   │  │
                        │  │           │  │
                        │  └───┬──▲────┘  │
                        │      │  │       │
                        │  ┌───▼──┴────┐  │
                        │  │           │  │
          ◄─────────────┤  │ Database  │  │
                        │  │           │  │
                        │  └───────────┘  │
                        │                 │
                        └─────────────────┘

                      ─────────────────────────────────────────────────
                        Web & DB separated.
                        ┌─────────────────┐     ┌─────────────────┐
                        │  Webapp server  │     │  DB server      │
           ────────────►│  ┌───────────┐  ├────►│  ┌───────────┐  │
                        │  │           │  │     │  │           │  │
                        │  │ Web app   │  │     │  │ Database  │  │
                        │  │           │  │     │  │           │  │
           ◄────────────┤  └───────────┘  │◄────┤  └───────────┘  │
                        │                 │     │                 │
                        └─────────────────┘     └─────────────────┘
```


3. Let's add another server to lessen the traffic burden. But how will the client decide which server to communicate with **(Ground rule #1)**? To solve this, we'll set up a load balancer(LB) at the front.
```
                            ─────────────────────────────────────────────────
                              Web & DB separated.
                              ┌─────────────────┐     ┌─────────────────┐
                              │  Webapp server  │     │  DB server      │
                 ────────────►│  ┌───────────┐  ├────►│  ┌───────────┐  │
                              │  │           │  │     │  │           │  │
                              │  │ Web app   │  │     │  │ Database  │  │
                              │  │           │  │     │  │           │  │
                 ◄────────────┤  └───────────┘  │◄────┤  └───────────┘  │
                              │                 │     │                 │
                              └─────────────────┘     └─────────────────┘

                            ─────────────────────────────────────────────────
                              Adding another server. 
                              Client got confused - server 1 or server 2? 
                              ┌─────────────────┐
                              │  Webapp server 1│
                              │  ┌───────────┐  │
                              │  │           │  │
                     ┌───────►│  │ Web app   │  │
                     │        │  │           │  ├─────►┌─────────────────┐
                     │        │  └───────────┘  │      │  DB server      │
                     │        │                 │◄─────┤  ┌───────────┐  │
                     │        └─────────────────┘      │  │           │  │
          ───────────┤ ???                             │  │ Database  │  │
                     │        ┌─────────────────┐      │  │           │  │
                     │        │  Webapp server 2├─────►│  └───────────┘  │
                     │        │  ┌───────────┐  │      │                 │
                     │        │  │           │  │◄─────┴─────────────────┘
                     └───────►│  │ Web app   │  │
                              │  │           │  │
                              │  └───────────┘  │
                              │                 │
                              └─────────────────┘

                            ─────────────────────────────────────────────────
                              Adding a LB to resolve this (Client is now happy) 

                       ┌────┐   ┌─────────────────┐
                       │    │   │  Webapp server 1│
                       │    │   │  ┌───────────┐  │
                       │    │   │  │           │  │
                       │    ├──►│  │ Web app   │  │
                       │    │   │  │           │  ├─────►┌─────────────────┐
                       │    │◄──┤  └───────────┘  │      │  DB server      │
          ────────────►│    │   │                 │◄─────┤  ┌───────────┐  │
                       │    │   └─────────────────┘      │  │           │  │
                       │ LB │                            │  │ Database  │  │
                       │    │   ┌─────────────────┐      │  │           │  │
          ◄────────────┤    │   │  Webapp server 2├─────►│  └───────────┘  │
                       │    ├──►│  ┌───────────┐  │      │                 │
                       │    │   │  │           │  │◄─────┴─────────────────┘
                       │    │   │  │ Web app   │  │
                       │    │◄──┤  │           │  │
                       │    │   │  └───────────┘  │
                       │    │   │                 │
                       └────┘   └─────────────────┘
```
4. We added many servers to handle skyrocketing user numbers. Now our DB is swamped - about to explode. We now introduce DB replica - These replicas will duplicate all the existing records in the source(primary) database to help distribute the load. 

- ~~Master~~ -> Source (or primary/leader)
- ~~Slave~~ -> Replica (or secondary/follower) 
- [MySQL Terminology Updates](https://dev.mysql.com/blog-archive/mysql-terminology-updates/)

```
                We are now too successful - DB is about to explore. 
                 ┌────┐
                 │    │  ┌─────────────────┬───────────────────┐
                 │    │  │  web server 1   │                   │
                 │    ├─►│ ┌───────────────┴─┬───────────────┐ │
                 │    │  │ │  web server 2   │               │ │Too busy!
                 │    │◄─┤ │ ┌───────────────┴─┬─────────┐ ┌─▼─▼─────────────┐
                 │    │  │ │ │  web server 3   │         └─►  DB server      │
    ────────────►│    │  │ │ │ ┌───────────────┴─┬─────┐   │  ┌───────────┐  │
                 │    │  │ │ │ │  web server ... │     └───►  │           │  │
                 │ LB │  └─┤ │ │ ┌───────────────┴─┐       │  │ Database  │  │
                 │    ├──► │ │ │ │  web server 20  ├───────►  │           │  │
    ◄────────────┤    │    └─┤ │ │                 │       │  └───────────┘  │
                 │    ├────► │ │ │                 │       │                 │
                 │    │      └─┤ │                 │       └─────────────────┘
                 │    ├──────► │ │                 │
                 │    │        └─┤                 │
                 │    ├────────► │                 │
                 │    │          └─────────────────┘
                 └────┘

                   ─────────────────────────────────────────────────
                    Added Replicas to alleviate read traffic. 
                 ┌────┐
                 │    │                                  ┌───────────────┐
                 │    │                                  │               │
                 │    │                             ┌────► DB Source     ├─────┬────┐
                 │    │                             │    │               │     │    │
                 │    │     ┌───────────────────┐ writes │               │     │    │
                 │    │     │                   │   │    └───────────────┘     │    │
    ────────────►│    ├────►│                   ├───┘                          │    │
                 │    │     │  Web server       │        ┌───────────────┐     │    │
                 │ LB │     │  Cluster          │        │               │     │    │
                 │    │     │                   ├──read──► DB Replica 1  ◄─────┘    │replication
    ◄────────────┤    │◄────┤                   │        │               │          │
                 │    │     │                   ├───┐    │               │          │
                 │    │     └───────────────────┘   │    └───────────────┘          │
                 │    │                             │                               │
                 │    │                           read   ┌───────────────┐          │
                 │    │                             │    │               │          │
                 │    │                             └────► DB Replica 2  │          │
                 └────┘                                  │               ◄──────────┘
                                                         │               │
                                                         └───────────────┘


```


5. Reading from DB is somewhat slow (because it uses slow storage medium- the hard drive). Let's add cache layer - a fast intermediary data retrieval service, using RAM. It's expensive, we can store a small part of database content. So let's put only very popular content. 

```

                  ┌────┐
                  │    │                                  ┌───────────────┐
                  │    │                                  │               │
                  │    │                             ┌────► DB Source     ├─────┬────┐
                  │    │                             │    │               │     │    │
                  │    │     ┌───────────────────┐ writes │               │     │    │
                  │    │     │                   │   │    └───────────────┘     │    │
     ────────────►│    ├────►│                   ├───┘                          │    │
                  │    │     │  Web server       │        ┌───────────────┐     │    │
                  │ LB │     │  Cluster          │        │               │     │    │
                  │    │     │                   ├──read──► DB Replica 1  ◄─────┘    │replication
     ◄────────────┤    │◄────┤                   │        │               │          │
                  │    │     │                   ├───┐    │               │          │
                  │    │     └───────────────────┘   │    └───────────────┘          │
                  │    │                             │                               │
                  │    │                           read   ┌───────────────┐          │
                  │    │                             │    │               │          │
                  │    │                             └────► DB Replica 2  │          │
                  └────┘                                  │               ◄──────────┘
                                                          │               │
                                                          └───────────────┘

                   ─────────────────────────────────────────────────


                 ┌────┐
                 │    │                                  ┌───────────────┐
                 │    │                                  │               │
                 │    │                             ┌────► DB Source     │
                 │    │                             │    │               │
                 │    │     ┌───────────────────┐ writes │               │
                 │    │     │                   │   │    └───────────────┘
    ────────────►│    ├────►│                   ├───┘
                 │    │     │  Web server       │        speed up!
                 │ LB │     │  Cluster          │      ┌──────────┐       ┌───────────────┐
                 │    │     │                   ├─────►│          │       │               │
    ◄────────────┤    │◄────┤                   │      │ Cache    ├──┬───►│ DB Replica 1  │
                 │    │     │                   │◄─────┤          │  │    │               │
                 │    │     └───────────────────┘      └──────────┘  │    │               │
                 │    │                                              │    └───────────────┘
                 │    │                                              │
                 │    │                                              │    ┌───────────────┐
                 │    │                                              │    │               │
                 └────┘                                              └───►│ DB Replica 2  │
                                                                          │               │
                                                                          │               │
                                                                          └───────────────┘


```

6. We're all good- but users in Korea now complaining it takes too long to load videos. It's not surprising-  Our servers are based in Canada. We'll introduce CDN - Distribute large-sized files across servers situated in multiple location around the globe. 

```

                                           ┌────┐   ┌─────────────────┐
                                           │    │   │ Web server      │
          User in ───────1.request─────────►    ├───► Database        │
          Canada▲                          │ LB │   │ Replica         │
            │   └──2.here's webpage.───────┤    ◄───┤ Cache           │
            │        get video from this   │    │   │ etc..           │
            3        url.                  └▲──┬┘   └─────────────────┘
            │                               │  │
            │  ┌───────────┐                │  │
            └─►│CDN-Alberta│                │  │
               └───────────┘                │  │
                                            │  │
                                            │  │
                                            │  │
                User in────────1────────────┘  │
                Korea▲                         │
                 │   └─────────2───────────────┘
                 │
                 │    ┌───────────┐
                 └─3─►│CDN-Japan  │
                      └───────────┘



```

7. There are some features that the nature requires asynchronous processing. Usually tasks requires long latency or human intervention is a good fit for this pattern. When implemented properly, it's effective and makes customer happy. (But it's sort of tricky to implement it well)
```

  ┌─────────────────────┐
  │                     │
  │ Not that easy       │
  │ to explain          │
  │ with basic boxes    │
  │ (Or annoying)       │
  │                     │
  └─────────────────────┘

``` 
