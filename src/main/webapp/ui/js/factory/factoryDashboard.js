angular.module('utilityFunctionsFactory', []).factory('utilityFunctions', function($window) {
    var utilityFunc = {};

    var allResources = {};

    utilityFunc.performSignOut = function() {
        // $window.sessionStorage.setItem("signout", "Perform Sign Out");
        // $window.sessionStorage.clear();
        gapi.load('auth2', function() {
            gapi.auth2.init();
        });
        gapi.auth2.init();
        var auth2 = gapi.auth2.getAuthInstance();
        console.log(auth2);
        gapi.auth2.signOut().then(function() {
            console.log('User signed out.');
        });

        // $window.location.href = "http://localhost:8080/Project-Authentication/";
    }

    utilityFunc.setAllResources = function(data) {
        allResources = data;
    }

    utilityFunc.getAllResources = function() {
        return allResources;
    }

    utilityFunc.setResourceDetails = function(data) {
        $window.sessionStorage.setItem('resource', angular.toJson(data));
    }

    utilityFunc.getResourceDetails = function() {
        return JSON.parse($window.sessionStorage.getItem('resource'));
    }

    return utilityFunc;
});
