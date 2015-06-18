using System.Collections.Generic;
using System.Web.Mvc;
using MachAroundU.Common;
using MachAroundU.Models;
using gorzService;
using System.Configuration;
using System;

namespace MachAroundU.Controllers
{
    public class MapController : WebBaseController
    {
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
                        Location = new Location() { Latitude = item.latitude, Longitude = item.longitude },
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
                        Location = new Location() { Latitude = item.latitude, Longitude = item.longitude },
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
            //GorzService gorzService = new GorzService(ConfigurationManager.AppSettings["dbconnection"]);
            //gorzService.addTopic("happy1", DateTime.Now, DateTime.Now.AddDays(2), 112, 134, "zack", "test desc",0)
            if (topic.Id == 0)
            {
                topicModel newRecord = new topicModel();
                newRecord.desc = topic.Description;
                newRecord.stime = topic.TimeSpan.StartTime;
                newRecord.etime = topic.TimeSpan.EndTime;
                newRecord.latitude = topic.Location.Latitude;
                newRecord.longitude = topic.Location.Longitude;
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
            else
            {
                topicModel newRecord = new topicModel();
                newRecord.id = topic.Id;
                newRecord.desc = topic.Description;
                newRecord.stime = topic.TimeSpan.StartTime;
                newRecord.etime = topic.TimeSpan.EndTime;
                newRecord.latitude = topic.Location.Latitude;
                newRecord.longitude = topic.Location.Longitude;
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