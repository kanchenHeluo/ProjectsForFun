angular.module('commonApp')
.directive("ckDatepicker", function () {
    return {
        restrict: 'E',
        scope: {
            ngModel: "="
        },
        template: '<p class="input-group">'
            + '<input type="text" class="form-control" datepicker-popup="{{format}}" ng-model="ngModel" is-open="opened" min-date="minDate" datepicker-options="dateOptions" ng-required="true" close-text="Close" />'
            + '<span class="input-group-btn">'
            + '<button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button>'
            + '</span></p>',

        link: function ($scope) {
            $scope.today = function () {
                $scope.ngModel = new Date();
            };
            $scope.today();

            $scope.clear = function () {
                $scope.ngModel = null;
            };

            //// Disable weekend selection
            //$scope.disabled = function (date, mode) {
            //    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
            //};

            $scope.toggleMin = function () {
                $scope.minDate = $scope.minDate ? null : new Date("1/1/2014");
            };
            $scope.toggleMin();

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened = true;
            };

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[0];
        }
    }
});