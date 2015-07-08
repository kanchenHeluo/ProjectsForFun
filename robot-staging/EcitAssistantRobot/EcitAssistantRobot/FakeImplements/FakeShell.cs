using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.FakeImplements
{
    internal class FakeShell : IShell
    {
        public bool IsReady
        {
            get { return true; }
        }

        public void Start()
        {
            Console.ForegroundColor = ConsoleColor.White;
        }

        public void Stop()
        {
        }

        public ShellMessage Read()
        {
            Console.Write("> ");
            var line = Console.ReadLine();
            var msg = new ShellMessage
            {
                ConversationID = _ConversationID,
                ParticipantName = _paticipantName,
                Content = line,
            };
            return msg;
        }

        public void Write(ShellMessage message)
        {
            Console.Write("> ");
            Console.WriteLine(message.Content);
        }

        public string AddConversation()
        {
            return Guid.NewGuid().ToString();
        }

        public void AddParticipant(string conversationID, string user)
        {
        }

        private string _ConversationID = Guid.NewGuid().ToString();
        private string _paticipantName = "Yanbo Liu";
    }
}
