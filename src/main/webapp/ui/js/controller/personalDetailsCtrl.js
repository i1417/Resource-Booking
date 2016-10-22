var personalDetailsPage = angular.module('personalDetailsApp', ['ui-notification', 'ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

personalDetailsPage.controller('personalDetailsCtrl', function($scope, $http, $window, userDetails, Notification) {
    $scope.currentUser = userDetails.getCurrentUser();

    $scope.updateDetails = function() {
        $http({
            method: 'POST',
            url: 'http://localhost:8080/Project-Authentication/user/update',
            data: $scope.currentUser,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            if (response.status == 403) {
                Notification.error({
                    message: response.errorMessage,
                    delay: 2000
                });
            } else {
                userDetails.setCurrentUser(response.data);
                $scope.currentUser = userDetails.getCurrentUser();
                $window.location.href = 'index.html';
            }
        }).error(function(response) {
            Notification.error({
                message: "Couldn't establish connection",
                delay: 2000
            });
        });
    }
});
