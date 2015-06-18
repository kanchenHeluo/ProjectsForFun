angular.module("commonApp")
.directive("ckHourMin", [function() {
    return {
        restrict: 'E',
        scope: {
            hour: "=",
            minute: "="
        },
        template: '<select ng-options="item.Value as item.Name for item in hours" ng-model="hour" ng-init="hour = hours[12].Value"></select>'
                 +'<select style="margin-left:5px" ng-options="item.Value as item.Name for item in minutes" ng-model="minute" ng-init="minute = minutes[0].Value"></select>',
        link: function(scope) {
            scope.hours = [
                { Name: '0 AM', Value: 0 },
                { Name: '1 AM', Value: 1 },
                { Name: '2 AM', Value: 2 },
                { Name: '3 AM', Value: 3 },
                { Name: '4 AM', Value: 4 },
                { Name: '5 AM', Value: 5 },
                { Name: '6 AM', Value: 6 },
                { Name: '7 AM', Value: 7 },
                { Name: '8 AM', Value: 8 },
                { Name: '9 AM', Value: 9 },
                { Name: '10 AM', Value: 10 },
                { Name: '11 AM', Value: 11 },
                { Name: '12 AM', Value: 12 },
                { Name: '1 PM', Value: 13 },
                { Name: '2 PM', Value: 14 },
                { Name: '3 PM', Value: 15 },
                { Name: '4 PM', Value: 16 },
                { Name: '5 PM', Value: 17 },
                { Name: '6 PM', Value: 18 },
                { Name: '7 PM', Value: 19 },
                { Name: '8 PM', Value: 20 },
                { Name: '9 PM', Value: 21 },
                { Name: '10 PM', Value: 22 },
                { Name: '11 PM', Value: 23 }];
            scope.minutes = [
                { Name: '0 Min', Value: 0 },
                { Name: '15 Min', Value: 15 },
                { Name: '30 Min', Value: 30 },
                { Name: '45 Min', Value: 45 }];
            scope.minute = 45;
            scope.hour = 18;
        }
    }
}]);