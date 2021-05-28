(function() {
	"use strict";

	angular.module('public')
		.controller('SettingsQuickViewComponentController', SettingsQuickViewComponentController);

	SettingsQuickViewComponentController.$inject = ['AppDataService'];
	function SettingsQuickViewComponentController(AppDataService) {

		var settingsCtrl = this;

		settingsCtrl.machine = AppDataService.getMachine();

		settingsCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();