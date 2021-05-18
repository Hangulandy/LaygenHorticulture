(function() {
	"use strict";

	angular.module('common')
		.controller('CommonController', CommonController);

	CommonController.$inject = ['AppDataService'];
	function CommonController(AppDataService) {
		var commonCtrl = this;

		commonCtrl.lang = AppDataService.getLang();

		commonCtrl.setLang = function(lang) {
			AppDataService.setLang(lang);
		}

		commonCtrl.get = function(entry) {
			return AppDataService.get(entry);
		}
	}



})();