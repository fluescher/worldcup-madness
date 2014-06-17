'use strict';

worldcup.controller('NavbarCtrl', function ($scope, $location, Auth) {
    $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'games';
        return page === currentRoute ? 'active' : '';
    };

    $scope.isLoggedIn = function() {
        return Auth.isLoggedIn();
    };

    $scope.logout = function() {
        Auth.logout();
    };
});
