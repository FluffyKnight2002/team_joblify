var table;

async function filterSwitch() {

    // Get a reference to the checkbox and the filter input element
    const checkbox = document.getElementById("withFilter");
    const pdfFilterInput = document.getElementById("filter");

    // Add an event listener to the checkbox to monitor changes
    checkbox.addEventListener("change", function () {
        if (checkbox.checked) {
            // If the checkbox is checked, set the value of the filter input to "1"
            pdfFilterInput.value = "1";

        } else {
            // If the checkbox is unchecked, set the value of the filter input to an empty string or any other desired value
            pdfFilterInput.value = "0";
        }

    })
}


async function reportDownload(){

    // JavaScript to handle form submission when links are clicked
    document.getElementById('pdfDownload').addEventListener('click', function(e) {
        e.preventDefault(); // Prevent the default link behavior

        document.getElementById('combinedForm').action = '/interview_process/pdf'; // Set the form action
        document.getElementById('combinedForm').submit(); // Submit the form
    });

    document.getElementById('excelDownload').addEventListener('click', function(e) {
        e.preventDefault(); // Prevent the default link behavior

        document.getElementById('combinedForm').action = '/interview_process/excel'; // Set the form action
        document.getElementById('combinedForm').submit(); // Submit the form
    });


}

$(document).ready(function () {





    table = $('#table2').DataTable(
        {
            "serverSide": true,
            "processing": true,
            "ajax": {

                url: '/process',
                type: 'GET',
                data: function (d) {
                    d.vacancyInfoId = $('#filter-vacancy-info-id').val();
                    d.applyDate = $('#filter-apply-date').val(),
                        d.title = $('#filter-title').val(),
                        d.department = $('#filter-department').val(),
                        d.startDateInput = $('#filter-start-date').val(),
                        d.endDateInput = $('#filter-end-date').val(),
                        d.level = $('#filter-level').val(),
                        d.selectionStatus = $('#filter-selection-status').val(),
                        d.interviewStatus = $('#filter-interview-status').val()
                }
            },
            "scrollY": 300,
            "scrollX": true,
            "scrollCollapse": true,
            "fixedHeader": {
                "header": true,
            },

            "columns": [
                {
                    data: "openDate",
                    render: function (data, type, row) {
                        return changeTimeFormat(data);
                    },
                    targets: 0
                },
                {
                    data: "closeDate",
                    render: function (data, type, row) {
                        return changeTimeFormat(data);
                    },
                    targets: 1
                },
                {
                    targets: 2,
                    data: "position",
                    className: "position",
                    render: function (data, type, row) {
                        return `
                            <div>
                                <a class="btn btn-sm btn-primary show-position w-100 d-flex justify-content-center align-items-center"
                                    style="min-height: 51.33px"
                                   href="/candidate-view-summary?viId=${row.id}&name=${data}">${data}</a>
                            </div>
                            `;

                    },
                    // sortable: false

                },
                {
                    targets: 3,
                    data: 'department'

                },
                {
                    targets: 4,
                    data: "totalCandidate",
                    render: function (data) {
                        let total = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-info bg-gradient rounded-pill px-4">' + data + '</span></div>';
                        return total;
                    },
                    sortable: false
                },
                {
                    targets: 5,
                    data: 'interviewedCounts',
                    render: function (data) {
                        let inter = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-dark bg-gradient rounded-pill px-4">' + data + '</span></div>';
                        return inter;
                    },
                    sortable: false



                },
                {
                    targets: 6,
                    data: "passedCandidate",
                    render: function (data) {
                        let passed = data == null ? '-' : data;
                        let passBtn = (passed == '-') ?
                            '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-success bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return passBtn;



                    },
                    sortable: false
                },
                {

                    data: "pendingCandidate",
                    targets: 7,
                    render: function (data) {
                        let pend = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-warning bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return pend;
                    },
                    sortable: false


                },
                {
                    targets: 8,
                    data: 'cancelCandidate',
                    render: function (data) {
                        let cancel = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-danger bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return cancel;
                    },
                    sortable: false
                },
                {
                    targets: 9,
                    data: "notInterviewCandidate",
                    render: function (data) {
                        let not = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-secondary bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return not;
                    },
                    sortable: false
                },
                {
                    targets: 10,
                    data: 'acceptedCandidate',
                    render: function (data) {
                        let acc = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-gradient-ltr rounded-pill px-4">' + data +'</span></div>';
                        return acc;
                    },
                    sortable: false
                },

            ],
            order: [[0, 'desc']]

        });

    // Filter session start
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
                <li class="dropdown-item filter-items apply-date-dropdown-item">
                    <span class="date-posted">Open Date</span>
                    <ul class="dropdown-menu dropdown-submenu datePostedDropdown" id="apply-date-dropdown-submenu">
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton($(this));checkAndToggleFilterButton();">Today</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton($(this));checkAndToggleFilterButton();">Last Week</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton($(this));checkAndToggleFilterButton();">Last Month</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton($(this));checkAndToggleFilterButton();">Last 6 Month</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton($(this));checkAndToggleFilterButton();">Last Year</li>

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
               <li>
           
            </ul>
            </div>
        </div>
    `;

    console.log("Table", $('#table2'))

    fetchTitleAndGenerateHTML().then(submenuHTML => {
        // Use the generated submenuHTML as needed
        $('#position-dropdown-submenu').html(submenuHTML);
    });

    fetchDepartmentAndGenerateHTML().then(submenuHTML => {
        // Use the generated submenuHTML as needed
        $('#department-dropdown-submenu').html(submenuHTML);
    });

    // Find the search input's parent div.row and append the custom filter inputs
    let searchRow = $('#table2_filter').closest('.row');
    $('.dt-row').css('margin-bottom', '40px')
    let recentFilterDropdownCon = `<div class="col-9" id="recent-filter-dropdown-con"></div>`;
    let reportButtonCon =
        `<div class="col-auto pt-2" id="report-button-con">
			<div class="row">
            		<div class="bg-primary rounded px-1">
            			<div class="text-center row">
                			<div class="text-light fw-bolder fs-6" >Reporting</div>
						</div>
            			<div class="row">
            				<div class="col-6 text-center" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Report PDF">
                                
                                
                                    <a id="pdfDownload" class="image-button" aria-label="Download pdf" 
                                    ></a>
                                
                    		</div>
                    		<div class="col-6 text-center" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Report Excel">
                                
                                
                                    <a id="excelDownload" class="image-button" aria-label="Download Excel" 
                                    ></a>
                                
                			</div>
                		</div>
                		<div class="text-center row">
                			<div class="form-check form-switch">
                                <label class="form-check-label text-light" for="withFiler" style="font-size: 0.8rem">Including filter</label>
                                <input class="form-check-input" type="checkbox" name="withFilter" id="withFilter" style="font-size: 0.7rem;transform: translate(5px,5px);}">
                            </div>
						</div>
            		</div>
            	</div>
			</div>
		</div>`;
    $(customFilterHtml).appendTo(searchRow);
    $(resetFilterButton).appendTo(searchRow);
    $('#reset-filter').hide();
    $(recentFilterDropdownCon).appendTo(searchRow);
    $(reportButtonCon).appendTo(searchRow);

    $('.dropdown-menu > li').hover(function () {
            $(this).children('.dropdown-submenu').css('display', 'block');
        }
        , function () {
            $(this).children('.dropdown-submenu').css('display', '');
        });

    // Get the current date
    const currentDate = moment();

    // Date range picker
    $(function () {
        // Initialize the daterangepicker
        $('input[name="datefilter"]').daterangepicker({
            autoUpdateInput: false,
            locale: {
                cancelLabel: 'Clear'
            },
            maxDate: currentDate // Set the maximum date initially to the current date
        });

        console.log($('#apply-date-dropdown-submenu'));

        // Handle apply event to update the input value and set start and end times
        $('input[name="datefilter"]').on('apply.daterangepicker', function (ev, picker) {
            const startDate = picker.startDate.format('MM/DD/YYYY');
            const endDate = picker.endDate.format('MM/DD/YYYY');

            console.log(startDate + 'aaaaaaaa' + endDate);

            $(this).val(startDate + ' - ' + endDate);

            // Set the start and end times in your input fields
            createDatePostedFilterButton('Custom', startDate, endDate);
            checkAndToggleFilterButton();
        });

        // Handle cancel event to clear the input value and reset start and end times
        $('input[name="datefilter"]').on('cancel.daterangepicker', function (ev, picker) {
            $(this).val('');
            $('#filter-start-time').val('');
            $('#filter-end-time').val('');
        });

        $('.daterangepicker').hover(function () {
            $('#apply-date-dropdown-submenu').css('display', 'block');
        });

        $('.daterangepicker th').each(function () {
            console.log("TH:", $(this))
            $(this).on('click', function (event) {
                console.log("Click!!!!")
                event.stopPropagation();
                $('#apply-date-dropdown-submenu').css('display', 'block');
            });
        });

    });

    // Initialize Bootstrap tooltips
    let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    if (table !== undefined) {
        filterSwitch();

        reportDownload();
    }
});
async function fetchTitleAndGenerateHTML() {
    try {
        const response = await fetch('titles'); // Replace 'titles' with the actual URL
        if (!response.ok) {
            throw new Error('Failed to fetch data');
        }

        const fetchedData = await response.json();

        // Sort the data alphabetically
        const sortedData = fetchedData.sort();

        // Create an HTML string to store the submenu content
        let submenuHTML = '';

        let currentLetter = null;
        sortedData.forEach(item => {
            const startingLetter = item.name[0].toUpperCase();
            if (startingLetter !== currentLetter) {
                submenuHTML += `<li style="background: #1e497b"><b class="ps-2 text-white">${startingLetter}</b></li>`;
                currentLetter = startingLetter;
            }
            submenuHTML += `
                <li class="dropdown-item filter-items"
                    onclick="createTitleFilterButton('${item.name}');checkAndToggleFilterButton();"  data-filter-id="filter-title">
                  ${item.name}
                </li>`;
        });

        // Return the generated HTML content
        return submenuHTML;
    } catch (error) {
        console.error('An error occurred:', error);
        return ''; // Return an empty string in case of an error
    }
}
async function fetchDepartmentAndGenerateHTML() {
    try {
        const response = await fetch('departments'); // Replace 'titles' with the actual URL
        if (!response.ok) {
            throw new Error('Failed to fetch data');
        }

        const fetchedData = await response.json();

        // Sort the data alphabetically
        const sortedData = fetchedData.sort();

        // Create an HTML string to store the submenu content
        let submenuHTML = '';

        let currentLetter = null;
        sortedData.forEach(item => {
            const startingLetter = item.name[0].toUpperCase();
            if (startingLetter !== currentLetter) {
                submenuHTML += `<li style="background: #1e497b"><b class="ps-2 text-white">${startingLetter}</b></li>`;
                currentLetter = startingLetter;
            }
            submenuHTML += `<li class="dropdown-item filter-items" 
                onclick="createDepartmentFilterButton('${item.name}');checkAndToggleFilterButton();" data-filter-id="filter-department">
                    ${item.name}
                </li>`;
        });

        // Return the generated HTML content
        return submenuHTML;
    } catch (error) {
        console.error('An error occurred:', error);
        return ''; // Return an empty string in case of an error
    }
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
            case 1:
                suffix = "st";
                break;
            case 2:
                suffix = "nd";
                break;
            case 3:
                suffix = "rd";
                break;
            default:
                suffix = "th";
        }
    }

    // Format the date as "Dayth Month Year" (e.g., "27th Jul 2023")
    var formattedDate = day + suffix + " " + monthNames[date.getMonth()] + " " + date.getFullYear();
    return formattedDate;
}


