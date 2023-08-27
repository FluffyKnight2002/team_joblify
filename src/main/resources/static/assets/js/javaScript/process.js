



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

														
															
														},
														{
															targets : 5,
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
															targets : 6,
															 render:function(data)
														    {let pend=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return pend;},
														    sortable:false
															
															
														},
														{
															targets:7,
															data:'cancelCandidate',
															 render:function(data)
														    {let cancel=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return cancel;},
														    sortable:false
														},
														{
															targets : 8,
															data:"notInterviewCandidate",
															 render:function(data)
														    {let not=data == null ? '<span>-</span>' :
																'<input type="submit" value="'+data+'">';
																return not;},
														    sortable:false
														},
														{
															targets:9,
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