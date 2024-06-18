# Chapter 1:  Reliable, Scalable, and Maintainable Applications

This chapter explores the fundamental principles of data-intensive applications, focusing on **reliability, scalability, and maintainability.**

**Introduction**

- Most applications are data-intensive rather than compute-intensive.
- The three main concerns are reliability, scalability, and maintainability.

**Reliability**

- Ensures systems work correctly even when faults occur.

<aside>
💡 fault vs. failure 
A fault is usually defined as one component of the system deviating from its spec, whereas a *failure* is when the system as a
whole stops providing the required service to the user.

</aside>

- Faults can arise from hardware, software, and human errors.
    - **Hardware fault:**
        - examples: hard disks crash, RAM becomes faulty, the power grid has a blackout, someone unplugs the wrong network cable.
        - helps: there is a move toward systems that can tolerate the loss of entire machines, by using software fault-tolerance techniques
    - **Software errors:**
        - examples:
            - Software bug causing crashes (e.g., Linux kernel bug with leap second)
            - Runaway processes consuming resources
            - Dependent services slowing down or returning corrupted responses
            - Cascading failures triggering widespread issues
        - helps: no quick solution; Careful assumptions, thorough testing, process isolation, crash and restart mechanisms, continuous monitoring, and self-checking systems.
    - **Human:**
        - examples: Configuration errors by operators
        - helps:
            - Design systems to minimize errors with well-designed abstractions, APIs, and admin interfaces.
            - Provide non-production sandbox environments for safe experimentation.
            - Test thoroughly at all levels, using automated and manual tests.
            - Enable quick and easy recovery from errors, such as rolling back changes and gradual code rollouts.
            - Set up detailed monitoring and telemetry for early warnings and diagnostics.
            - Implement good management practices and training.

**Scalability**

- Scalability ensures a system handles increased load without performance degradation, crucial for maintaining reliability and efficiency as demand grows.
    - **Describing Load:**
    Load parameters quantify the current system load, helping predict and plan for growth:
        - **Requests per Second (RPS)**
        - **Reads/Writes Ratio**
        - **Active Users**
        - **Cache Hit Rate**
    - **Describing Performance:**
    Performance metrics assess how the system responds to load, focusing on:
        - **Response Time:** Time between a request and its response, measured in percentiles (e.g., median, 95th, 99th).
        - **Throughput:** Number of records processed per second in batch systems.
    - **Approaches for Coping with Load:**
        - **Vertical Scaling (Scaling Up):** Adding more resources to a single machine.
        - **Horizontal Scaling (Scaling Out):** Distributing load across multiple machines.
        - **Elastic Systems:** Automatically adding resources with increased load.
        - **Manual Scaling:** Human analysis to add resources based on needs.
