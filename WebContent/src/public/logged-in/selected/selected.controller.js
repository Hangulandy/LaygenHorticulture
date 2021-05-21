(function() {
	"use strict";

	angular.module('public')
		.controller('SelectedController', SelectedController);

	SelectedController.$inject = ['AppDataService'];
	function SelectedController(AppDataService) {
		
		var selectedCtrl = this;
		
		selectedCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

	}


})();