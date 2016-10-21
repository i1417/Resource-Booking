angular.module('dataShareFactory',[]).factory('userDetails', function($window, $filter) {
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
    
    userFunctions.addCurrentBooking = function(data) {
    	var user = JSON.parse($window.sessionStorage.getItem('user'));
    	user.bookingsMade.push(data);
    	$window.sessionStorage.setItem('user', angular.toJson(user));
    }
    
    userFunctions.editCurrentBooking = function(data) {
    	var user = JSON.parse($window.sessionStorage.getItem('user'));
    	
    	 var foundItem = $filter('filter')(user.bookingsMade, { bookingId: data.bookingId }, true)[0];
		 var index = user.bookingsMade.indexOf(foundItem);
		 user.bookingsMade.splice(index, 1);
    	 
    	 if(data.status == "Approved"){
    		 user.bookingsMade.push(data);
    	 }
    	 $window.sessionStorage.setItem('user', angular.toJson(user));
    }

    return userFunctions;
});
