var pendingRequestsApp = angular.module('employeePendingRequestsApp',
		[ 'ngRoute', 'dataShareFactory', 'utilityFunctionsFactory', 'topbarApp',
				'sidebarApp' ]);

pendingRequestsApp
		.controller(
				'employeePendingRequestCtrl',
				function($scope, $http, userDetails, utilityFunctions,
						$location) {

					$scope.resources = {};
					$scope.response = {};
					$scope.updateData = {};
					var resourceId = utilityFunctions.getResourceDetails();
					$scope.currentUser = userDetails.getCurrentUser();

					/**
					 * method to update pending request status--Cancelled
					 */
					$scope.updateEmployeeRequest = function(bookingId,employeeId, employeeName, email, bookingStatus) {
						$scope.updateData.bookingId = bookingId;
						$scope.updateData.userDetails = {};
						$scope.updateData.userDetails.employeeId = employeeId;
						$scope.updateData.userDetails.name = employeeName;
						$scope.updateData.userDetails.email = email;
						$scope.updateData.status = bookingStatus;

						$http(
								{
									method : 'POST',
									url : 'http://localhost:8080/Project-Authentication/bookings/updateBookingsStatus',
									data : $scope.updateData,
									headers : {
										'Content-Type' : 'application/json'
									}
								})
								.success(
										function(data, status, headers, config) {
											if (status == 200) {
												alert('Successfully '
														+ bookingStatus);
												$scope
														.fetchPendingBookingsByEmployeeId();
											} else {
												alert('Can\'t be '
														+ bookingStatus);
											}
										})
								.error(function(data, status, headers, config) {
									alert('Error' + status);
								});
					};
					/**
					 * fetches all the pending request for the currently logged
					 * in user
					 */
					$scope.fetchPendingBookingsByEmployeeId = function() {

						$scope.filterData = {};
						$scope.filterData.employeeId = $scope.currentUser.employeeId;
						$http(
								{
									method : 'POST',
									url : 'http://localhost:8080/Project-Authentication/bookings/getPendingbookingsByEmployeeId',
									data : $scope.filterData,
									headers : {
										'Content-Type' : 'application/json'
									}
								}).success(
								function(response) {
									if (response.data == null
											|| response.status == 400
											|| response.data.length == 0) {
										$('#no-pending-requests').show();
										$('#pending-request-table').hide();
									} else {
										$scope.response = response.data;
										$('#no-pending-requests').hide();
										$('#pending-request-table').show();
									}
								}).error(function(response) {
							console.log(response);
						});
					}

					// load pending requests on page load
					angular.element(document).ready(function() {
						$scope.fetchPendingBookingsByEmployeeId();
					});

				});
