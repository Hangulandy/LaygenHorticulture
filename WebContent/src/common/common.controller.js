(function() {
	"use strict";

	angular.module('common')
		.controller('CommonController', CommonController);

	CommonController.$inject = ['AppDataService', '$rootScope', '$state'];
	function CommonController(AppDataService, $rootScope, $state) {
		var commonCtrl = this;
				
		$rootScope.$on('userStatusChanged', function(event) {
			commonCtrl.refreshUser();
		});

		$rootScope.$on('machineStatusChanged', function(event) {
			commonCtrl.refreshMachine();
		});

		commonCtrl.refreshUser = function() {
			commonCtrl.user = AppDataService.getUser();
		}

		commonCtrl.refreshMachine = function() {
			commonCtrl.machine = AppDataService.getMachine();
		}

		commonCtrl.lang = AppDataService.getLang();
		commonCtrl.refreshMachine();
		commonCtrl.refreshUser();

		commonCtrl.setLang = function(lang) {
			AppDataService.setLang(lang);
		}

		commonCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		commonCtrl.logout = function() {
			AppDataService.logout();
			$state.go('common.public.not-logged-in.login');
		}

		commonCtrl.viewMyMachines = function() {
			AppDataService.resetMachine();
			$state.go('common.public.logged-in.not-selected');
		}

		commonCtrl.getUserName = function() {
			return commonCtrl.user.name;
		}

	}



})();