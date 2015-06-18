angular.module("mapApp")
.factory('mapSvc', ['ajaxSvc', function (ajaxSvc) {
    return {
        searchTopics: function (searchCtriteria) {
            var request = {
                Keyword: searchCtriteria.keyword,

                Location: {
                    Latitude: searchCtriteria.location.latitude,
                    Longitude: searchCtriteria.location.longitude,
                    Distance: searchCtriteria.location.distance
                },

                TimeSpan: {
                    StartTime: searchCtriteria.timeSpan.startTime,
                    EndTime: searchCtriteria.timeSpan.endTime
                },

                InvoledAlias: searchCtriteria.involedAlias
            };

            return ajaxSvc.post(rootUrl + "Topic/SearchTopics", request);
        },
        loadTopics: function () {
            return ajaxSvc.post(rootUrl + "Topic/LoadTopics", null);
        },
        postTopics: function (newTopic) {
            var topic = {
                Id: 0,
                Title: newTopic.title,

                Description: newTopic.description,

                Location: {
                    Latitude: newTopic.location.latitude,
                    Longitude: newTopic.location.longitude,
                    LocationAddr: newTopic.location.locationAddr
                },
                TimeSpan: {
                    StartTime: newTopic.startTime,
                    EndTime: newTopic.endTime
                },
                Organizor: {
                    EmailAddr: newTopic.organizor.email
                },
                UpCnts: 0
            };

            return ajaxSvc.post(rootUrl + "Topic/PostTopics", topic);
        },

        updateTopics: function (newTopic) {
            var topic = {
                Id: newTopic.Id,
                Title: newTopic.Title,
                Description: newTopic.Description,
                Location: newTopic.Location,
                TimeSpan: newTopic.TimeSpan,
                Organizor: newTopic.Organizor,
                UpCnts: newTopic.UpCnts
            };

            return ajaxSvc.post(rootUrl + "Topic/PostTopics", topic);
        }
    };


}]);