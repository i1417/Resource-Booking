// Created By - Arpit Pittie
var cancelBookingPage = angular.module('cancelBookingApp', []);

// Controller for cancelling the pending bookings or today
cancelBookingPage.controller('cancelBookingCtrl', function($http, $window) {
    // Request to cancel the pending booking
    $http({
        method: 'GET',
        url: 'http://localhost:8080/Project-Authentication/bookings/cancelTodayBookings',
        headers: {
            'Content-Type': 'application/json'
        }
    }).success(function(response) {
        $window.close();
    });
});
