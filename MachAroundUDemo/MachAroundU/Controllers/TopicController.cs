using System;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Configuration;
using System.Collections.Generic;
using MachAroundU.Common;
using MachAroundU.Models;
using gorzService;

namespace MachAroundU.Controllers
{
    public class TopicController : WebBaseController
    {
        public ActionResult NavTopic()
        {
            return PartialView();
        }

        public ActionResult PostTopic()
        {
            return PartialView();
        }

        public ActionResult ViewTopic()
        {
            return PartialView();
        }

        // load topics
        public ActionResult SearchTopics(SearchTopicCriteria request)
        {
            try
            {
                //load and return IEnumerable<Topic>
                List<Topic> topicList = new List<Topic>();
                GorzService gorzService = new GorzService(ConfigurationManager.AppSettings["dbconnection"]);
                List<topicModel> allTopics = gorzService.search(request.Keyword, request.Location.Latitude, request.Location.Longitude, request.TimeSpan.StartTime, request.TimeSpan.EndTime, request.Location.Distance);
                foreach (var item in allTopics)
                {
                    Topic topic = new Topic()
                    {
                        Id = item.id,
                        Title = item.topicName,
                        Description = item.desc,
                        Location = new Location() { Latitude = item.latitude, Longitude = item.longitude, LocationAddr = item.locationAddr },
                        Organizor = new OrganizorInfo() { EmailAddr = item.owner },
                        TimeSpan = new PeriodDateTime() { StartTime = item.stime, EndTime = item.etime },
                        UpCnts = item.up
                    };
                    topicList.Add(topic);
                }
                Message = "Search topic successfully";
                return Json(topicList);
            }
            catch (Exception ex)
            {
                throw ex;
            }

        }

        public ActionResult LoadTopics()
        {
            try
            {
                GorzService gorzService = new GorzService(ConfigurationManager.AppSettings["dbconnection"]);
                List<topicModel> allTopics = gorzService.LoadTopics();
                List<Topic> topicList = new List<Topic>();
                foreach (var item in allTopics)
                {
                    Topic topic = new Topic()
                    {
                        Id = item.id,
                        Title = item.topicName,
                        Description = item.desc,
                        Location = new Location() { Latitude = item.latitude, Longitude = item.longitude, LocationAddr=item.locationAddr },
                        Organizor = new OrganizorInfo() { EmailAddr = item.owner },
                        TimeSpan = new PeriodDateTime() { StartTime = item.stime, EndTime = item.etime },
                        UpCnts = item.up
                    };
                    topicList.Add(topic);
                }
                Message = "Load topic successfully";
                return Json(topicList);
            }
            catch (Exception ex)
            {
                throw ex;
            }

        }

        // create and post an topic
        public ActionResult PostTopics(Topic topic)
        {
            if (topic.Id == 0) //create
            {
                topicModel newRecord = new topicModel();
                newRecord.desc = topic.Description;
                newRecord.stime = topic.TimeSpan.StartTime;
                newRecord.etime = topic.TimeSpan.EndTime;
                newRecord.latitude = topic.Location.Latitude;
                newRecord.longitude = topic.Location.Longitude;
                newRecord.locationAddr = topic.Location.LocationAddr;
                newRecord.owner = topic.Organizor.EmailAddr;
                newRecord.topicName = topic.Title;
                newRecord.up = 0;
                GorzService gorzService = new GorzService(ConfigurationManager.AppSettings["dbconnection"]);
                int successNum = gorzService.save(newRecord);
                if (successNum == 0)
                {
                    Error = "post topic fail";
                    return Json(null);
                }
                Message = "post topic successfully";
                return Json(true);
            }
            else //update
            {
                topicModel newRecord = new topicModel();
                newRecord.id = topic.Id;
                newRecord.desc = topic.Description;
                newRecord.stime = topic.TimeSpan.StartTime;
                newRecord.etime = topic.TimeSpan.EndTime;
                newRecord.latitude = topic.Location.Latitude;
                newRecord.longitude = topic.Location.Longitude;
                newRecord.locationAddr = topic.Location.LocationAddr;
                newRecord.owner = topic.Organizor.EmailAddr;
                newRecord.topicName = topic.Title;
                newRecord.up = topic.UpCnts;
                GorzService gorzService = new GorzService(ConfigurationManager.AppSettings["dbconnection"]);
                int successNum = gorzService.update(newRecord);
                if (successNum == 0)
                {
                    Error = "update topic fail";
                    return Json(null);
                }
                Message = "update topic successfully";
                return Json(true);

            }


        }
    }
}
