if (typeof (CoreStandards.grid) == 'undefined')
  CoreStandards.grid = {};

CoreStandards.grid.PublisherGrid = function(tableId, navigatorId) {
  var that = this;
  var myTableId = tableId;
  var myNavigatorId = navigatorId;
  var mapOfHandlers = [];
  
  // Following varialbes need to match with variables defined in Publisher.java
  // to send the rest call.
  var pName = 'key';
  var pDesc = 'description';
  var pSubj = 'subjectLabel';
  var pVersion = 'version';
  var fkPublisher = 'fkPublisher';
  var fkSubject = 'fkSubject';
  
  var rowData = [];
  // var pVersionEditable = false;
  
  this.getPublisherName = function() {
	return pName;
  };
  
  this.getPublicationDescription = function() {
	return pDesc;
  };
  this.getSubject = function() {
	return pSubj;
  };
  this.getVersion = function() {
	return pVersion;
  };
  
  this.getJsonObject = function(name, desc, subj, ver) {
	return eval(StringUtils.format('({' + fkPublisher + ':"{0}",' + pDesc
	    + ':"{1}",' + fkSubject + ':"{2}",' + pVersion + ':"{3}"})', [ name,
	    desc, subj, ver ]));
  };
  
  this.loadPublishers = function(sourceURL, saveURL, publisherConfig) {
	
	for ( var i = 0; i < publisherConfig.length; ++i) {
	  var configJsonData = publisherConfig[i];
	  mapOfHandlers[configJsonData.columnname] = configJsonData;
	}
	
	var parentDivID = jQuery("#gbox_" + myTableId).parent().attr("id");
	$("#" + parentDivID).remove("#" + myTableId);
	$("#" + parentDivID).html('<table id=' + myTableId + '></table>');
	$("#" + parentDivID).append('<div id=' + navigatorId + '></div>');
	
	var pVersionEditable = mapOfHandlers[pVersion] != null ? mapOfHandlers[pVersion].editable
	    : false;
	
	var lastsel = "";
	
	jQuery("#" + myTableId).jqGrid(
	    {
	      url : sourceURL,
	      datatype : "json",
	      mtype : 'GET',
	      colNames : [ 'Publisher Name', 'Publisher Description', 'Subject',
	          'Version' ],
	      colModel : [ {
	        name : pName,
	        index : pName,
	        width : "15%"
	      }, {
	        name : pDesc,
	        index : pDesc,
	        width : "35%"
	      }, {
	        name : pSubj,
	        index : pSubj,
	        width : "40%"
	      }, {
	        name : pVersion,
	        index : pVersion,
	        width : "10%",
	        editable : pVersionEditable,
	        formatter : fmEditView
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
	      afterSubmit : function(response) {
		    // Firebug.log("afterSubmit Event.... ");
	      },
	      editurl : saveURL,
	      beforeSaveCell : function(id, cell, value) {
		    // var editHandler = mapOfHandlers[cell].callbackHandler;
		    // if (editHandler != null) {
		    // editHandler.call(null, id, cell, value);
		    // todo: cancel the action.
		    // }
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
		    var name = '';
		    var desc = '';
		    var subj = '';
		    if (rowData) {
		      name = rowData[pName];
		      desc = rowData[pDesc];
		      subj = rowData[pSubj];
		    }
		    
		    var vers = postData[pVersion];
		    var jsonObj = that.getJsonObject(name, desc, subj, vers);
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
	      // sortname : pName,
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
  
  function fmEditView(cellval, opts, rowObject, action) {
	return cellval;
  }
  
  function cellNullCheck(cellvalue) {
	if (cellvalue == null) {
	  return '';
	} else {
	  return '' + cellvalue;
	}
  }
  
};