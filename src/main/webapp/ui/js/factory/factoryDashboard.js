// Created By - Arpit Pittie, Rohit Singhavi, Amit Sharma
angular.module('utilityFunctionsFactory', []).factory('utilityFunctions', function($window) {
    var utilityFunc = {};

    var allResources = {};

    // To perform the sign out action
    utilityFunc.performSignOut = function() {
        gapi.load('auth2', function() {
            gapi.auth2.init();
        });
        gapi.auth2.init();
        var auth2 = gapi.auth2.getAuthInstance();
        console.log(auth2);
        gapi.auth2.signOut().then(function() {
            console.log('User signed out.');
        });
    }

    // To set all the resources
    utilityFunc.setAllResources = function(data) {
        allResources = data;
    }

    // To get the resoources list
    utilityFunc.getAllResources = function() {
        return allResources;
    }

    // To set the resource details
    utilityFunc.setResourceDetails = function(data) {
        $window.sessionStorage.setItem('resource', angular.toJson(data));
    }

    // To get the resource details
    utilityFunc.getResourceDetails = function() {
        return JSON.parse($window.sessionStorage.getItem('resource'));
    }

    return utilityFunc;
});
