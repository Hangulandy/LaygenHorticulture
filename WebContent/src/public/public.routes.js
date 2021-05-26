(function() {
	'use strict';

	angular.module('public')
		.config(routeConfig);

	routeConfig.$inject = ['$stateProvider'];
	function routeConfig($stateProvider) {

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
			})
			.state('public.logged-in.selected', {
				abstract: true,
				templateUrl: 'src/public/logged-in/selected/selected.html',
				controller: 'SelectedController',
				controllerAs: 'selectedCtrl'
			})
			.state('public.logged-in.selected.info', {
				url: '/logged-in/info',
				templateUrl: 'src/public/logged-in/selected/info/info.html',
				controller: 'InfoController',
				controllerAs: 'infoCtrl',
				resolve: {
					machine: ['AppDataService', function(AppDataService) {
						return AppDataService.getMachine();
					}]
				}
			})
			.state('public.logged-in.selected.settings', {
				url: '/logged-in/settings',
				templateUrl: 'src/public/logged-in/selected/settings/settings.html',
				controller: 'SettingsController',
				controllerAs: 'settingsCtrl'
			})
			.state('public.logged-in.selected.data', {
				url: '/logged-in/data',
				templateUrl: 'src/public/logged-in/selected/data/data.html'
			})
			.state('public.logged-in.selected.camera', {
				url: '/logged-in/camera',
				templateUrl: 'src/public/logged-in/selected/camera/camera.html'
			});
	}
})();