// // WebSocket connection
// const socket = new WebSocket('ws://localhost:8080/ws');
//
// // Subscribe to the '/topic/allNotification' destination
// socket.onopen = function () {
//     socket.send('/app/notifications/show');
// };
//
// // Handle incoming notifications
// socket.onmessage = function (event) {
//     const notification = JSON.parse(event.data);
//     displayNotification(notification.message);
// };
//
// // Display notifications in the UI
// function displayNotification(message) {
//     const notificationContainer = document.getElementById('notifications-container');
//     const notificationElement = document.createElement('li');
//     notificationElement.innerHTML = `<a class="dropdown-item" href="#">${message}</a>`;
//     notificationContainer.appendChild(notificationElement);
// }