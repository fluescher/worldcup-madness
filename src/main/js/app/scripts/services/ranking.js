/**
 * Created by ookomo on 17/06/14.
 */
'use strict';

worldcup.factory('Ranking', ['$resource', function ($resource) {
    return $resource('http://' + worldcup.host + '/api/ranking/');
}]);