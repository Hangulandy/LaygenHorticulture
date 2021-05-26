(function() {
	"use strict";

	angular.module('public')
		.controller('SettingsController', SettingsController)
		.directive('growSettings', GrowSettingsDirective)
		.directive('waterSettings', WaterSettingsDirective)
		.directive('lightSettings', LightSettingsDirective)
		.directive('airSettings', AirSettingsDirective)
		.directive('cameraSettings', CameraSettingsDirective);

	function GrowSettingsDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/settings/grow-settings.template.html',
			controller: GrowSettingsDirectiveController,
			controllerAs: 'growCtrl'
		};
		return ddo;
	}

	GrowSettingsDirectiveController.$inject = ['AppDataService'];
	function GrowSettingsDirectiveController(AppDataService) {
		var growCtrl = this;

		growCtrl.machine = AppDataService.getMachine();
		
		console.log(growCtrl.machine);
		
		growCtrl.plant_date = new Date(growCtrl.machine.settings.plant_date);

		growCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
		
		growCtrl.getDate = function(){
			return "2021-04-01".toString();
		}
	}

	function WaterSettingsDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/settings/water-settings.template.html',
			controller: WaterSettingsDirectiveController,
			controllerAs: 'waterCtrl'
		};
		return ddo;
	}

	WaterSettingsDirectiveController.$inject = ['AppDataService'];
	function WaterSettingsDirectiveController(AppDataService) {
		var waterCtrl = this;

		waterCtrl.machine = AppDataService.getMachine();

		waterCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

	function LightSettingsDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/settings/light-settings.template.html',
			controller: LightSettingsDirectiveController,
			controllerAs: 'lightCtrl'
		};
		return ddo;
	}

	LightSettingsDirectiveController.$inject = ['AppDataService'];
	function LightSettingsDirectiveController(AppDataService) {
		var lightCtrl = this;

		lightCtrl.machine = AppDataService.getMachine();

		lightCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

	function AirSettingsDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/settings/air-settings.template.html',
			controller: AirSettingsDirectiveController,
			controllerAs: 'airCtrl'
		};
		return ddo;
	}

	AirSettingsDirectiveController.$inject = ['AppDataService'];
	function AirSettingsDirectiveController(AppDataService) {
		var airCtrl = this;

		airCtrl.machine = AppDataService.getMachine();

		airCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

	function CameraSettingsDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/settings/camera-settings.template.html',
			controller: CameraSettingsDirectiveController,
			controllerAs: 'cameraCtrl'
		};
		return ddo;
	}

	CameraSettingsDirectiveController.$inject = ['AppDataService'];
	function CameraSettingsDirectiveController(AppDataService) {
		var cameraCtrl = this;

		cameraCtrl.machine = AppDataService.getMachine();

		cameraCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

	SettingsController.$inject = ['AppDataService'];
	function SettingsController(AppDataService) {

		var settingsCtrl = this;

		settingsCtrl.machine = AppDataService.getMachine();

		settingsCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

	}


})();