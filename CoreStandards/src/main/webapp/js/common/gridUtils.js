CoreStandards.Common.Grid = {
  // Grid utils method to display editable symbol
  EditCheck : function(myTableId) {
	// Firebug.log("Inside gridUtils.... ");
	var data = jQuery("#" + myTableId).jqGrid('getGridParam', 'colModel');
	var columnNames = $("#" + myTableId).jqGrid('getGridParam', 'colNames');
	for ( var i = 0; i < data.length; ++i) {
	  var dataObj = data[i];
	  var colName = dataObj.name;
	  var editableVal = dataObj.editable;
	  // show the editable symbol when it is already not present and
	  // when the column is editable
	  if (editableVal
	      && columnNames[i].toLowerCase().indexOf("entypo-brush") <= 0) {
		jQuery("#" + myTableId).jqGrid(
		    'setLabel',
		    colName,
		    columnNames[i]
		        + "<span class='entypo-brush gridEditableCol'></span>");
	  }
	}
  },
  // Grid utils show hide divs
  showFirstHideRestDivs : function() {
	$('#' + arguments[0]).show();
	for ( var i = 1; i < arguments.length; i++) {
	  $('#' + arguments[i]).hide();
	}
  }
};
