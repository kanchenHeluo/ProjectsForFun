using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log
{
    internal class FileLogger : ILogger
    {
        public void Start()
        {
            this.Stop();

            var filename = string.Format("{0:yyyy_MM_dd__HH_mm_ss_fff}__{1}.txt", DateTime.Now, Process.GetCurrentProcess().Id);
            _logFileStream = new FileStream(filename, FileMode.Create, FileAccess.Write, FileShare.Read);
            _logWriter = new StreamWriter(_logFileStream, Encoding.UTF8);
        }

        public void Stop()
        {
            if(_logWriter != null)
            {
                _logWriter.Dispose();
                _logWriter = null;
            }

            if(_logFileStream != null)
            {
                _logFileStream.Dispose();
                _logFileStream = null;
            }
        }

        public void Write(TraceEventType type, string message)
        {
            var line = string.Format("{0:yyyy-MM-dd hh:mm:ss.fff}\t[{1}]\t{2}", DateTime.Now, type, message);
            _logWriter.WriteLine(line);
            _logWriter.Flush();
        }

        private static readonly object _syncRoot = new object();
        private FileStream _logFileStream;
        private StreamWriter _logWriter;
    }
}
