(function() {
	"use strict";

	angular.module('public')
		.controller('DataQuickViewComponentController', DataQuickViewComponentController);

	DataQuickViewComponentController.$inject = ['AppDataService'];
	function DataQuickViewComponentController(AppDataService) {

		var dataCtrl = this;

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
	}

})();