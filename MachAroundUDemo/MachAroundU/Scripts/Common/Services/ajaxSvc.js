angular.module("commonApp")
.factory("ajaxSvc", ['$http', '$q', 'notificationSvc', function ($http, $q, notificationSvc) {
    function beginRequest(supressGlobal) {
        notificationSvc.status.isProcessing = !supressGlobal;
        notificationSvc.status.message = '';
        notificationSvc.status.error = '';
    }

    function endRequest(deferred, data) {
        notificationSvc.status.isProcessing = false;
        if (data.IsRedirect === true) {
            if (data.RedirectLocation) {
                window.location = data.RedirectLocation;
            } else {
                failed(deferred, '404', data);
            }
            return;
        }
        if (data.HasBase) {
            if (data.Message) {
                notificationSvc.message(data.Message);
            }
            if (data.Error) {
                notificationSvc.error(data.Error);
                deferred.reject(data.Data);
                return;
            }
            deferred.resolve(data.Data);
        } else {
            deferred.resolve(data);
        }
    }

    function failed(deferred, status, data) {
        notificationSvc.status.isProcessing = false;
        notificationSvc.error('Unexpected Error');//TODO: we should redirect here
        deferred.reject(status);
    }

    function get(url, params, supressGlobal) {
        var deferred = $q.defer();
        beginRequest(supressGlobal);
        var request = $http.get(url, { params: params })
            .success(function (data) {
                endRequest(deferred, data);
            }).error(function (data, status) {
                failed(deferred, status, data);
            });
        return deferred.promise;
    }

    function post(url, params, supressGlobal) {
        var deferred = $q.defer();
        beginRequest(supressGlobal);
        $http.post(url, params).success(function (data) {
            endRequest(deferred, data);
        }).error(function (data, status) {
            failed(deferred, status, data);
        });
        return deferred.promise;
    }

    return {
        ajaxStatus: notificationSvc,
        get: get,
        post: post
    };
}]);