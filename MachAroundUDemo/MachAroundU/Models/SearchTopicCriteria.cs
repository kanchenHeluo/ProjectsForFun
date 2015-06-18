namespace MachAroundU.Models
{
    public class SearchTopicCriteria
    {
        public string Keyword { get; set; }

        public Location Location { get; set; }

        public PeriodDateTime TimeSpan { get; set; }

        public string InvoledAlias { get; set; }

    }
}