var sidebarApp = angular.module('sidebarApp', ['ui-notification', 'dataShareFactory', 'utilityFunctionsFactory']);

sidebarApp.controller('sidebarCtrl', function($scope, $http, $window, userDetails, utilityFunctions, Notification) {

    $scope.currentUser = userDetails.getCurrentUser();
    $scope.resources = {};

    if ($scope.currentUser.role != 'admin') {
        $('#resourcesMgmt').hide();
        $('#inviteUser').hide();
        if ($scope.currentUser.role != 'res_admin') {
            $('#pendingRequest').hide();
        }
    } else {
        $http({
            method: 'GET',
            url: 'http://localhost:8080/Project-Authentication/resources/getAll',
            data: $scope.currentUser,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            if (response.status == 403) {
                Notification.error({
                    message: responce.errorMessage,
                    delay: 2000
                });
            } else {
                $scope.currentUser.adminOfResources = response.data;
                userDetails.setCurrentUser($scope.currentUser);
            }
        }).error(function(response) {
            Notification.error({
                message: "Couldn't establish connection",
                delay: 2000
            });
        });
    }

    // approve or reject existing booking
    $scope.updatePendingRequest = function(resourceId) {
        utilityFunctions.setResourceDetails(resourceId);
        $window.location.href = "pendingRequests.html";

    }

    $scope.fetchResourceDetails = function(resource) {
        utilityFunctions.setResourceDetails(resource);
        $window.location.href = "resourceEdit.html";
    }

    $scope.newResource = function() {
        utilityFunctions.setResourceDetails(null);
        $window.location.href = "resourceEdit.html";
    }
});
