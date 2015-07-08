using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot
{
    class Session
    {
        public string ConversationID { get; set; }

        public string ParticipantName { get; set; }

        public RobotK Robot { get; set; }
    }
}
