using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.LyncShell;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot
{
    class Program
    {
        //static readonly Func<IShell> _createShell = () => new FakeImplements.FakeShell();
        static readonly Func<IShell> _createShell = () => new LyncShell.LyncShellImpl(@"ecitcnr@microsoft.com", @"1qaz!QAZ", _logger);
        //static readonly Func<IShell> _createShell = () => CreateLyncShell();

        //static readonly Func<IRobot> _createRobot = () => new FakeImplements.FakeRobot();
        //static readonly Func<IRobot> _createRobot = () => new BieFunRobot();
        static readonly Func<IRobot> _createRobot = () => new EcitAssistantRobotImpl();

        static IShell _shell;
        static ILogger _logger;
        static IRobot _robot;

        static void Main(string[] args)
        {
            try
            {
                Initialize();
                _robot.Run(_shell, _logger);
            }
            catch(Exception ex)
            {
                _logger.Write(TraceEventType.Critical, ex.ToString());
            }
            finally
            {
                Shutdown();
            }

#if DEBUG
            Console.Write("Press any key to exit.");
            Console.ReadKey(true);
#endif
        }

        static void Initialize()
        {
            _logger = new CompositeLogger(new ConsoleLogger(), new FileLogger());
            _logger.Start();
            _logger.Write(TraceEventType.Verbose, "Logger created.");

            _logger.Write(TraceEventType.Information, "Initializing.");

            _logger.Write(TraceEventType.Verbose, "Creating shell.");
            _shell = _createShell();

            if (_shell == null)
                throw new ApplicationException("The shell is not created.");

            _logger.Write(TraceEventType.Verbose, "Starting shell.");
            _shell.Start();

            _logger.Write(TraceEventType.Verbose, "Creating robot.");
            _robot = _createRobot();

            _logger.Write(TraceEventType.Information, "Initialized.");
        }

        static void Shutdown()
        {
            _logger.Write(TraceEventType.Information, "Shutting down.");

            _logger.Write(TraceEventType.Verbose, "Stopping shell.");
            if (_shell != null)
            {
                try
                {
                    _shell.Stop();
                }
                catch(Exception ex)
                {
                    _logger.Write(TraceEventType.Critical, ex.ToString());
                }
            }

            _logger.Write(TraceEventType.Verbose, "Stopping logger.");
            if (_logger != null)
            {
                try
                {
                    _logger.Stop();
                }
                catch(Exception ex)
                {
                    Console.WriteLine(ex);
                }
            }
        }

        static IShell CreateLyncShell()
        {
            using(var dlg = new LyncCredentialDialog())
            {
                if(DialogResult.OK == dlg.ShowDialog())
                {
                    return new LyncShellImpl(dlg.UserAddress, dlg.Password, _logger);
                }
                else
                {
                    return null;
                }
            }
        }
    }
}
