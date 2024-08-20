**Batch processing systems (offline systems)**
- A batch processing system takes a large amount of input data, runs a job to process it, and produces some output data.
- often scheduled to run periodically (for example, once a day).
- The primary performance measure of a batch job is usually throughput (the time it takes to crunch through an input dataset of a certain size).
  
**Stream processing systems (near-real-time systems)**
- somewhere between online and offline/batch processing (so it is sometimes called near-real-time or nearline processing).
- Like a batch processing system, a stream processor consumes inputs and produces outputs (rather than responding to requests).
- However, a stream job operates on events shortly after they happen, whereas a batch job operates on a fixed set of input data.
- have lower latency than the equivalent batch systems.

**MapReduce**
- was called “the algorithm that makes Google so massively scalable”, and subsequently implemented in various open source data systems, including Hadoop, CouchDB, and MongoDB.

## Batch Processing with Unix Tools
### The Unix Philosophy
- design principles that became popular among the developers and users of Unix, described in 1978
- automation, rapid prototyping, incremental iteration, being friendly to experimentation, and breaking down large projects into manageable chunks
1. Make each program do one thing well. To do a new job, build afresh rather than complicate old programs by adding new “features”.
2. Expect the output of every program to become the input to another, as yet unknown, program. Don’t clutter output with extraneous information. Avoid stringently columnar or binary input formats.** Don’t insist on interactive input.**
3. Design and build software, even operating systems, to be tried early, ideally within weeks. Don’t hesitate to throw away the clumsy parts and rebuild them.
4. Use tools in preference to unskilled help to lighten a programming task, even if you have to detour to build the tools and expect to throw some of them out after you’ve finished using them.

- A Unix shell like bash lets us easily compose these small programs into surprisingly powerful data processing jobs.
- What does Unix do to enable this composability?

#### A uniform interface
- To connect any program’s output to any program’s input, that means that all programs must use the same input/output interface.
- In Unix, that interface is a file (or, more precisely, a file descriptor). - ASCII text.
   - A file is just an ordered sequence of bytes, very simple interface
   - many different things can be represented using the same interface:
      - an actual file on the filesystem, a communication channel to another process (Unix socket, stdin, stdout), a device driver (say /dev/audio or /dev/lp0), a socket representing a TCP connection. 

#### Separation of logic and wiring
- their use of standard input (stdin) and standard output (stdout). If you run a program and don’t specify anything else, stdin comes from the keyboard and stdout goes to the screen.
- Separating the input/output wiring from the program logic makes it easier to compose small tools into bigger systems.
- limit: You can’t pipe a program’s output into a network connection. If a program directly opens files for reading and writing, or starts another program as a subprocess, or opens a network connection, then that I/O is wired up by the program itself.
  
#### Transparency and experimentation
- they make it quite easy to see what is going on:
• The input files to Unix commands are normally treated as immutable.
  - This means you can run the commands as often as you want, trying various command-line options, without damaging the input files.
• You can end the pipeline at any point, pipe the output into less, and look at it to see if it has the expected form. This ability to inspect is great for debugging.
• You can write the output of one pipeline stage to a file and use that file as input to the next stage.
  - This allows you to restart the later stage without rerunning the entire pipeline.

## MapReduce and Distributed Filesystems
**- the biggest limitation of Unix tools is that they run only on a single machine**—and that’s where tools like Hadoop come in. 
- MapReduce is a bit like Unix tools, but distributed across potentially thousands of machines.
