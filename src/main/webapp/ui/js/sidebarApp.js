var sidebarApp = angular.module('sidebarApp', ['dataShareFactory', 'dataFactory']);

sidebarApp.controller('sidebarCtrl', function($scope, $http, userDetails, utilityFunctions) {
    console.log("fsfs");
    $scope.currentUser = userDetails.getCurrentUser();
    $scope.resources = {};

    if($scope.currentUser.role != 'admin') {
    	$('#resourcesMgmt').hide();
    	if($scope.currentUser.role != 'res_admin') {
    		$('#pendingRequest').hide();
    	}
    } else {
        $http({
            method : 'GET',
            url : 'http://localhost:8080/Project-Authentication/resources/getAll',
            data : $scope.currentUser,
            headers : {'Content-Type': 'application/json'}
        }).success(function(response) {
            if(response.status == 403) {
                console.log(response.errorMessage);
            } else {
                $scope.currentUser.adminOfResources = response.data;
            }
        }).error(function(response) {
            alert("Connection Error");
        });
    }

    $scope.fetchBookings = function(resourceId) {
		console.log(resourceId);
		$scope.resources.resourceId = resourceId;
		console.log($scope.resources);
        $http({
            method : 'POST',
            url : 'http://localhost:8080/Project-Authentication/bookings/getPendingbookings',
            data : $scope.resources,
            headers : {'Content-Type': 'application/json'}
        }).success(function(response) {
            console.log(response);
            //console.log(response.data);
            // $scope.user.password = "";
            // if(response.status == 200 ) {
            // 	userDetails.setCurrentUser(response.data);
            // 	$window.location.href = 'admin/index.html';
            // } else {
            // 	$scope.user.password = "";
            // 	console.log(response.errorMessage);
            // }
        }).error(function(response) {
			console.log(response);
		});
	}

	$scope.fetchResourceDetails = function(resourceId) {
		console.log(resourceId);
	}
});
