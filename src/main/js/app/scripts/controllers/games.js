'use strict';

worldcup.controller('GamesCtrl', function ($scope, Games, Tipps, Auth) {

    Auth.getUser(function (user) {
        $scope.tipps = Tipps.get({userId: user.name}, function (tipps) {
            Games.query(function (games) {
                angular.forEach(tipps, function (tip) {
                    for(var i = 0; i < games.length; i++) {
                        if (games[i].gameId === tip.gameId) {
                            games[i].tip = tip;
                            if (angular.isDefined(tip.tippResult)) {
                                tip.points = tip.tippResult.totalPoint;
                            }
                            break;
                        }
                    }
                });

                var index = getIndexOfFirstNotAcceptedGame(games);

                if (index > 3) {
                    index = index - 3;
                }
                var filteredGames = games.splice(index, games.length);

                $scope.games = filteredGames;
            });
        });
    });

    function getIndexOfFirstNotAcceptedGame(games){
        for(var i = 0; i < games.length; i++) {
            if (games[i].tippsAccepted) {
                return i;
            }
        }
        return 0;
    }

    $scope.getPoints = function(points) {
        return new Array(points);
    };

    $scope.saveTip = function(game) {
        if (!angular.isNumber(game.tip.goalsTeam1) || game.tip.goalsTeam1 < 0){
            console.log('Goals Team 1 is not a number or negative.');
            return;
        }
        if (!angular.isNumber(game.tip.goalsTeam2) || game.tip.goalsTeam2 < 0){
            console.log('Goals Team 2 is not a number or negative.');
            return;
        }

        var tip = {
            user: Auth.getUser(),
            goalsTeam1: game.tip.goalsTeam1,
            goalsTeam2: game.tip.goalsTeam2,
            gameId: game.gameId
        };

        Tipps.save(tip, function () {
            console.log('Saved tip');
        });
    };

});
