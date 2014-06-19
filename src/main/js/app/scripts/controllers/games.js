'use strict';

worldcup.controller('GamesCtrl', function ($scope, Games, Tipps, Auth) {
    $scope.games = Games.query();

    $scope.save = function () {
        var tip = {
            user: Auth.getUser(),
            goalsTeam1: 1,
            goalsTeam2: 2,
            gameId: '1-BRA-CRO'
        };
        console.log(angular.toJson(tip));

        Tipps.save(tip, function () {
            console.log('Saved tip: ' + tip);
        });
    };

    Auth.getUser(function (user) {
        console.log(user);
        $scope.tipps = Tipps.get({userId: user.name});
    });

});
