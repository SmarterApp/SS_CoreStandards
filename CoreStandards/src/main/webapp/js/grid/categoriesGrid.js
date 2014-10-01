if (typeof (CoreStandards.grid) == 'undefined')
  CoreStandards.grid = {};

CoreStandards.grid.CategoryGrid = function(tableId, navigatorId) {
  var that = this;
  var myTableId = tableId;
  var myNavigatorId = navigatorId;
  var mapOfHandlers = [];
  
  // Following varialbes need to match with variables defined in
  // LoaderCategories.java to send the rest call.
  var cNewName = 'newCategory';
  var cName = 'category';
  var cTreeLevelName = 'treeLevel';
  var rowData = [];
  this.getCategoryName = function() {
	return cName;
  };
  
  this.getTreeLevelName = function() {
	return cTreeLevelName;
  };
  
  this.getJsonObject = function(prevName, level, newName) {
	return eval(StringUtils.format('({' + cName + ':"{0}",' + cTreeLevelName
	    + ':"{1}",' + cNewName + ':"{2}"})', [ prevName, level, newName ]));
  };
  
  this.loadCategories = function(sourceURL, saveURL, categoryConfig) {
	
	// Firebug.log("Inside loadCategories ");
	
	for ( var i = 0; i < categoryConfig.length; ++i) {
	  var configJsonData = categoryConfig[i];
	  mapOfHandlers[configJsonData.columnname] = configJsonData;
	}
	
	var cNameEditable = mapOfHandlers[cName] != null ? mapOfHandlers[cName].editable
	    : false;
	
	var parentDivID = jQuery("#gbox_" + myTableId).parent().attr("id");
	$("#" + parentDivID).remove("#" + myTableId);
	$("#" + parentDivID).html('<table id=' + myTableId + '></table>');
	$("#" + parentDivID).append('<div id=' + navigatorId + '></div>');
	
	var lastsel = "";
	jQuery("#" + myTableId).jqGrid({
	  url : sourceURL,
	  datatype : "json",
	  mtype : 'GET',
	  colNames : [ 'Category', 'Tree Level' ],
	  colModel : [ {
	    name : cName,
	    index : cName,
	    width : "50%",
	    editable : cNameEditable
	  }, {
	    name : cTreeLevelName,
	    index : cTreeLevelName,
	    width : "50%"
	  } ],
	  cmTemplate : {
		sortable : false
	  },
	  loadComplete : function() {
		/**
		 * Implementing pencil in the column header, representing column as
		 * editable. colmodel and colnames have same number of columns in grid.
		 * Display image when column is editable
		 */
		
		CoreStandards.Common.Grid.EditCheck(myTableId);
	  },
	  onSelectRow : function(id) {
		rowData = jQuery(this).getRowData(id);
		var editparameters = {
		  "keys" : true,
		  "url" : saveURL,
		  "mtype" : "PUT"
		};
		
		if (id) {
		  if (id !== lastsel) {
			jQuery("#" + myTableId).jqGrid('restoreRow', lastsel);
			jQuery("#" + myTableId).jqGrid('editRow', id, editparameters);
			lastsel = id;
		  } else {
			jQuery("#" + myTableId).jqGrid('restoreRow', lastsel);
			lastsel = "";
		  }
		}
		
	  },
	  editurl : saveURL,
	  beforeSaveCell : function(id, cell, value) {
		// TODO
	  },
	  /*
	   * This option allow to set global ajax settings for the row editiing when
	   * we save the data to the server.
	   */
	  ajaxRowOptions : {
		contentType : 'application/json; charset=utf-8'
	  },
	  /*
	   * Following property required to send data in post mechanism. This event
	   * can be used when a custom data should be passed to the server
	   */
	  serializeRowData : function(postData) {
		
		/*
		 * As postData will have only editable field information, reading the
		 * fields from rowData populated in onSelectRow method.
		 */
		var catLevel = '';
		var catPrevName = '';
		if (rowData) {
		  catPrevName = rowData[cName];
		  catLevel = rowData[cTreeLevelName];
		}
		
		var catName = postData[cName];
		var jsonObj = that.getJsonObject(catPrevName, catLevel, catName);
		return JSON.stringify(jsonObj);
	  },
	  forcefit : true,
	  shrinktofit : false,
	  autowidth : true,
	  rowNum : 50,
	  height : 'auto',
	  rowList : [ 50, 100, 200, 500 ],
	  pager : '#' + myNavigatorId,
	  // sortname : cName,
	  viewrecords : true,
	  // sortorder : "desc",
	  jsonReader : {
	    repeatitems : false,
	    // id : "0",
	    root : function(obj) {
		  return obj.payload;
	    },
	    records : function(obj) {
		  return obj.payload.length;
	    },
	    page : function() {
		  return 1;
	    },
	    total : function() {
		  return 1;
	    }
	  }
	});
	jQuery('#' + myTableId).jqGrid('navGrid', '#' + myNavigatorId, {
	  edit : false,
	  add : false,
	  del : false,
	  search : false,
	  refresh : false
	});
	
  };
  
};