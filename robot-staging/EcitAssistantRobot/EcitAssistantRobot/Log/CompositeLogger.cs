using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log
{
    internal class CompositeLogger : ILogger
    {
        public CompositeLogger(params ILogger[] loggers)
        {
            if (loggers == null)
                throw new ArgumentNullException("loggers");

            _loggers = loggers;
        }

        public void Start()
        {
            var exceptions = new List<Exception>();

            foreach(var log in _loggers)
            {
                try
                {
                    log.Start();
                }
                catch(Exception ex)
                {
                    exceptions.Add(ex);
                }
            }

            if (exceptions.Count > 0)
                throw new AggregateException(exceptions);
        }

        public void Stop()
        {
            var exceptions = new List<Exception>();

            foreach (var log in _loggers)
            {
                try
                {
                    log.Stop();
                }
                catch (Exception ex)
                {
                    exceptions.Add(ex);
                }
            }

            if (exceptions.Count > 0)
                throw new AggregateException(exceptions);
        }

        public void Write(System.Diagnostics.TraceEventType type, string message)
        {
            var exceptions = new List<Exception>();

            foreach (var log in _loggers)
            {
                try
                {
                    log.Write(type, message);
                }
                catch (Exception ex)
                {
                    exceptions.Add(ex);
                }
            }

            if (exceptions.Count > 0)
                throw new AggregateException(exceptions);
        }

        private ILogger[] _loggers;
    }
}
