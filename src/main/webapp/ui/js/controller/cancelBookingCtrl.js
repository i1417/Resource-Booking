var cancelBookingPage = angular.module('cancelBookingApp', []);

cancelBookingPage.controller('cancelBookingCtrl', function($http, $window) {
    $http({
        method : 'GET',
        url : 'http://localhost:8080/Project-Authentication/bookings/cancelTodayBookings',
        headers : {'Content-Type': 'application/json'}
    }).success(function(response) {
        $window.close();
    });
});
