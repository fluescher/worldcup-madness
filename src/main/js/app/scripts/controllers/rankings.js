/**
 * Created by ookomo on 17/06/14.
 */
'use strict';

worldcup.controller('RankingsCtrl', function ($scope) {
    $scope.rows = [
        {
            rank: '1', player: 'mäthu', points: 100
        },
        {
            rank: '2', player: 'päscu', points: 99
        }
    ];
    $scope.yourRank = 2;
    $scope.gridOptions = {data: 'rows'};
});