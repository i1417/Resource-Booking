// Created By - Arpit Pittie
var landingPage = angular.module('landingPage', ['ui-notification', 'angular-md5', 'dataShareFactory']);

addEventListener("load", function() {
        setTimeout(hideURLbar, 0);
    },
    false
);

function hideURLbar() {
    window.scrollTo(0, 1);
}

// Check if user is already logged in
if (sessionStorage.length != 0) {
    window.location = "user/index.html";
}

// Controller for login into the the application
landingPage.controller('loginForm', function($scope, $http, $window, $rootScope, userDetails, md5, Notification) {
    $scope.user = {};

    // To authenticate the user
    $scope.authenticateLogin = function() {
        $scope.user.password = md5.createHash($scope.user.password);
        $('#container_demo').hide();
        $('h1').hide();
        $('#spinner').show();

        // Request to check the user credentials
        $http({
            method: 'POST',
            url: 'http://localhost:8080/Project-Authentication/validate/custom',
            data: $scope.user,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            $('#container_demo').show();
            $('#spinner').hide();
            $('h1').show();

            $scope.user.password = "";
            if (response.status == 200) {
                userDetails.setCurrentUser(response.data);
                $window.location.href = 'user/index.html';
            } else {
                $scope.user.password = "";
                Notification.error({
                    message: response.errorMessage,
                    delay: 2000
                });
            }
        }).error(function(response) {
            $('#container_demo').show();
            $('#spinner').hide();
            $('h1').show();
            Notification.error({
                message: "Connection couldn't establish",
                delay: 2000
            });
        });
    }

    // To load the Google Login API
    var googleUser = {};
    $scope.startApp = function() {
        gapi.load('auth2', function() {
            // Retrieve the singleton for the GoogleAuth library and set up the client.
            auth2 = gapi.auth2.init({
                client_id: '170024686743-1qm8as78v2sh04k5tfdj9qlai0h9ptv9.apps.googleusercontent.com',
                cookiepolicy: 'single_host_origin',
            });
            $scope.attachSignin(document.getElementById('customBtn'));
        });
    };

    // Callback function from Google on successful login
    $scope.attachSignin = function(element) {
        auth2.attachClickHandler(element, {}, function(googleUser) {

            var profile = googleUser.getBasicProfile();
            var id_token = googleUser.getAuthResponse().id_token;

            var profileDetails = {};
            profileDetails.email = profile.getEmail();

            // Request to check if user already has a account
            $http({
                method: 'POST',
                url: 'http://localhost:8080/Project-Authentication/validate/custom',
                data: profileDetails,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                // User Already has a account
                if (response.status == 200) {
                    userDetails.setCurrentUser(response.data);
                    $window.location.href = 'user/index.html';
                } else {
                    // User does not exist and create a account
                    profileDetails.name = profile.getName();
                    userDetails.setUser(profileDetails);
                    $rootScope.$emit("setUserDetails", {});
                    location = "#toregister";
                }
            }).error(function(response) {
                Notification.error({
                    message: "Connection couldn't establish",
                    delay: 2000
                });
            });
        }, function(error) {
            alert(JSON.stringify(error, undefined, 2));
        });
    }

    // To generate the link to reset the password
    $scope.forgotPassword = function(value) {
        if (value) {
            $('h1').hide();
            $('#container_demo').hide();
            $('#spinner').show();

            // Request to generate the reset password link for the user
            $http({
                method: 'POST',
                url: 'http://localhost:8080/Project-Authentication/forgotPass',
                data: $scope.user,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                $('#container_demo').show();
                $('#spinner').hide();
                $('h1').show();
                Notification.success({
                    message: response.errorMessage,
                    delay: 2000
                });
            }).error(function(response) {
                $('#container_demo').show();
                $('#spinner').hide();
                $('h1').show();
                Notification.error({
                    message: response.errorMessage,
                    delay: 2000
                });
            });
        } else {
            Notification.error({
                message: "Enter mail id",
                delay: 2000
            });
        }
    }
});

// Controller to register the user on the web application
landingPage.controller('registerForm', function($scope, $http, $window, $rootScope, md5, userDetails, Notification) {
    $scope.user = {};
    $scope.confirm = {};

    $rootScope.$on("setUserDetails", function() {
        $scope.setUserDetails();
    });

    $scope.changeEmailBox = function() {
        $("input[type='email']").removeAttr('readonly');
    }

    // To modify the signup form according to the type of SignUp
    $scope.setUserDetails = function() {
        $scope.user = userDetails.getUser();

        $("input[type='password']").removeAttr("required");
        $(".passwordHide").hide();
        $("input[type='email']").prop('readonly', "true");
        $("#name").prop('readonly', "true");
    }

    // To setup a new account for the user
    $scope.createAccount = function() {
        $scope.user.role = 'user';

        // Checking if the password match
        if ($scope.confirm.password != $scope.user.password) {
            Notification.warning({
                message: "Password Do not match",
                delay: 2000
            });
        } else {
            if (!angular.isUndefined($scope.user.password)) {
                $scope.user.password = md5.createHash($scope.user.password);
                $scope.confirm.password = md5.createHash($scope.confirm.password);

            }
            $('h1').hide();
            $('#container_demo').hide();
            $('#spinner').show();
            // Request to create a new account
            $http({
                method: 'POST',
                url: 'http://localhost:8080/Project-Authentication/createAccount',
                data: $scope.user,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                $('#container_demo').show();
                $('h1').show();
                $('#spinner').hide();
                if (response.status == 400) {
                    Notification.error({
                        message: response.errorMessage,
                        delay: 2000
                    });
                } else {
                    $window.location.href = "http://localhost:8080/Project-Authentication";
                }
            }).error(function(response) {
                $('#container_demo').show();
                $('h1').show();
                $('#spinner').hide();
                Notification.error({
                    message: "Couldn't establish connection",
                    delay: 2000
                });
            });
        }
    }
});
