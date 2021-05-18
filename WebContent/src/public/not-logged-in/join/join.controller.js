(function() {
	"use strict";

	angular.module('public')
		.controller('JoinController', JoinController);

	JoinController.$inject = ['AppDataService', '$state'];
	function JoinController(AppDataService, $state) {
		var joinCtrl = this;

		joinCtrl.message = "";

		joinCtrl.join = function() {
			joinCtrl.message = "";
			console.log(joinCtrl.email, joinCtrl.name, joinCtrl.username, joinCtrl.organization, joinCtrl.pw1, joinCtrl.pw2);

			var promise = AppDataService.join(joinCtrl.email, joinCtrl.name, joinCtrl.username, joinCtrl.organization, joinCtrl.pw1, joinCtrl.pw2);
			promise.then(function(result) {
				joinCtrl.data = result;
				joinCtrl.message = joinCtrl.data.message;
				console.log(joinCtrl.data.object);
				if (joinCtrl.data.user === undefined) {
					$state.go('public.not-logged-in.join');
				} else {
					$state.go('public.not-logged-in.login');
				}
			});
		}

		joinCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		joinCtrl.getMessage = function() {
			if (joinCtrl.message === "") {
				return joinCtrl.message;
			} else {
				return AppDataService.get(joinCtrl.message);
			}
		}
	}

})();