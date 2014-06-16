/**
 * Created by ookomo on 16/06/14.
 */
'use strict';

angular.module('worldcupApp')
    .controller('LoginCtrl', function ($scope) {
        $scope.login = {};
        $scope.login.name = "";
        $scope.login.password = "";
        $scope.login.rememberMe = false;
        $scope.login.submitLoginForm = function () {
        }
    });