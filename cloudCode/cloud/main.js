var SEND_FOLLOW_REQUEST = "sendFollowRequest";

Parse.Cloud.define(SEND_FOLLOW_REQUEST, function(request, response) {
  // Params: 
  //  requestingUser
  //  targetUser

  var requestingUser = request.params.requestingUser;
  var targetUser = request.params.targetUser;
  
  var attrs = {
    'useMasterKey': true
  }

  var userFollowInfo = Parse.Object.extend('UserFollowInfo');

  getTargetUser()
    .then(addToTargetUser, errorHandler)
    .then(getRequestingUser, errorHandler)
    .then(addToRequestingUser, errorHandler)
    .then(success, errorHandler);

  function getTargetUser() {
    var targetUserQuery = new Parse.Query(userFollowInfo);
    targetUserQuery.equalTo('username', targetUser);
    return targetUserQuery.first();
  }

  function addToTargetUser(targetUserFollowInfo) {
    if (targetUserFollowInfo == undefined) {
      console.log('didn\'t find user \'' + targetUser + '\'');
      response.error('didnt find user');
    } else {
      console.log('got user: ');
      console.log(targetUserFollowInfo);
      targetUserFollowInfo.addUnique('incomingRequests', requestingUser);
      return targetUserFollowInfo.save(null, attrs);
    }
  }

  function getRequestingUser() {
    var requestingUserQuery = new Parse.Query(userFollowInfo);
    requestingUserQuery.equalTo('username', requestingUser);
    return requestingUserQuery.first();
  }

  function addToRequestingUser(requestingUserInfo) {
    if (requestingUserInfo == undefined) {
      console.log('didn\'t find user \'' + targetUser + '\'');
      response.error('didnt find user');
    } else {
      console.log('got user: ');
      console.log(requestingUserInfo);
      requestingUserInfo.addUnique('outgoingRequests', targetUser);
      return requestingUserInfo.save(null, attrs);
    }
  }

  function errorHandler(error) {
    console.log(error);
    response.error('error getting target user info');
  }

  function success() {
    response.success('ok');
  }
});