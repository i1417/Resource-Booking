var inviteUserPage = angular.module('inviteUserApp', ['ngRoute', 'topbarApp', 'sidebarApp']);

inviteUserPage.controller('inviteUserCtrl', function($scope, $http, $window) {
    $scope.user = {};

    $scope.sendInvite = function() {
        $http({
            method: 'POST',
            url: 'http://localhost:8080/Project-Authentication/user/sendInvitationToUser',
            data: $scope.user,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            alert("Invitation Send");
            $window.location.href = 'index.html';
        }).error(function(response) {
            alert("Connection Error");
        });
    }
});
