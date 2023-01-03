#include "Logger.h"
#include "Loggers.h"

using namespace Log;
int main()
{
    Log::CLogger* logger = new ConsoleLogger(LogLevels::All);
    logger = logger->SetNext(new EmailLogger(LogLevels::FunctionalMessage | LogLevels::FunctionalError));
    logger = logger->SetNext(new FileLogger(LogLevels::Warning | LogLevels::Error));

    logger->Message("Start of debugger", LogLevels::Debug);
    
    logger->Message("Order Dispatched", LogLevels::FunctionalMessage);
    
    logger->Message("File flush", LogLevels::Warning);

    logger->Message("End of debugger", LogLevels::All);
}