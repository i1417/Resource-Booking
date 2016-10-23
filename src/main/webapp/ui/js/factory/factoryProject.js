// Created By - Arpit Pittie, Rohit Singhavi
angular.module('dataShareFactory', []).factory('userDetails', function($window, $filter) {
    var userFunctions = {};

    var user = [];

    // To set the user details got by Google Login
    userFunctions.setUser = function(data) {
        user.push(data);
    }

    // To get the user details provided by the Google Login
    userFunctions.getUser = function() {
        return user.pop();
    }

    // To set the current user details
    userFunctions.setCurrentUser = function(data) {
        $window.sessionStorage.setItem('user', angular.toJson(data));
    }

    // To get the current user details
    userFunctions.getCurrentUser = function() {
        return JSON.parse($window.sessionStorage.getItem('user'));
    }

    //
    // userFunctions.addCurrentBooking = function(data) {
    //     var user = JSON.parse($window.sessionStorage.getItem('user'));
    //     console.log(data.status);
    //     if (data.status == "Approved") {
    //         user.bookingsMade.push(data);
    //     }
    //     $window.sessionStorage.setItem('user', angular.toJson(user));
    // }
    //
    // userFunctions.editCurrentBooking = function(data) {
    //     var user = JSON.parse($window.sessionStorage.getItem('user'));
    //
    //     var foundItem = $filter('filter')(user.bookingsMade, {
    //         bookingId: data.bookingId
    //     }, true)[0];
    //     var index = user.bookingsMade.indexOf(foundItem);
    //     user.bookingsMade.splice(index, 1);
    //
    //     if (data.status == "Approved") {
    //         user.bookingsMade.push(data);
    //     }
    //     $window.sessionStorage.setItem('user', angular.toJson(user));
    // }

    return userFunctions;
});
