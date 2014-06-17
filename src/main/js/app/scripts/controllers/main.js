'use strict';

worldcup.controller('MainCtrl', function ($scope, $http, Auth) {
    $scope.awesomeThings = [
        'HTML5 Boilerplate',
        'AngularJS',
        'Karma'
    ];

    console.log('isUserLoggedIn:' + Auth.isLoggedIn());
});
