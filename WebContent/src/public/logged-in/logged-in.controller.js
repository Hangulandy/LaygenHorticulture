(function() {
	"use strict";

	angular.module('public')
		.controller('LoggedInController', LoggedInController);

	LoggedInController.$inject = ['AppDataService', '$state'];
	function LoggedInController(AppDataService, $state) {
		var loggedInCtrl = this;
		
		loggedInCtrl.machine = null;

		loggedInCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
		
		loggedInCtrl.logout = function(){
			AppDataService.logout();
			$state.go('public.not-logged-in.login');
		}
		
		loggedInCtrl.viewMyMachines = function() {
			loggedInCtrl.machine = null;
			$state.go('public.logged-in.not-selected');
		}
	}

})();