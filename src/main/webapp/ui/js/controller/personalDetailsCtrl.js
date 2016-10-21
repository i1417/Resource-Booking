var personalDetailsPage = angular.module('personalDetailsApp', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

personalDetailsPage.controller('personalDetailsCtrl', function($scope, $http, $window, userDetails) {
    console.log(userDetails.getCurrentUser());
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
                console.log(response.errorMessage);
            } else {
                userDetails.setCurrentUser(response.data);
                $scope.currentUser = userDetails.getCurrentUser();
                $window.location.href = 'index.html';
            }
        }).error(function(response) {
            alert("Connection Error");
        });
    }
});
