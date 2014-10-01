CoreStandards.Common.Grade = function(jsonData) {
  var that = this;
  
  // Following varialbes need to match with variables defined in
  // GradeLevel.java to send the rest call.
  var gJavaName = "name";
  var gJavaDescr = "description";
  
  var myName = null;
  var myDescr = null;
  
  this.getName = function() {
	return myName;
  };
  
  this.getDescr = function() {
	return myDescr;
  };
  
  this.setName = function(newName) {
	myName = newName;
  };
  
  this.setDesc = function(newDesc) {
	myDescr = newDesc;
  };
  
  this.getGradeJsonObject = function() {
	return eval(StringUtils.format('({"' + gJavaName + '":"{0}", "'
	    + gJavaDescr + '":"{1}"})', [ myName, myDescr ]));
  };
  
  (function() {
	if (jsonData != null) {
	  myDescr = jsonData.Description;
	  myName = jsonData.Name;
	}
  })();
  
};

CoreStandards.Common.Grade.Settings = {
  
  restAddGradeUrl : function() {
	var url = StringUtils.format('{0}/api/grade',
	    [ CoreStandards.baseUrl ]);
	return url;
  }

};
