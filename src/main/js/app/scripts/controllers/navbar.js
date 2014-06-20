'use strict';

worldcup.controller('NavbarCtrl', function ($scope, $location, $rootScope, Auth) {
    $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'games';
        return currentRoute.indexOf(page) > -1 ? 'active' : '';
    };

    $scope.isLoggedIn = function () {
        return Auth.isLoggedIn();
    };

    $scope.logout = function () {
        Auth.logout();
    };

    $scope.user = null;

    $rootScope.$on('Auth:loginSuccess', function(){
        $scope.user = Auth.getUser();
    });

    $scope.getProfile = function () {
        return '#/user/' + $scope.user.name;
    };

});