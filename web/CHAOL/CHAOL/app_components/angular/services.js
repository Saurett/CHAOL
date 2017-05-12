(function () {
    var app = angular.module('app');

    app.factory('unixTime', function() {
        var getTime = function () {
            return Math.round((new Date()).getTime() / 1000);
        };
        return getTime;
    });
})();