/**
 * Created by ookomo on 16/06/14.
 */
'use strict';

worldcup.controller('RegisterCtrl', function ($scope, registration) {
    $scope.register = {
        email: '',
        password: '',
        name: '',
        forename: '',
        surname: ''
    };

    $scope.register.submitRegister = function (register) {
        registration.register(register);
    };
});