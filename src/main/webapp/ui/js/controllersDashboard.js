var homePage = angular.module('homePage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp']);
var personalDetailsPage = angular.module('personalDetailsPage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

if(sessionStorage.length == 0) {
	window.location = "http://localhost:8080/Project-Authentication/";
}

homePage.controller('dashboardCtrl', function($scope, $filter, userDetails) {
	console.log(userDetails.getCurrentUser());
	$scope.currentUser = userDetails.getCurrentUser();
	$scope.date = $filter('date')(new Date(), 'yyyy-MM-dd');
	$('#calendar').fullCalendar({
        defaultView: 'agendaDay',
		defaultDate: $scope.date,
		editable: true,
		selectable: true,
		eventLimit: true, // allow "more" link when too many events
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'agendaDay,agendaFourDay,agendaWeek,month'
		},
		views: {
			agendaFourDay: {
				type: 'agenda',
				duration: { days: 4 },
                snapDuration: {minutes: 15},

				// views that are more than a day will NOT do this behavior by default
				// so, we need to explicitly enable it
				groupByResource: true

				//// uncomment this line to group by day FIRST with resources underneath
				//groupByDateAndResource: true
			}
		},

		//// uncomment this line to hide the all-day slot
		//allDaySlot: false,

		resources: function(callback) {
		        callback($scope.currentUser.adminOfResources);
		},
		events: [
			{ id: '1', resourceId: 'a', start: '2016-09-06', end: '2016-09-08', title: 'event 1' },
			{ id: '2', resourceId: 'a', start: '2016-09-07T09:00:00', end: '2016-09-07T14:00:00', title: 'event 2' },
			{ id: '3', resourceId: 'b', start: '2016-09-07T12:00:00', end: '2016-09-08T06:00:00', title: 'event 3' },
			{ id: '4', resourceId: 'c', start: '2016-09-07T07:30:00', end: '2016-09-07T09:30:00', title: 'event 4' },
			{ id: '5', resourceId: 'd', start: '2016-09-07T10:00:00', end: '2016-09-07T15:00:00', title: 'event 5' }
		],

		select: function(start, end, jsEvent, view, resource) {
			console.log(
				'select',
				start.format(),
				end.format(),
				resource ? resource.id : '(no resource)'
			);
		},
		dayClick: function(date, jsEvent, view, resource) {
			console.log(
				'dayClick',
				date.format(),
				resource ? resource.id : '(no resource)'
			);
		}
	});
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
			//console.log(response);
			if(response.status == 403) {
				console.log(response.errorMessage);
			} else {
				userDetails.setCurrentUser(response.data);
				$scope.currentUser = userDetails.getCurrentUser();
				$window.location.href = 'index.html';
				//console.log(response.errorMessage);
			}
		}).error(function(response) {
			alert("Connection Error");
		});
	}


});
