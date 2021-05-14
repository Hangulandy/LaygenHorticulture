(function() {
	'use strict';

	angular.module('public')
		.config(routeConfig);

	/**
	 * Configures the routes and views
	 */
	routeConfig.$inject = ['$stateProvider'];
	function routeConfig($stateProvider) {
		// Routes
		$stateProvider
			.state('public', {
				abstract: true,
				templateUrl: 'src/public/public.jsp'
			})
			.state('public.login', {
				url: '/',
				templateUrl: 'src/public/not-logged-in/login/login.jsp',
				controller: 'LoginController',
				controllerAs: 'loginCtrl'
			})
			.state('public.join', {
				url: '/join',
				templateUrl: 'src/public/not-logged-in/join/join.jsp'
			});
	}
})();