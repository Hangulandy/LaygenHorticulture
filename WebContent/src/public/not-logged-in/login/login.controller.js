(function() {
	"use strict";

	angular.module('public')
		.controller('LoginController', LoginController);

	LoginController.$inject = ['AppDataService'];
	function LoginController(AppDataService) {
		var loginCtrl = this;

		loginCtrl.data = {};

		// Should always logout the existing user when navigating to this page
		AppDataService.logout();

		loginCtrl.login = function() {
			loginCtrl.data.message = "";

			if (loginCtrl.email !== undefined && loginCtrl.password !== undefined) {
				var promise = AppDataService.login(loginCtrl.email, loginCtrl.password);
				promise.then(function(result) {
					loginCtrl.data = result;
					AppDataService.verifyUserAndMachine();
				});
			} else {
				console.log("No data to send to login");
			}

		}

		loginCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}



})();