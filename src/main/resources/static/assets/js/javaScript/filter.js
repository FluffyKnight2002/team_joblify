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
    let jobType = $('input[name="jobType"]:checked').val();
    let onSiteOrRemote = $('input[name="onSiteOrRemote"]:checked').val();
    let levelArray = $('input[name="level"]:checked').val() === undefined ? null :
        $('input[name="level"]:checked').serializeArray().map(item => item.value);
    // let levelString = levelArray.join(',');
    let isUnder10 = $('input[name="under10"]:checked').val() === undefined ? "false" : "true";
    let page = 0;
    let itemPerPage = 5;

    console.log("Sort By Sort By:", sortBy);
    console.log("Sort By Date Posted:", datePosted);
    console.log("Sort By Job Type:", jobType);
    console.log("Sort By Level:",levelArray);
    console.log("Under 10 Applicants:",isUnder10);
    console.log("Page from applyJobs :",page);

    try {
        const data = await filterJobs(sortBy, datePosted, position, jobType,onSiteOrRemote, levelArray, isUnder10, page, itemPerPage);
        totalPages = data.totalPages;
        resultCount = data.totalElements;
        console.log("On_Site or remote : " + onSiteOrRemote)
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

async function resetFilter(event) {
    event.preventDefault();

    // Reset all filter options to their default values
    $("#sortRecent").prop("checked", true);
    $("#dateAny").prop("checked", true);
    $("#title-input").val("");
    $("input[name='level']").prop("checked", false);
    $("#type-both").prop("checked",true);
    $("#both-on-and-remote").prop("checked", true);
    $("input[name='under10']").prop("checked", false);

    let dataOfVacancies =await applyFilter();
    showResult();
    console.log(dataOfVacancies.totalElements, "In reset");
    $('#result-count').html(dataOfVacancies.totalElements);
    // Check the viewport width
    const mediaQuery = window.matchMedia('(max-width: 767px)'); // Adjust the breakpoint as needed

    if (mediaQuery.matches) {
        // Close the offcanvas here
        closeFilter();
    }
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

async function filterJobs(sortBy, datePosted, position, jobType,onSiteOrRemote, level, isUnder10, page, itemPerPage) {
    let filterData = {
        sortBy: sortBy,
        datePosted: datePosted,
        position: position,
        jobType: jobType,
        onSiteOrRemote: onSiteOrRemote,
        level: level,
        isUnder10: isUnder10,
    };

    console.log("Form : ", JSON.stringify(filterData)); // Log the filter data

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");

    console.log(csrfToken);

    try {
        const response = await fetch(`vacancy/filter?page=${page}&pageSize=${itemPerPage}`, {
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
    minLength: 1,
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
            <a class="page-link" href="#" onclick="event.preventDefault();loadVacancies(0)" aria-label="Previous">
                <span aria-hidden="true">First</span>
            </a>
        </li>
    `;

    // Create the next button
    const lastPageButton = `
        <li class="page-item">
            <a class="page-link" href="#" onclick="event.preventDefault();loadVacancies(countLastPage())" aria-label="Next">
                <span aria-hidden="true">Last</span>
            </a>
        </li>
    `;

    // Update the page link creation section
    const pageLinks = [];
    for (let i = startPage; i <= endPage; i++) {
        const activeClass = i === currentPage ? "active-page" : "";
        const pageLink = `
        <li class="page-item text-center">
            <a class="page-link ${activeClass}" href="#" onclick="event.preventDefault();loadVacancies(${i-1})">${i}</a>
        </li>
    `;
        pageLinks.push(pageLink);
    }

    // Combine all the components to form the pagination UI
    const paginationUI = `
        <div class="sticky-bottom pagination-container">
            <nav aria-label="Page navigation">
            <ul class="pagination">
                ${startPageButton}
                ${pageLinks.join("")}
                ${lastPageButton}
            </ul>
        </nav>
        </div>
    `;

    paginationContainer.append(paginationUI);
}

function countLastPage() {
    let page =(vacancies.length % 5 > 0) ? totalPages : totalPages - 1;
    console.log("Last page : " , page)
    return page;
}

function showResult() {
    console.log(vacancies)
    $("#jobs-container").empty();

    if(vacancies.length > 0) {
        // Loop through each vacancy and create the card dynamically
        vacancies.forEach(function (vacancy) {
            const card = `
                <div class="card flex-md-row">
                    <div class="">
                        <img class="m-3" src="/assets/images/candidate-images/backend_icon.png" alt="Backend Icon" width="50" height="50">
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${vacancy.position}<span class="applicants-text d-inline-block d-md-inline-block"><i class='bx bxs-droplet'></i> ${vacancy.applicants} applicants</span></h5>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block"><i class='bx bxs-briefcase' data-toggle="tooltip" data-placement="bottom" title="Post(Job type)"></i> ${vacancy.post} (${reconvertToString(vacancy.jobType)})</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block"><i class='bx bx-money' data-toggle="tooltip" data-placement="bottom" title="Salary"></i> ${convertToLakhs(vacancy.salary)}</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block"><i class='bx bx-time' data-toggle="tooltip" data-placement="bottom" title="Posted time"></i> ${timeAgo(vacancy.openDate)}</span>
                        <span class="default-font mx-2 d-block d-md-block d-xl-inline-block"><i class="bi bi-geo-alt-fill" data-toggle="tooltip" data-placement="bottom" title="Location"></i> ${vacancy.address} <span>(${reconvertToString(vacancy.onSiteOrRemote)})</span></span>
                    </div>
                    <div class="d-flex flex-column justify-content-center justify-content-md-center align-items-end mb-3">
                        <a href="/job-detail?id=${vacancy.id}" class="btn btn-sm btn-primary mb-1 more-detail-btn" style="min-width: 88.59px">More Details</a>
                        <span class="default-font me-4 d-inline-block end-date-text closed-date" ><i class='bx bx-calendar-exclamation' data-toggle="tooltip" data-placement="bottom" title="Close date"></i> ${changeTimeFormat(vacancy.closeDate)}</span>
                    </div>
                </div>
            `;
            // Append the card to the container
            $("#jobs-container").append(card);

            // Initialize Bootstrap tooltips
            $(function () {
                $('[data-toggle="tooltip"]').tooltip({
                    placement: 'bottom' // Set the desired placement here
                });
            });
        });

        // Apply the card animations
        // applyCardAnimations();
    }else {

        const card = `
            <div class="d-flex justify-content-center align-items-center">
                <img src="/assets/images/candidate-images/nothing_to_show.jpg" class="nothing-to-show" width="auto" height="300px"/>
            </div>
            <h4 class="text-center text-muted sub-title fw-bolder">No vacancy was found.</h4>
        `;

        // Append the card to the container
        $("#jobs-container").append(card);
    }

    updateRecentFilter();

    // Update the pagination UI
    updatePaginationUI(totalPages, currentPages);
}

function applyCardAnimations() {
    $(".card").each(function() {
        $(this).css({
            transform: "translateY(-10px)",
            "box-shadow": "0px 10px 10px rgba(0, 0, 0, 0.2)"
        });
    });
}

$('#show-result-btn').on('click', function(event) {
    event.preventDefault();
    showResult();
    // Check the viewport width
    const mediaQuery = window.matchMedia('(max-width: 767px)'); // Adjust the breakpoint as needed

    if (mediaQuery.matches) {
        // Close the offcanvas here
        closeFilter();
    }
});

async function loadVacancies(page) {
    console.log("Page from loadVacancies: ",page)
    // Get selected values
    let sortBy = $('input[name="sortBy"]:checked').val();
    let datePosted = $('input[name="datePosted"]:checked').val();
    let position = $('#title-input').val();
    let jobType = $('input[name="jobType"]:checked').val();
    let onSiteOrRemote = $('input[name="onSiteOrRemote"]:checked').val();
    // Extract selected level values into an array
    let levelArray = $('input[name="level"]:checked').val() === undefined ? null :
        $('input[name="level"]:checked').serializeArray().map(item => item.value);
    // let levelArray = $('input[name="level"]:checked').map(function() {
    //     return $(this).siblings('label').text();
    // }).get();

    // let levelString = levelArray.length > 0 ? levelArray.join(',') : null;
    let isUnder10 = $('input[name="under10"]:checked').val() === undefined ? "false" : "true";
    let itemPerPage = 5;

    try {
        const data = await filterJobs(sortBy, datePosted, position, jobType,onSiteOrRemote, levelArray, isUnder10, page, itemPerPage);
        totalPages = data.totalPages;
        vacancies = data.content; // Update vacancies array
        resultCount = data.totalElements;
        console.log("RESULT COUNT : ",resultCount);
        console.log("Loaded vacancies:", vacancies);

        showResult();
        updatePaginationUI(totalPages, page);
    } catch (error) {
        // Handle errors
        console.log(error);
    }
}

function updateRecentFilter() {
    // Get the selected filter options
    const sortBy = $('input[name="sortBy"]:checked').siblings('label').text();
    const datePosted = $('input[name="datePosted"]:checked').siblings('label').text();
    const jobType = $('input[name="jobType"]:checked').siblings('label').text();
    const onSiteOrRemote = $('input[name="onSiteOrRemote"]:checked').siblings('label').text();
    const levelsArray = $('input[name="level"]:checked').map(function () {
        return $(this).siblings('label').text();
    }).get();
    const under10 = $('input[name="under10"]').is(':checked') ? 'Under 10 applicants' : '';

    // Create an array of filter elements
    const filterElements = [
        `<span class="text-muted sub-title me-2 bg-white p-1 rounded-pill"><strong>Active Filter : </strong></span>`,
        `<span class="bg-light text-dark rounded-pill mx-1 d-inline-block px-2 p-1" style="border: 3px solid #1f3a62; font-size: 0.7rem"
           data-bs-toggle="dropdown" data-bs-placement="bottom" title="Sort By">
            ${sortBy}</span>`,
        `<span class="bg-light text-dark rounded-pill mx-1 d-inline-block px-2 p-1" style="border: 3px solid #1f3a62; font-size: 0.7rem"
            data-bs-toggle="dropdown" data-bs-placement="bottom" title="Date Posted">${datePosted}</span>`,
        `<span class="bg-light text-dark rounded-pill mx-1 d-inline-block px-2 p-1" style="border: 3px solid #1f3a62; font-size: 0.7rem"
            data-bs-toggle="dropdown" data-bs-placement="bottom" title="Job Type">${jobType}</span>`,
        `<span class="bg-light text-dark rounded-pill mx-1 d-inline-block px-2 p-1" style="border: 3px solid #1f3a62; font-size: 0.7rem"
            data-bs-toggle="dropdown" data-bs-placement="bottom" title="On-site or remote">${onSiteOrRemote}</span>`
    ];

    // Add level filters if levelsArray has values
    if (levelsArray.length > 0) {
        levelsArray.forEach(level => {
            filterElements.push(`<span class="bg-light text-dark rounded-pill mx-1 d-inline-block px-2 p-1" style="border: 3px solid #1f3a62; font-size: 0.7rem">${level}</span>`);
        });
    } else {
        filterElements.push(`<span class="bg-light text-dark rounded-pill mx-1 d-inline-block px-2 p-1" style="border: 3px solid #1f3a62; font-size: 0.7rem">ALL</span>`);
    }

    // Add under10 and includingClosed filter elements
    if (under10) {
        filterElements.push(`<span class="bg-light text-dark rounded-pill mx-1 d-inline-block px-2 p-1" style="border: 3px solid #1f3a62; font-size: 0.7rem">${under10}</span>`);
    }

    // Update the recent-filter section with the generated filter elements
    const recentFilter = $('#filter-data-con');
    recentFilter.empty(); // Clear previous content
    filterElements.forEach(element => {
        recentFilter.append(element);
    });

    // Initialize Bootstrap tooltips
    $(function () {
        $('[data-toggle="tooltip"]').tooltip({
            placement: 'bottom' // Set the desired placement here
        });
    });
}

$('#quick-search-btn').on('click', function () {
    const button = $(this);
    resetFilter(event);
    $('#title-input').val($('input[name="position"]').val());
    console.log("Filter value : ",$('#title-input').val())
    applyFilter().then(r => showResult(vacancies));
});

$(document).ready(async function () {
    // Add event listeners to relevant input elements
    $('input[name="sortBy"]').on('change', applyFilter);
    $('input[name="datePosted"]').on('change', applyFilter);
    $('#title-input').on('input', applyFilter);
    $('input[name="jobType"]').on('change', applyFilter);
    $('input[name="onSiteOrRemote"]').on('change', applyFilter);
    $('input[name="level"]').on('change', applyFilter);
    $('input[name="under10"]').on('change', applyFilter);

    // vacancies =await applyFilter();
    console.log("Doc ready : " +  vacancies);
    await loadVacancies(0);
    $('#result-count').html(resultCount)
    // showResult(vacancies);
});