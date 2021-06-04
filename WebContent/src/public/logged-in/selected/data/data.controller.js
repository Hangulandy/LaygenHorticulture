(function() {
	"use strict";

	angular.module('public')
		.controller('DataController', DataController)
		.component('singleChart', {
			templateUrl: 'src/public/logged-in/selected/data/data-chart-component.template.html',
			controller: 'DataChartComponentController as chartCtrl',
			bindings: {
				sensor: '<'
			}
		});
		
	DataController.$inject = ['AppDataService', '$rootScope'];
	function DataController(AppDataService, $rootScope) {

		var dataCtrl = this;

		$rootScope.$on('machineStatusChanged', function() {
			dataCtrl.refresh();
		});

		dataCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		dataCtrl.submit = function() {
			var startDate = document.getElementById('startDate').value;
			var startTime = document.getElementById('startTime').value;

			var endDate = document.getElementById('endDate').value;
			var endTime = document.getElementById('endTime').value;

			var params = {
				action: "viewMachineData",
				startDate: startDate,
				startTime: startTime,
				endDate: endDate,
				endTime: endTime
			}

			var promise = AppDataService.submitSettings(params);
			promise.then(function(result) {
				console.log(result);
			});

		}

		dataCtrl.refresh = function() {
			dataCtrl.machine = AppDataService.getMachine();

			dataCtrl.minDate = "2021-01-01";
			var startDate = dataCtrl.machine.startDate;
			startDate = AppDataService.isValidDate(startDate) ? startDate : dataCtrl.minDate;
			var startDatePicker = document.getElementById('startDate');
			if (startDatePicker != null) {
				startDatePicker.value = startDate;
			}

			dataCtrl.maxDate = "2030-12-31";
			var endDate = dataCtrl.machine.endDate;
			endDate = AppDataService.isValidDate(endDate) ? endDate : dataCtrl.maxDate;
			var endDatePicker = document.getElementById('endDate');
			if (endDatePicker != null) {
				endDatePicker.value = endDate;
			}

			dataCtrl.minTime = "00:00";
			var startTime = dataCtrl.machine.startTime;
			startTime = AppDataService.isValidTime(startTime) ? startTime : dataCtrl.minTime;
			var startTimePicker = document.getElementById('startTime');
			if (startTimePicker != null) {
				startTimePicker.value = startTime;
			}

			dataCtrl.maxTime = "23:59";
			var endTime = dataCtrl.machine.endTime;
			endTime = AppDataService.isValidTime(endTime) ? endTime : dataCtrl.maxTime;
			var endTimePicker = document.getElementById('endTime');
			if (endTimePicker != null) {
				endTimePicker.value = endTime;
			}
		}

		dataCtrl.refresh();
	}

})();