let currentId = new URLSearchParams(window.location.search).get("id");
const reopenBtn = $('#reopen-btn');
const closeBtn = $('#close-btn');
const resetButton = $('#reset-repoen-btn');
const reopenModeWarn = $('.reopen-mode-warn');
const inputsToDisable = $('.input-to-disable');
let defaultPosition = null;
let defaultDepartment = null;
let href;
let vacancyId;
$(document).ready(function () {

    // Check if the currentId is the same as the previousId
    if (currentId != null) {
        showDetailModalForVacancyId(currentId);
        clearIdParameter();
    }

    console.log($('#title'))
    console.log($('#department'))
    console.log("Input To Disable : ", inputsToDisable)

    // Store the initial visibility status of each column
        var columnVisibility = [true, true, true, true, true, true, false, false, true];
        var table = $('table#table').DataTable({
            "serverSide": true,
            "processing": true,
            "stateSave": true,
            "ajax": {
                url: '/vacancy/show-all-data',
                type: 'GET',
            },
            "columns": [
                // { name: "No", data: null, render: function (data, type, row, meta) { return meta.row + 1; }, target: 0 },
                { name: "Position", data: "position", target: 0 }, // Access object property directly
                { name: "Department", data: "department", target: 1 }, // Access object property directly
                { name: "Experience",
                    data: "level",
                    render: function (data, type, row, meta) {
                        return reconvertToString(row.level);
                    },
                    target: 2 }, // Access object property directly
                { name: "Salary", data: "salary", target: 3 }, // Access object property directly
                { name: "Status", data: "status", target: 4 }, // Access object property directly
                { name: "Applicants",
                    data: "applicants",
                    render: function (data, type, row, meta) {
                        let applicants =  (row.applicants === 0) ? "-" : row.applicants;
                        return '<p class="text-center">'+applicants+'</p>';
                    },
                    target: 5 }, // Access object property directly
                { name: "Created User/Time",
                    data: "createdUsername",
                    data: "createdTime",
                    render: function (data, type, row, meta) {
                        return '<span class="d-inline-block text-white rounded bg-info p-1" style="font-size: 0.7rem">' + row.createdUsername + '</span>' +
                            '<span class="d-inline-block text-white rounded bg-warning p-1" style="font-size: 0.7rem">' + changeTimeFormat(row.createdTime) + '</span>';
                    },
                    target: 6
                }, // Access object property directly
                { name: "Updated User/Time",
                    data: "updatedUsername",
                    data: "updatedTime",
                    render: function (data, type, row, meta) {
                        return '<span class="d-inline-block text-white rounded bg-info p-1" style="font-size: 0.7rem">' + row.updatedUsername + '</span>' +
                            '<span class="d-inline-block text-white rounded bg-warning p-1" style="font-size: 0.7rem">' + changeTimeFormat(row.updatedTime) + '</span>';
                    },
                    target: 7
                }, // Access object property directly
                {
                    name: "Open/Close",
                    data: "openDate",
                    data: "closeDate",
                    render: function (data, type, row, meta) {
                        var openDateFormatted = changeTimeFormat(row.openDate);
                        var closeDateFormatted = changeTimeFormat(row.closeDate);

                        return '<span class="d-inline-block text-white rounded bg-success p-1" style="font-size: 0.7rem">' + openDateFormatted + '</span>' +
                            '<span class="d-inline-block text-white rounded bg-danger p-1" style="font-size: 0.7rem">' + closeDateFormatted + '</span>';
                    },
                    target: 8
                },
                {
                    name: "Switch",
                    data: "id",
                    data: "status",
                    render: function (data, type, row, meta) {
                        var rowID = row.id;
                        var status = row.status;

                        // Create the dropdown with Detail and Closed options
                        var dropdown = `
                        <div class="dropdown">
                            <button class="btn btn-secondary btn-sm bg-transparent border-0 text-dark" type="button" data-bs-toggle="dropdown" data-bs-target="#dropdown-setting${rowID}" aria-expanded="false">
                            <i class="bi bi-three-dots-vertical"></i>
                            </button>
                        <ul class="dropdown-menu" id="dropdown-setting${rowID}" aria-labelledby="dropdown-setting${rowID}">
                        <li><a class="dropdown-item show-detail-btn" href="view-vacancy-detail?id=${rowID}">Detail</a></li>`;
                        if (status === 'OPEN') {
                            dropdown += `<li><a class="dropdown-item close-vacancy" href="close-vacancy?id=${rowID}">Close Vacancy</a></li>`;
                        } else {
                            dropdown += `<li><a class="dropdown-item reopen-vacancy" href="reopen-vacancy-by-id?id=${rowID}">Reopen Vacancy</a></li>`;
                        }
                        dropdown += `</ul>
                        </div>`;

                        return dropdown;
                    },
                    target: 9,
                    sortable: false
                }
            ],
            order: [[0,'asc']],
            paging: true,
            lengthMenu: [5,10,20],
            pageLength: 5,
        });


        // Function to toggle column visibility
        // Show columns 0 to 6 and the last column (index 10)
        // for (var i = 6; i <= 8; i++) {
        //     var column = table.column(i);
        //     column.visible(false);
        // }

        // Bind the button click event to toggle column visibility
        $('#toggleColumnsBtn').on('click', function () {
            // Toggle columns 6 to 8
            for (var i = 6; i <= 8; i++) {
                var column = table.column(i);
                var isVisible = column.visible();
                if (isVisible) {
                    // Hide columns 7 to 9 and show columns 3 to 6
                    columnVisibility[i] = isVisible;
                    column.visible(false);
                    for (var j = 2; j <= 5; j++) {
                        var col = table.column(j);
                        col.visible(true);
                    }
                } else {
                    // Show columns 7 to 9 and hide columns 3 to 6
                    columnVisibility[i] = isVisible;
                    column.visible(true);
                    for (var j = 2; j <= 5; j++) {
                        var col = table.column(j);
                        col.visible(false);
                    }
                }
            }
        });

    // Click event handler for the reopen button
    reopenBtn.on('click', function(event) {
        event.preventDefault();
        console.log(reopenBtn.hasClass('btn-bright'))
        console.log("Position : ",defaultPosition);
        console.log("Department : ",defaultDepartment)
        $('#title').val(defaultPosition);
        $('#department').val(defaultDepartment);
        console.log("Position : ",$('#title').val());
        console.log("Department : ",$('#department').val())
        if (reopenBtn.hasClass('btn-bright')) {
            // If the button is in the "bright" state, switch it off
            reopenBtn.removeClass('btn-bright');
            reopenBtn.addClass('btn-un-bright');
            $('#submit-btn')
                .attr('data-form-id', 'update-form')
                .attr('data-success-message', 'Update successful!')
                .attr('data-error-message', 'Update failed. Please try again.')
                .html('Update');
                reopenModeWarn.hide();
                $('#reopen-form')
                    .attr('id', 'update-form')
                    .attr('action', 'update-vacancy');
        } else {
            // If the button is in the "off" state, switch it on
            reopenBtn.removeClass('btn-un-bright');
            reopenBtn.addClass('btn-bright');
            reopenModeWarn.show();
            $('#submit-btn')
                .attr('data-form-id', 'reopen-form')
                .attr('data-success-message', 'Reopen successful!')
                .attr('data-error-message', 'Reopen failed. Please try again.')
                .html('Reopen');
            // Change form id and action
            $('#update-form')
                .attr('id', 'reopen-form')
                .attr('action', 'reopen-vacancy');
        }
        inputsToDisable.each(function() {
            $(this).prop('disabled', !$(this).prop('disabled')); // Toggle the disabled property
        });
    });
    // Click event handler for the reset button
    resetButton.on('click', function() {
        console.log('click dismiss')
        // Add 'btn-un-bright' class to reopenBtn
        if (reopenBtn.hasClass('btn-bright')) {
            // If the button is in the "bright" state, switch it off
            reopenBtn.removeClass('btn-bright');
            reopenBtn.addClass('btn-un-bright');
        }
        // Change the data attributes and button text
        $('#submit-btn')
            .attr('data-form-id', 'update-vacancy')
            .attr('data-success-message', 'Update successful!')
            .attr('data-error-message', 'Update failed. Please try again.')
            .html('Update');
        $('#reopen-form')
            .attr('id', 'update-form')
            .attr('action', 'update-vacancy');
        // Disable the inputs
        reopenModeWarn.hide();
        inputsToDisable.prop('disabled', false);
    });

    reopenModeWarn.hide();

    // Initialize Bootstrap tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

});

// Add an event listener for the "Detail" link
$(document).on("click", ".show-detail-btn", function (event) {
    event.preventDefault(); // Prevent the default behavior of the link

    // Get the vacancy ID from the link's href attribute
    href = $(this).attr("href");
    vacancyId = href.split("=")[1]; // Assuming the URL is like "view-vacancy-detail?id=123"

    // Fetch the vacancy details using AJAX
    var apiUrl = "/vacancy/job-detail?id=" + vacancyId;

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            // Handle the successful response and display the details in the modal
            populateModalWithData(data); // Call the function to populate the modal with data
            $("#detailModal").modal("show");
        });
});

// Reopen vacancy
$(document).on("click", ".reopen-vacancy", function (event) {
    event.preventDefault(); // Prevent the default behavior of the link

    // Get the vacancy ID from the link's href attribute
    href = $(this).attr("href");
    vacancyId = href.split("=")[1]; // Assuming the URL is like "view-vacancy-detail?id=123"

    console.log("Href : ", href);
    console.log("Vacancy Id : ",vacancyId);

    // Show the loader and the message-con modal
    $('#message-con').html('' +
        '<div class="loader"></div>' +
        '<div class="loader-txt">' +
        '<h3 class="text-white">Are you sure?</h3>' +
        '<p class="text-center text-white">Reopen will make this vacancy open for 30 days again</p>' +
        '<div>' +
        '<button type="button" class="btn btn-sm btn-light mx-1" onclick="actionToVacancy(href)">Sure</button>' +
        '<button class="btn btn-sm btn-light-danger mx-1" onclick="closeModal()">Cancel</button></div>' +
        '</div>' +
        '</div>');
    $('#loadMe').modal({
        backdrop: 'static' // Set backdrop to 'static' when the "Processing..." message is shown
    }).modal('show');

});

// Close vacancy
$(document).on("click", ".close-vacancy", function (event) {
    event.preventDefault(); // Prevent the default behavior of the link

    // Get the vacancy ID from the link's href attribute
    href = $(this).attr("href");
    vacancyId = href.split("=")[1]; // Assuming the URL is like "view-vacancy-detail?id=123"

    console.log("Href : ", href);
    console.log("Vacancy Id : ",vacancyId);

    // Show the loader and the message-con modal
    $('#message-con').html('' +
        '<div class="loader"></div>' +
        '<div class="loader-txt">' +
        '<h3 class="text-white">Are you sure?</h3>' +
        '<p class="text-center text-white">After close, this vacancy cannot be open and only reopen can do</p>' +
        '<div>' +
        '<button type="button" class="btn btn-sm btn-light-danger mx-1" onclick="actionToVacancy(href)">Sure</button>' +
        '<button class="btn btn-sm btn-light mx-1" onclick="closeModal()">Cancel</button></div>' +
        '</div>' +
        '</div>');
    $('#loadMe').modal({
        backdrop: 'static' // Set backdrop to 'static' when the "Processing..." message is shown
    }).modal('show');

});

function actionToVacancy(href) {

    // Show the loader and the message-con modal
    $('#message-con').html('<div class="loader"></div><div class="loader-txt"><p class="text-white">Processing...</p></div>');
    $('#loadMe').modal({
        backdrop: 'static' // Set backdrop to 'static' when the "Processing..." message is shown
    }).modal('show');

    const csrfToken = document.querySelector('#token').value;
    console.log(csrfToken);
    const metaCsrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");

    console.log(metaCsrfToken);

    // Submit the form using AJAX
    fetch(href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-Token': csrfToken,
        },
    })
        .then(response => response.json())
        .then(data  => {
            console.log(data)
            // Handle the success response and update the message-con modal
            if (data === true) {
                $('#message-con').html('<p class="text-white">Process successful!</p><div class="text-center"><button class="btn btn-sm btn-light" onclick="closeModal()">Close</button></div>'); // Replace with the content you want to show for success
            } else {
                // Handle the error response and update the message-con modal
                $('#message-con').html('<p class="text-white">Process failed. Please try again.</p></p><div class="text-center"><button class="btn btn-sm btn-light" onclick="closeModal()">Okay</button></div>'); // Replace with the content you want to show for error
            }
            hideMessageModalAfterDelay();
        })
        .catch(error => {
            // Handle any errors that occurred during the fetch
            console.error('Fetch error:', error);
            $('#message-con').html('<p class="text-white">An error occurred. Please contact us.</p><div class="text-center"><button class="btn btn-sm btn-light" onclick="closeModal()">Okay</button></div>'); // Replace with the content you want to show for error
            hideMessageModalAfterDelay();
        });
}

// Toggle column function
function toggleColumn() {
    for (var i = 6; i <= 8; i++) {
        var column = table.column(i);
        var isVisible = column.visible();
        if (isVisible) {
            // Hide columns 7 to 9 and show columns 3 to 6
            columnVisibility[i] = isVisible;
            column.visible(false);
            for (var j = 2; j <= 5; j++) {
                var col = table.column(j);
                col.visible(true);
            }
        } else {
            // Show columns 7 to 9 and hide columns 3 to 6
            columnVisibility[i] = isVisible;
            column.visible(true);
            for (var j = 2; j <= 5; j++) {
                var col = table.column(j);
                col.visible(false);
            }
        }
    }
}

// Function to populate the modal with data fetched from the server
function populateModalWithData(data) {
    // Replace the content of the form fields with the data you received from the server
    // Assuming the data object contains properties with the same names as the form field IDs
    console.log("Id : ", data.id);
    console.log("Type : ", data.type);
    console.log("Lvl : ", data.lvl);
    console.log("On-site or remote : ", data.onSiteOrRemote);

    // Put default position and department for reopen mode
    defaultPosition = data.position;
    defaultDepartment = data.department;

    if(data.status == "CLOSED") {
        console.log('close')
        reopenBtn.show();
        closeBtn.hide();
    }else {
        closeBtn.attr('href', `close-vacancy?id=${data.id}`);
        reopenBtn.hide();
        closeBtn.show();
    }

    // For example:
    $("#id").val(data.id);
    $("#vacancy-id").val(data.vacancyId);
    $("#title").val(data.position);
    $("#post").val(data.post);
    $("#department").val(data.department);
    $("#type").val(convertToEnumFormat(data.type)); // Convert and set the value
    $("#lvl").val(convertToEnumFormat(data.lvl)); // Convert and set the value
    $("#workingDays").val(data.workingDays);
    $("#workingHours").val(data.workingHours);
    $("#salary").val(data.salary);
    $("#onSiteOrRemote").val(convertToEnumFormat(data.onSiteOrRemote)); // Convert and set the value
    $("#descriptions").val(data.descriptions);
    $("#responsibilities").val(data.responsibilities);
    $("#requirements").val(data.requirements);
    $("#preferences").val(data.preferences);
    $("#address").val(data.address);

}

// Function to show the modal for a specific vacancy ID
function showDetailModalForVacancyId(vacancyId) {
    // Fetch the vacancy details using AJAX
    var apiUrl = "/vacancy/job-detail?id=" + vacancyId;

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            // Handle the successful response and display the details in the modal
            populateModalWithData(data); // Call the function to populate the modal with data
            $("#detailModal").modal("show");
        });
}


// Function to clear the "id" parameter from the URL
function clearIdParameter() {
    var newUrl = window.location.href.split('?')[0]; // Get the base URL without query parameters
    history.replaceState({}, document.title, newUrl); // Replace the current URL without the "id" parameter
}


// Enum convert and reconvert
function convertToEnumFormat(input) {
    console.log(input)
    // Replace spaces with underscores and convert to uppercase
    if(input == "On-site") {
        return "ON_SITE";
    }
    return input.replaceAll(" ", "_").toUpperCase();
}

function reconvertToString(input) {
    // Replace underscores with spaces and convert to title case
    if (input === "ON_SITE") {
        return "On-site";
    }
    return input.split('_').map(word => word.charAt(0) + word.slice(1).toLowerCase()).join(' ');
}

// Change time format
function changeTimeFormat(time) {
    var dateString = time;

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

    // Format the date as "Dayth Month Year" (e.g., "27th Jul")
    var formattedDate = day + suffix + " " + monthNames[date.getMonth()];
    return formattedDate;
}
