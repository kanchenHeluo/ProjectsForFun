angular.module("commonApp")
.directive("ckRequired", function () {
    return {
        restrict: 'A',
        require: ['^ckForm'],
        link: function (scope, element, attrs) {
            var errMsg = attrs['opValidationMsg']; 
            var parentFormCtrl = $(element).controller('ck-form');
            var guid = jQuery.guid++;
            if (!element.validatorFlag) element.validatorFlag = {};
            var model = attrs['ngModel'] || attrs['ngBind'];
            var errorClass = attrs['opErrorClass'] || 'error';

            var validate = function () {
                var ngModel = scope.$eval(model);
                var validationEnable = scope.$eval(attrs['validationEnable']);
                
                if (validationEnable == false) {
                    return true;
                }
                if (!ngModel) {
                    if (!errMsg) {
                        errMsg = attrs['name'] + " is required.";
                    }
                    parentFormCtrl.$pushError(guid, errMsg, false, element, errorClass);
                    return false;
                } else {
                    parentFormCtrl.$setPristine();
                    parentFormCtrl.$popError(guid, false, element, errorClass);
                    return true;
                }
            };

            $(element).on('blur', validate);

            scope.$watch(model, function () {
                validate();
            });

            scope.$watch(function() {
                return scope.$eval(element.attr('validation-enable'));
            }, function() {
                validate();
            });

            parentFormCtrl.$register(validate);

            $(element).on('$destroy', function handleDestroyEvent() {
                parentFormCtrl.$popError(guid, false, element, errorClass);
                parentFormCtrl.$unregister(validate);
            });
        }
    }
});