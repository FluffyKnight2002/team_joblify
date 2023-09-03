var table;
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
                  targets:3,
                  data:'department'

                },
                {
                    targets: 4,
                    data: "totalCandidate",
                    render: function (data) {
                        let total = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-info bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return total;
                    },
                    sortable: false
                },
                {
                    targets: 5,
                    data: 'interviewedCounts',
                    render: function (data) {
                        let inter = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-dark bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return inter;
                    },
                    sortable: false

														
															
														},
														{
															targets : 6,createdCell: function (td) {
																$(td).css('background-color', "#D5F5E3")
															},
															data:"passedCandidate",
															 render:function(data)
														    {
																let passed=data == null ?'-':data;
																let passBtn = (passed=='-')  ?
																'<span>'+passed+'</span>':
																'<input type="submit" value="'+passed+'">';
																return passBtn;


															},
															sortable:false
														},
														{

															data : "pendingCandidate",
															targets : 7,
															createdCell: function (td) {
																$(td).css('background-color', "#D5F5E3")
															},
															 render:function(data)
														    {let pend=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return pend;},
														    sortable:false


														},
														{
															targets:8,
															data:'cancelCandidate',
															createdCell: function (td) {
																$(td).css('background-color', "#D5F5E3")
															},
															 render:function(data)
														    {let cancel=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return cancel;},
														    sortable:false
														},
														{
															targets : 9,
															data:"notInterviewCandidate",
															createdCell: function (td) {
																$(td).css('background-color', "#D5F5E3 ")
															},
															 render:function(data)
														    {let not=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return not;},
														    sortable:false
														},
														{
															targets:10,
															data:'acceptedCandidate',
															 render:function(data)
														    {let acc=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return acc;},
														    sortable:false
														},
												
												],
												order:[[0,'desc']]

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
                    <span class="date-posted">Apply Date</span>
                    <ul class="dropdown-menu dropdown-submenu datePostedDropdown" id="apply-date-dropdown-submenu">
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
                <li class="dropdown-item filter-items selection-status-dropdown-item">
                    <span>Selection Status</span>
                    <ul class="dropdown-menu dropdown-submenu" id="selection-status-dropdown-submenu" style="top: -85px">
                        <li class="dropdown-item filter-items" onclick="createSelectionStatusFilterButton($(this));checkAndToggleFilterButton();">Received</li>
                        <li class="dropdown-item filter-items" onclick="createSelectionStatusFilterButton($(this));checkAndToggleFilterButton();">Considering</li>
                        <li class="dropdown-item filter-items" onclick="createSelectionStatusFilterButton($(this));checkAndToggleFilterButton();">Viewed</li>
                        <li class="dropdown-item filter-items" onclick="createSelectionStatusFilterButton($(this));checkAndToggleFilterButton();">Offered</li>
                    </ul>
                </li>
                <li class="dropdown-item filter-items interview-status-dropdown-item">
                    <span>Interview Status</span>
                    <ul class="dropdown-menu dropdown-submenu" id="interview-status-dropdown-submenu" style="top: -125px">
                        <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">None</li>
                        <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">Pending</li>
                        <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">Cancel</li>
                        <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">Passed</li>
                    </ul>
                </li>
            </ul>
            </div>
        </div>
    `;

    console.log("Table" , $('#table1'))

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
    $('.dt-row').css('margin-bottom','40px')
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
                    			href="/all_candidates/pdf"></a>
                    		</div>
                    		<div class="col-6 text-center" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Report Excel">
                				<a id="excelDownload" class="image-button" aria-label="Download Excel"
                				href="/interview_process/excel"></a>
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
    $(function() {
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
            $('#apply-date-dropdown-submenu').css('display', 'block');
        });

        $('.daterangepicker th').each(function() {
            console.log("TH:",$(this))
            $(this).on('click', function(event) {
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

});

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
