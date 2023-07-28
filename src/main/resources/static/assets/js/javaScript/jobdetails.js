var currentId = new URLSearchParams(window.location.search).get("id");
// Function to fetch job data and create job card UI
function fetchJobsAndRenderUI() {

    fetch("/vacancy/show-others")
        .then((response) => response.json())
        .then(data => {
        // Assuming 'data' is an array of job objects with properties like title, applicants, jobType, salary, postedTime, location, and closeDate
        // Loop through the job data to create job cards
            $("#job-list-container").empty();
            console.log($('#job-list-container'))
            data.forEach(job => {
                const jobCard = `
                <div class="card">
                <div class="d-flex flex-row">
                    <div class="">
                        <img class="m-3" src="/assets/images/candidate-images/backend_icon.png" alt="Backend Icon" width="50" height="50">
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${job.position}<span
                                class="applicants-text d-inline-block d-md-inline-block"><i class='bx bxs-droplet'></i>
                                ${job.applicants}
                                applicants</span></h5>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip"
                              data-placement="bottom" title="Post(Job type)"><i
                                class='bx bxs-briefcase'></i> ${job.post}
                            (${job.type})</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip"
                              data-placement="bottom" title="Salary"><i class='bx bx-money'></i>
                            ${job.salary}</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip"
                              data-placement="bottom" title="Posted time"><i class='bx bx-time'></i>
                              ${timeAgo(job.updatedTime)}
                            </span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip"
                              data-placement="bottom" title="Location"><i
                                class='bx bx-location-plus'></i>${job.address}</span>
                    </div>
                </div>
                <div class="d-flex flex-column justify-content-center justify-content-md-center align-items-end mb-3">
                    <button type="button" class="btn btn-sm btn-primary mb-1" data-job-id="${job.id}">More Details</button>
                    <span class="default-font me-4 d-inline-block end-date-text" data-toggle="tooltip"
                          data-placement="bottom" title="Close date"><i class='bx bx-calendar-exclamation'></i>
                        ${changeTimeFormat(job.closeDate)}      
                    </span>
                </div>
            </div>
                `;
                if (job.id != currentId) {
                    // Append the job card to the container
                    $("#job-list-container").append(jobCard);
                }
            });
            // Now that the job cards are added, set up the click event handler
            $(".btn-primary").on("click", function() {
                // Get the job ID from the data attribute
                const jobId = $(this).data("job-id");

                // Call the changeDetail function with the jobId
                changeDetail(jobId);
            });
    });
}

// Function to fetch job details and update the container
function fetchJobDetails(id) {
    const urlParams = new URLSearchParams(window.location.search);
    const jobId = (id === undefined) ? urlParams.get("id") : id;
    currentId = jobId;
    console.log(jobId);

    fetch(`/vacancy/job-details?id=${jobId}`)
        .then(response => response.json())
        .then(data => {
            // Assuming data contains the job details returned from the server
            const jobDetailsContainer = $("#job-details-con");
            jobDetailsContainer.empty();

            // Update the entire job details container with the received HTML
            const jobContainer = `
                    <div class="detail-header">
                        <div class="title-sesion d-flex justify-content-between">
                            <h4 class="default-font text-start">${data.position}</h4>
                            <h6 class="text-muted"><i class='bx bx-time'></i>${timeAgo(data.updatedTime)}</h6>
                        </div>
                        <div>
                            <h6 class="text-danger" style="font-size: 0.7rem">Close in : ${changeTimeFormat(data.closeDate)}</h6>
                            <u class="text-muted">${data.department}</u>
                            <span><i class='bx bx-location-plus'></i>${data.address}</span>
                        </div>
                    </div>
                    <div class="content-con">
                        <div class="general-fects">
                    <span class="my-2 d-block"><i class='bx bxs-briefcase'></i>
                        ${data.post} (${data.type})</span>
                    <span class="my-2 d-block"><i class='bx bx-money'></i>
                        ${data.salary}</span>
                    <span class="my-2 d-block"><i class='bx bxs-award'></i>
                        ${data.lvl}</span>
                </div>

                <!-- Apply button start -->
                <div>
                    <button type="button" class="btn btn-primary rounded-pill my-3" data-bs-toggle="modal"
                        data-bs-target="#staticBackdrop">
                        Apply
                    </button>
                </div>

                <!-- Apply button end-->

                <div class="mb-3">
                    <h5>Job Description</h5>
                    <p>${data.descriptions}</p>
                </div>

                <div class="mb-3">
                    <h5>Job Responsibilities</h5>
                    <p>${data.responsibilities}</p>
                </div>

                <div class="mb-3">
                    <h5>Job Requirements</h5>
                    <p>${data.requirements}</p>
                </div>

                <div class="mb-3">
                    <h5>Preferences</h5>
                    <p>${data.preferences}</p>
                </div>

                <table class="w-100 mx-2 mb-3">
                    <tr>
                        <th>Working Day</th>
                        <th>${data.workingDays}</th>
                    </tr>
                    <tr>
                        <th>Working Hours</th>
                        <th>${data.workingHours}</th>
                    </tr>
                    <tr>
                        <th>Salary</th>
                        <th>${data.salary}</th>
                    </tr>
                    <tr>
                        <th>Job Location</th>
                        <th>${data.location}</th>
                    </tr>
                    <tr>
                        <th>Job Type</th>
                        <th>${data.type}</th>
                    </tr>
                </table>

                <div class="about-com-con container-fluid rounded-top py-3 bg-white">
                    <h4 class="mb-3">About the company</h4>
                    <p class="mb-3">ACE Data Systems Ltd. was founded in 1992 as a small software house and IT training
                        center. Nowadays, ACE Data Systems
                        Group has grown into a group of twenty-two companies with over eight hundred and fifty employees
                        with business
                        operations in software development and system integration, outsourcing, IT infrastructure and
                        cyber security, education
                        services, e-commerce and business consulting.</p>
                    <div class="mb-3">
                        <h6>Website Link : </h6>
                        <a href="http://www.acedatasystems.com" target="_blank"
                            class="default-font">http://www.acedatasystems.com</a>
                    </div>
                </div>
            </div>`;

            // Append the new job details content to the container
            jobDetailsContainer.append(jobContainer);
        })
        .catch(error => {
            console.error("Error fetching job details:", error);
        });
}

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

function updateURLParams() {
    if (history.pushState) {
        // Create a new URLSearchParams object with the current search parameters
        var searchParams = new URLSearchParams(window.location.search);

        // Set the "id" parameter to the new currentId value
        searchParams.set("id", currentId);

        // Get the new search string
        var newSearchParams = searchParams.toString();

        // Create a new URL with the updated search parameters
        var newURL = window.location.pathname + "?" + newSearchParams;

        // Update the URL without triggering a page reload
        history.pushState(null, "", newURL);
    }
}

function changeDetail(id) {
    console.log("ID : ",id)
    currentId = id;
    fetchJobDetails(id);
    fetchJobsAndRenderUI();
    updateURLParams();
}

// Call the fetchJobDetails function when the page loads
$(document).ready(function (){
    console.log(currentId);
    $('#footer-content').css('margin-top', '85px');
    fetchJobDetails();
    fetchJobsAndRenderUI();
});