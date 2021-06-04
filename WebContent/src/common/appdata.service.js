(function() {
	"use strict";

	angular.module('common')
		.service('AppDataService', AppDataService);


	AppDataService.$inject = ['$http', '$rootScope', '$state'];
	function AppDataService($http, $rootScope, $state) {
		var service = this;

		var url = "Controller";

		service.lang = "ko";
		service.dict = undefined;


		service.verifyUser = function() {
			if (!service.userValid()) {
				service.redirectHome();
			}
		}

		service.userValid = function() {
			if (service.getUser() !== undefined) {
				return service.getUser().loggedIn;
			}
			return false;
		}

		service.redirectHome = function() {
			$state.go('common.public.not-logged-in.login');
		}

		service.machineValid = function() {
			if (service.getMachine() !== undefined) {
				return true;
			}
			return false;
		}

		service.userIsAuthorized = function(user) {
			// must have a result variable declared because the lambda expression 
			// will return a value for each value in the loop otherwise
			var found = false;
			service.getMachine().authorizedUsers.forEach(authUser => {
				if (authUser.id == user.id) {
					found = true;
				}
			});
			console.log();
			return found;
		}

		service.machineSelectionOk = function() {
			return (service.machineValid() && service.userIsAuthorized(service.getUser()));
		}

		service.verifyUserAndMachine = function() {
			if (!service.machineSelectionOk()) {
				service.redirectToMachinesList();
			}
		}

		service.redirectToMachinesList = function() {
			$state.go('common.public.logged-in.not-selected');
		}

		service.getDictionary = function() {
			if (service.dict === undefined) {
				return $http({
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
			} else {
				return service.dict;
			}
		}

		service.getDictionary = function() {
			if (service.dict === undefined) {
				return $http({
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
			} else {
				return service.dict;
			}
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
			service.setUser(undefined);
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
					service.setUser(data.user);
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

		service.searchForUserByEmail = function(email) {
			return $http({
				method: "GET",
				url: url,
				params: {
					email: email,
					action: "searchForUser"
				}
			}).then(function(result) {
				var data = result.data;
				return data;
			}).catch(function(error) {
				console.log("Something went terribly wrong", error);
			});
		}

		service.addUser = function(user) {
			return $http({
				method: "GET",
				url: url,
				params: {
					userToAdd: user.id,
					action: "addUser"
				}
			}).then(function(result) {
				var data = result.data;
				service.setMachine(data.object);
				$rootScope.$broadcast('userAdded');
				return data;
			}).catch(function(error) {
				console.log("Something went terribly wrong", error);
			});
		}

		service.removeUser = function(user) {
			return $http({
				method: "GET",
				url: url,
				params: {
					userToRemove: user.id,
					action: "removeUser"
				}
			}).then(function(result) {
				var data = result.data;
				service.setMachine(data.object);
				$rootScope.$broadcast('userRemoved');
				return data;
			}).catch(function(error) {
				console.log("Something went terribly wrong", error);
			});
		}

		service.transferOwnership = function(user) {
			return $http({
				method: "GET",
				url: url,
				params: {
					newOwnerId: user.id,
					action: "transferOwnership"
				}
			}).then(function(result) {
				var data = result.data;
				service.setMachine(data.object);
				service.setUser(data.user);
				return data;
			}).catch(function(error) {
				console.log("Something went terribly wrong", error);
			});
		}

		service.refreshMachineInfo = function() {
			return $http({
				method: "GET",
				url: url,
				params: {
					action: "refreshMachineInfo"
				}
			}).then(function(result) {
				var data = result.data;
				service.setMachine(data.object);
				service.setUser(data.user);
				return data;
			}).catch(function(error) {
				console.log("Something went terribly wrong", error);
			});
		}

		service.submitSettings = function(params) {
			return $http({
				method: "GET",
				url: url,
				params: params
			}).then(function(result) {
				var data = result.data;
				service.setMachine(data.object);
				service.setUser(data.user);
				return data;
			})
				.catch(function(error) {
					console.log("Something went terribly wrong", error);
				});
		}

		service.getUser = function() {
			return service.getFromStorage("user");
		}

		service.setUser = function(user) {
			service.setInStorage("user", user);
			$rootScope.$broadcast('userStatusChanged');
		}

		service.getMachine = function() {
			return service.getFromStorage("machine");
		}

		service.setMachine = function(machine) {
			service.setInStorage("machine", machine);
			if (machine != undefined) {
				$rootScope.$broadcast('machineStatusChanged');
			}
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
			if (entry === undefined) {
				return undefined;
			}
			return service.dict[entry][service.lang];
		}

		service.clearMessagesExcept = function(exclude) {
			$rootScope.$broadcast('clearMessages', { exclude: exclude });
		}
		
		service.isValidDate = function(date){
			return (new Date(date) instanceof Date) && !isNaN(new Date(date));
		}
		
		service.isValidTime = function(time){
			
			var pattern = new RegExp('([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]');			
			return pattern.test(time);
		}

	}

})();
