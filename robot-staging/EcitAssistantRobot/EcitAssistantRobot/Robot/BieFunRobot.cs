using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot
{
    public class BieFunRobot : IRobot
    {
        public void Run(IShell shell, ILogger logger)
        {
            while (!shell.IsReady) ;

            var cid = shell.AddConversation();
            shell.AddParticipant(cid, "yanbol@microsoft.com");
            //shell.AddParticipant(cid, "tigeren@microsoft.com");
            //shell.AddParticipant(cid, "jeffsun@microsoft.com");
            //shell.AddParticipant(cid, "vifeng@microsoft.com");
            //shell.AddParticipant(cid, "liy@microsoft.com");
            //shell.AddParticipant(cid, "leilu@microsoft.com");
            //shell.AddParticipant(cid, "t-diwei@microsoft.com");
            //shell.AddParticipant(cid, "bingu@microsoft.com");
            //shell.AddParticipant(cid, "pauli@microsoft.com");
            //shell.AddParticipant(cid, "kanchen@microsoft.com");
            //shell.AddParticipant(cid, "dalya@microsoft.com");
            //shell.AddParticipant(cid, "jduan@microsoft.com");
            //shell.AddParticipant(cid, "wemu@microsoft.com");
            //shell.AddParticipant(cid, "t-lidong@microsoft.com");
            //shell.AddParticipant(cid, "jl@microsoft.com");

            shell.Write(new ShellMessage { ConversationID = cid, Content = "[BieFunRobot] Helle, I'm BIE FUN robot." + _helpString });

            while (true)
            {
                var msg = shell.Read();

                // God model.
                if ((msg.ParticipantName == "Yanbo Liu" || msg.ParticipantName == "Kan Chen")
                    && msg.Content == "exit\r\n")
                {
                    shell.Write(new ShellMessage
                    {
                        ConversationID = msg.ConversationID,
                        Content = "[BieFunRobot] Bye."
                    });
                    break;
                }

                // For other conversation.
                if (msg.ConversationID != cid)
                {
                    shell.Write(new ShellMessage
                    {
                        ConversationID = msg.ConversationID,
                        Content = string.Format("[BieFunRobot] This account is taken over by a robot."),
                    });
                    continue;
                }

                // Ignore general dialogues.
                if (!msg.Content.StartsWith("@fun", StringComparison.InvariantCultureIgnoreCase))
                {
                    if(IsHelpCommand(msg.Content.Trim().ToLower()))
                        shell.Write(new ShellMessage { ConversationID = cid, Content = "[BieFunRobot]" + _helpString });
                    continue;
                }

                var answer = GetAnswer(msg);

                shell.Write(new ShellMessage
                {
                    ConversationID = msg.ConversationID,
                    Content = string.Format("[BieFunRobot] {0}, {1}", msg.ParticipantName, answer),
                });
            }
        }

        string GetAnswer(ShellMessage msg)
        {
            var cmd = msg.Content.Substring(4).Trim().ToLower();  // Remove '@fun' prefix and surrounding blanks.

            string answer = null;

            if (IsHelpCommand(cmd))
                return _helpString;

            // Fixed commands.
            switch (cmd)
            {
                case "who":
                case "whofun":
                case "who fun":
                case "whofunhere":
                case "who fun here":
                    answer = ListFunners();
                    break;
            }

            if (answer != null)
                return answer;

            if (_registerFunRegex.IsMatch(cmd))
            {
                int n = 0;

                if(!int.TryParse(cmd, out n))
                {
                    answer = "我不识数，你别骗我。。。";
                }
                else if (n == 0)
                {
                    answer = "别淘气！";
                }
                else if (n < 0)
                {
                    answer = "尼玛还想带走几位不成？！";
                }
                else if (n > 3)
                {
                    answer = "来这么多人，想吃霸王餐啊？！";
                }
                else
                {
                    if (_registeredFunners.ContainsKey(msg.ParticipantName))
                    {
                        var oldN = _registeredFunners[msg.ParticipantName];

                        if (n == oldN)
                        {
                            answer = string.Format("寻开心是不是？你已经预定了{0}个人了。", n);
                        }
                        else
                        {
                            _registeredFunners[msg.ParticipantName] = n;
                            answer = string.Format("本来呢，你已经预定了{0}个人，现在帮你改成{1}个啦~", oldN, n);
                        }
                    }
                    else
                    {
                        _registeredFunners.Add(msg.ParticipantName, n);
                        answer = string.Format("恭喜，你已成功预订{0}人！", n);
                    }
                }
            }

            if (answer != null)
                return answer;

            return "I don't understand '" + cmd + "'.\r\n" + _helpString;
        }

        string ListFunners()
        {
            if (_registeredFunners.Count == 0)
                return "真悲桑，没人订饭……";

            var sb = new StringBuilder();
            sb.AppendFormat("总共{0}预订，共计预订了{1}人。",
                _registeredFunners.Count,
                _registeredFunners.Select(r => r.Value).Sum());
            sb.AppendLine();

            foreach(var p in _registeredFunners.Keys)
            {
                sb.AppendFormat("{0} 预定了{1}位。", p, _registeredFunners[p]);
                sb.AppendLine();
            }

            return sb.ToString();
        }

        bool IsHelpCommand(string cmd)
        {
            return cmd == "?" || cmd == "？" || cmd == "help";
        }

        const string _helpString = @"
- help -
Type '?', 'h', 'help' to show this help.
Support commands:
@fun +<n>   - Register for fun, where <n> should be between 1 and 3 (both are inclusive).
@fun who    - List people who replyed.";

        static readonly Regex _registerFunRegex = new Regex(@"^[+|-]\d+$");

        Dictionary<string, int> _registeredFunners = new Dictionary<string, int>();
    }
}
