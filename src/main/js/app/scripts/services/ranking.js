/**
 * Created by ookomo on 17/06/14.
 */
'use strict';

worldcup.factory('Ranking', function ($resource) {
    return $resource('http://localhost:8080/api/ranking/');
});