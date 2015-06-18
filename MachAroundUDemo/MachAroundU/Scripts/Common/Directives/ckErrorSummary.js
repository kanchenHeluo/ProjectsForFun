angular.module("commonApp")
  .directive('ckErrorSummary', ['$compile', function ($compile) {
      function link(scope, element) {
          var template =  
            '<div class="validation-summary-errors" data-valmsg-summary="true" ng-if="errorMessages && alias && errorMessages[alias].length > 0">' +
            '<span>{{messageTitle}}:</span>' +
            '<ul>' +
            '<li ng-repeat="message in errorMessages[alias] track by $index">{{message}}</li>' +
            '</ul>' +
            '</div>';

            var parentFormCtrl = element.parent().controller('ck-form');

            if (parentFormCtrl.errorMessages && parentFormCtrl.alias) {
                scope.errorMessages = parentFormCtrl.errorMessages;
                scope.alias = parentFormCtrl.alias;
            };

            var compliedElement = $compile(template)(scope);
            element.append(compliedElement);
      }
      return {
          restrict: 'E',
          scope: {
              messageTitle : '@',
          },
          link: link
      };
  }]);