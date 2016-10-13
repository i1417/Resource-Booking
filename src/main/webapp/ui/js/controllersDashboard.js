var homePage = angular.module('homePage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp', 'dataFactory']);
var personalDetailsPage = angular.module('personalDetailsPage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

if(sessionStorage.length == 0) {
	window.location = "http://localhost:8080/Project-Authentication/";
}

homePage.controller('dashboardCtrl', function($rootScope, $scope, $http, $filter, userDetails, utilityFunctions) {
	console.log(userDetails.getCurrentUser());
	$scope.currentUser = userDetails.getCurrentUser();
	$scope.date = $filter('date')(new Date(), 'yyyy-MM-dd');
	$scope.allResources = {};

	$http({
		method : 'GET',
		url : 'http://localhost:8080/Project-Authentication/resources/getAll',
		data : $scope.currentUser,
		headers : {'Content-Type': 'application/json'}
	}).success(function(response) {
		if(response.status == 403) {
			console.log(response.errorMessage);
		} else {
			utilityFunctions.setAllResources(response.data);
			$rootScope.$emit("populateResources", {});
		}
	}).error(function(response) {
		alert("Connection Error");
	});

});

homePage.controller('calendarCtrl', function($rootScope, $scope, $http, utilityFunctions) {
	$rootScope.$on("populateResources", function(){
       $scope.allResources = utilityFunctions.getAllResources();
       console.log("Called");
	   $scope.showCalendar();
    });

	$scope.showCalendar = function() {
		$('#calendar').fullCalendar({
			defaultView: 'agendaDay',
		   defaultDate: $scope.date,
		   editable: true,
		   selectable: true,
		   eventLimit: true, // allow "more" link when too many events
		   snapDuration: {minutes: 15},
		   header: {
			   left: 'prev,next today',
			   center: 'title',
			   right: 'agendaDay,agendaTwoDay,agendaWeek,month'
		   },
		   views: {
			   agendaTwoDay: {
				   type: 'agenda',
				   duration: { days: 2 },

				   // views that are more than a day will NOT do this behavior by default
				   // so, we need to explicitly enable it
				   groupByResource: true

				   //// uncomment this line to group by day FIRST with resources underneath
				   //groupByDateAndResource: true
			   }
		   },

		   //// uncomment this line to hide the all-day slot
		   //allDaySlot: false,

		   resources: function(reply) {
			   var resources = [];
			   $($scope.allResources).each(function() {
 				  resources.push({
 					  id : $(this).attr('resourceId'),
 					  title : $(this).attr('resourceName'),
 					  eventColor : 'black'
 				  });
 			  });
			   reply(resources);
		   },
		   events: function(start, end, timezone, callback) {
			   var events = [];
//			   console.log($($scope.currentUser.bookingsMade).length);
			   $($scope.currentUser.bookingsMade).each(function() {
				   var res = $(this).attr('resourceDetails');
				   var startTime = $(this).attr('date')+'T'+$(this).attr('startTime');
				   var endTime = $(this).attr('date')+'T'+$(this).attr('endTime');
				   events.push({
					   title: $(this).attr('bookingId'),
					   start: startTime, // will be parsed
					   end : endTime,
					   resourceId : $(res).attr('resourceId')
				   });
				   console.log(events);
			   });
			   callback(events);
		   },

		   select: function(start, end, jsEvent, view, resource) {
			   console.log(
				   'select',
				   start.format(),
				   end.format(),
				   resource ? resource.id : '(no resource)'
			   );
		   },
//		   dayClick: function(date, jsEvent, view, resource) {
//			   console.log(
//				   'dayClick',
//				   date.format(),
//				   resource ? resource.id : '(no resource)'
//			   );
//		   },
		   eventClick: function(event, element) {

			   console.log(event);

			   $('#calendar').fullCalendar('updateEvent', event);

		   }
		});
	}
});

personalDetailsPage.controller('personalCtrl', function($scope, $http, $window, userDetails) {
	console.log(userDetails.getCurrentUser());
	$scope.currentUser = userDetails.getCurrentUser();
	$scope.updateDetails = function() {
		$http({
			method : 'POST',
			url : 'http://localhost:8080/Project-Authentication/user/update',
			data : $scope.currentUser,
			headers : {'Content-Type': 'application/json'}
		}).success(function(response) {
			if(response.status == 403) {
				console.log(response.errorMessage);
			} else {
				userDetails.setCurrentUser(response.data);
				$scope.currentUser = userDetails.getCurrentUser();
				$window.location.href = 'index.html';
			}
		}).error(function(response) {
			alert("Connection Error");
		});
	}


});
