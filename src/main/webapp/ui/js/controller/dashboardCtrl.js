/**
 * @author Rohit Singhavi
 */

/**
 * @class angular_module.homePageApp
 * @memberOf angular_module
 */
var homePage = angular.module('homePageApp', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp', 'utilityFunctionsFactory', 'ui.bootstrap', 'ui-notification']);


/**
 * @class angular_module.homePageApp.bookingCtrl
 * @description Controller for the booking modal, used for edit and creating a Resource booking
 */ 
var bookingCtrl = function($scope, $http, $window, $modal, $modalInstance, userDetails, utilityFunctions, itemObj, Notification) {

    $scope.booking = {};
    $scope.booking.resourceDetails = {};
    $scope.booking.userDetails = {};
    $scope.booking.userDetails.employeeId = userDetails.getCurrentUser().employeeId;
    $scope.booking.userDetails.email = userDetails.getCurrentUser().email;
    $scope.booking.userDetails.name = userDetails.getCurrentUser().name;
    $scope.booking.userDetails.mobileNumber = userDetails.getCurrentUser().mobileNumber;
    $scope.allResources = utilityFunctions.getAllResources();

    $scope.sTime = itemObj.startTime;
    $scope.eTime = itemObj.endTime;
    $scope.dateFormat = itemObj.dateFormat;
    $scope.booking.bookingId = itemObj.bookingId;
    $scope.booking.title = itemObj.title;
    $scope.booking.description = itemObj.description;
    $scope.noOfParticipants = itemObj.numberOfParticipants;
    $scope.resourceValue = itemObj.resourceId.toString();

    if (angular.isUndefined(itemObj.bookBtn) || itemObj.bookBtn == "new" ) {
        //enabling resource selection in booking modal form
    	$scope.truefalse = false;
    	//changing submit button value of booking modal form
        $scope.bookBtn = "Book";
        //setting url value
        $scope.urlValue = "/Resource-Booking/bookings/createBooking";
    } else {
        $scope.truefalse = true;
        $scope.bookBtn = "Edit Booking";
        $scope.urlValue = "/Resource-Booking/bookings/editBooking";
    }

    /**
     * @name $scope.cancel
     * @function
     * @memberOf angular_module.homePageApp.bookingCtrl
     * @description Called when user click on cancel button in booking modal
     */
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    /**
     * @name $scope.bookResource
     * @function
     * @memberOf angular_module.homePageApp.bookingCtrl
     * @description Called when user clicks on Book button
     */
    $scope.bookResource = function() {
        //setting booking object
    	$scope.booking.date = $('#datePickerInput').val();
        $scope.booking.startTime = $('#startTimeInput').val();
        $scope.booking.endTime = $('#endTimeInput').val();
        $scope.booking.resourceDetails.resourceId = $('#resSelect :selected').val();
        $scope.booking.resourceDetails.resourceName = $('#resSelect :selected').text();

        //Showing spinner
        $('#wrapper').hide();
        $('#spinner').show();
        $modalInstance.dismiss('cancel');

        $http({
            method: 'POST',
            url: $scope.urlValue,
            data: $scope.booking,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
        	//hiding spinner
            $('#wrapper').show();
            $('#spinner').hide();
            
            if (response.status == 200) {

                Notification({
                    message: 'Your Current Booking is ' + response.data.status + '.',
                    title: 'Booking Status',
                    delay: 2000
                });
                setTimeout(function() {
                    $window.location.href = 'index.html';
                }, 2500);

            }
        }).error(function(response) {
            Notification.error({
                message: "Couldn't establish connection",
                delay: 2000
            });
        });
    }

    
    /**
     * @name $scope.pickDateTime
     * @function
     * @memberOf angular_module.homePageApp.bookingCtrl
     * @description called as ng-init when modal opens
     */
    $scope.pickDateTime = function() {
        $('#datePicker').datetimepicker({
            format: 'YYYY-MM-DD',
            ignoreReadonly: true,

            icons: {
                up: "fa fa-chevron-circle-up",
                down: "fa fa-chevron-circle-down",
                next: 'fa fa-chevron-circle-right',
                previous: 'fa fa-chevron-circle-left',
                time: "fa fa-clock-o",
                date: "fa fa-calendar",
            }
        }).on('dp.change', function(event) {
        	//setting minimum date selection to current date
            $('#datePicker').data('DateTimePicker').minDate($scope.date);
            
            //check if current date is selected
            if (event.date.format().substring(0, 10) === moment().format().substring(0, 10)) {
            	//setting start time picker to current time
            	$('#startTime').data('DateTimePicker').minDate(moment({
                    H: new Date().getHours(),
                    m: new Date().getMinutes()
                }));
            } else {
                $('#startTime').data('DateTimePicker').minDate(0);
            }
        });

        //Formatting start time picker of booking modal
        $('#startTime').datetimepicker({
            format: 'HH:mm:00',
            ignoreReadonly: true,
            stepping: 15,
            icons: {
                up: "fa fa-chevron-circle-up",
                down: "fa fa-chevron-circle-down",
                next: 'fa fa-chevron-circle-right',
                previous: 'fa fa-chevron-circle-left',
                time: "fa fa-clock-o",
                date: "fa fa-calendar",
            }
        }).on('dp.change', function(event) {
        	//setting minimum time of end time picker to selected start time
            $('#endTime').data('DateTimePicker').minDate(event.date);
        });

        //Formatting end time picker of booking modal
        $('#endTime').datetimepicker({
            format: 'HH:mm:00',
            ignoreReadonly: true,
            stepping: 15,

            icons: {
                up: "fa fa-chevron-circle-up",
                down: "fa fa-chevron-circle-down",
                next: 'fa fa-chevron-circle-right',
                previous: 'fa fa-chevron-circle-left',
                time: "fa fa-clock-o",
                date: "fa fa-calendar",
            }
        });

    }//end of datetimepicker
};

/**
 * @class angular_module.homePageApp.dashboardCtrl
 * @description Controller to perform the actions on the main dashboard before loading the calendar
 */ 
homePage.controller('dashboardCtrl', function($rootScope, $scope, $modal, $http, $filter, userDetails, utilityFunctions) {

    $scope.currentUser = userDetails.getCurrentUser();
    $scope.date = $filter('date')(new Date(), 'yyyy-MM-dd');
    $scope.currentTime = $filter('date')(new Date(), 'HH:mm:ss');


    $http({
        method: 'GET',
        url: '/Resource-Booking/resources/getAll',
        headers: {
            'Content-Type': 'application/json'
        }
    }).success(function(response) {
        if (response.status == 200) {
            utilityFunctions.setAllResources(response.data);
            $rootScope.$emit("populateResources", {});
        }
    }).error(function(response) {
        alert("Connection Error");
    });

    /*To open the modal dialog box*/
    $scope.showModal = function() {
        /*Setting the modal options*/
        $scope.opts = {
            backdrop: true,
            dialogFade: false,
            backdropClick: true,
            keyboard: true,
            templateUrl: 'bookingForm.html',
            controller: bookingCtrl,
            resolve: {
                itemObj: function() {
                    return {
                        startTime: "",
                        endTime: "",
                        resourceId: ""
                    };
                }
            }
        };

        /*Opening up the modal*/
        var modalInstance = $modal.open($scope.opts);
    }

});

/**
 * @class angular_module.homePageApp.calendarCtrl
 * @description Controller for the calendar
 */
homePage.controller('calendarCtrl', function($rootScope, $scope, $http, $modal, userDetails, utilityFunctions, Notification) {
    $rootScope.$on("populateResources", function() {
        $scope.allResources = utilityFunctions.getAllResources();
        $scope.currentUser = userDetails.getCurrentUser();

        $http({
            method: 'GET',
            url: '/Resource-Booking/bookings/getApprovedBookings?employeeId=' + $scope.currentUser.employeeId,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            if (response.status == 200) {
                $scope.allApprovedBookings = response.data;
            }
            $http({
                method: 'POST',
                url: '/Resource-Booking/bookings/getApprovedbookingsByEmployeeId',
                data: $scope.currentUser,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                if (response.status == 200) {
                    $scope.currentUser.bookingsMade = response.data;
                    userDetails.setCurrentUser($scope.currentUser);
                }
                //showing calendar after successful fetching of all approved bookings
                $scope.showCalendar();
            }).error(function(response) {
                Notification.error({
                    message: "Couldn't establish connection",
                    delay: 2000
                });
            });
        }).error(function(response) {
            Notification.error({
                message: "Couldn't establish connection",
                delay: 2000
            });
        });
    });

    // To show the calendar with the given options
    $scope.showCalendar = function() {
        $('#calendar').fullCalendar({
            defaultView: 'agendaDay',
            defaultDate: $scope.date,
            editable: true,
            selectable: true,
            eventLimit: true,
            snapDuration: {
                minutes: 15
            },
            timezone: 'Asia/Kolkata',
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'agendaDay,agendaTwoDay,agendaWeek,month,listDay,listWeek'
            },
            views: {
                listDay: {
                    buttonText: 'list day'
                },
                listWeek: {
                    buttonText: 'list week'
                },
                agendaTwoDay: {
                    type: 'agenda',
                    duration: {
                        days: 2
                    },
                    groupByResource: true
                }
            },

            allDaySlot: false,

            //Tells the calendar to display resources from an array input.
            resources: function(reply) {
                var resources = [];
                $($scope.allResources).each(function() {
                    resources.push({
                        id: $(this).attr('resourceId'),
                        title: $(this).attr('resourceName') + ", Capacity(" + $(this).attr('capacity') + ")"
                    });
                });
                reply(resources);
            },

            /**
             * FullCalendar will call this function whenever it needs new event data. 
             * This is triggered when the user clicks prev/next or switches views.
             */
            events: function(start, end, timezone, callback) {
                var events = [];

                $($scope.allApprovedBookings).each(function() {
                    var res = $(this).attr('resourceDetails');
                    var startTime = $(this).attr('date') + 'T' + $(this).attr('startTime') + '+05:30';
                    var endTime = $(this).attr('date') + 'T' + $(this).attr('endTime') + '+05:30';

                    events.push({
                        id: $(this).attr('bookingId'),
                        title: $(this).attr('title') + "\n" + $(this).attr('description') + "\nParticipants:  \b" + $(this).attr('numberOfParticipants'),
                        start: startTime, 
                        end: endTime,
                        editable: false,
                        resourceId: $(res).attr('resourceId'),
                        textColor: 'white',
                        color: 'black'
                    });
                });

                $($scope.currentUser.bookingsMade).each(function() {
                    var res = $(this).attr('resourceDetails');
                    var startTime = $(this).attr('date') + 'T' + $(this).attr('startTime') + '+05:30';
                    var endTime = $(this).attr('date') + 'T' + $(this).attr('endTime') + '+05:30';
                    var currentTime = new Date().getHours() + ":" + new Date().getMinutes();

                    //changing editable value of event according to date and time
                    if (startTime.substring(0, 10) < $scope.date) {
                        var editableValue = false;
                    } else if (startTime.substring(0, 10) == $scope.date) {
                        if (startTime.substring(11, 19) < $scope.currentTime) {
                            var editableValue = false;
                        } else {
                            var editableValue = true;
                        }
                    } else {
                        var editableValue = true;
                    }

                    events.push({
                        id: $(this).attr('bookingId'),
                        title: $(this).attr('title') + "\n" + $(this).attr('description') + "\nParticipants:  \b" + $(this).attr('numberOfParticipants'),
                        start: startTime,
                        end: endTime,
                        editable: editableValue,
                        resourceId: $(res).attr('resourceId'),
                        textColor: 'black',
                        color: '#9ccefc'
                    });
                });

                //it must be called when the custom event function has generated its events
                callback(events);
            },

            //A method for programmatically selecting a period of time.
            select: function(start, end, jsEvent, view, resource) {
                var startT = start.format().substring(11);
                var endT = end.format().substring(11);
                var dateFormat = start.format().substring(0, 10);
                if (!angular.isUndefined(resource)) {
                    var resourceId = resource.id;
                } else {
                    var resourceId = "";
                }

                var id = "";
                var title = "";
                var description = "";
                var bookBtn = "new";
                var numberOfParticipants = "";

                if ($scope.checkDate(dateFormat, startT)) {
                    $scope.showModal(startT, endT, dateFormat, id, title, description, numberOfParticipants, resourceId, bookBtn);
                } else {
                    Notification.error({
                        message: 'Unable to book at selected time',
                        positionY: 'bottom',
                        positionX: 'center',
                        delay: 2000
                    });
                }

            },

            /**
             * Gives programmatic control over where an event can be dropped.
             * In addition to receiving information about which date the user is attempting to drop the event upon,
             *  it will also receive information about the resource:
             */
            eventAllow: function(dropLocation, draggedEvent) {
                if (dropLocation.resourceId == draggedEvent.resourceId) {
                    return true;
                } else {
                    return false;
                }
            },

            //Triggered when the user clicks an event.
            eventClick: function(calEvent) {
                if (calEvent.editable) {
                    $scope.callShowModal(calEvent);
                } else {
                    Notification.error({
                        message: 'Can not Edit!',
                        positionY: 'bottom',
                        positionX: 'right',
                        delay: 2000
                    });
                }
            },

            //Triggered when dragging stops and the event has moved to a different day/time.
            eventDrop: function(event, revertFunc) {
                $scope.callShowModal(event);
            },

            //Triggered when resizing stops and the event has changed in duration
            eventResize: function(event) {
                $scope.callShowModal(event);
            },


            //Triggered when the user mouses over an event.
            eventMouseover: function(calEvent, jsEvent) {
                var title = calEvent.title.substring(0, calEvent.title.indexOf('\n') + 1);
                var description = calEvent.title.substring(calEvent.title.indexOf('\n') + 1, calEvent.title.indexOf('\nP'));
                var numberOfParticipants = calEvent.title.substring(calEvent.title.indexOf('\b'));

                //initializing and showing tooltip
                var tooltip = '<div class="tooltipevent" style="border-radius:10px;background:#bac8f2;position:absolute;z-index:1000; padding-right:10px;"><ul>' +
                    '<li>Title: ' + title + '</li><li>Description:' + description + '</li><li>Participants:' + numberOfParticipants + '</li></ul></div>';
                var $tooltip = $(tooltip).appendTo('body');

                $(this).mouseover(function(e) {
                    $(this).css('z-index', 1000);
                    $tooltip.fadeIn('500');
                    $tooltip.fadeTo('10', 1.9);
                }).mousemove(function(e) {
                    $tooltip.css('top', e.pageY + 10);
                    $tooltip.css('left', e.pageX + 20);
                });
            },

            //Triggered when the user mouses out of an event.
            eventMouseout: function(calEvent, jsEvent) {
            	//removing tooltip
                $(this).css('z-index', 8);
                $('.tooltipevent').remove();
            }

        });
    }

    //method to call showModal method with required arguments
    $scope.callShowModal = function(event) {
        var startT = event.start.format().substring(11, 19);
        if (event.end == null) {
            var endT = "";
        } else {
            var endT = event.end.format().substring(11, 19);
        }
        var dateFormat = event.start.format().substring(0, 10);
        var resourceId = event.resourceId;
        var bookingId = event.id;
        var title = event.title.substring(0, event.title.indexOf('\n'));
        var description = event.title.substring(event.title.indexOf('\n') + 1, event.title.indexOf('\nP'));
        var numberOfParticipants = event.title.substring(event.title.indexOf('\b'));
        var bookBtn = "edit";
        $(this).css('border-color', 'yellow');
        $scope.showModal(startT, endT, dateFormat, bookingId, title, description, numberOfParticipants, resourceId, bookBtn);
    }

    //method to caheck current date and time
    $scope.checkDate = function(date, startTime) {
        var selectedDate = date;
        var selectedTime = startTime;
        if (selectedTime < $scope.currentTime && selectedDate < $scope.date) {
            return false;
        } else if (selectedDate < $scope.date) {
            return false;
        } else if (selectedTime < $scope.currentTime) {
            if (selectedDate === $scope.date) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    //Method to show booking modal form
    $scope.showModal = function(start, end, dateFormat, bookingId, title, description, numberOfParticipants, resource, bookBtn) {
        /*Setting the modal options*/
        $scope.opts = {
            backdrop: true,
            dialogFade: false,
            backdropClick: true,
            keyboard: true,
            templateUrl: 'bookingForm.html',
            controller: bookingCtrl,
            resolve: {
                itemObj: function() {
                    return {
                        startTime: start,
                        endTime: end,
                        dateFormat: dateFormat,
                        bookingId: bookingId,
                        title: title,
                        description: description,
                        numberOfParticipants: numberOfParticipants,
                        resourceId: resource,
                        bookBtn: bookBtn,
                    };
                }
            }
        };

        /*Opening up the modal*/
        var modalInstance = $modal.open($scope.opts);
    }
});