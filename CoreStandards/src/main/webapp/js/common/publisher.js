CoreStandards.Common.Publisher = function(jsonData) {
  var myKey = null;
  // var myUrl = null;
  var myName = null;
  var mySessionKey = null;
  var that = this;
  
  // Following varialbes need to match with variables defined in
  // Publisher.java to send the rest call.
  var pubJavaName = "name";
  var pubJavaKey = "key";
  // var pubJavaUrl = "url";
  
  this.getKey = function() {
	return myKey;
  };
  
  this.getName = function() {
	return myName;
  };
  
  this.getSessionKey = function() {
	return mySessionKey;
  };
  
  this.setKey = function(newKey) {
	myKey = newKey;
  };
  
  this.setName = function(newName) {
	myName = newName;
  };
  
  this.setSessionKey = function(newSessionKey) {
	mySessionKey = newSessionKey;
  };
  
  this.getJsonObject = function() {
	return eval(StringUtils.format('({"' + pubJavaName + '":"{0}", "'
	    + pubJavaKey + '":"{1}"})', [ myName, myKey ]));
  };
  
  (function() {
	if (jsonData != null) {
	  myKey = jsonData.key;
	  myName = jsonData.name;
	  // myUrl = jsonData.url;
	}
  })();
};

CoreStandards.Common.Publisher.Settings = {
  restPublisherUrl : function() {
	var url = StringUtils.format('{0}/api/publisher',
	    [ CoreStandards.baseUrl ]);
	return url;
  }
};
