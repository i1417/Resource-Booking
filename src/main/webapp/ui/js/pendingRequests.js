var pendingRequestsApp = angular.module('pendingRequestsApp',['ngRoute', 'dataShareFactory', 'dataFactory', 'topbarApp', 'sidebarApp']);

pendingRequestsApp.controller('pendingRequestCtrl', function($scope, $http, userDetails, utilityFunctions,$location) {
	
	$scope.resources={};
    $scope.response={};
    $scope.updateData={};
	var resourceId = utilityFunctions.getResourceDetails();
    $scope.currentUser = userDetails.getCurrentUser();
	
	$scope.fetchPendingBookings =  function() {
	
		$scope.resources.resourceId = resourceId;
		$http({
            method : 'POST',
            url : 'http://localhost:8080/Project-Authentication/bookings/getPendingbookings',
            data : $scope.resources,
            headers : {'Content-Type': 'application/json'}
        }).success(function(response) {
        	if(response.data==null || response.status==400){
        		$('#no-pending-requests').show();
        		$('#pending-request-table').hide();
        	}else{
        		$('#no-pending-requests').hide();
        		$('#pending-request-table').show();
        	}
           $scope.response=response.data;
            //console.log(response);
       }).error(function(response) {
			console.log(response);
		});
	}
	
	//method to update pending request status--Approved or Rejected
	$scope.updateRequest  =  function(bookingId,employeeId,employeeName,email,bookingStatus){
		    
		$scope.updateData.bookingId=bookingId;
		$scope.updateData.userDetails={};
		$scope.updateData.userDetails.employeeId=employeeId;
		$scope.updateData.userDetails.name=employeeName;
		$scope.updateData.userDetails.email=email;
		$scope.updateData.status=bookingStatus;
		
		$http({
            method : 'POST',
            url : 'http://localhost:8080/Project-Authentication/bookings/updateBookingsStatus',
            data : $scope.updateData,
            headers : {'Content-Type': 'application/json'}
        })
		.success(
				function(data, status, headers,	config) {
					if (status == 200) {
						alert('Successfully '+bookingStatus);
						$scope.fetchPendingBookings($scope.resources.resourceId );
					} else {
						alert('Can\'t be '+bookingStatus);
					}
				}).error(
				function(data, status, headers,	config) {
					alert('Error'+status);
				});
};
	
	 angular.element(document).ready(function () {
		$scope.fetchPendingBookings();
	    });
	
});