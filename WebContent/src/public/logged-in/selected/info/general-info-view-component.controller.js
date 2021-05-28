(function() {
	"use strict";

	angular.module('public')
		.controller('GeneralInfoViewComponentController', GeneralInfoViewComponentController);

	GeneralInfoViewComponentController.$inject = ['AppDataService'];
	function GeneralInfoViewComponentController(AppDataService) {

		var genInfoCtrl = this;

		genInfoCtrl.machine = AppDataService.getMachine();

		genInfoCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();