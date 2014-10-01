if (typeof (CoreStandards.grid) == 'undefined')
  CoreStandards.grid = {};

CoreStandards.grid.StandardsGrid = function(tableId, navigatorId) {
  var that = this;
  var myTableId = tableId;
  var myNavigatorId = navigatorId;
  var mapOfHandlers = [];
  
  // Following varialbes need to match with variables defined in Standard.java
  // to send the rest call.
  var stdLevel = 'treeLevel';
  var stdKey = 'key';
  var stdName = 'name';
  var stdDescription = 'description';
  var stdShortName = 'shortName';
  
  var rowData = [];
  
  this.getLevel = function() {
	return stdLevel;
  };
  
  this.getKey = function() {
	return stdKey;
  };
  
  this.getName = function() {
	return stdName;
  };
  
  this.getShortName = function() {
	return stdShortName;
  };
  
  this.getDescription = function() {
	return stdDescription;
  };
  
  this.getJsonObject = function(lvl, key, name, pDesc, pSName) {
	return eval(StringUtils.format('({' + stdLevel + ':"{0}",' + stdKey
	    + ':"{1}",' + stdName + ':"{2}",' + stdDescription + ':"{3}",'
	    + stdShortName + ':"{4}"})', [ lvl, key, name, pDesc, pSName ]));
  };
  
  this.loadStandards = function(sourceURL, saveUrl, socksConfig) {
	
	// Firebug.log("Inside loadStandards.... ");
	for ( var i = 0; i < socksConfig.length; ++i) {
	  var configJsonData = socksConfig[i];
	  mapOfHandlers[configJsonData.columnname] = configJsonData;
	}
	
	var parentDivID = jQuery("#gbox_" + myTableId).parent().attr("id");
	$("#" + parentDivID).remove("#" + myTableId);
	$("#" + parentDivID).html('<table id=' + myTableId + '></table>');
	$("#" + parentDivID).append('<div id=' + navigatorId + '></div>');
	
	var stdDescriptionEditable = mapOfHandlers[stdDescription] != null ? mapOfHandlers[stdDescription].editable
	    : false;
	var stdShortNameEditable = mapOfHandlers[stdShortName] != null ? mapOfHandlers[stdShortName].editable
	    : false;
	
	// Firebug.log("stdDescriptionEditable.... " + stdDescriptionEditable);
	// Firebug.log("stdShortNameEditable.... " + stdShortNameEditable);
	var lastsel = "";
	jQuery("#" + myTableId).jqGrid(
	    {
	      url : sourceURL,
	      datatype : "json",
	      mtype : 'GET',
	      colNames : [ 'Tree Level', 'Key', 'Standard Name',
	          'Standard Description', 'Short Name' ],
	      colModel : [ {
	        name : stdLevel,
	        index : stdLevel,
	        width : "6%"
	      }, {
	        name : stdKey,
	        index : stdKey,
	        width : "10%"
	      }, {
	        name : stdName,
	        index : stdName,
	        width : "10%"
	      }, {
	        name : stdDescription,
	        index : stdDescription,
	        width : "50%",
	        cellattr : function(rowId, tv, rawObject, cm, rdata) {
		      return 'style="white-space: normal;"';
	        },
	        editable : stdDescriptionEditable
	      }, {
	        name : stdShortName,
	        index : stdShortName,
	        width : "18%",
	        editable : stdShortNameEditable,
	        cellattr : function(rowId, tv, rawObject, cm, rdata) {
		      return 'style="white-space: normal;"';
	        }
	      } ],
	      cmTemplate : {
		    sortable : false
	      },
	      loadComplete : function() {
		    /**
			 * Implementing pencil in the column header, representing column as
			 * editable. colmodel and colnames have same number of columns in
			 * grid. Display image when column is editable
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
		   * This option allow to set global ajax settings for the row editiing
		   * when we save the data to the server.
		   */
	      ajaxRowOptions : {
		    contentType : 'application/json; charset=utf-8'
	      },
	      /*
		   * Following property required to send data in post mechanism. This
		   * event can be used when a custom data should be passed to the server
		   */
	      serializeRowData : function(postData) {
		    
		    /*
			 * As postData will have only editable field information, reading
			 * the fields from rowData populated in onSelectRow method.
			 */
		    var lvl = '';
		    var key = '';
		    var name = '';
		    var desc = '';
		    var sName = '';
		    
		    var prevDescription = '';
		    var prevShortName = '';
		    
		    if (rowData) {
		      lvl = rowData[stdLevel];
		      key = rowData[stdKey];
		      name = rowData[stdName];
		      prevDescription = rowData[stdDescription];
		      prevShortName = rowData[stdShortName];
		    }
		    
		    desc = postData[stdDescription];
		    sName = postData[stdShortName];
		    
		    var jsonObj = that.getJsonObject(lvl, key, name, desc, sName);
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
	      // sortname : stdName,
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