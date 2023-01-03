#include "Logger.h"

using namespace Log;

CLogger::CLogger(int mask) :
    mLogMask((LogLevels)mask),
    mpNext(0)
{
}

CLogger* CLogger::SetNext(CLogger* nextLogger)
{
    if (mpNext != 0)
        nextLogger->mpNext = mpNext;
    
    mpNext = nextLogger;
    return this;
}

void CLogger::Message(const std::string& msg, int severity)
{
    if ((severity & mLogMask) != 0)
    {
        WriteMessage(msg);
    }

    if (mpNext != NULL) 
    {
        mpNext->Message(msg, severity);
    }
}