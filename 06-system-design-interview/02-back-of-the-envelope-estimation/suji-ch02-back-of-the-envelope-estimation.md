# CH2. **BACK-OF-THE-ENVELOPE ESTIMATION**

# Power of two

Data volumes can become enormous, but calculation boils down to basics.

For precise calculations, you need to be aware of the power of two, which corresponds to given data units:

- 2^10 == ~1000 == 1kb
- 2^20 == ~1mil == 1mb
- 2^30 == ~1bil == 1gb
- 2^40 == ~1tril == 1tb
- 2^50 == ~1quad == 1pb

# Latency numbers every programmer should know

Some conclusions from the above numbers:

- Memory is fast, disk is slow
- Avoid disk seeks if possible
- Compression is usually fast
- Compress data before sending over the network if possible
- Data centers are usually in different regions, and it takes time to send data between them.

# Availability numbers

High availability is the ability of a system to be continuously operational for a desirably long
period of time. Most services’ availability fall between 99% and 100%.

### SLA: Service Level Agreement

An SLA(Service level agreement) is a formal agreement between a service provider and a customer. Cloud providers typically set their SLA at 99.9% or more.
 

![The number of nines correlate to the expected system downtime: ](https://github.com/preslavmihaylov/booknotes/raw/master/system-design/system-design-interview/chapter03/images/sla-chart.png)

The number of nines correlate to the expected system downtime: