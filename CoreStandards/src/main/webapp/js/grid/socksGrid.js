if (typeof (CoreStandards.grid) == 'undefined')
  CoreStandards.grid = {};

CoreStandards.grid.SocksGrid = function(tableId, navigatorId) {
  var that = this;
  var myTableId = tableId;
  var myNavigatorId = navigatorId;
  var mapOfHandlers = [];
  var rowData = [];
  var lastsel = "";
  
  // Following varialbes need to match with variables defined in
  // LoaderSOCKs.java to send the rest call.
  var sCategoryName = 'knowledgeCategory';
  var sDescr = 'description';
  var newCategoryName = 'newKnowledgeCategory';
  var newDescr = 'newDescription';
  
  this.getKnowledgeCategory = function() {
	return sCategoryName;
  };
  
  this.getDescription = function() {
	return sDescr;
  };
  
  this.getJsonObject = function(prevName, prevDesc, name, desc) {
	return eval(StringUtils.format('({' + sCategoryName + ':"{0}",' + sDescr
	    + ':"{1}",' + newCategoryName + ':"{2}",' + newDescr + ':"{3}"})', [
	    prevName, prevDesc, name, desc ]));
  };
  
  this.loadSocks = function(sourceURL, saveUrl, socksConfig) {
	
	// Firebug.log("Inside loadSocks.... ");
	
	for ( var i = 0; i < socksConfig.length; ++i) {
	  var configJsonData = socksConfig[i];
	  mapOfHandlers[configJsonData.columnname] = configJsonData;
	}
	
	var sCategoryNameEditable = mapOfHandlers[sCategoryName] != null ? mapOfHandlers[sCategoryName].editable
	    : false;
	var sDescrEditable = mapOfHandlers[sDescr] != null ? mapOfHandlers[sDescr].editable
	    : false;
	var parentDivID = jQuery("#gbox_" + myTableId).parent().attr("id");
	$("#" + parentDivID).remove("#" + myTableId);
	$("#" + parentDivID).html('<table id=' + myTableId + '></table>');
	$("#" + parentDivID).append('<div id=' + navigatorId + '></div>');
	
	jQuery("#" + myTableId).jqGrid({
	  url : sourceURL,
	  datatype : "json",
	  mtype : 'GET',
	  colNames : [ 'Knowledge Categories', 'Description' ],
	  colModel : [ {
	    name : sCategoryName,
	    index : sCategoryName,
	    width : "50%",
	    editable : sCategoryNameEditable
	  }, {
	    name : sDescr,
	    index : sDescr,
	    width : "50%",
	    editable : sDescrEditable
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
		  "url" : saveUrl,
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
	  editurl : saveUrl,
	  beforeSaveCell : function(id, cell, value) {
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
		var prevName = '';
		var prevDesc = '';
		
		if (rowData) {
		  prevName = rowData[sCategoryName];
		  prevDesc = rowData[sDescr];
		}
		
		var Name = postData[sCategoryName];
		var des = postData[sDescr];
		
		var jsonObj = that.getJsonObject(prevName, prevDesc, Name, des);
		return JSON.stringify(jsonObj);
	  },
	  forcefit : true,
	  shrinktofit : false,
	  autowidth : true,
	  rowNum : 50,
	  height : 'auto',
	  rowList : [ 50, 100, 200, 500 ],
	  pager : '#' + myNavigatorId,
	  // sortname : sCategoryName,
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