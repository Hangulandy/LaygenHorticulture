(function() {
	"use strict";

	angular.module('public')
		.controller('WaterSettingsComponentController', WaterSettingsComponentController);

	WaterSettingsComponentController.$inject = ['AppDataService', '$scope', '$rootScope'];
	function WaterSettingsComponentController(AppDataService, $scope, $rootScope) {
		var waterCtrl = this;

		$rootScope.$on('machineStatusChanged', function() {
			waterCtrl.refresh();
		})

		$scope.$watch('machine', function() {
			waterCtrl.refresh();
		});

		$scope.$on('clearMessages', function() {
			waterCtrl.message = "null";
		});

		waterCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		waterCtrl.getOpenedOrClosed = function(setting) {
			if (waterCtrl.machine.settings[setting] == "1") {
				return "opened";
			} else {
				return "closed";
			}
		}

		waterCtrl.getOnOrOffLabel = function(setting) {
			if (waterCtrl.machine.settings[setting] == "1") {
				return "onLabel";
			} else {
				return "offLabel";
			}
		}

		waterCtrl.changesDetected = function() {

			if (waterCtrl.waterInValveOn != waterCtrl.machine.settings.water_in_valve_on) {
				return true;
			}

			if (waterCtrl.waterCycleOn != waterCtrl.machine.settings.water_cycle_on) {
				return true;
			}

			if (waterCtrl.waterCycleDuration != parseInt(waterCtrl.machine.settings.water_cycle_duration)) {
				return true;
			}

			if (waterCtrl.newCycleHours != waterCtrl.currentCycleHours) {
				return true;
			}

			if (waterCtrl.newCycleMinutes != waterCtrl.currentCycleMinutes) {
				return true;
			}

			return false;
		}

		waterCtrl.submitChanges = function() {
			var period = (waterCtrl.newCycleHours * 3600) + (waterCtrl.newCycleMinutes * 60);

			var params = {
				water_in_valve_on: waterCtrl.waterInValveOn,
				water_cycle_on: waterCtrl.waterCycleOn,
				water_cycle_duration: waterCtrl.waterCycleDuration,
				water_cycle_period: period,
				action: "updateWaterSettings"
			}

			var promise = AppDataService.submitSettings(params);
			promise.then(function(result) {
				waterCtrl.message = result.message;
			})
		}

		waterCtrl.refresh = function() {
			waterCtrl.machine = AppDataService.getMachine();
			waterCtrl.waterInValveOn = waterCtrl.machine.settings.water_in_valve_on;
			waterCtrl.waterCycleOn = waterCtrl.machine.settings.water_cycle_on;
			waterCtrl.waterCycleDuration = parseInt(waterCtrl.machine.settings.water_cycle_duration);
			waterCtrl.waterCyclePeriod = parseInt(waterCtrl.machine.settings.water_cycle_period);

			var tempTime = waterCtrl.waterCyclePeriod;

			waterCtrl.newCycleHours = waterCtrl.currentCycleHours = Math.floor(tempTime / 3600);
			tempTime = tempTime % 3600; // remove whole hours
			waterCtrl.newCycleMinutes = waterCtrl.currentCycleMinutes = Math.floor(tempTime / 60);
		}
		
		waterCtrl.refresh();

	}

})();