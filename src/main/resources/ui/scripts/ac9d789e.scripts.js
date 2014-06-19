"use strict";var worldcup=angular.module("worldcupApp",["ngCookies","ngResource","ngSanitize","ngRoute"]);worldcup.config(function($routeProvider){$routeProvider.when("/",{templateUrl:"views/games.html",controller:"GamesCtrl"}).when("/login",{templateUrl:"views/login.html",controller:"LoginCtrl"}).when("/register",{templateUrl:"views/register.html",controller:"RegisterCtrl"}).when("/user",{templateUrl:"views/user.html",controller:"UserCtrl"}).when("/rankings/",{templateUrl:"views/rankings.html",controller:"RankingsCtrl"}).otherwise({redirectTo:"/"})}),worldcup.controller("NavbarCtrl",function($scope,$location,Auth){$scope.navClass=function(page){var currentRoute=$location.path().substring(1)||"games";return page===currentRoute?"active":""},$scope.isLoggedIn=function(){return Auth.isLoggedIn()},$scope.logout=function(){Auth.logout()},Auth.getUser(function(user){$scope.user=user})}),worldcup.controller("GamesCtrl",function($scope,Games,Tipps,Auth){function getIndexOfFirstNotAcceptedGame(games){for(var i=0;i<games.length;i++)if(games[i].tippsAccepted)return i;return 0}Auth.getUser(function(user){$scope.tipps=Tipps.get({userId:user.name},function(tipps){Games.query(function(games){angular.forEach(tipps,function(tip){for(var i=0;i<games.length;i++)if(games[i].gameId===tip.gameId){games[i].tip=tip,tip.points=3;break}});var index=getIndexOfFirstNotAcceptedGame(games);index>2&&(index-=2);var filteredGames=games.splice(index,games.length);$scope.games=filteredGames})})}),$scope.getPoints=function(points){return new Array(points)},$scope.saveTip=function(game){if(!angular.isNumber(game.tip.goalsTeam1)||game.tip.goalsTeam1<0)return void console.log("Goals Team 1 is not a number or negative.");if(!angular.isNumber(game.tip.goalsTeam2)||game.tip.goalsTeam2<0)return void console.log("Goals Team 2 is not a number or negative.");var tip={user:Auth.getUser(),goalsTeam1:game.tip.goalsTeam1,goalsTeam2:game.tip.goalsTeam2,gameId:game.gameId};Tipps.save(tip,function(){console.log("Saved tip")})}}),worldcup.controller("UserCtrl",function($scope,Ranking,Games,Tipps,Auth){$scope.players=Ranking.query(),$scope.yourRank=function(players){for(var i=0;i<players.length;i++)if(players[i].username===Auth.getUser().name)return players[i].points},Auth.getUser(function(user){$scope.user=user,$scope.tipps=Tipps.get({userId:user.name},function(tipps){Games.query(function(games){angular.forEach(tipps,function(tip){for(var i=0;i<games.length;i++)if(games[i].gameId===tip.gameId){games[i].tip=tip,tip.points=3;break}});var filteredGames=[];angular.forEach(games,function(game){angular.isDefined(game.tip)&&!game.tippsAccepted&&filteredGames.push(game)}),$scope.games=filteredGames})})}),$scope.getPoints=function(points){return new Array(points)}}),worldcup.controller("LoginCtrl",function($scope,$location,Auth){$scope.credentials={user:"",password:"",rememberMe:!0},$scope.error="",$scope.hasError=function(){return $scope.error.length>0},$scope.submit=function(credentials){Auth.login(credentials,function(user){console.log("Successful login for user:"+user.name),$location.path("/")},function(error){$scope.error=error})}}),worldcup.controller("RegisterCtrl",function($scope,registration,Auth,$location){$scope.register={email:"",password:"",name:"",forename:"",surname:""},$scope.error="",$scope.hasError=function(){return $scope.error.length>0},$scope.register.submitRegister=function(register){registration.register(register,function(){var credentials={user:register.name,password:register.password,rememberMe:!0};console.log("Successful registraion of user:"+register.name),Auth.login(credentials,function(user){console.log("Successful login for user:"+user.name),$location.path("/")},function(error){$scope.error=error})},function(error){$scope.error=error.data})}}),worldcup.controller("RankingsCtrl",function($scope,Auth,Ranking){$scope.players=Ranking.query(),$scope.yourRank=function(players){for(var i=0;i<players.length;i++)if(players[i].username===Auth.getUser().name)return players[i].points},$scope.show=function(player){console.log(player.username)}}),worldcup.factory("User",function($resource){return $resource("http://localhost:8080/api/user/")}),worldcup.factory("Games",function($resource){return $resource("http://localhost:8080/api/games")}),worldcup.factory("registration",["$resource",function($resource){return $resource("http://localhost:8080/api/:register",{register:"@register"},{register:{method:"POST",params:{register:"register"}}})}]),worldcup.factory("Auth",function($rootScope,$q,$location,BasicAuth,User){function loadUser(callback){console.log("loadUser"),User.get(function(newUser){user=newUser,user.password="",angular.isFunction(callback)&&callback(user)})}var Auth={},user=null,errorCallback=null;return loadUser(),Auth.login=function(credentials,success,error){errorCallback=error,BasicAuth.setCredentials(credentials),loadUser(success)},Auth.logout=function(){user=null,BasicAuth.clearCredentials(),$location.path("/login")},Auth.isLoggedIn=function(){return null!==user},Auth.getUser=function(callback){return null!==user?(angular.isFunction(callback)&&callback(user),user):void loadUser(callback)},$rootScope.$on("Auth:loginFailed",function(){user=null,angular.isFunction(errorCallback)&&errorCallback("Username or password wrong.")}),Auth}),worldcup.config(function($httpProvider){$httpProvider.interceptors.push(function($q,$location,$rootScope){return{responseError:function(rejection){if(401===rejection.status){var deferred=$q.defer();return"/login"===$location.path()?$rootScope.$broadcast("Auth:loginFailed"):($location.path("/login"),$rootScope.$broadcast("Auth:notLoggedIn")),deferred.promise}return $q.reject(rejection)}}})}),worldcup.factory("BasicAuth",["Base64","$cookieStore","$http",function(Base64,$cookieStore,$http){var authdata=$cookieStore.get("authdata");return angular.isDefined(authdata)&&($http.defaults.headers.common.Authorization="Basic "+$cookieStore.get("authdata")),{setCredentials:function(credentials){var encoded=Base64.encode(credentials.user+":"+credentials.password);$http.defaults.headers.common.Authorization="Basic "+encoded,credentials.rememberMe?$cookieStore.put("authdata",encoded):$cookieStore.remove("authdata")},clearCredentials:function(){document.execCommand("ClearAuthenticationCache"),$cookieStore.remove("authdata"),$http.defaults.headers.common.Authorization="Basic "}}}]),worldcup.factory("Base64",function(){var keyStr="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";return{encode:function(input){var chr1,chr2,enc1,enc2,enc3,output="",chr3="",enc4="",i=0;do chr1=input.charCodeAt(i++),chr2=input.charCodeAt(i++),chr3=input.charCodeAt(i++),enc1=chr1>>2,enc2=(3&chr1)<<4|chr2>>4,enc3=(15&chr2)<<2|chr3>>6,enc4=63&chr3,isNaN(chr2)?enc3=enc4=64:isNaN(chr3)&&(enc4=64),output=output+keyStr.charAt(enc1)+keyStr.charAt(enc2)+keyStr.charAt(enc3)+keyStr.charAt(enc4),chr1=chr2=chr3="",enc1=enc2=enc3=enc4="";while(i<input.length);return output},decode:function(input){var chr1,chr2,enc1,enc2,enc3,output="",chr3="",enc4="",i=0,base64test=/[^A-Za-z0-9\+\/\=]/g;base64test.exec(input)&&console.error("There were invalid base64 characters in the input text.\nValid base64 characters are A-Z, a-z, 0-9, +, /,and =\nExpect errors in decoding."),input=input.replace(/[^A-Za-z0-9\+\/\=]/g,"");do enc1=keyStr.indexOf(input.charAt(i++)),enc2=keyStr.indexOf(input.charAt(i++)),enc3=keyStr.indexOf(input.charAt(i++)),enc4=keyStr.indexOf(input.charAt(i++)),chr1=enc1<<2|enc2>>4,chr2=(15&enc2)<<4|enc3>>2,chr3=(3&enc3)<<6|enc4,output+=String.fromCharCode(chr1),64!==enc3&&(output+=String.fromCharCode(chr2)),64!==enc4&&(output+=String.fromCharCode(chr3)),chr1=chr2=chr3="",enc1=enc2=enc3=enc4="";while(i<input.length);return output}}}),worldcup.factory("Ranking",["$resource",function($resource){return $resource("http://localhost:8080/api/ranking/")}]),worldcup.factory("Tipps",function($resource){return $resource("http://localhost:8080/api/tipps/:userId",{userId:"@id"},{get:{method:"GET",isArray:!0}})});