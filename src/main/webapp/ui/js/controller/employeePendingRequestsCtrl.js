// Created By - Amit Sharma
var pendingRequestsApp = angular.module('employeePendingRequestsApp', ['ui-notification', 'ngRoute', 'dataShareFactory', 'utilityFunctionsFactory', 'topbarApp', 'sidebarApp','ui.bootstrap']);

// Controller to Cancel the user's pending requests
pendingRequestsApp.controller('employeePendingRequestCtrl', function($scope, $http, userDetails, utilityFunctions, $location, Notification) {

    $scope.resources = {};
    $scope.response = {};
    $scope.updateData = {};
    $scope.currentUser = userDetails.getCurrentUser();
    
    // method to update pending request status--Cancelled
    $scope.updateEmployeeRequest = function(bookingId, employeeId, employeeName, email, mobileNumber, bookingStatus) {
        $scope.updateData.bookingId = bookingId;
        $scope.updateData.userDetails = {};
        $scope.updateData.userDetails.employeeId = employeeId;
        $scope.updateData.userDetails.name = employeeName;
        $scope.updateData.userDetails.email = email;
        $scope.updateData.userDetails.mobileNumber = mobileNumber;
        $scope.updateData.status = bookingStatus;

        $('#wrapper').hide();
        $('#spinner').show();
        // Request to update the booking status to - Cancelled
        $http({
                method: 'POST',
                url: '/Resource-Booking/bookings/updateBookingsStatus',
			                data : $scope.updateData,
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .success(function(data, status, headers, config) {
                $('#wrapper').show();
                $('#spinner').hide();
                if (status == 200) {
                    Notification.success({
                        message: "Successfully" + bookingStatus,
                        delay: 2000
                    });
                    $scope.fetchPendingBookingsByEmployeeId();
                } else {
                    Notification.info({
                        message: "Can't " + bookingStatus,
                        delay: 2000
                    });
                }
            })
            .error(function(data, status, headers, config) {
                $('#wrapper').show();
                $('#spinner').hide();
                Notification.error({
                    message: status,
                    delay: 2000
                });
            });
    };
    
    
    // fetches all the pending request for the currently logged in user
    $scope.fetchPendingBookingsByEmployeeId = function() {
    	 	
          //pagination 
    	  $scope.currentPage = 4;
     	  $scope.itemsPerPage = 2;//items per page
     	  $scope.maxSize = 5; //Number of pager buttons to show

        $scope.filterData = {};
        $scope.filterData.employeeId = $scope.currentUser.employeeId;
        // Request to fetch all the pending requests for the user
        $http({
            method: 'POST',
            url: '/Resource-Booking/bookings/getPendingbookingsByEmployeeId',
            data: $scope.filterData,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            if (response.data == null ||
                response.status == 400 ||
                response.data.length == 0) {
                $('#no-pending-requests').show();
                $('#pending-request-table').hide();
            } else {
                $scope.response = response.data;
                $scope.totalItems = response.data.length;
                
                $('#no-pending-requests').hide();
                $('#pending-request-table').show();
            }
        }).error(function(response) {
            Notification.error({
                message: "Couldn't establish connection",
                delay: 2000
            });
        });
    }
});
