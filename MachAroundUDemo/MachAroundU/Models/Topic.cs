
namespace MachAroundU.Models
{
    public class Topic
    {
        public long Id { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public OrganizorInfo Organizor { get; set; }
        public Location Location { get; set; }
        public PeriodDateTime TimeSpan { get; set; }
        public long UpCnts { get; set; }
    }
}