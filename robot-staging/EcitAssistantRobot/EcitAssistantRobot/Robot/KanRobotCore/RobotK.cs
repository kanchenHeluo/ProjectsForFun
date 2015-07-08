using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore
{
    public class RobotK
    {
        private RobotMatch rm1;
        private RobotMatch rm2;
        private RobotLogic rl;
        private RobotCasual rc;
        private RewriteQuestion rq;

        private Dictionary<string, string> _uriFormerQuery;
        public RobotK()
        {
            rm1 = new RobotMatch(false);
            rm2 = new RobotMatch(true);
            rl = new RobotLogic();
            rc = new RobotCasual();
            rq = new RewriteQuestion();

            _uriFormerQuery = new Dictionary<string, string>();

        }

        public void learn(string question, string answer)
        {
            question = question.ToLower();
            //answer = answer.ToLower();

            lock (FilePathConfig.NewQaPath)
            {
                StreamWriter sw = new StreamWriter(FilePathConfig.NewQaPath, true, Encoding.UTF8);
                sw.WriteLine(question + "\t" + answer);
                sw.Close();
            }
            //update data for match robot
            rm1 = new RobotMatch(false);
            rm2 = new RobotMatch(true);
        }

        public string Query(string query, string participantName, string uri)
        {
            var sw = new StreamWriter(FilePathConfig.LogPath, true, Encoding.UTF8);
            if ((query[0] == 'N' || query[0] == 'n')
                && (query[1] == 'O' || query[1] == 'o')
                && ",.，。、".Contains(query[2]))
            {
                string ans = query.Remove(0, 3).Trim();
                if (_uriFormerQuery != null && _uriFormerQuery.Count > 0)
                {
                    if (_uriFormerQuery.ContainsKey(uri))
                    {
                        learn(_uriFormerQuery[uri], ans);

                        if (sw != null)
                        {
                            lock (FilePathConfig.LogPath)
                            {
                                sw.WriteLine("learn_query_:" + _uriFormerQuery[uri]);
                                sw.WriteLine("learn_answer:" + ans);
                                sw.WriteLine();
                            }
                        }
                        sw.Close();
                        return string.Format("Thank you, I get that about '{0}', should be '{1}'.", _uriFormerQuery[uri], ans);
                    }
                }
                return "Okay, but I don't remember what's wrong, please ask me again?~";
            }
            else
            {
                if (_uriFormerQuery != null && _uriFormerQuery.Count > 0)
                {
                    if (!_uriFormerQuery.ContainsKey(uri))
                    {
                        _uriFormerQuery.Add(uri, "");
                    }
                    _uriFormerQuery[uri] = query;
                }
                else
                {
                    uri = "kanchen@microsoft.com";
                }
               
                // rewrite and get query list
                List<string> queryList = rq.Rewrite(query, uri);

                string ans = string.Empty;
                //call pre robot
                ans = rc.Query(queryList, participantName);

                if (string.IsNullOrEmpty(ans))
                {
                    // call rm1
                    ans = rm1.QueryFirst(queryList);
                }
                // call rl
                if (string.IsNullOrEmpty(ans))
                {
                    ans = rl.Query(queryList);
                }
                // call rm2
                if (string.IsNullOrEmpty(ans))
                {
                    ans = rm2.QuerySecond(queryList);
                }

                if (sw != null)
                {
                    lock (FilePathConfig.LogPath)
                    {
                        sw.WriteLine("query_:" + query);
                        sw.WriteLine("answer:" + ans);
                        sw.WriteLine();
                    }
                }
                sw.Close();

                return ans;
            }
        }
    }
}
