(function() {
	"use strict";

	angular.module('public')
		.controller('SearchUsersComponentController', SearchUsersComponentController);

	SearchUsersComponentController.$inject = ['AppDataService', '$rootScope'];
	function SearchUsersComponentController(AppDataService, $rootScope) {

		/* Declare controller */

		var searchUsersCtrl = this;

		/* Declare functions */
		
		$rootScope.$on('userRemoved', function(){
			searchUsersCtrl.resetMessage();
		})

		searchUsersCtrl.search = function() {
			var promise = AppDataService.searchForUserByEmail(searchUsersCtrl.email);
			promise.then(function(result) {
				searchUsersCtrl.foundUser = result.object;
				searchUsersCtrl.message = result.message;
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

		searchUsersCtrl.addUser = function() {
			var user = searchUsersCtrl.foundUser;
			searchUsersCtrl.resetMessage();
			var promise = AppDataService.addUser(user);
			promise.then(function(result) {
				searchUsersCtrl.message = result.message;
				AppDataService.verifyUserAndMachine();
			});
		}
		
		searchUsersCtrl.clearResults = function(){
			searchUsersCtrl.foundUser = undefined;
		}

		searchUsersCtrl.resetMessage = function() {
			searchUsersCtrl.message = "null";
		}

		/* Run init code */

		searchUsersCtrl.resetMessage();
	}

})();