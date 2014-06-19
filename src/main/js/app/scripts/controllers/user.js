'use strict';

worldcup.controller('UserCtrl', function ($scope, Ranking, Games, Tipps, Auth) {
    $scope.players = Ranking.query();

    $scope.yourRank = function(players) {
        for (var i = 0; i < players.length; i++) {
            if(players[i].username === Auth.getUser().name) {
                return players[i].points;
            }
        }
    };

    Auth.getUser(function (user) {
        $scope.user = user;

        $scope.tipps = Tipps.get({userId: user.name}, function (tipps) {
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
    });

    $scope.getPoints = function (points) {
        return new Array(points);
    };

});
