/**
 * Created by ookomo on 16/06/14.
 */
'use strict';

worldcup.controller('RegisterCtrl', function ($scope, registration, $http) {
    $scope.register = {
        email: '',
        password: '',
        nickname: '',
        forename: '',
        surname: ''
    };

    $scope.register.submitRegister = function (register) {
        registration.register(register);
    };
});