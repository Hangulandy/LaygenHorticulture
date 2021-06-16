(function() {
	"use strict";

	angular.module('public').controller('DataQuickViewComponentController',
			DataQuickViewComponentController);

	DataQuickViewComponentController.$inject = [ 'AppDataService' ];
	function DataQuickViewComponentController(AppDataService) {

		var dataCtrl = this;

		dataCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		dataCtrl.getReading = function(sensor) {

			var val = 0;

			if (sensor != undefined && sensor.name != undefined
					&& dataCtrl.machine != undefined
					&& dataCtrl.machine.readings != undefined) {
				val = dataCtrl.machine.readings[sensor.name];
			}
			return val;
		}
	}

})();