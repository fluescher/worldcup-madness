/**
 * Created by ookomo on 17/06/14.
 */
'use strict';

worldcup.controller('RankingsCtrl', function ($rootScope, $scope, Auth, Ranking) {
    $scope.players = Ranking.query();

    $scope.yourRank = function() {
        for (var i = 0; i < $scope.players.length; i++) {
            if($scope.players[i].username === Auth.getUser().name) {
                return $scope.players[i].points;
            }
        }
    };

    $scope.show = function(player) {
        console.log(player.username);
    };
});