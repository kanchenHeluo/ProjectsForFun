using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore
{
    public class RobotMatch
    {
        public class MatchQueryVal
        {
            public double MatchedWeight; // matched part for [qid, query/rewerite query]'s weight
            public double MatchedQueryTotalWeight; // matched qid's weight
            public double OriginalQueryTotalWeight; //query or rewrite query's weight

            public double Weight
            {
                get
                {
                    OriginalQueryTotalWeight = OriginalQueryTotalWeight < MatchedWeight ? MatchedWeight : OriginalQueryTotalWeight;
                    return (2.0 * MatchedWeight) / (MatchedQueryTotalWeight + OriginalQueryTotalWeight);
                }
            }

            public MatchQueryVal() { }

            public MatchQueryVal(double matchedWeight, double matchedQueryTotalWeight, double originalQueryTotalWeight)
            {
                this.MatchedWeight = matchedWeight;
                this.MatchedQueryTotalWeight = matchedQueryTotalWeight;
                this.OriginalQueryTotalWeight = originalQueryTotalWeight;
            }
        }

        public List<string[]> qaList; //{[question,answer], [], []}
        public Dictionary<string, List<int>> wordQusIndex; // dic[word] = questionId
        public List<double> qusTotalWeight; //[query, weight]
        public List<string> essentialWords; // [words]
        private RewriteQuestion rwq; 

        Dictionary<string, string> nameAlias = new Dictionary<string, string>();

        private string _formerQuery;

        private const double EXACTMATCH = 0.96; 

        public RobotMatch(bool isTwo)
        {
            qaList = new List<string[]>();
            wordQusIndex = new Dictionary<string, List<int>>();
            qusTotalWeight = new List<double>();
            essentialWords = new List<string>();
            rwq = new RewriteQuestion();

            this.Init(isTwo);
        }

        public string QueryFirst(List<string> queryList)
        {
            for (int i = 0; i < queryList.Count; i++) // priority first one
            {
                string ans = string.Empty;
                ans = query(queryList[i], EXACTMATCH);
                if(!string.IsNullOrEmpty(ans))
                    return ans;
            }
            return string.Empty;
        }

        public string QuerySecond(List<string> queryList)
        {
            for (int i = 0; i < queryList.Count; i++) // priority first one
            {
                string ans = string.Empty;
                ans = query(queryList[i], 0.65);
                if (!string.IsNullOrEmpty(ans))
                    return ans;
            }
            return string.Empty;
        }

        private void loadQA(string filename){
            StreamReader sr = new StreamReader(filename, Encoding.UTF8);
            if (sr == null)
                return;

            string sLine = "";
            char[] qaSep = new char[] { '\t' };

            sLine = sr.ReadLine();
            List<string> sort_name_list = nameAlias.Keys.ToList();
            sort_name_list.Sort((y, x) => { return x.Length.CompareTo(y.Length); });

            while (sLine != null)
            {
                //sLine = sLine.ToLower();
                string[] q = sLine.Split(qaSep, StringSplitOptions.RemoveEmptyEntries);
                if (q.Length == 2)
                {
                    //rewrite
                    q[0] = rwq.RewriteAsStandard(q[0].ToLower());

                    foreach (string name in sort_name_list)
                    {
                        if (name.Length <= 2)
                        {
                            break;
                        }
                        int index = q[0].IndexOf(name);
                        if (index != -1 
                            && (index == 0 || q[0][index - 1] == ' ') 
                            && (index + name.Length == q[0].Length || q[0][index + name.Length] == ' '))
                        {
                            q[0] = q[0].Replace(name, nameAlias[name]);
                        }
                    }
                    qaList.Add(q);
                }
                sLine = sr.ReadLine();
            }
            sr.Close();
        }

        private void Init(bool isTwo)
        {
            //name List
            Dictionary<string, List<string>> aliasName = new Dictionary<string, List<string>>();
            StreamReader sr = new StreamReader(FilePathConfig.dataPath[0], Encoding.UTF8);
            if (sr == null)
                return;
            char[] dpSep = new char[] { '\n', '\t' };
            string sLine = "";
            sLine = sr.ReadLine();
            while (sLine != null)
            {
                sLine = sLine.ToLower().Trim();
                string[] tmp = sLine.Split(dpSep, StringSplitOptions.RemoveEmptyEntries);
                if (tmp.Length == 3)
                {
                    if (tmp[1].Equals("givenName", StringComparison.CurrentCultureIgnoreCase) || tmp[1].Equals("displayName", StringComparison.CurrentCultureIgnoreCase) || tmp[1].Equals("sAMAccountName", StringComparison.CurrentCultureIgnoreCase))
                    {
                        if (!aliasName.ContainsKey(tmp[0].ToLower()))
                        {
                            aliasName.Add(tmp[0].ToLower(), new List<string>());
                        }
                        aliasName[tmp[0].ToLower()].Add(tmp[2].ToLower());

                        if (!nameAlias.ContainsKey(tmp[2]))
                        {
                            nameAlias.Add(tmp[2], tmp[0]);
                        }
                    }
                }
                sLine = sr.ReadLine();
            }
            sr.Close();


            loadQA(FilePathConfig.QaPath);
            loadQA(FilePathConfig.NewQaPath);
            if (isTwo)
            {
               loadQA(FilePathConfig.GenerateQaPath);
            }
            /*
            sr = new StreamReader(FilePathConfig.NewQaPath, Encoding.Default);
            if (sr == null)
                return;
            char[] qaSep = new char[] { '\t' };
            sLine = "";
            sLine = sr.ReadLine();
            while (sLine != null)
            {
                sLine = sLine.ToLower();
                string[] q = sLine.Split(qaSep, StringSplitOptions.RemoveEmptyEntries);
                if (q.Length == 2)
                {
                    q[0] = rwq.RewriteAsStandard(q[0]);
                    qaList.Add(q);
                }
                sLine = sr.ReadLine();
            }
            sr.Close();*/

            // question - word tracking: qusTotalWeight[], wordQusIndex[];
            // both index refer to 'qid'
            int qid = 0;
            foreach (string[] tmp in qaList)
            {
                double total_weight = 0;
                foreach (string word in WordSeg.wordSeg(tmp[0]))
                {
                    if (!wordQusIndex.ContainsKey(word))
                    {
                        wordQusIndex.Add(word, new List<int>());
                    }
                    wordQusIndex[word].Add(qid);

                    total_weight += getTermWeight(word);
                }
                qusTotalWeight.Add(total_weight);
                qid++;
            }
            // essential words
            /*
            sr = new StreamReader(FilePathConfig.EssentialWordPath, Encoding.Default);
            if (sr == null)
                return;
            char[] ewSep = new char[] { '\n', '\t' };
            sLine = "";
            sLine = sr.ReadLine();
            while (sLine != null)
            {
                sLine = sLine.ToLower().Trim();
                string[] tmp = sLine.Split(ewSep, StringSplitOptions.RemoveEmptyEntries);
                essentialWords.AddRange(tmp);
                sLine = sr.ReadLine();
            }
            sr.Close();*/



        }

        private double getTermWeight(string word)
        {
            return word.Length;
        }

        public string query(string q, double threshold)
        {
            _formerQuery = q;

            // go normal robot
            // original query
            string ans = string.Empty;
            Dictionary<int, MatchQueryVal> matchQuery = new Dictionary<int, MatchQueryVal>();

            if (q != null)
                q = q.ToLower();

            //sort matchQuery, find max Weight && suit essential words
            Dictionary<string, int> queryEssentialWord = new Dictionary<string, int>();
            /*
            foreach (string ew in essentialWords) 
            {
                if (q.IndexOf(ew) != -1)
                {
                    queryEssentialWord.Add(ew, 1);
                }
            }*/

            List<string> sort_name_list = nameAlias.Keys.ToList();
            sort_name_list.Sort((y, x) => { return x.Length.CompareTo(y.Length); });
            foreach (string name in sort_name_list)
            {
                if (name.Length <= 2)
                {
                    break;
                }
                int index = q.IndexOf(name);
                if (index != -1 
                    && (index == 0 || q[index-1]==' ') 
                    && (index+name.Length==q.Length || q[index+name.Length]==' '))
                {
                    q = q.Replace(name, nameAlias[name]);
                    if (queryEssentialWord.ContainsKey(nameAlias[name]))
                    {
                        continue;
                    }
                    queryEssentialWord.Add(nameAlias[name], 1);
                }
            }

            // search.
            singleQuery(q, ref matchQuery);

            List<string> ansList = new List<string>();
            List<KeyValuePair<int, MatchQueryVal>> matchQidWeight = matchQuery.OrderByDescending(c => c.Value.Weight).ToList();
            for (int i = 0; i < matchQidWeight.Count; i++)
            {
                int qid = matchQidWeight[i].Key;
                if (matchQidWeight[i].Value.Weight < threshold)
                {
                    break;
                }
                //check if the query suit the essential word
                string query = qaList[qid][0];
                bool flag = true;
                foreach (string ew in queryEssentialWord.Keys)
                {
                    if (query.IndexOf(ew) == -1)
                    {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                {
                    if(ansList.Count < 4)
                        ansList.Add(qaList[qid][1]);
                    //return qaList[qid][1]; //TODO: choose one answer
                }
                
            }

            if(ansList.Count > 0){
                string answer = string.Empty;
                Random r = new Random();
                int num = r.Next(1,100);
                if (num > 45)
                {
                    answer = ansList[0];
                }
                else if (num > 20)
                {
                    if (ansList.Count < 2)
                        answer = ansList[0];
                    else
                        answer = ansList[1];
                }
                else 
                {
                    if (ansList.Count < 3)
                        answer = ansList[0];
                    else
                        answer = ansList[1];
                }
                
                return answer;
            }

            return null;
        }

        public void singleQuery(string q, ref Dictionary<int, MatchQueryVal> matchQuery)
        {
            List<int> relatedQueryId = new List<int>();
            Dictionary<int, double> ansDict = new Dictionary<int, double>(); // for query: q, get the matched [queryid,weight]

            //find matched query|matched weight: [qid, weight]
            double totalWeight = 0;
            List<string> wList = WordSeg.wordSeg(q);
            foreach (string w in wList)
            {
                double tw = getTermWeight(w);
                totalWeight += tw;
                // TODO: deal with w 的 单复数，去掉时态
                List<string> wordsList = new List<string>();
                wordsList.Add(w);
                if (w.EndsWith("s") && w != "is")
                {
                    wordsList.Add(w.Remove(w.Length - 1, 1));
                }
                if (w.EndsWith("shes") || w.EndsWith("ches"))
                {
                    wordsList.Add(w.Remove(w.Length - 2, 2));
                }
                if (w.EndsWith("ing") && w != "thing")
                {
                    wordsList.Add(w.Remove(w.Length - 3, 3));
                    wordsList.Add(w.Remove(w.Length - 3, 3) + 'e');
                }
                if (w.EndsWith("ed"))
                {
                    wordsList.Add(w.Remove(w.Length - 1, 1));
                    wordsList.Add(w.Remove(w.Length - 2, 2));
                }

                foreach (string word in wordsList) //hit one
                {
                    if (wordQusIndex.ContainsKey(word))
                    {
                        relatedQueryId = wordQusIndex[word]; // TODO: if uncovered symbol, will throw exception (#,$)
                        foreach (int qid in relatedQueryId)
                        {
                            if (!ansDict.ContainsKey(qid))
                            {
                                ansDict.Add(qid, 0);
                            }
                            ansDict[qid] += tw;
                        }
                        break;
                    }
                }
            }

            //update matchQueryWeight -- different query matched the same qid
            foreach (int qid in ansDict.Keys)
            {
                if (!matchQuery.ContainsKey(qid))
                    matchQuery.Add(qid, new MatchQueryVal(ansDict[qid], qusTotalWeight[qid], totalWeight));
                else
                {
                    if (matchQuery[qid].MatchedWeight < ansDict[qid])
                    {
                        matchQuery[qid].MatchedWeight = ansDict[qid];
                        matchQuery[qid].OriginalQueryTotalWeight = totalWeight;
                    }
                }
            }
        }

    }
}
