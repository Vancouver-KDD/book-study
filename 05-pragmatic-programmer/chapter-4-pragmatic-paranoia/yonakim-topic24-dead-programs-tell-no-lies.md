# Dead Programs Tell No Lies

## Crash Early

* It's easy to fall into the "it can't happen" mentality. The code wouldn't fail under normal conditions. However, these can happen:
    * Code hits _default_ case unexpectedly.
    * We've passed in a _nil_ value or an empty list.
    * Missing key in hash.
    * Network error or filesystem error that we didn't catch. (empty or corrupted data)
* We should code defensively.
    * Data is what we think it is.
    * Code in production is the code we think it is.
    * The correct versions of dependencies are actually loaded.

* All errors give you information. If there is an error, something very bad has happened.

## Catch and Release is For Fish

* Don't catch or rescue all exceptions!
    * If writer of the method adds another exception, this code is subtle and out of date.
```
try do
    add_score_to_board(score);
rescue InvalidScore
    Logger.error("Can't add invalid score. Exiting"); 
    raise
rescue BoardServerDown
    Logger.error("Can't add score: board is down. Exiting"); 
    raise
rescue StaleTransaction
    Logger.error("Can't add score: stale transaction. Exiting"); 
    raise
end
```

* Do this instead:
    * Application code shouldn't be eclipsed by the error handling.
    * Code should be less coupled.
    * Any new exception is automatically propagated.
```
    add_score_to_board(score);
```

## Crash, Don't trash!
* Crash early.
* Defensive programming (high-availability, fault-tolerant):
    * Design composed of _supervisor trees_ - program is designed to fail, but that failure is managed with _supervisors_. A supervisor is responsible for running code and knows what to do in case the code fails, which could include cleaning up after it.

* When your code discovers that something that was supposed to be impossible just happenend, your program is no longer viable. Anything it does from this point forward becomes suspect, so terminate it ASAP.

* A dead program normally does a lot less damange than a crippled one.
