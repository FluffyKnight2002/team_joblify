$('.summernote').summernote({
	tabsize: 2,
	height: 500,
})

function getCsrfToken() {
		const metaTag = document.querySelector('meta[name="_csrf"]');
		return metaTag ? metaTag.getAttribute('content') : null;
	}
	const csrfToken = getCsrfToken();
	
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

	$('#table1').on('change',':checkbox',
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

	$('#table1 tbody').on('click', '.btn-outline-primary',function() {
       var modalTitle = $(this).data('modal-title');
       var row = table.row($(this).closest('tr')).data();
      
       var emailContent = `

Dear ${row.name},

Welcome from ACE Data Systems Ltd. We would like to invite for an interview at Zoom Meeting to you for interview to get to know you more detail appointment on (<span style="color:red;" class='date-setting text-bold' ></span> )  <span style="color:red;" class='time-setting text-bold'></span> to <span  style="color:red;" class='end-setting text-bold'></span>.
Please join below the zoom link. Thanks.

Note.... We want you to be in good condition during the interview with good internet and a quiet environment.

Join Zoom Meeting
https://zoom.us/j/92191528025?pwd=K1BMUzR4M0hQZDJqQm1DUWxsRTN3dz09

Meeting ID: 921 9152 8025
Passcode: 178426
`;
	var offermail=`
<b>Dear ${row.name}.</b>
 
We are pleased to inform you that you are appointed as Software Engineer in Banking and Finance Department of ACE Data Systems Ltd. Please see your entitlement information detail and brief HR rule of company in below.

Entitlement Pay Information

Joined Start Date           	– 

Position                         	– Software Engineer

Basic Pay                      	– 

Project Allowance		– 	

Meal + Transportation Allowance   – 3000 * (Working Days per Month) = Normally 60, 000 MMK

Note for probation period:

*After probation and if you are selected, you must work minimum 2 years contract at ACE
*If you wish to resign in violation of within 2 years Agreement, you must give three months’ notice and your two months’ current net salary must be refunded.   
*No leaves are entitled in the probation period.
*Any leave must be informed in advance by phone.
*You must inform us 1 month in advance to resign.
*ACE will inform you 1 month in advance if we want to terminate.
*You must learn all technical knowledge required by project(.NET, Java or any other technologies)


 
HR Rule of Company (Brief)

(1)   Working Hour            	: From 9:00 AM To 06:00 PM
                                                                          : From Monday to Friday (Except gazette holidays)
(2)  Leave Entitlement (After probation period)

a.     Earn Leave                  	: 10 days per year after one year service history
                                                                         (Need to apply one month in advance)


b.     Casual Leave              	: 10 days per year after probation period 
                                                                        (Maximum 3 days per Month)

c.     Medical Leave                : 30 days per year (With approved medical certification)


(3)   Resignation after Employment Period     :          

a.     The employee shall give not less than three months written notice to the Employer for his/her resignation.

b.     In case the employee for any reason, leave the services of the Company before the agreement period then employee shall pay 3 times of the current net salary.

(4)   Confidentiality                                        :          

a.     The employee agrees not to disclose any of Company’s confidential information, Company’s trademarks or knowledge pertaining to the business of the Company during or after the employment.
`


        $('#emailModal .modal-title').text(modalTitle);
        $('#emailModal .candidatEmail').val(row.email);
        $("#emailModal .userEmail").val(row.email);
	 	$("#emailModal #candidate-id").val(row.id);

      
		emailContent=emailContent.replace(/\n/g, '<br>');
		offermail=offermail.replace(/\n/g, '<br>');
$('#where').on('change', function() {
    var type = $(this).val();
    var updatedContent = getEmailContent(type);
     updatedContent = updatedContent.replace(/\n/g, '<br>');
    $('#emailModal #data').summernote('code', updatedContent);
});

function getEmailContent(type) {
    var onlineText = `

Dear ${row.name},

Welcome from ACE Data Systems Ltd. We would like to invite for an interview at Zoom Meeting to you for interview to get to know you more detail appointment on (<span style="color:red;" class='date-setting text-bold' ></span> )  <span style="color:red;" class='time-setting text-bold'></span> to <span  style="color:red;" class='end-setting text-bold'></span>.
Please join below the zoom link. Thanks.

Note.... We want you to be in good condition during the interview with good internet and a quiet environment.

Join Zoom Meeting
https://zoom.us/j/92191528025?pwd=K1BMUzR4M0hQZDJqQm1DUWxsRTN3dz09

Meeting ID: 921 9152 8025
Passcode: 178426
`;
    var offlineText = `

Dear ${row.name},

Welcome from ACE Data Systems Ltd. We would like to invite for an interview at Zoom Meeting to you for interview to get to know you more detail appointment on (<span style="color:red;" class='date-setting text-bold' ></span> )  <span style="color:red;" class='time-setting text-bold'></span> to <span  style="color:red;" class='end-setting text-bold'></span>.
Building 18, 7th floor, MICT Park,Hlaing Township, Yangon, Myanmar. Thanks.

Note.... We want you to be in good condition during the interview with good internet and a quiet environment.

Join Zoom Meeting
https://zoom.us/j/92191528025?pwd=K1BMUzR4M0hQZDJqQm1DUWxsRTN3dz09

Meeting ID: 921 9152 8025
Passcode: 178426
`;
    
    if (type == 'OFFLINE') {
        return offlineText;
    } else {
        return onlineText;
    }
}

		if(modalTitle=='Interview Invert Mail'){
		$('#emailModal #data').summernote('code', emailContent);
		 $('#emailModal .subject').val('Interview Invert Mail');
		 $('#emailModal .status').show();
		}else{
			 $('#emailModal #data').summernote('code',offermail);
			  $('#emailModal .status').hide();
			 // ('#emailModal #view-type').hide();
			  $('#emailModal .subject').val('Job offer Mail');
		}
        
       
           });
 $('#date').on('input', function() {
	  const inputDate = $(this).val();
    const date = new Date(inputDate);
    const day = date.getDate();
    const month = date.toLocaleString('default', { month: 'long' });
    const year = date.getFullYear();
    const formattedDate = `${day}-${month}-${year}`;
    
    $('.date-setting').html(formattedDate);    });
 $('#time').on('input',function(){
	  var inputTime = $(this).val();
	  var date = new Date();
    var timeParts = inputTime.split(':');
    date.setHours(parseInt(timeParts[0], 10));
    date.setMinutes(parseInt(timeParts[1], 10));
    
    // Format time with AM/PM
    var formattedTime = date.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
   
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

const form = document.getElementById('send-mail');
const editor = document.getElementById('object');
const hiddenInput = document.getElementById('content');
const to = document.getElementById('to');
const date1=document.getElementById('date');
const time=document.getElementById('time');
const type=document.getElementById('where');
const stage=document.getElementById('interview-status');
const canid=document.getElementById('candidate-id');
const fetchValueButton = document.getElementById('fetchValueButton'); // Add the missing button ID
// Add a click event listener to the button
fetchValueButton.addEventListener('click', function() {
    // Get the value of the input with id="time"
    hiddenInput.value =editor.querySelector('.summernote').value
	console.log( hiddenInput.value)
	console.log(to)
    const data = {
        to: to.value,
        content:hiddenInput.value,
        date:date1.value,
        time:time.value,
        status:stage.value,
        type:type.value,
        canId:canid.value,
        
        
    };

    try {
        fetch('/send-invite-email', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8',
                'X-XSRF-Token': csrfToken // Include CSRF token as a request header
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                console.log('Email sent successfully:', response);
            } else {
                console.error('Failed to send email:', response.statusText);
            }
        })
        .catch(error => {
            console.error('An error occurred:', error);
        });
    } catch (error) {
        console.error('An error occurred:', error);
    }
});

// Get the textarea element by its ID



/////////////////////////////////////////
   

