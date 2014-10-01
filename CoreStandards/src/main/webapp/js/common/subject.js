CoreStandards.Common.Subject = function(jsonData) {
	var myKey = null;
	var myCode = null;
	var myName = null;
	var myDescr = null;

	var that = this;

	// Following varialbes need to match with variables defined in Subject.java
	// to send the rest call.
	var subJavaName = "name";
	var subJavaCode = "code";

	this.getKey = function() {
		return myKey;
	};

	this.getName = function() {
		return myName;
	};

	this.getCode = function() {
		return myCode;
	};

	this.getDescr = function() {
		return myDescr;
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

	this.setCode = function(newCode) {
		myCode = newCode;
	};

	this.getSubJsonObject = function() {
		return eval(StringUtils.format('({"' + subJavaName + '":"{0}", "'
				+ subJavaCode + '":"{1}"})', [ myName, myCode ]));
	};

	(function() {
		if (jsonData != null) {
			myKey = jsonData.key;
			myName = jsonData.name;
			myCode = jsonData.code;
		}
	})();

};

CoreStandards.Common.Subject.Settings = {
	restSubjectUrl : function() {
		var url = StringUtils.format('{0}/api/subject',
				[ CoreStandards.baseUrl ]);
		return url;
	}
};
