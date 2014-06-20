'use strict';

worldcup.controller('NavbarCtrl', function ($scope, $location, Auth) {
    $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'games';
        page = page === 'user/' ? page + $scope.user.name : page;
        return page === currentRoute ? 'active' : '';
    };

    $scope.isLoggedIn = function () {
        return Auth.isLoggedIn();
    };

    $scope.logout = function () {
        Auth.logout();
    };

    Auth.getUser(function(user) {
        $scope.user = user;
    });

});