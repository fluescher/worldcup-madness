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
            templateUrl: 'views/main.html',
            controller: 'MainCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
});
