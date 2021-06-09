(function() {
	"use strict";

	angular.module('public')
		.controller('CameraController', CameraController);

	CameraController.$inject = ['AppDataService', 'machine'];
	function CameraController(AppDataService, machine) {

		var cameraCtrl = this;

		cameraCtrl.machine = machine;
		cameraCtrl.imageString = undefined;
		cameraCtrl.message = "";

		cameraCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}

		cameraCtrl.selectImage = function() {
			cameraCtrl.displayImage = cameraCtrl.selectedImage;
			var params = {
				action: "selectImage",
				imageId: cameraCtrl.selectedImage
			};
			var promise = AppDataService.getImageString(params);
			promise.then(function(result) {
				cameraCtrl.imageString = result.string;
				cameraCtrl.message = result.message;
			});
		}

		cameraCtrl.hasImage = function() {
			return cameraCtrl.imageString != undefined;
		}

		cameraCtrl.deleteImage = function() {
			var params = {
				action: "deleteImage",
				imageId: cameraCtrl.displayImage
			};
			var promise = AppDataService.standardGet(params);
			promise.then(function(result) {
				cameraCtrl.message = result.message;
				cameraCtrl.machine = result.object;
				cameraCtrl.imageString = undefined;
			});
		}

		cameraCtrl.downloadImage = function() {
			const linkSource = 'data:image/jpg;base64,' + cameraCtrl.imageString;
			const downloadLink = document.createElement('a');
			downloadLink.href = linkSource;
			downloadLink.download = cameraCtrl.displayImage;
			downloadLink.click();
		}

		cameraCtrl.refreshPage = function() {
			cameraCtrl.clearData();
			var promise = AppDataService.getMachineWithImages();
			promise.then(function(result) {
				cameraCtrl.machine = result;
			});
		}

		cameraCtrl.clearData = function() {
			cameraCtrl.message = undefined;
			cameraCtrl.imageString = undefined;
			cameraCtrl.displayImage = undefined;
			cameraCtrl.selectedImage = undefined;
		}

		cameraCtrl.takePicture = function() {
			var params = {
				action: "captureImage"
			};
			var promise = AppDataService.standardGet(params);
			promise.then(function(result) {
				cameraCtrl.message = result.message;
				cameraCtrl.machine = result.object;
			});
		}

	}

})();