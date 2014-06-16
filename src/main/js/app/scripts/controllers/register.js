/**
 * Created by ookomo on 16/06/14.
 */
angular.module('worldcupApp')
    .controller('RegisterCtrl', function ($scope) {
        $scope.register = {};
        $scope.register.email = "";
        $scope.register.password = "";
        $scope.register.nickname = "";
        $scope.register.forename = "";
        $scope.register.surname = "";
        $scope.register.submitRegister = function($http) {
        }
    });