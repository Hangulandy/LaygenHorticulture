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
			service.setMachine(undefined);
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
				return $http({
					method: "GET",
					url: url,
					params: {
						action: "getAuthorizations"
					}
				})
					.then(function(result) {
						var data = result.data;
						service.setUser(data.user);
						return data;
					}).catch(function(error) {
						console.log("Something went terribly wrong", error);
					});
			}
			return undefined;
		}

		service.fetchMachine = function(sn) {
			service.setMachine(undefined);
			return $http({
				method: "GET",
				url: url,
				params: {
					selectedMachineId: sn,
					action: "selectMachine"
				}
			}).then(function(result) {
				var data = result.data;
				if (result.status == 200) {
					service.setMachine(data.object);
				}
				return data;
			}).catch(function(error) {
				console.log("Something went terribly wrong", error);
			});
		}

		service.searchForUserByEmail = function(email){
			console.log("Inside service.searchForUserByEmail : ", email);
			return $http({
				method: "GET",
				url: url,
				params: {
					email: email,
					action: "searchForUser"
				}
			}).then(function(result){
				var data = result.data;
				return data;
			}).catch(function(error){
				console.log("Something went terribly wrong", error);
			});
		}

		service.getUser = function() {
			return service.getFromStorage("user");
		}

		service.setUser = function(user) {
			service.setInStorage("user", user);
		}

		service.getMachine = function() {
			return service.getFromStorage("machine");
		}

		service.setMachine = function(machine) {
			service.setInStorage("machine", machine);
		}

		service.resetMachine = function() {
			service.setMachine(undefined);
		}

		service.getFromStorage = function(itemName) {
			var item = sessionStorage.getItem(itemName);
			var returnItem = undefined;
			if (item != null) {
				returnItem = JSON.parse(item);
			}
			return returnItem;
		}

		service.setInStorage = function(itemName, item) {
			if (item === undefined) {
				sessionStorage.removeItem(itemName);
			} else {
				sessionStorage.setItem(itemName, JSON.stringify(item));
			}
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

		service.setMessage = function(message) {
			service.message = message;
		}

		service.getMessage = function() {
			if (service.message === undefined) {
				return "null";
			} else {
				return service.message;
			}
		}
	}

})();