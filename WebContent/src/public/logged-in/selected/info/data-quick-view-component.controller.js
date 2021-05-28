(function() {
	"use strict";

	angular.module('public')
		.controller('DataQuickViewComponentController', DataQuickViewComponentController);

	DataQuickViewComponentController.$inject = ['AppDataService'];
	function DataQuickViewComponentController(AppDataService) {

		var dataCtrl = this;

		dataCtrl.machine = AppDataService.getMachine();

		dataCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();