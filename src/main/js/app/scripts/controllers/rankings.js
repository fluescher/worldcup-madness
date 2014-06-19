/**
 * Created by ookomo on 17/06/14.
 */
'use strict';

worldcup.controller('RankingsCtrl', function ($scope, Auth, Ranking) {
    $scope.rankings = Ranking.query(function (rankings) {
        var points = null;
        var rank = 0;
        angular.forEach(rankings, function (ranking) {
            if (points !== ranking.points) {
                rank++;
            }
            ranking.rank = rank;
            points = ranking.points;
        });
        console.log(rankings);
        $scope.rankings = rankings;
    });

    $scope.yourRank = function () {
        console.log($scope.rankings);
        for (var i = 0; i < $scope.rankings.length; i++) {
            if ($scope.rankings[i].username === Auth.getUser().name) {
                return ++i;
            }
        }
    };

    $scope.show = function (player) {
        console.log(player.username);
    };
});