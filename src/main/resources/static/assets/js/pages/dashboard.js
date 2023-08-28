let post4;
let post5;
let post6;
let chartProfileVisit;
let graph;

let monthlyVacancyCount;

function getYear() {
	const currentDate = new Date();
	const currentYear = currentDate.getFullYear();
	fetch('/getYear')
		.then(response => response.json())
		.then(data => {
			console.log(data);
			data.forEach(yearArray => {
				const select = $('#year'); // Assuming you have an element with id 'year' for your select element
				const year = yearArray[0]; // Extract the year from the inner array

				const option = $('<option>').val(year).text(year);
				select.append(option);

				const select3 = $('#year3');
				const year3 = yearArray[0];

				const option3 = $('<option>').val(year3).text(year3);
				select3.append(option3);
			});
		})
		.catch(error => {
			console.error('Error fetching data:', error);
		});
}


function getDepartment() {
	fetch('/all-department')
	  .then(response => response.json())
	  .then(data => {
		const department3 = $('#department3');
		$.each(data, function(index, department) {
		  const optionElement = $('<option>')
			.val(department.name)
			.text(department.name);
		  department3.append(optionElement);
		});
	  })
	  .catch(error => {
		console.error('Error fetching data:', error);
	  });
  }
  



async function fetchFor1stChart(selectedValue) {
	
	try {
		const response = await fetch('/dashboard?timeSession=' + selectedValue, {
			method: 'POST',
			headers: {
				// 'Content-Type': 'application/json;charset=utf-8', // Add this line
				[csrfHeader]: csrfToken
			},
		});
		
		const data = await response.json();
		
		post4 = [];
		post5 = [];
		post6 = [];

		data.forEach(post => {
			post4.push(post.postTotal);
			post5.push(post.totalApplied);
			post6.push(post.hiredPost);
		});

		chartProfileVisit.updateSeries([
			{ name: 'Total Post', data: post4 },
			{ name: 'Total Candidate', data: post5 },
			{ name: 'Hired', data: post6 }
		]);
	} catch (error) {
		console.error('Error fetching data:', error);
	}
}

async function fetchFor3rdChart() {

	let selectedDepartment = $("#department3").val();
	let selectedYear = $("#year3").val();

	console.log(selectedDepartment + 'sdd');
	console.log(selectedYear + 'aa');
	if(selectedDepartment === null){
		selectedDepartment = 'All'
	} else if(selectedYear === null){
			selectedYear = 'All'
	}
	try {
		const response = await fetch('/yearly-vacancy-count?timeSession=' + selectedYear + '&department=' + selectedDepartment)
		
		const data = await response.json();
		
		monthlyVacancyCount = [];

		data.forEach(post => {
			monthlyVacancyCount.push(post.totalVacancyCount);
		});

		graph.updateSeries([
			{ name: 'Vacancy Count', data: monthlyVacancyCount }
		]);
	} catch (error) {
		console.error('Error fetching data:', error);
	}
}




document.addEventListener('DOMContentLoaded', function(){
	post4 = [];
	post5 = [];
	post6 = []
	totalVacancyCount = [];

	getYear();
	getDepartment();

	var optionsProfileVisit = {

		series: [{
			name: 'Total Post',
			data: post4

		}, {
			name: 'Total Candidate',
			data: post5
		}, {
			name: 'Hired',
			data: post6
		}],
		chart: {
			width: "100%",
			height: 380,
			type: "bar",

		},
		plotOptions: {
			bar: {
				horizontal: false,
				columnWidth: '55%',
				endingShape: 'rounded'
			},
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 2,
			colors: ['transparent']
		},
		xaxis: {
			categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
		},
		yaxis: {
			title: {
				text: 'Detail'
			}
		},
		fill: {
			opacity: 1
		},
		tooltip: {
			y: {
				formatter: function (val) {
					return " " + val + ""
				}
			}
		},
		responsive: [
			{
				breakpoint: 1000,
				options: {
					plotOptions: {
						bar: {
							horizontal: true
						}
					},
					legend: {
						position: "bottom"
					}
				}
			}
		]
	};
	chartProfileVisit = new ApexCharts(document.querySelector("#chart-profile-visit"), optionsProfileVisit);
	chartProfileVisit.render();

	$('#year').on('change', function () {
		const selectedValue = $(this).val();

		console.log('neww>>>>>>>>>>>>', post4, post5, post6)
		fetchFor1stChart(selectedValue);
	}

	)

	//////////////////////////////////////////


	var options = {
		series: [
			{
				name: "Vacancy Count",
				data: totalVacancyCount
			},

		],
		chart: {
			height: 350,
			type: 'area',
			zoom: {
				enabled: true
			},
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			width: 3,
			curve: 'smooth'

		},

		legend: {
			tooltipHoverFormatter: function (val, opts) {
				return val + ' - ' + opts.w.globals.series[opts.seriesIndex][opts.dataPointIndex] + ''
			}
		},
		markers: {
			size: 0,
			hover: {
				sizeOffset: 6
			}
		},
		xaxis: {
			categories: ['Jan', 'Feb', 'March', 'April', 'May', 'June', 'July', 'August', 'Sept', 'Oct',
				'Nov', 'Dec'
			],
		},
		tooltip: {
			y: [
				{
					title: {
						formatter: function (val) {
							return val + " (mins)"
						}
					}
				},
				{
					title: {
						formatter: function (val) {
							return val + " per session"
						}
					}
				},
				{
					title: {
						formatter: function (val) {
							return val;
						}
					}
				}
			]
		},
		grid: {
			borderColor: '#f1f1f1',
		}
	};

	graph = new ApexCharts(document.querySelector("#graph"), options);
	graph.render();

	$("#department3, #year3").change(fetchFor3rdChart);


})


var pine = {
	series: [14, 23, 21, 17, 15, 10, 12, 17, 21],
	chart: {
		type: 'polarArea',
	},
	stroke: {
		colors: ['#fff']
	},
	fill: {
		opacity: 0.8
	},
	responsive: [{
		breakpoint: 480,
		options: {
			chart: {
				width: 200
			},
			legend: {
				position: 'bottom'
			}
		}
	}]
};

var chart = new ApexCharts(document.querySelector("#chart"), pine);
chart.render();







