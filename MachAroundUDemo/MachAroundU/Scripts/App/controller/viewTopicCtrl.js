mapApp.controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'item', ModalInstanceCtrl]);

mapApp.controller('viewTopicCtrl', ['$scope', 'mapSvc', '$modal', '$compile', viewTopicCtrl]);

function ModalInstanceCtrl($scope, $modalInstance, item) {
    $scope.item = item;

    $scope.ok = function () {
        $modalInstance.close($scope.item);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

function viewTopicCtrl($scope, mapSvc, $modal, $compile) {
    //bing map information
    var map = null, infoboxLayer = null, mapDataLayer = null;

    var loc = null;
    var searchManager = null;
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
                $scope.searchTopicCriteria.location = loc;
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
                        var pin = new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(loc.latitude, loc.longitude), { draggable: true});
                        if (mapDataLayer && pin) {
                            map.setView({ center: loc, zoom: 13 });
                            mapDataLayer.push(pin);
                        }
                    }
                }
            }
        };
        searchManager.geocode(request);
    };


    $scope.init = function () {
        var mapOptions = {
            credentials: "AlYIlu1qh5wwQge-SprZ8egmfuDZtlFWLAzE6wvBzNs0dFsFB5Qpcs84p_PYN3c4",
            center: new Microsoft.Maps.Location(39.978295407702404, 116.3042957028998),
            mapTypeId: Microsoft.Maps.MapTypeId.birdseye,
            zoom: 17,
            showScalebar: false
        };

        map = new Microsoft.Maps.Map(document.getElementById("myMap"), mapOptions);

        mapDataLayer = new Microsoft.Maps.EntityCollection();
        map.entities.push(mapDataLayer);
        infoboxLayer = new Microsoft.Maps.EntityCollection();
        map.entities.push(infoboxLayer);
    };

    $scope.addPinPoint = function (index) {
        var infoboxOptions = { visible: false, offset: new Microsoft.Maps.Point(0, 20), width: 240, height: 240 };
        infobox = new Microsoft.Maps.Infobox(new Microsoft.Maps.Location(0, 0), infoboxOptions);
        infoboxLayer.push(infobox);
        var center = new Microsoft.Maps.Location($scope.topics[index].Location.Latitude, $scope.topics[index].Location.Longitude);
        var pin = new Microsoft.Maps.Pushpin(center, { draggable: false });
        pin.Title = "Title:<span style=color:red>" + $scope.topics[index].Title + "</span>";
        pin.Description = '<div id="pin_' + index + '"></div>';

        Microsoft.Maps.Events.addHandler(pin, 'click', function displayInfobox(e) {
            if (e.targetType == 'pushpin') {
                infobox.setLocation(e.target.getLocation());
                infobox.setOptions({ visible: true, title: e.target.Title, description: e.target.Description });

                var elemHTML = "Description:<b>" + $scope.topics[index].Description + "</b><br/><a href='#' ng-click='openTopicDetail(" + index + ")'>Detail</a>";
                var elem = $compile(elemHTML)($scope);
                $('#pin_' + index).html(elem);
            }
        });
        mapDataLayer.push(pin);

    };

    $scope.refreshMap = function () {
        if (mapDataLayer && infoboxLayer) {
            mapDataLayer.clear();
            infoboxLayer.clear();
            var len = $scope.topics.length;
            for (var i = 0; i < len; i++) {
                $scope.addPinPoint(i);

            }
        }
    };

    $scope.map = {
        show: true
    };

    $scope.list = {
        show: false
    };

    $scope.searchTopicCriteria = {
        keyword: null,
        location: {
            latitude: 39.978295407702404,
            longitude: 116.3042957028998,
            distance: 100
        },
        timeSpan: {
            startTime: new Date("2015/1/1"),
            endTime: new Date("2016/1/1"),
            start: {
                startHour: null,
                startMin: null
            },
            end: {
                endHour: null,
                endMin: null
            }
        },
        involedAlias: null
    };

    $scope.topics = [];
    $scope.topicSelected = null;

    $scope.addLike = function (index) {
        $scope.topics[index].UpCnts++;
        $scope.updateTopic($scope.topics[index]);
    };

    $scope.updateTopic = function (topic) {
        mapSvc.updateTopics(topic).then(function () {

        });
    };

    $scope.searchTopics = function () {
        $scope.searchTopicCriteria.timeSpan.startTime.setHours($scope.searchTopicCriteria.timeSpan.start.startHour);
        $scope.searchTopicCriteria.timeSpan.startTime.setMinutes($scope.searchTopicCriteria.timeSpan.start.startMin);
        $scope.searchTopicCriteria.timeSpan.startTime.setSeconds(0);
        $scope.searchTopicCriteria.timeSpan.endTime.setHours($scope.searchTopicCriteria.timeSpan.end.endHour);
        $scope.searchTopicCriteria.timeSpan.endTime.setMinutes($scope.searchTopicCriteria.timeSpan.end.endMin);
        $scope.searchTopicCriteria.timeSpan.endTime.setSeconds(59);

        mapSvc.searchTopics($scope.searchTopicCriteria).then(function (data) {
            if (data) {
                $scope.topics = data;
                $scope.refreshMap();
                for (var i = 0; i < $scope.topics.length; i++) {
                    var r = (Math.floor((Math.random()) * 10) % 5 + 1);
                    $scope.topics[i].iconId = "/Content/Images/" + r + ".png";
                }
            }
        });
    };

    $scope.loadTopics = function () {
        mapSvc.loadTopics(null).then(function (data) {
            if (data) {
                $scope.topics = data;
                $scope.refreshMap();

                for (var i = 0; i < $scope.topics.length; i++) {
                    var r = (Math.floor((Math.random()) * 10) % 5 + 1);
                    $scope.topics[i].iconId = "/Content/Images/" + r + ".png";
                }
            }
        });
    };

    $scope.loadTopics();

    $scope.detailDialog = {
        isOpen: true
    };

    $scope.pageInfo = {
        detailPage: {
            itemsPerPage: 3,
            currentPage: 1,
            pageInterval: 3
        }
    };

    /*hold for dialog*/
    $scope.openTopicDetail = function (index) {
        $scope.topicSelected = $scope.topics[index];
        var stimeArr = $scope.topicSelected.TimeSpan.StartTime.split(/[- :]/);
        $scope.topicSelected.STime = stimeArr[0] + '/' + stimeArr[1] + '/' + stimeArr[2] + ' ' + stimeArr[3] + ':' + stimeArr[4];

        var etimeArr = $scope.topicSelected.TimeSpan.EndTime.split(/[- :]/);
        $scope.topicSelected.ETime = etimeArr[0] + '/' + etimeArr[1] + '/' + etimeArr[2] + ' ' + etimeArr[3] + ':' + etimeArr[4];
        
        $scope.open();
    };

    $scope.open = function (size) {
        var modalInstance = $modal.open({
            templateUrl: 'myModalContent.html',
            controller: 'ModalInstanceCtrl',
            size: size,
            resolve: {
                item: function () {
                    return $scope.topicSelected;
                }
            }
        });

        modalInstance.result.then(function (selectedItem) {
            $scope.selected = selectedItem;
        }, function () {
            console.log('Modal dismissed at: ' + new Date());
        });
    };
};