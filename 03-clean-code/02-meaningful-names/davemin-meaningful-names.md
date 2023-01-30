## 2. Naming

### (1) Name should tell the intent(Use Intention-Revealing Names)
    why it exists, what it does and how to use it. Make it easier to understand.
    E.G good name woulg be employeePaymentInfo vs ePay

### (2) Avoid Disinformation
    hp, aix, and sco variable?(X)

### (3) Make Meaningful Distinctions
    list1, list2(X) -> productInfo and productDetails

### (4) Use Pronounceable Names
    genymdhms (X) -> generationTimestamp
    modymdhms (X) -> modificationTimestamp

### (5) Use Searchable Names
    e, z, 8 -> Event, Max_Students, realDaysPerIdealDay, WORK_DAYS_PER_WEEK, NUMBER_OF_TASKS, realTaskDays, realTaskWeeks etx

### (6) Avoid Encodings
    - Hungarian Notation
    PhoneNumber phoneString -> PhoneNumber phone

    - Member Prefixes
    String m_dsc -> String description

    - Interfaces and Implementations
    IShapeFactory -> ShapeFactoryImp CshapeFactory ???

### (7) Avoid Mental Mapping
    for(a=0; a <10; a++) (X)
    for(b=0; b <10; b++) (X)

### (8) Class Names - Nouns
    : student, car, emplyee etc

### (8) Function Names - Verbs
    : postPayment, deletepage

### (9) Pick One word per Concept
    fetch, retrieve, get //  as equivalent methods
    controller, manager, driver // confusing

### (10) Don't Pun
    avoid using the same word for two purpose.

### (11) Use Solution Domain Name
    AccountVisitor, JobQueue (O)

### (12) Add Meaningful Context
    firstName, lastName, street, city, state, zipcode
    -> addrFirstName, addLastName, addrState // a better solution

    If the function is a bit too long and the variables are used throughout, then split the functions. it'e more clear.

### (13) Don't Add Gratuitous Cpntext
    Address // for a class name
    AccountAddress, CustomerAddress// for instances of the class address, not for classes

    Mac addresses, port addresses, Web addresses 
    PostalAddress, MAC, URI // a better solution