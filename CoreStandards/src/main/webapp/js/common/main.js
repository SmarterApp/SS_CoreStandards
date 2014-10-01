CoreStandards = {};
CoreStandards.Common = {};
Pages = {};

jQuery(document).ready(function() {
  $.event.trigger({
	type : "pageLoad"
  });
});

StringUtils = {
  // Code to format into JSON object
  format : function(text, params) {
	if (params == null)
	  return text;
	
	for ( var i = 0, l = params.length; i < l; ++i) {
	  var reg = new RegExp("\\{" + i + "\\}", "g");
	  text = text.replace(reg, params[i]);
	}
	
	return text;
  },
  // Code to check string is empty or not
  emptyChk : function(text) {
	if (text.length > 0) {
	  return text;
	} else {
	  return null;
	}
  }
};

Event = {
  // from
  // http://www.webdevelopment2.com/the-secret-of-cancelling-and-stopping-events-using-javascript/
  stopEvent : function(e) {
	if (!e)
	  e = window.event;
	if (e.stopPropagation) {
	  e.stopPropagation();
	} else {
	  e.cancelBubble = true;
	}
  },
  
  cancelEvent : function(e) {
	if (!e)
	  e = window.event;
	if (e.preventDefault) {
	  e.preventDefault();
	} else {
	  e.returnValue = false;
	}
  }
};

// Logic to find the query parameters in the url
QueryParameters = (function() {
  var result = {};
  
  if (window.location.search) {
	// split up the query string and store in an associative array
	var params = window.location.search.slice(1).split("&");
	for ( var i = 0; i < params.length; i++) {
	  var tmp = params[i].split("=");
	  result[tmp[0]] = unescape(tmp[1]);
	}
  }
  
  return result;
}());

CommonURLs = {
  homePageUrl : function() {
	var url = StringUtils.format('{0}/pages/home.jsp',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  copyPrvwPubUrl : function() {
	var url = StringUtils.format('{0}/pages/copyPreviewPublication.jsp',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  viewPrvwPubUrl : function() {
	var url = StringUtils.format('{0}/pages/viewPreview.jsp',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  prvwPubCopyPageUrl : function() {
	var url = StringUtils.format(
	    '{0}/pages/copyPreviewPublication.jsp?pageName=copy',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  setupPageUrl : function() {
	var url = StringUtils.format('{0}/pages/setup.jsp',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  csrInstructionsUrl : function() {
	var url = StringUtils.format('{0}/downloads/Core Standards Guidelines.pdf',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  downloadCSRUrl : function() {
	var url = StringUtils.format('{0}/downloads/SBAC-ELA-v1.ods',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  importPageUrl : function() {
	var url = StringUtils.format('{0}/pages/import.jsp',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  partialImportPageUrl : function() {
	var url = StringUtils.format('{0}/pages/partialImport.jsp',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  copyPubCopyPageUrl : function() {
	var url = StringUtils.format('{0}/pages/copyPublication.jsp?pageName=copy',
	    [ CoreStandards.baseUrl ]);
	return url;
  },
  copyPubViewPageUrl : function() {
	var url = StringUtils.format('{0}/pages/copyPublication.jsp?pageName=view',
	    [ CoreStandards.baseUrl ]);
	return url;
  }

};

Dom = {
  removeAllChildren : function(domNode) {
	while (domNode.hasChildNodes()) {
	  domNode.removeChild(domNode.lastChild);
	}
  },
  appendChild : function(parentNode, newNode) {
	parentNode.appendChild(newNode);
  }
};
