'use strict';

worldcup.controller('UserCtrl', function ($scope, Ranking, Games, Tipps, $routeParams) {

    $scope.players = Ranking.query(function (players) {
        var points = null;
        var rank = 0;
        angular.forEach(players, function (player) {
            if (points !== player.points) {
                rank++;
            }
            player.rank = rank;
            points = player.points;
        });
        $scope.players = players;
    });

    $scope.yourObject = function () {
        var you = {};
        for (var i = 0; i < $scope.players.length; i++) {
            if ($scope.players[i].username === $scope.username) {
                you.points = $scope.players[i].points;
                you.rank = $scope.players[i].rank;
                return you;
            }
        }
    };

    $scope.username = $routeParams.user;

    $scope.tipps = Tipps.get({userId: $scope.username}, function (tipps) {
        Games.query(function (games) {
            angular.forEach(tipps, function (tip) {
                for (var i = 0; i < games.length; i++) {
                    if (games[i].gameId === tip.gameId) {
                        games[i].tip = tip;
                        tip.points = 3;
                        break;
                    }
                }
            });

            var filteredGames = [];
            angular.forEach(games, function (game) {
                if (angular.isDefined(game.tip) && !game.tippsAccepted) {
                    filteredGames.push(game);
                }
            });

            $scope.games = filteredGames;
        });
    });

    $scope.getPoints = function (points) {
        return new Array(points);
    };

});
