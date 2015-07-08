using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore
{
    public class WordSeg
    {
        public static List<string> wordSeg(string tmp)
        {
            // replace with jieba seg
            char[] sep = new char[] { ' ' };
            List<string> words = tmp.Split(sep, StringSplitOptions.RemoveEmptyEntries).ToList<string>();
            return words;
        }
    }
}
