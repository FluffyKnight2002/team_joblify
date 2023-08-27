



var table;
$(document).ready(function() {table = $('#table2').DataTable(
								{
								"serverSide" : true,
								"processing" : true,
								"ajax" : '/process',
								      
								
								"columns" : [
														{
															data : "openDate",
															targets : 0
														},
														{
															data : "closeDate",
															targets : 1
														},
														{
															targets:2,
															data:"position",
															className:"position",
															render:function(data,type,row)
															{   return `<a href="/CandidateViewSummary?viId=${row.id}&name=${data}">${data}</a>`;},
															sortable:false
															
														},
														
														{
														    targets: 3,
														    data: "totalCandidate",
														    render:function(data)
														    {let total=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return total;},
														    sortable:false
														},
														{
															targets:4,
															data:'interviewedCounts',
															 render:function(data)
														    {
																let inter=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return inter;},
														    sortable:false


															}]
														})



});