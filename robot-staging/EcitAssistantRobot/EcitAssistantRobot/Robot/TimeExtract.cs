using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;
using System.IO;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot
{
    class TimeExtract
    {
        private Dictionary<string, string> aliasUri;
        private Dictionary<string, string> aliasName;
        private Dictionary<string, string> uriAlias;

        public TimeExtract()
        {
            aliasUri = new Dictionary<string, string>();
            aliasName = new Dictionary<string, string>();
            uriAlias = new Dictionary<string, string>();
            StreamReader sr = new StreamReader(FilePathConfig.dataPath[0], Encoding.UTF8);
            if (sr == null)
                return;

            string sLine = "";
            char[] sep = new char[] { '\t' };
            sLine = sr.ReadLine();

            while (sLine != null)
            {
                sLine = sLine.ToLower();
                string[] periods = sLine.Split(sep);
                if (periods.Length == 3)
                {
                    string S = periods[0].Trim();
                    string P = periods[1].Trim();
                    string O = periods[2].Trim();

                    if (!aliasName.ContainsKey(S) && string.Equals(P, "displayName", StringComparison.CurrentCultureIgnoreCase))
                    {
                        aliasName.Add(S, O);
                    }
                    if (!uriAlias.ContainsKey(O) && !aliasUri.ContainsKey(S) && string.Equals(P, "mail", StringComparison.CurrentCultureIgnoreCase))
                    {
                        aliasUri.Add(S, O);
                        uriAlias.Add(O, S);
                    } 
                }
                sLine = sr.ReadLine();
            }
            sr.Close();

        }

        public List<ReminderPage> isReminder(string content, string uri)
        {
            List<ReminderPage> rpList = new List<ReminderPage>();
            ReminderPage rp = isRemindMe(content, uri);
            if (rp.reminderTime == DateTime.MinValue)
            {
                rpList = isRemindOthers(content, uri);
            }
            else
            {
                rpList.Add(rp);
            }
            return rpList;
        }

        private List<ReminderPage> isRemindOthers(string content, string uri){
            content = content.ToLower();

            List<ReminderPage> rpList = new List<ReminderPage>();
            
            HashSet<string> aliasList = new HashSet<string>();

            string information = string.Copy(content);
            DateTime reminderTime = new DateTime();
            if(content.IndexOf("remind") != -1 ){ // if contains "remind","alias",time pattern
                int index = content.IndexOf("remind");
                information = information.Remove(0,index+6).Trim();


                foreach(string alias in aliasUri.Keys){
                    if (content.IndexOf(alias) != -1)
                    {
                        aliasList.Add(alias);
                        information = information.Replace(alias, "").Trim();
                    }
                }

                if (aliasList.Count > 0)
                {
                    reminderTime = timePatternMatch(content, ref information);
                    if (reminderTime == DateTime.MinValue)
                        reminderTime = System.DateTime.Now;
                }
                //remove the head and tail , and ' '
                information = information.Trim(new char[] { ',', ' ',':' ,'：'});
                while (information.StartsWith("and") || information.StartsWith("after"))
                {
                    if( information.StartsWith("and"))
                        information = information.Substring(3).Trim();
                    else
                        information = information.Substring(5).Trim();
                }
                while (information.EndsWith("and") || information.EndsWith("after"))
                {
                    if (information.EndsWith("and"))
                        information = information.Remove(information.Length - 3, 3).Trim();
                    else
                        information = information.Remove(information.Length - 5, 5).Trim();
                }

                foreach (string alias in aliasList)
                {
                    ReminderPage rp = new ReminderPage();
                    rp.reminderTime = reminderTime;
                    rp.sender = aliasUri[alias];
                    rp.info = information;
                    if(uriAlias.ContainsKey(uri))
                        rp.fromWho = aliasName[uriAlias[uri]];
                    rpList.Add(rp);
                }
            }
            return rpList;
        }


        private DateTime timePatternMatch(string content, ref string information){
            string matchedItem = string.Empty;

            string timePattern = @"(at|after|in|on)(?<time>.*)";
            DateTime dt = new DateTime();
            dt = DateTime.MinValue;

            Regex reContent = new Regex(timePattern);
            var contentGroups = reContent.Match(content);
            if (contentGroups.Length > 2)
            {
                string timePattern1 = @"(?<hour>\d{1,2})(:|：)(?<minute>\d{1,2})((:|：)(?<second>\d{1,2}))?";
                
                Regex re = new Regex(timePattern1);
                var groups = re.Match(contentGroups.Groups["time"].Value);

                if (groups.Length < 2) // after
                {
                    string DayPattern = @"(?<day>\d+)[ ]*(day|days)";
                    string HourPattern = @"(?<hour>\d+)[ ]*(hour|hours)";
                    string MinPattern = @"(?<min>\d+)[ ]*(min|minute|minutes)";
                    string SecPattern = @"(?<sec>\d+)[ ]*(sec|second|seconds)";
                    
                    re = new Regex(DayPattern);
                    var day_groups = re.Match(contentGroups.Groups["time"].Value);
                    re = new Regex(HourPattern);
                    var hour_groups = re.Match(contentGroups.Groups["time"].Value);
                    re = new Regex(MinPattern);
                    var min_groups = re.Match(contentGroups.Groups["time"].Value);
                    re = new Regex(SecPattern);
                    var sec_groups = re.Match(contentGroups.Groups["time"].Value);

                    if (day_groups.Length>0 || hour_groups.Length>0 || min_groups.Length>0 || sec_groups.Length>0)
                    {
                        string day = day_groups.Groups["day"].Value;
                        string dayInfo = day_groups.ToString();
                        if(dayInfo.Length>0)
                            information = information.Replace(dayInfo, "");

                        string hour = hour_groups.Groups["hour"].Value;
                        string hourInfo = hour_groups.ToString();
                        if(hourInfo.Length > 0)
                            information = information.Replace(hourInfo, "");

                        string min = min_groups.Groups["min"].Value;
                        string minInfo = min_groups.ToString();
                        if(minInfo.Length > 0)
                            information = information.Replace(minInfo, "");

                        string sec = sec_groups.Groups["sec"].Value;
                        string secInfo = sec_groups.ToString();
                        if(secInfo.Length > 0)
                            information = information.Replace(secInfo, "");

                        dt = System.DateTime.Now;
                        dt = dt.AddDays(day == "" ? 0 : Convert.ToInt32(day));
                        dt = dt.AddHours(hour == "" ? 0 : Convert.ToInt32(hour));
                        dt = dt.AddMinutes(min == "" ? 0 : Convert.ToInt32(min));
                        dt = dt.AddSeconds(sec == "" ? 0 : Convert.ToInt32(sec));
                    }
                }
                else //at , in, on
                {
                    string hour = groups.Groups["hour"].Value;
                    string min = groups.Groups["minute"].Value;
                    string sec = groups.Groups["second"].Value;
                    dt = new DateTime(System.DateTime.Now.Year, System.DateTime.Now.Month, System.DateTime.Now.Day,
                                                    Convert.ToInt32(hour), Convert.ToInt32(min), sec==""?0:Convert.ToInt32(sec));
                }
            }
            return dt;
        }


        private ReminderPage isRemindMe(string content, string uri)
        {
            ReminderPage rp = new ReminderPage();
            rp.reminderTime = DateTime.MinValue;
            // remind me XXXX at XXX | remind me at XXX to XXXX
            // reminde me XXX after XXX 
            content = content.ToLower();
            string contentPattern = @"remind me(?<info>.*) (at|after|in|on)(?<time>.*)";
            Regex reContent = new Regex(contentPattern);
            var contentGroups = reContent.Match(content);
            if (contentGroups.Length > 2)
            {
                string info = contentGroups.Groups["info"].Value;
                rp.info = info;
            
                string timePattern1 = @"(?<hour>\d{1,2})(:|：)(?<minute>\d{1,2})((:|：)(?<second>\d{1,2}))?";
                
                Regex re = new Regex(timePattern1);
                var groups = re.Match(contentGroups.Groups["time"].Value);
                if (groups.Length < 2) // after
                {
                    string DayPattern = @"(?<day>\d+)[ ]*(day|days)";
                    string HourPattern = @"(?<hour>\d+)[ ]*(hour|hours)";
                    string MinPattern = @"(?<min>\d+)[ ]*(min|minute|minutes)";
                    string SecPattern = @"(?<sec>\d+)[ ]*(sec|second|seconds)";
                    
                    re = new Regex(DayPattern);
                    var day_groups = re.Match(contentGroups.Groups["time"].Value);
                    re = new Regex(HourPattern);
                    var hour_groups = re.Match(contentGroups.Groups["time"].Value);
                    re = new Regex(MinPattern);
                    var min_groups = re.Match(contentGroups.Groups["time"].Value);
                    re = new Regex(SecPattern);
                    var sec_groups = re.Match(contentGroups.Groups["time"].Value);

                    if (day_groups.Length>0 || hour_groups.Length>0 || min_groups.Length>0 || sec_groups.Length>0)
                    {
                        string day = day_groups.Groups["day"].Value;
                        string hour = hour_groups.Groups["hour"].Value;
                        string min = min_groups.Groups["min"].Value;
                        string sec = sec_groups.Groups["sec"].Value;
                        rp.reminderTime = System.DateTime.Now;
                        rp.reminderTime = rp.reminderTime.AddDays(day == "" ? 0 : Convert.ToInt32(day));
                        rp.reminderTime = rp.reminderTime.AddHours(hour == "" ? 0 : Convert.ToInt32(hour));
                        rp.reminderTime = rp.reminderTime.AddMinutes(min == "" ? 0 : Convert.ToInt32(min));
                        rp.reminderTime = rp.reminderTime.AddSeconds(sec == "" ? 0 : Convert.ToInt32(sec));
                    }
                }
                else //at , in, on
                {
                    string hour = groups.Groups["hour"].Value;
                    string min = groups.Groups["minute"].Value;
                    string sec = groups.Groups["second"].Value;
                    rp.reminderTime = new DateTime(System.DateTime.Now.Year, System.DateTime.Now.Month, System.DateTime.Now.Day,
                                                    Convert.ToInt32(hour), Convert.ToInt32(min), sec==""?0:Convert.ToInt32(sec));
                }
            }

            rp.fromWho = "self";
            rp.sender = uri;
            return rp;
        }
    }
}
