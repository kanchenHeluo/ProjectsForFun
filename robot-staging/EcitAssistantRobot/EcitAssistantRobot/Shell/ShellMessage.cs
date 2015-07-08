using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell
{
    /// <summary>
    /// Represents a message read from shell.
    /// </summary>
    public class ShellMessage
    {
        /// <summary>
        /// Identifier of a conversation.
        /// </summary>
        public string ConversationID { get; set; }

        /// <summary>
        /// Name of the participant, who start the dialogue.
        /// </summary>
        public string ParticipantName { get; set; }

        /// <summary>
        /// Content of the message.
        /// </summary>
        public string Content { get; set; }

        public string ParticipantUri { get; set; }
    }
}
