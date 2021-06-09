(function() {
	"use strict";

	angular.module('public')
		.controller('GeneralInfoViewComponentController', GeneralInfoViewComponentController);

	GeneralInfoViewComponentController.$inject = ['AppDataService'];
	function GeneralInfoViewComponentController(AppDataService) {

		var genInfoCtrl = this;

		genInfoCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}		
	}

})();