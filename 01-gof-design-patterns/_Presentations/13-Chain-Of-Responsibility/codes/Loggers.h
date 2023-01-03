#pragma once

#include "Logger.h"
#include <iostream>

namespace Log
{
    class ConsoleLogger : public CLogger 
    {
    public:
        ConsoleLogger(int mask) : CLogger(mask)
        {

        }

        virtual void WriteMessage(const std::string& msg)
        {
            std::cout << "STDIO... " << msg << std::endl;
        }
    };

    class EmailLogger : public CLogger 
    {
    public:
        EmailLogger(int mask) : CLogger(mask)
        {

        }

        virtual void WriteMessage(const std::string& msg)
        {
            std::cout << "HTTP SMTP Request... " << msg << std::endl;
        }
    };

    class FileLogger : public CLogger 
    {
    public:
        FileLogger(int mask) : CLogger(mask)
        {

        }

        virtual void WriteMessage(const std::string& msg)
        {
            std::cout << "Flusing to disk... " << msg << std::endl;
        }
    };
}