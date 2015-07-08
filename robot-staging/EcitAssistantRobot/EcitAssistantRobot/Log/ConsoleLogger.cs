using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log
{
    internal class ConsoleLogger : ILogger
    {
        public void Start()
        {
            Console.SetBufferSize(1024, 1024);
            Console.OutputEncoding = Encoding.UTF8;
        }

        public void Stop()
        {
        }

        public void Write(TraceEventType type, string message)
        {
            lock (_syncLock)
            {
                var line = string.Format("{0:yyyy-MM-dd hh:mm:ss.fff}\t[{1}]\t{2}", DateTime.Now, type, message);
                var color = GetForeColor(type);

                var oldForeColor = Console.ForegroundColor;
                Console.ForegroundColor = color;
                Console.WriteLine(line);
                Console.ForegroundColor = oldForeColor;
            }
        }

        private static ConsoleColor GetForeColor(TraceEventType type)
        {
            switch(type)
            {
                case TraceEventType.Verbose:
                    return ConsoleColor.Cyan;
                case TraceEventType.Information:
                    return ConsoleColor.Green;
                case TraceEventType.Warning:
                    return ConsoleColor.Yellow;
                case TraceEventType.Critical:
                case TraceEventType.Error:
                    return ConsoleColor.Red;

                case TraceEventType.Start:
                    return ConsoleColor.DarkGreen;
                case TraceEventType.Stop:
                    return ConsoleColor.DarkRed;
                case TraceEventType.Suspend:
                    return ConsoleColor.DarkYellow;
                case TraceEventType.Resume:
                    return ConsoleColor.DarkCyan;

                case TraceEventType.Transfer:
                    return ConsoleColor.DarkMagenta;

                default:
                    return ConsoleColor.DarkGray;
            }
        }

        private static readonly object _syncLock = new object();
    }
}
