'use strict';

worldcup.controller('MainCtrl', function ($scope, user) {
    $scope.awesomeThings = [
        'HTML5 Boilerplate',
        'AngularJS',
        'Karma'
    ];
    user.get(function(data){
        console.log(data);
    });
});
