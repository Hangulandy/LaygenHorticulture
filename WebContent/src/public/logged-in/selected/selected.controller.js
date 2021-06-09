(function() {
	"use strict";

	angular.module('public')
		.controller('SelectedController', SelectedController);

	SelectedController.$inject = ['AppDataService', 'machine', '$rootScope'];
	function SelectedController(AppDataService, machine, $rootScope) {

		var selectedCtrl = this;

		selectedCtrl.machine = machine;

		selectedCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		$rootScope.$on('machineStatusUpdated', function() {
			if (AppDataService.getMachine() != undefined) {
				selectedCtrl.machine = AppDataService.getMachine();	
			}
		})
	}


})();