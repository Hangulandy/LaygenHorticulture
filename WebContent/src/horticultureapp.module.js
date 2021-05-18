(function() {
	"use strict";

	/**
	 * HorticultureApp module that includes the public module as a dependency
	 */
	angular.module('HorticultureApp', ['common', 'public'])
		.config(config);

	config.$inject = ['$urlRouterProvider'];
	function config($urlRouterProvider) {

		// If user goes to a path that doesn't exist, redirect to public root
		$urlRouterProvider.otherwise('/');
	}

})();