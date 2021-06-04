(function() {
	"use strict";

	angular.module('public')
		.controller('DataChartComponentController', DataChartComponentController);

	DataChartComponentController.$inject = ['AppDataService', '$rootScope'];
	function DataChartComponentController(AppDataService, $rootScope) {

		var chartCtrl = this;
		
		$rootScope.$on('machineStatusChanged', function(){
			chartCtrl.refresh();
		})

		chartCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
		
		chartCtrl.refresh = function(){
			console.log("Just refreshed chartCtrl");
			chartCtrl.machine = AppDataService.getMachine();
		}
		
		chartCtrl.refresh();
	}

})();