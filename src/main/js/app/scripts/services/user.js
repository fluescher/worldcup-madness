'use strict';

worldcup.factory('User', function ($resource) {
    return $resource('http://' + worldcup.host + '/api/user/');
});