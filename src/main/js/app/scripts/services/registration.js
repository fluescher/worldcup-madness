/**
 * Created by ookomo on 16/06/14.
 */
'use strict';
worldcup.factory('registration', ['$resource', function ($resource) {
    return $resource('http://' + worldcup.host + '/api/:register',
        {
            register: '@register'
        },
        {
            register: {
                method: 'POST',
                params: {
                    register: 'register'
                }
            }
        }
    );
}]);