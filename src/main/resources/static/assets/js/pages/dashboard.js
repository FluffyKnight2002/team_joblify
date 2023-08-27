let post4 ;
let post5;
let post6;
$(document).ready(function() {
				post4=[];
				post5=[];
				post6=[]
		const currentDate = new Date();
		const currentYear = currentDate.getFullYear();
	fetch('/getYear')  .then(response => response.json())
  .then(data => {
data.forEach(yearArray => {
    const select = $('#year'); // Assuming you have an element with id 'year' for your select element
    const year = yearArray[0]; // Extract the year from the inner array

    const option = $('<option>').val(year).text(year);
    select.append(option);
});
  })
  .catch(error => {
    console.error('Error fetching data:', error);
  });
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
				categories:["Jan","Feb","Mar","Apr","May","Jun","Jul", "Aug","Sep","Oct","Nov","Dec"],
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
					formatter: function(val) {
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
		var chartProfileVisit = new ApexCharts(document.querySelector("#chart-profile-visit"), optionsProfileVisit);
		chartProfileVisit.render();
$('#year').on('change',function(){
	 const selectedValue = $(this).val();
	
	 console.log('neww>>>>>>>>>>>>',post4,post5,post6)
		fetch('/dashboard?timeSession=' + selectedValue, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json;charset=utf-8', // Add this line
				[csrfHeader]: csrfToken
			},
		})
			.then(response => response.json())
			.then(data => {
				
    post4=[];
	 post5=[];
	 post6=[];

		$.each(data, function(index, post){
			
			post4.push(post.postTotal);
			post5.push(post.totalApplied);
			post6.push(post.hiredPost);
			
			console.log('oddd>>>>>>>>>>>>',post4,post5,post6)
		});
		
	 chartProfileVisit.updateSeries([
                { name: 'Total Post', data: post4 },
                { name: 'Total Candidate', data: post5 },
                { name: 'Hired', data: post6 }
            ]);


	})
}

	)
})


let optionsVisitorsProfile  = {
	series: [70, 30],
	labels: ['Male', 'Female'],
	colors: ['#435ebe','#55c6e8'],
	chart: {
		type: 'donut',
		width: '100%',
		height:'350px'
	},
	legend: {
		position: 'bottom'
	},
	plotOptions: {
		pie: {
			donut: {
				size: '30%'
			}
		}
	}
}

var optionsEurope = {
	series: [{
		name: 'series1',
		data: [310, 800, 600, 430, 540, 340, 605, 805,430, 540, 340, 605]
	}],
	chart: {
		height: 80,
		type: 'area',
		toolbar: {
			show:false,
		},
	},
	colors: ['#5350e9'],
	stroke: {
		width: 2,
	},
	grid: {
		show:false,
	},
	dataLabels: {
		enabled: false
	},
	xaxis: {
		type: 'datetime',
		categories: ["2018-09-19T00:00:00.000Z", "2018-09-19T01:30:00.000Z", "2018-09-19T02:30:00.000Z", "2018-09-19T03:30:00.000Z", "2018-09-19T04:30:00.000Z", "2018-09-19T05:30:00.000Z", "2018-09-19T06:30:00.000Z","2018-09-19T07:30:00.000Z","2018-09-19T08:30:00.000Z","2018-09-19T09:30:00.000Z","2018-09-19T10:30:00.000Z","2018-09-19T11:30:00.000Z"],
		axisBorder: {
			show:false
		},
		axisTicks: {
			show:false
		},
		labels: {
			show:false,
		}
	},
	show:false,
	yaxis: {
		labels: {
			show:false,
		},
	},
	tooltip: {
		x: {
			format: 'dd/MM/yy HH:mm'
		},
	},
};

let optionsAmerica = {
	...optionsEurope,
	colors: ['#008b75'],
}
let optionsIndonesia = {
	...optionsEurope,
	colors: ['#dc3545'],
}




var chartVisitorsProfile = new ApexCharts(document.getElementById('chart-visitors-profile'), optionsVisitorsProfile)
var chartEurope = new ApexCharts(document.querySelector("#chart-europe"), optionsEurope);
var chartAmerica = new ApexCharts(document.querySelector("#chart-america"), optionsAmerica);
var chartIndonesia = new ApexCharts(document.querySelector("#chart-indonesia"), optionsIndonesia);


chartIndonesia.render();
chartAmerica.render();
chartEurope.render();

chartVisitorsProfile.render()
