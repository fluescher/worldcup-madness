'use strict';

worldcup.factory('Games', function ($resource) {
    return $resource('http://localhost:8080/api/games');
});