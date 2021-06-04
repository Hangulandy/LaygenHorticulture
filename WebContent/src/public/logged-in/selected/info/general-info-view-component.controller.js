(function() {
	"use strict";

	angular.module('public')
		.controller('GeneralInfoViewComponentController', GeneralInfoViewComponentController);

	GeneralInfoViewComponentController.$inject = ['AppDataService', '$rootScope'];
	function GeneralInfoViewComponentController(AppDataService, $rootScope) {

		var genInfoCtrl = this;
		
		$rootScope.$on('machinStatusChanged', function(){
			genInfoCtrl.refresh();
		})

		genInfoCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
		
		genInfoCtrl.refresh = function(){
				genInfoCtrl.machine = AppDataService.getMachine();
		}
		
		genInfoCtrl.refresh();
	}

})();