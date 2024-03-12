# CHAPTER 7: DESIGN A UNIQUE ID GENERATOR IN DISTRIBUTED SYSTEMS
> auto_increment does not work in a distributed environment

## Step 1
- Requirements are listed as follows:
  - IDs must be unique.
  - IDs are numerical values only.
  - IDs fit into 64-bit.
  - IDs are ordered by date.
  - Ability to generate over 10,000 unique IDs per second.

## Step 2
- Multi-master replication -> not fix distribution system
- Universally unique identifier (UUID) -> sort problem
- Ticket server -> single point of failure
- Twitter snowflake approach -> Good

## Step 3
> 1 bit | 41 bits | 5 bits | 5 bits | 12 bits
1. 1 bit: reserved for future uses
2. 41 bits: timestamp <- Milliseconds since the epoch or custom epoch
3. 5 bits: datacenter ID
4. 12 bits: sequence number <- 2 ^ 12 = 4096 combinations <- 4096 new IDs per millisecond

## Step 4
- Clock synchronization
- Section length tuning
- High availability <- a mission-critical system