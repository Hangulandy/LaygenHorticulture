(function() {
	"use strict";

	angular.module('public')
		.controller('SearchUsersComponentController', SearchUsersComponentController);

	SearchUsersComponentController.$inject = ['AppDataService'];
	function SearchUsersComponentController(AppDataService) {

		var searchUsersCtrl = this;

		searchUsersCtrl.hasSearched = false;

		searchUsersCtrl.machine = AppDataService.getMachine();

		searchUsersCtrl.search = function() {
			var promise = AppDataService.searchForUserByEmail(searchUsersCtrl.email);
			promise.then(function(result) {
				searchUsersCtrl.foundUser = result.object;
				searchUsersCtrl.hasSearched = true;
			});
		}

		searchUsersCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		searchUsersCtrl.userIsOwner = function() {
			var user = AppDataService.getUser();
			return user.email == searchUsersCtrl.machine.info.owner_email;
		}

		searchUsersCtrl.userIsAuthorized = function(user) {
			return AppDataService.userIsAuthorized(user);
		}
		
		searchUsersCtrl.addUser = function(){
			console.log("User to add : ", searchUsersCtrl.foundUser);
		}
	}

})();