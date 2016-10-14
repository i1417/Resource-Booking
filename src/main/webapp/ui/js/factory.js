angular.module('dataShareFactory',[]).factory('userDetails', function($window) {
    var userFunctions = {};

    var user = [];

    var currentUser = {};

    userFunctions.setUser = function(data) {
        user.push(data);
    }

    userFunctions.getUser = function() {
        return user.pop();
    }

    var currentUser = [];

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

/*landingPage.factory('currentUserDetails', function() {
    var userDataFunction = {};
    var currentUser = [];

    userDataFunction.setCurrentUser = function(data) {
        currentUser.push(data);
    }

    userDataFunction.getCurrentUser = function() {
        return currentUser.pop();
    }

    console.log(currentUser.length);
    return userDataFunction;
});*/
