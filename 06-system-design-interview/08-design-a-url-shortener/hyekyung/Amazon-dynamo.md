# Dynamo: Amazon's Highly Available Key-value Store

It is a **highly available key-value storage system** that some of Amazon's core services used to provide an "always-on" experience. To achieve this level of availability, Dynamo sacrifices consistency under certain failure scenarios. It makes extensive use of **object versioning** and **application-assisted conflict resolution** in a manner that provides a novel interface for developer to use.

- Design choice ( Conflict resolution:  When? Read / Who? Application)
  - **Eventual consistency & highly availability**
  - **When to perform** the process of resolving update conflicts?
    - Many traditional data stores execute conflict resolution during writes and keep the read complexity simple. (In such system, writes may be rejected if the data store cannot reach all or a majority of the replicas at a given time)
    - However, Dynamo targets-> "always writeable" data store.
      - Push the **complexity of conflict resolution to the reads** to ensure that writes are never rejected
    - get() operation
      - return a single object or a list of objects with conflicting versions along with a context.

- Examples of many services on Amazon's platform
  - best seller lists, shopping carts, customer preferences, session management, sales rank, and product catalog, ...
  - Dynamo provides a simple primary-key only interface to meet the requirement of these applications.

## Dynamo vs DynamoDB

- Dynamo : Key-value Store used by Amazon's internal services (2007년도에 논문을 통해 공개됨)
- DynamoDB: NoSql database, Dynamo의 아이디어를 바탕으로 만든 서비스형 Database
  - Amazon 이 원래 사용하던 SimpleDB 라는 database 에 Dynamo 의 design 일부를 접목시켜 만듬.
    - "Dynamo might have been the best technology in the world at the time but it was still software you had to run yourself. And nobody wanted to learn how to do that if they didn't have to. Ultimately, developers wanted a service."
  - Read consistency options
    - Eventually consistent (Default)
      - The responses may not reflect the results of a recently completed write operation. (Old version data 를 read할수 있음.)
    - Strong Consistent read ( with 'consistent-read' option)
      - Returns a response with the most up-to-date data.
      - Strong consistent read 는 네트워크 지연 또는 중단이 발생한 경우에 사용이 어려울 수 있다. 이경우 DynamoDB 는 서버 오류(HTTP 500)을 반환할 수도 있다. 
