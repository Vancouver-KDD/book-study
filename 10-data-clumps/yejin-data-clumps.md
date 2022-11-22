# Data clumps 

## Quick preview 
Sometimes, you just find out that a small set of data items are constantly passed around together across different sections of your code.     
You can extract it by class or function. By doing that, you can reduce long parameter list and make method easy to call.  
어떤 데이터들이 항상 같이 다닐때 이걸 set 으로 묶어서 구성할 수 있다.  

```java

public class Booking
{
    public Booking(int bookingId, int roomId, DateTime? from, DateTime? to)
    {
        BookingId = bookingId;
        RoomId = roomId;
        From = from;
        To = to;
    }
    public int BookingId { get; private set; }
    public int RoomId { get; private set; }
    public DateTime? From { get; private set; }
    public DateTime? To { get; private set; }
}

///after

public class TimeInterval
{
    public TimeInterval(DateTime? from = null, DateTime? to = null)
    {
        From = from;
        To = to;
    }
    public DateTime? From { get; set; }
    public DateTime? To { get; set; }
}


public Booking(int bookingId, int roomId, TimeInterval timeInterval)
    {
        BookingId = bookingId;
        RoomId = roomId;
        Interval = timeInterval;
    }
```

[Join data items that want to go together – code smells series](https://blog.jetbrains.com/dotnet/2018/07/02/join-data-items-want-go-together-code-smells-series/)