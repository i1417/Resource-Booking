angular.module('dataFactory',[]).factory('utilityFunctions', function($window) {
    var utilityFunc = {};

    utilityFunc.performSignOut = function() {
        $window.sessionStorage.clear();
        $window.location.href = "http://localhost:8080/Project-Authentication/";
    }

    return utilityFunc;
});
