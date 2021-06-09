(function() {
	"use strict";

	angular.module('public')
		.controller('SettingsController', SettingsController)
		.component('growSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/grow-settings-component.template.html',
			controller: 'GrowSettingsComponentController as growCtrl',
			bindings: {
				machine: '<'
			}
		})
		.component('waterSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/water-settings-component.template.html',
			controller: 'WaterSettingsComponentController as waterCtrl',
			bindings: {
				machine: '<'
			}
		})
		.component('lightSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/light-settings-component.template.html',
			controller: 'LightSettingsComponentController as lightCtrl',
			bindings: {
				machine: '<'
			}
		})
		.component('airSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/air-settings-component.template.html',
			controller: 'AirSettingsComponentController as airCtrl',
			bindings: {
				machine: '<'
			}
		})
		.component('cameraSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/camera-settings-component.template.html',
			controller: 'CameraSettingsComponentController as cameraCtrl',
			bindings: {
				machine: '<'
			}
		});


	SettingsController.$inject = ['AppDataService', '$scope', '$rootScope'];
	function SettingsController(AppDataService, $scope, $rootScope) {

		var settingsCtrl = this;

		$rootScope.$on('machineStatusChanged', function() {
			if (AppDataService.getMachine() != undefined) {
				settingsCtrl.machine = AppDataService.getMachine();
			}
		});

		settingsCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		settingsCtrl.refreshSettings = function() {
			var promise = AppDataService.refreshMachineInfo();
			promise.then(function(result) {
				settingsCtrl.machine = result.object;
				$scope.$broadcast('clearMessages');
			});
		}

		settingsCtrl.getMachine = function() {
			settingsCtrl.machine = AppDataService.getMachine();
		}

		settingsCtrl.getMachine();
	}


})();