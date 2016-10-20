var homePage = angular.module('homePage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp', 'dataFactory','ui.bootstrap']);
var personalDetailsPage = angular.module('personalDetailsPage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp']);

//if(sessionStorage.length == 0) {
//	window.location = "http://localhost:8080/Project-Authentication/";
//}


var bookingCtrl = function($scope, $http, $window, $modal, $modalInstance, userDetails, utilityFunctions,itemObj) {

	console.log(itemObj);
	
	 $scope.sTime= itemObj.startTime.substring(11);
	 $scope.eTime= itemObj.endTime.substring(11);
	 $scope.dateFormat= itemObj.startTime.substring(0,10);
	$scope.resourceValue = itemObj.resourceId; 
	 
	 /* $('#resSelect option[value="100"]').prop('selected', true);*/
	 
	$scope.booking = {};
	$scope.booking.resourceDetails = {};
	$scope.booking.userDetails = {};
	$scope.booking.userDetails.employeeId = userDetails.getCurrentUser().employeeId;
	$scope.allResources = utilityFunctions.getAllResources();

	
	$scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

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
		
		// datetime picker
		$scope.pickDateTime = function() {
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
};



homePage.controller('dashboardCtrl', function($rootScope, $scope, $modal, $http, $filter, userDetails, utilityFunctions) {
	console.log(userDetails.getCurrentUser());
	$scope.currentUser = userDetails.getCurrentUser();
	$scope.date = $filter('date')(new Date(), 'yyyy-MM-dd');
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
	
	
	
	   /*To open the modal dialog box*/
	$scope.showModal = function() {
        /*Setting the modal options*/
        $scope.opts = {
            backdrop: true,
            dialogFade: false,
            backdropClick: true,
            keyboard: true,
            templateUrl : 'bookingForm.html',
            controller : bookingCtrl,
            resolve: {
            	itemObj: function(){
            		return {
            			startTime: "",
                		endTime: "",
                		resourceId : ""
            		};
            	}
            }
        };

        /*Opening up the modal*/
        var modalInstance = $modal.open($scope.opts);
    }

});

homePage.controller('calendarCtrl', function($rootScope, $scope, $http, $modal, utilityFunctions) {
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
			   if($scope.checkDate(start.format().substring(0,10))){
				   $scope.showModal(start, end, resource);
			   }else{
				   console.log("can't book at this date");
			   }
			  
		   },
		   
		   eventAllow: function(dropLocation, draggedEvent) {
			   if (dropLocation.resourceId == draggedEvent.resourceId) {
			        return true;
			    }
			    else {
			        return false;
			    }
			},
		  
			 /*dayClick: function(date, allDay, jsEvent, view) {
				 console.log(jsEvent);
				 if($scope.checkDate(date)){
					   $scope.showModal(start, end, resource);
					 console.log("please");
				   }else{
					   console.log("cant book at this date");
				   }
				 
		        },*/
		        
		   eventClick: function(event, element) {
			   console.log(event);
			   $('#calendar').fullCalendar('updateEvent', event);

		   }
		});
	}
	
	$scope.checkDate = function(date){
		console.log(date);
		var myDate = date;   
		 if (myDate < $scope.date) {  
			 console.log('here');
               return false;
           }
		 else{
			 return true;
		 }
	}
	
	
	$scope.showModal = function(start, end, resource) {
        /*Setting the modal options*/
		
        $scope.opts = {
            backdrop: true,
            dialogFade: false,
            backdropClick: true,
            keyboard: true,
            templateUrl : 'bookingForm.html',
            controller : bookingCtrl,
            resolve: {
            	itemObj: function(){
            		return {
            			startTime: start.format(),
                		endTime: end.format(),
                		resourceId : resource.id
            		};
            	}
            }
        };
       

        /*Opening up the modal*/
        var modalInstance = $modal.open($scope.opts);
        
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