(function() {
	"use strict";

	angular.module('public')
		.controller('CameraSettingsComponentController', CameraSettingsComponentController);

	CameraSettingsComponentController.$inject = ['AppDataService'];
	function CameraSettingsComponentController(AppDataService) {
		var cameraCtrl = this;

		cameraCtrl.machine = AppDataService.getMachine();

		cameraCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();