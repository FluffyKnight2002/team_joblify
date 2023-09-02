$(document).ready(function() {
	$('#data').summernote({
		height: 'auto'
	});
	$('#data_1').summernote({height: 'auto'});

});

let table;

var currentId = new URLSearchParams(window.location.search);
var id = currentId.get("viId");
var dname = currentId.get(decodeURIComponent("name"));
var position1 = currentId.get("position");
var select = currentId.get("selection");
var interview1 = currentId.get("interview");
var postId = currentId.get("postId");
var candidateId=currentId.get('candidateId');
var ccMails = [];
var updatedString = null;
var concatenatedValue = null;
let role;


$(document).ready( async function() {
	if(id!=null){
		$('#filter-vacancy-info-id').val(id);
		console.log('mmmmmmmmmmm',id)
	}else {
		$('#filter-vacancy-info-id').val("All");
	}

	console.log("FIlter Vadjaf", $('#filter-vacancy-info-id'))
	const isHidden = document.getElementById('second');
	const response = await fetch('/authenticated-user-data', {
		method: 'POST',
		headers: {
			[csrfHeader]: csrfToken
		}
	});

	if (response.ok) {
		const [userDetails, passwordMatches] = await response.json();
		role = await userDetails.user.role;

		if(role==='DEFAULT_HR' || role==='SENIOR_HR'){
			document.getElementById('second').hidden = false;
		}else{
			console.log(isHidden)
			document.getElementById('second').hidden = true;

		}
	}



	$('#emailModal').on('show.bs.modal', function() {
		// Reset specific input fields, selects, and textarea inside the form
		$('#send-mail').find('input[type="date"], input[type="time"], select, textarea').val('');
		$('#data').summernote('reset');
		// Call getEmailContent with appropriate values for type and name
		var emailContent = '';
		getEmailContent(emailContent, 'John Doe'); // Example values
		// Do something with emailContent, like updating the modal content
	});


	table = $('#table1').DataTable(
		{
			"serverSide": true,
			"processing": true,
			"scrollY": 410,
			"scrollX": true,
			"scrollCollapse": true,
			"fixedHeader": {
				"header": true,
			},
			"ajax": {

				url:'/allCandidate',
				type:'GET',
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
				},
			},
			"sScrollY": "auto",

			// "bScrollCollapse": true,

			"columns": [

				{
					targets: 0,
					data: "id",
					render: function(data, type, row) { return '<input type="checkbox" class="ck" value="' + data + '">'; },
					sortable: false
				},
				{
					processing: false,
					target: 1,
					className: 'dt-control',
					orderable: false,
					data: "viId",
					render: function(data) {
						return "";
					}

				},
				{
					data: "id",
					targets: 2,
					visible: false
				},
				{
					data: "name",
					targets: 3
				},
				{
					targets: 4,
					data: "position",


				},

				{
					targets: 5,
					data: "selectionStatus",

				},
				{
					targets: 6,
					data: 'phone',



				},
				{
					targets: 7,
					data: "email",
					render: function(data, type, row) {
						return '<a id="stage" data-bs-toggle="modal" data-bs-target="#emailModal" data-modal-title="Interview Invert Mail" class="btn btn-outline-primary btn-sm btn-block">Send Invert Mail</a>';
					},
					sortable: false,
					visible: false

				},
				{
					className:"display",
					data: "interviewStatus",
					targets: 8,
					render: function(data, type, row) {
						return '<select class="form-select" style="font-size: 0.8rem" id="changeStatus"' + (data === 'ACCEPTED' ? ' disabled' : '') + '>' +
							'<option value="NONE"' + (data === 'NONE' ? ' selected' : '') + ' >NONE</option>' +
							'<option value="PENDING"' + (data === 'PENDING' ? ' selected' : '') + '>PENDING</option>' +
							'<option value="PASSED"' + (data === 'PASSED' ? ' selected' : '') + '>PASSED</option>' +
							'<option value="CANCEL"' + (data === 'CANCEL' ? ' selected' : '') + '>CANCEL</option>' +
							'<option value="ACCEPTED"' + (data === 'ACCEPTED' ? ' selected' : '') + '>ACCEPTED</option>' +
							'</select>';
					},
					sortable: false,

				},
				{
					targets: 9,
					data: 'lvl',
					render: function(data, type, row) {
						return reconvertToString(row.lvl);
					}

				},
				{
					targets: 10,
					data: "email",
					render: function(data, type, row) {
						return '<a  data-bs-toggle="modal" data-bs-target="#offer-Email-Modal" data-modal-title="Job Offer Mail"' +
							'style="font-size: 0.8rem" class="btn btn-outline-primary btn-sm btn-block">Send Offer Mail</a>';
					},
					sortable: false,
					visible: false

				},
				{
					targets: 11,
					data: 'experience',
				}, {
					targets: 12,
					data: 'date',
					render: function(data, type, row) {
						// if (type === 'display' || type === 'filter') {
						// 	// Assuming data is in the format '2023-01-01T00:00:00'
						// 	var dateParts = data.split('T')[0].split('-');
						// 	return dateParts[2] + '-' + dateParts[1] + '-' + dateParts[0];
						// }

						return changeTimeFormat(data); // For other types, return the original data
					}
				},

			],
			order: [[2, 'desc']]
		});
f
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
	let searchRow = $('#table1_filter').closest('.row');
	$('.dt-row').css('margin-bottom','40px')
	let recentFilterDropdownCon = `<div class="col-8" id="recent-filter-dropdown-con"></div>`;
	let reportButtonCon =
		`<div class="col-auto pt-2" id="report-button-con">
			<div class="row">
				<div class="col-4">
					<div class="text-center" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Download CV">
            			<button id="download" class="image-button" style="transform: translate(-7%,77%)" aria-label="Download CV"></button>
					</div>
            		</div>
            		<div class="col-8 p-0 bg-primary rounded px-1">
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
                				href="/all_candidates/excel"></a>
                			</div>
                		</div>
                		<div class="text-center row">
                			<div class="form-check form-switch">
                                <label class="form-check-label text-light" for="withFiler" style="font-size: 0.8rem">Including filter</label>
                                <input class="form-check-input" type="checkbox" name="withFilter" id="withFilter" style="font-size: 0.7rem;transform: translate(5px,5px);
}">
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


	// Assuming you have initialized DataTable properly

	$('#home-tab').on('click', function() {
		table.columns([6, 9]).visible(true);
		table.columns([7, 10]).visible(false);
	});

	$('#profile-tab').on('click', function() {
		table.columns([6, 9]).visible(false);
		table.columns([7, 10]).visible(true);
	});

	table.on('click', '#stage', async function (e) {
		let tr = e.target.closest('tr');
		let row = table.row(tr);
		var rowData = row.data();
		const data = await seeMoreFetch(rowData.id);
		console.log(data.interviewStage)
		const interviewStageSelect = document.getElementById('interview-stage-select');
		for (const value of data.interviewStage) {
			if (value === 'FIRST') {
				console.log(data.interviewStage)
				interviewStageSelect.options[0].disabled = true;
				interviewStageSelect.options[1].disabled = false;
			} else if (value === 'SECOND') {
				console.log(data.interviewStage)
				interviewStageSelect.options[1].disabled = true;
			}else{
				interviewStageSelect.options[0].disabled = false;
				interviewStageSelect.options[1].disabled = false;
			}
		}

	});
	if (position1 || select || interview1 || postId || candidateId) {

		let filters = [];

		if (position1) {
			filters.push({ column: 4, value: position1 });
		}
		if (select) {
			statusSelect.val(select);
			filters.push({ column: 5, value: select });
		}
		if (interview1) {
			interview.val(interview1);
			filters.push({ column: 8, value: interview1 });
		}
		if (postId) {
			interview.val(postId);
			filters.push({ column: 1, value: postId });
		}
		if(candidateId){
			filters.push({ column: 0, value: candidateId });
		}
		filters.forEach(filter => {
			table.column(filter.column).search(filter.value);
		});

		table.draw();
	}

	table.on('click', 'td.dt-control', async function(e) {
		let tr = e.target.closest('tr');
		let row = table.row(tr);
		var rowData = row.data();
		if (rowData.selectionStatus == 'RECEIVED') {
			fetch('/changStatus', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json;charset=utf-8',
					[csrfHeader]: csrfToken
				},
				body: JSON.stringify(rowData.id)
			})
				.then(response => {
					if (response.ok) {

						console.log("Change SelectStatus");

					}
					else {
						console.log("Error change SelectStatus");
					}
				})
		}

		if (row.child.isShown()) {
			if (rowData.selectionStatus == "RECEIVED") {

				rowData.selectionStatus = "VIEWED"
				row.child.hide();
				table.row(row).data(rowData)
				console.log(rowData.selectionStatus)
			}
			else {
				row.child.hide();
			}
		}
		else {
			var id = rowData.id;
			const response = await seeMoreFetch(id);

			if (response) {
				console.log('Hello', response.interviewStage);
				row.child(format(response)).show();
			} else {
				console.log("Someone is error");
			}
		}


	});

	var onDateBoundChange = function() {
		var first = $('input#minValue').val();
		var second = $('input#maxValue').val();

		table.column(12).search(first + ';' + second).draw();
	};
	$('input#minValue').on('input', onDateBoundChange);
	$('input#maxValue').on('input', onDateBoundChange);

	$('#filter_range').change(function() {
		var filterOption = $(this).find('option:selected').val();
		var currentDate = new Date();
		var endDate = currentDate.toISOString().split('T')[0]; // End date is today
		var startDate = new Date(currentDate);
		switch (filterOption) {
			case 'today':
				var first = currentDate.toISOString().split('T')[0];
				console.log(first) // Convert to ISO format (YYYY-MM-DD)
				table.column(12).search(first).draw();
				break;

			case 'last_week':
				// Copy current date to calculate the start date
				startDate.setDate(currentDate.getDate() - 7); // Subtract 7 days from current date
				var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
				console.log(isoStartDate); // Output: 2023-07-16 (example)
				console.log(endDate);
				// Perform action for 'last_week' option
				table.column(12).search(isoStartDate + ';' + endDate).draw();
				break;

			case 'past_1month':
				startDate.setMonth(currentDate.getMonth() - 1); // Subtract 1 month
				var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
				console.log(isoStartDate); // Output: 2023-07-16 (example)
				console.log(endDate); // Output: 2023-08-16 (example)

				// Perform action for 'last_month' option
				table.column(12).search(isoStartDate + ';' + endDate).draw();
				break;
			case 'past_6month':
				startDate.setMonth(currentDate.getMonth() - 6); // Subtract 1 month
				var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
				console.log(isoStartDate); // Output: 2023-07-16 (example)
				console.log(endDate); // Output: 2023-08-16 (example)

				// Perform action for 'last_month' option
				table.column(12).search(isoStartDate + ';' + endDate).draw();
				break;
			case 'past_year':
				var startDate = new Date(currentDate.getFullYear() - 1, 0, 1); // January 1 of the previous year
				var endDate = new Date(currentDate.getFullYear(), 0, 1); // January 1 of the year before the previous year
				var isoStartDate = startDate.toISOString().split('T')[0];
				var isoEndDate = endDate.toISOString().split('T')[0]; // Convert dates to ISO format
				console.log(isoStartDate);
				console.log(isoEndDate);

				// Perform action for 'past_year' option
				table.column(12).search(isoStartDate + ';' + isoEndDate).draw();
				break;
			case '':
				table.column(12).search('' + ';' + '').draw();
				break;
			default:
				return false;
		}

	})

	$('#selectAll').on(
		'change',
		function() {
			var checkboxes = $('#table1').find(
				':checkbox');
			checkboxes
				.prop('checked', this.checked);
			checkboxes.closest('tbody tr')
				.toggleClass('selected-row',
					this.checked);
		});

	$('#table1').on('change', ':checkbox',
		function() {
			var checkboxes = $('#table1')
				.find(':checkbox');
			var selectAllCheckbox = $('#selectAll');
			if (!this.checked) {
				selectAllCheckbox.prop(
					'checked', false);

			} else {
				selectAllCheckbox
					.prop(
						'checked',
						checkboxes.length === checkboxes
							.filter(':checked').length);
			}
			$(this).closest('tbody tr')
				.toggleClass(
					'selected-row',
					this.checked);

		});


	$('#table1 tbody').on('change', '#changeStatus', function() {
		var selectedValue = $(this).val();
		var tr = $(this).closest('tr');
		var row = table.row(tr).data();
		console.log(selectedValue)
		if(selectedValue==='ACCEPTED'){
			$('#confirmationModal').modal('show'); // Show the modal
			$('#confirmationModal .modal-body').html('Are you sure you want to proceed with ' + row.name + '?');
			$('#confirmActionBtn').on('click', function() {

				performAction(row.id, selectedValue);
				$('#confirmationModal').modal('hide');
				tr.find('td').find('select[id="changeStatus"]').prop('disabled',true);
			});

		}else{
			fetch('/changeInterview?id=' + row.id + '&status=' + selectedValue, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json;charset=utf-8',
					[csrfHeader]: csrfToken
				}
			})
				.then(response => {
					if (response.ok) {
						iziToast.success({
							title:'Success',
							position:'topCenter',
							message:'Success Change Stataus',
						})
					} else {
						iziToast.error({
							title:'Error',
							position:'topCenter',
							message:'Not Change Stataus',
						})

					}
				});
		}
	})




	//Filter  start
	// const selectElement = $('#positionSelect');
	// const selectELement1 = $('#post');
	// const statusSelect = $('#Selection');
	// const interview = $('#Interview');
	// $.ajax({
	// 	url: '/allPositions',
	// 	type: 'GET',
	// 	success: function(response) {
	// 		$.each(response, function(index, position) {
	// 			const option = $('<option>').val(position.name).text(position.name);
	// 			selectElement.append(option);
	//
	// 			if (dname && position.name === dname) {
	// 				selectElement.val(position.name);
	// 			}
	//
	// 			if (position1 && position.name === position1) {
	//
	// 				option.prop('selected', true);
	// 			}
	//
	// 		});
	// 	},
	// 	error: function(xhr, status, error) {
	// 		console.log(status);
	// 		console.error('Error fetching positions:', error);
	// 	}
	// });
	// $.ajax({
	// 	url: '/post',
	// 	type: 'GET',
	// 	success: function(response) {
	// 		$.each(response, function(index, post) {
	// 			const optionText = post.openDate + ' To ' + post.closeDate;
	// 			const option = $('<option>', { value: post.id, text: optionText });
	//
	// 			selectELement1.append(option);
	//
	// 			if (id && post.id == id) {
	// 				selectELement1.val(post.id);
	// 			}
	//
	// 			if (post.id == postId) {
	//
	// 				option.prop('selected', true);
	// 			}
	// 		});
	// 	},
	// 	error: function(xhr, status, error) {
	// 		console.log(status);
	// 		console.error('Error fetching positions:', error);
	// 	}
	// });
	//
	// selectElement.on('change', function() {
	// 	handleFilterChange(4, this.value, 'position');
	// });
	//
	// statusSelect.on('change', function() {
	// 	handleFilterChange(5, this.value, 'selection');
	// });
	//
	// interview.on('change', function() {
	// 	handleFilterChange(8, this.value, 'interview');
	// });
	// selectELement1.on('change', function() {
	// 	handleFilterChange(1, this.value, 'postId');
	// })
	//
	// if (id != null || dname != null) {
	// 	console.log(id)
	// 	table.column(1).search(id).draw();
	// 	table.column(4).search(dname).draw();
	// 	selectElement.on('change', function() {
	// 		table.column(1).search("").draw();
	// 		handleFilterChange(4, this.value, 'position');
	// 	});
	// 	statusSelect.on('change', function() {
	// 		table.column(1).search("").draw();
	// 		handleFilterChange(5, this.value, 'selection');
	// 	});
	// 	interview.on('change', function() {
	// 		table.column(1).search("").draw();
	// 		handleFilterChange(8, this.value, 'interview');
	// 	});
	// 	selectELement1.on('change', function() {
	// 		table.column(1).search("").draw();
	// 		handleFilterChange(1, this.value, 'postId');
	// 	});
	// }
	//
	// if (position1 || select || interview1 || postId) {
	//
	// 	let filters = [];
	//
	// 	if (position1) {
	// 		filters.push({ column: 4, value: position1 });
	// 	}
	// 	if (select) {
	// 		statusSelect.val(select);
	// 		filters.push({ column: 5, value: select });
	// 	}
	// 	if (interview1) {
	// 		interview.val(interview1);
	// 		filters.push({ column: 8, value: interview1 });
	// 	}
	// 	if (postId) {
	// 		interview.val(postId);
	// 		filters.push({ column: 1, value: postId });
	// 	}
	// 	filters.forEach(filter => {
	// 		table.column(filter.column).search(filter.value);
	// 	});
	//
	// 	table.draw();
	// }
	// function handleFilterChange(columnIndex, filterValue, idKey) {
	// 	table.column(columnIndex).search(filterValue).draw();
	// 	currentId.delete('viId');
	// 	currentId.delete('name');
	// 	currentId.set(idKey, filterValue);
	// 	history.pushState(null, null, '?' + currentId.toString());
	// }

	//Filter end

	// CV dowload starst
	var downloadButton = document.querySelector('#download');

	downloadButton.addEventListener('click', async function() {
		var selectedIds = [];
		var checkbox = document.querySelectorAll('.ck:checked');


		checkbox.forEach(function(checkbox) {
			selectedIds.push(checkbox.value);
		});

		if (selectedIds.length > 0) {
			var downloadUrl = '/downloadFile?id=' + selectedIds.join(',');

			try {
				const response = await fetch(downloadUrl);
				if (!response.ok) {
					throw new Error('Network response was not ok.');
				}

				const blob = await response.blob();
				const filename = getFilenameFromResponseHeaders(response);

				var url = window.URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.style.display = 'none';
				a.href = url;
				a.download = filename;
				document.body.appendChild(a);
				a.click();
				$('#selectAll').prop('checked', false);
				$('.ck').prop('checked', false);
				table.ajax.reload();
			} catch (error) {
				console.error('Fetch error:', error);

			}
		} else {
			console.log("Select at least one checkbox.");
		}
	});
	// CV download end

	// //Pdf download Start
	// $('#pdfDownload').on('click',function (){
	// 	fetch('/all_candidates/pdf')
	// 		.then(response => {
	// 		if (response.ok) {
	// 			iziToast.success({
	// 				title:'Success',
	// 				position:'topCenter',
	// 				message:'Success Download PDF',
	// 			})
	// 		} else {
	// 			iziToast.error({
	// 				title:'Error',
	// 				position:'topCenter',
	// 				message:'Not Download PDF',
	// 			})
	//
	// 		}
	// 	});
	// })
	// //Pdf Download End
	//
	// //excel download start
	// $('#excelDownload').on('click',function (){
	// 	fetch('/all_candidates/excel',{
	// 		method:'POST',
	// 		headers: {
	// 			'Content-Type': 'application/json;charset=utf-8',
	// 			[csrfHeader]: csrfToken
	//
	// 		}
	// 	})	.then(response => {
	// 		if (response.ok) {
	// 			iziToast.success({
	// 				title:'Success',
	// 				position:'topCenter',
	// 				message:'Success Download excel',
	// 			})
	// 		} else {
	// 			iziToast.error({
	// 				title:'Error',
	// 				position:'topCenter',
	// 				message:'Not Download excel',
	// 			})
	//
	// 		}
	// 	});
	// })
	//excel download end

	$('.cc').keyup(function(data) {
		if (data.keyCode === 13) {
			var value = $(this).val();
			ccMails.push($(this).val());
			if (value.trim() !== "") {
				var id = Math.floor(Math.random() * 100);
				var CcMail = '<div class="bg-light rounded-pill row w-auto m-2" id="skill' +
					id +
					'">' +
					'<span class="default-font col-8 d-inline-block text-center" id="">' +
					value +
					"</span>" +
					'<span class="rounded-circle fs-4 col-4 d-inline-block d-flex justify-content-center align-content-center remove-skill" data-count="' +
					id +
					'" style="cursor: pointer;"><i class="bx bx-x"></i></span>' +
					"</div>";
				$(".Ccmail").append(CcMail);
				$(this).val("");

			}
		}
	})
	$(document).on("click", ".remove-skill", function() {
		var count = $(this).data("count");
		var valueToRemove = $("#skill" + count + " .default-font").text().trim();
		// Remove the element with the matching value from the array
		var indexToRemove = ccMails.indexOf(valueToRemove);
		if (indexToRemove !== -1) {
			ccMails.splice(indexToRemove, 1);
		}

		$("#skill" + count).remove();
		updateCcMails();
		console.log("ConcateValue:",concatenatedValue)
		console.log("Removed count:", count);
		console.log("Value to remove:", valueToRemove);
		console.log("Updated ccMails array:", ccMails);
	});



});

function updateCcMails() {

	ccMails.forEach(function() {
		concatenatedValue = ccMails.join(',');
		$('#mails').val(concatenatedValue);
		console.log(concatenatedValue);
	})
}

//document ready end




function format(d) {
	return `
  <div class="slider">
    <div class="row">
      <div class="col-md-2">
        Full Name:
      </div>
      <div class="col-auto">
        ${d.name}
      </div>
    </div>
    <div class="row">
      <div class="col-md-2">
        Email:
      </div>
      <div class="col-auto">
        ${d.email}
      </div>
    </div>
    <div class="row">
      <div class="col-md-2">
        Interview type:
      </div>
      <div class="col-auto">
      	${d.interviewType}
      </div>
    </div>
    <div class="row">
      <div class="col-md-2">
        Interview stage: 
      </div>
      <div class="col-auto">
      	${d.interviewStage}
      </div>
    </div>
    <div class="row">
      <div class="col-md-2">
        Level:
      </div>
      <div class="col-auto">
      	${reconvertToString(d.lvl)}
      </div>
    </div>
    <div class="row">
     <div class="col-md-2">
        Language Skill: 
      </div>
      <div class="col-auto">
      	${d.languageSkill}
      </div>
    </div >
    <div class="row">
     <div class="col-md-2">
        Tech Skill: 
      </div>
      <div class="col-auto">
      	${d.techSkill}
      </div>
    </div>
  </div>`;



}

$('#table1 tbody').on('click', '.btn-outline-primary', function() {
	var modalTitle = $(this).data('modal-title');
	var row = table.row($(this).closest('tr')).data();

	$('#emailModal .modal-title').text(modalTitle);
	$('#emailModal .candidatEmail').val(row.email);
	$("#emailModal .userEmail").val(row.email);
	$("#emailModal #candidate-id").val(row.id);
	$("#emailModal #userName").val(row.name);
	$('#offer-Email-Modal #to_1').val(row.email);
	$('#offer-Email-Modal .vacancy_id').val(row.viId);


	$('#type').on('change',function(){
		const type=$(this).val();
		const content=getofferMail(type,row.name);
		$('#data_1').summernote('code',content);
	});

	$('#where').on('change', function() {
		const type = $(this).val();
		const updatedContent = getEmailContent(type, row.name,row.id);
		$('#data').summernote('code', updatedContent);
	});
	$('#add-date').on('click', function() {
		const edit = '<span style="color:red" class="date-setting">Date</span>'
		$('#data').summernote('pasteHTML', edit);
		// edit = '';

	})
	$('#add-time').on('click', function() {
		const edit = `<span Style='color:red' class='time-setting'>Start Time</span> to <span Style='color:red' class='end-setting'>End Time</span>`
		$('#data').summernote('pasteHTML', edit);
		// edit = '';

	})



	const fetchValueButton = document.getElementById('fetchValueButton');
	fetchValueButton.addEventListener('click', function() {
		const hiddenInput = document.getElementById('content');
		const to = document.getElementById('to');
		const subject = document.getElementById('subject');
		const ccmail = document.getElementById('mails');
		const date1 = document.getElementById('date');
		const time = document.getElementById('time');
		const type = document.getElementById('where');
		const stage = document.getElementById('interview-status');
		const canid=document.getElementById('candidate-id');
		const name=document.getElementById('userName');
		updateCcMails();
		if ($('#data').summernote('isEmpty')) {


			$('#message-con').html('' +
				'<div class="loader"></div>' +
				'<div class="loader-txt">' +
				'<h3 class="text-white">Email is Empty</h3>' +
				'<div>' +
				`<button class="btn btn-sm btn-light mx-1" onclick="closeModal()">OK</button></div>` +
				'</div>');
		}
		else {
			$('#data').summernote('insertText', '');
			hiddenInput.value = document.querySelector('#data').value;

			const data = {
				name:name.value,
				to: to.value,
				ccmail: ccMails,
				subject: subject.value,
				content: hiddenInput.value,
				date: date1.value,
				time: time.value,
				status: stage.value,
				type: type.value,
				canId: canid.value,
				position: position1

			};
			try {
				fetch('/send-invite-email', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json;charset=utf-8',
						[csrfHeader]: csrfToken // Include CSRF token as a request header
					},
					body: JSON.stringify(data)
				})
					.then(response => response.json())
					.then(data => {
						if (data === true) {
							console.log('Success send to mail');
							$('table#table1').DataTable().ajax.reload(null, false);
							$('#message-con').html('' +
								'<div class="loader"></div>' +
								'<div class="loader-txt">' +
								'<h3 class="text-white">Email was sent</h3>' +
								'<div>' +
								`<button class="btn btn-sm btn-light mx-1" onclick="closeModal()" >OK</button></div>` +
								'</div>');

						} else {
							$('#message-con').html('' +
								'<div class="loader"></div>' +
								'<div class="loader-txt">' +
								'<h3 class="text-white">Email was not sent</h3>' +
								'<h3 class="text-white">Something have error</h3>'+
								'<div>' +
								`<button class="btn btn-sm btn-light mx-1" onclick="closeModal()" >OK</button></div>` +
								'</div>');
							console.error('Failed to send email:', response.statusText);
						}
					});


			} catch (error) {
				console.error('An error occurred:', error);
			}
		}
	});
	const fetchofferMail=document.getElementById('Send_Offer_Mail');
	fetchofferMail.addEventListener('click',function (){
		const hiddenInput = document.getElementById('content_1');
		const to=document.getElementById('to_1');
		const subject=document.getElementById('subject_1');
		const ccmail=document.getElementById('mails_1');
		const canid = document.getElementById('candidate-id');
		const viId=document.getElementById('viId');
		console.log('<<<<<',viId.value)
		if ($('#data_1').summernote('isEmpty')) {


			$('#message-con').html('' +
				'<div class="loader"></div>' +
				'<div class="loader-txt">' +
				'<h3 class="text-white">Email is Empty</h3>' +
				'<div>' +
				`<button class="btn btn-sm btn-light mx-1" onclick="closeModal()">OK</button></div>` +
				'</div>');
		}
		else{
			$('#data_1').summernote('insertText', '');
			hiddenInput.value = document.querySelector('#data_1').value;

			/*	console.log('>>>>>>',date,'>>>>>>>',time,'<<<<<',row.viId)*/
			const data={
				to:to.value,
				subject:subject.value,
				ccmail:ccMails,
				vacancyId:viId.value,
				canId: canid.value,
				content:hiddenInput.value
			}
			fetch('/send-offer-mail',{
				method: 'POST',
				headers: {
					'Content-Type': 'application/json;charset=utf-8',
					[csrfHeader]: csrfToken // Include CSRF token as a request header
				},
				body: JSON.stringify(data)
			}).then(response =>response.json(data))
				.then(data => {
					if (data === true) {
						console.log('Success send to mail');
						$('table#table1').DataTable().ajax.reload(null, false);
						$('#message-con').html('' +
							'<div class="loader"></div>' +
							'<div class="loader-txt">' +
							'<h3 class="text-white">Email was sent</h3>' +
							'<div>' +
							`<button class="btn btn-sm btn-light mx-1" onclick="closeModal()" >OK</button></div>` +
							'</div>');

					} else {
						console.error('Failed to send email:', response.statusText);
					}
				});

		}

	})


});
$('#date').on('input', function() {
	const inputDate = $(this).val();
	const date = new Date(inputDate);
	const day = date.getDate();
	const month = date.toLocaleString('default', { month: 'long' });
	const year = date.getFullYear();
	const formattedDate = `${day}-${month}-${year}`;

	$('.date-setting').html(formattedDate);
});
$('#time').on('input', function() {
	var inputTime = $(this).val();
	var date = new Date();
	var timeParts = inputTime.split(':');
	date.setHours(parseInt(timeParts[0], 10));
	date.setMinutes(parseInt(timeParts[1], 10));

	// Format time with AM/PM
	var formattedTime = date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

	const endTime = new Date(date.getTime() + 30 * 60000); // 30 minutes in milliseconds
	const formattedEndTime = endTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
	// Update the content of the time-setting element
	$('.time-setting').html(formattedTime);
	$('.end-setting').html(formattedEndTime);

});

/*function updateDateSetting() {
	var selectedDate = $('#date').val();
	var emailContent = $('#emailModal #data').summernote('code');

	// Update the Date Setting section in the email content
	emailContent = emailContent.replace(/Date Setting\s+-.*\n/,
		'Date Setting    - ' + selectedDate + '\n');
console.log(emailContent)
	// Replace newline characters with HTML <br> tags
	//emailContent = emailContent.replace(/\n/g, '<br>');

	// Set the updated content back to the textarea
	$('#emailModal #data').summernote('code', emailContent);
}*/
// Function to get the CSRF token from the cookie









// Define the custom search function

function getFilenameFromResponseHeaders(response) {
	var contentDisposition = response.headers.get('content-disposition');
	return contentDisposition.split('filename=')[1].replace(/"/g, '');
}






const triggerTabList = document.querySelectorAll('#myTab button')
triggerTabList.forEach(triggerEl => {
	const tabTrigger = new bootstrap.Tab(triggerEl)

	triggerEl.addEventListener('click', event => {
		event.preventDefault()
		tabTrigger.show()
	})
})
function getofferMail(type,name){
	const custom='';
	var offermail = `
<b>Dear ${name}.</b></br>
 
We are pleased to inform you that you are appointed as Software Engineer in Banking and Finance Department of ACE Data Systems Ltd. Please see your entitlement information detail and brief HR rule of company in below.</br>

Entitlement Pay Information</br>

Joined Start Date           	– </br>

Position                         	– Software Engineer</br>

Basic Pay                      	– </br>

Project Allowance		– 	</br>

Meal + Transportation Allowance   – 3000 * (Working Days per Month) = Normally 60, 000 MMK</br>

Note for probation period:</br>
</br>
*After probation and if you are selected, you must work minimum 2 years contract at ACE</br>
*If you wish to resign in violation of within 2 years Agreement, you must give three months’ notice and your two months’ current net salary must be refunded.   </br>
*No leaves are entitled in the probation period.</br>
*Any leave must be informed in advance by phone.</br>
*You must inform us 1 month in advance to resign.</br>
*ACE will inform you 1 month in advance if we want to terminate.</br>
*You must learn all technical knowledge required by project(.NET, Java or any other technologies)</br>
</br>

 </br>
HR Rule of Company (Brief)</br>

(1)   Working Hour            	: From 9:00 AM To 06:00 PM</br>
                                                                          : From Monday to Friday (Except gazette holidays)</br>
(2)  Leave Entitlement (After probation period)

a.     Earn Leave                  	: 10 days per year after one year service history</br>
                                                                         (Need to apply one month in advance)</br>


b.     Casual Leave              	: 10 days per year after probation period </br>
                                                                        (Maximum 3 days per Month)</br>

c.     Medical Leave                : 30 days per year (With approved medical certification)</br>


(3)   Resignation after Employment Period     :      </br>    

a.     The employee shall give not less than three months written notice to the Employer for his/her resignation.</br>

b.     In case the employee for any reason, leave the services of the Company before the agreement period then employee shall pay 3 times of the current net salary.</br>

(4)   Confidentiality       </br>                                 :          

a.     The employee agrees not to disclose any of Company’s confidential information, Company’s trademarks or knowledge pertaining to the business of the Company during or after the employment.<br>

`
	return (type === 'offer_mail') ? offermail : custom;

}
function getEmailContent(type, name,id) {
	const custom = ''; // Add your custom message here

	const onlineText = `
        Dear ${name},<br>

        Welcome from ACE Data Systems Ltd. We would like to invite you for an interview at Zoom Meeting to get to know you in more detail on
         (<span Style='color:red' class='date-setting'>Date</span> ) <span Style='color:red' class='time-setting'>Start Time</span> to <span Style='color:red' class='end-setting'>End Time</span>.
        Please join using the Zoom link below. Thanks.<br>

        Note... We expect you to be in good condition during the interview with a stable internet connection and a quiet environment.<br>
        
        Join Zoom Meeting<br>
        <a href="https://zoom.us/j/92191528025?pwd=K1BMUzR4M0hQZDJqQm1DUWxsRTN3dz09">Zoom Meeting Link</a><br>
        Meeting ID: 921 9152 8025<br>
        Passcode: 178426<br>
 
    `;

	const offlineText = `
        Dear ${name},<br>

        Welcome from ACE Data Systems Ltd. We would like to invite you for an interview at our office on(<span Style='color:red' class='date-setting'>Date</span> ) <span Style='color:red' class='time-setting'>Start Time</span> to <span Style='color:red' class='end-setting'>End Time</span>.
        Our office is located at Building 18, 7th floor, MICT Park, Hlaing Township, Yangon, Myanmar. Thanks.<br>

        Note... We expect you to be in good condition during the interview with a stable internet connection and a quiet environment.<br>
    `;

	return (type === 'OFFLINE') ? offlineText : (type === 'ONLINE' ? onlineText : custom);
}

function closeModal() {
	let modal = $('#loadMe');
	if (modal.length) {
		modal.modal('hide');
	}
	let modalBackdrop = $('.modal-backdrop');
	if (modalBackdrop.length) {
		modalBackdrop.hide();
	}
}
function performAction(id, status) {
	fetch('/changeInterview?id=' + id + '&status=' + status, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json;charset=utf-8',
			[csrfHeader]: csrfToken
		}
	})
		.then(response => {
			if (response.ok) {
				iziToast.success({
					title: 'Success',
					position: 'topCenter',
					message: 'Success Change Status',
				});
			} else {
				iziToast.error({
					title: 'Error',
					position: 'topCenter',
					message: 'Not Change Status',
				});
			}
		});
}

async function seeMoreFetch(id){
	const response = await fetch("/seeMore?id="+id, {
		method: "POST",
		headers: {
			'Content-Type': 'application/json;charset=utf-8',
			[csrfHeader]: csrfToken
		},

	})

	if(response.ok){
		const data =await response.json();
		return data
	}else {
		throw new Error('Failed to fetch data'); // Throw an error in case of failure
	}
}
/*
document.addEventListener('DOMContentLoaded', function() {
	var downloadButton = document.querySelector('#download');

	downloadButton.addEventListener('click', function() {
		var selectedIds = [];
		var checkboxes = document.querySelectorAll('.ck:checked');

		checkboxes.forEach(function(checkbox) {
			selectedIds.push(checkbox.value);
		});

		if (selectedIds.length > 0) {
			var downloadUrl = '/downloadFile?id=' + selectedIds.join(',');
			window.location.href = downloadUrl;
		} else {
			console.log("No checkboxes are selected.");
		}

	});
});

*/

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