using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore
{
    public class RobotLogic
    {
        private bool DEBUG = false;

        public class MatchClass
        {
            public List<object> wlist;
            public List<string> tmp;

            public MatchClass() { }
            public MatchClass(List<string> t, List<object> wl)
            {
                tmp = t;
                wlist = wl;
            }

            public MatchClass(MatchClass mc)
            {
                this.tmp = mc.tmp;
                this.wlist = mc.wlist;
            }
        }

        public class SPO{
            public string alias;
            public string attribute;
            public string value;

            public SPO(){
            }

            public SPO(string alias, string attribute, string value){
                this.alias = alias;
                this.attribute = attribute;
                this.value = value;
            }

            public string key()
            {
                string r = "";
                r = alias + "\t" + attribute + "\t" + value;
                return r;
            }
        }

        public class Template
        {
            public List<string> template;
            public bool flag;

            public Template() { }
            public Template(string[] sl, bool f)
            {
                template = sl.ToList<string>();
                flag = f;
            }
        }

        //public List<NameFieldValue> edgeList;

        public Dictionary<string, List<string>> logicReplace;
        public List<Template> logicTemplate;


        public List<string> andTerm;
        public List<string> orTerm;
        public List<string> startTerm;

        public Dictionary<string, List<string>> tempDp;


        public List<SPO> spoList;
        public HashSet<string> allNameSet; // nameSet
        public HashSet<string> allValueSet; // valueSet
        public HashSet<string> allFieldSet; // fieldSet  = propNameSet
        public HashSet<string> allClassSet; // class name
        public Dictionary<string, HashSet<string>> allClassValue; // classInfo: [class] = valueList(people, project, teams )

        public Dictionary<string, string> aliasName;
        public Dictionary<string, HashSet<string>> nameAlias; // syncNameDict

        public Dictionary<string, string[]> objectClassDict; // propertyName -> class
        public Dictionary<string, string> wordPrototypeDict; // word->word prototype

        public RobotLogic()
        {
            andTerm = new List<string>() { "and", "is", "was", "were", "are", "do", "did", "does", "both", "of", "with" }; //has, have?
            orTerm = new List<string>() { "or" };
            

            logicReplace = new Dictionary<string, List<string>>();
            logicTemplate = new List<Template>();
            tempDp = new Dictionary<string, List<string>>();

            spoList = new List<SPO>();
            allNameSet = new HashSet<string>();
            allValueSet = new HashSet<string>();
            allFieldSet = new HashSet<string>();
            allClassSet = new HashSet<string>();
            allClassValue = new Dictionary<string, HashSet<string>>();
            aliasName = new Dictionary<string, string>();
            nameAlias = new Dictionary<string, HashSet<string>>();
            wordPrototypeDict = new Dictionary<string, string>();

            objectClassDict = new Dictionary<string, string[]>();
            objectClassDict["sAMAccountName".ToLower()] = new string[] {"people", "persons"};
            objectClassDict["ProjectsWorkedOn".ToLower()] = new string[] { "project", "projects" };
            objectClassDict["ProjectsWorkingOn".ToLower()] = new string[] { "project", "projects" };

            this.init();
        }

        public string Query(List<string> queryList)
        {

            string ans = string.Empty;

            foreach (string q in queryList)
            {
                //replace word with word-prototype
                char[] sep = new char[] { ' ' };
                string[] wlist = q.Split(sep, StringSplitOptions.RemoveEmptyEntries);
                string newQuery = string.Empty;
                for (int i = 0; i < wlist.Length; i++ )
                {
                    string w = string.Copy(wlist[i]);
                    if (wordPrototypeDict.ContainsKey(wlist[i]))
                        w = wordPrototypeDict[wlist[i]];
                    newQuery += w + " ";
                }
                newQuery.TrimEnd();

                ans = query(newQuery);
                if (ans != null)
                    return ans;
            }
            return null;
        }

        private void init()
        {
            // read from logic.txt, get [replaceInfo dict]
            StreamReader sr = new StreamReader(FilePathConfig.LogicPath, Encoding.UTF8);
            if (sr == null)
                return;
            string sLine = "";
            char[] sep = new char[] { '=' };
            sLine = sr.ReadLine();

            while (sLine != null)
            {
                sLine = sLine.ToLower();
                string[] wordArr = sLine.Split(sep);
                if (wordArr.Length > 1)
                {
                    if (!logicReplace.ContainsKey(wordArr[0]))
                    {
                        logicReplace.Add(wordArr[0], new List<string>());
                    }
                    logicReplace[wordArr[0]].Add(wordArr[1]);
                }
                sLine = sr.ReadLine();
            }
            sr.Close();

            //read logic parser template
            sr = new StreamReader(FilePathConfig.LogicParsePath, Encoding.UTF8);
            if (sr == null)
                return;
            sLine = sr.ReadLine();

            sep = new char[] { ' ' };
            while (sLine != null)
            {
                //sLine = sLine.ToLower();
                bool f = false;
                if (sLine.IndexOf(":2J") != -1)
                {
                    sLine = sLine.Replace(":2J", "");
                    f = true;
                }
                logicTemplate.Add(new Template(sLine.Split(sep), f));
                sLine = sr.ReadLine();
            }
            sr.Close();

            // load SPO file
            HashSet<string> dedup_set = new HashSet<string>();
            foreach (string fn in FilePathConfig.dataPath)
            {
                load(fn, ref dedup_set);
            }

            load_class();
            load_wordPrototype();

        }

        private void load_wordPrototype()
        {
            StreamReader sr = new StreamReader(FilePathConfig.prototypeWordPath, Encoding.UTF8);
            if (sr == null)
                return;
            char[] sep = new[] { '\t' };
            string sLine = sr.ReadLine();
            while (sLine != null)
            {
                sLine = sLine.ToLower();
                string[]s = sLine.Split(sep, StringSplitOptions.RemoveEmptyEntries);
                for (int i = 1; i < s.Length; i++)
                {
                    if(!wordPrototypeDict.ContainsKey(s[i]))
                        wordPrototypeDict.Add(s[i],s[0]);
                }
                sLine = sr.ReadLine();
            }
            sr.Close();
        }


        private void load_class()
        {
            // load class info.
            StreamReader sr = new StreamReader(FilePathConfig.classPath, Encoding.UTF8);
            if (sr == null)
                return;
            char[] sep = new[] { '=' };
            char[] wordSep = new[] { ',' };
            string sLine = sr.ReadLine();

            HashSet<string> extend_S_class = new HashSet<string>();
            while (sLine != null)
            {
                bool extend = false;
                if (sLine.IndexOf("S#") == 0)
                {
                    // need to find S of value in hash.
                    extend = true;
                }

                sLine = sLine.Replace("S#", "").ToLower();
                string[] s = sLine.Split(sep);
                s[0] = s[0].Trim();
                allClassSet.Add(s[0]);
                List<string> itemList = s[1].Split(wordSep).ToList<string>();
                if (!allClassValue.ContainsKey(s[0]))
                    allClassValue.Add(s[0], new HashSet<string>());
                if (extend) 
                {
                    extend_S_class.Add(s[0]);
                }

                foreach (string item in itemList)
                {
                    allClassValue[s[0]].Add(item.Trim());
                }
                sLine = sr.ReadLine();
            }
            sr.Close();

            foreach (string key in allClassValue.Keys)
            {
                List<string> value = allClassValue[key].ToList();
                foreach (string v in value)
                {
                    if (allClassValue.ContainsKey(v))
                    {
                        foreach (string originvalue in allClassValue[v])
                        {
                            allClassValue[key].Add(originvalue);
                        }
                        allClassValue[key].Remove(v);
                    }
                }
            }

            List<string> key_list = allClassValue.Keys.ToList();
            foreach (string key in key_list)
            {
                if (!extend_S_class.Contains(key)) {
                    continue;
                }
                HashSet<string> after_extend_set = new HashSet<string>();
                foreach (SPO spo in spoList)
                {
                    if (allClassValue[key].Contains(spo.value)) 
                    {
                        after_extend_set.Add(spo.alias);
                    }
                }
                allClassValue[key] = after_extend_set;
            }
        }

        private void load(string filename, ref HashSet<string> dedup_set)
        {
            // get nameSet; fieldSet; valueSet; classSet; spoList;
            StreamReader sr = new StreamReader(filename, Encoding.UTF8);
            if (sr == null)
                return;

            string sLine = "";
            char[] sep = new char[] { '\t' };
            sLine = sr.ReadLine();
            char[] comma = new char[] { ','};

            while(sLine != null){
                sLine = sLine.ToLower();
                string[] periods = sLine.Split(sep);
                if (periods.Length == 3)
                {
                    string S = periods[0].Trim();
                    string P = periods[1].Trim();
                    string orig_O = periods[2].Trim();

                    allFieldSet.Add(P);
                    string [] o_list = orig_O.Split(comma);
                    foreach (string oo in o_list)
                    {
                        string O = oo.Trim();
                        SPO edge = new SPO(S, P, O);
                        if ( dedup_set.Contains(edge.key()))
                        {
                            continue;
                        }
                        dedup_set.Add(edge.key());

                        spoList.Add(edge);
                        allValueSet.Add(O);
                        if (P == "physicalDeliveryOfficeName".ToLower())
                        {
                            char[] se = new char [] {'/'};
                            string [] s = O.Split(se);
                            if (s.Length == 2)
                            {
                                allValueSet.Add(s[1]);
                            }
                        }
                        allNameSet.Add(S);
                    
                        // add class.
                        if (objectClassDict.ContainsKey(P))
                        {
                            foreach (string c in objectClassDict[P])
                            {
                                if (!allClassSet.Contains(c))
                                {
                                    allClassSet.Add(c);
                                    allClassValue[c] = new HashSet<string>();
                                }
                                allClassValue[c].Add(O);
                            }
                        }
                    }

                    if (string.Equals(P, "cn", StringComparison.OrdinalIgnoreCase)
                        || string.Equals(P, "givenname", StringComparison.OrdinalIgnoreCase)
                        || string.Equals(P, "displayname", StringComparison.OrdinalIgnoreCase)
                        || string.Equals(P, "mailnickname", StringComparison.OrdinalIgnoreCase)
                        || string.Equals(P, "name", StringComparison.OrdinalIgnoreCase)
                        || string.Equals(P, "chinesename", StringComparison.OrdinalIgnoreCase))
                    {
                        string O = orig_O;
                        allNameSet.Add(O);
                        if (!nameAlias.ContainsKey(O))
                        {
                            nameAlias.Add(O, new HashSet<string>());
                        }
                        nameAlias[O].Add(S);

                        if (string.Equals(P, "displayname", StringComparison.OrdinalIgnoreCase))
                        {
                            aliasName[S] = O;
                        }

                        string fullname = string.Empty;
                        char[] blankSep = new char[] { ' ' };
                        string[] nsep = O.Split(blankSep, StringSplitOptions.RemoveEmptyEntries);
                        if (nsep.Length > 1)
                        {
                            foreach (string shortname in nsep)
                            {
                                fullname += shortname;
                            }
                            allValueSet.Add(fullname);
                            
                            if (!nameAlias.ContainsKey(fullname))
                                nameAlias.Add(fullname, new HashSet<string>());
                            nameAlias[fullname].Add(S); 
                        } 
                    } // name
                }
                sLine = sr.ReadLine();
            }


            // deal with spo.value -> align to alias
            foreach(SPO spo in spoList){
                if(nameAlias.ContainsKey(spo.value)){
                    if(nameAlias[spo.value].Count >= 1){
                        spo.value = nameAlias[spo.value].First();
                    }
                }
            }
            sr.Close();
        }

        public string query(string queryStr)
        {
            if (queryStr == null || queryStr.Length < 1)
                return null;
            List<string> wList = WordSeg.wordSeg(queryStr);

            //wipe the first words: do you know/ do you think
            bool f = false;
            List<string> wl = new List<string>();
            startTerm = new List<string>() { "who", "whose", "what", "whats", "what's", "which", "where", "list", "how", "where's" };
            for (int i = 0; i < wList.Count; i++)
            {
                if (startTerm.IndexOf(wList[i]) != -1)
                {
                    int j = i;
                    while (j < wList.Count)
                    {
                        wl.Add(wList[j++]);
                    }
                    f = true;
                    break;
                }
            }
            if (!f)
            {
                wl.AddRange(wList);
            }
            int end = wl.Count;

            // who and what : ans
            List<string> ans = new List<string>();
            List<string> stw = new List<string>() { "who", "who's", "whose", "what", "what's", "what're", "which", "where", "where's", "list" }; //start word
            List<string> secw = new List<string>() { "is", "are", "was", "were", "does", "do", "did", "have", "has" };//second word
            if (stw.IndexOf(wl[0]) != -1)
            {
                if (secw.IndexOf(wl[1]) != -1 || wl[0] == "what's" || wl[0] == "what're" || wl[0] == "who's" || wl[0] == "where's")
                { // wlist[1] in set([]
                    if (wl[0] == "what's" || wl[0] == "what're" || wl[0] == "who's" || wl[0] == "where's")
                        ans = process(wl.ToList<object>(), 1, end, 1);
                    else
                        ans = process(wl.ToList<object>(), 2, end, 1);
                }
                else
                {
                    ans = process(wl.ToList<object>(), 1, end, 1);
                }

                if (ans != null)
                {
                    if (ans.Count > 0)
                    {
                        string ret = "";

                        
                        string is_are = "are";
                        string it_them = "them";
                        if (ans.Count == 1)
                        {
                            is_are = "is";
                            it_them = "it";
                        }
                        if (wl[0] != "where")
                        {
                            ret += "I find " + ans.Count + " of " + it_them + ".";
                        }
                        ret += "That " + is_are;
                        foreach (string a in ans)
                        {
                            ret += " " + display(a) + ",";
                            
                            if ((wl[0] == "who's" || wl[0] == "who")&&(ans.Count < 3))
                            {
                                string department = string.Empty;
                                string title = string.Empty;
                                bool titleF = false;
                                bool departmentF = false;
                                foreach(SPO spo in spoList){
                                    if (string.Equals(spo.alias, a, StringComparison.OrdinalIgnoreCase))
                                    {
                                        if (!departmentF && string.Equals(spo.attribute, "department", StringComparison.OrdinalIgnoreCase))
                                        {
                                            department = spo.value;
                                            departmentF = true;
                                        }
                                        else if (!titleF && string.Equals(spo.attribute, "title", StringComparison.OrdinalIgnoreCase))
                                        {
                                            title = spo.value;
                                            titleF = true;
                                        }
                                    }
                                    if(titleF && departmentF)
                                        break;
                                }
                                ret += "who is";
                                if(titleF)
                                    ret += " a "+title;
                                if (departmentF)
                                    ret += " in " + department;
                                ret += ".";
                            }
                        }
                        return ret;
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                    return null;
            }
            if ((wl[0] == "when")||(wl[0] == "how" && wl[1] == "long"))
            {
                if (wl[0] == "when")
                {
                    ans = process(wl.ToList<object>(), 1,end,1 );
                }else
                    ans = process(wl.ToList<object>(), 2, end, 1);
                if (ans == null)
                    return null;
                if (ans.Count > 0 )
                {
                    int days = 0;
                    if (int.TryParse(ans[0], out days) == true)
                    {
                        int year = Convert.ToInt32(ans[0]) / 365;
                        int month = (Convert.ToInt32(ans[0]) % 365) / 30;
                        DateTime dt = DateTime.Now.AddDays(0 - Convert.ToInt32(ans[0]));

                        string ret = string.Format("He joined ms around {3}/{4}. It's about {0} days, almost {1} years and {2} months!",
                                        ans[0], year, month, dt.Year, dt.Month);
                        return ret;
                    }else
                        return "should be "+ans[0];
                }
            }
            //how many.
            if (wl[0] == "how" && wl[1] == "many")
            {
                ans = process(wl.ToList<object>(), 2, end, 1);
                if (ans!= null && ans.Count == 0)
                {
                    return "there seems none of it!";
                }
                else if(ans!= null && ans.Count > 0)
                {
                    string is_are = "are";
                    if (ans.Count == 1)
                        is_are = "is";
                    string str = "I find there " + is_are + " " + ans.Count + " of them. There " + is_are + " ";
                    foreach (string a in ans)
                    {
                        str += " " + display(a) + ",";
                    }
                    return str;
                }
            }

            //judgement: is
            if (wl[0] == "is" || wl[0] == "was" || wl[0] == "has" || wl[0] == "have" || wl[0] == "do" || wl[0] == "does")
            {
                ans = process(wl.ToList<object>(), 1, end, 1);
                if (ans != null && ans.Count == 0)
                    return "No!";
                else if (ans != null && ans.Count > 0)
                {
                    return "Yes!";
                }
            }

            //others:
            ans = process(wl.ToList<object>(), 0, end, 1);
            if (ans == null || ans.Count == 0)
                return null;
            else if (ans.Count > 0)
            {
                string is_are = "are";
                if (ans.Count == 1)
                    is_are = "is";
                //string str = "There " + is_are + " " + ans.Count + " of them. There " + is_are + " ";
                string str = "There " + is_are + " ";
                foreach (string a in ans)
                {
                    str += " " + display(a);
                    if (aliasName.ContainsKey(a))
                    {
                        str += ", who sits on ";
                        foreach (SPO spo in spoList)
                        {
                            if (string.Equals(spo.alias, a, StringComparison.OrdinalIgnoreCase))
                            {
                                if (string.Equals(spo.attribute, "physicalDeliveryOfficeName", StringComparison.OrdinalIgnoreCase))
                                {
                                    str += spo.value;
                                    break;
                                }
                            }
                        }
                    }
                }
                return str;
            }
            return null;
        }

        public string display(string s)
        {
            if (aliasName.ContainsKey(s))
            {
                return aliasName[s] + "(" + s + ")";
            }
            return s;
        }

        public void DP(string key, List<string> value)
        {
            if (!tempDp.ContainsKey(key))
                tempDp.Add(key, new List<string>(value));
            else
                tempDp[key] = value;
        }

        public bool can_lost(List<Object> wlist, int begin, int end) 
        {
            string temp = "";
            for (int i = begin; i < end; ++i)
            {
                if (wlist[i] is string)
                {
                    temp += " " + wlist[i];
                }
                else
                {
                    return false;
                }
            }
            temp = temp.Trim();
            if (logicReplace.ContainsKey(temp) && logicReplace[temp].IndexOf( "" )!=-1)
            {
                return true;
            }
            return false;
        }

        public List<string> process(List<object> wlist, int begin, int end, int layer)
        {

            if (begin >= end || layer > 7)
                return null;

            //DP. memory.
            string myKey = key(wlist.GetRange(begin, end - begin));
            if (myKey != null)
            {
                if (tempDp.ContainsKey(myKey))
                {
                    return tempDp[myKey];
                }
                //avoid unlimit resycle
                tempDp[myKey] = null;

                //direct match
                List<object> tlist = wlist.GetRange(begin, end - begin);
                List<string> ans = directList(tlist);

                if (DEBUG)
                {
                    if (ans != null)
                    {
                        Console.WriteLine("DIRECT: [{0}] : [{1}]", key(tlist), string.Join(",", ans.ToArray()));
                    }
                }
                
                if (ans != null && ans.Count > 0)
                {
                    DP(myKey, ans);
                    return ans;
                }

                //not direct match, go divide and conque
                List<string> best_ans = null;
                int best_sep = -1;
                List<string> ansA = new List<string>();
                List<string> ansB = new List<string>();

                for (int mid = begin + 1; mid < end; mid++)
                {
                    if (can_lost(wlist, begin, mid))
                    {
                        ans = process(wlist, mid, end, layer + 1);
                        if (ans != null && (best_ans == null || ans.Count > best_ans.Count))
                        {
                            best_ans = ans;
                            best_sep = mid;
                        }
                    }
                    if (wlist[mid] is string && (andTerm.IndexOf(wlist[mid].ToString()) != -1 || orTerm.IndexOf(wlist[mid].ToString()) != -1))
                    {
                        ansA = process(wlist, begin, mid, layer + 1);
                        ansB = process(wlist, mid + 1, end, layer + 1);
                        ans = null;

                        if (ansA == null || ansB == null)
                            continue;

                        if (andTerm.IndexOf(wlist[mid].ToString()) != -1)
                        {
                            //intersection
                            ans = Intersect(ansA, ansB);
                        }
                        else if (orTerm.IndexOf(wlist[mid].ToString()) != -1)
                        {
                            //union
                            ans = Union(ansA, ansB);
                        }
                        if (ans != null && (best_ans == null || ans.Count > best_ans.Count))
                        {
                            best_ans = ans;
                            best_sep = mid;
                        }
                    }
                    else
                    {
                        ansA = process(wlist, begin, mid, layer + 1);
                        ansB = process(wlist, mid, end, layer + 1);

                        List<object> newList = new List<object>();
                        if (ansA != null)
                        {
                            newList.Add(ansA);
                        }
                        else
                        {
                            newList.AddRange(wlist.GetRange(begin, mid - begin));
                        }
                        if (ansB != null)
                        {
                            newList.Add(ansB);
                        }
                        else
                        {
                            newList.AddRange(wlist.GetRange(mid, end - mid));
                        }
                        ans = process(newList, 0, newList.Count, layer + 1);

                        if (ans != null && (best_ans == null || ans.Count > best_ans.Count))
                        {
                            best_ans = ans;
                            best_sep = mid;
                        }
                        //try to intersect.
                        if (ansA != null && ansB != null)
                        {
                            ans = Intersect(ansA, ansB);
                            if (ans != null && (best_ans == null || ans.Count > best_ans.Count))
                            {
                                best_ans = ans;
                                best_sep = mid;
                            }
                        }
                    }
                }
                DP(myKey, best_ans);

                if (DEBUG)
                {
                    if (ans != null)
                    {
                        Console.WriteLine("PROCESS: [{0}] : [{1}]", myKey, string.Join(",", ans.ToArray()));
                    }
                }
                return best_ans;
            }
            return null;
        }

        public List<string> Union(List<string> a, List<string> b)
        {
            if (a == null)
                return b;
            if (b == null)
                return a;
            List<string> ans = new List<string>();
            foreach (string x in a)
            {
                ans.Add(x);
            }
            foreach (string x in b)
            {
                if (ans.IndexOf(x) == -1)
                {
                    ans.Add(x);
                }
            }
            return ans;
        }

        public List<string> Intersect(List<string> a, List<string> b)
        {
            if (a == null || b == null)
                return null;
            List<string> ans = new List<string>();
            foreach (string x in b)
            {
                if (a.IndexOf(x) != -1)
                {
                    ans.Add(x);
                }
            }
            return ans;
        }

        public string key(List<object> wlist)
        {
            if (wlist == null)
                return null;

            string key = "";
            foreach (object item in wlist)
            {
                if (item is string)
                {
                    key += item + ",";
                }
                else if (item is List<string>)
                {
                    key += "{";
                    foreach (string s in item as List<string>)
                    {
                        key += s + ",";
                    }
                    key += "}";
                }
            }
            return key;
        }

        public bool OMatch(SPO spo, object OSet)
        {
            if (spo.attribute == "physicalDeliveryOfficeName".ToLower())
            {
                foreach (string o in (OSet as List<string>))
                {
                    if (spo.value.IndexOf(o) != -1)
                    {
                        return true;
                    } 
                }
                return false;
            }

            return ((OSet as List<string>).IndexOf(spo.value) != -1);
        }

        public List<string> directList(List<object> wlist, bool rep = true)
        {
            try
            {
                if (wlist == null)
                    return null;
                if (wlist.Count == 1 && (wlist[0] is List<string>))
                    return wlist[0] as List<string>;

                foreach (Template template in logicTemplate)
                {
                    //sub prop
                    Object S = null, P = null, O = null, C = null;
                    List<object> left = wlist;
                    bool ok = true;
                    foreach (string item in template.template)
                    {
                        object cur = item;
                        if (item == "S")
                            //cur = nameSet;
                            cur = allNameSet;
                        else if (item == "P")
                            //cur = propNameSet;
                            cur = allFieldSet;
                        else if (item == "O")
                            //cur = valueSet;
                            cur = allValueSet;
                        else if (item == "C")
                            cur = allClassSet;
                        //parse one-term.
                        MatchClass mc = match(left, cur);
                        left = (mc == null) ? null : mc.wlist;

                        if (mc == null || mc.tmp == null)
                        {
                            ok = false;
                            break;
                        }
                        if (mc.tmp is List<string>)
                        {
                            if (item == "S")
                                S = mc.tmp;
                            else if (item == "P")
                                P = mc.tmp;
                            else if (item == "O")
                                O = mc.tmp;
                            else if (item == "C")
                                C = mc.tmp;

                        }
                    }
                    if (ok && (left == null || left.Count == 0) && (S != null || P != null || O != null || C != null))
                    {
                        List<string> ans = new List<string>();
                        if (C != null)
                        {
                            ans = allClassValue[(C as List<string>)[0]].ToList();
                            return ans;
                        }
                        if (O != null && P == null)
                        {
                            bool tempOK = false;
                            foreach (string o in O as List<string>)
                            {
                                if ( allNameSet.Contains(o) )
                                {
                                    tempOK = true;
                                    break;
                                }
                            }

                            if (tempOK)
                            {
                                if (S == null)
                                {
                                    return O as List<string>;
                                }
                                else
                                {
                                    continue;
                                }
                            }
                        }
                        if (S != null && P == null && O == null)
                        {
                            ans = Union(ans, S as List<string>);
                        }
                        else
                        {
                            foreach (SPO spo in spoList)
                            {
                                if (P != null && (P as List<string>).IndexOf(spo.attribute) == -1)
                                    continue;
                                if ((S == null || nameOK(spo.alias as string, S as List<string>))
                                        && (O == null || OMatch(spo, O))) //TODO: alias?
                                {
                                    if (S != null && O == null)
                                    {
                                        if (ans.IndexOf(spo.value) == -1)
                                            ans.Add(spo.value);
                                    }
                                    else
                                    {
                                        if (ans.IndexOf(spo.alias) == -1)
                                            ans.Add(spo.alias);
                                    }

                                }
                            }
                            if (template.flag) // special sentence
                            {
                                List<string> tempSet = ans;
                                ans = new List<string>();
                                foreach (SPO spo in spoList)
                                {
                                    if (P != null && (P as List<string>).IndexOf(spo.attribute) == -1)
                                        continue;
                                    if ( (tempSet.IndexOf(spo.value) != -1) && O == null )
                                    {
                                        if ((S != null && (S as List<string>).IndexOf(spo.alias) == -1) && ans.IndexOf(spo.alias) == -1)
                                            ans.Add(spo.alias);
                                    }
                                }
                            }
                        }
                        return ans as List<string>;
                    }
                }

                // otherwise.
                {
                    List<string> ans = null;
                    List<List<object>> output = replace(wlist, rep);
                    foreach (List<object> o in output)
                    {
                        List<string> temp = directList(o, false);
                        if (ansCompare(temp, ans))
                            ans = temp;
                    }
                    return ans;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception in directlist," + ex.Message);
                throw ex;
            }
        }

        public bool ansCompare(List<string> a, List<string> b)
        {
            if (a != null && (b == null || (b != null && a.Count > b.Count)))
                return true;
            return false;
        }

        public bool nameOK(string s, List<string> S)
        {
            if (S.IndexOf(s) != -1)
                return true;

            char[] sep = new char[] { ' ' };
            string[] subList = s.Split(sep);
            foreach (string sub in subList)
            {
                if (S.IndexOf(sub) != -1)
                {
                    return true;
                }
            }
            return false;
        }

        public MatchClass match(List<object> wlist, object name_or_set)
        { // sentence type match, left indentation to  S/P/O/C
            if (wlist == null || wlist.Count == 0)
                return null;

            if (wlist[0] is List<string>)
            {
                List<string> ret = new List<string>();
                if (name_or_set is HashSet<string>)
                {
                    List<string> wl = wlist[0] as List<string>;
                    foreach (string it in wl)
                    {
                        HashSet<string> nos = name_or_set as HashSet<string>;
                        if ( it!="" && nos.Contains(it) )
                        {
                            if (nameAlias.ContainsKey(it))
                            {
                                foreach (string refer in nameAlias[it])
                                {
                                    ret.Add(refer);

                                }
                            }
                            else
                            {
                                ret.Add(it);
                            }

                        }
                    }
                    if (ret.Count == 0)
                        return new MatchClass(null, wlist);
                    return new MatchClass(ret, new List<object>(wlist.GetRange(1, wlist.Count - 1)));
                }
                else
                {
                    return new MatchClass(null, wlist);
                }
            }
            else
            {
                for (int i = Math.Min(3, wlist.Count); i > 0; i--)
                {
                    bool tempOK = true;
                    for (int j = 0; j < i; j++)
                    {
                        if (wlist[j] is List<string>)
                        {
                            tempOK = false;
                            break;
                        }
                    }
                    if (!tempOK)
                    {
                        continue;
                    }
                    string tmp = string.Empty;
                    for (int j = 0; j < i; j++)
                    {
                        tmp += wlist[j] + " ";
                    }
                    if (tmp.Length > 0)
                        tmp = tmp.Remove(tmp.Length - 1);

                    List<string> tlist = new List<string>();
                    tlist.Add(tmp);
                    if (logicReplace.ContainsKey(tmp))
                        tlist.AddRange(logicReplace[tmp]);

                    List<string> s = new List<string>();
                    foreach (string item in tlist)
                    {
                        if (name_or_set is HashSet<string>)
                        {
                            if ( item!="" && (name_or_set as HashSet<string>).Contains(item) )
                            {
                                if (nameAlias.ContainsKey(item))
                                {
                                    foreach (string refer in nameAlias[item])
                                    {
                                        s.Add(refer);
                                    }
                                }
                                else
                                {
                                    s.Add(item);
                                }
                            }

                        }
                        else
                        {
                            if ((name_or_set as string) == item)
                                s.Add(item);
                        }
                    }
                    if (s.Count > 0)
                    {
                        return new MatchClass(s, wlist.GetRange(i, wlist.Count - i));
                    }

                }
            }

            return new MatchClass(null, wlist);
        }

        //for wlist replacement
        public List<List<object>> replace(List<object> wlist, bool can_replace)
        {
            if (wlist == null)
                return null;
            int begin = 0;
            int end = wlist.Count;

            List<List<object>> wl = new List<List<object>>();

            for (int start = begin; start < end; start++)
            {
                for (int i = 3; i > 0; i--)
                {
                    if (start + i > end)
                        continue;

                    bool ok = true;
                    for (int j = start; j < start + i; j++)
                    {
                        if (wlist[j] is List<string>)
                        {
                            ok = false;
                            break;
                        }
                    }
                    if (!ok)
                        continue;

                    string a = string.Empty;
                    for (int j = start; j < start + i; j++)
                    {
                        a += wlist[j] + " ";
                    }
                    if (a.Length > 0)
                        a = a.Remove(a.Length - 1);

                    char[] sep = new char[] { ' ' };
                    if (logicReplace.ContainsKey(a))
                    {
                        foreach (string b in logicReplace[a])
                        {
                            if (!can_replace && b != "")
                            {
                                // if can not replace, only delete term can go.
                                continue;
                            }
                            string c = b.ToLower();
                            string[] list = c.Split(sep, StringSplitOptions.RemoveEmptyEntries);
                            List<object> l = new List<object>(list);

                            List<object> wll = new List<object>();
                            wll.AddRange(wlist.GetRange(begin, start - begin));
                            if (l.Count > 0)
                            {
                                wll.AddRange(l);
                            }
                            wll.AddRange(wlist.GetRange(start + i, end - start - i));
                            wl.Add(wll);

                        }
                    }
                }
            }
            return wl;
        }
    }
}
