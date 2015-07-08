using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore
{
    public class RewriteQuestion
    {
        private List<string> stopSymbols = new List<string>();
        private Dictionary<string, List<string>> standardWords = new Dictionary<string, List<string>>();
        private Dictionary<string, string> synStandWordDict = new Dictionary<string, string>();
        public RewriteQuestion()
        {
            this.init();
        }

        private void init()
        {
            //read stop symbols
            StreamReader sr = new StreamReader(FilePathConfig.StopSymbolsPath, Encoding.UTF8);
            if (sr == null)
                return;
            char[] swSep = new char[] { '\n', '\t' };
            string sLine = "";
            sLine = sr.ReadLine();
            while (sLine != null)
            {
                sLine = sLine.ToLower();
                string[] tmp = sLine.Split(swSep, StringSplitOptions.RemoveEmptyEntries);
                stopSymbols.AddRange(tmp);

                sLine = sr.ReadLine();
            }
            sr.Close();

            // read standard rewrite words
            sr = new StreamReader(FilePathConfig.RewriteStandardWords, Encoding.UTF8);
            if (sr == null)
                return;
            char[] rwSep = new char[] { '\t', ',', '|' };
            sLine = "";
            sLine = sr.ReadLine();

            while (sLine != null)
            {
                sLine = sLine.ToLower();
                string[] sList = sLine.Trim().Split(rwSep, StringSplitOptions.RemoveEmptyEntries);
                if (sList == null || sList.Length <= 1)
                    continue;

                string firstWord = sList[0].Trim();
                foreach (string s in sList)
                {
                    if (!synStandWordDict.ContainsKey(s.Trim()))
                        synStandWordDict.Add(s.Trim(), firstWord);
                }

                sLine = sr.ReadLine();
            }
            sr.Close();
        }

        private string replace(string query, bool isStandard)
        {
            if (isStandard) {
                query = query.ToLower();
                Dictionary<string, bool> noted = new Dictionary<string, bool>();
                foreach (string s in synStandWordDict.Keys)
                {
                    noted.Add(s, false);
                }
                foreach (string s in synStandWordDict.Keys)
                {
                    int startindex = query.IndexOf(s);
                    while (!noted[s] && startindex != -1)
                    {
                        if ((startindex == 0 || query[startindex - 1] == ' ') && (startindex == query.Length - s.Length || query[startindex + s.Length] == ' ')) // hit the word/period
                        {
                            StringBuilder sb = new StringBuilder(query);
                            sb.Replace(s, synStandWordDict[s], startindex, s.Length);
                            query = sb.ToString();
                            
                        }
                        startindex = query.IndexOf(s, startindex+1);
                    }
                    noted[s] = true;
                    
                }
                //Console.WriteLine("rewrite query:"+query);
                return query;
            }
            else
            {
                // casual replace
            }
            return query;
        }

        // for every query come in
        // for keep & train data query-ans come in 
        public string RewriteAsStandard(string question)
        {
            // remove stopwords
            question = question.ToLower().Trim();
            for (int i = 0; i < stopSymbols.Count; i++)
            {
                int startIndex = question.IndexOf(stopSymbols[i]);
                while (startIndex != -1)
                {
                    if (question.Length>startIndex+3 && string.Equals(stopSymbols[i], ".") && question.Substring(startIndex, 4)==".com" )
                    {
                        ;
                    }
                    else
                    {
                        question = question.Remove(startIndex, stopSymbols[i].Length);
                    }

                    if (startIndex >= question.Length)
                    {
                        break;
                    }
                    startIndex = question.IndexOf(stopSymbols[i], startIndex+1);
                }
            }

            return question;
        }

        private List<string> RewriteAsCasual(string question)
        {
            question = replace(question, false);
            return null;
        }

        //for every standard query come in
        public List<string> Rewrite(string question, string uri)
        {
            List<string> questionList = new List<string>();
            question = this.RewriteAsStandard(question);

            // add standard original question
            questionList.Add(question);

            // replace similar words to standard one
            string reQuestion = replace(question, true);
            if (!reQuestion.Equals(question, StringComparison.OrdinalIgnoreCase))
            {
                questionList.Add(reQuestion);
            }
            

            //if contains i, replace with alias
            reQuestion = string.Empty;
            string alias = "kanchen";
            if(uri != null)
                alias = uri.Replace("@microsoft.com","").Trim();
            question = question.ToLower();
            char[] sep = new char[] { ' ' };
            string[] s = question.Split(sep);

            for (int i = 0; i < s.Length; i++)
            {
                if (s[i].Equals("i") || s[i].Equals("me") || s[i].Equals("my"))
                {
                    s[i] = alias;
                }
                if (s[i].Equals("am"))
                {
                    s[i] = "is";
                }
                reQuestion += s[i] + " ";
            }
            reQuestion = reQuestion.TrimEnd();
            if (!reQuestion.Equals(question, StringComparison.OrdinalIgnoreCase))
            {
                questionList.Add(reQuestion);
            }

            // add differenct casual expression
            /*
            List<string> casualQueryList = RewriteAsCasual(question);
            if (casualQueryList != null && casualQueryList.Count > 0)
                questionList.AddRange(casualQueryList);
            */
            return questionList;
        }
    }
}
