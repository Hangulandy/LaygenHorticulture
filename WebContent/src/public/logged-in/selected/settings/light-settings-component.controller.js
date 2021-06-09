(function() {
	"use strict";

	angular.module('public')
		.controller('LightSettingsComponentController', LightSettingsComponentController);

	LightSettingsComponentController.$inject = ['AppDataService', '$scope'];
	function LightSettingsComponentController(AppDataService, $scope) {
		var lightCtrl = this;

		$scope.$watch('machine', function() {
			lightCtrl.clearChanges();
		});

		$scope.$on('clearMessages', function() {
			lightCtrl.message = "null";
			lightCtrl.clearChanges();
		});

		lightCtrl.getOnOrOffLabel = function(setting) {
			if (lightCtrl.machine.settings[setting] == "1") {
				return "onLabel";
			} else {
				return "offLabel";
			}
		}

		lightCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		lightCtrl.submitChanges = function() {
			var params = {
				action: "updateLightSettings",
				light_on: lightCtrl.lightOn,
				brightness: lightCtrl.brightness,
				light_color: lightCtrl.lightColor
			}

			var promise = AppDataService.submitSettings(params);
			promise.then(function(result) {
				lightCtrl.message = result.message;
			})
		}

		lightCtrl.submitCustomColorChanges = function() {
			
			var params = {
				action: "updateCustomColor",
				light_color: lightCtrl.lightColor,
				redValue: lightCtrl.newRedValue,
				greenValue: lightCtrl.newGreenValue,
				blueValue: lightCtrl.newBlueValue
			}

			var promise = AppDataService.submitSettings(params);
			promise.then(function(result) {
				lightCtrl.message = result.message;
			})
		}

		lightCtrl.brightnessSliderChanged = function() {
			var slider = document.getElementById("brightnessSlider");
			lightCtrl.brightness = parseInt(slider.value);
		}

		lightCtrl.redSliderChanged = function() {
			var slider = document.getElementById("redSlider");
			lightCtrl.newRedValue = parseInt(slider.value);
		}

		lightCtrl.greenSliderChanged = function() {
			var slider = document.getElementById("greenSlider");
			lightCtrl.newGreenValue = parseInt(slider.value);
		}

		lightCtrl.blueSliderChanged = function() {
			var slider = document.getElementById("blueSlider");
			lightCtrl.newBlueValue = parseInt(slider.value);
		}

		lightCtrl.changesDetected = function() {

			if (lightCtrl.lightOn != lightCtrl.machine.settings.light_on) {
				return true;
			}

			if (lightCtrl.brightness != lightCtrl.machine.settings.brightness) {
				return true;
			}

			if (lightCtrl.lightColor != lightCtrl.machine.settings.light_color) {
				return true;
			}

			return false;
		}

		lightCtrl.clearChanges = function() {
			lightCtrl.lightOn = lightCtrl.machine.settings.light_on;
			lightCtrl.brightness = parseInt(lightCtrl.machine.settings.brightness);
			lightCtrl.lightColor = lightCtrl.machine.settings.light_color;
			lightCtrl.orderedColors = lightCtrl.getColorsInOrder();
			lightCtrl.calculateCustomColors();
		}

		lightCtrl.customColorChangesDetected = function() {
			if (lightCtrl.redValue != lightCtrl.newRedValue) {
				return true;
			}

			if (lightCtrl.greenValue != lightCtrl.newGreenValue) {
				return true;
			}

			if (lightCtrl.blueValue != lightCtrl.newBlueValue) {
				return true;
			}
		}

		lightCtrl.clearCustomColorChanges = function() {
			lightCtrl.newRedValue = lightCtrl.redValue;
			lightCtrl.newGreenValue = lightCtrl.greenValue;
			lightCtrl.newBlueValue = lightCtrl.blueValue;
		}

		lightCtrl.calculateCustomColors = function() {
			lightCtrl.redValue = lightCtrl.newRedValue = lightCtrl.greenValue = lightCtrl.newGreenValue = lightCtrl.blueValue = lightCtrl.newBlueValue = 0;

			if (lightCtrl.lightColor.includes('custom')) {
				var tempVal = parseInt(lightCtrl.machine.lightColors[lightCtrl.lightColor]);

				lightCtrl.redValue = lightCtrl.newRedValue = Math.floor(tempVal / 1000000);
				tempVal = tempVal % 1000000;
				lightCtrl.greenValue = lightCtrl.newGreenValue = Math.floor(tempVal / 1000);
				lightCtrl.blueValue = lightCtrl.newBlueValue = tempVal % 1000;
			}
		}

		lightCtrl.checkColor = function() {
			if (lightCtrl.isCustom()) {
				lightCtrl.calculateCustomColors();
			}
		}

		lightCtrl.isCustom = function() {
			return lightCtrl.lightColor.includes('custom');
		}

		lightCtrl.getColorsInOrder = function() {
			var output = {};

			lightCtrl.colorOrder.forEach(key => {
				var value = lightCtrl.machine.lightColors[key]
				if (value !== undefined) {
					output[key] = value;
				}
			});

			return output;
		}

		lightCtrl.colorOrder = ['ld', 'red', 'green', 'blue', 'yellow', 'magenta', 'cyan', 'custom1', 'custom2', 'custom3'];

	}

})();