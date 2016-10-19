var resourceEdit = angular.module('resourceEditApp', ['ngRoute', 'dataFactory', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

resourceEdit.controller('resourceEditCtrl', function($scope, $http, $filter, utilityFunctions, userDetails) {
//    $scope.resource = {};
    $scope.resource = utilityFunctions.getResourceDetails();
    console.log(utilityFunctions.getResourceDetails());

    $http({
		method : 'GET',
		url : 'http://localhost:8080/Project-Authentication/users/getAll',
		data : $scope.currentUser,
		headers : {'Content-Type': 'application/json'}
	}).success(function(response) {
		if(response.status == 403) {
			console.log(response.errorMessage);
		} else {
            $scope.allUsers = response.data;
            $scope.determineAdmin();
            console.log(response);
		}
	}).error(function(response) {
		alert("Connection Error");
	});

    $scope.determineAdmin = function() {
        $scope.currentUser = userDetails.getCurrentUser();
        if($scope.resource != null) {
            angular.forEach($scope.resource.resourceAdmins, function(obj) {
            	var foundItem = $filter('filter')($scope.allUsers, { email: obj.email  }, true)[0];
            	var index = $scope.allUsers.indexOf(foundItem);
            	$scope.allUsers.splice(index, 1);
            });

            $('#resType').hide();
        } else {
            var unique = {};
            $scope.distinct = [];
            $scope.currentUser.adminOfResources.forEach(function (x) {
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

        var foundItem = $filter('filter')($scope.allUsers, { email: $scope.currentUser.email  }, true)[0];
        var index = $scope.allUsers.indexOf(foundItem);
        $scope.allUsers.splice(index, 1);
    }

    $scope.removeAdmin = function() {
        var index = $("#resourceAdmin option:selected").index();
        $scope.allUsers.push($scope.resource.resourceAdmins[index]);
        $scope.resource.resourceAdmins.splice(index, 1);
    }

    $scope.addAdmin = function() {
        var index = $("#userList option:selected").index();
        $scope.resource.resourceAdmins.push($scope.allUsers[index]);
        $scope.allUsers.splice(index, 1);
    }

    $scope.updateResource = function() {
        console.log($scope.resource);
        if($scope.currentUser == undefined) {
            $http({
        		method : 'POST',
        		url : 'http://localhost:8080/Project-Authentication/resources/editResource',
        		data : $scope.resource,
        		headers : {'Content-Type': 'application/json'}
        	}).success(function(response) {
                console.log(response);
        		// if(response.status == 403) {
        		// 	console.log(response.errorMessage);
        		// } else {
                //     $scope.allUsers = response.data;
                //     $scope.determineAdmin();
                //     console.log(response);
        		// }
        	}).error(function(response) {
        		alert("Connection Error");
        	});
        }
    }
});
