/**
 * Created by ookomo on 17/06/14.
 */
'use strict';

worldcup.controller('RankingsCtrl', function ($rootScope, $scope, Auth, Ranking) {
    $scope.players = null;
    Ranking.get(function(data) {
        console.log(data);
        $scope.players = data;
    });
    $scope.yourRank = function() {
        for (var i = 0; i < $scope.players.length; i++) {
            if($scope.players.name === Auth.getUser().name) {
                return $scope.players;
            }
        }
    };
});