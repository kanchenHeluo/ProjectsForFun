using Microsoft.Lync.Model.Conversation;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.LyncShell
{
    class LyncInstantMessage
    {
        public InstantMessageModality Modality { get; set; }

        public string Content { get; set; }
    }
}
