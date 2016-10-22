var forgotPasswordPage = angular.module('forgotPasswordApp', ['ui-notification', 'angular-md5']);

forgotPasswordPage.config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode(true);
}]);

forgotPasswordPage.controller('forgotPasswordCtrl', function($scope, $window, $location, $http, md5, Notification) {
    $scope.user = {};

    $scope.redirect = function() {
        $window.location.href = 'http://localhost:8080/Project-Authentication/';
    }
    $scope.changePassword = function() {
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

            $scope.user.email = 'anant.sharma@company.com';

            var urlToRedirect = 'http://localhost:8080/Project-Authentication/changePassword?token=' + $location.search().token;

            $('h1').hide();
            $('#container_demo').hide();
            $('#spinner').show();
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
