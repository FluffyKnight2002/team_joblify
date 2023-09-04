let filterElements = [
    {name: 'apply-date-dropdown-item', isRemove: false, filterId: 'filter-apply-date'},
    {name: 'position-dropdown-item', isRemove: false, filterId: 'filter-title'},
    {name: 'department-dropdown-item', isRemove: false, filterId: 'filter-department'},
    {name: 'level-dropdown-item', isRemove: false, filterId: 'filter-level'},
    {name: 'selection-status-dropdown-item', isRemove: false, filterId: 'filter-selection-status'},
    {name: 'interview-status-dropdown-item', isRemove: false, filterId: 'filter-interview-status'}
];
let selectedLevels = [];

function updateDataTable() {
    $('#filter-vacancy-info-id').val("All");
    table.ajax.reload();
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
        console.log("Selected Value : " , selectedValue);
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
        if(selectedValue !== 'Custom') {
            $('input[name="datefilter2"]').val('');
            button.text($.trim(selectedValue)); // Update the text of the button
        }else {
            if($('.apply-date-fliter-btn').length > 0) {
                $('.apply-date-filter-btn').text('Custom');
            }else {
                $('.date-posted-filter-btn').text('Custom');
            }
        }
        // }

        updateDataTable();
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

    if(filterName === 'level-dropdown-item') {

        const selectedLevels = [];

        $('.level-checkbox').each(function () {

            let checkbox = $(this);
            const checkboxes2 = $('.level-filter-checkbox:checked');

            // Iterate through the checked checkboxes and collect their values
            checkboxes2.each(function () {
                selectedLevels.push($(this).val());
            });

            console.log("Selected Levels :",selectedLevels)
            console.log("It match : ", selectedLevels.includes(checkbox.val()));
            if (selectedLevels.includes(checkbox.val())) {
                console.log("checkbox change!!!!")
                checkbox.prop('checked', true).trigger('change');
            }
            console.log("level-checkbox.val() ", checkbox.val());
            console.log("checked : ", checkbox.is(":checked"));
        });
    }else if(filterName === 'salary-dropdown-item') {
        console.log("Value changed!!!")
        rangeBar1.noUiSlider.set([minValue, maxValue]);
    }

    $(this).closest('.btn-group').remove();
    checkAndToggleFilterButton();

    // Update data table
    updateDataTable();
});

function showSelectedDropdownRemoveButton(button) {
    const removeButton = $(button).next('.selected-dropdown-remove-button');
    removeButton.show();
}

function createDatePostedFilterButton(selectedValue,startDate,endDate) {

    filterElements[0].isRemove = true;
    $('.apply-date-dropdown-item').hide();

    let selectedText = null;

    if(selectedValue === 'Custom') {
        $('#filter-start-date').val(startDate);
        $('#filter-end-date').val(endDate);
        selectedText = selectedValue;
        $('#filter-apply-date').val(selectedText);
    }else {
        selectedText =  selectedValue.text();
        $('#filter-apply-date').val(selectedText);
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
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="apply-date-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu datePostedDropdown" id="date-posted-filter-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-apply-date">Today</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-apply-date">Last Week</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-apply-date">Last Month</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-apply-date">Last 6 Month</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-apply-date">Last Year</li>

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
            const selectedFilterItem = $('<li class="dropdown-item filter-items" data-filter-id="filter-apply-date">Custom</li>');

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
    updateDataTable();
}

function createTitleFilterButton(selectedValue) {

    filterElements[1].isRemove = true;
    $('.position-dropdown-item').hide();

    $('#filter-title').val(selectedValue);

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
    fetchTitleAndGenerateHTML().then(submenuHTML => {
        // Use the generated submenuHTML to populate the submenu
        $('#position-filter-dropdown-submenu').html(submenuHTML);

        // Remove the existing onclick attribute from recent-filter-items
        $('#position-filter-dropdown-submenu .filter-items').removeAttr('onclick');

        // Add the onclick attribute to every recent-filter-items
        $('#position-filter-dropdown-submenu .filter-items').attr('onclick', 'changeSelectedFilterName(this);');

        // Hide other remove buttons and show the recent-filter-dropdown-btn
        $('.selected-dropdown-remove-button').hide();
        $('.recent-filter-dropdown-btn').show();
    });

    // Update data table
    updateDataTable();
}

function createDepartmentFilterButton(selectedValue) {

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
        $('#department-filter-dropdown-submenu .filter-items').attr('onclick', 'changeSelectedFilterName(this);');

        // Hide other remove buttons and show the recent-filter-dropdown-btn
        $('.selected-dropdown-remove-button').hide();
        $('.recent-filter-dropdown-btn').show();
    });

    // Update data table
    updateDataTable();
}

function createLevelFilterButton(selectedValue) {

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn position-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                Level
            </button>
            <span class="bg-danger selected-dropdown-remove-button level-filter-remove" data-filter-name="level-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu ps-3" id="level-filter-dropdown-submenu">
                <div class="form-check">
                    <input class="form-check-input level-filter-checkbox" type="checkbox" name="level" value="ENTRY_LEVEL" id="level-entry">
                    <label class="form-check-label" for="level-entry">
                        Entry level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input level-filter-checkbox" type="checkbox" name="level" value="JUNIOR_LEVEL" id="level-junior">
                    <label class="form-check-label " for="level-junior">
                        Junior level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input level-filter-checkbox" type="checkbox" name="level" value="MID_LEVEL" id="level-mid">
                    <label class="form-check-label" for="level-mid">
                        Mid level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input level-filter-checkbox" type="checkbox" name="level" value="SENIOR_LEVEL" id="level-senior">
                    <label class="form-check-label" for="level-senior">
                        Senior level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input level-filter-checkbox" type="checkbox" name="level" value="SUPERVISOR_LEVEL" id="level-supervisor">
                    <label class="form-check-label" for="level-supervisor">
                        Supervisor level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input level-filter-checkbox" type="checkbox" name="level" value="EXECUTIVE_LEVEL" id="level-executive">
                    <label class="form-check-label" for="level-executive">
                        Executive level
                    </label>
                </div>
                <div class="d-flex justify-content-end align-items-center py-2">
                    <span class="filter-items btn btn-sm btn-outline-primary rounded-pill px-2 py-1 me-3" style="font-size: 0.8rem" 
                    onclick="updateFilterLevel(); updateDataTable();" data-filter-id="filter-level">Confirm</span>
                </div>
            </ul>
        </div>`;

    if($('.level-checkbox:checked').length > 0) {

        filterElements[3].isRemove = true;
        $('.level-dropdown-item').hide();

        checkAndToggleFilterButton();

        // Append the selectedDropdown to the appropriate container
        $('#recent-filter-dropdown-con').append(selectedDropdown);

        updateFilterLevel();

        // Hide other remove buttons and show the recent-filter-dropdown-btn
        $('.selected-dropdown-remove-button').hide();
        $('.recent-filter-dropdown-btn').show();

        // Update data table
        updateDataTable();
    }
}

function createSelectionStatusFilterButton(selectedValue) {

    filterElements[4].isRemove = true;
    $('.selection-status-dropdown-item').hide();

    checkAndToggleFilterButton();

    var selectedText = selectedValue.text();
    $('#filter-selection-status').val(selectedText);

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn status-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedText}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="selection-status-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu" id="status-filter-dropdown-submenu" style="top: -90px">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-selection-status">Received</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-selection-status">Considering</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-selection-status">Viewed</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-selection-status">Offered</li>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();

    // Update data table
    updateDataTable();
}

function createInterviewStatusFilterButton(selectedValue) {

    filterElements[5].isRemove = true;
    $('.interview-status-dropdown-item').hide();

    checkAndToggleFilterButton();

    var selectedText = selectedValue.text();
    $('#filter-interview-status').val(selectedText);

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn status-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedText}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="interview-status-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu" id="status-filter-dropdown-submenu" style="top: -90px">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-interview-status">None</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-interview-status">Pending</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-interview-status">Cancel</li>
                 <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-interview-status">Passed</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-interview-status">Accepted</li>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();
    // Update data table
    updateDataTable();
}

// Fetch and GenerateHTML start

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

function updateFilterLevel() {

    const selectedLevels = [];

    // Select all checkboxes with class 'level-checkbox' that are checked
    const checkboxes = $('.level-checkbox:checked');
    const checkboxes2 = $('.level-filter-checkbox:checked');

    // Iterate through the checked checkboxes and collect their values
    checkboxes.each(function () {
        selectedLevels.push($(this).val());
    });

    // If no checkboxes are checked in checkboxes, use checkboxes2
    if (selectedLevels.length === 0) {
        checkboxes2.each(function () {
            selectedLevels.push($(this).val());
        });
    }else {
        console.log("selectedLevels ", selectedLevels)
        $('.level-filter-checkbox').each(function () {
            var checkbox = $(this);
            console.log("level-filter-checkbox.val() ", checkbox.val());
            console.log("checked : ", checkbox.is(":checked"));
            if (selectedLevels.includes(checkbox.val())) {
                checkbox.prop('checked', true); // Check the checkbox
            }
        });

    }

    console.log("Selected Levels : ", selectedLevels);
    checkboxes.prop('checked', false); // Check the checkbox

    // Optionally, close the dropdown menu if needed
    // $('#level-dropdown-submenu').dropdown('hide');

    if (selectedLevels.length > 0) {
        $('#filter-level').val(selectedLevels.join(', '));
    } else {
        // Handle the case where no checkboxes are checked
        $('#filter-level').val(""); // Set to an empty string or any default value
    }
}

// Function to update the filter value and close the dropdown submenu
function updateFilterValueAndClose(rangeBar) {

    console.log("Range Bar", rangeBar)

    minValue = rangeBar.noUiSlider.get()[0];
    maxValue = rangeBar.noUiSlider.get()[1];
    const filterValue = minValue + ',' + maxValue;
    $('#filter-minAndMax').val(filterValue);

    console.log("Filter Min Max", $('#filter-minAndMax').val())
}

// Function to replace datefilter2 with the value from datefilter
function replaceDateFilter2Value() {
    const dateFilterValue = $('input[name="datefilter"]').val();
    $('input[name="datefilter2"]').val(dateFilterValue);
    $('input[name="datefilter"]').val('')
}

