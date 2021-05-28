(function() {
	"use strict";

	angular.module('public')
		.controller('SettingsController', SettingsController)
		.component('growSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/grow-settings-component.template.html',
			controller: 'GrowSettingsComponentController as growCtrl'
		})
		.component('waterSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/water-settings-component.template.html',
			controller: 'WaterSettingsComponentController as waterCtrl',
		})
		.component('lightSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/light-settings-component.template.html',
			controller: 'LightSettingsComponentController as lightCtrl',
		})
		.component('airSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/air-settings-component.template.html',
			controller: 'AirSettingsComponentController as airCtrl',
		})
		.component('cameraSettings', {
			templateUrl: 'src/public/logged-in/selected/settings/camera-settings-component.template.html',
			controller: 'CameraSettingsComponentController as cameraCtrl',
		});


	SettingsController.$inject = ['AppDataService'];
	function SettingsController(AppDataService) {

		var settingsCtrl = this;

		settingsCtrl.machine = AppDataService.getMachine();

		settingsCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

	}


})();