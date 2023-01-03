#pragma once

#include <string>

namespace Log
{
    enum LogLevels
    {
        None = 0,                 //        0
        Info = 1,                 //        1
        Debug = 2,                //       10
        Warning = 4,              //      100
        Error = 8,                //     1000
        FunctionalMessage = 16,   //    10000
        FunctionalError = 32,     //   100000
        All = 63                  //   111111
    };

    class CLogger
    {
    public:
        CLogger(int mask);
        CLogger* SetNext(CLogger* nextLogger);
        virtual void Message(const std::string& msg, int severity);
        virtual void WriteMessage(const std::string& msg) {};
    protected:
        LogLevels mLogMask;
        CLogger* mpNext;
    };
}