// const pageSize = 5; // Number of items to show per page
// let currentPage = 1; // Variable to store the current page
//
// async function loadVacancies(page) {
//     try {
//         console.log("Load Page : ",page)
//         // const response = await fetch(`/vacancy/show-all?page=${page}&size=${pageSize}`);
//         const response = await applyFilter();
//         const responseData = await response.json();
//         const vacancies = responseData.content;
//         const totalPages = responseData.totalPages;
//         console.log(vacancies);
//
//         // Clear the existing content in the container
//         $("#jobs-container").empty();
//
//         // Loop through each vacancy and create the card dynamically
//         vacancies.forEach(function (vacancy) {
//             const card = `
//                 <div class="card flex-md-row">
//                     <div class="">
//                         <img class="m-3" src="/assets/images/candidate-images/backend_icon.png" alt="Backend Icon" width="50" height="50">
//                     </div>
//                     <div class="card-body">
//                         <h5 class="card-title">${vacancy.position}<span class="applicants-text d-inline-block d-md-inline-block"><i class='bx bxs-droplet'></i> ${vacancy.applicants} applicants</span></h5>
//                         <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip" data-placement="bottom" title="Post(Job type)"><i class='bx bxs-briefcase'></i>${vacancy.post} (${reconvertToString(vacancy.jobType)})</span>
//                         <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip" data-placement="bottom" title="Salary"><i class='bx bx-money'></i>${vacancy.salary}</span>
//                         <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip" data-placement="bottom" title="Posted time"><i class='bx bx-time'></i> ${timeAgo(vacancy.updatedTime)}</span>
//                         <span class="default-font mx-2 d-block d-md-block d-xl-inline-block" data-toggle="tooltip" data-placement="bottom" title="Location"><i class="bi bi-geo-alt-fill"></i>${vacancy.address}</span>
//                     </div>
//                     <div class="d-flex flex-column justify-content-center justify-content-md-center align-items-end mb-3">
//                         <a href="/job-detail?id=${vacancy.id}" class="btn btn-sm btn-primary mb-1">More Details</a>
//                         <span class="default-font me-4 d-inline-block end-date-text" data-toggle="tooltip" data-placement="bottom" title="Close date"><i class='bx bx-calendar-exclamation'></i> ${changeTimeFormat(vacancy.closeDate)}</span>
//                     </div>
//                 </div>
//             `;
//             // Append the card to the container
//             $("#jobs-container").append(card);
//         });
//
//         // Update the pagination UI
//         updatePaginationUI(totalPages, page);
//     } catch (error) {
//         console.error("Error fetching data:", error);
//     }
// }
//
// function updatePaginationUI(totalPages, currentPage) {
//     const paginationContainer = $("#pagination-container");
//     paginationContainer.empty();
//
//     if (totalPages <= 1) {
//         // Don't show pagination if there's only one page
//         return;
//     }
//
//     // Determine the range of pages to display
//     const maxPageLinks = 3; // Number of page links to display
//     currentPage = currentPage + 1;
//     let startPage, endPage;
//
//     if (totalPages <= maxPageLinks) {
//         startPage = 1;
//         endPage = totalPages;
//     } else {
//         // Calculate the start and end pages, considering the current page
//         startPage = Math.max(1, currentPage - Math.floor(maxPageLinks / 2));
//         endPage = Math.min(totalPages, startPage + maxPageLinks - 1);
//
//         // Adjust the start and end pages if the current page is near the boundaries
//         if (endPage - startPage + 1 < maxPageLinks) {
//             if (currentPage <= Math.ceil(maxPageLinks / 2)) {
//                 endPage = Math.min(totalPages, startPage + maxPageLinks - 1);
//             } else {
//                 startPage = Math.max(1, endPage - maxPageLinks + 1);
//             }
//         }
//     }
//
//     console.log("currentPage : ",currentPage);
//     console.log("Start page : ",startPage);
//     console.log("End page : ",endPage);
//
//     // Create the previous button
//     const startPageButton = `
//         <li class="page-item">
//             <a class="page-link" href="#" onclick="loadVacancies(${startPage-1})" aria-label="Previous">
//                 <span aria-hidden="true">&laquo;</span>
//             </a>
//         </li>
//     `;
//
//     // Create the next button
//     const lastPageButton = `
//         <li class="page-item">
//             <a class="page-link" href="#" onclick="loadVacancies(${endPage-1})" aria-label="Next">
//                 <span aria-hidden="true">&raquo;</span>
//             </a>
//         </li>
//     `;
//
//     // Create the page links
//     const pageLinks = [];
//     for (let i = startPage; i <= endPage; i++) {
//         const activeClass = i === currentPage ? "active-page" : "";
//         const pageLink = `
//             <li class="page-item">
//                 <a class="page-link ${activeClass}" href="#" onclick="loadVacancies(${i-1})">${i}</a>
//             </li>
//         `;
//         pageLinks.push(pageLink);
//     }
//
//     // Combine all the components to form the pagination UI
//     const paginationUI = `
//         <nav aria-label="Page navigation example">
//             <ul class="pagination">
//                 ${startPageButton}
//                 ${pageLinks.join("")}
//                 ${lastPageButton}
//             </ul>
//         </nav>
//     `;
//
//     paginationContainer.append(paginationUI);
// }
//
// // Call this function when the page is loaded or when the user clicks on pagination buttons
// function loadPage(page) {
//     // Load the paginated data for the given page
//     loadVacancies(page);
//
//     // Code to update the pagination UI based on the current page and available pages
//     // You can use the response from the backend to determine the total number of pages
//     // and whether the first and last page buttons should be shown.
// }
//
// function changeTimeFormat(time) {
//
//     // Parse the date string to a JavaScript Date object
//     var date = new Date(time);
//
//     // Array to map month numbers to month names
//     var monthNames = [
//         "Jan", "Feb", "Mar", "Apr", "May", "Jun",
//         "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
//     ];
//
//     // Get the day of the month
//     var day = date.getDate();
//
//     // Determine the suffix for the day (st, nd, rd, or th)
//     var suffix;
//     if (day >= 11 && day <= 13) {
//         suffix = "th";
//     } else {
//         switch (day % 10) {
//             case 1: suffix = "st"; break;
//             case 2: suffix = "nd"; break;
//             case 3: suffix = "rd"; break;
//             default: suffix = "th";
//         }
//     }
//
//     // Format the date as "Dayth Month Year" (e.g., "27th Jul 2023")
//     var formattedDate = day + suffix + " " + monthNames[date.getMonth()];
//     return formattedDate;
// }
//
// function timeAgo(time) {
//     const currentTime = new Date();
//     const inputTime = new Date(time);
//     const timeDifferenceInSeconds = Math.floor((currentTime - inputTime) / 1000);
//
//     // Define time units in seconds
//     const minute = 60;
//     const hour = 60 * minute;
//     const day = 24 * hour;
//     const week = 7 * day;
//     const month = 30 * day;
//
//     if (timeDifferenceInSeconds < minute) {
//         return 'Just now';
//     } else if (timeDifferenceInSeconds < hour) {
//         const minutesAgo = Math.floor(timeDifferenceInSeconds / minute);
//         return `${minutesAgo} minute${minutesAgo > 1 ? 's' : ''} ago`;
//     } else if (timeDifferenceInSeconds < day) {
//         const hoursAgo = Math.floor(timeDifferenceInSeconds / hour);
//         return `${hoursAgo} hour${hoursAgo > 1 ? 's' : ''} ago`;
//     } else if (timeDifferenceInSeconds < week) {
//         const daysAgo = Math.floor(timeDifferenceInSeconds / day);
//         return `${daysAgo} day${daysAgo > 1 ? 's' : ''} ago`;
//     } else if (timeDifferenceInSeconds < month) {
//         const weeksAgo = Math.floor(timeDifferenceInSeconds / week);
//         return `${weeksAgo} week${weeksAgo > 1 ? 's' : ''} ago`;
//     } else {
//         // Display the date in the format: 'MMM DD YYYY'
//         const formattedDate = inputTime.toLocaleDateString('en-US', {
//             year: 'numeric',
//             month: 'short',
//             day: 'numeric'
//         });
//         return formattedDate;
//     }
// }
//
// function reconvertToString(input) {
//     // Replace underscores with spaces and convert to title case
//     if (input === "ON_SITE") {
//         return "On-site";
//     }
//     return input.split('_').map(word => word.charAt(0) + word.slice(1).toLowerCase()).join(' ');
// }
//
// $(document).ready(function () {
//     loadPage(0); // Load the initial page when the page is loaded
//
//     // Add click event listeners to pagination buttons
//     $(".pagination .page-item").click(function (e) {
//         e.preventDefault();
//         const page = $(this).find(".page-link").text();
//         loadPage(page - 1); // Adjust the page number to 0-based index
//     });
//
//     // Initialize Bootstrap tooltips
//     var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
//     var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
//         return new bootstrap.Tooltip(tooltipTriggerEl);
//     });
// });