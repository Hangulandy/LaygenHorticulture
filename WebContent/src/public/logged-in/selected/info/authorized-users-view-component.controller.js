(function() {
	"use strict";

	angular.module('public')
		.controller('AuthorizedUsersViewComponentController', AuthorizedUsersViewComponentController);

	AuthorizedUsersViewComponentController.$inject = ['AppDataService', '$rootScope'];
	function AuthorizedUsersViewComponentController(AppDataService, $rootScope) {

		/* Declare controller */
		var authUsersCtrl = this;

		/* Declare functions */
		
		$rootScope.$on('machineStatusChanged', function(){
			authUsersCtrl.machine = AppDataService.getMachine();
		})
		
		$rootScope.$on('userAdded', function(){
			authUsersCtrl.resetMessage();
		})

		authUsersCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		authUsersCtrl.hasUsers = function() {
			// return authUsers.Ctrl.machine.authorizedUsers.length > 0;
			return true;
		}

		authUsersCtrl.canRevoke = function(user) {
			// Current user must be the owner
			// Listed user cannot be the owner
			var outcome = false;
			if (authUsersCtrl.machine.info.owner_email == AppDataService.getUser().email && user.email != AppDataService.getUser().email) {
				outcome = true;
			}
			return outcome;
		}

		authUsersCtrl.canMakeOwner = function(user) {
			// Currently, criteria are the same, but this may change in the future.
			return authUsersCtrl.canRevoke(user);
		}

		authUsersCtrl.revoke = function(user) {
			authUsersCtrl.resetMessage();
			var promise = AppDataService.removeUser(user);
			promise.then(function(result) {
				authUsersCtrl.message = result.message;
				authUsersCtrl.machine = AppDataService.getMachine();
				AppDataService.verifyUserAndMachine();
			});
		}

		authUsersCtrl.transfer = function(user) {
			authUsersCtrl.resetMessage();
			var promise = AppDataService.transferOwnership(user);
			promise.then(function(result){
				authUsersCtrl.message = result.message;
				authUsersCtrl.machine = AppDataService.getMachine();
				AppDataService.verifyUserAndMachine();
			});
		}

		authUsersCtrl.resetMessage = function() {
			authUsersCtrl.message = "null";
		}

		/* Run init code */

		authUsersCtrl.machine = AppDataService.getMachine();
		authUsersCtrl.resetMessage();
	}

})();