# Blackboards

> Did Humpty Dumpty (Male, Egg) fall, or was he pushed?

* Key features of the bloackboard approach:
    * Decoupled/loosely coupled - None of the contributors needs to know of the existence of any other contributors. They watch the board for new information, and add their findings.

    * The contributors may be trained in different diciplines, may have different levels of education and expertise, and may not even work in the same precinct. They only share the desire to solve the case.

    * Different contributors may come and go during the course of the process, and may work different shifts.

    * There are no restrictions on what may be placed on the blackboard.

* Form of _laissez faire_ concurrency - contributors are independent processes, agents, actors, and so on.

* Computer-based blackboard systems:
    * AI applications - large and complex problems such as speech recognition, knowledge-based reasoning systsems, etc
    * David Gelernter's Linda - first blackboard system.
    * JavaSpaces and T Spaces - distributed blackboard-like systems.

## A Blackboard in Action

* Example: program to accept and process mortgage or load applications:
    * Responses can arrive in any order.
    * Data gathering may be done by different people.
    * Some data gathering may be done automatically by other systems.
    * Certain data may be dependent on other data.
    * The arrival of new data may raise new questions and policies.

* Trying to handling every possible combination and circumstances using a workflow system can be complex and programmer intensive. 
    * As regulations change, the workflow must be reorganized.

* A blackboard, in combination with a rules engine that encapsulates the legal requirements, is an elegant solution to the difficulties found here.

> Tip 60: Use Blackboards to Coordinate Workflow

## Messaging Systems can be Like Blackboards

* Many applications are constructed using small, decoupled services, all communicating via some form of messaging system (i.e. Kafka and NATS). 
    * Send data from A to B.
    * Offer persistence (in the form of an event log) and the ability to retrieve messages through a form of pattern matching.

## But it's Not That Simple...

* The benefit comes at a cost.
    * Approaches are harder to reason about (a lot of the action is indirect).
    * May need to keep a central repository of message formats and/or APIs, particularly if the repository can generate the code and documentation for you.
    * Need good tooling to trace messages and facts as they progress through the system.
        * Add a unique _trace id_ when a particular business function is initiated and then propagate it to all the actors involved.
    * More troublesome to deploy and manage (more moving parts).

## Challenges
* Do you use blackboard systems in the real world - the message board by the regrigerator, or the big whiteboard at work? What makes them effective? Are messages ever posted with a consistent format? Does it matter?