var pendingRequestsApp = angular.module('pendingRequestsApp', ['ui-notification', 'ngRoute', 'dataShareFactory', 'utilityFunctionsFactory', 'topbarApp', 'sidebarApp']);

pendingRequestsApp.controller('pendingRequestCtrl', function($scope, $http, userDetails, utilityFunctions, $location, Notification) {

    $scope.resources = {};
    $scope.response = {};
    $scope.updateData = {};
    var resourceId = utilityFunctions.getResourceDetails();
    $scope.currentUser = userDetails.getCurrentUser();

    /**
     * The following function fetches all the upcoming pending bookings
     */
    $scope.fetchPendingBookings = function() {

        $scope.resources.resourceId = resourceId;
        $('#wrapper').hide();
        $('#spinner').show();
        $http({
            method: 'POST',
            url: 'https://localhost:8443/Project-Authentication/bookings/getPendingbookings',
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

    /**
     * method to update pending request status--Approved or Rejected
     */
    $scope.updateRequest = function(bookingId, employeeId, employeeName, email, mobileNumber, bookingStatus) {

        $scope.updateData.bookingId = bookingId;
        $scope.updateData.userDetails = {};
        $scope.updateData.userDetails.employeeId = employeeId;
        $scope.updateData.userDetails.name = employeeName;
        $scope.updateData.userDetails.email = email;
        $scope.updateData.userDetails.mobileNumber = mobileNumber;
        $scope.updateData.status = bookingStatus;

        $('#wrapper').hide();
        $('#spinner').show();
        if ($scope.updateData.status == 'Approved') {
            $http({
                method: 'POST',
                url: 'https://localhost:8443/Project-Authentication/bookings/updateBookingsStatusApproved',
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
            $http({
                method: 'POST',
                url: 'https://localhost:8443/Project-Authentication/bookings/updateBookingsStatus',
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

    angular.element(document).ready(function() {
        $scope.fetchPendingBookings();
    });

});
