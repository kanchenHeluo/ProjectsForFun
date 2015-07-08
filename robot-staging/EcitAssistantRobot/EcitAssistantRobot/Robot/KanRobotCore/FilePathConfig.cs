using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.KanRobotCore
{
    public class FilePathConfig
    {
        //for all
        public static string StopSymbolsPath = @"..\..\Config\stopSymbols.txt";
        public static string RewriteStandardWords = @"..\..\Config\rewriteStandardWords.txt";
        public static string NewQaPath = @"..\..\config\NewQa.txt";
        public static string LogPath = @"..\..\config\trainLog.txt";

        //for logic robot
        public static string LogicPath = @"..\..\Config\logic.txt";
        public static string LogicParsePath = @"..\..\Config\logic_parse_template.txt";
        public static string classPath = @"..\..\Config\logic_class.txt";
        public static string [] dataPath = { 
                @"..\..\Config\total.out"
                                        } ;
        public static string prototypeWordPath = @"..\..\Config\words.out";

        // for match robot
        public static string QaPath = @"..\..\Config\qa.txt";
        public static string GenerateQaPath = @"..\..\Config\generatedData.txt";
        public static string EssentialWordPath = @"..\..\Config\essentialwords.txt";
    }
}
