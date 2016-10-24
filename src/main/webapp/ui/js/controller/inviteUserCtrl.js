// Created By - Arpit Pittie
var inviteUserPage = angular.module('inviteUserApp', ['ui-notification',
    'ngRoute', 'topbarApp', 'sidebarApp'
]);

// Controlller for sending the invite to a user
inviteUserPage
    .controller(
        'inviteUserCtrl',
        function($scope, $http, $window, Notification) {
            $scope.user = {};

            $scope.sendInvite = function() {
                $('#wrapper').hide();
                $('#spinner').show();

                // Request to send mail for Invitation
                $http({
                    method: 'POST',
                    url: '/Project-Authentication/user/sendInvitationToUser',
                    data: $scope.user,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function(response) {
                    $('#wrapper').show();
                    $('#spinner').hide();
                    if (response.status != 403) {
                        Notification.info({
                            message: "Invitation Send",
                            delay: 2000

                        });
                    } else {
                        Notification.info({
                            message: "User aleardy Exist",
                            delay: 2000
                        });
                    }
                    setTimeout(function() {
                        $window.location.href = 'index.html';
                    }, 2500);
                }).error(function(response) {
                    $('#wrapper').show();
                    $('#spinner').hide();
                    Notification.error({
                        message: "Couldn't establish connection",
                        delay: 2000
                    });
                });
            }
        });
