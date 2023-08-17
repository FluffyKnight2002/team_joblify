let stompClient;
let status = false;
let notificationLight = $('#noti-light');
let selectedNotifications = new Set(); // Keep track of selected notification IDs
let isDropdownVisible = false;
let notiDropdown = $('#noti-dropdown');
let deleteBtn = $('#delete-noti-btn');
notificationLight.hide();
$(document).ready(function() {

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log("WebSocket Connection Established"); // Debugging statement
        console.log(frame);
        stompClient.subscribe('/all/notifications', function(result) {
            // fetchCount();
            fetchNotifications();
            // console.log("Received notification:"); // Debugging statement
            // console.log(result.body);
            // var newNotification = JSON.parse(result.body);
            // console.log(newNotification); // Debugging statement
            // handleNewNotification(newNotification);
        });
    });
    console.log(notiDropdown)
    fetchNotifications();

    notiDropdown.hasClass('.show', function () {
        isDropdownVisible = false;
        console.log("Dropdown is hidden. isDropdownVisible: ", isDropdownVisible);
        updateDeleteButtonAndSelectedNotifications();
    });

        function updateDeleteButtonAndSelectedNotifications() {
        if (!isDropdownVisible) {
            deleteBtn.html('Delete All <i class="bi bi-trash-fill"></i>');
            selectedNotifications = new Set();
        }
    }
});

$('#view-noti-btn').on('click', function(e) {
    e.preventDefault();
    console.log("Click");
    fetchNotifications();
});

function fetchNotifications() {
    fetch("/notifications/show")
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            console.log("Data received from server:", data); // Debugging statement
            if (data.length === 0) {
                console.log("No notifications found.");
                $('#notifications-container').empty();
                $('#notifications-container').append('<div class="text-muted sub-title text-center p-3">No notification to show</div>')
            } else {
                displayNotification(data);
            }
        })
        .catch(error => console.error('Error fetching notifications:', error));
}


function displayNotification(notifications) {
    const notificationContainer = $('#notifications-container');
    notificationContainer.empty(); // Clear existing notifications
    console.log(notifications)

    /* Loop through notifications and call the addNotification function for each notification */
    notifications.forEach(function (notification) {
        addNotifications(notification);
    });
}

/* Function to add notifications to the notificationContainer */
function addNotifications(notification) {
    const isSeen = notification.seen;
    const isDeleted = notification.deleted;
    console.log(isSeen)
    console.log(typeof(isSeen))
    const notificationElement = $('<div class="pe-3 border-bottom dropdown-item" style="cursor: pointer">').html(`
    <div class="d-flex justify-content-between mb-2">
        <a href="${notification.link}" data-noti-id="${notification.id}" onclick="makeAsRead(${notification.id})" style="cursor: pointer">
            ${notification.message}
        </a>
        ${isSeen === false ? '<span class="d-inline-block badge bg-danger text-white m-1 rounded-pill text-center" style="font-size: 0.6rem">New</span>' : ''}
    </div>
    <div>
        <h6 class="text-start text-muted" style="font-size: 13px">${timeAgo(notification.time)}<i class="bi bi-clock-history p-1 pt-2"></i></h6>
    </div>
  `);

    // Add the notification ID to the set when clicked and toggle class
    notificationElement.on('click', function (event) {
        event.stopPropagation(); // Prevent the event from propagating to the parent elements
        const notificationId = notification.id;
        if (selectedNotifications.has(notificationId)) {
            selectedNotifications.delete(notificationId);
        } else {
            selectedNotifications.add(notificationId);
        }
        $(this).toggleClass('selectedNotification'); // Toggle the class on click

        if(selectedNotifications.size != 0) {
            deleteBtn.html('Delete <i class="bi bi-trash-fill"></i>');
        } else {
            deleteBtn.html('Delete All <i class="bi bi-trash-fill"></i>');
        }
    });

    if(isSeen === false) {
        notificationLight.show();
    }

    if(isDeleted === false) {
        $('#notifications-container').append(notificationElement);
    }
}

function handleNewNotification(notification) {
    // Update the notification count
    var currentCount = parseInt($('#notifications-count').text(), 10);
    $('#notifications-count').text(currentCount + 1);

    // Set the 'seen' property to 'false' for the new notification
    notification.seen = 'false';

    // Check if the notifications container is currently being viewed
    const isViewingNotifications = $('#view-noti-btn').hasClass('show');
    if (isViewingNotifications) {
        // If the container is open, directly add the new notification
        addNotifications(notification);
    }

    // Store the new notification message in shared state (localStorage)
    var notifications = JSON.parse(localStorage.getItem('notifications')) || [];
    notifications.push(notification);
    localStorage.setItem('notifications', JSON.stringify(notifications));
}

// Function to convert time to a relative format
function timeAgo(time) {
    const currentTime = new Date();
    const inputTime = new Date(time);
    const timeDifferenceInSeconds = Math.floor((currentTime - inputTime) / 1000);

    // Define time units in seconds
    const minute = 60;
    const hour = 60 * minute;
    const day = 24 * hour;
    const week = 7 * day;
    const month = 30 * day;

    if (timeDifferenceInSeconds < minute) {
        return 'Just now';
    } else if (timeDifferenceInSeconds < hour) {
        const minutesAgo = Math.floor(timeDifferenceInSeconds / minute);
        return `${minutesAgo} minute${minutesAgo > 1 ? 's' : ''} ago`;
    } else if (timeDifferenceInSeconds < day) {
        const hoursAgo = Math.floor(timeDifferenceInSeconds / hour);
        return `${hoursAgo} hour${hoursAgo > 1 ? 's' : ''} ago`;
    } else if (timeDifferenceInSeconds < week) {
        const daysAgo = Math.floor(timeDifferenceInSeconds / day);
        return `${daysAgo} day${daysAgo > 1 ? 's' : ''} ago`;
    } else if (timeDifferenceInSeconds < month) {
        const weeksAgo = Math.floor(timeDifferenceInSeconds / week);
        return `${weeksAgo} week${weeksAgo > 1 ? 's' : ''} ago`;
    } else {
        // Display the date in the format: 'MMM DD YYYY'
        const formattedDate = inputTime.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
        return formattedDate;
    }
}

// Delete notifications
// Assuming deleteBtn is the delete button element
deleteBtn.on('click', function () {
    const buttonText = deleteBtn.html().trim();
    if (selectedNotifications.size === 0) {
        deleteNotification();
    } else {
        deleteSelectedNotifications(selectedNotifications);
    }
    // Use console.log to check if the dropdown is hidden
    console.log($('#noti-dropdown').is(':hidden'));
});

// Make as read
function makeAsRead(notificationId) {
    console.log("notificationId : " + notificationId)
    fetch(`/notifications/make-as-read?id=${notificationId}`)
        .then(notificationLight.hide())
        .then(fetchNotifications)
}

function makeAllAsRead() {
    fetch("/notifications/make-all-as-read")
        .then(notificationLight.hide())
        .then(fetchNotifications)
}

// Delete notifications
function deleteNotification(){
    fetch(`/notifications/delete-all-notification`)
        .then(notificationLight.hide())
        .then(fetchNotifications)
}

function deleteSelectedNotifications(selectedNotifications) {
    const selectedIds = Array.from(selectedNotifications); // Convert the set to an array

    console.log(selectedIds);
    fetch(`/notifications/delete-notifications?notifications=`+ selectedIds)
        .then(notificationLight.hide())
        .then(fetchNotifications);
}


// Example usage:
// const inputTime = '2023-07-23T23:03:52.123781';
// const result = timeAgo(inputTime);
// $('#timeDisplay').text(result);