var landingPage = angular.module('landingPage', ['angular-md5', 'dataShareFactory']);

addEventListener("load", function() {
		setTimeout(hideURLbar, 0);
	},
	false
);
function hideURLbar() {
	window.scrollTo(0,1);
}

if(sessionStorage.length != 0) {
	window.location = "user/index.html";
} else {
	console.log("Here");
	localStorage.setItem('getSessionStorage', "Getting");
	console.log(localStorage);
}

window.addEventListener('storage', function(event) {
	console.log("Called");
	if (event.key == 'user' && !sessionStorage.length) {
		var data = JSON.parse(event.newValue),
					value;
		sessionStorage.setItem('user', localStorage.getItem('user'));
	}
	localStorage.clear();
	window.location = "user/index.html";
});

landingPage.controller('loginForm', function($scope, $http, $window, $rootScope, userDetails, md5) {
	$scope.user = {};
	$scope.authenticateLogin = function() {
		$scope.user.password = md5.createHash($scope.user.password);
        $http({
            method : 'POST',
            url : 'http://localhost:8080/Project-Authentication/validate/custom',
            data : $scope.user,
            headers : {'Content-Type': 'application/json'}
        }).success(function(response) {
            console.log(response);
            //console.log(response.data);
            $scope.user.password = "";
            if(response.status == 200 ) {
            	userDetails.setCurrentUser(response.data);
            	$window.location.href = 'user/index.html';
            } else {
            	$scope.user.password = "";
				$('#loginError').text(response.errorMessage);
            	$('#loginError').show();
            }
        }).error(function(response) {
			alert("Connection Error");
		});
    }

	var googleUser = {};
	$scope.startApp = function() {
		gapi.load('auth2', function(){
	    // Retrieve the singleton for the GoogleAuth library and set up the client.
	    auth2 = gapi.auth2.init({
	      client_id: '170024686743-1qm8as78v2sh04k5tfdj9qlai0h9ptv9.apps.googleusercontent.com',
	      cookiepolicy: 'single_host_origin',
	      // Request scopes in addition to 'profile' and 'email'
	      //scope: 'additional_scope'
	    });
	    $scope.attachSignin(document.getElementById('customBtn'));
	  });
	};

	$scope.attachSignin = function(element) {
		   auth2.attachClickHandler(element, {},function(googleUser) {

			   var profile = googleUser.getBasicProfile();
				var id_token = googleUser.getAuthResponse().id_token;

				var profileDetails = {};
				profileDetails.email = profile.getEmail();

				// console.log(id_token);
				console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
				console.log('Name: ' + profile.getName());
				console.log('Image URL: ' + profile.getImageUrl());
				console.log('Email: ' + profile.getEmail());
				console.log(profileDetails);
				$http({
					method : 'POST',
					url : 'http://localhost:8080/Project-Authentication/validate/custom',
					data : profileDetails,
					headers : {'Content-Type': 'application/json'}
				}).success(function(response) {
					console.log(response);
					if(response.status == 200) {
						userDetails.setCurrentUser(response.data);
						$window.location.href = 'user/index.html';
					} else {
						profileDetails.name = profile.getName();
						userDetails.setUser(profileDetails);
						$rootScope.$emit("setUserDetails", {});
						location = "#toregister";
					}
				}).error(function(response) {
					alert("Connection Error");
				});


	  }, function(error) {
	        alert(JSON.stringify(error, undefined, 2));
	      });
	}

	$scope.forgotPassword = function(value) {
		if(value) {
			console.log("I am here");
			$("#error").hide();

			$http({
	            method : 'POST',
	            url : 'http://localhost:8080/Project-Authentication/forgotPass',
	            data : $scope.user,
	            headers : {'Content-Type': 'application/json'}
	        }).success(function(response) {
	            console.log(response);
	            if(response.status == 200 ) {
	            	console.log("completed");
	            }
				$('#loginError').text(response.errorMessage);
				$('#loginError').show();
	        }).error(function(response) {
				alert("Connection Error");
			});
		} else {
			$("#error").text("Enter mail ID");
			$("#error").show();
		}
	}
});

landingPage.controller('registerForm', function($scope, $http, $window, $rootScope, md5, userDetails) {
	$scope.user = {};
	$scope.confirm = {};

	$rootScope.$on("setUserDetails", function(){
       $scope.setUserDetails();
    });

	$scope.changeEmailBox = function() {
		console.log("Here");
		$("input[type='email']").removeAttr('readonly');
	}

	$scope.setUserDetails = function() {
		$scope.user = userDetails.getUser();

		$("input[type='password']").removeAttr("required");
		$(".passwordHide").hide();
		$("input[type='email']").prop('readonly', "true");
		$("#name").prop('readonly', "true");

		// $scope.$apply();
		// $scope.$evalAsync();
	}

	$scope.createAccount = function() {
		$scope.user.role = 'user';

		if($scope.confirm.password != $scope.user.password) {
			$("#error").show();
		} else {
			if(!angular.isUndefined($scope.user.password)) {
				$scope.user.password = md5.createHash($scope.user.password);
				$scope.confirm.password = md5.createHash($scope.confirm.password);
				$("#error").hide();
			}

			$http({
	            method : 'POST',
	            url : 'http://localhost:8080/Project-Authentication/createAccount',
	            data : $scope.user,
	            headers : {'Content-Type': 'application/json'}
	        }).success(function(response) {
	            console.log(response);
				if(response.status == 400) {
					alert(response.message);
				} else {
					//$("input[type='email']").prop('readonly', "false");
					$window.location.href = "http://localhost:8080/Project-Authentication";
				}
	        }).error(function(response) {
				alert("Connection Error");
			});
		}
	}
});
