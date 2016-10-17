angular.module('dataFactory',[]).factory('utilityFunctions', function($window) {
    var utilityFunc = {};

    var allResources = {};

    utilityFunc.performSignOut = function() {
    	$window.sessionStorage.setItem("signout", "Perform Sign Out");
        $window.sessionStorage.clear();
        $window.location.href = "http://localhost:8080/Project-Authentication/";
    }

    utilityFunc.setAllResources = function(data) {
        allResources = data;
    }

    utilityFunc.getAllResources = function() {
        return allResources
    }

    return utilityFunc;
});
