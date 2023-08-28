$(document).ready(function() {
	$('#data').summernote({
		height: 'auto'
	});
	$('#data_1').summernote({height: 'auto'});

});

var table;
var currentId = new URLSearchParams(window.location.search);
var id = currentId.get("viId");
var dname = currentId.get(decodeURIComponent("name"));
var position1 = currentId.get("position");
var select = currentId.get("selection");
var interview1 = currentId.get("interview");
var postId = currentId.get("postId");
var ccMails = [];
var updatedString = null;
var concatenatedValue = null;
$(document).ready(function() {

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
			"ajax": '/allCandidate',
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
						return '<a  data-bs-toggle="modal" data-bs-target="#emailModal" data-modal-title="Interview Invert Mail" class="btn btn-outline-primary btn-sm btn-block">Send Invert Mail</a>';
					},
					sortable: false,
					visible: false

				},
				{

					data: "interviewStatus",
					targets: 8,
					render: function(data, type, row) {
						return '<select id="changeStatus">' +
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

				},
				{
					targets: 10,
					data: "email",
					render: function(data, type, row) {
						return '<a  data-bs-toggle="modal" data-bs-target="#offer-Email-Modal" data-modal-title="Job Offer Mail" class="btn btn-outline-primary btn-sm btn-block">Send Offer Mail</a>';
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
						if (type === 'display' || type === 'filter') {
							// Assuming data is in the format '2023-01-01T00:00:00'
							var dateParts = data.split('T')[0].split('-');
							return dateParts[2] + '-' + dateParts[1] + '-' + dateParts[0];
						}
						return data; // For other types, return the original data
					}
				},

			],
			order: [[2, 'desc']]
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
			console.log(id),
				fetch("/seeMore", {
					method: "POST",
					headers: {
						'Content-Type': 'application/json;charset=utf-8',
						[csrfHeader]: csrfToken
					},
					body: JSON.stringify(id)
				})
					.then(async response => {

						if (response.ok) {
							const data = await response.json();
							row.child(format(data)).show();

						}
						else {
							console.log("SomeOne is error");
						}
					})

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
	});
	//Filter  start
	const selectElement = $('#positionSelect');
	const selectELement1 = $('#post');
	const statusSelect = $('#Selection');
	const interview = $('#Interview');
	$.ajax({
		url: '/allPositions',
		type: 'GET',
		success: function(response) {
			$.each(response, function(index, position) {
				const option = $('<option>').val(position.name).text(position.name);
				selectElement.append(option);

				if (dname && position.name === dname) {
					selectElement.val(position.name);
				}

				if (position1 && position.name === position1) {

					option.prop('selected', true);
				}

			});
		},
		error: function(xhr, status, error) {
			console.log(status);
			console.error('Error fetching positions:', error);
		}
	});
	$.ajax({
		url: '/post',
		type: 'GET',
		success: function(response) {
			$.each(response, function(index, post) {
				const optionText = post.openDate + ' To ' + post.closeDate;
				const option = $('<option>', { value: post.id, text: optionText });

				selectELement1.append(option);

				if (id && post.id == id) {
					selectELement1.val(post.id);
				}

				if (post.id == postId) {

					option.prop('selected', true);
				}
			});
		},
		error: function(xhr, status, error) {
			console.log(status);
			console.error('Error fetching positions:', error);
		}
	});

	selectElement.on('change', function() {
		handleFilterChange(4, this.value, 'position');
	});

	statusSelect.on('change', function() {
		handleFilterChange(5, this.value, 'selection');
	});

	interview.on('change', function() {
		handleFilterChange(8, this.value, 'interview');
	});
	selectELement1.on('change', function() {
		handleFilterChange(1, this.value, 'postId');
	})

	if (id != null || dname != null) {
		console.log(id)
		table.column(1).search(id).draw();
		table.column(4).search(dname).draw();
		selectElement.on('change', function() {
			table.column(1).search("").draw();
			handleFilterChange(4, this.value, 'position');
		});
		statusSelect.on('change', function() {
			table.column(1).search("").draw();
			handleFilterChange(5, this.value, 'selection');
		});
		interview.on('change', function() {
			table.column(1).search("").draw();
			handleFilterChange(8, this.value, 'interview');
		});
		selectELement1.on('change', function() {
			table.column(1).search("").draw();
			handleFilterChange(1, this.value, 'postId');
		});
	}

	if (position1 || select || interview1 || postId) {

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
		filters.forEach(filter => {
			table.column(filter.column).search(filter.value);
		});

		table.draw();
	}
	function handleFilterChange(columnIndex, filterValue, idKey) {
		table.column(columnIndex).search(filterValue).draw();
		currentId.delete('viId');
		currentId.delete('name');
		currentId.set(idKey, filterValue);
		history.pushState(null, null, '?' + currentId.toString());
	}

	//Filter end	

	//CV dowload starst
	var downloadButton = document.querySelector('#download');

	downloadButton.addEventListener('click', async function() {
		var selectedIds = [];
		var checkboxe = document.querySelectorAll('.ck:checked');


		checkboxe.forEach(function(checkbox) {
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
				table.ajax.reload();
			} catch (error) {
				console.error('Fetch error:', error);

			}
		} else {
			console.log("Select at least one checkbox.");
		}
	});
	//CV download end

	//Pdf download Start
	$('#pdfDownload').on('click',function (){
		fetch('/all_candidates/pdf',{
			method:'POST',
			headers: {
				'Content-Type': 'application/json;charset=utf-8',
				[csrfHeader]: csrfToken

		}
	})	.then(response => {
			if (response.ok) {
				iziToast.success({
					title:'Success',
					position:'topCenter',
					message:'Success Download PDF',
				})
			} else {
				iziToast.error({
					title:'Error',
					position:'topCenter',
					message:'Not Download PDF',
				})

			}
		});
	})
	//Pdf Download End

	//excel download start
	$('#excelDownload').on('click',function (){
		fetch('/all_candidates/excel',{
			method:'POST',
			headers: {
				'Content-Type': 'application/json;charset=utf-8',
				[csrfHeader]: csrfToken

			}
		})	.then(response => {
			if (response.ok) {
				iziToast.success({
					title:'Success',
					position:'topCenter',
					message:'Success Download excel',
				})
			} else {
				iziToast.error({
					title:'Error',
					position:'topCenter',
					message:'Not Download excel',
				})

			}
		});
	})
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
	return '<div class="slider">' +
		'<div class="row">' +
		'<div class="col-md-2">' +
		'Full Name: ' + d.name +
		'</div>' +
		'</div>' +
		'<div class="row">' +
		'<div class="col-md-2">' +
		'Email: ' + d.email +
		'</div>' +
		'</div>' +
		'<div class="row">' +
		'<div class="col-md-2">' +
		'Interview type: ' + d.interviewType +
		'</div>' +
		'</div>' +
		'<div class="row">' +
		'<div class="col-md-2">' +
		'Interview stage: ' + d.interviewStage +
		'</div>' +
		'</div>' +
		'<div class="row">' +
		'<div class="col-md-2">' +
		'Level: ' + d.lvl +
		'</div>' +
		'</div>' +
		'</div>';



}

$('#table1 tbody').on('click', '.btn-outline-primary', function() {
	var modalTitle = $(this).data('modal-title');
	var row = table.row($(this).closest('tr')).data();

	$('#emailModal .modal-title').text(modalTitle);
	$('#emailModal .candidatEmail').val(row.email);
	$("#emailModal .userEmail").val(row.email);
	$("#emailModal #candidate-id").val(row.id);
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
				to: to.value,
				ccmail: ccMails,
				subject: subject.value,
				content: hiddenInput.value,
				date: date1.value,
				time: time.value,
				status: stage.value,
				type: type.value,
				canId: canid.value,


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