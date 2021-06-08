(function() {
	"use strict";

	angular.module('public')
		.controller('CameraController', CameraController);

	CameraController.$inject = ['AppDataService', 'machine', '$scope'];
	function CameraController(AppDataService, machine, $scope) {

		var cameraCtrl = this;

		$scope.$watch('cameraCtrl.machine', function() {
			console.log(cameraCtrl.machine);
			cameraCtrl.selectedImage = "";
		});

		cameraCtrl.machine = machine;


		cameraCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
		
		cameraCtrl.selectImage = function(){
			console.log(cameraCtrl.selectedImage);
		}

		AppDataService.verifyUserAndMachine();
	}

})();