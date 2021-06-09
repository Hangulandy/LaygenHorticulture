(function() {
	"use strict";

	angular.module('public')
		.controller('InfoController', InfoController)
		.component('settingsQuickView', {
			templateUrl: 'src/public/logged-in/selected/info/settings-quick-view-component.template.html',
			controller: 'SettingsQuickViewComponentController as settingsCtrl',
			bindings: {
				machine: '<'
			}
		})
		.component('machineDataQuickView', {
			templateUrl: 'src/public/logged-in/selected/info/data-quick-view-component.template.html',
			controller: 'DataQuickViewComponentController as dataCtrl',
			bindings: {
				machine: '<'
			}
		})
		.component('generalInfoView', {
			templateUrl: 'src/public/logged-in/selected/info/general-info-view-component.template.html',
			controller: 'GeneralInfoViewComponentController as genInfoCtrl',
			bindings: {
				machine: '<'
			}
		})
		.component('authorizedUsersView', {
			templateUrl: 'src/public/logged-in/selected/info/authorized-users-view-component.template.html',
			controller: 'AuthorizedUsersViewComponentController as authUsersCtrl',
			bindings: {
				machine: '<'
			}
		});

	InfoController.$inject = ['AppDataService', 'machine', '$rootScope'];
	function InfoController(AppDataService, machine, $rootScope) {

		var infoCtrl = this;

		$rootScope.$on('machineStatusChanged', function() {
			if (AppDataService.getMachine() != undefined) {
				infoCtrl.machine = AppDataService.getMachine();
			}
		});

		infoCtrl.machine = machine;

		infoCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();