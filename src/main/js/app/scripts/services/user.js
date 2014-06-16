'use strict';

worldcup.factory('user', function ($resource) {
    return $resource('/user/');
});