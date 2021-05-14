(function() {
	"use strict";

	angular.module('public')
		.controller('LoginController', LoginController);

	LoginController.$inject = ['AppDataService', '$state'];
	function LoginController(AppDataService, $state) {
		var loginCtrl = this;

		loginCtrl.login = function() {
			var promise = AppDataService.login(loginCtrl.email, loginCtrl.password);
			promise.then(function(result) {
				loginCtrl.data = result;
				AppDataService.setUser(loginCtrl.data.user);
				console.log(loginCtrl.data.object);
				if (loginCtrl.data.user === undefined){
					$state.go('public.join')
				}
			});
		}
	}



})();