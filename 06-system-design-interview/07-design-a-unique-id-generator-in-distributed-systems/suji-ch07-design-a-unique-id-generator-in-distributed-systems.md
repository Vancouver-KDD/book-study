# Ch 7. DESIGN A UNIQUE ID GENERATOR IN
DISTRIBUTED SYSTEMS

### Step 1 - Understand the problem and establish design scope

Asking clarification questions is the first step to tackle any system design interview question.

Here is an example of candidate-interviewer interaction:
Candidate: What are the characteristics of unique IDs?
Interviewer: IDs must be unique and sortable.
Candidate: For each new record, does ID increment by 1?
Interviewer: The ID increments by time but not necessarily only increments by 1. IDs
created in the evening are larger than those created in the morning on the same day.
Candidate: Do IDs only contain numerical values?
Interviewer: Yes, that is correct.
Candidate: What is the ID length requirement?
Interviewer: IDs should fit into 64-bit.
Candidate: What is the scale of the system?
Interviewer: The system should be able to generate 10,000 IDs per second.

Above are some of the sample questions that you can ask your interviewer. It is important to
understand the requirements and clarify ambiguities. For this interview question, the
requirements are listed as follows:
• IDs must be unique.
• IDs are numerical values only.
• IDs fit into 64-bit.
• IDs are ordered by date.
• Ability to generate over 10,000 unique IDs per second.

### Step 2 - Propose high-level design and get buy-in

Multiple options can be used to generate unique IDs in distributed systems. The options we
considered are:
• Multi-master replication
• Universally unique identifier (UUID)
• Ticket server
• Twitter snowflake approach

**Ways to implement unique IDs**

1. UUID
    - A UUID is another easy way to obtain unique IDs. UUID is a 128-bit number used to identify
    information in computer systems. UUID has a very low probability of getting collusion.

1. Ticket Server
- Ticket servers are another interesting way to generate unique IDs. Flicker developed ticket
servers to generate distributed primary keys [2].

1. Twitter snowflake approach

### Step 3 - Design deep dive

Timestamp

Sequence number 

### Step 4 - wrap up

If there is extra time at the end of the interview, here are a few additional talking points:

- Clock synchronization. In our design, we assume ID generation servers have the same
clock. This assumption might not be true when a server is running on multiple cores. The
same challenge exists in multi-machine scenarios. Solutions to clock synchronization are
out of the scope of this book; however, it is important to understand the problem exists.
Network Time Protocol is the most popular solution to this problem. For interested
readers, refer to the reference material [4].
- Section length tuning. For example, fewer sequence numbers but more timestamp bits
are effective for low concurrency and long-term applications.
- High availability. Since an ID generator is a mission-critical system, it must be highly
available.