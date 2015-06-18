﻿angular.module("commonApp")
.directive("ckNumber", function() {
    return {
        restrict: 'A',
        require: ['ngModel', '^ckForm'],
        link: function (scope, element, attrs) {
            var NUMBER_REGEXP = /^\s*(\-|\+)?(\d+|(\d*(\.\d*)))\s*$/;
            var INT_REGEXP =  /^\s*(\+)?\d+\s*$/;
            var guid = jQuery.guid++;
            var parentFormCtrl = $(element).controller("ck-form");
            if (!element.validatorFlag) element.validatorFlag = {};
            var errorClass = attrs['opErrorClass'] || 'error';

            var validate = function () {
                var data = scope.$eval(attrs['ngModel']);
                var validationEnable = scope.$eval(attrs['validationEnable']);
                if (validationEnable == false) {
                    return true;
                }

                if (data) {
                    parentFormCtrl.$setPristine();
                    if (NUMBER_REGEXP.test(data)) {
                        var min, max;
                        with (scope) {
                            max = eval(attrs["ckMax"]);
                            min = eval(attrs["ckMin"]);
                        }
                        if (attrs.ckInt == "") {
                            if (!INT_REGEXP.test(data)) {
                                parentFormCtrl.$pushError(guid, data + " is not an qualify number", false, element, errorClass);
                                return false;
                            }
                        }
                        if (data < min) {
                            parentFormCtrl.$pushError(guid, data + " < " + min, false, element, errorClass);
                            return false;
                        } else if (data >= max) {
                            parentFormCtrl.$pushError(guid, data + " >= " + max, false, element, errorClass);
                            return false;
                        } else {
                            parentFormCtrl.$popError(guid, false, element, errorClass);
                            return true;
                        }

                    } else {
                        parentFormCtrl.$pushError(guid, data + " is not a number", false, element, errorClass);
                        return false;
                    }
                } else {
                    parentFormCtrl.$popError(guid, false, element, errorClass);
                    return false;
                };
            };

            $(element).on('blur', validate);

            scope.$watch(attrs['ngModel'], function (newValue, oldValue) {
                if (newValue != oldValue) {
                    validate();
                };
            });

            parentFormCtrl.$register(validate);

            $(element).on('$destroy', function handleDestroyEvent() {
                parentFormCtrl.$popError(guid, false, element, errorClass);
                parentFormCtrl.$unregister(validate);
            });
        }
    }
});