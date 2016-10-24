// Created By - Arpit Pittie
var statusChangePage = angular.module('statusChangeApp', []);

// To configure it to open from the email
statusChangePage.config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode(true);
}]);

// Controller to change the status to Pending for today's pending booking list
statusChangePage.controller('statusChangeCtrl', function($scope, $location, $window, $http) {
    var urlToRedirect = '/Project-Authentication/bookings/statusChange?status=' + $location.search().status +
        '&bookingId=' + $location.search().bookingId + '&newBookingId=' + $location.search().newBookingId;

    // Request to cancel the bookings for today's day
    $http({
        method: 'GET',
        url: urlToRedirect,
        headers: {
            'Content-Type': 'application/json'
        }
    }).success(function(response) {
        $window.close();
    });
});
