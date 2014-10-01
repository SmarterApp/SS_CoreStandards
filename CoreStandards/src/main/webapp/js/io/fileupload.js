/*
 * Upload a file without causing a refresh.
 */
CoreStandards.io.FileUploadFormSubmission = function(formId, successFunction,
    errorFunction) {
  
  var myId = CoreStandards.io.AjaxRequestId.getId();
  var myFormId = formId;
  var iFrameId = null;
  var mySuccessHandler = successFunction;
  var myErrorHandler = errorFunction;
  
  // var myRetrievedData = null;
  // var that = this;
  
  this.getRequestId = function() {
	return myId;
  };
  
  this.send = function() {
	
	// append a iframe by the name of <myFormId>iFrame to the parent
	// of the form.
	
	// create an iframe
	iFrameId = formId + 'iFrame' + myId;
	var iframeNode = createIframeNode(iFrameId);
	var parentNode = document.getElementById(formId).parentNode;
	parentNode.appendChild(iframeNode);
	// set it as a target.
	document.forms[myFormId].target = iFrameId;
	// attach a onload event handler so that we can delete this iFrame.
	document.getElementById(iFrameId).onload = function(e) {
	  Event.stopEvent(e);
	  Event.cancelEvent(e);
	  mySuccessHandler(getData());
	  finalize();
	};
	try {
	  document.forms[myFormId].submit();
	} catch (e) {
	  myErrorHandler(e);
	  finalize();
	}
  };
  
  function finalize() {
	// now we need to remove the hidden iframe.
	var iframeElement = document.getElementById(iFrameId);
	var parentNode = iframeElement.parentNode;
	parentNode.removeChild(iframeElement);
  }
  
  function getData() {
	// !! this is a little hacky turns out that the browser puts the json
	// data
	// as a text node of body>>pre
	var content = document.getElementById(iFrameId).contentDocument.body.firstChild.innerHTML;
	return eval('(' + content + ')');
	;
  }
  
  function createIframeNode(id) {
	
	var newNode = document.createElement('iframe');
	newNode.id = id;
	newNode.setAttribute('style', 'visibility:hidden;display:none');
	newNode.setAttribute('name', id);
	return newNode;
  }
};