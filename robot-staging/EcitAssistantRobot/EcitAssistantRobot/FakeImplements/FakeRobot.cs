using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.FakeImplements
{
    internal class FakeRobot : IRobot
    {
        public void Run(IShell shell, ILogger logger)
        {
            while (true)
            {
                if (!shell.IsReady)
                    continue;

                shell.Write(new ShellMessage { Content = "[Robot] Hello, please input something:" });

                var msg = shell.Read();
                var response = new ShellMessage
                {
                    ConversationID = msg.ConversationID,
                    ParticipantName = msg.ParticipantName,
                };

                if (msg.Content.StartsWith("exit"))
                {
                    response.Content = "[Robot] Bye.";
                    shell.Write(response);
                    break;
                }

                response.Content = "[Robot] I've received your message:";
                shell.Write(response);
                response.Content = string.Format("ConversationID={0}, ParticipantName={1}, Content={2}",
                    msg.ConversationID, msg.ParticipantName, msg.Content);
                shell.Write(response);
                response.Content = "[Robot] But I'm not a real robot, type 'exit' to exit.";
                shell.Write(response);
            }
        }
    }
}
