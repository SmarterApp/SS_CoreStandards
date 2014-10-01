if (typeof (CoreStandards.grid) == 'undefined')
  CoreStandards.grid = {};

CoreStandards.grid.BenchmarkGrid = function(tableId, navigatorId) {
  var that = this;
  var myTableId = tableId;
  var myNavigatorId = navigatorId;
  var mapOfHandlers = [];
  var rowData = [];
  
  // Following varialbes need to match with variables defined in
  // LoaderBenchmarkGrades.java to send the rest call.
  var benchmarkName = 'benchmark';
  var benchmarkGrade = 'grade';
  var newBenchmarkGrade = 'newGrade';
  
  this.getBenchmarkName = function() {
	return benchmarkName;
  };
  
  this.getBenchmarkGrade = function() {
	return benchmarkGrade;
  };
  
  this.getJsonObject = function(name, prevGrade, newGrade) {
	return eval(StringUtils.format('({' + benchmarkName + ':"{0}",'
	    + benchmarkGrade + ':"{1}",' + newBenchmarkGrade + ':"{2}"})', [ name,
	    prevGrade, newGrade ]));
  };
  
  this.loadBenchmarkGrades = function(sourceURL, saveUrl, socksConfig) {
	
	for ( var i = 0; i < socksConfig.length; ++i) {
	  var configJsonData = socksConfig[i];
	  mapOfHandlers[configJsonData.columnname] = configJsonData;
	}
	
	var bNameEditable = mapOfHandlers[benchmarkName] != null ? mapOfHandlers[benchmarkName].editable
	    : false;
	var bGradeEditable = mapOfHandlers[benchmarkGrade] != null ? mapOfHandlers[benchmarkGrade].editable
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
	  colNames : [ 'Benchmark Name', 'Benchmark Grade' ],
	  colModel : [ {
	    name : benchmarkName,
	    index : benchmarkName,
	    width : "50%",
	    editable : bNameEditable
	  }, {
	    name : benchmarkGrade,
	    index : benchmarkGrade,
	    width : "50%",
	    editable : bGradeEditable
	  } ],
	  editurl : saveUrl,
	  beforeSaveCell : function(id, cell, value) {
		// TODO
	  },
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
		var bName = '';
		var bGrade = '';
		var pGrade = '';
		if (rowData) {
		  pGrade = rowData[benchmarkGrade];
		  bName = rowData[benchmarkName];
		}
		
		bGrade = postData[benchmarkGrade];
		
		var jsonObj = that.getJsonObject(bName, pGrade, bGrade);
		return JSON.stringify(jsonObj);
	  },
	  forcefit : true,
	  shrinktofit : false,
	  autowidth : true,
	  loadonce : true,
	  rowNum : 50,
	  height : 'auto',
	  rowList : [ 50, 100, 200, 500 ],
	  pager : '#' + myNavigatorId,
	  // sortname : benchmarkName,
	  viewrecords : true,
	  // sortorder : "desc",
	  jsonReader : {
	    repeatitems : false,
	    // id : "0",
	    root : function(obj) {
		  return obj.payload;
	    },
	    records : function(obj) {
	      Firebug.log("record count..."+obj.payload.length);
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