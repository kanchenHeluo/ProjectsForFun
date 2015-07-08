using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot
{
    /// <summary>
    /// Represents a robot that can response users' query.
    /// </summary>
    public interface IRobot
    {
        /// <summary>
        /// Main function of the robot.
        /// Once this method returns, means the robot is shutted down.
        /// </summary>
        /// <param name="shell">Use shell to interactive with user.</param>
        /// <param name="logger">User logger to record diagnostics information.</param>
        void Run(IShell shell, ILogger logger);
    }
}
