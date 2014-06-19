'use strict';

worldcup.factory('Tipps', function ($resource) {
    return $resource('http://' + worldcup.host + '/api/tipps/:userId', {userId: '@id'}, {
        'get' : {'method': 'GET', isArray: true}
    });
});