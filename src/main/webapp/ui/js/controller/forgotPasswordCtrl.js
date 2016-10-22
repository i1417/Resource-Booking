var forgotPasswordPage = angular.module('forgotPasswordApp', ['angular-md5']);

forgotPasswordPage.config(['$locationProvider', function($locationProvider){
    $locationProvider.html5Mode(true);
}]);

forgotPasswordPage.controller('forgotPasswordCtrl', function($scope, $window, $location, $http, md5) {
	$scope.user = {};

	$scope.redirect = function() {
		console.log("Called");
		$window.location.href = 'http://localhost:8080/Project-Authentication/';
	}
    $scope.changePassword = function() {
        if($scope.confirmPassword != $scope.user.password) {
            $('#error').text('Password do not match');
			$("#error").show();
		} else {
			if(!angular.isUndefined($scope.user.password)) {
				$scope.user.password = md5.createHash($scope.user.password);
				$scope.confirmPassword = md5.createHash($scope.confirmPassword);
				$("#error").hide();
			}

			$scope.user.email = 'anant.sharma@company.com';

            var urlToRedirect = 'http://localhost:8080/Project-Authentication/changePassword?token=' + $location.search().token;

			$http({
	            method : 'POST',
	            url : urlToRedirect,
	            data : $scope.user,
	            headers : {'Content-Type': 'application/json'}
	        }).success(function(response) {
                $scope.user.password = "";
                $scope.confirmPassword = "";
                $('#info').text(response.errorMessage);
                $('#info').show();
	        }).error(function(response) {
				alert("Connection Error");
			});
		}
    }
});
