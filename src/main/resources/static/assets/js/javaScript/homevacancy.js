$(document).ready(function () {

    $(window).load(showLastVacancies());
});

function showLastVacancies() {

    fetch("/vacancy/show-last")
        .then((response)=> response.json())
        .then(data => {
            console.log("Data received from server:", data);
            // Handle the data received from the server
            displayVacancies(data); // Assuming you have the displayVacancies function to handle the data
        })
        .catch(error => console.error('Error fetching vacancies:', error));
}

function displayVacancies(data) {
    var vacancyCard;
    const cardContainer = $('#card-container');
    for(var vacancyData of data) {
        vacancyCard = `
        <div class="card flex-md-row">
                <div class="">
                    <img class="m-3" src="/assets/images/candidate-images/backend_icon.png" alt="Backend Icon" width="50" height="50">
                </div>
                <div class="card-body">
                    <h5 class="card-title">${vacancyData.position}<span class="applicants-text d-inline-block d-md-inline-block"><i
                                class='bx bxs-droplet'></i> 11 applicants</span></h5>
                    <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip"
                        data-placement="bottom" title="Post(Job type)"><i class='bx bxs-briefcase'></i>${vacancyData.post} (${vacancyData.type})</span>
                    <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip"
                        data-placement="bottom" title="Salary"><i class='bx bx-money'></i>
                        ${vacancyData.salary}</span>
                    <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip"
                        data-placement="bottom" title="Posted time"><i class='bx bx-time'></i> 1
                        Hour ago</span>
                    <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip"
                        data-placement="bottom" title="Location"><i class='bx bx-location-plus'></i>${vacancyData.address}</span>
                </div>
                <div class="d-flex flex-column justify-content-center justify-content-md-center align-items-end mb-3">
                    <a href="/job-details?id=${vacancyData.id}" class="btn btn-sm btn-primary mb-1">More Details</a>
                    <span class="default-font me-4 d-inline-block end-date-text" data-toggle="tooltip" data-placement="bottom"
                        title="Close date"><i class='bx bx-calendar-exclamation'></i>${changeTimeFormat(vacancyData.closeDate)}</span>
                </div>
        </div>
    `;
        cardContainer.prepend(vacancyCard);
    }
}

// Change time format
function changeTimeFormat(time) {
    var dateString = "2023-07-27";

    // Parse the date string to a JavaScript Date object
    var date = new Date(dateString);

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
}