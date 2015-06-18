mapApp.controller('postTopicCtrl', ['$scope', 'mapSvc', postTopicCtrl]);

function postTopicCtrl($scope, mapSvc) {
    //topic information
    $scope.topic = {
        title: '',
        description: '',

        startTime: new Date(),
        endTime: new Date(),
        start: {
            startHour: null,
            startMin: null
        },
        end: {
            endHour: null,
            endMin: null
        },
        location: {
            latitude: 47.592,
            longitude: -122.332,
            locationAddr: ''
        },

        organizor: {
            email: ''
        }
    };

    $scope.post = function () {
        $scope.topic.startTime.setHours($scope.topic.start.startHour);
        $scope.topic.startTime.setMinutes($scope.topic.start.startMin);
        $scope.topic.endTime.setHours($scope.topic.end.endHour);
        $scope.topic.endTime.setMinutes($scope.topic.end.endMin);
        mapSvc.postTopics($scope.topic);
    };

    //Bing Map information
    var map = null;
    var mapDataLayer = null;
    var searchManager = null;
    var loc = null;
    $scope.searchTerm = '';

    var createSearchManager = function () {
        if (!searchManager) {
            Microsoft.Maps.loadModule("Microsoft.Maps.Search");
            map.addComponent("SearchManager", new Microsoft.Maps.Search.SearchManager(map));
            searchManager = map.getComponent("SearchManager");
        }
    };

    var searchLocationByPoint = function () {
        // search address.
        request = {
            location: new Microsoft.Maps.Location(loc.latitude, loc.longitude),
            callback: function (result, userData) {
                loc.locationAddr = result.name;
                $scope.topic.location = loc;
                $scope.$apply();
            },
        };
        searchManager.reverseGeocode(request);
    };

    $scope.searchTermBingMap = function () {
        createSearchManager();
        var request =
        {
            where: $scope.searchTerm,
            count: 5,
            bounds: map.getBounds(),
            callback: function (result, userData) {
                if (result) {
                    var topResult = result.results && result.results[0];                    
                    if (topResult) {
                        loc = topResult.location;
                        searchLocationByPoint();
                        var pin = new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(loc.latitude, loc.longitude), null);
                        if (mapDataLayer && pin) {
                            map.setView({ center: loc, zoom: 13 });
                            mapDataLayer.clear();
                            mapDataLayer.push(pin);
                        }
                    }
                }
            }
        };
        searchManager.geocode(request);
    };    

    var clickGetLocation = function (e) {
        if (e.targetType == "map") {
            var point = new Microsoft.Maps.Point(e.getX(), e.getY());
            loc = e.target.tryPixelToLocation(point);
            searchLocationByPoint();

            var pin = new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(loc.latitude, loc.longitude), { draggable: false });
            if (mapDataLayer && pin) {
                mapDataLayer.clear();
                mapDataLayer.push(pin);
            }
        }
    };

    $scope.init = function () {
        var mapOptions = {
            credentials: "AlYIlu1qh5wwQge-SprZ8egmfuDZtlFWLAzE6wvBzNs0dFsFB5Qpcs84p_PYN3c4",
            center: new Microsoft.Maps.Location(39.978295407702404, 116.3042957028998),
            mapTypeId: Microsoft.Maps.MapTypeId.birdseye,
            zoom: 17,
            showScalebar: false
        }
        map = new Microsoft.Maps.Map(document.getElementById("myPostMap"), mapOptions);
        
        mapDataLayer = new Microsoft.Maps.EntityCollection();
        map.entities.push(mapDataLayer);

        Microsoft.Maps.Events.addHandler(map, 'click', function (e) {
            clickGetLocation(e);
        });
    };
}