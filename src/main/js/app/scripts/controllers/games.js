'use strict';

worldcup.controller('GamesCtrl', function ($scope, $rootScope, Games, $http, Tipps, Auth) {
    $scope.games = Games.query();

    $scope.save = function () {
        var tip = {
            user: Auth.getUser(),
            goalsTeam1: 1,
            goalsTeam2: 2,
            gameId: '1-BRA-CRO'
        };
        console.log(angular.toJson(tip));

        $http.post('http://localhost:8080/api/tipps', tip).success(function (data) {
            console.log(data);
        });
    };

    $rootScope.$on('Auth:login', function () {
        console.log(Tipps.get({userId: 'test'}));
        console.log(Auth.getUser());

        $http.get('http://localhost:8080/api/tipps/' + Auth.getUser().name).success(function (data) {
            console.log(data);
            $scope.tipps = data;
        });
    });

});
