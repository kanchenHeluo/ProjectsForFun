using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.SearchEngines;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot
{
    public class ReminderPage
    {
        public string fromWho;
        public string sender;
        public string info;
        public bool hasReminded;
        public DateTime reminderTime;
    }

    public class EcitAssistantRobotImpl : IRobot
    {
        static readonly BingSearchEngine _bing = new BingSearchEngine();

        public void Run(IShell shell, ILogger logger)
        {
            while (!shell.IsReady) ;

            //var cid = shell.AddConversation();
            //shell.AddParticipant(cid, "yanbol@microsoft.com");
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

            //shell.Write(new ShellMessage { ConversationID = cid, Content = "[EcitAssistantRobot] Helle, I'm robot born by 大王." });

            List<ReminderPage> reminder = new List<ReminderPage>();
            TimeExtract te = new TimeExtract();
            bool firstTime = true;

            while (true)
            {
                if (!shell.IsReady)
                    continue;

                // check reminder:
                for (int i = 0; i < reminder.Count; ++i)
                {
                    if (!reminder[i].hasReminded && reminder[i].reminderTime.CompareTo(System.DateTime.Now) < 0)
                    {
                        var cid = shell.AddConversation();
                        shell.AddParticipant(cid, reminder[i].sender);
                        shell.Write(new ShellMessage
                        {
                            ConversationID = cid,
                            Content = reminder[i].fromWho + " remind U that : " + reminder[i].info
                        });
                        reminder[i].hasReminded = true;
                    }
                }

                // remove hasReminded reminder
                for (int i = reminder.Count - 1; i >= 0; i--)
                {
                    if (reminder[i].hasReminded)
                    {
                        reminder.Remove(reminder[i]);
                    }
                }

                var msg = shell.Read();
                if (msg == null) // loop out to do reminder
                {
                    System.Threading.Thread.Sleep(200);
                    continue;
                }

                var response = new ShellMessage
                {
                    ConversationID = msg.ConversationID,
                    ParticipantName = msg.ParticipantName,
                };

                //----------------------------
                // Fake for FY15 Kickoff, must be removed before anybody see this.
                var msgstr = msg.Content.Trim().ToLower();
                if (msgstr.Contains("gems") && msgstr.Contains("are you there"))
                {
                    // for 'Hi GEMs, are you there?'
                    var firstName = msg.ParticipantName;
                    var nameParts = msg.ParticipantName.Split(new[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
                    if (nameParts.Length > 1)
                        firstName = nameParts[0];
                    
                    string ans = string.Format("Yes, {0}. How can I help you?", firstName);
                    response.Content = string.Format("<p style='font-size:20;font-family=Corbel;font-weight:bold;color=#007500;'>To [{0}]: {1}</p>", msg.ParticipantName, ans); 
                    shell.Write(response);
                    continue;
                }

                if (msgstr.Contains("is") && msgstr.Contains("who") && msgstr.Contains("faith") && firstTime)
                {
                    firstTime = false;
                    // for 'Can you tell us Who Faith is?'
                    string ans = "Searching...";
                    System.Threading.Thread.Sleep(800);
                    response.Content = string.Format("<p style='font-size:20;font-family=Corbel;font-weight:bold;color=#007500;'>To [{0}]: {1}</p>", msg.ParticipantName, ans); 
                    shell.Write(response);
                    System.Threading.Thread.Sleep(800);
                    shell.Write(response);
                    System.Threading.Thread.Sleep(1000);
                    shell.Write(response);
                    System.Threading.Thread.Sleep(800);
                    ans = "Retrieving Info from Linked in –";
                    response.Content = string.Format("<p style='font-size:20;font-family=Corbel;font-weight:bold;color=#007500;'>To [{0}]: {1}</p>", msg.ParticipantName, ans); 
                    shell.Write(response);
                    System.Threading.Thread.Sleep(1200);
                    ans = "Faith is currently working as Chief of Staff/Business Manager at Microsoft. Previously worked at AT&T and more… Sorry that's all I got.";
                    response.Content = string.Format("<p style='font-size:20;font-family=Corbel;font-weight:bold;color=#007500;'>To [{0}]: {1}</p>", msg.ParticipantName, ans); 
                    shell.Write(response);
                    continue;
                }
                else if (msgstr.Contains("is") && msgstr.Contains("who") && msgstr.Contains("faith"))
                {
                    string ans = "Faith is working as Chief of Staff/Business Manager at Microsoft. Previously worked at AT&T.";
                    response.Content = string.Format("<p style='font-size:20;font-family=Corbel;font-weight:bold;color=#007500;'>To [{0}]: {1}</p>", msg.ParticipantName, ans);
                    shell.Write(response);
                    continue;
                }else if(msgstr.Contains("b-bye")){
                    string ans = "Bye, Nakul; Bye, Tony and Faith.";
                    response.Content = string.Format("<p style='font-size:20;font-family=Corbel;font-weight:bold;color=#007500;'>To [{0}]: {1}</p>", msg.ParticipantName, ans);
                    shell.Write(response);
                    continue;
                }
                //----------------------------

                if ((msg.ParticipantName == "Yanbo Liu" || msg.ParticipantName == "Kan Chen") && msg.Content.StartsWith("exit"))
                {
                    response.Content = "Bye.";
                    shell.Write(response);
                    break;
                }

                //if reminder
                string answer = string.Empty;

                List<ReminderPage> rList = te.isReminder(msg.Content, msg.ParticipantUri);
                //List<ReminderPage> rList = new List<ReminderPage>();
                if (rList.Count == 0)
                {
                    var session = GetSession(msg.ConversationID, msg.ParticipantName);
                    answer = session.Robot.Query(msg.Content.Trim(), msg.ParticipantName, msg.ParticipantUri);
                }
                else
                {
                    foreach (ReminderPage r in rList)
                    {
                        reminder.Add(r);
                    }
                    answer = "sure, I will remind at " + rList[0].reminderTime.ToString();
                }

                if (!string.IsNullOrEmpty(answer))
                {
                    response.Content = string.Format("<p style='font-size:20;font-family=Corbel;font-weight:bold;color=#007500;'>To [{0}]: {1}</p>", msg.ParticipantName, answer);
                }
                else
                {
                    try
                    {
                        response.Content = Search(msg.Content);
                    }
                    catch (Exception ex)
                    {
                        logger.Write(TraceEventType.Warning, ex.ToString());
                        answer = string.Format("Sorry, I don't know any about '{0}', even Bing cannot tell me.", msg.Content);
                    }
                }
                shell.Write(response);
            }
        }

        private Session GetSession(string conversationID, string participantName)
        {
            var session = _sessions.SingleOrDefault(s =>
                s.ConversationID == conversationID && s.ParticipantName == participantName);

            if (session == null)
            {
                session = new Session
                {
                    ConversationID = conversationID,
                    ParticipantName = participantName,
                    Robot = new RobotK(),
                };
                _sessions.Add(session);
            }

            return session;
        }

        private string Search(string query)
        {
            var results = _bing.GetTopAnswers(query, 5);

            var sb = new StringBuilder();
            sb.AppendFormat("<div>");
            sb.AppendFormat("<p>Results from Bing.com:</p>");
            var n = 1;
            foreach(var r in results)
            {
                sb.AppendFormat("<p>[{0}] <a href=\"{1}\">{2}</a></p>", n++, r.Url, r.Title);
            }
            sb.AppendFormat("<p><a href=\"http://www.bing.com/search?q={0}\">View all results</a></p>", query);
            sb.AppendFormat("</div>");
            return sb.ToString();
        }

        private List<Session> _sessions = new List<Session>();
    }
}
