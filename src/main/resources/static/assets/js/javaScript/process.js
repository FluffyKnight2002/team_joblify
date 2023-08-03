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
															
															
														},
														
														{
														    targets: 3,
														    data: "totalCandidate",
														    
														},
														{
															targets:4,
															data:'interviewCandidate',
															visible:true
														
															
														},
														{
															targets : 5,
															data:"passedCandidate",
														
															
														},
														{
															
															data : "pendingCandidate",
															targets : 6,
															
															
														},
														{
															targets:7,
															data:'cancelCandidate',
														},
														{
															targets : 8,
															data:"notInterviewCandidate",
														},
														{
															targets:9,
															data:'acceptedCandidate',
														},
												
												],

											});
											});