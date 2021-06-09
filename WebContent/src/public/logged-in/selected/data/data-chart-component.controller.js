(function() {
	"use strict";

	angular.module('public')
		.controller('DataChartComponentController', DataChartComponentController);

	DataChartComponentController.$inject = ['AppDataService', '$scope'];
	function DataChartComponentController(AppDataService, $scope) {

		var chartCtrl = this;

		chartCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		$scope.$watch('chartCtrl.selected', function() {
			if (chartCtrl.selected) {
				chartCtrl.populateChart();
			}
		})

		chartCtrl.populateChart = function() {
			var id = chartCtrl.sensor.name;

			if (chartCtrl.sensor.readings != undefined) {
				var labels = chartCtrl.sensor.readings.map(function(reading) {
					var temp = parseInt(reading.rowId.split('-')[2]);
					var divisor = 10000000000;
					var year = Math.floor(temp / divisor);
					temp = temp % divisor;
					divisor = divisor / 100;
					var month = Math.floor(temp / divisor)
					temp = temp % divisor;
					divisor = divisor / 100;
					var day = Math.floor(temp / divisor)
					temp = temp % divisor;
					divisor = divisor / 100;
					var hour = Math.floor(temp / divisor);
					temp = temp % divisor;
					divisor = divisor / 100;
					var min = Math.floor(temp / divisor);
					var sec = temp % divisor;

					return new Date(year, month, day, hour, min, sec);

				});

				var data = chartCtrl.sensor.readings.map(function(reading) {
					return reading.value;
				});
			}

			var ctx = document.getElementById(id).getContext('2d');
			var config = {
				type: 'line', // bar, horizontalBar, pie, line, doughnut, radar
				data: {
					labels: labels,
					datasets: [{
						label: chartCtrl.get(chartCtrl.sensor.type),
						data: data,
					}]
				},
				options: {
					animation: {
						duration: 0,
					},
					scales: {
						x: {
							type: 'time',
						},
						xAxes: [{
							ticks: {
								autoSkip: true,
								maxTicksLimit: 20
							},
							display: true,
							type: 'time',
							time: {
								parser: 'YY/MM/DD HH:mm',
								tooltipFormat: 'll HH:mm',
								unit: 'day',
								unitStepSize: 1,
								displayFormats: {
									'day': 'YY/MM/DD'
								}
							},
							gridLines: {
								color: "rgba(0, 0, 0, 0)",
							}
						}],
					},
					showLines: false,
					title: {
						display: true,
						text: chartCtrl.get(chartCtrl.sensor.name) + " : " + chartCtrl.get(chartCtrl.sensor.type) + chartCtrl.sensor.units,
					},
					responsive: true,
					maintainAspectRatio: false,
				}
			};

			chartCtrl.chart = new Chart(ctx, config);
		}

		chartCtrl.hasData = function() {
			return chartCtrl.sensor.readings != undefined;
		}
	}


})();