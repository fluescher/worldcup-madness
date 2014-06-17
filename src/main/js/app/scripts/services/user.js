'use strict';

worldcup.factory('user', function ($resource) {
    return $resource('http://localhost:8080/api/user/');
});