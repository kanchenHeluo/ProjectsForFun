using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore
{
    //deal with pre-set question and casual answer
    class RobotCasual
    {
        public string Query(List<string> queryList, string participantName)
        {
            string answer = null;

            if ((answer = QueryTime(queryList)) != null)
                return answer;

            if ((answer = QueryGreeting(queryList, participantName)) != null)
                return answer;

            return null;
        }

        private string QueryTime(List<string> queryList)
        {
            foreach (var q in queryList)
            {
                if (q.Contains("date") && q.Contains("today") && q.Contains("what"))
                    return "Today is " + DateTime.Now.ToLongDateString();

                if (q.Contains("time") && q.Contains("now") && q.Contains("what"))
                    return "Now is " + DateTime.Now.ToLongTimeString();
            }

            return null;
        }

        private string QueryGreeting(List<string> queryList, string participantName)
        {
            var firstName = participantName;
            var nameParts = participantName.Split(new[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
            if (nameParts.Length > 1)
                firstName = nameParts[0];

            foreach (var q in queryList)
            {
                if (q.Length >= 3 && q.Length < 16
                    && (q[0] == 'H' || q[0] == 'h')
                    && (q[1] == 'I' || q[1] == 'i')
                    && ",.!，。！、 ".Contains(q[2]))
                    return string.Format("Hi, {0}!", firstName);

                if (q.Length >= 6
                    && (q[0] == 'H' || q[0] == 'h')
                    && (q[1] == 'E' || q[1] == 'e')
                    && (q[2] == 'L' || q[2] == 'l')
                    && (q[3] == 'L' || q[3] == 'l')
                    && (q[4] == 'O' || q[4] == 'o')
                    && ",.!，。！、 ".Contains(q[5]))
                    return string.Format("Hello, {0}!", firstName);
            }

            return null;
        }
    }
}
