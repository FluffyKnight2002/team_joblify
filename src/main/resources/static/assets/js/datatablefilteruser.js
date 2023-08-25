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
    console.log("AnyIsRemove",anyIsRemove)

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
        $('.' +filterElements[i].name).show();
        $('#' +filterElements[i].filterId).val('');
    }
    $('.selected-dropdown-remove-button').each(function () {
        $(this).closest('.btn-group').remove();
    });

    $('#reset-filter').hide();
    $('#custom-filter').show();

    updateDataTable();
}

// Update the text of the recent filter dropdown button when an option is selected
function changeSelectedFilterName(item) {
    if (item) {
        let selectedValue = $(item).text(); // Get the selected value from the clicked item
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

        if ($('input[name="datefilter2"]').length > 0) {
            if(selectedValue != 'Custom') {
                $('input[name="datefilter2"]').val('');
                button.text(selectedValue); // Update the text of the button
            }else {
                $('.date-posted-filter-btn').text('Custom');
            }
        }

        // updateDataTable();
    }
}

// Function to remove selected dropdown and new button
$(document).on('click', '.selected-dropdown-remove-button', function () {
    let filterName = $(this).data('filter-name');

    // Find and update the isRemove property in filterElements
    for (let i = 0; i < filterElements.length; i++) {
        if (filterElements[i].name === filterName) {
            filterElements[i].isRemove = false;
            $('.' +filterElements[i].name).show();
            $('#'+filterElements[i].filterId).val('');
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

function createDateCreatedFilterButton(selectedValue,startDate,endDate) {

    filterElements[0].isRemove = true;
    $('.date-created-dropdown-item').hide();

    let selectedText = null;

    if(selectedValue === 'Custom') {
        $('#filter-start-date').val(startDate);
        $('#filter-end-date').val(endDate);
        selectedText = selectedValue;
        $('#filter-date-posted').val(selectedText);
    }else {
        selectedText =  selectedValue.text();
        $('#filter-date-posted').val(selectedText);
    }

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

    $(function() {
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
        $('input[name="datefilter2"]').on('apply.daterangepicker', function(ev, picker) {
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
        $('input[name="datefilter2"]').on('cancel.daterangepicker', function(ev, picker) {
            $(this).val('');
            $('#filter-start-time').val('');
            $('#filter-end-time').val('');
        });

        $('.daterangepicker').hover(function () {
            $('.datePostedDropdown').css('display', 'block');
        },function() {
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
            <ul class="dropdown-menu dropdown-submenu" id="status-filter-dropdown-submenu" style="top: -90px">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-status">Open</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-status">Closed</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-status">Expired</li>
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
            <ul class="dropdown-menu dropdown-submenu" id="status-filter-dropdown-submenu" style="top: -90px">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-status">Open</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-status">Closed</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-status">Expired</li>
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