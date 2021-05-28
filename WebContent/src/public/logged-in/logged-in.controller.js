(function() {
	"use strict";

	angular.module('public')
		.controller('LoggedInController', LoggedInController);

	LoggedInController.$inject = ['AppDataService'];
	function LoggedInController(AppDataService) {
		
		var loggedInCtrl = this;
		loggedInCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
		
		AppDataService.verifyUser();

	}

})();