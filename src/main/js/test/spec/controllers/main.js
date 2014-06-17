'use strict';

describe('Controller: GamesCtrl', function () {

    // load the controller's module
    beforeEach(module('worldcupApp'));

    var GamesCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        GamesCtrl = $controller('GamesCtrl', {
            $scope: scope
        });
    }));

    it('there is nothing to see here', function () {
        expect(2 + 1).toBe(3);
    });
});
