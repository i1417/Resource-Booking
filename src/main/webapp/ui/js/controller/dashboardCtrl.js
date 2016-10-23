var homePage = angular.module('homePageApp', ['ngRoute', 'dataShareFactory', 'topbarApp', 'sidebarApp', 'utilityFunctionsFactory', 'ui.bootstrap', 'ui-notification']);

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

    if (angular.isUndefined(itemObj.bookBtn)) {
        $scope.truefalse = false;
        $scope.bookBtn = "Book";
        $scope.urlValue = "http://localhost:8080/Project-Authentication/bookings/createBooking";
    } else if (itemObj.bookBtn == "new") {
        $scope.truefalse = true;
        $scope.bookBtn = "Book";
        $scope.urlValue = "http://localhost:8080/Project-Authentication/bookings/createBooking";
    } else {
        $scope.truefalse = true;
        $scope.bookBtn = "Edit Booking";
        $scope.urlValue = "http://localhost:8080/Project-Authentication/bookings/editBooking";
    }

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    $scope.bookResource = function() {
        $scope.booking.date = $('#datePickerInput').val();
        $scope.booking.startTime = $('#startTimeInput').val();
        $scope.booking.endTime = $('#endTimeInput').val();
        $scope.booking.resourceDetails.resourceId = $('#resSelect :selected').val();
        $scope.booking.resourceDetails.resourceName = $('#resSelect :selected').text();

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
            $('#wrapper').show();
            $('#spinner').hide();
            if (response.status == 200) {

//                if (itemObj.bookBtn == "new" || angular.isUndefined(itemObj.bookBtn)) {
//                    userDetails.addCurrentBooking(response.data);
//                } else {
//                    userDetails.editCurrentBooking(response.data);
//                }


                Notification({
                    message: 'Your Current Booking is ' + response.data.status + '.',
                    title: 'Booking Status',
                    delay: 2000
                });
//                if(response.data.status == 'Approved' && (itemObj.bookBtn == "new" || angular.isUndefined(itemObj.bookBtn))) {
	                setTimeout(function() {
	                    $window.location.href = 'index.html';
	                }, 2500);
//                }

            }
        }).error(function(response) {
            Notification.error({
                message: "Couldn't establish connection",
                delay: 2000
            });
        });
    }

    // datetime picker
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
            $('#datePicker').data('DateTimePicker').minDate($scope.date);
            if (event.date.format().substring(0, 10) === moment().format().substring(0, 10)) {
                $('#startTime').data('DateTimePicker').minDate(moment({
                    h: new Date().getHours(),
                    m: new Date().getMinutes()
                }));
            } else {
                $('#startTime').data('DateTimePicker').minDate(0);
            }
        });

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
            $('#endTime').data('DateTimePicker').minDate(event.date);
        });

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

    }
    //end of datetimepicker
};

homePage.controller('dashboardCtrl', function($rootScope, $scope, $modal, $http, $filter, userDetails, utilityFunctions) {

    $scope.currentUser = userDetails.getCurrentUser();
    $scope.date = $filter('date')(new Date(), 'yyyy-MM-dd');
    $scope.currentTime = $filter('date')(new Date(), 'HH:mm:ss');


    $http({
        method: 'GET',
        url: 'http://localhost:8080/Project-Authentication/resources/getAll',
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

homePage.controller('calendarCtrl', function($rootScope, $scope, $http, $modal, userDetails, utilityFunctions, Notification) {
    $rootScope.$on("populateResources", function() {
        $scope.allResources = utilityFunctions.getAllResources();
        $scope.currentUser = userDetails.getCurrentUser();

        $http({
            method: 'GET',
            url: 'http://localhost:8080/Project-Authentication/bookings/getApprovedBookings?employeeId=' + $scope.currentUser.employeeId,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            if (response.status == 200) {
                $scope.allApprovedBookings = response.data;
            }
            $http({
                method: 'POST',
                url: 'http://localhost:8080/Project-Authentication/bookings/getApprovedbookingsByEmployeeId',
                data: $scope.currentUser,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function(response) {
                if (response.status == 200) {
                    $scope.currentUser.bookingsMade = response.data;
                    userDetails.setCurrentUser($scope.currentUser);
                }
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

    $scope.showCalendar = function() {
        $('#calendar').fullCalendar({
            defaultView: 'agendaDay',
            defaultDate: $scope.date,
            editable: true,
            selectable: true,
            eventLimit: true, // allow "more" link when too many events
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

            events: function(start, end, timezone, callback) {
                var events = [];

                $($scope.allApprovedBookings).each(function() {
                    var res = $(this).attr('resourceDetails');
                    var startTime = $(this).attr('date') + 'T' + $(this).attr('startTime') + '+05:30';
                    var endTime = $(this).attr('date') + 'T' + $(this).attr('endTime') + '+05:30';

                    events.push({
                        id: $(this).attr('bookingId'),
                        title: $(this).attr('title') + "\n" + $(this).attr('description') + "\nParticipants:  \b" + $(this).attr('numberOfParticipants'),
                        start: startTime, // will be parsed
                        end: endTime,
                        editable: false,
                        resourceId: $(res).attr('resourceId'),
                        textColor: 'black',
                        color: '#5fefe6'
                    });
                });

                $($scope.currentUser.bookingsMade).each(function() {
                    var res = $(this).attr('resourceDetails');
                    var startTime = $(this).attr('date') + 'T' + $(this).attr('startTime') + '+05:30';
                    var endTime = $(this).attr('date') + 'T' + $(this).attr('endTime') + '+05:30';
                    var currentTime = new Date().getHours() + ":" + new Date().getMinutes();

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
                        start: startTime, // will be parsed
                        end: endTime,
                        editable: editableValue,
                        resourceId: $(res).attr('resourceId'),
                        textColor: 'black',
                        color: '#9ccefc'
                    });
                });

                callback(events);
            },

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

            eventAllow: function(dropLocation, draggedEvent) {
                if (dropLocation.resourceId == draggedEvent.resourceId) {
                    return true;
                } else {
                    return false;
                }
            },

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

            eventDrop: function(event, revertFunc) {
                $scope.callShowModal(event);
            },

            eventResize: function(event) {
                $scope.callShowModal(event);
            },


            eventMouseover: function(calEvent, jsEvent) {
                var title = calEvent.title.substring(0, calEvent.title.indexOf('\n') + 1);
                var description = calEvent.title.substring(calEvent.title.indexOf('\n') + 1, calEvent.title.indexOf('\nP'));
                var numberOfParticipants = calEvent.title.substring(calEvent.title.indexOf('\b'));

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

            eventMouseout: function(calEvent, jsEvent) {
                $(this).css('z-index', 8);
                $('.tooltipevent').remove();
            }

        });
    }

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
