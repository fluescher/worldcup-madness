/**
 * Created by ookomo on 16/06/14.
 */
'use strict';

worldcup.controller('RegisterCtrl', function ($scope, registration, Auth, $location) {
    $scope.register = {
        email: '',
        password: '',
        name: '',
        forename: '',
        surname: ''
    };

    $scope.rememberMe = false;

    $scope.error = '';

    $scope.hasError = function () {
        return $scope.error.length > 0;
    };

    $scope.register.submitRegisterWithLogin = function(register) {
        registration.register(register, function() {
            var credentials = {
                user: register.name,
                password: register.password,
                rememberMe: $scope.rememberMe
            };
            console.log('Successful registraion of user:' + register.name);
            Auth.login(credentials, function (user) {
                console.log('Successful login for user:' + user.name);
                $location.path('/');
            }, function (error) {
                $scope.error = error;
            });
        }, function(error) {
            $scope.error = error.data;
        });
    };

    $scope.register.submitRegister = function (register) {
        registration.register(register, function() {
            console.log('Successful registraion of user:' + register.name);
            $location.path('/login/');
        }, function(error) {
            $scope.error = error.data;
        });
    };
});