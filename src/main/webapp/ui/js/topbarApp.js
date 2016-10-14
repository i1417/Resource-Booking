var topbarApp = angular.module('topbarApp', ['dataFactory', 'dataShareFactory']);

topbarApp.controller('topbarCtrl', function($scope, $window, userDetails, utilityFunctions) {
    $scope.currentUser = userDetails.getCurrentUser();
    $scope.signOut = function() {
		console.log("Called");
		utilityFunctions.performSignOut();
	}
});
