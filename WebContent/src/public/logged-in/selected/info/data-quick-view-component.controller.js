(function() {
	"use strict";

	angular.module('public')
		.controller('DataQuickViewComponentController', DataQuickViewComponentController);

	DataQuickViewComponentController.$inject = ['AppDataService', '$rootScope'];
	function DataQuickViewComponentController(AppDataService, $rootScope) {

		var dataCtrl = this;

		$rootScope.$on('machineStatusChanged', function(){
			dataCtrl.refresh();
		})

		dataCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
		
		dataCtrl.getReading = function(sensor){
			var val = dataCtrl.machine.readings[sensor.name];
			
			if (val === undefined){
				val = 0;
			}
			return val; 
		}
		
		dataCtrl.refresh = function(){
			dataCtrl.machine = AppDataService.getMachine();
		}
		
		dataCtrl.refresh();
	}

})();