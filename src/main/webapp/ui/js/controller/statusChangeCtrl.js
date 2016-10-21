var statusChangePage = angular.module('statusChangeApp', []);

statusChangePage.config(['$locationProvider', function($locationProvider){
    $locationProvider.html5Mode(true);
}]);

statusChangePage.controller('statusChangeCtrl', function($scope, $location, $window, $http) {
    var urlToRedirect = 'http://localhost:8080/Project-Authentication/bookings/statusChange?status=' + $location.search().status
        + '&bookingId=' + $location.search().bookingId + '&newBookingId=' + $location.search().newBookingId;
    $http({
        method : 'GET',
        url : urlToRedirect,
        headers : {'Content-Type': 'application/json'}
    }).success(function(response) {
        $window.close();
    });
});
