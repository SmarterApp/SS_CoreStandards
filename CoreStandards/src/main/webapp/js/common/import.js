Pages.FullImport = new (function() {
  
  this.attachEventHandlers = function() {
	$("#fullImportButton").click(
	    function() {
		  Pages.showBusy("Loading ...");
		  // upload the file.
		  var uploadHandler = new CoreStandards.io.FileUploadFormSubmission(
		      'fullImportForm', successHandler, errorHandler);
		  uploadHandler.send();
	    });
	
  };
  
  function successHandler(data) {
	// show the popup when the status is failed with appropriate message.
	if (data.status == "failed") {
	  var popupconfig = new CoreStandards.Common.Popup({
		"status" : data.status,
		"errors" : data.errors,
		"warnings" : data.warnings,
		"validationsPassed" : data.validationsPassed,
		"payload" : data.payload != null ? data.payload : "Validation Results"
	  }, {});
	  
	  Pages.PopupManager.displayReturnStatus(popupconfig);
	} else if (data.status == "success") {
	  Pages.takeToUrl(CommonURLs.prvwPubCopyPageUrl(), true);
	}
	// turn off the busy spinner.
	Pages.hideBusy();
  }
  
  function errorHandler(data) {
	// turn off the busy spinner.
	Pages.hideBusy();
  }
  
})();

$(document).on("pageLoad", function() {
  Pages.FullImport.attachEventHandlers();
});