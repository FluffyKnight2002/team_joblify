$(document).ready(function() {
    $('#data').summernote({
        height: 'auto'
    });
    $('#data_1').summernote({height: 'auto'});

});


let filterElements = [
    {name: 'apply-date-dropdown-item', isRemove: false, filterId: 'filter-apply-date'},
    {name: 'position-dropdown-item', isRemove: false, filterId: 'filter-title'},
    {name: 'level-dropdown-item', isRemove: false, filterId: 'filter-level'},
    {name: 'selection-status-dropdown-item', isRemove: false, filterId: 'filter-selection-status'},
    {name: 'interview-status-dropdown-item', isRemove: false, filterId: 'filter-interview-status'}
];
var table;
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
                    targets: 6,
                    data: "passedCandidate",
                    render: function (data) {
                        let passed = data == null ? '-' : data;
                        let passBtn = (passed == '-') ?
                            '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-success bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return passBtn;


                    },
                    sortable: false
                },
                {

                    data: "pendingCandidate",
                    targets: 7,
                    render: function (data) {
                        let pend = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-warning bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return pend;
                    },
                    sortable: false
                },
                {
                    targets: 8,
                    data: 'cancelCandidate',
                    render: function (data) {
                        let cancel = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-danger bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return cancel;
                    },
                    sortable: false
                },
                {
                    targets: 9,
                    data: "notInterviewCandidate",
                    render: function (data) {
                        let not = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-secondary bg-gradient rounded-pill px-4">' + data +'</span></div>';
                        return not;
                    },
                    sortable: false
                },
                {
                    targets: 10,
                    data: 'acceptedCandidate',
                    render: function (data) {
                        let acc = data == null ? '<div class="text-center"><span>-</span></div>' :
                            '<div class="text-center"><span class="badge bg-gradient-ltr rounded-pill px-4">' + data +'</span></div>';
                        return acc;
                    },
                    sortable: false
                },
                        
                        ],
                        order:[[0,'desc']]

        });

    // Assuming you have initialized DataTable properly
    let searchRow = $('#table1_filter').closest('.row');
    $('.dt-row').css('margin-bottom','40px')
    let recentFilterDropdownCon = `<div class="col-8" id="recent-filter-dropdown-con"></div>`;
    let resetFilterButton = `
        <div id="reset-filter" class="mt-3 col-1 text-center">
            <img src="/assets/images/candidate-images/filter_reset.svg" class="reset-filter"
                 style="padding: 8px;border: 2px dotted gray;border-radius: 15px;cursor: pointer;" width="50px" data-bs-toggle="tooltip"
                data-bs-placement="right" title="Reset filter" onclick="resetFilters()">
            </span>
        </div>
    `;
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
                        <li class="dropdown-item filter-items" onclick="DateFilterButton('Today');checkAndToggleFilterButton();">Today</li>
                        <li class="dropdown-item filter-items" onclick="DateFilterButton('Last Week');checkAndToggleFilterButton();">Last Week</li>
                        <li class="dropdown-item filter-items" onclick="DateFilterButton('Last Month');checkAndToggleFilterButton();">Last Month</li>
                        <li class="dropdown-item filter-items" onclick="DateFilterButton('Last 6 Month');checkAndToggleFilterButton();">Last 6 Month</li>
                        <li class="dropdown-item filter-items" onclick="DateFilterButton('Last Year');checkAndToggleFilterButton();">Last Year</li>
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
                        <li class="dropdown-item filter-items" onclick="createSelectionStatusFilterButton($(this));checkAndToggleFilterButton();">RECEIVED</li>
                        <li class="dropdown-item filter-items" onclick="createSelectionStatusFilterButton($(this));checkAndToggleFilterButton();">CONSIDERING</li>
                        <li class="dropdown-item filter-items" onclick="createSelectionStatusFilterButton($(this));checkAndToggleFilterButton();">VIEWED</li>
                        <li class="dropdown-item filter-items" onclick="createSelectionStatusFilterButton($(this));checkAndToggleFilterButton();">OFFERED</li>
                    </ul>
                </li>
                <li class="dropdown-item filter-items interview-status-dropdown-item">
                    <span>Interview Status</span>
                    <ul class="dropdown-menu dropdown-submenu" id="interview-status-dropdown-submenu" style="top: -125px">
                        <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">NONE</li>
                        <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">PENDING</li>
                        <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">CANCEL</li>
                        <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">PASSED</li>
                         <li class="dropdown-item filter-items" onclick="createInterviewStatusFilterButton($(this));checkAndToggleFilterButton();">ACCEPTED</li>
                    </ul>
                </li>
            </ul>
            </div>
        </div>
    `;
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
            					<a id="pdfDownload" class="image-button" aria-label="Download pdf" href="/all_candidates/pdf"
                    			></a>
                    		</div>
                    		<div class="col-6 text-center" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Report Excel">
                				<a id="excelDownload" class="image-button" aria-label="Download Excel" href="/all_candidates/excel"
                				></a>
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
            $('#confirmationModal .modal-body').html('Are you sure Accepted ' + row.name + '?');
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
    const selectElement = $('#positionSelect');
    const selectELement1 = $('#post');
    const statusSelect = $('#Selection');
    const interview = $('#Interview');
    $.ajax({
        url: '/allPositions',
        type: 'GET',
        success: function(response) {
            let submenuHTML = '';

            let currentLetter = null;
            $.each(response, function(index, item) {
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
            $('#position-dropdown-submenu').html(submenuHTML);
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
            let submenuHTML = '';

            let currentLetter = null;
            $.each(response, function(index, post) {
                const startingLetter = post.openDate.substring(0, 4);
                if (startingLetter !== currentLetter) {
                    submenuHTML += `<li style="background: #1e497b"><b class="ps-2 text-white">${startingLetter}</b></li>`;
                    currentLetter = startingLetter;
                }
                submenuHTML += `
            <li class="dropdown-item filter-items"
                onclick="createPostFilterButton('${post.id}','${post.openDate}','${post.position}');checkAndToggleFilterButton();" data-filter-id="filter-post">
              ${post.openDate}(${post.position})
            </li>`;
            });
            $('#post-dropdown-submenu').html(submenuHTML);
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
    //CV download end

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
        edit = '';

    })
    $('#add-time').on('click', function() {
        const edit = `<span Style='color:red' class='time-setting'>Start Time</span> to <span Style='color:red' class='end-setting'>End Time</span>`
        $('#data').summernote('pasteHTML', edit);
        edit = '';

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
        const stage = document.getElementById('interview-stage-select');
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
            console.log(stage.value)
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
    let colse=$('#confirmationModal');
    if(colse.length){
        colse.modal('hide');
        table.ajax.reload();
    }
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
function DateFilterButton(selectedValue) {
    console.log('>>>>>>>>>>>>',selectedValue)
    var filterOption = $(this).find('option:selected').val();
    var currentDate = new Date();
    var endDate = currentDate.toISOString().split('T')[0]; // End date is today
    var startDate = new Date(currentDate);
    filterElements[0].isRemove = true;
    $('.apply-date-dropdown-item').hide();

    let selectedText = null;

    if(selectedValue === 'Custom') {
        $('#filter-start-date').val(startDate);
        $('#filter-end-date').val(endDate);
        selectedText = selectedValue;
        $('#filter-apply-date').val(selectedText);
    }else {
        $('#filter-apply-date').val(selectedText);
    }
    switch (selectedValue) {
        case 'Today':
            var first = currentDate.toISOString().split('T')[0];
            console.log(first) // Convert to ISO format (YYYY-MM-DD)
            table.column(12).search(first).draw();
            break;

        case 'Last Week':
            // Copy current date to calculate the start date
            startDate.setDate(currentDate.getDate() - 7); // Subtract 7 days from current date
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate);
            // Perform action for 'last_week' option
            table.column(12).search(isoStartDate + ';' + endDate).draw();
            break;

        case 'Last Month':
            startDate.setMonth(currentDate.getMonth() - 1); // Subtract 1 month
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate); // Output: 2023-08-16 (example)

            // Perform action for 'last_month' option
            table.column(12).search(isoStartDate + ';' + endDate).draw();
            break;
        case 'Last 6 Month':
            startDate.setMonth(currentDate.getMonth() - 6); // Subtract 1 month
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate); // Output: 2023-08-16 (example)

            // Perform action for 'last_month' option
            table.column(12).search(isoStartDate + ';' + endDate).draw();
            break;
        case 'Last Year':
            startDate.setFullYear( currentDate.getFullYear() - 1)
            var isoStartDate = startDate.toISOString().split('T')[0];
            console.log(startDate);
            console.log(endDate);

            // Perform action for 'past_year' option
            table.column(12).search(isoStartDate + ';' + endDate).draw();
            break;
        case '':
            table.column(12).search('' + ';' + '').draw();
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
function showSelectedDropdownRemoveButton(button) {
    const removeButton = $(button).next('.selected-dropdown-remove-button');
    removeButton.show();
}
function SelectedFilterName(item) {
    console.log(item)
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
    var filterOption = $(this).find('option:selected').val();
    var currentDate = new Date();
    var endDate = currentDate.toISOString().split('T')[0]; // End date is today
    var startDate = new Date(currentDate);
    switch (selectedValue) {
        case 'Today':
            var first = currentDate.toISOString().split('T')[0];
            console.log(first) // Convert to ISO format (YYYY-MM-DD)
            table.column(12).search(first).draw();
            break;

        case 'Last Week':
            // Copy current date to calculate the start date
            startDate.setDate(currentDate.getDate() - 7); // Subtract 7 days from current date
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate);
            // Perform action for 'last_week' option
            table.column(12).search(isoStartDate + ';' + endDate).draw();
            break;

        case 'Last Month':
            startDate.setMonth(currentDate.getMonth() - 1); // Subtract 1 month
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate); // Output: 2023-08-16 (example)

            // Perform action for 'last_month' option
            table.column(12).search(isoStartDate + ';' + endDate).draw();
            break;
        case 'Last 6 Month':
            startDate.setMonth(currentDate.getMonth() - 6); // Subtract 1 month
            var isoStartDate = startDate.toISOString().split('T')[0]; // Convert start date to ISO format
            console.log(isoStartDate); // Output: 2023-07-16 (example)
            console.log(endDate); // Output: 2023-08-16 (example)

            // Perform action for 'last_month' option
            table.column(12).search(isoStartDate + ';' + endDate).draw();
            break;
        case 'Last Year':
            startDate.setFullYear( currentDate.getFullYear() - 1)
            var isoStartDate = startDate.toISOString().split('T')[0];
            console.log(startDate);
            console.log(endDate);

            // Perform action for 'past_year' option
            table.column(12).search(isoStartDate + ';' + endDate).draw();
            break;
        case '':
            table.column(12).search('' + ';' + '').draw();
            break;
        default:
            return false;
    }

    // if ($('input[name="datefilter2"]').length > 0) {
    if(item != 'Custom') {
        $('input[name="datefilter2"]').val('');
        button.text($.trim(selectedValue)); // Update the text of the button
    }else {
        $('.date-posted-filter-btn').text('Custom');
    }
    // }
}
function handleFilterChange(columnIndex, filterValue, idKey) {
    table.column(columnIndex).search(filterValue).draw();
    currentId.delete('viId');
    currentId.delete('name');
    currentId.set(idKey, filterValue);
    history.pushState(null, null, '?' + currentId.toString());
}
function createTitleFilterButton(selectedValue) {

    filterElements[1].isRemove = true;
    $('.position-dropdown-item').hide();

    $('#filter-title').val(selectedValue);
    console.log(selectedValue)
    checkAndToggleFilterButton();

    table.column(4).search(selectedValue).draw();

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
// function createPostFilterButton(selectedValue,date,name) {
//
//     filterElements[1].isRemove = true;
//     $('.post-dropdown-item').hide();
//
//     $('#filter-post').val(selectedValue);
//
//     checkAndToggleFilterButton();
//     console.log(selectedValue,date,name)
//     table.column(1).search(selectedValue).draw();
//
//     // Create a filter button with the selected filter item
//     var selectedDropdown = `
//         <div class="btn-group mt-3 p-2 position-relative">
//             <button type="button" class="btn btn-sm btn-primary dropdown-toggle col-3
//                 recent-filter-dropdown-btn position-filter-btn"
//                 onmouseenter="showSelectedDropdownRemoveButton(this);"
//                 data-bs-toggle="dropdown" aria-expanded="false">
//                ${date} (${name})
//             </button>
//             <span class="bg-danger selected-dropdown-remove-button post-filter-remove" data-filter-name="post-dropdown-item">
//                 <i class="bi bi-x"></i>
//             </span>
//             <ul class="dropdown-menu dropdown-submenu scrollable-submenu" id="post-filter-dropdown-submenu">
//                 <!-- Submenu items will be populated here -->
//             </ul>
//         </div>`;
//
//     // Append the selectedDropdown to the appropriate container
//     $('#recent-filter-dropdown-con').append(selectedDropdown);
//
//     // Fetch and populate submenu items
//     fetch('/post')
//         .then(response => response.text())
//         .then(submenuHTML => {
//             name=submenuHTML.name;
//             console.log(name)
//             // Use the generated submenuHTML to populate the submenu
//             $('#position-filter-dropdown-submenu').html(submenuHTML);
//
//             // Remove the existing onclick attribute from recent-filter-items
//             $('#position-filter-dropdown-submenu .filter-items').removeAttr('onclick');
//
//             // Add the onclick attribute to every recent-filter-items
//             $('#position-filter-dropdown-submenu .filter-items').attr('onclick', 'changeSelectedFilterName(this);');
//
//             // Hide other remove buttons and show the recent-filter-dropdown-btn
//             $('.selected-dropdown-remove-button').hide();
//             $('.recent-filter-dropdown-btn').show();
//         });
//
// }
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
                    onclick="updateFilterLevel();" data-filter-id="filter-level">Confirm</span>
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

    }
}
function updateFilterLevel() {

    const selectedLevels = [];

    // Select all checkboxes with class 'level-checkbox' that are checked
    const checkboxes = $('.level-checkbox:checked');
    const checkboxes2 = $('.level-filter-checkbox:checked');

    checkboxes.each(function () {
        selectedLevels.push($(this).val());
    });
    // Iterate through the checked checkboxes and collect their values
    const selectedValues = checkboxes.map(function () {
        return this.value;
    }).get().join('|');
    console.log('satge-1',selectedValues)
    table.column(9).search(selectedValues).draw()
    if (selectedLevels.length === 0) {
        console.log("selectedLevels ", selectedLevels)
        const selectedValues = checkboxes2.map(function () {
            return this.value;
        }).get().join('|');
        console.log('satge-2',selectedValues)
        table.column(9).search(selectedValues).draw()
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
function updateDataTable() {
    $('#filter-vacancy-info-id').val("All");
    table.ajax.reload();
}
function createSelectionStatusFilterButton(selectedValue) {

    filterElements[4].isRemove = true;
    $('.selection-status-dropdown-item').hide();
    console.log(selectedValue.val())
    checkAndToggleFilterButton();
    table.column(5).search(selectedValue).draw();

    var selectedText = selectedValue.text();
    table.column(5).search(selectedText).draw();
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
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-selection-status">RECEIVED</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-selection-status">CONSIDERING</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-selection-status">VIEWED</li>
                <li class="dropdown-item filter-items" onclick="changeSelectedFilterName(this);" data-filter-id="filter-selection-status">OFFERED</li>
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
function changeSelectedFilterName(item) {
    console.log(item)
    let selectedValue = $(item).text();
    console.log(selectedValue)
    if (item) {

        if(selectedValue==='OFFERED' || selectedValue==='CONSIDERING' || selectedValue==='VIEWED' || selectedValue==='RECEIVED'){
            table.column(5).search($(item).text()).draw();// Get the selected value from the clicked item
            console.log("Selected Value-1 : " , selectedValue);
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
            if(selectedValue != 'Custom') {
                $('input[name="datefilter2"]').val('');
                button.text($.trim(selectedValue)); // Update the text of the button
            }else {
                $('.apply-date-filter-btn').text('Custom');
            }
            // }
        }
        else{
            console.log("hello")
            table.column(4).search(selectedValue).draw();
            console.log("Selected Value-2 : " , selectedValue);
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
            if(selectedValue != 'Custom') {
                $('input[name="datefilter2"]').val('');
                button.text($.trim(selectedValue)); // Update the text of the button
            }else {
                $('.apply-date-filter-btn').text('Custom');
            }
            // }
        }
    }





}
function createInterviewStatusFilterButton(selectedValue) {

    filterElements[4].isRemove = true;
    $('.interview-status-dropdown-item').hide();

    checkAndToggleFilterButton();

    var selectedText = selectedValue.text();
    $('#filter-interview-status').val(selectedText);
    table.column(8).search(selectedText).draw();
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
               <li class="dropdown-item filter-items" onclick="InterviewStatusFilterButton(this)">NONE</li>
                        <li class="dropdown-item filter-items" onclick="InterviewStatusFilterButton(this);" data-filter-id="filter-interview-status">PENDING</li>
                        <li class="dropdown-item filter-items" onclick="InterviewStatusFilterButton(this)" data-filter-id="filter-interview-status">CANCEL</li>
                        <li class="dropdown-item filter-items" onclick="InterviewStatusFilterButton(this)" data-filter-id="filter-interview-status">PASSED</li>
                         <li class="dropdown-item filter-items" onclick="InterviewStatusFilterButton(this)" data-filter-id="filter-interview-status">ACCEPTED</li>
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
function InterviewStatusFilterButton(item) {
    if (item) {
        let selectedValue = $(item).text(); // Get the selected value from the clicked item
        console.log("Selected Value : " , selectedValue);
        table.column(8).search(selectedValue).draw();
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
        if(selectedValue != 'Custom') {
            $('input[name="datefilter2"]').val('');
            button.text($.trim(selectedValue)); // Update the text of the button
        }else {
            $('.apply-date-filter-btn').text('Custom');
        }
        // }

        updateDataTable();
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
    table.column(1).search('').draw();
    table.column(4).search('').draw();
    table.column(5).search('').draw();
    table.column(8).search('').draw();
    table.column(9).search('').draw();

    $('#reset-filter').hide();
    $('#custom-filter').show();

    updateDataTable();
}

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
        table.column(9).search('').draw();
    }else if (filterName === 'apply-date-dropdown-item') {
        table.column(12).search('').draw();
    }else if(filterName==='position-dropdown-item'){
        table.column(4).search('').draw();
    }else if (filterName === 'selection-status-dropdown-item') {
        table.column(5).search('').draw();
    }else if(filterName==='post-dropdown-item'){
        table.column(1).search('').draw();
    }else if(filterName==='interview-status-dropdown-item'){
        table.column(8).search('').draw();
    }

    $(this).closest('.btn-group').remove();
    checkAndToggleFilterButton();

});