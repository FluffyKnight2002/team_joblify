let post4;
let post5;
let post6;
let chartProfileVisit;
let graph;
let monthlyVacancyCount;
let pchart;
let year;
let department;
let pine;
let position;

async function getYear() {

	try {

	const response = await fetch('/getYear')
	const data = await response.json();


			console.log(data);
			data.forEach(yearArray => {
				const select = $('#year1'); // Assuming you have an element with id 'year' for your select element
				const year = yearArray[0]; // Extract the year from the inner array

				const option = $('<option>').val(year).text(year).attr('selected', 'selected');
				select.append(option);

				const select3 = $('#year3');
				const year3 = yearArray[0];

				const option3 = $('<option>').val(year3).text(year3).attr('selected', 'selected');
				select3.append(option3);

				const year4 = yearArray[0];
				const select2=$('#pine');
				select2.append($('<option>').val(year4).text(year4));


			});
			return data ;
		}  catch (error) {
			console.error('Error fetching data:', error);
		}
}


async function getDepartment() {
    try {
        const response = await fetch('/all-department'); // Make sure to use await here
        const data = await response.json();

        const department3 = $('#department3'); // Remove the unnecessary await here
        const department4 = $('#department4'); // Remove the unnecessary await here

        $.each(data, function(index, department) {
            const optionElement = $('<option>')
                .val(department.name)
                .text(department.name)
                .attr('selected', 'selected');
            department3.append(optionElement);
        });

        $.each(data, function(index, department) {
            const optionElement = $('<option>')
                .val(department.name)
                .text(department.name);
            department4.append(optionElement);
        });
		return data;

    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

async function getPosition(){
	try {
		const response = await fetch('/allPositions')
		const data = await response.json();
		const position1 = $('#position');
		$.each(data, function (index, position) {
			const optionElement = $('<option>')
				.val(position.name)
				.text(position.name);
			position1.append(optionElement);
		});

		return data;

	}catch (error) {
		console.error('Error fetching data:', error);
	}
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
    try {
        let selectedDepartment = $("#department3").val();
        let selectedYear =  $("#year3").val();

        // console.log(selectedDepartment + 'sdd');
        // console.log(selectedYear + 'aa');
        // if (selectedDepartment === null) {
        //     selectedDepartment = 'All';
        // } else if (selectedYear === null) {
        //     selectedYear = 'All';
        // }
        const response = await fetch('/yearly-vacancy-count?timeSession=' + selectedYear + '&department=' + selectedDepartment);

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

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

async function fetchForPineChart() {
	const year = $("#pine").val();
	const month = $("#month").val();
	const position = $("#position").val();

	console.log('>>>>>' + year + '<<<<<' + position + '<<<<<<' + month);

	try {
		const response = await fetch('/chart?year='+year+'&month='+month+'&position='+position);

		const data = await response.json();
		// Do something with the data
		console.log(data)
		pchart.updateOptions({series:data,});
	} catch (error) {
		console.error('Error:', error);
	}
}



document.addEventListener('DOMContentLoaded', async function(){
	post4 = [];
	post5 = [];
	post6 = []
	totalVacancyCount = [];

	year = await getYear();
	department = await getDepartment()
	position = await getPosition()
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


	if(department !== undefined && year !== undefined && position !== undefined){

		let selectedYear =  $("#year1").val();
		console.log(selectedYear + "aa");
		fetchFor1stChart(selectedYear);
	}



	$('#year1').on('change', function () {
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

	if(department !== undefined && year !== undefined && position !== undefined){
		console.log('fetch3rd')
		fetchFor3rdChart();


	}

	$("#department3, #year3").change(fetchFor3rdChart);

	////////////////////////////////////////////

	pine =  options = {
		name:"Total",
		series: [0, 0, 0, 0, 0],
		chart: {
			width: 380,
			type: 'pie',
		},
		labels: ['Passed', 'Pending', 'Cancel', 'Not Interview', 'Accepted'],
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


	pchart = new ApexCharts(document.querySelector("#chart"), pine);
	pchart.render();

	if(department !== undefined && year !== undefined && position !== undefined){
		
		fetchForPineChart();


	}
	fetchForPineChart

	$("#pine,#position,#month").change(fetchForPineChart);


})




