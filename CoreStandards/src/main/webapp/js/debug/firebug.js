Firebug = {
  log : function(object) {
	if (typeof (console) != 'undefined')
	  console.log(object);
  },
  
  printKeys : function(object) {
	var keys = "";
	for ( var key in object) {
	  keys = keys + " ; " + key + "=" + object[key];
	}
	Firebug.log(keys);
  }
};
