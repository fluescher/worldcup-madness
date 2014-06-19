'use strict';

worldcup.factory('Games', function ($resource) {
    return $resource('http://' + worldcup.host + '/api/games');
});