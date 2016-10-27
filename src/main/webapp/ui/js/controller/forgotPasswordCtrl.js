// Created By - Arpit Pittie
var forgotPasswordPage = angular.module('forgotPasswordApp', ['ui-notification', 'angular-md5']);

// To configure it to open from the email
forgotPasswordPage.config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode(true);
}]);

// Controller To change the user's Password
forgotPasswordPage.controller('forgotPasswordCtrl', function($scope, $window, $location, $http, md5, Notification) {
    $scope.user = {};

    $scope.redirect = function() {
        $window.location.href = '/Resource-Booking/';
    }

    $scope.changePassword = function() {
        // Checking if both password match
        if ($scope.confirmPassword != $scope.user.password) {
            Notification.warning({
                message: "Password Do not match",
                delay: 2000
            });
        } else {
            if (!angular.isUndefined($scope.user.password)) {
                $scope.user.password = md5.createHash($scope.user.password);
                $scope.confirmPassword = md5.createHash($scope.confirmPassword);
            }

            $scope.user.email = $location.search().email;

            var urlToRedirect = '/Resource-Booking/changePassword?token=' + $location.search().token;

            $('h1').hide();
            $('#container_demo').hide();
            $('#spinner').show();
            // Request to update the password
            $http({
                method: 'POST',
                url: urlToRedirect,
                data: $scope.user,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                $('#container_demo').show();
                $('h1').show();
                $('#spinner').hide();
                $scope.user.password = "";
                $scope.confirmPassword = "";
                Notification.info({
                    message: response.errorMessage,
                    delay: 2000
                });
            }).error(function(response) {
                $('#container_demo').show();
                $('h1').show();
                $('#spinner').hide();
                Notification.error({
                    message: "Couldn't establish connection",
                    delay: 2000
                });
            });
        }
    }
});
