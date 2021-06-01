(function() {
	"use strict";

	angular.module('public')
		.controller('SelectedController', SelectedController);

	SelectedController.$inject = ['AppDataService', 'machine'];
	function SelectedController(AppDataService, machine) {

		var selectedCtrl = this;

		selectedCtrl.machine = machine;

		selectedCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
		
		AppDataService.verifyUserAndMachine();
	}


})();