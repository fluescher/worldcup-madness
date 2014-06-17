'use strict';

worldcup.factory('User', function ($resource) {
    return $resource('http://localhost:8080/api/user/');
});