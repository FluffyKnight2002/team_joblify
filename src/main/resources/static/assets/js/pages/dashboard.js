let post4 ;
let post5;
let post6;
let post7;
let post8;
let post9;
let post10;
let post11;
let post12;
$(document).ready(function() {
				post4=[];
				post5=[];
				post6=[];
		
	fetch('/getYear')  .then(response => response.json())
  .then(data => {
data.forEach(yearArray => {
    const select = $('#year');
     const select1=$('#pine');
    const year = yearArray[0]; // Extract the year from the inner array

    const option = $('<option>').val(year).text(year);
    select.append(option);
    select1.append(option);
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
	fetch('/chart')  .then(response => response.json())
  .then(data => {
	  var pine = {
	series: [data.total, data.not, data.panding,data.interviewed,data.passed,data.cancel],
	labels:['Total Candidate','Not Interview','Panding','Interviewed','Passed','Cancel'],
     chart: {
		  width: 400,
          type: 'polarArea',
        },
        stroke: {
          colors: ['#fff']
        },
        fill: {
          opacity: 0.8
        },
          yaxis: {
    show: false,
   
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

        var pchart = new ApexCharts(document.querySelector("#chart"), pine);
        pchart.render();
  }
  
  )


	
})








var options = {
	series: [{
		name: "Session Duration",
		data: [45, 52, 38, 24, 33, 26, 21, 20, 6, 8, 15, 10]
	},
		{
			name: "Page Views",
			data: [35, 41, 62, 42, 13, 18, 29, 37, 36, 51, 32, 35]
		},
		{
			name: 'Total Visits',
			data: [87, 57, 74, 99, 75, 38, 62, 47, 82, 56, 45, 47]
		}
	],
	chart: {
		height: 350,
		type: 'line',
		zoom: {
			enabled: false
		},
	},
	dataLabels: {
		enabled: false
	},
	stroke: {
		width: [5, 7, 5],
		curve: 'straight',
		dashArray: [0, 8, 5]
	},
	title: {
		text: 'Page Statistics',
		align: 'left'
	},
	legend: {
		tooltipHoverFormatter: function(val, opts) {
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
		categories: ['01 Jan', '02 Jan', '03 Jan', '04 Jan', '05 Jan', '06 Jan', '07 Jan', '08 Jan', '09 Jan',
			'10 Jan', '11 Jan', '12 Jan'
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

var graph = new ApexCharts(document.querySelector("#graph"), options);
graph.render();
