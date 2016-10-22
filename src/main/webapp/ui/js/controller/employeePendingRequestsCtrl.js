var pendingRequestsApp = angular.module('employeePendingRequestsApp',
		['ui-notification', 'ngRoute', 'dataShareFactory', 'utilityFunctionsFactory', 'topbarApp',
				'sidebarApp' ]);

pendingRequestsApp
		.controller(
				'employeePendingRequestCtrl',
				function($scope, $http, userDetails, utilityFunctions,
						$location, Notification) {

					$scope.resources = {};
					$scope.response = {};
					$scope.updateData = {};
					var resourceId = utilityFunctions.getResourceDetails();
					$scope.currentUser = userDetails.getCurrentUser();

					/**
					 * method to update pending request status--Cancelled
					 */
					$scope.updateEmployeeRequest = function(bookingId,employeeId, employeeName, email, mobileNumber, bookingStatus) {
						$scope.updateData.bookingId = bookingId;
						$scope.updateData.userDetails = {};
						$scope.updateData.userDetails.employeeId = employeeId;
						$scope.updateData.userDetails.name = employeeName;
						$scope.updateData.userDetails.email = email;
						$scope.updateData.userDetails.mobileNumber = mobileNumber;
						$scope.updateData.status = bookingStatus;

						$('#wrapper').hide();
						$('#spinner').show();
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
											$('#wrapper').show();
											$('#spinner').hide();
											if (status == 200) {
												Notification.success({message: "Successfully" + bookingStatus ,delay:2000});
												$scope
														.fetchPendingBookingsByEmployeeId();
											} else {
														Notification.info({message: "Can't "+bookingStatus ,delay:2000});
											}
										})
								.error(function(data, status, headers, config) {
									$('#wrapper').show();
									$('#spinner').hide();
									Notification.error({message: status ,delay:2000});
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
							Notification.error({message: "Couldn't establish connection",delay:2000});
						});
					}

					// load pending requests on page load
					angular.element(document).ready(function() {
						$scope.fetchPendingBookingsByEmployeeId();
					});

				});
