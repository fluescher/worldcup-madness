/**
 * Created by ookomo on 16/06/14.
 */
'use strict';

worldcup.controller('LoginCtrl', function ($scope, $location, Auth) {
    $scope.credentials = {
        user: '',
        password: '',
        rememberMe: true
    };

    $scope.error = '';

    $scope.hasError = function () {
        return $scope.error.length > 0;
    };

    $scope.submit = function (credentials) {
        Auth.login(credentials, function (user) {
            console.log('Successful login for user:' + user.name);
            $location.path('/');
        }, function (error) {
            $scope.error = error;
        });
    };

});