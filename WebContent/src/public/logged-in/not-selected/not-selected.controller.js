(function() {
	"use strict";

	angular.module('public')
		.controller('NotSelectedController', NotSelectedController);

	NotSelectedController.$inject = ['AppDataService', '$state'];
	function NotSelectedController(AppDataService, $state) {

		var notSelectedCtrl = this;
		notSelectedCtrl.authorizations = undefined;

		notSelectedCtrl.message = "";

		if (AppDataService.getUser() !== undefined) {
			var promise = AppDataService.getAuthorizations();
			promise.then(function(result) {
				notSelectedCtrl.data = result;
				notSelectedCtrl.message = notSelectedCtrl.data.message;
				notSelectedCtrl.authorizations = notSelectedCtrl.data.user.authorizations;
			});
		} else {
			$state.go('public.not-logged-in.login');
		}

		notSelectedCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		notSelectedCtrl.getMessage = function() {
			if (notSelectedCtrl.message === "") {
				return notSelectedCtrl.message;
			} else {
				return AppDataService.get(notSelectedCtrl.message);
			}
		}

		notSelectedCtrl.selectMachine = function(index) {
			// Use the index to get the sn
			var sn = notSelectedCtrl.authorizations[index].machineSerialNumber;

			// Use AppDataService to get the machine info
			var promise = AppDataService.fetchMachine(sn);
			promise.then(function() {
				var machine = AppDataService.getMachine();
				// Redirect to either the machine info view page or the machine list page
				if (machine !== undefined) {
					$state.go('public.logged-in.selected.info');
				} else {
					$state.go('public.logged-in.not-selected');
				}
			});

			// TODO - Be sure to validate on the server side

		}
	}


})();