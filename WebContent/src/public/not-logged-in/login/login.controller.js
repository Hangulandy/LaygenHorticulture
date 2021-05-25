(function() {
	"use strict";

	angular.module('public')
		.controller('LoginController', LoginController);

	LoginController.$inject = ['AppDataService', '$state'];
	function LoginController(AppDataService, $state) {
		var loginCtrl = this;

		loginCtrl.message = "";

		// Should always logout the existing user when navigating to this page
		AppDataService.logout();

		loginCtrl.login = function() {
			loginCtrl.message = "";
			console.log(loginCtrl.email, loginCtrl.password);

			if (loginCtrl.email !== undefined && loginCtrl.password !== undefined) {
				var promise = AppDataService.login(loginCtrl.email, loginCtrl.password);
				promise.then(function(result) {
					loginCtrl.data = result;
					AppDataService.setUser(loginCtrl.data.user);
					loginCtrl.message = loginCtrl.data.message;
					if (loginCtrl.data.user === undefined) {
						$state.go('public.not-logged-in.login');
					} else {
						$state.go('public.logged-in.not-selected');
					}
				});
			} else {
				console.log("No data to send to login");
			}

		}

		loginCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		loginCtrl.getMessage = function() {
			if (loginCtrl.message === "") {
				return loginCtrl.message;
			} else {
				return AppDataService.get(loginCtrl.message);
			}
		}
	}



})();