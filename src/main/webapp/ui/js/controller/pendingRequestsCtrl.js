// Created By - Amit Sharma
var pendingRequestsApp = angular.module('pendingRequestsApp', ['ui-notification', 'ngRoute', 'dataShareFactory', 'utilityFunctionsFactory', 'topbarApp', 'sidebarApp','ui.bootstrap']);

// Controller To handle the pending request resource specific
pendingRequestsApp.controller('pendingRequestCtrl', function($scope, $http, userDetails, utilityFunctions, $location, Notification) {

    $scope.resources = {};
    $scope.response = {};
    $scope.updateData = {};
    var resourceToBeFetched = utilityFunctions.getResourceDetails();
    var resourceId=resourceToBeFetched.resourceId;
    $scope.resourceName=resourceToBeFetched.resourceName;
    $scope.currentUser = userDetails.getCurrentUser();
    	 
    // To fetch all the pending request for the resource
    $scope.fetchPendingBookings = function() {
    	
      //pagination 
      $scope.currentPage = 4;
   	  $scope.itemsPerPage = 2;//items per page
   	  $scope.maxSize = 5; //Number of pager buttons to show
   	  
        $scope.resources.resourceId = resourceId;
        $('#wrapper').hide();
        $('#spinner').show();
        // Request fetch the pending booking for that resource
        $http({
            method: 'POST',
            url: '/Resource-Booking/bookings/getPendingbookings',
            data: $scope.resources,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            $('#wrapper').show();
            $('#spinner').hide();
            if (response.data == null || response.status == 400 || response.data.length == 0) {
                $('#no-pending-requests').show();
                $('#pending-request-table').hide();
            } else {
            	 $scope.totalItems = response.data.length;
            	 $('#no-pending-requests').hide();
                $('#pending-request-table').show();
            }
            $scope.response = response.data;
        }).error(function(response) {
            $('#wrapper').show();
            $('#spinner').hide();
            Notification.error({
                message: "Couldn't establish connection",
                delay: 2000
            });
        });
    }

    //method to update pending request status--Approved or Rejected
    $scope.updateRequest = function(bookingId, employeeId, employeeName, email, mobileNumber, bookingStatus) {

    	alert(mobileNumber);
        $scope.updateData.bookingId = bookingId;
        $scope.updateData.userDetails = {};
        $scope.updateData.userDetails.employeeId = employeeId;
        $scope.updateData.userDetails.name = employeeName;
        $scope.updateData.userDetails.email = email;
        $scope.updateData.userDetails.mobileNumber = mobileNumber;
        $scope.updateData.status = bookingStatus;

        $('#wrapper').hide();
        $('#spinner').show();
        // Checking if the status is approved
        if ($scope.updateData.status == 'Approved') {
            // Request to approve the particular booking
            $http({
                method: 'POST',
                url: '/Resource-Booking/bookings/updateBookingsStatusApproved',
                data: $scope.updateData,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                $('#wrapper').show();
                $('#spinner').hide();
                if (response.status == 200) {
                    Notification.success({
                        message: 'Successfully ' + bookingStatus,
                        delay: 2000
                    });
                    $scope.fetchPendingBookings($scope.resources.resourceId);
                } else {
                    Notification.info({
                        message: "Can't be " + bookingStatus,
                        delay: 2000
                    });
                }
            }).error(function(response) {
                $('#wrapper').show();
                $('#spinner').hide();
                Notification.error({
                    message: "Couldn't establish connection",
                    delay: 2000
                });
            });
        } else {
            // Request to cancel the particular booking
            $http({
                method: 'POST',
                url: '/Resource-Booking/bookings/updateBookingsStatus',
                data: $scope.updateData,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(data, status, headers, config) {
                $('#wrapper').show();
                $('#spinner').hide();
                if (status == 200) {
                    Notification.success({
                        message: 'Successfully ' + bookingStatus,
                        delay: 2000
                    });
                    $scope.fetchPendingBookings($scope.resources.resourceId);
                } else {
                    Notification.info({
                        message: "Can't be " + bookingStatus,
                        delay: 2000
                    });
                }
            }).error(function(data, status, headers, config) {
                $('#wrapper').show();
                $('#spinner').hide();
                Notification.error({
                    message: "Couldn't establish connection",
                    delay: 2000
                });
            });
        }
    };
});