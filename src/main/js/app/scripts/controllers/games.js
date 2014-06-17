'use strict';

worldcup.controller('GamesCtrl', function ($scope, $http, Auth, Games) {
    $scope.games = Games.query();
});
