'use strict';

worldcup.factory('Tipps', function ($resource) {
    return $resource('http://localhost:8080/api/tipps/:userId', {userId: '@id'}, {
        'get' : {'method': 'GET', isArray: false}
    });
});