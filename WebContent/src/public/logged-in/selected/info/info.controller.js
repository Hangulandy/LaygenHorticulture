(function() {
	"use strict";

	angular.module('public')
		.controller('InfoController', InfoController)
		.directive('settingsQuickView', SettingsQuickViewDirective)
		.directive('machineDataQuickView', MachineDataQuickViewDirective)
		.directive('generalInfoView', GeneralInfoViewDirective)
		.directive('authorizedUsersView', AuthorizedUsersViewDirective)
		.directive('searchUsers', SearchUsersDirective);

	function SettingsQuickViewDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/info/settings-quick-view.template.html',
			controller: SettingsQuickViewController,
			controllerAs: 'settingsCtrl'
		};
		return ddo;
	}

	SettingsQuickViewController.$inject = ['AppDataService'];
	function SettingsQuickViewController(AppDataService) {

		var settingsCtrl = this;

		settingsCtrl.machine = AppDataService.getMachine();

		settingsCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

	function MachineDataQuickViewDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/info/data-quick-view.template.html',
			controller: DataQuickViewController,
			controllerAs: 'dataCtrl'
		};
		return ddo;
	}

	DataQuickViewController.$inject = ['AppDataService'];
	function DataQuickViewController(AppDataService) {

		var dataCtrl = this;

		dataCtrl.machine = AppDataService.getMachine();

		dataCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

	function GeneralInfoViewDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/info/general-info-view.template.html',
			controller: GeneralInfoViewController,
			controllerAs: 'genInfoCtrl'
		};
		return ddo;
	}

	GeneralInfoViewController.$inject = ['AppDataService'];
	function GeneralInfoViewController(AppDataService) {

		var genInfoCtrl = this;

		genInfoCtrl.machine = AppDataService.getMachine();

		genInfoCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

	function AuthorizedUsersViewDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/info/authorized-users-view.template.html',
			controller: AuthorizedUsersViewController,
			controllerAs: 'authUsersCtrl'
		};
		return ddo;
	}

	AuthorizedUsersViewController.$inject = ['AppDataService'];
	function AuthorizedUsersViewController(AppDataService) {

		var authUsersCtrl = this;

		authUsersCtrl.machine = AppDataService.getMachine();

		authUsersCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		authUsersCtrl.hasUsers = function() {
			// return authUsers.Ctrl.machine.authorizedUsers.length > 0;
			return true;
		}
	}

	function SearchUsersDirective() {
		var ddo = {
			templateUrl: 'src/public/logged-in/selected/info/search-users.template.html',
			controller: SearchUsersController,
			controllerAs: 'searchUsersCtrl'
		};
		return ddo;
	}
	
	SearchUsersController.$inject = ['AppDataService'];
	function SearchUsersController(AppDataService){

		var searchUsersCtrl = this;

		searchUsersCtrl.machine = AppDataService.getMachine();
		
		searchUsersCtrl.search = function(){
			console.log(searchUsersCtrl.email);
		}

		searchUsersCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}


	InfoController.$inject = ['AppDataService', 'machine'];
	function InfoController(AppDataService, machine) {

		var infoCtrl = this;

		infoCtrl.machine = machine;

		infoCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}


})();