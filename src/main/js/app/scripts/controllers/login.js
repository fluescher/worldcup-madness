/**
 * Created by ookomo on 16/06/14.
 */
'use strict';

worldcup.controller('LoginCtrl', function ($scope, Auth) {
    $scope.credentials = {
        user: '',
        password: '',
        rememberMe: false
    };

    $scope.submit = function (credentials) {
        Auth.login(credentials, function (user) {
            console.log(user);
        }, function (error) {
            console.log(error);
        });
    };

});