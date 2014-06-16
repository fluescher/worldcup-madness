'use strict';

angular
    .module('worldcupApp', [
        'ngCookies',
        'ngResource',
        'ngSanitize',
        'ngRoute'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/',
            {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when("/login/",
            {
                templateUrl: 'views/login.html',
                controller: 'LoginCtrl'
            })
            .when("/register/" , {
                templateUrl: 'views/register.html',
                controller: 'RegisterCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
