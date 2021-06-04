(function() {
	"use strict";

	angular.module('public')
		.controller('SettingsQuickViewComponentController', SettingsQuickViewComponentController);

	SettingsQuickViewComponentController.$inject = ['AppDataService', '$rootScope'];
	function SettingsQuickViewComponentController(AppDataService, $rootScope) {

		var settingsCtrl = this;

		$rootScope.$on('machineStatusChanged', function() {
			settingsCtrl.refresh();
		});

		settingsCtrl.getOpenedOrClosed = function(setting) {
			if (settingsCtrl.machine.settings[setting] == "1") {
				return "opened";
			} else {
				return "closed";
			}
		}

		settingsCtrl.getOnOrOffLabel = function(setting) {
			if (settingsCtrl.machine.settings[setting] == "1") {
				return "onLabel";
			} else {
				return "offLabel";
			}
		}

		settingsCtrl.getAutoOrContinuousLabel = function(setting) {
			if (settingsCtrl.machine.settings[setting] == "1") {
				return "auto";
			} else {
				return "cont";
			}
		}

		settingsCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		settingsCtrl.refresh = function() {
			settingsCtrl.machine = AppDataService.getMachine();
			settingsCtrl.waterCyclePeriod = parseInt(settingsCtrl.machine.settings.water_cycle_period);

			var tempTime = settingsCtrl.waterCyclePeriod;

			settingsCtrl.currentCycleHours = Math.floor(tempTime / 3600);
			tempTime = tempTime % 3600; // remove whole hours
			settingsCtrl.currentCycleMinutes = Math.floor(tempTime / 60);
		}

		settingsCtrl.refresh();
	}

})();