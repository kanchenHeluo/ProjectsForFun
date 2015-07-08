using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log
{
    public interface ILogger
    {
        void Start();

        void Stop();

        void Write(TraceEventType type, string message);
    }
}
