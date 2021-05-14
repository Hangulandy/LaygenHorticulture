(function() {
	"use strict";

	angular.module('common')
		.service('AppDataService', AppDataService);


	AppDataService.$inject = ['$http'];
	function AppDataService($http) {
		var service = this;

		var url = "Controller";


		service.login = function(email, password) {
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

		service.getUser = function() {
			return service.user;
		}

		service.setUser = function(user) {
			service.user = user;
		}
	}

})();