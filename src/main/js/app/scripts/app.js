'use strict';

var worldcup = angular.module('worldcupApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
]);

worldcup.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'views/games.html',
            controller: 'GamesCtrl'
        })
        .when('/login',
        {
            templateUrl: 'views/login.html',
            controller: 'LoginCtrl'
        })
        .when('/register', {
            templateUrl: 'views/register.html',
            controller: 'RegisterCtrl'
        })
        .when('/user', {
            templateUrl: 'views/user.html',
            controller: 'UserCtrl'
        })
        .when('/rankings/', {
            templateUrl: 'views/rankings.html',
            controller: 'RankingsCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
});