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
            	console.log(response.errorMessage);
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

	/*function onSignIn(googleUser) {
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
				$window.location.href = 'admin/index.html';
			} else {
				profileDetails.name = profile.getName();
				userDetails.setUser(profileDetails);
				$rootScope.$emit("setUserDetails", {});
				location = "#toregister";
			}
		}).error(function(response) {
			alert("Connection Error");
		});
		// $rootScope.$emit("setUserDetails", {});

		// location = "#toregister";
	}

	window.onSignIn = onSignIn;*/
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
					$("input[type='email']").prop('readonly', "false");
					location = "#tologin"
				}
	        }).error(function(response) {
				alert("Connection Error");
			});
		}
	}
});

// function onSignIn(googleUser) {
// 	var profile = googleUser.getBasicProfile();
// 	var id_token = googleUser.getAuthResponse().id_token;
//
// 	profileDetails = {};
// 	profileDetails.name = profile.getName();
// 	profileDetails.email = profile.getEmail();
// 	//userDetails.setUser(profileDetails);
//
// 	console.log(id_token);
// 	console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
// 	console.log('Name: ' + profile.getName());
// 	console.log('Image URL: ' + profile.getImageUrl());
// 	console.log('Email: ' + profile.getEmail());
//
// 	console.log(profileDetails);
// 	location = "#toregister";
// 	//console.log("sdgd"+$location.host());
// }

function onFailure(error) {
	console.log(error);
}

function signOut() {
	var auth2 = gapi.auth2.getAuthInstance();
	console.log(auth2);
	auth2.signOut().then(function () {
		console.log('User signed out.');
	});
}

// var CLIENT_ID = '170024686743-1qm8as78v2sh04k5tfdj9qlai0h9ptv9.apps.googleusercontent.com';
//
//       var SCOPES = ['https://www.googleapis.com/auth/gmail.readonly'];
//
//       /**
//        * Check if current user has authorized this application.
//        */
//       function checkAuth() {
//         gapi.auth.authorize(
//           {
//             'client_id': CLIENT_ID,
//             'scope': SCOPES.join(' '),
//             'immediate': true
//           }, handleAuthResult);
//       }
//
//       /**
//        * Handle response from authorization server.
//        *
//        * @param {Object} authResult Authorization result.
//        */
//       function handleAuthResult(authResult) {
//         var authorizeDiv = document.getElementById('authorize-div');
//         if (authResult && !authResult.error) {
//           // Hide auth UI, then load client library.
//           authorizeDiv.style.display = 'none';
//           loadGmailApi();
//         } else {
//           // Show auth UI, allowing the user to initiate authorization by
//           // clicking authorize button.
//           authorizeDiv.style.display = 'inline';
//         }
//       }
//
//       /**
//        * Initiate auth flow in response to user clicking authorize button.
//        *
//        * @param {Event} event Button click event.
//        */
//       function handleAuthClick(event) {
//         gapi.auth.authorize(
//           {client_id: CLIENT_ID, scope: SCOPES, immediate: false},
//           handleAuthResult);
//         return false;
//       }
//
//       /**
//        * Load Gmail API client library. List labels once client library
//        * is loaded.
//        */
//       function loadGmailApi() {
//         gapi.client.load('gmail', 'v1', listLabels);
//       }
//
//       /**
//        * Print all Labels in the authorized user's inbox. If no labels
//        * are found an appropriate message is printed.
//        */
//       function listLabels() {
//         var request = gapi.client.gmail.users.labels.list({
//           'userId': 'me'
//         });
//
//         request.execute(function(resp) {
//           var labels = resp.labels;
//           appendPre('Labels:');
//
//           if (labels && labels.length > 0) {
//             for (i = 0; i < labels.length; i++) {
//               var label = labels[i];
//               appendPre(label.name)
//             }
//           } else {
//             appendPre('No Labels found.');
//           }
//         });
//       }
//
//       /**
//        * Append a pre element to the body containing the given message
//        * as its text node.
//        *
//        * @param {string} message Text to be placed in pre element.
//        */
//       function appendPre(message) {
//         var pre = document.getElementById('output');
//         var textContent = document.createTextNode(message + '\n');
//         pre.appendChild(textContent);
//       }
//
