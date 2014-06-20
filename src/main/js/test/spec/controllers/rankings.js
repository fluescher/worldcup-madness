/**
 * Created by ookomo on 17/06/14.
 */
'use strict';

describe('Controller: RankingsCtrl', function () {

    // load the controller's module
    beforeEach(angular.mock.module('worldcupApp'));

    var RankingsCtrl,
        scope,
        Auth,
        Ranking;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        Auth = {
            getUser: function () {
                return 'pat';
            },
            isLoggedIn: function () {
                return true;
            }
        };
        Ranking = {
            query: function() {
                return [
                    {
                        username: 'pat',
                        rank: 1,
                        points: 12
                    },
                    {
                        username: 'mat',
                        rank: 2,
                        points: 8
                    }
                ];
            }
        };
        RankingsCtrl = $controller('RankingsCtrl', {
            $scope: scope,
            Auth: Auth,
            Ranking: Ranking
        });

    }));

    it('should have a RankingsCtrl controller', function () {
        expect(RankingsCtrl).toBeDefined();
    });

    it('find user rank', function () {
        var players = [
            {
                username: 'pat',
                rank: 1,
                points: 12
            },
            {
                username: 'mat',
                rank: 2,
                points: 8
            }
        ];
        expect(scope.yourRank(players), 1);
    });
    it('there is nothing to see here', function () {
        expect(2 + 1).toBe(3);
    });
});
