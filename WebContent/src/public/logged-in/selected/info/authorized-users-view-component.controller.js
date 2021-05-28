(function() {
	"use strict";

	angular.module('public')
		.controller('AuthorizedUsersViewComponentController', AuthorizedUsersViewComponentController);

	AuthorizedUsersViewComponentController.$inject = ['AppDataService'];
	function AuthorizedUsersViewComponentController(AppDataService) {

		var authUsersCtrl = this;

		authUsersCtrl.machine = AppDataService.getMachine();

		authUsersCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		authUsersCtrl.hasUsers = function() {
			// return authUsers.Ctrl.machine.authorizedUsers.length > 0;
			return true;
		}

		authUsersCtrl.canRevoke = function(user) {
			return true;
		}

		authUsersCtrl.canMakeOwner = function(user) {
			return true;
		}

		authUsersCtrl.revoke = function(user) {
			console.log("User to revoke : ", user);
		}

		authUsersCtrl.transfer = function(user) {
			console.log("User to make owner : ", user);
		}
	}

})();