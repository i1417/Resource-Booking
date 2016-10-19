var sidebarApp = angular.module('sidebarApp', ['dataShareFactory', 'dataFactory']);

sidebarApp.controller('sidebarCtrl', function($scope, $http, $window, userDetails, utilityFunctions) {
  
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
                userDetails.setCurrentUser($scope.currentUser);
                console.log(response);
            }
        }).error(function(response) {
            alert("Connection Error");
        });
    }
    
    // approve or reject existing booking 
    $scope.updatePendingRequest = function(resourceId) {
    	      	utilityFunctions.setResourceDetails(resourceId);
    	       	$window.location.href = "pendingRequests.html";
    	       	
    }

	$scope.fetchResourceDetails = function(resource) {
        //console.log(resource);
		utilityFunctions.setResourceDetails(resource);
        console.log(utilityFunctions.getResourceDetails());
		$window.location.href = "resourceEdit.html";
	}
	
	$scope.newResource = function() {
        utilityFunctions.setResourceDetails(null);
        $window.location.href = "resourceEdit.html";
    }
});