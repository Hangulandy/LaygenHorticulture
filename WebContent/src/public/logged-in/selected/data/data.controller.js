(function() {
	"use strict";

	angular.module('public')
		.controller('DataController', DataController)
		.component('singleChart', {
			templateUrl: 'src/public/logged-in/selected/data/data-chart-component.template.html',
			controller: 'DataChartComponentController as chartCtrl',
			bindings: {
				sensor: '<',
				selected: '<'
			}
		});

	DataController.$inject = ['AppDataService', '$rootScope'];
	function DataController(AppDataService, $rootScope) {

		var dataCtrl = this;

		$rootScope.$on('machineStatusChanged', function() {
			if (AppDataService.getMachine() != undefined) {
				dataCtrl.refresh();
				console.log("Machine status changed");
			}
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

			var promise = AppDataService.standardGet(params);
			promise.then(function(result) {
				// What to do here?
			});

		}

		dataCtrl.refresh = function() {
			dataCtrl.machine = AppDataService.getMachine();
			dataCtrl.sensorIsSelected = dataCtrl.getSelectedSensorsFromStorage();

			dataCtrl.sensorIsSelected = dataCtrl.sensorIsSelected === undefined ? {} : dataCtrl.sensorIsSelected;

			for (const [key, value] of Object.entries(dataCtrl.machine.sensors)) {
				dataCtrl.sensorIsSelected[key] = false;
			}

			var plantDate = dataCtrl.machine.settings.plant_date;

			dataCtrl.minDate = AppDataService.isValidDate(plantDate) != undefined ? plantDate : "2021-01-01";
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

		dataCtrl.saveSensors = function() {
			AppDataService.setInStorage("sensorIsSelected", dataCtrl.sensorIsSelected);
		}

		dataCtrl.getSelectedSensorsFromStorage = function() {
			AppDataService.getFromStorage("sensorIsSelected");
		}

		dataCtrl.getSelectedSensors = function() {
			var output = {};
			for (const [key, value] of Object.entries(dataCtrl.sensorIsSelected)) {
				if (dataCtrl.sensorIsSelected[key]) {
					output[key] = dataCtrl.machine.sensors[key];
				}
			}
			return output;
		}

		dataCtrl.hasData = function(sensor) {
			return dataCtrl.machine.sensors[sensor].readings != undefined;
		}

		dataCtrl.isSelected = function(sensorName) {
			return dataCtrl.sensorIsSelected[sensorName];
		}

		dataCtrl.refresh();
	}

})();