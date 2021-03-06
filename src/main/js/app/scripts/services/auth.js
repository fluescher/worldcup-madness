'use strict';

worldcup.factory('Auth', function ($rootScope, $q, $location, BasicAuth, User) {
    var Auth = {};

    var user = null;
    var errorCallback = null;

    function loadUser(callback) {
        console.log('loadUser');
        User.get(function (newUser) {
            user = newUser;
            user.password = '';

            if (angular.isFunction(callback)) {
                callback(user);
            }
            $rootScope.$broadcast('Auth:loginSuccess');
        });
    }
    loadUser();

    Auth.login = function (credentials, success, error) {
        errorCallback = error;

        BasicAuth.setCredentials(credentials);
        console.log('Login');

        loadUser(success);
    };

    Auth.logout = function () {
        user = null;
        BasicAuth.clearCredentials();
        $location.path('/login');
    };

    Auth.isLoggedIn = function () {
        return user !== null;
    };

    Auth.getUser = function (callback) {
        if (user === null) {
            loadUser(callback);
        } else {
            if (angular.isFunction(callback)){
                callback(user);
            }
            return user;
        }
    };

    $rootScope.$on('Auth:loginFailed', function () {
        user = null;
        if (angular.isFunction(errorCallback)) {
            errorCallback('Username or password wrong.');
        }
    });

    return Auth;
});

worldcup.config(function ($httpProvider) {
    $httpProvider.interceptors.push(function ($q, $location, $rootScope) {
        return {
            responseError: function (rejection) {
                if (rejection.status === 401) {
                    var deferred = $q.defer();

                    if ('/login' === $location.path()) {
                        $rootScope.$broadcast('Auth:loginFailed');
                    } else {
                        $location.path('/login');
                        $rootScope.$broadcast('Auth:notLoggedIn');
                    }

                    return deferred.promise;
                }

                return $q.reject(rejection);
            }
        };
    });
});

worldcup.factory('BasicAuth', ['Base64', '$cookieStore', '$http', function (Base64, $cookieStore, $http) {

    var authdata = $cookieStore.get('authdata');
    if (angular.isDefined(authdata)) {
        $http.defaults.headers.common.Authorization = 'Basic ' + $cookieStore.get('authdata');
    }

    return {
        setCredentials: function (credentials) {
            var encoded = Base64.encode(credentials.user + ':' + credentials.password);
            $http.defaults.headers.common.Authorization = 'Basic ' + encoded;
            if (credentials.rememberMe) {
                $cookieStore.put('authdata', encoded);
            } else {
                $cookieStore.remove('authdata');
            }
        },
        clearCredentials: function () {
            document.execCommand('ClearAuthenticationCache');
            $cookieStore.remove('authdata');
            $http.defaults.headers.common.Authorization = 'Basic ';
        }
    };
}]);

worldcup.factory('Base64', function () {
    var keyStr = 'ABCDEFGHIJKLMNOP' +
        'QRSTUVWXYZabcdef' +
        'ghijklmnopqrstuv' +
        'wxyz0123456789+/' +
        '=';
    return {
        encode: function (input) {
            var output = '';
            var chr1, chr2, chr3 = '';
            var enc1, enc2, enc3, enc4 = '';
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                    keyStr.charAt(enc1) +
                    keyStr.charAt(enc2) +
                    keyStr.charAt(enc3) +
                    keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = '';
                enc1 = enc2 = enc3 = enc4 = '';
            } while (i < input.length);

            return output;
        },

        decode: function (input) {
            var output = '';
            var chr1, chr2, chr3 = '';
            var enc1, enc2, enc3, enc4 = '';
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                console.error('There were invalid base64 characters in the input text.\n' +
                    'Valid base64 characters are A-Z, a-z, 0-9, +, /,and =\n' +
                    'Expect errors in decoding.');
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, '');

            do {
                enc1 = keyStr.indexOf(input.charAt(i++));
                enc2 = keyStr.indexOf(input.charAt(i++));
                enc3 = keyStr.indexOf(input.charAt(i++));
                enc4 = keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 !== 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 !== 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = '';
                enc1 = enc2 = enc3 = enc4 = '';

            } while (i < input.length);

            return output;
        }
    };
});