var resourceEdit = angular.module('resourceEditApp', ['ui-notification', 'ngRoute', 'utilityFunctionsFactory', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

resourceEdit.controller('resourceEditCtrl', function($scope, $http, $window, $filter, utilityFunctions, userDetails, Notification) {
    $scope.resource = utilityFunctions.getResourceDetails();

    if ($scope.resource == null) {
        $scope.heading = 'New Resource';
    } else {
        $scope.heading = 'Edit Resource';
    }

    $http({
        method: 'GET',
        url: 'http://localhost:8080/Project-Authentication/users/getAll',
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

    $scope.determineAdmin = function() {
        $scope.currentUser = userDetails.getCurrentUser();
        if ($scope.resource != null) {
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

        var foundItem = $filter('filter')($scope.allUsers, {
            email: $scope.currentUser.email
        }, true)[0];

        var index = $scope.allUsers.indexOf(foundItem);
        
        $scope.allUsers.splice(index, 1);
    }

    $scope.removeAdmin = function() {
        var index = $("#resourceAdmin option:selected").index();
        $scope.allUsers.push($scope.resource.resourceAdmins[index]);
        $scope.resource.resourceAdmins.splice(index, 1);

        $('#addAdminBtn').attr('disabled', false);
        if ($scope.resource.resourceAdmins.length == 0) {
            $('#removeAdminBtn').attr('disabled', true);
        }
    }

    $scope.addAdmin = function() {
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
        if ($scope.allUsers.length == 0) {
            $('#addAdminBtn').attr('disabled', true);
        }
    }

    $scope.updateResource = function() {
        if (utilityFunctions.getResourceDetails() == null) {
            $http({
                method: 'POST',
                url: 'http://localhost:8080/Project-Authentication/resources/createResource',
                data: $scope.resource,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                Notification.success({
                    message: "Resource Creation Successfull",
                    delay: 2000
                });
                setTimeout(function() {
                    $window.location.href = 'index.html';
                }, 2500);
            }).error(function(response) {
                Notification.error({
                    message: "Couldn't establish connection",
                    delay: 2000
                });
            });
        } else {
            $http({
                method: 'POST',
                url: 'http://localhost:8080/Project-Authentication/resources/editResource',
                data: $scope.resource,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                Notification.success({
                    message: "Resource Edit Successfull",
                    delay: 2000
                });
                setTimeout(function() {
                    $window.location.href = 'index.html';
                }, 2500);
            }).error(function(response) {
                Notification.error({
                    message: "Couldn't establish connection",
                    delay: 2000
                });
            });
        }
    }
});
