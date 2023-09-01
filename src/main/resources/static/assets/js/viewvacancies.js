let currentId = new URLSearchParams(window.location.search).get("id");
const reopenBtn = $('#reopen-btn');
const closeBtn = $('#close-btn');
const resetButton = $('#reset-repoen-btn');
// const reopenModeWarn = $('.reopen-mode-warn');
// const inputsToDisable = $('.input-to-disable');
let defaultPosition = null;
let defaultDepartment = null;
let href;
let vacancyId;
let table;
let rangeBar1;
let sliderValue1;
let sliderValue2;
let tooltipsEnabled1 = false;
let currentData = {};
// let tooltipTriggerPost;
// let tooltipPost;
let userRole;

// async function validateUIButton() {
//     const response = await fetch('/authenticated-user-data', {
//         method: 'POST',
//         headers: {
//             [csrfHeader]: csrfToken
//         }
//     });
//
//     if (response.ok) {
//         const [userDetails, passwordMatches] = await response.json();
//         userRole = userDetails.role;
//         console.log("User Role", userRole);
//         if(userRole != 'DEFAULT_HR' && userRole != 'SENIOR_HR' && userRole != 'JUNIOR_HR') {
//             $('#reset-form, #reopen-btn, #close-btn, #submit-btn, .disabled-warn').each(function () {
//                 $(this).remove();
//             })
//             $('input[type="text"], input[type="number"], select, textarea').each(function () {
//                 $(this).prop('disabled', true);
//             })
//         }
//
//     }
//
// }
$(document).ready(function () {

    fetch('/authenticated-user-data', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(async response => {
            if (response.ok) {
                const [userDetails, passwordMatches] = await response.json();
                userRole = await userDetails.user.role;
                console.log("User Role", userRole);
            }

            if (userRole === 'OTHER' || userRole === 'MANAGEMENT' || userRole === 'INTERVIEWER') {
                $('#reset-form, #reopen-btn, #close-btn, #submit-btn, .disabled-warn').each(function () {
                    $(this).remove();
                })
                $('input[type="text"], input[type="number"], select, textarea').each(function () {
                    $(this).prop('disabled', true);
                })
                $('.close-vacancy').remove();
                $('.reopen-vacancy').remove();
                $('#timePickerBtn').prop('disabled', true);
                $('#calendar-btn').prop('disabled', true);
            }else {
                console.log("Hello")
            }
        });

    // Check if the currentId is the same as the previousId
    if (currentId != null) {
        showDetailModalForVacancyId(currentId);
        clearIdParameter();
    }

    console.log($('#title'))
    console.log($('#department'))
    // console.log("Input To Disable : ", inputsToDisable)

    // Store the initial visibility status of each column
        let columnVisibility = [true,true, true, true, true, true, false, false, false, true];
        table = $('table#table').DataTable({
            "serverSide": true,
            "processing": true,
            "stateSave": true,
            "scrollY": 300,
            "scrollX": true,
            "scrollCollapse": true,
            "fixedHeader": {
                "header": true,
            },
            "ajax": {
                url: '/vacancy/show-all-data',
                type: 'GET',
                data: function (d) {
                    d.datePosted = $('#filter-date-posted').val(),
                    d.title = $('#filter-title').val(),
                    d.department = $('#filter-department').val(),
                    d.startDateInput = $('#filter-start-date').val(),
                    d.endDateInput = $('#filter-end-date').val(),
                    d.jobType = $('#filter-jobType').val(),
                    d.level = $('#filter-level').val(),
                    d.minAndMax = $('#filter-minAndMax').val(),
                    d.applicants = $('#filter-applicants').val(),
                    d.status = $('#filter-status').val()
                }
            },
            "columns": [
                {
                    className: 'dt-control',
                    orderable: false,
                    data: "note",
                    render: function (data, type, row, meta) {
                        return "";
                    },
                    defaultContent: '',
                    target: 0
                },
                { name: "Position", data: "position", target: 1 }, // Access object property directly
                { name: "Department", data: "department", target: 2 }, // Access object property directly
                { name: "Experience",
                    data: "level",
                    render: function (data, type, row, meta) {
                        return reconvertToString(row.level);
                    },
                    target: 3 }, // Access object property directly
                { name: "Salary",
                    data: "salary",
                    render: function (data, type, row, meta) {
                        if(row.salary === 0) {
                            return "Negotiate";
                        }
                        return convertToLakhs(row.salary);
                    },
                    target: 4 }, // Access object property directly
                { name: "Status", data: "status", target: 5 }, // Access object property directly
                { name: "Applicants",
                    data: "hiredPost",
                    data: "applicants",
                    data: "post",
                    render: function (data, type, row, meta) {
                        let hiredPost = row.hiredPost;
                        let applicants =  row.applicants;
                        const returnRow = `
                            <div class="text-nowrap">
                                <span data-bs-toggle="tooltip" data-bs-placement="top" title="Hired Post">
                                    <span class="bg-gradient-ltr py-1 px-2 text-white rounded-pill"
                                    style="font-size: 0.8rem;">${hiredPost}</span>
                                </span>
                                <span data-bs-toggle="tooltip" data-bs-placement="top" title="Applicants">
                                    <span class="bg-success bg-gradient py-1 px-2 mx-1 text-white rounded-pill"
                                        style="font-size: 0.8rem">${applicants}</span>
                                </span>
                                <span data-bs-toggle="tooltip" data-bs-placement="top" title="Required Post">
                                    <span class="bg-primary bg-gradient py-1 px-2 text-white rounded-pill"
                                    style="font-size: 0.8rem">${row.post}</span>
                                </span>
                            </div>
                         `;

                        // Initialize Bootstrap tooltips
                        let tooltipTriggerPost = [].slice.call($('[data-bs-toggle="tooltip"]'));
                        let tooltipPost = tooltipTriggerPost.map(function (tooltipTriggerEl) {
                            return new bootstrap.Tooltip(tooltipTriggerEl);
                        });

                        return returnRow;
                    },
                    target: 6 }, // Access object property directly
                { name: "Created User/Time",
                    data: "createdUsername",
                    data: "createdTime",
                    render: function (data, type, row, meta) {
                        return '<span class="d-inline-block text-white rounded bg-gradient-ltr p-1" style="font-size: 0.7rem">' + row.createdUsername + '</span>' +
                            '</br>' +
                            '<span class="d-inline-block text-white rounded bg-primary bg-gradient p-1 position-relative" style="font-size: 0.7rem">' + changeTimeFormat(row.createdTime) +
                            '<span class="position-absolute translate-middle badge rounded-pill text-dark glass-transparent" style="transform: translate(-90%,-76%) !important;">' +
                            changeTime(row.createdTime) +
                            '    <span class="visually-hidden">unread messages</span>' +
                            '</span>';
                    },
                    target: 7
                }, // Access object property directly
                { name: "Updated User/Time",
                    data: "updatedUsername",
                    data: "updatedTime",
                    render: function (data, type, row, meta) {
                        return '<span class="d-inline-block text-white rounded bg-gradient-ltr p-1" style="font-size: 0.7rem">' + row.updatedUsername + '</span>' +
                            '</br>' +
                            '<span class="d-inline-block text-white rounded bg-primary bg-gradient p-1 position-relative" style="font-size: 0.7rem">' + changeTimeFormat(row.updatedTime) +
                            '<span class="position-absolute translate-middle badge rounded-pill text-dark glass-transparent" style="transform: translate(-90%,-76%) !important;">' +
                            changeTime(row.updatedTime) +
                            '    <span class="visually-hidden">unread messages</span>' +
                            '</span>';
                    },
                    target: 8
                }, // Access object property directly
                {
                    name: "Open/Close",
                    data: "openDate",
                    data: "closeDate",
                    render: function (data, type, row, meta) {
                        var openDateFormatted = changeTimeFormat(row.openDate);
                        var closeDateFormatted = changeTimeFormat(row.closeDate);

                        return '<span class="d-inline-block text-white rounded bg-gradient-ltr p-1" style="font-size: 0.7rem">' + openDateFormatted + '</span>' +
                            '</br>' +
                            '<span class="d-inline-block text-white rounded bg-danger bg-gradient p-1" style="font-size: 0.7rem">' + closeDateFormatted + '</span>';
                    },
                    target: 9
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
                            // if(userRole === 'DEFAULT_HR' || userRole === 'SENIOR_HR' || userRole === 'JUNIOR_HR') {
                                dropdown += `<li><a class="dropdown-item close-vacancy" href="close-vacancy?id=${rowID}">Close Vacancy</a></li>`;
                            // }
                        } else {
                            // if(userRole === 'DEFAULT_HR' || userRole === 'SENIOR_HR' || userRole === 'JUNIOR_HR') {
                                dropdown += `<li><a class="dropdown-item reopen-vacancy" href="reopen-vacancy-by-id?id=${rowID}">Reopen Vacancy</a></li>`;
                            // }
                        }
                        dropdown += `</ul>
                        </div>`;

                        return dropdown;
                    },
                    target: 10,
                    sortable: false
                }
            ],
            order: [[0,'desc']],
            paging: true,
            lengthMenu: [5,10,20],
            pageLength: 5,
        });
    function format(d) {
        // `d` is the original data object for the row
        let note = '';
        if(d.note === '' || d.note === null) {
            return '<p class="text-muted sub-title">' + "No note to show." + '</p>'
        }

        return (
            '<dl>' +
            '<dt>Note:</dt>' +
            '<dd>' +
            d.note +
            '</dd>' +
            '</dl>'
        );
    }

    // Add event listener for opening and closing details
    table.on('click', 'td.dt-control', function (e) {
        let tr = e.target.closest('tr');
        let row = table.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
        }
        else {
            // Open this row
            row.child(format(row.data())).show();
        }
    });

        // Function to toggle column visibility
        // Show columns 0 to 6 and the last column (index 10)
        // for (var i = 6; i <= 8; i++) {
        //     var column = table.column(i);
        //     column.visible(false);
        // }

        // Bind the button click event to toggle column visibility
        $('#toggleColumnsBtn').on('click', function () {
            // Toggle columns 7 to 9
            for (var i = 7; i <= 9; i++) {
                var column = table.column(i);
                var isVisible = column.visible();
                if (isVisible) {
                    // Hide columns 8 to 9 and show columns 3 to 7
                    columnVisibility[i] = isVisible;
                    column.visible(false);
                    for (var j = 3; j <= 6; j++) {
                        var col = table.column(j);
                        col.visible(true);
                    }
                } else {
                    // Show columns 7 to 9 and hide columns 3 to 6
                    columnVisibility[i] = isVisible;
                    column.visible(true);
                    for (var j = 3; j <= 6; j++) {
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
                .data('form-id', 'update-form')
                .data('warning-message', 'Your vacancy will be updated.')
                .data('success-message', 'Update successful!')
                .data('error-message', 'Update failed. Please try again.')
                .html('Update');
                // reopenModeWarn.hide();
                $('#reopen-form')
                    .attr('id', 'update-form')
                    .attr('action', 'update-vacancy');
        } else {
            // If the button is in the "off" state, switch it on
            reopenBtn.removeClass('btn-un-bright');
            reopenBtn.addClass('btn-bright');
            // reopenModeWarn.show();
            $('#submit-btn')
                .data('form-id', 'reopen-form')
                .data('warning-message', 'Reopen will make this vacancy open for 30 days again.')
                .data('success-message', 'Reopen successful!') // Change this as needed
                .data('error-message', 'Reopen failed. Please try again.') // Change this as needed
                .html('Reopen');
            // Change form id and action
            $('#update-form')
                .attr('id', 'reopen-form')
                .attr('action', 'reopen-vacancy');
        }
        // inputsToDisable.each(function() {
        //     $(this).prop('disabled', !$(this).prop('disabled')); // Toggle the disabled property
        // });
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
            .data('form-id', 'update-form')
            .data('warning-message', 'Your vacancy will be updated.')
            .data('success-message', 'Update successful!')
            .data('error-message', 'Update failed. Please try again.')
            .html('Update');
        $('#reopen-form')
            .attr('id', 'update-form')
            .attr('action', 'update-vacancy');
        // Disable the inputs
        // reopenModeWarn.hide();
        // inputsToDisable.prop('disabled', false);
    });

    // reopenModeWarn.hide();

    // Create reset filter button
    let resetFilterButton = `
        <div id="reset-filter" class="mt-3 col-1 text-center">
            <img src="/assets/images/candidate-images/filter_reset.svg" class="reset-filter"
                 style="padding: 8px;border: 2px dotted gray;border-radius: 15px;cursor: pointer;" width="50px" data-bs-toggle="tooltip"
                data-bs-placement="right" title="Reset filter" onclick="resetFilters()">
            </span>
        </div>
    `;

    // Create and append the custom filter inputs and button
    let customFilterHtml = `
        <div id="custom-filter" class="mt-3 col-1 text-center">
            <div data-bs-toggle="tooltip"
                data-bs-placement="right" title="Add filter">
                <img src="/assets/images/candidate-images/filter_plus.png" class="dropdown" data-bs-toggle="dropdown"
                 style="border: 2px dotted gray;border-radius: 15px;cursor: pointer" width="50px"/>
                 <ul class="dropdown-menu filter-dropdown rounded-3 glass-transparent text-primary shadow-lg">
                <li class="dropdown-item filter-items date-posted-dropdown-item">
                    <span class="date-posted">Date posted</span>
                    <ul class="dropdown-menu dropdown-submenu datePostedDropdown" id="date-posted-dropdown-submenu">
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton($(this));checkAndToggleFilterButton();">Last 24 hours</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton($(this));checkAndToggleFilterButton();">Last week</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton($(this));checkAndToggleFilterButton();">Last month</li>
                        <li class="dropdown-item filter-items">
                            <input type="text" class="px-2 rounded datefilter" name="datefilter" value="" placeholder="Custom" />
                        </li>
                    </ul>
                </li>
                <li class="dropdown-item filter-items position-dropdown-item">
                    <span>Position</span>
                    <ul class="dropdown-menu dropdown-submenu positionDropdown scrollable-submenu" id="position-dropdown-submenu">
                        <li class="dropdown-item filter-items"></li>
                    </ul>
                </li>
                <li class="dropdown-item filter-items department-dropdown-item">
                    <span>Department</span>
                    <ul class="dropdown-menu dropdown-submenu scrollable-submenu" id="department-dropdown-submenu">
                    </ul>
                </li>
                <li class="dropdown-item filter-items job-type-dropdown-item">
                    <span>Job type</span>
                    <ul class="dropdown-menu dropdown-submenu" id="job-type-dropdown-submenu">
                        <li class="dropdown-item filter-items" onclick="createJopTypeFilterButton($(this));checkAndToggleFilterButton();">Full time</li>
                        <li class="dropdown-item filter-items" onclick="createJopTypeFilterButton($(this));checkAndToggleFilterButton();">Part time</li>
                    </ul>
                </li>
                <li class="dropdown-item filter-items level-dropdown-item">
                    <span>Level</span>
                    <ul class="dropdown-menu dropdown-submenu ps-3" id="level-dropdown-submenu" style="top: -20px">
                        <div class="form-check">
                            <input class="form-check-input level-checkbox" type="checkbox" name="level" value="ENTRY_LEVEL" id="level-entry">
                            <label class="form-check-label" for="level-entry">
                                Entry level
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input level-checkbox" type="checkbox" name="level" value="JUNIOR_LEVEL" id="level-junior">
                            <label class="form-check-label" for="level-junior">
                                Junior level
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input level-checkbox" type="checkbox" name="level" value="MID_LEVEL" id="level-mid">
                            <label class="form-check-label" for="level-mid">
                                Mid level
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input level-checkbox" type="checkbox" name="level" value="SENIOR_LEVEL" id="level-senior">
                            <label class="form-check-label" for="level-senior">
                                Senior level
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input level-checkbox" type="checkbox" name="level" value="SUPERVISOR_LEVEL" id="level-supervisor">
                            <label class="form-check-label" for="level-supervisor">
                                Supervisor level
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input level-checkbox" type="checkbox" name="level" value="EXECUTIVE_LEVEL" id="level-executive">
                            <label class="form-check-label" for="level-executive">
                                Executive level
                            </label>
                        </div>
                        <div class="d-flex justify-content-end align-items-center py-2">
                            <span class="filter-items btn btn-sm btn-outline-primary rounded-pill px-2 py-1 me-3" style="font-size: 0.8rem" onclick="createLevelFilterButton($(this))">Confirm</span>
                        </div>
                    </ul>
                </li>
                <li class="dropdown-item filter-items salary-dropdown-item">
                    <span>Salary</span>
                <ul class="dropdown-menu dropdown-submenu p-2" id="salary-dropdown-submenu">
                    <div id="rangeBar1" class="custom-slider"></div>
                    <div class="d-flex justify-content-around text-center">
                        <span class="col-4 text-primary mt-3" style="font-size: 0.7rem">Min: <span id="sliderValue1" class="d-block">100000</span></span>
                        <span id="selected-salary-label" style="font-size: 0.7rem;max-height: 30px;margin-top: 10px" 
                        class="col-4 bg-primary text-white rounded-pill d-flex justify-content-center align-items-center" onclick="createSalaryFilterButton($(this));checkAndToggleFilterButton();">Confirm</span>
                        <span class="col-4 text-primary mt-3" style="font-size: 0.7rem">Max: <span id="sliderValue2" class="d-block">9000000</span></span>
                    </div>
                </ul>
                </li>
                <li class="dropdown-item filter-items applicants-dropdown-item">
                    <span>Applicants</span>
                    <ul class="dropdown-menu dropdown-submenu" id="applicants-dropdown-submenu">
                        <li class="dropdown-item filter-items" onclick="createApplicantsFilterButton($(this));checkAndToggleFilterButton();">Over require</li>
                        <li class="dropdown-item filter-items" onclick="createApplicantsFilterButton($(this));checkAndToggleFilterButton();">Doesn't reach half</li>
                    </ul>
                </li>
                <li class="dropdown-item filter-items status-dropdown-item">
                    <span>Status</span>
                    <ul class="dropdown-menu dropdown-submenu" id="status-dropdown-submenu" style="top: -90px">
                        <li class="dropdown-item filter-items" onclick="createStatusFilterButton($(this));checkAndToggleFilterButton();">Open</li>
                        <li class="dropdown-item filter-items" onclick="createStatusFilterButton($(this));checkAndToggleFilterButton();">Closed</li>
                        <li class="dropdown-item filter-items" onclick="createStatusFilterButton($(this));checkAndToggleFilterButton();">Expired</li>
                    </ul>
                </li>
            </ul>
            </div>
        </div>
    `;

    fetchTitleAndGenerateHTML().then(submenuHTML => {
        // Use the generated submenuHTML as needed
        $('#position-dropdown-submenu').html(submenuHTML);
    });

    fetchDepartmentAndGenerateHTML().then(submenuHTML => {
        // Use the generated submenuHTML as needed
        $('#department-dropdown-submenu').html(submenuHTML);
    });

    // Find the search input's parent div.row and append the custom filter inputs
    var searchRow = $('#table_filter').closest('.row');
    var recentFilterDropdownCon = `<div class="col-11" id="recent-filter-dropdown-con"></div>`;
    $(customFilterHtml).appendTo(searchRow);
    $(resetFilterButton).appendTo(searchRow);
    $('#reset-filter').hide();
    $(recentFilterDropdownCon).appendTo(searchRow);

    $('.dropdown-menu > li').hover(function () {
        $(this).children('.dropdown-submenu').css('display', 'block');
    }
    , function () {
        $(this).children('.dropdown-submenu').css('display', '');
    });

    rangeBar1 = document.getElementById('rangeBar1');
    sliderValue1 = document.getElementById('sliderValue1');
    sliderValue2 = document.getElementById('sliderValue2');
    tooltipsEnabled1 = false;

    noUiSlider.create(rangeBar1, {
        start: [250000, 800000], // Initial range values
        connect: true,   // Connect the two handles
        range: {         // Set the range
            'min': 100000,
            'max': 1000000
        },
        step: 10000,         // Add a step of 5
        // Disable tooltips initially
        tooltips: [false, false],
        css: [
            // Change the color of the range bar
            "background: linear-gradient(to right, #007BFF, #007BFF) !important;",
            // Change the color of the range buttons and dots
            ".noUi-connect { background: #1e497b !important; }",
            ".noUi-handle { background: #007BFF !important; }",
            ".noUi-tooltip { background: #007BFF !important; }",
        ],
    });

    rangeBar1.noUiSlider.on('update', function (values, handle) {
        sliderValue1.innerText = values[0];
        sliderValue2.innerText = values[1];
    });

    // Prevent dropdown-submenu from closing when interacting with the range slider
    $(rangeBar1).on('click', function (event) {
        event.stopPropagation();
    });

    // Prevent dropdown-submenu from closing when interacting with the range slider values
    $('#sliderValue1, #sliderValue2').on('click', function (event) {
        event.stopPropagation();
    });

    // Get the current date
    const currentDate = moment();

    // Date range picker
    $(function() {
        // Initialize the daterangepicker
        $('input[name="datefilter"]').daterangepicker({
            autoUpdateInput: false,
            locale: {
                cancelLabel: 'Clear'
            },
            maxDate: currentDate // Set the maximum date initially to the current date
        });

        console.log($('#date-posted-dropdown-submenu'));

        // Handle apply event to update the input value and set start and end times
        $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
            const startDate = picker.startDate.format('MM/DD/YYYY');
            const endDate = picker.endDate.format('MM/DD/YYYY');

            $(this).val(startDate + ' - ' + endDate);

            // Set the start and end times in your input fields
            createDatePostedFilterButton('Custom',startDate,endDate);
            checkAndToggleFilterButton();
        });

        // Handle cancel event to clear the input value and reset start and end times
        $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
            $(this).val('');
            $('#filter-start-time').val('');
            $('#filter-end-time').val('');
        });

        $('.daterangepicker').hover(function () {
            $('#date-posted-dropdown-submenu').css('display', 'block');
        });

        $('.daterangepicker th').each(function() {
            console.log("TH:",$(this))
            $(this).on('click', function(event) {
                console.log("Click!!!!")
                event.stopPropagation();
                $('#date-posted-dropdown-submenu').css('display', 'block');
            });
        });

    });

    // Initialize Bootstrap tooltips
    let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

});

// Prevent the dropdown from closing when date inputs are clicked
$('.startTimePicker, .endTimePicker').on('click', function (e) {
    e.stopPropagation(); // Stop the event from propagating
    $(this).closest('.dropdown-submenu').dropdown('toggle'); // Show the dropdown
});

// Add an event listener for the "Detail" link
$(document).on("click", ".show-detail-btn", function (event) {
    event.preventDefault(); // Prevent the default behavior of the link

    // Get the vacancy ID from the link's href attribute
    href = $(this).attr("href");
    vacancyId = href.split("=")[1];
    $('#reset-form').attr('data-vacancy-id', vacancyId);

    console.log("VVIDDDD",vacancyId)

    // Fetch the vacancy details using AJAX
    var apiUrl = "/vacancy/job-detail?id=" + vacancyId;

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            currentData = data;
            // Handle the successful response and display the details in the modal
            populateModalWithData(data); // Call the function to populate the modal with data
            $("#detailModal").modal("show");
            let emptyInputs = $('input[type="text"], input[type="number"], input#post, textarea').filter(function() {
                return $.trim($(this).val()) === '' && $.trim($(this).val()) <= 0 && $(this).prop('required'); // Only consider required fields
            });

            $('input[type="text"], input[type="number"], input#post, textarea').each(function () {
                $(this).removeClass('is-valid');
                $(this).removeClass('is-invalid'); // Remove Bootstrap is-invalid class
                $(this).removeClass('is-valid'); // Remove Bootstrap is-valid class if previously added
                $(this).css('background-image', 'none');
                $(this).closest('.mb-3').find('.feedback-message').css('display','none');
            });

            console.log("Empty Inputs : ",emptyInputs)
        });
});

$(document).on("click", "#reset-form", function (event) {
    event.preventDefault(); // Prevent the default behavior of the link

    // Fetch the vacancy details using AJAX
    var apiUrl = "/vacancy/job-detail?id=" + $('#id').val();

    console.log("IDDDDD",$('#id').val())

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            // Handle the successful response and display the details in the modal
            let inputsToReset = $('input[type="text"], input[type="number"], input#post, textarea').filter(function() {
                return $.trim($(this).val()) === '' && $(this).prop('required'); // Only consider required fields
            });

            // if(inputsToReset.hasClass('is-invalid')) {
            //     inputsToReset.removeClass('is-invalid');
            //     inputsToReset.addClass('is-valid');
            // }

            inputsToReset.each(function () {
                if($(this).hasClass('is-invalid')) {
                    $(this).removeClass('is-invalid');
                }
                $(this).addClass('is-valid');
            });

            $('.feedback-message').css('display','none');
            populateModalWithData(data); // Call the function to populate the modal with data
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
        '<p class="text-center text-white">Reopen will make this vacancy open for 30 days again.</p>' +
        '<div>' +
        '<button type="button" class="btn btn-sm btn-light mx-1" onclick="actionForCloseOrReopen(href)">Sure</button>' +
        '<button class="btn btn-sm btn-secondary mx-1" onclick="closeModal()">Cancel</button></div>' +
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
        '<p class="text-center text-white">After closing, no candidate will be allowed to apply this vacancy.</p>' +
        '<div>' +
        '<button type="button" class="btn btn-sm btn-light mx-1" onclick="actionForCloseOrReopen(href)">Sure</button>' +
        '<button class="btn btn-sm btn-secondary mx-1" onclick="closeModal()">Cancel</button></div>' +
        '</div>' +
        '</div>');
    $('#loadMe').modal({
        backdrop: 'static' // Set backdrop to 'static' when the "Processing..." message is shown
    }).modal('show');

});

function actionForCloseOrReopen(href) {

    // Show the loader and the message-con modal
    $('#message-con').html('<div class="loader"></div><div class="loader-txt"><p class="text-white">Processing...</p></div>');
    $('#loadMe').modal({
        backdrop: 'static' // Set backdrop to 'static' when the "Processing..." message is shown
    }).modal('show');

    const csrfToken = document.querySelector('#token').value;
    console.log(csrfToken);
    const metaCsrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");

    console.log(metaCsrfToken);

    console.log("Action was ",href)
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

    $('#calendar-btn').off('click');
    $('#timePickerBtn').off('click');

    // vacancyId = data.id;
    // console.log("IDDDDDDD",vacancyId)

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
    $("#salary").val(data.salary != 0 ? data.salary : '');
    $("#onSiteOrRemote").val(convertToEnumFormat(data.onSiteOrRemote)); // Convert and set the value
    $("#descriptions").val(data.descriptions);
    $("#responsibilities").val(data.responsibilities);
    $("#requirements").val(data.requirements);
    $("#preferences").val(data.preferences);
    $("#address").val(data.address);
    $('#note').val(data.note);

    const $calendar = $('#calendar').hide();
    const $workingDaysInput = $('#workingDays');
    const $timePickerBtn = $('#timePickerBtn');
    const $timePickerContainer = $('#timePickerContainer').hide();
    const $startTimePicker = $('#startTimePicker');
    const $endTimePicker = $('#endTimePicker');
    const $workingHoursInput = $('#workingHours'); // Add this line
    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

    function convertDaysStringToArray(daysString) {
        if(daysString === 'Mon ~ Fri') {
            daysString = "Mon ~ Tue ~ Wed ~ Thu ~ Fri";
        }else if (daysString === 'Weekend') {
            daysString = "Sun ~ Sat";
        }
        return daysString.split(' ~ ');
    }

    const workingDaysInputValue = $workingDaysInput.val();
    let selectedDays = convertDaysStringToArray(workingDaysInputValue);


    console.log(selectedDays); // Output: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri']

    function convertTimeTo24HourFormat(time) {
        const [hours, minutes, period] = time.match(/(\d+):(\d+)\s*(\w+)/).slice(1);

        const numericHours = parseInt(hours);

        if (period.toLowerCase() === 'pm' && numericHours !== 12) {
            return `${numericHours + 12}:${minutes}`;
        } else if (period.toLowerCase() === 'am' && numericHours === 12) {
            return `00:${minutes}`;
        } else {
            const formattedHours = numericHours.toString().padStart(2, '0'); // Add leading zero if needed
            return `${formattedHours}:${minutes}`;
        }
    }

    const timeRange = $('#workingHours').val();
    console.log("Time Range : ",timeRange)
    const [start, end] = timeRange.split(' ~ ');

    let startTime = convertTimeTo24HourFormat(start);
    let endTime = convertTimeTo24HourFormat(end);

    console.log('Start Time:', startTime); // Output: 09:00
    console.log('End Time:', endTime);     // Output: 18:00

    $('#calendar-btn').on('click', function() {
        $calendar.toggle();
    });

    function updateInputValue() {
        if (selectedDays.length === 5 && selectedDays.every(day => ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'].includes(day))) {
            $workingDaysInput.val('Mon ~ Fri');
        } else if (selectedDays.length === 2 && selectedDays.includes('Sun') && selectedDays.includes('Sat')) {
            $workingDaysInput.val('Weekend');
        } else {
            let textSelectedDays = daysOfWeek.filter(day => selectedDays.includes(day));
            $workingDaysInput.val(textSelectedDays.join(' ~ '));
        }

        console.log("Start Time : ", startTime);
        console.log("End Time : ",endTime);
        // Format start and end times
        const formattedStartTime = formatTime(startTime);
        const formattedEndTime = formatTime(endTime);

        // Update workingHours input value
        $workingHoursInput.val(`${formattedStartTime} ~ ${formattedEndTime}`);
    }

    $endTimePicker.on('change', function() {
        endTime = $(this).val(); // Update the endTime value from the time picker
        updateInputValue();
    });

    function formatTime(time) {
        const [hours, minutes, period] = time.split(/[:\s]/);
        const numericHours = parseInt(hours); // Convert hours to a number

        if (numericHours === 12) {
            return `12:${minutes} PM`;
        } else if (numericHours === 0 || numericHours === 24) {
            return `12:${minutes} AM`;
        } else if (numericHours > 12) {
            return `${numericHours - 12}:${minutes} PM`;
        } else {
            return `${numericHours}:${minutes} AM`;
        }
    }

    function updateCalendar() {
        $calendar.empty();

        daysOfWeek.forEach(day => {
            const $dayElement = $('<div>', {
                text: day,
                class: 'calendar-day'
            });

            if (selectedDays.includes(day)) {
                $dayElement.addClass('selected');
            }

            $dayElement.on('click', () => {
                if (selectedDays.length === 1 && selectedDays.includes(day)) {
                    // If there's only one selected day, prevent unselecting it
                    return;
                }

                if (selectedDays.includes(day)) {
                    selectedDays = selectedDays.filter(selectedDay => selectedDay !== day);
                } else {
                    selectedDays.push(day);
                }

                updateInputValue();
                updateCalendar();
            });

            $calendar.append($dayElement);
        });
    }

    $timePickerBtn.on('click', function() {
        $timePickerContainer.toggle();
    });

    $startTimePicker.on('change', function() {
        startTime = $(this).val();
        updateInputValue();
    });

    $endTimePicker.on('change', function() {
        endTime = $(this).val();
        updateInputValue();
    });

    let inputsToReset = $('input[type="text"], input[type="number"], input#post, textarea').filter(function() {
        return $.trim($(this).val()) === '' && $(this).prop('required'); // Only consider required fields
    });

    updateCalendar();
    updateInputValue();

    // Attach input event handler to the salary input
    $('#salary').on('input', function() {
        const inputValue = $(this).val();

        // Remove all non-numeric characters using regex
        const numericValue = inputValue.replace(/\D/g, '');

        // Update the input value with the cleaned numeric value
        $(this).val(numericValue);
    });
}

// Function to show the modal for a specific vacancy ID
function showDetailModalForVacancyId(vacancyId) {
    // Fetch the vacancy details using AJAX
    var apiUrl = "/vacancy/job-detail?id=" + vacancyId;

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            currentData = data;
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

function convertToLakhs(decimalValue) {
    const lakhsValue = parseFloat(decimalValue) / 100000;
    const formattedValue = lakhsValue.toFixed(6).replace(/\.?0+$/, ''); // Remove trailing zeros
    return `${formattedValue} Lakhs`;
}
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

    // Format the date as "Dayth Month Year" (e.g., "27th Jul 2023")
    var formattedDate = day + suffix + " " + monthNames[date.getMonth()] + " " + date.getFullYear();
    return formattedDate;
}

function changeTime(time) {
    let dateString = time;

    // Parse the date string to a JavaScript Date object
    let date = new Date(dateString);
    let hours = date.getHours();
    let minutes = date.getMinutes();
    let period = 'AM';

    if (hours === 12) {
        hours = 12;
        period = 'AM';
    } else if (hours > 12) {
        hours = hours - 12;
        period = 'PM';
    }

    if (hours < 10) {
        hours = '0' + hours;
    }

    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    return hours + ":" + minutes + " " + period;

}
