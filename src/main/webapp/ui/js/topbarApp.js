var topbarApp = angular.module('topbarApp', ['dataFactory', 'dataShareFactory']);

if(sessionStorage.length == 0) {
	localStorage.setItem('getSessionStorage', "Getting");
}

window.addEventListener('storage', function(event) {
	console.log(event);
	if (event.key == 'getSessionStorage') {
		// Some tab asked for the sessionStorage -> send it
		localStorage.setItem('user', sessionStorage.getItem('user'));
		localStorage.setItem('url', event.url);
		console.log("Topbar.js");
	}

	if (event.key == 'user' && !sessionStorage.length) {
		var data = JSON.parse(event.newValue),
					value;
		sessionStorage.setItem('user', localStorage.getItem('user'));
		var redirect = localStorage.getItem('url');
		localStorage.clear();

		window.location = redirect;
	}

	if(event.key == 'signout') {
		sessionStorage.clear();
		window.location = "index.html";
	}
});

topbarApp.controller('topbarCtrl', function($scope, $window, userDetails, utilityFunctions) {
    $scope.currentUser = userDetails.getCurrentUser();
    $scope.signOut = function() {
		console.log("Called");
		utilityFunctions.performSignOut();
	}
});
