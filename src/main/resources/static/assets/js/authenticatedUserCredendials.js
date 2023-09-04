const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

// This event listener will trigger your function when the DOM content is loaded
document.addEventListener("DOMContentLoaded",  async function () {
    authenticatedUserData();

    fetch('/getCookies')
            .then(response => response.json())
            .then(data => {
                // Process the data received from the servlet
                console.log(data + 'cookie');
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });

});

async function authenticatedUserData() {
    try {
        const response = await fetch('/authenticated-user-data', {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            }
        });

        if (response.ok) {
            const [userDetails, passwordMatches] = await response.json();
            console.log(userDetails);
            console.log(passwordMatches);
            console.log(userDetails.user.role)
            const name = document.getElementById('authenticated-name');
            const username = document.getElementById('authenticated-username');
            const department = document.getElementById('authenticated-department');
            const profileLink = document.getElementById('profile-link');
            const profileImg = document.getElementById('profile-img');
            const passwordChange = document.getElementById('self-password-change');

            const role = userDetails.user.role.replace(/_/g, ' ');

            passwordChange.href = '/password-change?email=' + encodeURIComponent(userDetails.email);
            name.innerHTML = userDetails.name + ' <span class="d-block text-muted text-right sub-title" style="font-size: 0.7rem">(' + role + ')</span>';
            username.innerHTML = userDetails.username;
            department.innerHTML = userDetails.department;
            profileLink.href = '/user-profile-edit?email=' + encodeURIComponent(userDetails.email);
            profileImg.src = 'data:image/png;base64,' + userDetails.photo;
            const loader = document.getElementById('loader');
            const credentials = document.getElementById('credentials');
            if (loader) {
                loader.remove();
            }
            credentials.style.display = 'inline-block';

            if (passwordMatches) {
                iziToast.warning({
                    title: 'Caution',
                    message: 'You\'re Still Using Default Password. Please Change Immediately',
                    position: 'topCenter',
                    backgroundColor: '#f58787',
                    progressBarColor: 'red', // Set the progress bar color to red
                    theme: 'dark', // Optionally, you can set the theme to 'dark' to ensure the text color is visible on the red background
                });
            }



        } else {
            console.error('Failed to fetch authenticated user data:', response.status, response.statusText);
        }

    } catch (error) {
        console.error('An error occurred while fetching authenticated user data:', error);
    }
}


