(function() {
	"use strict";

	angular.module('public')
		.controller('WaterSettingsComponentController', WaterSettingsComponentController);

	WaterSettingsComponentController.$inject = ['AppDataService'];
	function WaterSettingsComponentController(AppDataService) {
		var waterCtrl = this;
		
		waterCtrl.waterInValveOn = waterCtrl.machine.settings.water_in_valve_on;
		waterCtrl.waterCycleOn = waterCtrl.machine.settings.water_cycle_on;
		waterCtrl.waterCycleDuration = parseInt(waterCtrl.machine.settings.water_cycle_duration);
		waterCtrl.waterCyclePeriod = parseInt(waterCtrl.machine.settings.water_cycle_period);

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
		
		waterCtrl.getOnOrOffLabel = function(setting){
			if (waterCtrl.machine.settings[setting] == "1"){
				return "onLabel";
			} else {
				return "offLabel";
			}
			
		}
	}

})();