// Created By - Arpit Pittie
var resourceEdit = angular.module('resourceEditApp', ['ui-notification', 'ngRoute', 'utilityFunctionsFactory', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

// Controller to edit and create a new resource
resourceEdit.controller('resourceEditCtrl', function($scope, $http, $window, $filter, utilityFunctions, userDetails, Notification) {
    $scope.resource = utilityFunctions.getResourceDetails();

    // Checking if the reuest is for new resource or edit resource
    if ($scope.resource == null) {
        $scope.heading = 'New Resource';
    } else {
        $scope.heading = 'Edit Resource';
    }

    // Request to get all the user list
    $http({
        method: 'GET',
        url: '/Resource-Booking/user/getAll',
        data: $scope.currentUser,
        headers: {
            'Content-Type': 'application/json'
        }
    }).success(function(response) {
        if (response.status == 200) {
            $scope.allUsers = response.data;
            $scope.determineAdmin();
        }
    }).error(function(response) {
        Notification.error({
            message: "Couldn't establish connection",
            delay: 2000
        });
    });

    // To determine which user are already user Admins for the resource
    $scope.determineAdmin = function() {
        $scope.currentUser = userDetails.getCurrentUser();
        if ($scope.resource != null) {
            // Removing the resource admins from the users list
            angular.forEach($scope.resource.resourceAdmins, function(obj) {
                var foundItem = $filter('filter')($scope.allUsers, {
                    email: obj.email
                }, true)[0];
                var index = $scope.allUsers.indexOf(foundItem);
                $scope.allUsers.splice(index, 1);
            });

            $('#resType').hide();
        } else {
            var unique = {};
            $scope.distinct = [];
            // Getting distinct typpe of resource types
            $scope.currentUser.adminOfResources.forEach(function(x) {
                if (!unique[x.type]) {
                    $scope.distinct.push(x.type);
                    unique[x.type] = true;
                }
            });
            $('button[type="submit"]').html("Save");
            $('#resAdmin').prop('required', 'true');
            $scope.resource = {};
            $scope.resource.resourceAdmins = [];
        }

        // Removing the admin from the list
        var foundItem = $filter('filter')($scope.allUsers, {
            email: $scope.currentUser.email
        }, true)[0];

        var index = $scope.allUsers.indexOf(foundItem);

        $scope.allUsers.splice(index, 1);
    }

    // To remove a user from the resource admin list
    $scope.removeAdmin = function() {
        var index = $("#resourceAdmin option:selected").index();
        $scope.allUsers.push($scope.resource.resourceAdmins[index]);
        $scope.resource.resourceAdmins.splice(index, 1);

        $('#addAdminBtn').attr('disabled', false);
        // Disabling the delete button if no resource admin left
        if ($scope.resource.resourceAdmins.length == 0) {
            $('#removeAdminBtn').attr('disabled', true);
        }
    }

    // To add a user to the resource admin list
    $scope.addAdmin = function() {
        // Checking if admin has selectd a user or not
        if ($("#userList option:selected").index() >= 0) {
            var index = $("#userList option:selected").index();
            $scope.resource.resourceAdmins.push($scope.allUsers[index]);
            $scope.allUsers.splice(index, 1);
        } else {
            Notification.warning({
                message: "Select a user",
                delay: 2000
            });
        }

        $('#removeAdminBtn').attr('disabled', false);
        // Disabling the add button if no user left
        if ($scope.allUsers.length == 0) {
            $('#addAdminBtn').attr('disabled', true);
        }
    }

    // To update or create the resourcee
    $scope.updateResource = function() {
    	$('#wrapper').hide();
        $('#spinner').show();
        // Checking if the resource is for edit or creation
        if (utilityFunctions.getResourceDetails() == null) {
            // Request to create a resource
            $http({
                method: 'POST',
                url: '/Resource-Booking/resources/createResource',
                data: $scope.resource,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
            	$('#wrapper').show();
                $('#spinner').hide();
                Notification.success({
                    message: "Resource Creation Successfull",
                    delay: 2000
                });
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
        } else {
            // Request to update a resource
            $http({
                method: 'POST',
                url: '/Resource-Booking/resources/editResource',
                data: $scope.resource,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
            	$('#wrapper').show();
                $('#spinner').hide();
                Notification.success({
                    message: "Resource Edit Successfull",
                    delay: 2000
                });
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
    }
});
