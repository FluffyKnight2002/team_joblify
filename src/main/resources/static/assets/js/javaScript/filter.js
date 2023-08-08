let titleSpinner = $(".title-input-spinner").hide();
let vacancies = [];
let resultCount = 0;
let totalPages = 0;
let currentPages = 0;
async function applyFilter() {
  // Get selected values
  let sortBy = $('input[name="sortBy"]:checked').val();
  let datePosted = $('input[name="datePosted"]:checked').val();
  let position = $('#title-input').val();
  let jobType = $('input[name="jobType"]:checked').val() === undefined ? null :
      $('input[name="jobType"]:checked').serializeArray().map(item => item.value);
  let level = $('input[name="level"]:checked').val() === undefined ? null :
      $('input[name="level"]:checked').serializeArray().map(item => item.value);
  let under10Applicants = $('input[name="under10"]:checked').val() === undefined ? false : true;
  let status = $('input[name="status"]:checked').val() === undefined ? "OPEN" : "";

  console.log("Sort By Sort By:", sortBy);
  console.log("Sort By Date Posted:", datePosted);
  console.log("Sort By Job Type:", jobType);
  console.log("Sort By Level:",level);
  console.log("Under 10 Applicants:",under10Applicants);
  console.log("Show both :",status);

    try {
        const data = await filterJobs(sortBy, datePosted, position, jobType, level, under10Applicants, status);
        totalPages = data.totalPages;
        resultCount = data.totalElements;
        console.log("Result Count from apply: " + resultCount);
        console.log("Total Pages: from apply" + totalPages);
        console.log(data);
        vacancies = data.content; // Assuming 'content' holds the actual array of VacancyView objects
        // Update result count display
        console.log("Result count : " + resultCount);
        console.log("Result con : " + $('#result-count'));
        $('#result-count').html(resultCount); // Show loading or some indication
        // Call showResult here, after the data is available
        // showResult();
        return data;
    } catch (error) {
        // Handle errors
        console.log(error);
    }
  // Apply filter logic based on the selected values
  // Replace this with your actual filter implementation
}

function resetFilter() {
  // Reset all filter options to their default values
  document.getElementById("sortRelevant").checked = true;
  document.getElementById("dateAny").checked = true;
  document.getElementById("positionInput").value = "";
}

// Function to open the filter
function openFilter() {
  document.getElementById("filterOffcanvas").classList.add("show");
  document.body.classList.add("offcanvas-open");
}

// Function to close the filter
function closeFilter() {
  document.getElementById("filterOffcanvas").classList.remove("show");

  // Wait for the transition to complete before removing the offcanvas class
  setTimeout(function() {
    document.getElementById("filterOffcanvas").classList.remove("offcanvas-transition");
    document.body.classList.remove("offcanvas-open");
  }, 300); // 300 milliseconds = transition duration
}

async function filterJobs(sortBy, datePosted, position, jobType, level, under10Applicants, status) {
    let filterData = {
        sortBy: sortBy,
        datePosted: datePosted,
        position: position,
        jobType: jobType,
        level: level,
        under10Applicants: under10Applicants,
        status: status
    };

    console.log("Form : ", JSON.stringify(filterData)); // Log the filter data

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");

    console.log(csrfToken);

    try {
        const response = await fetch("vacancy/filter", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-Token': csrfToken,
            },
            body: JSON.stringify(filterData),
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        throw error;
    }
}

$("#title-input").autocomplete({
  minLength: 2,
  source: function(request, response) {
    titleSpinner.show();
    console.log("Spinner loading..")
    $.ajax({
      url: "/fetch-titles", // Replace with your server-side endpoint to fetch names
      data: {
        term: request.term
      },
      success: function(data) {
        if(data.length == 0) {
          console.log(" + Add new ");
          response(["+ Add new"]);
        }else {
          console.log("Spinner hide")
          response(data);
        }
        titleSpinner.hide()
      }
    });
  },
  open: function(event, ui) {
    let menu = $(this).autocomplete("widget");
    let maxHeight = 200; // Set the maximum height for the suggestion box
    let itemCount = menu.children('li').length;
    menu.css('max-height', maxHeight + 'px');
    if (itemCount > 5) {
      menu.css({
        'overflow-y': 'auto',
        'overflow-x': 'hidden' // Hide the horizontal scrollbar
      });
    } else {
      menu.css('overflow', 'hidden');
    }
    menu.css( {
      "font-size" : "0.8rem",
      "border-radius": "0.25rem"
    });
    menu.find(".ui-menu-item").css( {
      "color" :"#6c757d"});
  },
  select: function(e, ui) {
    let titleValue = (ui.item.value === "+ Add new") ? $('#title-input').val() : ui.item.value;
    $('#title-input').val(titleValue);
    return false;
  }
});

// Function to update result count and trigger filter
// function updateResultCountAndFilter() {
//     applyFilter();
// }

function reconvertToString(input) {
  // Replace underscores with spaces and convert to title case
  if (input === "ON_SITE") {
    return "On-site";
  }
  return input.split('_').map(word => word.charAt(0) + word.slice(1).toLowerCase()).join(' ');
}

function updatePaginationUI(totalPages, currentPage) {
    const paginationContainer = $("#pagination-container");
    paginationContainer.empty();

    if (totalPages <= 1) {
        // Don't show pagination if there's only one page
        return;
    }

    // Determine the range of pages to display
    const maxPageLinks = 3; // Number of page links to display
    currentPage = currentPage + 1;
    let startPage, endPage;

    if (totalPages <= maxPageLinks) {
        startPage = 1;
        endPage = totalPages;
    } else {
        // Calculate the start and end pages, considering the current page
        startPage = Math.max(1, currentPage - Math.floor(maxPageLinks / 2));
        endPage = Math.min(totalPages, startPage + maxPageLinks - 1);

        // Adjust the start and end pages if the current page is near the boundaries
        if (endPage - startPage + 1 < maxPageLinks) {
            if (currentPage <= Math.ceil(maxPageLinks / 2)) {
                endPage = Math.min(totalPages, startPage + maxPageLinks - 1);
            } else {
                startPage = Math.max(1, endPage - maxPageLinks + 1);
            }
        }
    }

    console.log("currentPage : ",currentPage);
    console.log("Start page : ",startPage);
    console.log("End page : ",endPage);

    // Create the previous button
    const startPageButton = `
        <li class="page-item">
            <a class="page-link" href="#" onclick="loadVacancies(${startPage-1})" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
    `;

    // Create the next button
    const lastPageButton = `
        <li class="page-item">
            <a class="page-link" href="#" onclick="loadVacancies(${endPage-1})" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    `;

    // Create the page links
    const pageLinks = [];
    for (let i = startPage; i <= endPage; i++) {
        const activeClass = i === currentPage ? "active-page" : "";
        const pageLink = `
            <li class="page-item">
                <a class="page-link ${activeClass}" href="#" onclick="loadVacancies(${i-1})">${i}</a>
            </li>
        `;
        pageLinks.push(pageLink);
    }

    // Combine all the components to form the pagination UI
    const paginationUI = `
        <nav aria-label="Page navigation example">
            <ul class="pagination">
                ${startPageButton}
                ${pageLinks.join("")}
                ${lastPageButton}
            </ul>
        </nav>
    `;

    paginationContainer.append(paginationUI);
}

function showResult() {
    console.log(vacancies)
  $("#jobs-container").empty();

  // Loop through each vacancy and create the card dynamically
  vacancies.forEach(function (vacancy) {
    const card = `
                <div class="card flex-md-row">
                    <div class="">
                        <img class="m-3" src="/assets/images/candidate-images/backend_icon.png" alt="Backend Icon" width="50" height="50">
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${vacancy.position}<span class="applicants-text d-inline-block d-md-inline-block"><i class='bx bxs-droplet'></i> ${vacancy.applicants} applicants</span></h5>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip" data-placement="bottom" title="Post(Job type)"><i class='bx bxs-briefcase'></i>${vacancy.post} (${reconvertToString(vacancy.jobType)})</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip" data-placement="bottom" title="Salary"><i class='bx bx-money'></i>${vacancy.salary}</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip" data-placement="bottom" title="Posted time"><i class='bx bx-time'></i> ${timeAgo(vacancy.updatedTime)}</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip" data-placement="bottom" title="Location"><i class="bi bi-geo-alt-fill"></i>${vacancy.address}</span>
                    </div>
                    <div class="d-flex flex-column justify-content-center justify-content-md-center align-items-end mb-3">
                        <a href="/job-detail?id=${vacancy.id}" class="btn btn-sm btn-primary mb-1">More Details</a>
                        <span class="default-font me-4 d-inline-block end-date-text" data-toggle="tooltip" data-placement="bottom" title="Close date"><i class='bx bx-calendar-exclamation'></i> ${changeTimeFormat(vacancy.closeDate)}</span>
                    </div>
                </div>
            `;
    // Append the card to the container
    $("#jobs-container").append(card);
  });

  // Update the pagination UI
  updatePaginationUI(totalPages, currentPages);
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
}

$('#show-result-btn').on('click', function(event) {
    event.preventDefault();
    showResult(vacancies);
});

async function loadVacancies(page) {
    // Get selected values
    let sortBy = $('input[name="sortBy"]:checked').val();
    let datePosted = $('input[name="datePosted"]:checked').val();
    let position = $('#title-input').val();
    let jobType = $('input[name="jobType"]:checked').val() === undefined ? null :
        $('input[name="jobType"]:checked').serializeArray().map(item => item.value);
    let level = $('input[name="level"]:checked').val() === undefined ? null :
        $('input[name="level"]:checked').serializeArray().map(item => item.value);
    let under10Applicants = $('input[name="under10"]:checked').val() === undefined ? false : true;
    let status = $('input[name="status"]:checked').val() === undefined ? "OPEN" : "";

    try {
        const data = await applyFilter(sortBy, datePosted, position, jobType, level, under10Applicants, status, page);
        totalPages = data.totalPages;
        vacancies = data.content; // Update vacancies array
        console.log("Loaded vacancies:", vacancies);

        updatePaginationUI(totalPages, page);
    } catch (error) {
        // Handle errors
        console.log(error);
    }
}

$(document).ready(async function () {
    // Add event listeners to relevant input elements
    $('input[name="sortBy"]').on('change', applyFilter);
    $('input[name="datePosted"]').on('change', applyFilter);
    $('#title-input').on('input', applyFilter);
    $('input[name="jobType"]').on('change', applyFilter);
    $('input[name="level"]').on('change', applyFilter);
    $('input[name="under10"]').on('change', applyFilter);
    $('input[name="status"]').on('change', applyFilter);

    // vacancies =await applyFilter();
    console.log("Doc ready : " +  vacancies);
    await loadVacancies();
});