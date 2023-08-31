$(document).ready(function () {

    showLastVacancies();
});

function showLastVacancies() {

    fetch("/vacancy/show-last")
        .then((response)=> response.json())
        .then(data => {
            console.log("Data received from server:", data);
            // Handle the data received from the server
            if(data.length > 0) {
                displayVacancies(data); // Assuming you have the displayVacancies function to handle the data
            }else {
                const card = `
                <div class="d-flex justify-content-center align-items-center">
                    <img src="/assets/images/candidate-images/nothing_to_show.jpg" class="nothing-to-show" width="auto" height="300px"/>
                </div>
                <h4 class="text-center text-muted sub-title fw-bolder">No vacancy was found.</h4>
                `;

                // Append the card to the container
                $("#card-container").append(card);
            }
        })
        .catch(error => console.error('Error fetching vacancies:', error));
}

function displayVacancies(data) {
    let vacancyCard;
    const cardContainer = $('#card-container');
    for(let vacancy of data) {
        vacancyCard = `
                <div class="card flex-md-row">
                    <div class="">
                        <img class="m-3" src="/assets/images/candidate-images/backend_icon.png" alt="Backend Icon" width="50" height="50">
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${vacancy.position}<span class="applicants-text d-inline-block d-md-inline-block"><i class='bx bxs-droplet'></i> ${vacancy.applicants} applicants</span></h5>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block"><i class='bx bxs-briefcase' data-toggle="tooltip" data-placement="bottom" title="Post(Job type)"></i> ${vacancy.post} (${reconvertToString(vacancy.jobType)})</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block"><i class='bx bx-money' data-toggle="tooltip" data-placement="bottom" title="Salary"></i> ${vacancy.salary === 0 ? 'Negotiate' : convertToLakhs(vacancy.salary)}</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block"><i class='bx bx-time' data-toggle="tooltip" data-placement="bottom" title="Posted time"></i> ${timeAgo(vacancy.openDate)}</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block"><i class="bi bi-geo-alt-fill" data-toggle="tooltip" data-placement="bottom" title="Location"></i> ${vacancy.address} <span>( ${reconvertToString(vacancy.onSiteOrRemote)} )</span></span>
                    </div>
                    <div class="d-flex flex-column justify-content-center justify-content-md-center align-items-end mb-3">
                        <a href="/job-detail?id=${vacancy.id}" class="btn btn-sm btn-primary mb-1" id="more-detail-btn" style="min-width: 88.59px">More Detail</a>
                        <span class="default-font me-4 d-inline-block end-date-text"><i class='bx bx-calendar-exclamation' data-toggle="tooltip" data-placement="bottom" title="Close date"></i> ${changeTimeFormat(vacancy.closeDate)}</span>
                    </div>
                </div>
            `;

        // Initialize Bootstrap tooltips
        $(function () {
            $('[data-toggle="tooltip"]').tooltip({
                placement: 'bottom' // Set the desired placement here
            });
        });
        cardContainer.append(vacancyCard);
    }
}

// Change time format
function changeTimeFormat(time) {

    // Parse the date string to a JavaScript Date object
    var date = new Date(time);

    // Array to map month numbers to month names
    var monthNames = [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];

    // Get the day of the month
    var day = date.getDate();

    // Determine the suffix for the day (st, nd, rd, or th)
    var suffix;
    if (day >= 11 && day <= 13) {
        suffix = "th";
    } else {
        switch (day % 10) {
            case 1: suffix = "st"; break;
            case 2: suffix = "nd"; break;
            case 3: suffix = "rd"; break;
            default: suffix = "th";
        }
    }

    // Format the date as "Dayth Month Year" (e.g., "27th Jul 2023")
    var formattedDate = day + suffix + " " + monthNames[date.getMonth()];
    return formattedDate;

    // Initialize Bootstrap tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}