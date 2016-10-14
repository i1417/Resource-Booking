var sidebarApp = angular.module('sidebarApp', ['dataShareFactory']);

sidebarApp.controller('sidebarCtrl', function($scope, userDetails) {
    $scope.currentUser = userDetails.getCurrentUser();
    $scope.fetchBookings = function(resourceId) {
		console.log(resourceId);
	}

	$scope.fetchResourceDetails = function(resourceId) {
		console.log(resourceId);
	}
});
