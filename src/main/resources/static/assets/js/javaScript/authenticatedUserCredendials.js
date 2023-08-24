const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

// This event listener will trigger your function when the DOM content is loaded
document.addEventListener("DOMContentLoaded", function () {
    authenticatedUserData();
});

async function authenticatedUserData() {
    try {
        const response = await fetch('/authenticated-user-data', {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            }
        });
        const data = await response.json();
        console.log(data);
        const name = document.getElementById('authenticated-name');
        const username = document.getElementById('authenticated-username');
        const department = document.getElementById('authenticated-department');
        const profileLink = document.getElementById('profile-link');
        const profileImg = document.getElementById('profile-img');

        name.innerHTML = data.name;
        username.innerHTML = data.username;
        department.innerHTML = data.department;
        profileLink.href = '/user-profile-edit?email=' + encodeURIComponent(data.email);
        profileImg.src = 'data:image/png;base64,' + data.photo;
        const loader = document.getElementById('loader');
        const credentials = document.getElementById('credentials');

        loader.remove();
        credentials.style.display = 'inline-block';

    } catch (error) {
        console.error("Error at fetching authenticated User Data: " + error);
    }
}