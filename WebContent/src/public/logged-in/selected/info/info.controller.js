(function() {
	"use strict";

	angular.module('public')
		.controller('InfoController', InfoController)
		.component('settingsQuickView', {
			templateUrl: 'src/public/logged-in/selected/info/settings-quick-view-component.template.html',
			controller: 'SettingsQuickViewComponentController as settingsCtrl'
		})
		.component('machineDataQuickView', {
			templateUrl: 'src/public/logged-in/selected/info/data-quick-view-component.template.html',
			controller: 'DataQuickViewComponentController as dataCtrl'
		})
		.component('generalInfoView', {
			templateUrl: 'src/public/logged-in/selected/info/general-info-view-component.template.html',
			controller: 'GeneralInfoViewComponentController as genInfoCtrl',
		})
		.component('authorizedUsersView', {
			templateUrl: 'src/public/logged-in/selected/info/authorized-users-view-component.template.html',
			controller: 'AuthorizedUsersViewComponentController as authUsersCtrl'
		})
		.component('searchUsers', {
			templateUrl: 'src/public/logged-in/selected/info/search-users-component.template.html',
			controller: 'SearchUsersComponentController as searchUsersCtrl'
		});

	InfoController.$inject = ['AppDataService', 'machine'];
	function InfoController(AppDataService, machine) {

		var infoCtrl = this;

		infoCtrl.machine = machine;

		infoCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}

})();