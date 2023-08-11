$('.summernote').summernote({
	tabsize: 2,
	height: 110,
})

var table;
var currentId = new URLSearchParams(window.location.search);
var id = currentId.get("viId");
var dname=currentId.get(decodeURIComponent("name"));
var position1=currentId.get("position");
var select=currentId.get("selection");
var interview1=currentId.get("interview");

$(document).ready(function() {
	table = $('#table1').DataTable(
		{
			"serverSide": true,
			"processing": true,
			"ajax": '/allCandidate',


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
					targets: 2
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
					visible: true


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
					visible: true
				},
				{
					targets: 10,
					data: "email",
					render: function(data, type, row) {
						return '<a  data-bs-toggle="modal" data-bs-target="#emailModal" data-modal-title="Job Offer Mail" class="btn btn-outline-primary btn-sm btn-block">Send Offer Mail</a>';
					},
					sortable: false,
					visible: false
				},
				{
					targets: 11,
					data: 'experience',
				}

			],

		});

		
		const selectElement = $('#positionSelect');
		const statusSelect=$('#Selection');
		const interview=$('#Interview');
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
	//Filter  start
function handleFilterChange(columnIndex, filterValue, idKey) {
    table.column(columnIndex).search(filterValue).draw();
    currentId.delete('viId');
    currentId.delete('name');
    currentId.set(idKey, filterValue);
    history.pushState(null, null, '?' + currentId.toString());
}

selectElement.on('change', function() {
    handleFilterChange(4, this.value, 'position');
});

statusSelect.on('change', function() {
    handleFilterChange(5, this.value, 'selection');
});

interview.on('change', function() {
    handleFilterChange(8, this.value, 'interview');
});

if (id != null) {
    table.column(1).search(id).draw();
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
}

	if (position1 || select || interview1) {
	
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
		filters.forEach(filter => {
			table.column(filter.column).search(filter.value);
		});
	
		table.draw();
	}
	

	//Filter end

	function getCsrfToken() {
		const metaTag = document.querySelector('meta[name="_csrf"]');
		return metaTag ? metaTag.getAttribute('content') : null;
	}
	const csrfToken = getCsrfToken();
	//end 
	table.on('click', 'td.dt-control', async function(e) {
		let tr = e.target.closest('tr');
		let row = table.row(tr);
		var rowData = row.data();
		if (rowData.selectionStatus == 'RECEIVED') {
			console.log(rowData.selectionStatus)
			fetch('/changStatus', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json;charset=utf-8',
					'X-XSRF-Token': csrfToken
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
			row.child.hide();
			$('table#table1').DataTable().ajax.reload(null, false);
		}
		else {
			var id = rowData.id;
			console.log(id),
				fetch("/seeMore", {
					method: "POST",
					headers: {
						'Content-Type': 'application/json;charset=utf-8',
						'X-XSRF-Token': csrfToken
					},
					body: JSON.stringify(id)
				})
					.then(async response => {

						if (response.ok) {
							const data = await response.json();
							console.log(data.name);
							row.child(format(data)).show();

						}
						else {
							console.log("SomeOne is error");
						}
					})

		}
	});

	function format(d) {
		return '<div class="slider">' +
			'<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
			'<tr>' +
			'<td>Full name:</td>' +
			'<td>' + d.name + '</td>' +
			'</tr>' +
			'<tr>' +
			'<td>Extension number:</td>' +
			'<td>' + d.email + '</td>' +
			'</tr>' +
			'<tr>' +
			'<td>Extra info:</td>' +
			'<td>And any further details here (images etc)...</td>' +
			'</tr>' +
			'</table>' +
			'</div>';
	}

	$('.first-page').on('click', function() {

		var firstColumn = table.column(6);
		var secondColumn = table.column(9);

		firstColumn.visible(!firstColumn.visible());
		secondColumn.visible(!secondColumn.visible());

		var one = table.column(7);
		var two = table.column(10);

		one.visible(!one.visible());
		two.visible(!two.visible());


	});

	$('.second-page').on('click', function() {
		var firstColumn = table.columns('.first');
		var secondColumn = table.columns('.second');
		firstColumn.visible(!firstColumn.visible());
		secondColumn.visible(!secondColumn.visible());
	});


	



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

	$('#table1')
		.on(
			'change',
			':checkbox',
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

	$('#table1 tbody').on('click', '.btn-outline-primary',
		function() {
			var modalTitle = $(this).data(
				'modal-title');
			var row = table.row($(this).closest('tr')).data();
			console.log(email);
			$('#emailModal .modal-title').text(
				modalTitle);
			$('#emailModal .candidatEmail').val(row.email);
			$("#emailModal .userEmail").val(row.email);
		});

	// Function to get the CSRF token from the cookie


	$('#table1 tbody').on('click', '.seeMoreBtn',
		function() {
			var tr = $(this).closest('tr');
			var row = table.row(tr).data();
			$('#seeMoreModal').modal('show');

			var id = row.id;



			{
				fetch('/seeeMore', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json;charset=utf-8',
						'X-XSRF-Token': csrfToken

					},
					body: JSON.stringify(id)
				})
					.then(response => {
						if (response.ok) {
							return response.json();

						} else {
							console.error("Fail");
						}
					})
					.then(candidateData => {
						console.log(candidateData);
						$('#name').html(candidateData.name);
						$('#email').html(candidateData.email);
						$('#phone').html(candidateData.phone);
						$('#dob').html(candidateData.dob);
						$('#gender').html(candidateData.gender);
						$('#education').html(candidateData.education);
						$('#applyPosition').html(candidateData.apply_position);
						$('#expectedSalary').html(candidateData.expectedSalary);
						$('#experience').html(candidateData.experience);
						$('#level').html(candidateData.lvl);
						$('#specialist').html(candidateData.specialist_tech);
					})
					.catch(error => {
						console.error("Error:", error);
					});
			}

		});

	

	$('#table1 tbody').on('change', '#changeStatus', function() {
		var selectedValue = $(this).val();
		var tr = $(this).closest('tr');
		var row = table.row(tr).data();
		fetch('/changeInterview?id=' + row.id + '&status=' + selectedValue, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json;charset=utf-8',
				'X-XSRF-Token': csrfToken
			}
		})
			.then(response => {
				if (response.ok) {
					Swal.fire({
						icon: "success",
						title: "Success Update"
					})
				} else {
					console.error("Fail");
					Swal.fire({
						icon: "error",
						title: "Error"
					})
				}
			});
	});




});
const triggerTabList = document.querySelectorAll('#myTab button')
triggerTabList.forEach(triggerEl => {
  const tabTrigger = new bootstrap.Tab(triggerEl)

  triggerEl.addEventListener('click', event => {
    event.preventDefault()
    tabTrigger.show()
  })
})


// Add a click event listener to the button
fetchValueButton.addEventListener('click', function() {
	// Get the value of the input with id="time"
	const timeInputValue = document.getElementById('time').value;

	// Do something with the value (e.g., display it in an alert)
	alert('Value of time: ' + timeInputValue);
});

