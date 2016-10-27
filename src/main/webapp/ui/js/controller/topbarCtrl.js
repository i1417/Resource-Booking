// Created By - Arpit Pittie
var topbarApp = angular.module('topbarApp', ['utilityFunctionsFactory', 'dataShareFactory']);

// Checking if user tries to access the page without login
if (sessionStorage.length == 0) {
    window.location = '/Resource-Booking/';
}

// Controller to control the topbar functionality
topbarApp.controller('topbarCtrl', function($scope, $window, userDetails, utilityFunctions) {
    $scope.currentUser = userDetails.getCurrentUser();

    // To load the Google API for the logout functionality
    $scope.onLoad = function() {
        gapi.load('auth2', function() {
            gapi.auth2.init();
        });
    }

    // To perform the logout
    $scope.logOut = function() {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function() {
            sessionStorage.clear();
            $window.location.href = '/Resource-Booking/';
        });

    }
});
