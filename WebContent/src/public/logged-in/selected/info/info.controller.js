(function() {
	"use strict";

	angular.module('public')
		.controller('InfoController', InfoController)
		.directive('settingsQuickView', SettingsQuickViewDirective)
		.directive('machineDataQuickView', MachineDataQuickViewDirective);

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

		console.log("Inside settings controller : ", AppDataService.getMachine());

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
		console.log("Inside settings controller : ", AppDataService.getMachine());

		var dataCtrl = this;

		dataCtrl.machine = AppDataService.getMachine();

		dataCtrl.get = function(entry) {
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