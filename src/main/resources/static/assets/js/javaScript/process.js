var table;

let filterElements = [
    { name: 'apply-date-dropdown-item', isRemove: false, filterId: 'filter-apply-date' },
    { name: 'position-dropdown-item', isRemove: false, filterId: 'filter-title' },
    { name: 'department-dropdown-item', isRemove: false, filterId: 'filter-department' },
];

async function filterSwitch(){

    // Get a reference to the checkbox and the filter input element
    const checkbox = document.getElementById("withFilter");
    const filterInput = document.getElementById("filter");

    // Add an event listener to the checkbox to monitor changes
    checkbox.addEventListener("change", function() {
        if (checkbox.checked) {
            // If the checkbox is checked, set the value of the filter input to "1"
            filterInput.value = "1";
        } else {
            // If the checkbox is unchecked, set the value of the filter input to an empty string or any other desired value
            filterInput.value = "0";
        }
    });

}



$(document).ready(function () {

     


    
    table = $('#table2').DataTable(
        {
            "serverSide": true,
            "processing": true,
            "ajax": '/process',
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
                    targets: 6, createdCell: function (td) {
                        $(td).css('background-color', "#D5F5E3")
                    },
                    data: "passedCandidate",
                    render: function (data) {
                        let passed = data == null ? '-' : data;
                        let passBtn = (passed == '-') ?
                            '<span>' + passed + '</span>' :
                            '<input type="submit" value="' + passed + '">';
                        return passBtn;


                    },
                    sortable: false
                },
                {

                    data: "pendingCandidate",
                    targets: 7,
                    createdCell: function (td) {
                        $(td).css('background-color', "#D5F5E3")
                    },
                    render: function (data) {
                        let pend = data == null ? '<span>-</span>' :
                            '<input type="submit" value="' + data + '">';
                        return pend;
                    },
                    sortable: false


                },
                {
                    targets: 8,
                    data: 'cancelCandidate',
                    createdCell: function (td) {
                        $(td).css('background-color', "#D5F5E3")
                    },
                    render: function (data) {
                        let cancel = data == null ? '<span>-</span>' :
                            '<input type="submit" value="' + data + '">';
                        return cancel;
                    },
                    sortable: false
                },
                {
                    targets: 9,
                    data: "notInterviewCandidate",
                    createdCell: function (td) {
                        $(td).css('background-color', "#D5F5E3 ")
                    },
                    render: function (data) {
                        let not = data == null ? '<span>-</span>' :
                            '<input type="submit" value="' + data + '">';
                        return not;
                    },
                    sortable: false
                },
                {
                    targets: 10,
                    data: 'acceptedCandidate',
                    render: function (data) {
                        let acc = data == null ? '<span>-</span>' :
                            '<input type="submit" value="' + data + '">';
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
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton('Today');checkAndToggleFilterButton();">Today</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton('Last Week');checkAndToggleFilterButton();">Last Week</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton('Last Month');checkAndToggleFilterButton();">Last Month</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton('Last 6 Month');checkAndToggleFilterButton();">Last 6 Month</li>
                        <li class="dropdown-item filter-items" onclick="createDatePostedFilterButton('Last Year');checkAndToggleFilterButton();">Last Year</li>

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
                                <form action="/interview_process/pdf">
                                <input type="hidden" name="filter" id="filter" value="0">
                                <input type="hidden" name="openDate" id="filter-apply-date" value="">    
                                <input type="hidden" name="position" id="filter-title" value="">                            
                                <input type="hidden" name="department" id="filter-department" value="">
                                    <button id="pdfDownload" class="image-button" aria-label="Download pdf" 
                                    ></button>
                                </form>
                    		</div>
                    		<div class="col-6 text-center" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Report Excel">
                                <form action="/interview_process/excel">
                                <input type="hidden" name="filter" id="filter" value="0">
                                <input type="hidden" name="openDate" id="filter-apply-date" value="">    
                                <input type="hidden" name="position" id="filter-title" value="">                            
                                <input type="hidden" name="department" id="filter-department" value="">
                                    <button id="excelDownload" class="image-button" aria-label="Download Excel" 
                                    ></btutton>
                                </form>
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

    if(table !== undefined){
    filterSwitch();
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
function createDatePostedFilterButton(selectedValue) {
    console.log('>>>>>>>>>>>>', selectedValue)
    var filterOption = $(this).find('option:selected').val();
    var currentDate = new Date();
    var endDate = currentDate.toISOString().split('T')[0]; // End date is today
    var startDate = new Date(currentDate);

    filterElements[0].isRemove = true;
    $('.apply-date-dropdown-item').hide();

    let selectedText = null;


    if (selectedValue === 'Custom') {
        $('#filter-start-date').val(startDate);
        $('#filter-end-date').val(endDate);
        selectedText = selectedValue;
        $('#filter-apply-date').val(selectedText);
    } else {
        selectedText = selectedValue;
        $('#filter-apply-date').val(selectedText);
    }
    switch (selectedValue) {
        case 'Today':
            var first = currentDate.toISOString().split('T')[0];
            console.log(first) // Convert to ISO format (YYYY-MM-DD)
            table.column(0).search(first).draw();
            break;

        case 'Last Week':
            // Copy current date to calculate the start date
            startDate.setDate(currentDate.getDate() - 7); // Subtract 7 days from current date
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate);
            // Perform action for 'last_week' option
            table.column(0).search(isoStartDate + ';' + endDate).draw();
            break;

        case 'Last Month':
            startDate.setMonth(currentDate.getMonth() - 1); // Subtract 1 month
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate); // Output: 2023-08-16 (example)

            // Perform action for 'last_month' option
            table.column(0).search(isoStartDate + ';' + endDate).draw();
            break;
        case 'Last 6 Month':
            startDate.setMonth(currentDate.getMonth() - 6); // Subtract 1 month
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate); // Output: 2023-08-16 (example)

            // Perform action for 'last_month' option
            table.column(0).search(isoStartDate + ';' + endDate).draw();
            break;
        case 'Last Year':
            startDate.setFullYear(currentDate.getFullYear() - 1)
            var isoStartDate = startDate.toISOString().split('T')[0];
            console.log(startDate);
            console.log(endDate);

            // Perform action for 'past_year' option
            table.column(0).search(isoStartDate + ';' + endDate).draw();
            break;
        case '':
            table.column(0).search('' + ';' + '').draw();
            break;
        default:
            return false;
    }
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn date-posted-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedValue}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="apply-date-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu datePostedDropdown" id="date-posted-filter-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="SelectedFilterName($(this));" data-filter-id="filter-apply-date">Today</li>
                <li class="dropdown-item filter-items" onclick="SelectedFilterName($(this));" data-filter-id="filter-apply-date">Last Week</li>
                <li class="dropdown-item filter-items" onclick="SelectedFilterName($(this));" data-filter-id="filter-apply-date">Last Month</li>
                <li class="dropdown-item filter-items" onclick="SelectedFilterName($(this));" data-filter-id="filter-apply-date">Last 6 Month</li>
                <li class="dropdown-item filter-items" onclick="SelectedFilterName($(this));" data-filter-id="filter-apply-date">Last Year</li>

                <li class="dropdown-item filter-items">
                    <input type="text" class="px-2 rounded datefilter2" name="datefilter2" value="" placeholder="Custom" />
                </li>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);
    $(function () {
        // Replace value of date range
        replaceDateFilter2Value();
        // Initialize the daterangepicker
        $('input[name="datefilter2"]').daterangepicker({
            autoUpdateInput: false,
            locale: {
                cancelLabel: 'Clear'
            }
        });

        // Handle apply event to update the input value and set start and end times
        $('input[name="datefilter2"]').on('apply.daterangepicker', function (ev, picker) {
            const startDate = picker.startDate.format('MM/DD/YYYY');
            const endDate = picker.endDate.format('MM/DD/YYYY');

            $(this).val(startDate + ' - ' + endDate);

            $('#filter-start-date').val(startDate);
            $('#filter-end-date').val(endDate);
            $('.datePostedDropdown').hide();

            // Get the selected item with a data-filter-id attribute
            const selectedFilterItem = $('<li class="dropdown-item filter-items" data-filter-id="filter-apply-date">Custom</li>');

            // Call the function and pass the selected item
            changeSelectedFilterName(selectedFilterItem);

        });

        // Handle cancel event to clear the input value and reset start and end times
        $('input[name="datefilter2"]').on('cancel.daterangepicker', function (ev, picker) {
            $(this).val('');
            $('#filter-start-time').val('');
            $('#filter-end-time').val('');
        });

        $('.daterangepicker').hover(function () {
            $('.datePostedDropdown').css('display', 'block');
        }, function () {
            $('.datePostedDropdown').css('display', '');
        });
    });

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();


}

function checkAndToggleFilterButton() {
    let anyIsRemove = false;

    for (let i = 0; i < filterElements.length; i++) {
        if (!filterElements[i].isRemove) {
            anyIsRemove = true;
            break; // Exit the loop once an element with isRemove = true is found
        }
    }

    console.log("Filter Elements", filterElements)
    console.log("AnyIsRemove", anyIsRemove)

    // Toggle the visibility of the buttons based on the anyIsRemove variable
    if (anyIsRemove) {
        $('#custom-filter').show();
        $('#reset-filter').hide();
    } else {
        $('#reset-filter').show();
        $('#custom-filter').hide();
    }
}
function SelectedFilterName(item) {
    console.log(item)
    let selectedValue = $(item).text(); // Get the selected value from the clicked item
    console.log("Selected Value : ", selectedValue);
    let button = $(item).closest('.btn-group').find('.recent-filter-dropdown-btn');

    let filterId = $(item).data('filter-id');

    // Find and update the isRemove property in filterElements
    console.log(filterId)
    for (let i = 0; i < filterElements.length; i++) {
        console.log(filterElements[i].filterId)
        if (filterElements[i].filterId === filterId) {
            $('#' + filterElements[i].filterId).val($.trim(selectedValue));
            break; // Exit the loop once the element is found
        }
    }
    var filterOption = $(this).find('option:selected').val();
    var currentDate = new Date();
    var endDate = currentDate.toISOString().split('T')[0]; // End date is today
    var startDate = new Date(currentDate);
    switch (selectedValue) {
        case 'Today':
            var first = currentDate.toISOString().split('T')[0];
            console.log(first) // Convert to ISO format (YYYY-MM-DD)
            table.column(0).search(first).draw();
            break;

        case 'Last Week':
            // Copy current date to calculate the start date
            startDate.setDate(currentDate.getDate() - 7); // Subtract 7 days from current date
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate);
            // Perform action for 'last_week' option
            table.column(0).search(isoStartDate + ';' + endDate).draw();
            break;

        case 'Last Month':
            startDate.setMonth(currentDate.getMonth() - 1); // Subtract 1 month
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate); // Output: 2023-08-16 (example)

            // Perform action for 'last_month' option
            table.column(0).search(isoStartDate + ';' + endDate).draw();
            break;
        case 'Last 6 Month':
            startDate.setMonth(currentDate.getMonth() - 6); // Subtract 1 month
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate); // Output: 2023-08-16 (example)

            // Perform action for 'last_month' option
            table.column(0).search(isoStartDate + ';' + endDate).draw();
            break;
        case 'Last Year':
            startDate.setFullYear(currentDate.getFullYear() - 1)
            var isoStartDate = startDate.toISOString().split('T')[0];
            console.log(startDate);
            console.log(endDate);

            // Perform action for 'past_year' option
            table.column(0).search(isoStartDate + ';' + endDate).draw();
            break;
        case '':
            table.column(0).search('' + ';' + '').draw();
            break;
        default:
            return false;
    }

    // if ($('input[name="datefilter2"]').length > 0) {
    if (item != 'Custom') {
        $('input[name="datefilter2"]').val('');
        button.text($.trim(selectedValue)); // Update the text of the button
    } else {
        $('.date-posted-filter-btn').text('Custom');
    }
    // }
}
function createTitleFilterButton(selectedValue) {

    filterElements[1].isRemove = true;
    $('.position-dropdown-item').hide();

    $('#filter-title').val(selectedValue);
    console.log(selectedValue)
    checkAndToggleFilterButton();

    table.column(2).search(selectedValue).draw();

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn position-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedValue}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="position-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu scrollable-submenu" id="position-filter-dropdown-submenu">
                <!-- Submenu items will be populated here -->
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Fetch and populate submenu items
    fetch('/allPositions')
        .then(response => response.json()) // Assuming the response is JSON
        .then(data => {
            let submenuHTML = '';
            let currentLetter = null;

            data.forEach(item => {
                const startingLetter = item.name[0].toUpperCase();
                if (startingLetter !== currentLetter) {
                    submenuHTML += `<li style="background: #1e497b"><b class="ps-2 text-white">${startingLetter}</b></li>`;
                    currentLetter = startingLetter;
                }
                submenuHTML += `
                <li class="dropdown-item filter-items"
                    onclick="createTitleFilterButton('${item.name}'); checkAndToggleFilterButton();" data-filter-id="filter-title">
                  ${item.name}
                </li>`;
            });

            // Use the generated submenuHTML to populate the submenu
            $('#position-filter-dropdown-submenu').html(submenuHTML);

            // Remove the existing onclick attribute from recent-filter-items
            $('#position-filter-dropdown-submenu .filter-items').removeAttr('onclick');

            // Add the onclick attribute to every recent-filter-items
            $('#position-filter-dropdown-submenu .filter-items').attr('onclick', 'changeSelectedFilterName(this);');

            // Hide other remove buttons and show the recent-filter-dropdown-btn
            $('.selected-dropdown-remove-button').hide();
            $('.recent-filter-dropdown-btn').show();
        })
        .catch(error => {
            console.error('Error:', error);
        });


}
function checkAndToggleFilterButton() {
    let anyIsRemove = false;

    for (let i = 0; i < filterElements.length; i++) {
        if (!filterElements[i].isRemove) {
            anyIsRemove = true;
            break; // Exit the loop once an element with isRemove = true is found
        }
    }

    console.log("Filter Elements", filterElements)
    console.log("AnyIsRemove", anyIsRemove)

    // Toggle the visibility of the buttons based on the anyIsRemove variable
    if (anyIsRemove) {
        $('#custom-filter').show();
        $('#reset-filter').hide();
    } else {
        $('#reset-filter').show();
        $('#custom-filter').hide();
    }
}
function showSelectedDropdownRemoveButton(button) {
    const removeButton = $(button).next('.selected-dropdown-remove-button');
    removeButton.show();
}
function changeSelectedFilterName(item) {
    console.log(item)

    let selectedValue = $(item).text();
    console.log(selectedValue)
    if (item) {

        console.log("hello")
        table.column(2).search(selectedValue).draw();
        console.log("Selected Value-2 : ", selectedValue);
        let button = $(item).closest('.btn-group').find('.recent-filter-dropdown-btn');
        let filterId = $(item).data('filter-id');

        // Find and update the isRemove property in filterElements
        console.log(filterId)
        for (let i = 0; i < filterElements.length; i++) {
            console.log(filterElements[i].filterId)
            if (filterElements[i].filterId === filterId) {
                $('#' + filterElements[i].filterId).val($.trim(selectedValue));
                break; // Exit the loop once the element is found
            }
        }

        // if ($('input[name="datefilter2"]').length > 0) {
        if (selectedValue != 'Custom') {
            $('input[name="datefilter2"]').val('');
            button.text($.trim(selectedValue)); // Update the text of the button
        } else {
            $('.apply-date-filter-btn').text('Custom');
        }
        // }
    }

}
function changeSelectedFilterDepartment(item) {
    console.log(item)

    let selectedValue = $(item).text();
    console.log(selectedValue)
    if (item) {

        console.log("hello")
        table.column(3).search(selectedValue).draw();
        console.log("Selected Value-2 : ", selectedValue);
        let button = $(item).closest('.btn-group').find('.recent-filter-dropdown-btn');
        let filterId = $(item).data('filter-id');

        // Find and update the isRemove property in filterElements
        console.log(filterId)
        for (let i = 0; i < filterElements.length; i++) {
            console.log(filterElements[i].filterId)
            if (filterElements[i].filterId === filterId) {
                $('#' + filterElements[i].filterId).val($.trim(selectedValue));
                break; // Exit the loop once the element is found
            }
        }

        // if ($('input[name="datefilter2"]').length > 0) {
        if (selectedValue != 'Custom') {
            $('input[name="datefilter2"]').val('');
            button.text($.trim(selectedValue)); // Update the text of the button
        } else {
            $('.apply-date-filter-btn').text('Custom');
        }
        // }
    }

}
function createDepartmentFilterButton(selectedValue) {
    table.column(3).search(selectedValue).draw();
    filterElements[2].isRemove = true;
    $('.department-dropdown-item').hide();

    $('#filter-department').val(selectedValue);

    checkAndToggleFilterButton();

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn position-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedValue}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="department-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu scrollable-submenu" id="department-filter-dropdown-submenu">
                <!-- Submenu items will be populated here -->
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Fetch and populate submenu items
    fetchDepartmentAndGenerateHTML().then(submenuHTML => {

        $('#department-filter-dropdown-submenu').html(submenuHTML);

        $('#department-filter-dropdown-submenu .filter-items').removeAttr('onclick');

        // Add the onclick attribute to every recent-filter-items within the department-filter-dropdown-submenu
        $('#department-filter-dropdown-submenu .filter-items').attr('onclick', 'changeSelectedFilterDepartment(this);');

        // Hide other remove buttons and show the recent-filter-dropdown-btn
        $('.selected-dropdown-remove-button').hide();
        $('.recent-filter-dropdown-btn').show();
    });

    // Update data table
    updateDataTable();
}
function updateDataTable() {
    $('#filter-vacancy-info-id').val("All");
    table.ajax.reload();
}
function resetFilters() {

    console.log("Reset filters work");

    // Find and update the isRemove property in filterElements
    for (let i = 0; i < filterElements.length; i++) {
        filterElements[i].isRemove = false;
        $('.' + filterElements[i].name).show();
        $('#' + filterElements[i].filterId).val('');
    }
    $('.selected-dropdown-remove-button').each(function () {
        $(this).closest('.btn-group').remove();
    });
    table.column(0).search('').draw();
    table.column(2).search('').draw();
    table.column(3).search('').draw();

    $('#reset-filter').hide();
    $('#custom-filter').show();

    updateDataTable();
}
function replaceDateFilter2Value() {
    const dateFilterValue = $('input[name="datefilter"]').val();
    $('input[name="datefilter2"]').val(dateFilterValue);
    $('input[name="datefilter"]').val('')
}
$(document).on('click', '.selected-dropdown-remove-button', function () {
    let filterName = $(this).data('filter-name');

    // Find and update the isRemove property in filterElements
    for (let i = 0; i < filterElements.length; i++) {
        if (filterElements[i].name === filterName) {
            filterElements[i].isRemove = false;
            $('.' + filterElements[i].name).show();
            $('#' + filterElements[i].filterId).val('');
            break; // Exit the loop once the element is found
        }
    }

    console.log("Filter name : ", filterName);


    if (filterName === 'apply-date-dropdown-item') {
        table.column(0).search('').draw();
    } else if (filterName === 'position-dropdown-item') {
        table.column(2).search('').draw();
    } else if (filterName === 'department-dropdown-item') {
        table.column(5).search('').draw();
    }

    $(this).closest('.btn-group').remove();
    checkAndToggleFilterButton();

});