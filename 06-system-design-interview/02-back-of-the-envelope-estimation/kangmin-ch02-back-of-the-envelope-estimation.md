# Chapter 2 : Back of the envelope estimation

## Power of two

- Data Volume 을 다룰 때, Power of two 로 주로 표현 되며, 이를 이해 할 필요가 있다.

| Power | Approximate Value | Full name  | Short name |
|-------|-------------------|------------|------------|
| 10    | 1 Thousand        | 1 Kilobyte | 1 KB       |
| 20    | 1 Million         | 1 Megabyte | 1 MB       |
| 30    | 1 Billion         | 1 Gigabyte | 1 GB       |
| 40    | 1 Trillion        | 1 Terabyte | 1 TB       |
| 50    | 1 Quadrillion     | 1 Petabyte | 1 PB       |

## Latency numbers every programmer should know

- 지연시간(Latency) 를 나타내는 방식들
- ns = nanosecond, μs = microsecond, ms = millisecond

| Operation name                                   | Time                    |
|--------------------------------------------------|-------------------------|
| L1 cache reference                               | 0.5 ns                  |
| Branch mispredict                                | 5 ns                    |
| L2 cache reference                               | 7ns                     |
| Mutex lock/unlock                                | 100 ns                  |
| Main memory reference                            | 100 ns                  |
| Compress 1K bytes with Zippy                     | 10,000 ns = 10 μs       |
| Send 2K bytes over 1 Gbps network                | 20,000 ns = 20 μs       |
| Read 1 MB sequentially from memory               | 250,000 ns = 250 μs     |
| Round trip within the same datacenter            | 500,000 ns = 500 μs     |
| Disk seek                                        | 10,000,000 ns = 10 ms   |
| Read 1 MB sequentially from the network          | 10,000,000 ns = 10 ms   |
| Read 1 MB sequentially from the disk             | 30,000,000 ns = 30 ms   |
| Send packet CA (California) -> Netherlands -> CA | 150,000,000 ns = 150 ms |


- Memory 가 disk 보다 빠르다 -> 왠만하면 disk seeks 을 ㅍ하자
- 간단한 compression 작업 역시 리소스를 많이 먹지 않는다.
- 위 표를 보면 compression 을 한 뒤 network 로 보내는 것이 Latency 를 줄일 수 있 음.
- Data Center 의 위치를 무시 할 수 없다... 마지막 표를 보면

## Availability numbers

- High availability 이란 시스템이 지속적으로 운영될 수 있는 능력을 말한다.
- 100% 는 0의 downtime 을 의미한다.
- 대부분 서비스들은 99 ~ 100 % 의 downtime 을 갖는다.

| Availability (%) | Downtime per day    | Downtime per year |
|------------------|---------------------|-------------------|
| 99%              | 14.40 minutes       | 3.65 days         |
| 99.9%            | 14.4 minutes        | 8.77 hours        |
| 99.99%           | 8.64 seconds        | 52.60 minutes     |
| 99.999%          | 864.00 milliseconds | 5.26 minutes      |
| 99.9999%         | 86.40 milliseconds  | 31.56 seconds     |
