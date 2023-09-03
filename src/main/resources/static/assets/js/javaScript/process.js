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
