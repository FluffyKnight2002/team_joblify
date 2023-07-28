var stompClient;
var status = false;
var notificationLight = $('#noti-light');
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
    fetchNotifications();
    // fetchCount();
});

$('#view-noti-btn').on('click', function(e) {
    e.preventDefault();
    console.log("Click");
    fetchNotifications();
});

// function fetchCount() {
//     fetch("/notifications/count")
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error("Network response was not ok");
//             }
//             return response.json();
//         })
//         .then(data => showNotificationCount(data))
//         .catch(error => console.error('Error fetching notifications count:', error));
// }
//
// function showNotificationCount(data) {
//     $('#notifications-count').text(data); // Update the notification count live
// }

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
            } else {
                displayNotification(data);
            }
        })
        .catch(error => console.error('Error fetching notifications:', error));
}


function displayNotification(notifications) {
    const notificationContainer = $('#notifications-container');
    notificationContainer.empty(); // Clear existing notifications

    /* Loop through notifications and call the addNotification function for each notification */
    notifications.forEach(function (notification) {
        addNotifications(notification);
    });
}

/* Function to add notifications to the notificationContainer */
function addNotifications(notification) {
    const isNewNotification = notification.seen;
    // console.log(isNewNotification)
    // console.log(typeof(isNewNotification))
    const notificationElement = $('<div class="pe-3 border-bottom dropdown-item">').html(`
    <div class="d-flex justify-content-between mb-2">
        <a th:href="@{${notification.link}}" onclick="makeAsRead(${notification.id})" style="cursor: pointer">
            ${notification.message}
        </a>
        ${isNewNotification === true ? '<span class="d-inline-block badge bg-danger text-white m-1 rounded-pill text-center" style="font-size: 0.6rem">New</span>' : ''}
    </div>
    <div>
        <h6 class="text-start text-muted" style="font-size: 13px">${timeAgo(notification.time)}<i class="bi bi-clock-history p-1 pt-2"></i></h6>
    </div>
  `);
    if(isNewNotification === true) {
        notificationLight.show();
    }
    $('#notifications-container').append(notificationElement);
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

// Make as read
function makeAsRead(notificationId) {
    console.log("notificationId : " + notificationId)
    fetch(`/notifications/makeAsRead?id=${notificationId}`)
        .then(notificationLight.hide())
        .then(fetchNotifications)
    // window.location.href = link;
}

function makeAllAsRead() {
    fetch("/notifications/makeAllAsRead")
        .then(notificationLight.hide())
        .then(fetchNotifications)
}

// Delete notifications
function deleteNotification(notificationId){
    fetch(`/notifications/delete?id=${notificationId}`)
        .then(notificationLight.hide())
        .then(fetchNotifications)
}

// Example usage:
// const inputTime = '2023-07-23T23:03:52.123781';
// const result = timeAgo(inputTime);
// $('#timeDisplay').text(result);