(function() {
	"use strict";

	angular.module('public')
		.controller('GrowSettingsComponentController', GrowSettingsComponentController);

	GrowSettingsComponentController.$inject = ['AppDataService', '$scope'];
	function GrowSettingsComponentController(AppDataService, $scope) {
		var growCtrl = this;

		$scope.$watch('machine', function() {
			growCtrl.clearChanges();
		});

		$scope.$on('clearMessages', function() {
			growCtrl.resetMessage();
		})

		growCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		growCtrl.submitChanges = function() {
			var dateAsString = growCtrl.plant_date.toISOString().substring(0, 10);

			var params = {
				plant_date: dateAsString,
				action: "updateGrowSettings"
			}

			var promise = AppDataService.submitSettings(params);
			promise.then(function(result) {
				growCtrl.message = result.message;
			})
		}

		growCtrl.changesDetected = function() {
			var newDateString = growCtrl.plant_date.toISOString().substring(0, 10);
			var oldDateString = growCtrl.machine.settings.plant_date;
			var match = newDateString.trim() == oldDateString.trim();
			return (!match);
		}

		growCtrl.clearChanges = function() {
			var picker = document.getElementById('plant_date');

			if (picker != null) {
				picker.value = growCtrl.machine.settings.plant_date;
			}
			growCtrl.plant_date = new Date(growCtrl.machine.settings.plant_date);
		}

		growCtrl.resetMessage = function() {
			growCtrl.message = "";
		}

		growCtrl.resetMessage();
		growCtrl.clearChanges();
	}

})();