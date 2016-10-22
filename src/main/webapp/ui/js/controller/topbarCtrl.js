var topbarApp = angular.module('topbarApp', ['utilityFunctionsFactory', 'dataShareFactory']);

if (sessionStorage.length == 0) {
    localStorage.setItem('getSessionStorage', "Getting");
    window.location = 'http://localhost:8080/Project-Authentication/';
}

window.addEventListener('storage', function(event) {
    if (event.key == 'getSessionStorage') {
        // Some tab asked for the sessionStorage -> send it
        localStorage.setItem('user', sessionStorage.getItem('user'));
        localStorage.setItem('url', event.url);
    }

    if (event.key == 'user' && !sessionStorage.length) {
        var data = JSON.parse(event.newValue);
        sessionStorage.setItem('user', localStorage.getItem('user'));
        var redirect = localStorage.getItem('url');
        localStorage.clear();

        window.location = redirect;
    }

    if (event.key == 'signout') {
        sessionStorage.clear();
        window.location = "index.html";
    }
});

topbarApp.controller('topbarCtrl', function($scope, $window, userDetails, utilityFunctions) {
    $scope.currentUser = userDetails.getCurrentUser();

    $scope.onLoad = function() {
        gapi.load('auth2', function() {
            gapi.auth2.init();
        });
    }

    $scope.logOut = function() {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function() {
            sessionStorage.clear();
            $window.location.href = 'http://localhost:8080/Project-Authentication/';
        });

    }
});
