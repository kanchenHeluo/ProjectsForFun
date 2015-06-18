var mapApp = angular.module("mapApp", ['commonApp', 'ui.router']);

mapApp.config(['$stateProvider', '$urlRouterProvider', '$locationProvider', function ($stateProvider, $urlRouterProvider, $locationProvider) {
    $urlRouterProvider.otherwise('/'); 
    $stateProvider.state('home', {
        url: '/',
        templateUrl: '/Topic/navTopic',
        controller: 'naviTopicCtrl'

    }).state('viewTopic', {
        url: '/topic/view',
        templateUrl: '/Topic/viewTopic',
        controller: 'viewTopicCtrl'

    }).state('postTopic', {
        url: '/topic/post',
        templateUrl: '/Topic/postTopic',
        controller: 'postTopicCtrl'

    });

    //$locationProvider.html5Mode({ enabled: true, requireBase: false }); 
}]);