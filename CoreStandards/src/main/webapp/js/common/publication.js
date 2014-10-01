CoreStandards.Common.Publication = function(jsonData) {
  var that = this;
  var myKey = null;
  var myName = null;
  var myDescr = null;
  var myVersion = null;
  
  // Following varialbes need to match with variables defined in
  // Publication.java to send the rest call.
  var pubJavaKey = "key";
  
  this.getKey = function() {
	return myKey;
  };
  
  this.getName = function() {
	return myName;
  };
  
  this.getDescr = function() {
	return myDescr;
  };
  
  this.getVersion = function() {
	return myVersion;
  };
  
  this.setKey = function(newKey) {
	myKey = newKey;
  };
  
  this.setName = function(newName) {
	myName = newName;
  };
  
  this.setDesc = function(newDesc) {
	myDescr = newDesc;
  };
  
  this.setVersion = function(newVers) {
	myVersion = newVers;
  };
  
  this.getJsonObject = function() {
	return eval(StringUtils.format('({"' + pubJavaKey + '":"{0}"})', [ myKey ]));
  };
  
  (function() {
	if (jsonData != null) {
	  myKey = jsonData.key;
	  myVersion = jsonData.version;
	  myName = "V" + myVersion + "-" + jsonData.description;
	  myDescr = jsonData.description;
	}
  })();
  
};

CoreStandards.Common.Publication.Settings = {

  restPreviewCategoriesUrl : function() {
	var url = StringUtils.format(
	    '{0}/api/staging/category',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
 
  restPreviewStandardsUrl : function() {
	var url = StringUtils.format(
	    '{0}/api/staging/standard',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restPreviewSocksUrl : function() {
	var url = StringUtils.format('{0}/api/staging/sock',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restPublicationUrl : function() {
	var url = StringUtils.format(
	    '{0}/api/staging/publication',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restPublicationVersionUrl : function() {
	var url = StringUtils.format(
		'{0}/api/staging/publication/version',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restPublicationEditUrl : function() {
	var url = StringUtils.format(
		 '{0}/api/staging/publication',
		 [ CoreStandards.baseUrl ]);
	return url;
  },	  
  restSaveCategoriesGridUrl : function() {
	var url = StringUtils.format('{0}/api/staging/category',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restSaveSocksGridUrl : function() {
	var url = StringUtils.format('{0}/api/staging/sock',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restStandardssGridUrl : function() {
	var url = StringUtils.format('{0}/api/staging/standard',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restGradesGridUrl : function() {
	var url = StringUtils.format('{0}/api/staging/grade',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restSavePublicationUrl : function() {
	var url = StringUtils.format(
	    '{0}/api/publication',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  restGetNextVersionUrl : function() {
	var url = StringUtils.format(
	    '{0}/api/staging/publication/nextVersion',
	    [ CoreStandards.baseUrl ]);
	return url;
  }
};
