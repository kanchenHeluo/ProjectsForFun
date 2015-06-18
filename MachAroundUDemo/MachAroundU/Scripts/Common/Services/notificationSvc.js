angular.module("commonApp")
.factory("notificationSvc", function () {
    var baseStatus = {
        isProcessing: false,
        message: '',
        error: ''
    };

    function message(msg) {
        baseStatus.message = msg;
        if (msg && window.toastMessage) {
            window.toastMessage.Show({ Color: 'Green', Tilte: 'Success', Message: msg });
        }
    };

    function error(err) {
        baseStatus.error = error;
        if (err && window.toastMessage) {
            window.toastMessage.Show({ Color: 'Yellow', Tilte: 'Error', Message: err });
        }
    };
   
    return {
        status: baseStatus,
        message: message,
        error: error
    };
});