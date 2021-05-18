(function() {
	"use strict";

	angular.module('common')
		.service('AppDataService', AppDataService);


	AppDataService.$inject = ['$http'];
	function AppDataService($http) {
		var service = this;

		var url = "Controller";

		service.lang = "ko";
		service.dict = undefined;

		if (service.dict === undefined) {
			$http({
				method: "GET",
				url: url,
				params: {
					action: "getDictionary"
				}
			})
				.then(function(result) {
					var data = result.data;
					service.dict = data;
				}).catch(function(error) {
					console.log("Something went terribly wrong", error);
				});
		}

		service.logout = function() {
			service.setUser(undefined);
			$http({
				method: "GET",
				url: url,
				params: {
					action: "logout"
				}
			}).catch(function(error) {
				console.log("Something went terribly wrong", error);
			});
		}

		service.login = function(email, password) {
			return $http({
				method: "POST",
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

		service.join = function(email, name, username, organization, pw1, pw2) {
			return $http({
				method: "POST",
				url: url,
				params: {
					email: email,
					name: name,
					username: username,
					organization: organization,
					pw1: pw1,
					pw2: pw2,
					action: "join"
				}
			})
				.then(function(result) {
					var data = result.data;
					return data;
				}).catch(function(error) {
					console.log("Something went terribly wrong", error);
				});
		}

		service.getAuthorizations = function() {
			if (service.getUser() !== undefined) {
				if (service.getUser().authorizations !== undefined) {
					return service.getUser().authorizations;
				}
				return $http({
					method: "GET",
					url: url,
					params: {
						action: "getAuthorizations"
					}
				})
					.then(function(result) {
						var data = result.data;
						service.setUser(data);
						return data.user.authorizations;
					}).catch(function(error) {
						console.log("Something went terribly wrong", error);
					});
			}
			return "No user is logged in";
		}

		service.getUser = function() {
			return localStorage.getItem("user");
		}

		service.setUser = function(user) {
			localStorage.setItem("user", user);
		}

		service.getLang = function() {
			return service.lang;
		}

		service.setLang = function(lang) {
			service.lang = lang;
		}

		service.get = function(entry) {
			return service.dict[entry][service.lang];
		}
	}

})();