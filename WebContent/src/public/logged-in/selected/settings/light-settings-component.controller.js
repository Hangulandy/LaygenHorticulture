(function() {
	"use strict";

	angular.module('public')
		.controller('LightSettingsComponentController', LightSettingsComponentController);

	LightSettingsComponentController.$inject = ['AppDataService'];
	function LightSettingsComponentController(AppDataService) {
		var lightCtrl = this;

		lightCtrl.machine = AppDataService.getMachine();

		lightCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();