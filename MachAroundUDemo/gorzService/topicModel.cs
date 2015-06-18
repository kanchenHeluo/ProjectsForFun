using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace gorzService
{
    public class topicModel
    {
        public long id;
        public string topicName;
        public DateTime stime;
        public DateTime etime;

        public double longitude;
        public double latitude;
        public string owner;
        public string desc;
        public long up;
        public string locationAddr;

        public topicModel() { }
        public topicModel(string topicName, DateTime stime, DateTime etime, double longitude, double latitude, string locationAddr, string owner, string desc, long up = 0)
        {
            this.topicName = topicName;
            this.stime = stime;
            this.stime = etime;
            this.longitude = longitude;
            this.latitude = latitude;
            this.locationAddr = locationAddr;
            this.up = up;
            this.owner = owner;
            this.desc = desc;
        }
    }
}
