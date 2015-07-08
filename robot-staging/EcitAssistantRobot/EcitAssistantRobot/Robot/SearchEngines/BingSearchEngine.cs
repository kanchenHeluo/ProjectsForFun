using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Robot.SearchEngines
{
    class BingSearchEngine
    {
        const string _accountKey = "TJXPJNGwMW5SgA0rpoFLLFzRE0AkuDfbd5RRmoWQQLo=";
        const string _rootUri = "https://api.datamarket.azure.com/Bing/Search";
        const string _market = "en-us";

        static readonly Bing.BingSearchContainer _bing;

        static BingSearchEngine()
        {
            _bing = new Bing.BingSearchContainer(new Uri(_rootUri));
            _bing.Credentials = new NetworkCredential(_accountKey, _accountKey);
        }

        public IEnumerable<SearchResult> GetTopAnswers(string query, int topN)
        {
            var webQuery = _bing.Web(query, null, null, _market, null, null, null, null);
            var results = webQuery.Execute();
            return results
                .Take(topN)
                .Select(r => new SearchResult
                {
                    Title = r.Title,
                    Url = r.Url,
                })
                .ToArray();
        }
    }
}
