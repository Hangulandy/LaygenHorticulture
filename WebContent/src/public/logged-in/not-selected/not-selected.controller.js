(function() {
	"use strict";

	angular.module('public')
		.controller('NotSelectedController', NotSelectedController);

	NotSelectedController.$inject = ['AppDataService'];
	function NotSelectedController(AppDataService) {
		
		console.log(AppDataService.getUser());
		
		var notSelectedCtrl = this;
		
		notSelectedCtrl.authorizations = undefined;

		var promise = AppDataService.getAuthorizations();
		promise.then(function(result) {
			notSelectedCtrl.authorizations = result;
			console.log(notSelectedCtrl.authorizations);
		});
		
		console.log(AppDataService.getUser());

		notSelectedCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();