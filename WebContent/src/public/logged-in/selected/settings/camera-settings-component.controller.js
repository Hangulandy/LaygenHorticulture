(function() {
	"use strict";

	angular.module('public')
		.controller('CameraSettingsComponentController', CameraSettingsComponentController);

	CameraSettingsComponentController.$inject = ['AppDataService', '$scope', '$rootScope'];
	function CameraSettingsComponentController(AppDataService, $scope, $rootScope) {
		var cameraCtrl = this;

		$rootScope.$on('machineStatusChanged', function() {
			cameraCtrl.refresh();
		})

		$scope.$watch('machine', function() {
			cameraCtrl.refresh();
		});

		$scope.$on('clearMessages', function() {
			cameraCtrl.message = "null";
		});


		cameraCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		cameraCtrl.getOnOrOffLabel = function(setting) {
			if (cameraCtrl.machine.settings[setting] == "1") {
				return "onLabel";
			} else {
				return "offLabel";
			}
		}

		cameraCtrl.changesDetected = function() {

			if (cameraCtrl.cameraCycleOn != cameraCtrl.machine.settings.camera_cycle_on) {
				return true;
			}

			if (cameraCtrl.newCycleHours != cameraCtrl.currentCycleHours) {
				return true;
			}

			if (cameraCtrl.newCycleMinutes != cameraCtrl.currentCycleMinutes) {
				return true;
			}

			return false;
		}

		cameraCtrl.submitChanges = function() {
			var period = (cameraCtrl.newCycleHours * 3600) + (cameraCtrl.newCycleMinutes * 60);

			var params = {
				camera_cycle_on: cameraCtrl.cameraCycleOn,
				camera_cycle_period: period,
				action: "updatecameraSettings"
			}

			var promise = AppDataService.submitSettings(params);
			promise.then(function(result) {
				cameraCtrl.message = result.message;
			})
		}

		cameraCtrl.refresh = function() {
			cameraCtrl.machine = AppDataService.getMachine();
			cameraCtrl.cameraCycleOn = cameraCtrl.machine.settings.camera_cycle_on;
			cameraCtrl.cameraCyclePeriod = parseInt(cameraCtrl.machine.settings.camera_cycle_period);

			var tempTime = cameraCtrl.cameraCyclePeriod;

			cameraCtrl.newCycleHours = cameraCtrl.currentCycleHours = Math.floor(tempTime / 3600);
			tempTime = tempTime % 3600; // remove whole hours
			cameraCtrl.newCycleMinutes = cameraCtrl.currentCycleMinutes = Math.floor(tempTime / 60);
		}

		cameraCtrl.refresh();
	}

})();