'use strict';

worldcup.controller('GamesCtrl', function ($scope, Games, Tipps, Auth) {

    $scope.save = function () {
        var tip = {
            user: Auth.getUser(),
            goalsTeam1: 1,
            goalsTeam2: 2,
            gameId: '1-bra-cro'
        };
        console.log(angular.toJson(tip));

        Tipps.save(tip, function () {
            console.log('Saved tip');
            $scope.tipps = Tipps.get({userId: Auth.getUser().name});
        });
    };

    Auth.getUser(function (user) {
        $scope.tipps = Tipps.get({userId: user.name}, function (tipps) {
            Games.query(function (games) {
                angular.forEach(tipps, function (tip) {
                    for(var i = 0; i < games.length; i++) {
                        if (games[i].gameId === tip.gameId) {
                            games[i].tip = tip;
                            break;
                        }
                    }
                });
                $scope.games = games;
            });
        });
    });

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
