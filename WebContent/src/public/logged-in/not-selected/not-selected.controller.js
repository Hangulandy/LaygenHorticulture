(function() {
	"use strict";

	angular.module('public')
		.controller('NotSelectedController', NotSelectedController);

	NotSelectedController.$inject = ['AppDataService', '$state', 'data'];
	function NotSelectedController(AppDataService, $state, data) {

		var notSelectedCtrl = this;
		notSelectedCtrl.authorizations = undefined;

		notSelectedCtrl.data = data;

		notSelectedCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		notSelectedCtrl.getMessage = function() {
			if (notSelectedCtrl.data.message === "") {
				return notSelectedCtrl.data.message;
			} else {
				return AppDataService.get(notSelectedCtrl.data.message);
			}
		}

		notSelectedCtrl.selectMachine = function(index) {
			// Use the index to get the sn
			var sn = notSelectedCtrl.data.user.authorizations[index].machineSerialNumber;

			// Use AppDataService to get the machine info
			var promise = AppDataService.fetchMachine(sn);
			promise.then(function() {
				var machine = AppDataService.getMachine();
				// Redirect to either the machine info view page or the machine list page
				if (machine != undefined) {
					$state.go('common.public.logged-in.selected.info');
				} else {
					$state.go('common.public.logged-in.not-selected');
				}
			});

			// TODO - Be sure to validate on the server side

		}
	}


})();