angular.module('dataShareFactory',[]).factory('userDetails', function($window) {
    var userFunctions = {};

    var user = [];

    userFunctions.setUser = function(data) {
        user.push(data);
    }

    userFunctions.getUser = function() {
        return user.pop();
    }

    userFunctions.setCurrentUser = function(data) {
        $window.sessionStorage.setItem('user', angular.toJson(data));
        // currentUser.push(data);
        //console.log(JSON.parse($window.sessionStorage.getItem('user')));
    }

    userFunctions.getCurrentUser = function() {
        return JSON.parse($window.sessionStorage.getItem('user'));
        // return currentUser.pop();
    }

    return userFunctions;
});
