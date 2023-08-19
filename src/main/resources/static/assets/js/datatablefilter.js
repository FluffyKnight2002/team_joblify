let filterElements = [
    {name: 'date-posted-dropdown-item', isRemove: false},
    {name: 'position-dropdown-item', isRemove: false},
    {name: 'department-dropdown-item', isRemove: false},
    {name: 'job-type-dropdown-item', isRemove: false},
    {name: 'level-dropdown-item', isRemove: false},
    {name: 'salary-dropdown-item', isRemove: false},
    {name: 'applicants-dropdown-item', isRemove: false},
    {name: 'status-dropdown-item', isRemove: false}
];
$(document).ready(function () {

});

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
    }
    $('.selected-dropdown-remove-button').each(function () {
        $(this).closest('.btn-group').remove();
    });

    $('#reset-filter').hide();
    $('#custom-filter').show();
}

// Update the text of the recent filter dropdown button when an option is selected
function changeSelectedFilterName(item) {
    let selectedValue = $(item).text(); // Get the selected value from the clicked item
    let button = $(item).closest('.btn-group').find('.recent-filter-dropdown-btn');
    button.text(selectedValue); // Update the text of the button
}

// Function to remove selected dropdown and new button
$(document).on('click', '.selected-dropdown-remove-button', function () {
    let filterName = $(this).data('filter-name');

    // Find and update the isRemove property in filterElements
    for (let i = 0; i < filterElements.length; i++) {
        if (filterElements[i].name === filterName) {
            filterElements[i].isRemove = false;
            $('.' +filterElements[i].name).show();
            break; // Exit the loop once the element is found
        }
    }

    $(this).closest('.btn-group').remove();
    checkAndToggleFilterButton();
});

function showSelectedDropdownRemoveButton(button) {
    const removeButton = $(button).next('.selected-dropdown-remove-button');
    removeButton.show();
}

function createDatePostedFilterButton(selectedValue) {

    filterElements[0].isRemove = true;
    $('.date-posted-dropdown-item').hide();
    var selectedText = selectedValue.text();

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn date-posted-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedText}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="date-posted-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu datePostedDropdown" id="date-posted-filter-dropdown-submenu">
                <li class="dropdown-item filter-items recent-filter-items" onclick="changeSelectedFilterName(this);">Last 24 hours</li>
                <li class="dropdown-item filter-items recent-filter-items" onclick="changeSelectedFilterName(this);">Last week</li>
                <li class="dropdown-item filter-items recent-filter-items" onclick="changeSelectedFilterName(this);">Last month</li>
                <li class="dropdown-item">
                    <span>Custom</span>
                    <div class="dropdown-menu dropdown-submenu customDatePicker p-2 pe-4">
                        <label for="startTimePicker" class="mx-2">Start Date:</label>
                        <input type="date" id="startTimePicker" class="form-control mx-2">
                        <label for="endTimePicker" class="mx-2">End Date:</label>
                        <input type="date" id="endTimePicker" class="form-control mx-2">
                    </div>
                </li>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();
}

function createTitleFilterButton(selectedValue) {

    filterElements[1].isRemove = true;
    $('.position-dropdown-item').hide();

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
}

function createDepartmentFilterButton(selectedValue) {

    filterElements[2].isRemove = true;
    $('.department-dropdown-item').hide();

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
}

function createJopTypeFilterButton(selectedValue) {

    filterElements[3].isRemove = true;
    $('.job-type-dropdown-item').hide();

    checkAndToggleFilterButton();

    var selectedText = selectedValue.text();

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn position-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedText}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="job-type-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu" id="job-type-filter-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);">Full time</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);">Part time</li>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();
}

function createLevelFilterButton(selectedValue) {

    filterElements[4].isRemove = true;
    $('.level-dropdown-item').hide();

    checkAndToggleFilterButton();

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
                    <input class="form-check-input" type="checkbox" name="level" value="ENTRY_LEVEL" id="level-entry">
                    <label class="form-check-label" for="level-entry">
                        Entry level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="level" value="JUNIOR_LEVEL" id="level-junior">
                    <label class="form-check-label" for="level-junior">
                        Junior level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="level" value="MID_LEVEL" id="level-mid">
                    <label class="form-check-label" for="level-mid">
                        Mid level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="level" value="SENIOR_LEVEL" id="level-senior">
                    <label class="form-check-label" for="level-senior">
                        Senior level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="level" value="SUPERVISOR_LEVEL" id="level-supervisor">
                    <label class="form-check-label" for="level-supervisor">
                        Supervisor level
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="level" value="EXECUTIVE_LEVEL" id="level-executive">
                    <label class="form-check-label" for="level-executive">
                        Executive level
                    </label>
                </div>
                <div class="d-flex justify-content-end align-items-center py-2">
                    <span class="filter-items btn btn-sm btn-outline-primary rounded-pill px-2 py-1 me-3" style="font-size: 0.8rem">Confirm</span>
                </div>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();
}

function createSalaryFilterButton(selectedValue) {

    filterElements[5].isRemove = true;
    $('.salary-dropdown-item').hide();

    checkAndToggleFilterButton();

    var selectedText = selectedValue.text();

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn date-posted-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                Salary
            </button>
            <span class="bg-danger selected-dropdown-remove-button salary-filter-remove" data-filter-name="salary-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu p-2" id="salary-filter-dropdown-submenu">
                <div id="rangeBar" class="custom-slider"></div>
                <div class="d-flex justify-content-around text-center">
                    <span class="col-4 text-primary mt-3" style="font-size: 0.7rem">Min: <span id="sliderValue1" class="d-block">100000</span></span>
                    <span id="selected-salary-label" style="font-size: 0.7rem;max-height: 30px;margin-top: 10px" 
                    class="col-4 bg-primary text-white rounded-pill d-flex justify-content-center align-items-center">Confirm</span>
                    <span class="col-4 text-primary mt-3" style="font-size: 0.7rem">Max: <span id="sliderValue2" class="d-block">9000000</span></span>
                </div>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    activeRangeBar();

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();
}

function createApplicantsFilterButton(selectedValue) {

    filterElements[6].isRemove = true;
    $('.applicants-dropdown-item').hide();

    checkAndToggleFilterButton();

    var selectedText = selectedValue.text();

    // Create a filter button with the selected filter item
    var selectedDropdown = `
        <div class="btn-group mt-3 p-2 position-relative">
            <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
                recent-filter-dropdown-btn applicants-filter-btn"
                onmouseenter="showSelectedDropdownRemoveButton(this);"
                data-bs-toggle="dropdown" aria-expanded="false">
                ${selectedText}
            </button>
            <span class="bg-danger selected-dropdown-remove-button position-filter-remove" data-filter-name="applicants-dropdown-item">
                <i class="bi bi-x"></i>
            </span>
            <ul class="dropdown-menu dropdown-submenu" id="applicants-filter-dropdown-submenu">
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);">Over require</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);">Doesn't reach half</li>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();
}

function createStatusFilterButton(selectedValue) {

    filterElements[7].isRemove = true;
    $('.status-dropdown-item').hide();

    checkAndToggleFilterButton();

    var selectedText = selectedValue.text();

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
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);">Open</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);">Closed</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);">Expired</li>
            </ul>
        </div>`;

    // Append the selectedDropdown to the appropriate container
    $('#recent-filter-dropdown-con').append(selectedDropdown);

    // Hide other remove buttons and show the recent-filter-dropdown-btn
    $('.selected-dropdown-remove-button').hide();
    $('.recent-filter-dropdown-btn').show();
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
                submenuHTML += `<li class="bg-dark"><b class="ps-2">${startingLetter}</b></li>`;
                currentLetter = startingLetter;
            }
            submenuHTML += `
                <li class="dropdown-item filter-items"
                    onclick="createTitleFilterButton('${item.name}');checkAndToggleFilterButton();">
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
                submenuHTML += `<li class="bg-dark"><b class="ps-2">${startingLetter}</b></li>`;
                currentLetter = startingLetter;
            }
            submenuHTML += `<li class="dropdown-item filter-items" 
                onclick="createDepartmentFilterButton('${item.name}');checkAndToggleFilterButton();">
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

function activeRangeBar() {
    // Function to create a filter button end

    var rangeBar = document.getElementById('rangeBar');
    var sliderValue1 = document.getElementById('sliderValue1');
    var sliderValue2 = document.getElementById('sliderValue2');
    var tooltipsEnabled = false;

    noUiSlider.create(rangeBar, {
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
            ".noUi-connect { background: #007BFF !important; }",
            ".noUi-handle { background: #007BFF !important; }",
            ".noUi-tooltip { background: #007BFF !important; }",
        ],
    });

    rangeBar.noUiSlider.on('update', function (values, handle) {
        sliderValue1.innerText = values[0];
        sliderValue2.innerText = values[1];
    });

    // Enable tooltips when handle is pressed
    rangeBar.noUiSlider.on('start', function () {
        if (!tooltipsEnabled) {
            rangeBar.noUiSlider.updateOptions({
                tooltips: [true, true]
            });
            tooltipsEnabled = true;
        }
    });

    // Disable tooltips when handle is released
    rangeBar.noUiSlider.on('end', function () {
        if (tooltipsEnabled) {
            rangeBar.noUiSlider.updateOptions({
                tooltips: [false, false]
            });
            tooltipsEnabled = false;
        }
    });

    // Prevent dropdown-submenu from closing when interacting with the range slider
    $(rangeBar).on('click', function (event) {
        event.stopPropagation();
    });

    // Prevent dropdown-submenu from closing when interacting with the range slider values
    $('#sliderValue1, #sliderValue2').on('click', function (event) {
        event.stopPropagation();
    });
}

// Fetch and GenerateHTML end

// Add a delegated event listener to dynamically created .recent-filter-dropdown-btn elements
// $(document).on('click', '.position-filter-btn', function () {
//     const $submenu = $(this).closest('.position-filter-dropdown-submenu');
//     console.log("Clicked on position filter button");
//     // Clear the submenu content
//     $submenu.empty();
//
//     // Fetch the submenu items from a predefined array or data source
//     const submenuItems =async ()=> { await fetch('/titles')
//         .then(response => response.json());};
//
//     console.log(submenuItems);
//
//     submenuItems.forEach(item => {
//         const startingLetter = item.name[0].toUpperCase();
//         if (startingLetter !== currentLetter) {
//             $submenu.append(`<li class="bg-dark"><b class="ps-2">${startingLetter}</b></li>`);
//             currentLetter = startingLetter;
//         }
//         $submenu.addClass('scrollable-submenu');
//         $submenu.append(`<li class="dropdown-item filter-items">${item.name}</li>`);
//     });
// });

// // Add an event listener to the datedPostedDropdown filter option
// $('.datePostedDropdown .filter-items').on('click', function () {
//     var selectedValue = $(this).text();
//
//     var selectedDropdown = `
//     <div class="btn-group mt-3 p-2 position-relative">
//     <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3 recent-filter-dropdown-btn" data-bs-toggle="dropdown" aria-expanded="false">
//         ${selectedValue}
//     </button>
//     <span class="bg-danger selected-dropdown-remove-button posted-date-filter-remove
//         onmouseenter="showSelectedDropdownRemoveButton(this);"">
//             <i class="bi bi-x"></i>
//     </span>
//     <ul class="dropdown-menu glass-transparent">
//         <li><a class="dropdown-item filter-items dropdown-recent-items">Last 24 hours</a></li>
//         <li><a class="dropdown-item filter-items dropdown-recent-items">Last week</a></li>
//         <li><a class="dropdown-item filter-items dropdown-recent-items">Last month</a></li>
//         <li class="dropdown-item filter-items">
//             <span>Custom</span>
//                 <div class="dropdown-menu dropdown-submenu customDatePicker p-2 pe-4">
//                     <label for="startTimePicker" class="mx-2">Start Date:</label>
//                     <input type="date" id="startTimePicker" class="form-control mx-2">
//                     <label for="endTimePicker" class="mx-2">End Date:</label>
//                     <input type="date" id="endTimePicker" class="form-control mx-2">
//                 </div>
//         </li>
//     </ul>
// </div>`;

// Update the text of the recent filter dropdown button when an option is selected
// $(document).on('click', '.dropdown-recent-items', function () {
//     var selectedValue = $(this).text(); // Get the selected value from the clicked item
//     $(this).closest('.btn-group').find('.recent-filter-dropdown-btn').text(selectedValue); // Update the text of the button
// });
//
// // Function to remove selected dropdown and new button
// $(document).on('click', '.posted-date-filter-remove', function () {
//     $(this).closest('.btn-group').remove();
// });

// Append the selectedDropdown to the appropriate container
// $('.selected-dropdown-remove-button').hide();
// $('.recent-filter-dropdown-btn').show();
// $(selectedDropdown).appendTo('#recent-filter-dropdown-con');

// });
// async function fetchDepartmentAndPopulateSubmenus() {
//     try {
//         const $submenu = $('#department-dropdown-submenu');
//         console.log($('#department-dropdown-submenu'))
//         $submenu.append("<h6 class='text-center text-muted sub-title'>Loading...</h6>");
//
//         const response = await fetch('departments'); // Replace 'titles' with the actual URL
//         if (!response.ok) {
//             throw new Error('Failed to fetch data');
//         }
//
//         const fetchedData = await response.json();
//
//         // Sort the data alphabetically
//         const sortedData = fetchedData.sort();
//
//         // Populate the submenu
//         $submenu.empty(); // Clear previous content
//         let currentLetter = null;
//         sortedData.forEach(item => {
//             console.log("Processing Item  :" , item)
//             const startingLetter = item.name[0].toUpperCase();
//             if (startingLetter !== currentLetter) {
//                 $submenu.append(`<li class="bg-dark"><b class="ps-2">${startingLetter}</b></li>`);
//                 currentLetter = startingLetter;
//             }
//             $submenu.addClass('scrollable-submenu');
//             $submenu.append(`<li class="dropdown-item filter-items">${item.name}</li>`);
//         });
//     } catch (error) {
//         console.error('An error occurred:', error);
//     }
// }

// const departmentDropdown = document.querySelector('.department-dropdown-item');
// departmentDropdown.addEventListener('mouseenter', function () {
//     console.log("Hovering over Department dropdown item");
//     fetchDepartmentAndPopulateSubmenus();
// });

// Function to add filter
// Simulated data fetched from the backend

// const positionDropdown = document.querySelector('.position-dropdown-item');
// positionDropdown.addEventListener('mouseenter', function () {
//     console.log("Hovering over Position dropdown item");
//     fetchTitleAndPopulateSubmenus($('#position-dropdown-submenu'));
// });

// console.log("POsiton Dropdown " ,$('.positionDropdown .filter-items'))
// Add an event listener to the position-dropdown-submenu filter option

