/**
 * Created by ookomo on 17/06/14.
 */
'use strict';

worldcup.controller('RankingsCtrl', function ($scope, Auth, Ranking) {
    $scope.players = Ranking.query();

    $scope.yourRank = function(players) {
        for (var i = 0; i < players.length; i++) {
            if(players[i].username === Auth.getUser().name) {
                return players[i].points;
            }
        }
    };

    $scope.show = function(player) {
        console.log(player.username);
    };
});