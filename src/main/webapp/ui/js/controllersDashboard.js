var homePage = angular.module('homePage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp', 'dataFactory','ui.bootstrap']);
var personalDetailsPage = angular.module('personalDetailsPage', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp']);


var bookingCtrl = function($scope, $http, $window, $modal, $modalInstance, userDetails, utilityFunctions,itemObj) { 
	$scope.booking = {};
	$scope.booking.resourceDetails = {};
	$scope.booking.userDetails = {};
	$scope.booking.userDetails.employeeId = userDetails.getCurrentUser().employeeId;
	$scope.booking.userDetails.email =  userDetails.getCurrentUser().email;
	$scope.booking.userDetails.name = userDetails.getCurrentUser().name;
	$scope.allResources = utilityFunctions.getAllResources();
	
	 $scope.sTime= itemObj.startTime;
	 $scope.eTime= itemObj.endTime;
	 $scope.dateFormat= itemObj.dateFormat;
	 $scope.booking.title= itemObj.title;
	 $scope.booking.description= itemObj.description;
	 $scope.booking.numberOfParticipants = itemObj.numberOfParticipants;
	 $scope.resourceValue = itemObj.resourceId.toString();
	
	console.log(itemObj.bookBtn); 
	
	if(angular.isUndefined(itemObj.bookBtn)){
		$scope.truefalse = false;
		$scope.bookBtn="Book";
		$scope.urlValue= "http://localhost:8080/Project-Authentication/bookings/createBooking";
	}
	else if(itemObj.bookBtn == "new"){
		$scope.truefalse = true;
		$scope.bookBtn="Book";
		$scope.urlValue= "http://localhost:8080/Project-Authentication/bookings/createBooking";
	}else{
		$scope.truefalse = true;
		$scope.bookBtn = "Edit Booking";
		console.log("else edit");
		//$scope.urlValue= "http://localhost:8080/Project-Authentication/bookings/editBooking";
	}
	
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
	            url : $scope.urlValue,
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
			
			
			$('#startTime').datetimepicker({
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
			}).on('dp.change',function(event){
				$('#endTime').data('DateTimePicker').minDate(event.date);
			});
			 
			$('#endTime').datetimepicker({
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
					   title: $(this).attr('title')+"\n"+$(this).attr('description')+"\nParticipants:  \b"+$(this).attr('numberOfParticipants'),
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
					   title: $(this).attr('title')+"\n"+$(this).attr('description')+"\nParticipants:  \b"+$(this).attr('numberOfParticipants'),
					   start: startTime, // will be parsed
					   end : endTime,
					   resourceId : $(res).attr('resourceId')
				   });
			   });
			   callback(events);
		   },

		   select: function(start, end, jsEvent, view, resource) {
			   var startT= start.format().substring(11);
			   var endT =  end.format().substring(11);
			   var dateFormat = start.format().substring(0,10);
			   if(!angular.isUndefined(resource)){
				   var resourceId = resource.id;
			   }else{
				   var resourceId = "";
			   }
			   
			   var title="";
			   var description="";
			   var bookBtn = "new";
			   var numberOfParticipants = "";
			
			   if($scope.checkDate(dateFormat)){
				   $scope.showModal(startT, endT, dateFormat,title,description, numberOfParticipants, resourceId, bookBtn);
			   }else{
				   console.log("Can't book at this date");
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
		        
		   eventClick: function(calEvent) {
			  $scope.callShowModal(calEvent);
		   },
			
			eventDrop: function(event, revertFunc) {		        
				  $scope.callShowModal(event);
		    },
		    
		    eventResize: function(event) { 
				  $scope.callShowModal(event);
		    }	
		});
	}
	
	$scope.callShowModal= function(event){
		var startT = event.start.format().substring(11,19);
		   var endT = event.end.format().substring(11,19);
		   var dateFormat = event.start.format().substring(0,10);
		   var resourceId = event.resourceId;
		   var title = event.title.substring(0,event.title.indexOf('\n'));
		   var description = event.title.substring(event.title.indexOf('\n'), event.title.indexOf('\nP'));
		   //var numberOfParticipants = event.title.substring(event.title.indexOf('\b'));
		   var numberOfParticipants = 1;
		   var bookBtn= "edit";
		   $(this).css('border-color', 'yellow');
		   $scope.showModal(startT,endT,dateFormat,title,description,numberOfParticipants,resourceId, bookBtn);
	}
	
	$scope.checkDate = function(date){
		console.log(date);
		var myDate = date;   
		 if (myDate < $scope.date) {  
               return false;
           }
		 else{
			 return true;
		 }
	}
	
	
	$scope.showModal = function(start, end, dateFormat, title, description,numberOfParticipants, resource, bookBtn) {
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
            			startTime: start,
                		endTime: end,
                		dateFormat: dateFormat,
                		title: title,
                		description: description,
                		numberOfParticipants: numberOfParticipants,
                		resourceId : resource,
                		bookBtn : bookBtn,
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
