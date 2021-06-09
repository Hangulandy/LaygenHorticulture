(function() {
	"use strict";

	angular.module('public')
		.controller('AirSettingsComponentController', AirSettingsComponentController);

	AirSettingsComponentController.$inject = ['AppDataService', '$scope'];
	function AirSettingsComponentController(AppDataService, $scope) {
		var airCtrl = this;

		$scope.$watch('machine', function() {
			airCtrl.clearChanges();
		});

		$scope.$on('clearMessages', function() {
			airCtrl.message = "null";
			airCtrl.clearChanges();
		});

		airCtrl.humiditySliderChanged = function() {
			var slider = document.getElementById("humiditySlider");
			airCtrl.fanHumidity = parseInt(slider.value);
		}

		airCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		airCtrl.getOnOrOffLabel = function(setting) {
			if (airCtrl.machine.settings[setting] == "1") {
				return "onLabel";
			} else {
				return "offLabel";
			}
		}

		airCtrl.getAutoOrContinuousLabel = function(setting) {
			if (airCtrl.machine.settings[setting] == "1") {
				return "auto";
			} else {
				return "cont";
			}
		}

		airCtrl.changesDetected = function() {

			if (airCtrl.fanOn != airCtrl.machine.settings.fan_on) {
				return true;
			}

			if (airCtrl.fanAuto != airCtrl.machine.settings.fan_auto) {
				return true;
			}

			if (airCtrl.fanHumidity != airCtrl.machine.settings.fan_humidity) {
				return true;
			}

			return false;
		}

		airCtrl.clearChanges = function() {
			airCtrl.fanOn = airCtrl.machine.settings.fan_on;
			airCtrl.fanAuto = airCtrl.machine.settings.fan_auto;
			airCtrl.fanHumidity = parseInt(airCtrl.machine.settings.fan_humidity);
		}

		airCtrl.submitChanges = function() {
			var params = {
				action: "updateAirSettings",
				fan_on: airCtrl.fanOn,
				fan_auto: airCtrl.fanAuto,
				fan_humidity: airCtrl.fanHumidity
			}

			var promise = AppDataService.submitSettings(params);
			promise.then(function(result) {
				airCtrl.message = result.message;
			})
		}
	}

})();