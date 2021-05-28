(function() {
	"use strict";

	angular.module('public')
		.controller('GrowSettingsComponentController', GrowSettingsComponentController);

	GrowSettingsComponentController.$inject = ['AppDataService'];
	function GrowSettingsComponentController(AppDataService) {
		var growCtrl = this;
		const datePicker = document.getElementById('plant_date');
		datePicker.addEventListener('change', growCtrl.doSomething);

		growCtrl.doSomething = function(event) {
			console.log(event);
		}

		growCtrl.machine = AppDataService.getMachine();
		growCtrl.message = "";

		growCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		growCtrl.submitChanges = function() {
			var dateAsString = growCtrl.plant_date.toISOString().substring(0, 10);
			console.log(dateAsString);
		}

		growCtrl.clearChanges = function() {
			var picker = document.getElementById('plant_date');
			picker.value = growCtrl.machine.settings.plant_date;
			growCtrl.plant_date = new Date(growCtrl.machine.settings.plant_date);
		}

		growCtrl.datesDifferent = function() {
			var newDateString = growCtrl.plant_date.toISOString().substring(0, 10);
			var oldDateString = growCtrl.machine.settings.plant_date;
			var match = newDateString.trim() == oldDateString.trim();
			return (!match);
		}

		growCtrl.clearChanges();
	}

})();