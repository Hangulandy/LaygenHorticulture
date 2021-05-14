(function() {
	'use strict';

	angular.module('HorticultureApp', [])
		.controller('HorticultureController', HorticultureController)
		.service('HorticultureDataService', HorticultureDataService);

	HorticultureController.$inject = ['HorticultureDataService'];
	function HorticultureController(HorticultureDataService) {
		var ctrl = this;

		ctrl.login = function() {
			var promise = HorticultureDataService.login(ctrl.email, ctrl.password);
			promise.then(function(result) {
				ctrl.user = result;
				console.log(ctrl.user);
			});
		}
	}

	HorticultureDataService.$inject = ['$http'];
	function HorticultureDataService($http) {

		var service = this;

		service.login = function(email, password) {
			var url = "Controller";
			console.log(email, password);
			return $http({
				method: "GET",
				url: url,
				params: {
					email: email,
					password: password,
					action: "login"
				}
			})
				.then(function(result) {
					var data = result.data;
					return data;
				}).catch(function(error) {
					console.log("Something went terribly wrong", error);
				});
		}
	}

})();