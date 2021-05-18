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
				templateUrl: 'src/public/public.html',
			})
			.state('public.not-logged-in', {
				abstract: true,
				templateUrl: 'src/public/not-logged-in/not-logged-in.html',
				controller: 'CommonController',
				controllerAs: 'commonCtrl'
			})
			.state('public.not-logged-in.login', {
				url: '/',
				templateUrl: 'src/public/not-logged-in/login/login.html',
				controller: 'LoginController',
				controllerAs: 'loginCtrl'
			})
			.state('public.not-logged-in.join', {
				url: '/join',
				templateUrl: 'src/public/not-logged-in/join/join.html',
				controller: 'JoinController',
				controllerAs: 'joinCtrl'
			})
			.state('public.logged-in', {
				abstract: true,
				templateUrl: 'src/public/logged-in/logged-in.html',
				controller: 'LoggedInController',
				controllerAs: 'loggedInCtrl'
			})
			.state('public.logged-in.not-selected', {
				url: '/logged-in/',
				templateUrl: 'src/public/logged-in/not-selected/not-selected.html',
				controller: 'NotSelectedController',
				controllerAs: 'notSelectedCtrl'
			});
	}
})();