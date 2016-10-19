var homePage = angular.module('homePage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp', 'dataFactory']);
var personalDetailsPage = angular.module('personalDetailsPage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

//if(sessionStorage.length == 0) {
//	window.location = "http://localhost:8080/Project-Authentication/";
//}

homePage.controller('bookingCtrl', function($scope, $http, $window, userDetails, utilityFunctions) {

	$scope.booking = {};
	$scope.booking.resourceDetails = {};
	$scope.booking.userDetails = {};
	$scope.booking.userDetails.employeeId = userDetails.getCurrentUser().employeeId;

		// datetime picker
		$scope.pickDateTime = function() {
			console.log("hellohello");
			$('#datePicker').datetimepicker({
				format: 'YYYY-MM-DD',
				minDate : new Date(),
				ignoreReadonly : true,

				icons : {
					up : "fa fa-chevron-circle-up",
					down : "fa fa-chevron-circle-down",
					next : 'fa fa-chevron-circle-right',
					previous : 'fa fa-chevron-circle-left',
					time : "fa fa-clock-o",
					date : "fa fa-calendar",
				}
			});

			$('#startTime,#endTime').datetimepicker({
				format : 'HH:mm:00',
				ignoreReadonly : true,
				stepping : 15,

				icons : {
					up : "fa fa-chevron-circle-up",
					down : "fa fa-chevron-circle-down",
					next : 'fa fa-chevron-circle-right',
					previous : 'fa fa-chevron-circle-left',
					time : "fa fa-clock-o",
					date : "fa fa-calendar",
				}
			});

		}
		//end of datetimepicker


		$scope.bookResource=function() {
			$scope.booking.date = $('#datePickerInput').val();
			$scope.booking.startTime = $('#startTimeInput').val();
			$scope.booking.endTime = $('#endTimeInput').val();
			$scope.booking.resourceDetails.resourceId = $('#resSelect :selected').val();
			$scope.booking.resourceDetails.resourceName = $('#resSelect :selected').text();
			console.log($scope.booking);

			$http({
	            method : 'POST',
	            url : 'http://localhost:8080/Project-Authentication/bookings/createBooking',
	            data : $scope.booking,
	            headers : {'Content-Type': 'application/json'}
	        }).success(function(response) {
	            console.log(response);
	            if(response.status == 200 ) {

	            	$window.location.href = 'index.html';
	            } else {
	            	console.log(response.errorMessage);
	            }
	        }).error(function(response) {
				alert("Connection Error");
			});
		}

});


homePage.controller('dashboardCtrl', function($rootScope, $scope, $http, $filter, userDetails, utilityFunctions) {
	console.log(userDetails.getCurrentUser());
	$scope.currentUser = userDetails.getCurrentUser();
	$scope.date = $filter('date')(new Date(), 'yyyy-MM-dd');
	$scope.allResources = {};

	$http({
		method : 'GET',
		url : 'http://localhost:8080/Project-Authentication/resources/getAll',
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
		
		$http({
			method : 'GET',
			url : 'http://localhost:8080/Project-Authentication/bookings/getApprovedBookings',
			headers : {'Content-Type': 'application/json'}
		}).success(function(response) {
			if(response.status == 400) {
				console.log(response.errorMessage);
			} else {
				$scope.allApprovedBookings = response.data;
				console.log($scope.allApprovedBookings);
			}
			$scope.showCalendar();
		}).error(function(response) {
			alert("Connection Error");
		});
		
		
    });

	$scope.showCalendar = function() {
		$('#calendar').fullCalendar({
			defaultView: 'agendaDay',
		   defaultDate: $scope.date,
		   editable: true,
		   selectable: true,
		   eventLimit: true, // allow "more" link when too many events
		   snapDuration: {minutes: 15},
		   timezone : 'Asia/Kolkata',
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
				   var startTime = $(this).attr('date')+'T'+$(this).attr('startTime')+'+05:30';
				   var endTime = $(this).attr('date')+'T'+$(this).attr('endTime')+'+05:30';
				   
				   events.push({
					   title: $(this).attr('title')+"\n"+$(this).attr('description'),
					   start: startTime, // will be parsed
					   end : endTime,
					   resourceId : $(res).attr('resourceId')
				   });
			   });
			   
			   $($scope.allApprovedBookings).each(function() {
				   var res = $(this).attr('resourceDetails');
				   var startTime = $(this).attr('date')+'T'+$(this).attr('startTime')+'+05:30';
				   var endTime = $(this).attr('date')+'T'+$(this).attr('endTime')+'+05:30';
				   
				   events.push({
					   title: $(this).attr('title')+"\n"+$(this).attr('description'),
					   start: startTime, // will be parsed
					   end : endTime,
					   resourceId : $(res).attr('resourceId')
				   });
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
