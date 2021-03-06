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
        $scope.rankings = rankings;
    });

    $scope.yourRank = function () {
        if (Auth.isLoggedIn()) {
            for (var i = 0; i < $scope.rankings.length; i++) {
                if ($scope.rankings[i].username === Auth.getUser().name) {
                    return $scope.rankings[i].rank;
                }
            }
        } else {
            return '';
        }
    };

    $scope.profileUrl = function (username) {
        return '/user/' + username;
    };
});