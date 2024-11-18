# A UNIQUE ID GENERATOR IN DISTRIBUTED SYSTEMS

## Step 1: Questions to ask
- characteristic? IDs must be unique.
- increment by 1? Not necessarily, IDs are ordered by date.
- only numerical? Yes
- length requirement? IDs fit into 64-bit.
- Scale of the system? Ability to generate over 10,000 unique IDs per second.

## Step 2: Propose high-level design and get buy-in
Options: • Multi-master replication • Universally unique identifier (UUID) • Ticket server • Twitter snowflake approach

### Multi-master replication
- Increment by the number of DB servers in use
- **Drawbacks**
  - Hard to scale with multiple data centers
  - IDs do not go up with time across multiple servers.
  - It does not scale well when a server is added or removed.

### UUID
- 128-bit number used to identify information in computer systems, a very low probability of getting collusion
- Simple, easy to scale, no coordination between servers is required, no synchronization issue.
**- Drawbacks**
  - IDs are 128 bits long, but our requirement is 64 bits.
  - IDs do not go up with time.
  - IDs could be non-numeric.

### Ticket Server
- use a centralized auto_increment feature in a single database server (Ticket Server)
- numeric ID, easy and works for small to medium-scale applications.
- Drawback: single point of failure - multiple ticket servers will add new challenges for synchronization

## Step 3 - Design deep dive
### Twitter snowflake approach
- Divide an ID into different sections. Meets the requirements
![Screenshot 2024-11-18 at 8 52 33 AM](https://github.com/user-attachments/assets/9800fa47-41ce-49ec-b49b-0d00c3828aa1)
- Sign bit: always be 0, reserved for future uses. It can potentially be used to distinguish between signed and unsigned numbers.
- Datacenter IDs and machine IDs are chosen at the startup time, generally fixed

- Timestamp
  - As timestamps grow with time, IDs are sortable by time.
  - with 41 bit, 2^41 = 2199023255551, 69 years

- Sequence number
  - For every ID generated on that machine/process, the sequence number is incremented by 1 and reset to 0 every millisecond
  - A machine can support a maximum of 4096 new IDs per millisecond (12 bits, 2 ^ 12 = 4096 combinations)

## Additional talking points
- Clock synchronization.
   - we assume ID generation servers have the same clock. This assumption might not be true when a server is running on multiple cores.
   - Solution: Network Time Protocol
- Section length tuning: fewer sequence numbers but more timestamp bits are effective for low concurrency and long-term applications.
- High availability: an ID generator is a mission-critical system, it must be highly available.
