(function() {
	"use strict";

	angular.module('public')
		.controller('LoggedInController', LoggedInController);

	LoggedInController.$inject = ['AppDataService', '$state'];
	function LoggedInController(AppDataService, $state) {
		var loggedInCtrl = this;

		loggedInCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		loggedInCtrl.logout = function() {
			AppDataService.logout();
			$state.go('public.not-logged-in.login');
		}

		loggedInCtrl.viewMyMachines = function() {
			AppDataService.resetMachine();
			$state.go('public.logged-in.not-selected');
		}
	}

})();