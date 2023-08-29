let userRole;

// Define the closeModal function in a global scope
function closeModal() {
    $('#confirmationModal').modal('hide');
}

function departmentFiltered(selectedValue) {
    table.column(5).search(selectedValue).draw();
}


function roleFiltered(selectedValue) {
    console.log(selectedValue);
    let data;
    if (selectedValue === 'Default HR') {
        data = 'DEFAULT_HR'
    } else if (selectedValue === 'Senior HR') {
        data = 'SENIOR_HR'
    } else if (selectedValue === 'Junior HR') {
        data = 'JUNIOR_HR'
    } else if (selectedValue === 'Management') {
        data = 'MANAGEMENT'
    } else if (selectedValue === 'Interviewer') {
        data = 'INTERVIEWER'
    } else if (selectedValue === 'Other') {
        data = 'OTHER'
    } else {
        data = ''
    }


    table.column(6).search(data).draw();
}


function statusFiltered(selectedValue) {

    let data;
    if (selectedValue === 'Active') {
        data = true;
    } else if (selectedValue === 'Suspend') {
        data = false;
    }
    table.column(7).search(data).draw();

}


function createdDateFiltered(selectedValue) {
    console.log(selectedValue);
    let data;
    const currentDate = new Date();
    const endDate = currentDate.toISOString().split('T')[0]; // End date is today
    const startDate = new Date(currentDate);
    const isoStartDate = startDate.toISOString().split('T')[0];

    if (selectedValue === '') {
        data = '';
    } else if (selectedValue === 'Last 24 hours') {
        startDate.setHours(currentDate.getHours() - 24);
        data = isoStartDate + ';' + endDate;

    } else if (selectedValue === 'Last week') {
        startDate.setDate(currentDate.getDate() - 7);
        const isoLastWeekStartDate = startDate.toISOString().split('T')[0]; // ISO formatted last week start date
        data = isoLastWeekStartDate + ';' + endDate;

    } else if (selectedValue === 'Last month') {
        startDate.setMonth(currentDate.getMonth() - 1);
        const isoLastMonthStartDate = startDate.toISOString().split('T')[0]; // ISO formatted last month start date
        data = isoLastMonthStartDate + ';' + endDate;

    } else {
        const dateRange = selectedValue;
        const formattedDateRange = dateRange.replace(/ - /g, ';').replace(/\//g, '-');
        const parts = formattedDateRange.split(';');
        const startDateParts = parts[0].split('-');
        const endDateParts = parts[1].split('-');
        const isoStartDate = `${startDateParts[2]}-${startDateParts[0]}-${startDateParts[1]}`;
        const isoEndDate = `${endDateParts[2]}-${endDateParts[0]}-${endDateParts[1]}`;
        data = `${isoStartDate};${isoEndDate}`;
    }

    table.column(8).search(data).draw();
}


let table;

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


        });

    function format(d) {
        const createdDate = new Date(d.createdDate).toLocaleString(); // Format createdDate
        const lastUpdatedDate = new Date(d.lastUpdatedDate).toLocaleString(); // Format lastUpdatedDate

        return (
            'Created Date: ' +
            createdDate +
            '<br>' +
            'Updated Date: ' +
            lastUpdatedDate +
            '<br>' +
            'Note: ' +
            d.note +
            '<br>'
        );
    }


    table = $('table#table').DataTable({

        ajax: '/get-all-user',
        columns: [
            {
                target: 0,
                class: 'dt-control',
                orderable: false,
                searchable: false,
                data: null,
                defaultContent: '',

            },
            {data: "name", targets: 1},
            {data: "email", targets: 2},
            {data: "phone", targets: 3},
            {data: "address", targets: 4},
            {data: "department.name", targets: 5},
            {

                data: "role",
                targets: 6, // Adjust the target index to match the column
                render: function (data, type, row) {
                    const formattedRole = data
                        .split('_')
                        .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
                        .join(' ');

                    return formattedRole;
                }
            },
            {
                targets: 7,
                data: "email",
                data: "accountStatus",

                render: function (data, type, row) {

                    const buttonText = row.accountStatus ? "Suspend" : "Activate";
                    const buttonLink = row.accountStatus ? "suspend" : "activate";
                    if (userRole === 'SENIOR_HR' && row.role === 'DEFAULT_HR') {
                        return `<p class="text-center">-</p>`;
                    } else if (userRole === 'DEFAULT_HR' && row.role === 'DEFAULT_HR') {

                        return `<div class="dropdown" id="user-detail-btn">
                    <button class="btn border-0 text-dark bg-transparent drop" type="button" data-bs-toggle="dropdown">
                        <i class="bi bi-three-dots-vertical"></i>
                    </button>
                    <div class="dropdown-menu"> 
                        <a class="dropdown-item" href="/user-profile-edit?email=${encodeURIComponent(row.email)}">Profile</a>
                        
<!--                            <a class="dropdown-item" href="#" data-action="${buttonLink}" data-id="${row.id}">${buttonText}</a>-->
                        
                    </div>
                    </div>`;

                    } else {
                        return `<div class="dropdown" id="user-detail-btn">
                    <button class="btn border-0 text-dark bg-transparent drop" type="button" data-bs-toggle="dropdown">
                        <i class="bi bi-three-dots-vertical"></i>
                    </button>
                    <div class="dropdown-menu"> 
                        <a class="dropdown-item" href="/user-profile-edit?email=${encodeURIComponent(row.email)}">Profile</a>
                        
                            <a class="dropdown-item" href="#" data-action="${buttonLink}" data-id="${row.id}">${buttonText}</a>
                        
                    </div>
                    </div>`;
                    }
                },
                    sortable: false
                },
            {
                target: 8,
                data: "createdDate",
                visible: false
            },


        ],
        serverSide: true,
        processing: true
        // stateSave: true
    });


    // $('.dropdown-item.filter-items.department-dropdown-item ul li.filter-items').on('click', function() {
    //     // Get the text of the selected department
    //     const selectedDepartment = $(this).text().trim();
    //     console.log(selectedDepartment + 'aaa');
    //     // table.column(5).search(selectedDepartment).draw();


    // });


    // $('select#departmentSelector').change(function () {
    //     const selectedOption = $('select#departmentSelector option:selected').text();
    //     let filter = '';

    //     if (selectedOption !== 'All') {
    //         filter = selectedOption;
    //     }

    //     table.column(5).search(filter).draw();


    // });


    // Create reset filter button
    let resetFilterButton = `
<div id="reset-filter" class="mt-3 col-1 text-center">
    <span class="d-inline-block bg-transparent mt-2 reset-filter"
    onclick="resetFilters()">
        <i class="bi bi-arrow-clockwise" data-bs-toggle="tooltip" data-bs-placement="right" title="Reset filter"></i>
    </span>
</div>
`;

    // Create and append the custom filter inputs and button
    let customFilterHtml = `
<div id="custom-filter" class="mt-3 col-1 text-center">
    <span class="d-inline-block bg-transparent mt-2 add-filter dropdown" data-bs-toggle="dropdown">
        <i class="bi bi-plus-square-dotted" data-bs-toggle="tooltip"
     data-bs-placement="right" title="Add filter"></i>
    </span>
    <ul class="dropdown-menu filter-dropdown rounded-3 glass-transparent text-primary shadow-lg">
        <li class="dropdown-item filter-items date-created-dropdown-item">
            <span class="date-posted">Created Date</span>
            <ul class="dropdown-menu dropdown-submenu dateCreatedDropdown" id="date-created-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="createDateCreatedFilterButton($(this));checkAndToggleFilterButton();">Last 24 hours</li>
                <li class="dropdown-item filter-items" onclick="createDateCreatedFilterButton($(this));checkAndToggleFilterButton();">Last week</li>
                <li class="dropdown-item filter-items" onclick="createDateCreatedFilterButton($(this));checkAndToggleFilterButton();">Last month</li>
                <li class="dropdown-item filter-items">
                    <input type="text" class="px-2 rounded datefilter" name="datefilter" value="" placeholder="Custom" />
                </li>
            </ul>
        </li>
        <li class="dropdown-item filter-items department-dropdown-item">
            <span>Department</span>
            <ul class="dropdown-menu dropdown-submenu scrollable-submenu" id="department-dropdown-submenu">
            </ul>
        </li>
        <li class="dropdown-item filter-items status-dropdown-item">
            <span>Status</span>
            <ul class="dropdown-menu dropdown-submenu" id="status-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="createStatusFilterButton($(this));checkAndToggleFilterButton();">Active</li>
                <li class="dropdown-item filter-items" onclick="createStatusFilterButton($(this));checkAndToggleFilterButton();">Suspend</li>
            </ul>
        </li>
        <li class="dropdown-item filter-items role-dropdown-item">
            <span>Role</span>
            <ul class="dropdown-menu dropdown-submenu" id="role-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="createRoleFilterButton($(this));checkAndToggleFilterButton();">Default HR</li>
                <li class="dropdown-item filter-items" onclick="createRoleFilterButton($(this));checkAndToggleFilterButton();">Senior HR</li>
                <li class="dropdown-item filter-items" onclick="createRoleFilterButton($(this));checkAndToggleFilterButton();">Junior HR</li>
                <li class="dropdown-item filter-items" onclick="createRoleFilterButton($(this));checkAndToggleFilterButton();">Management</li>
                <li class="dropdown-item filter-items" onclick="createRoleFilterButton($(this));checkAndToggleFilterButton();">Interviewer</li>
                <li class="dropdown-item filter-items" onclick="createRoleFilterButton($(this));checkAndToggleFilterButton();">Other</li>
            </ul>
        </li>
    </ul>
</div>
`;

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

        console.log($('#date-posted-dropdown-submenu'));

        // Handle apply event to update the input value and set start and end times
        $('input[name="datefilter"]').on('apply.daterangepicker', function (ev, picker) {
            const startDate = picker.startDate.format('MM/DD/YYYY');
            const endDate = picker.endDate.format('MM/DD/YYYY');

            $(this).val(startDate + ' - ' + endDate);

            // Set the start and end times in your input fields
            createDateCreatedFilterButton('Custom', startDate, endDate);
            checkAndToggleFilterButton();
        });

        // Handle cancel event to clear the input value and reset start and end times
        $('input[name="datefilter"]').on('cancel.daterangepicker', function (ev, picker) {
            $(this).val('');
            $('#filter-start-time').val('');
            $('#filter-end-time').val('');
        });

        $('.daterangepicker').hover(function () {
            $('#date-created-dropdown-submenu').css('display', 'block');
        });

        $('.daterangepicker th').each(function () {
            console.log("TH:", $(this))
            $(this).on('click', function (event) {
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

    // Prevent the dropdown from closing when date inputs are clicked
    $('.startTimePicker, .endTimePicker').on('click', function (e) {
        e.stopPropagation(); // Stop the event from propagating
        $(this).closest('.dropdown-submenu').dropdown('toggle'); // Show the dropdown
    });

    //////////////////////////////////////////////

    function showConfirmationModal(action, id) {
        const confirmationModal = `
    <div class="modal fade" id="confirmationModal" tabindex="-1" data-bs-backdrop="static" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body text center" style="background: #0c233d">
                    <div class="loader"></div>
                    <div class="loader-txt text-center">
                        <h3 class="text-white">Are you sure to change Candidate Account Status?</h3>
                        
                        <div>
                            <button id="confirmYes" type="button" class="btn btn-sm btn-light-danger mx-1">Yes</button>
                            <button id="confirmCancel" class="btn btn-sm btn-light mx-1" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>`;
        $(document.body).append(confirmationModal); // Append the modal to the document

        function showProcessingModal() {
            const processingMessage = `
        <div class="modal-body text-center" style="background: #0c233d">
            <div class="loader"></div>
            <div class="loader-txt"><p class="text-white">Processing...</p></div>
        </div>`;

            $('.modal-content .modal-body').replaceWith(processingMessage);
        }

        function showSuccessModal() {
            const successMessage = `
        <div class="modal-body text-center" style="background: #0c233d">
            <div class="loader"></div>
            <div class="loader-txt"><p class="text-white">Process successful!</p>
                <div class="text-center">
                    <button id="close" class="btn btn-sm btn-light" data-bs-dismiss="modal">Close
                        </button>
                    </div>
                </div>
        </div>`;

            $('.modal-content .modal-body').replaceWith(successMessage);
        }


        function resetModal() {
            const reset = `<div class="modal-body text center" style="background: #0c233d">
                    <div class="loader"></div>
                    <div class="loader-txt text-center">
                        <h3 class="text-white">Are you sure to edit this Account?</h3>

                        <div>
                            <button id="confirmYes" type="button" class="btn btn-sm btn-light-danger mx-1">Yes</button>
                            <button id="confirmCancel" class="btn btn-sm btn-light mx-1" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>`
            $('.modal-content .modal-body').replaceWith(reset);
        }

        $(document).on("click", "#close", function () {

            resetModal();
        });

        $(document).on("click", "#confirmYes", function () {
            showProcessingModal();

            fetch('/' + action + '?id=' + encodeURIComponent(id), {
                method: "POST", // Or "PUT", depending on your API design
                headers: {
                    [csrfHeader]: csrfToken
                }
            })
                .then(response => {
                    // Handle success, for example, reload the DataTable
                    table.ajax.reload();
                    showSuccessModal();
                })
                .catch(error => {
                    // Handle errors
                });

            // Close the modal after triggering the API call
            // $('#confirmationModal').modal('hide');
        });

        // Show the modal when the "Suspend" or "Activate" button is clicked
        $('#confirmationModal').modal('show');
    }

    $(document).on("click", ".dropdown-item[data-action]", function (event) {
        event.preventDefault();
        const action = $(this).data("action");
        const id = $(this).data("id");

        showConfirmationModal(action, id);
    });


    // Attach event listener for the dropdown toggle
    $(document).on('click', '.drop', function () {
        const dropdown = $(this).closest('.dropdown');
        const dropdownMenu = dropdown.find('.dropdown-menu');

        // Calculate the position of the dropdown menu
        const toggleOffset = $(this).offset();
        dropdownMenu.css({
            top: toggleOffset.top + $(this).outerHeight(),
            left: toggleOffset.left,
        });

        dropdownMenu.toggle(); // Toggle the visibility of the dropdown menu
    });


    // Array to track the ids of the details displayed rows
    const detailRows = [];

    $('#table tbody').on('click', 'tr td.dt-control', function () {
        const tr = $(this).closest('tr');
        const row = table.row(tr);
        const idx = detailRows.indexOf(tr.attr('id'));

        if (row.child.isShown()) {
            tr.removeClass('details');
            row.child.hide();

            // Remove from the 'open' array
            detailRows.splice(idx, 1);
        } else {
            tr.addClass('details');
            row.child(format(row.data())).show();

            // Add to the 'open' array
            if (idx === -1) {
                detailRows.push(tr.attr('id'));
            }
        }
    });

    // On each draw, loop over the `detailRows` array and show any child rows
    table.on('draw', function () {
        detailRows.forEach(function (id, i) {
            $('#' + id + ' td.dt-control').trigger('click');
        });
    });

})
    ;


/////////////////////////////////////


    let filterElements = [
        {name: 'date-created-dropdown-item', isRemove: false, filterId: 'filter-created-posted'},
        {name: 'department-dropdown-item', isRemove: false, filterId: 'filter-department'},
        {name: 'status-dropdown-item', isRemove: false, filterId: 'filter-status'},
        {name: 'role-dropdown-item', isRemove: false, filterId: 'filter-role'}
    ];

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

        $('#reset-filter').hide();
        $('#custom-filter').show();

        departmentFiltered('');
        statusFiltered('');
        roleFiltered('');
        createdDateFiltered('');


    }

// Update the text of the recent filter dropdown button when an option is selected
    function changeSelectedFilterName(item) {

        if (item) {
            let selectedValue = $(item).text(); // Get the selected value from the clicked item


            let button = $(item).closest('.btn-group').find('.recent-filter-dropdown-btn');

            let filterId = $(item).data('filter-id');

            console.log(filterId + ' AKZ');

            if (filterId === 'filter-department') {
                departmentFiltered(selectedValue);

            } else if (filterId === 'filter-date-posted') {
                if (selectedValue !== 'Custom') {
                    createdDateFiltered(selectedValue);
                } else {
                    const inputData = $('input[name="datefilter2"]').val();
                    createdDateFiltered(inputData);
                }


            } else if (filterId === 'filter-status') {
                statusFiltered(selectedValue);

            } else if (filterId === 'filter-role') {
                roleFiltered(selectedValue);
            }
            // Find and update the isRemove property in filterElements

            for (let i = 0; i < filterElements.length; i++) {
                console.log("Filter Element Name : ", filterElements[i].name)
                if (filterElements[i].filterId === filterId) {
                    $('#' + filterElements[i].filterId).val($.trim(selectedValue));
                    break; // Exit the loop once the element is found
                }
            }

            // if ($('input[name="datefilter2"]').length > 0) {
            if (selectedValue != 'Custom') {
                $('input[name="datefilter2"]').val('');
                button.text(selectedValue); // Update the text of the button
            } else {
                $('.date-posted-filter-btn').text('Custom');
            }
            // }

            // updateDataTable();
        }
    }

// Function to remove selected dropdown and new button
    $(document).on('click', '.selected-dropdown-remove-button', function () {
        let filterName = $(this).data('filter-name');
        console.log(filterName + 'CZe');


        if (filterName === 'date-created-dropdown-item') {
            createdDateFiltered('');
        } else if (filterName === 'department-dropdown-item') {
            departmentFiltered('');
        } else if (filterName === 'status-dropdown-item') {
            statusFiltered('');
        } else if (filterName === 'role-dropdown-item') {
            roleFiltered('');
        }

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

        $(this).closest('.btn-group').remove();
        checkAndToggleFilterButton();

        // Update data table
        // updateDataTable();
    });

    function showSelectedDropdownRemoveButton(button) {
        const removeButton = $(button).next('.selected-dropdown-remove-button');
        removeButton.show();
    }

    function createDateCreatedFilterButton(selectedValue, startDate, endDate) {

        filterElements[0].isRemove = true;
        $('.date-created-dropdown-item').hide();

        let selectedText = null;
        let data;
        if (selectedValue === 'Custom') {
            $('#filter-start-date').val(startDate);
            $('#filter-end-date').val(endDate);
            selectedText = selectedValue;
            data = startDate + ';' + endDate
            $('#filter-date-posted').val(selectedText);
        } else {
            selectedText = selectedValue.text();
            data = selectedValue.text();
            $('#filter-date-posted').val(selectedText);
        }
        createdDateFiltered(data);
        // Create a filter button with the selected filter item
        var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn date-posted-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedText}
            </button>
            <span class="bg-danger selected-dropdown-remove-button date-created-filter-remove" data-filter-name="date-created-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu dateCreatedDropdown" id="date-created-filter-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-date-posted">Last 24 hours</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-date-posted">Last week</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-date-posted">Last month</li>
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
                const selectedFilterItem = $('<li class="dropdown-item filter-items" data-filter-id="filter-date-posted">Custom</li>');

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

        // Update data table
        // updateDataTable();
    }

    function createDepartmentFilterButton(selectedValue) {

        filterElements[1].isRemove = true;
        $('.department-dropdown-item').hide();
        departmentFiltered(selectedValue);
        // $('#filter-department').val(selectedValue);

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
            $('#department-filter-dropdown-submenu .filter-items').attr('onclick', 'changeSelectedFilterName(this);');

            // Hide other remove buttons and show the recent-filter-dropdown-btn
            $('.selected-dropdown-remove-button').hide();
            $('.recent-filter-dropdown-btn').show();
        });

        // Update data table
        // updateDataTable();


    }

    function createRoleFilterButton(selectedValue) {

        filterElements[2].isRemove = true;
        $('.role-dropdown-item').hide();

        checkAndToggleFilterButton();

        var selectedText = selectedValue.text();
        roleFiltered(selectedText);
        // $('#filter-role').val(selectedText);

        // Create a filter button with the selected filter item
        var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn status-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedText}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="role-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu" id="role-filter-dropdown-submenu">
            <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-role">Default-HR</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-role">Senior-HR</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-role">Junior-HR</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-role">Management-HR</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-role">Interviewer</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-role">Other</li>
            </ul>
        </div>`;

        // Append the selectedDropdown to the appropriate container
        $('#recent-filter-dropdown-con').append(selectedDropdown);

        // Hide other remove buttons and show the recent-filter-dropdown-btn
        $('.selected-dropdown-remove-button').hide();
        $('.recent-filter-dropdown-btn').show();

        // Update data table
        // updateDataTable();
    }

    function createStatusFilterButton(selectedValue) {

        filterElements[3].isRemove = true;
        $('.status-dropdown-item').hide();

        checkAndToggleFilterButton();

        var selectedText = selectedValue.text();
        statusFiltered(selectedText);
        // $('#filter-status').val(selectedText);

        // Create a filter button with the selected filter item
        var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn status-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedText}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="status-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu" id="status-filter-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-status">Active</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-status">Suspend</li>
            </ul>
        </div>`;

        // Append the selectedDropdown to the appropriate container
        $('#recent-filter-dropdown-con').append(selectedDropdown);

        // Hide other remove buttons and show the recent-filter-dropdown-btn
        $('.selected-dropdown-remove-button').hide();
        $('.recent-filter-dropdown-btn').show();

        // Update data table
        // updateDataTable();
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
                    submenuHTML += `<li class="bg-dark"><b class="ps-2">${startingLetter}</b></li>`;
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

// Function to replace datefilter2 with the value from datefilter
    function replaceDateFilter2Value() {
        const dateFilterValue = $('input[name="datefilter"]').val();
        $('input[name="datefilter2"]').val(dateFilterValue);
        $('input[name="datefilter"]').val('')
    }