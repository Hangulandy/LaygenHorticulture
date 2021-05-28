(function() {
	"use strict";

	angular.module('public')
		.controller('AirSettingsComponentController', AirSettingsComponentController);

	AirSettingsComponentController.$inject = ['AppDataService'];
	function AirSettingsComponentController(AppDataService) {
		var airCtrl = this;

		airCtrl.machine = AppDataService.getMachine();

		airCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();