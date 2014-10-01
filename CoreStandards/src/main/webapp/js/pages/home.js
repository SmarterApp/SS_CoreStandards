Pages.Home = new (function() {
  var that = this;
  
  this.attachEventHandlers = function() {
	$("#editPublication").click(function() {
	  Pages.takeToUrl(CommonURLs.copyPubViewPageUrl(), true);
	});
	$("#importExcel").click(function() {
	  Pages.takeToUrl(CommonURLs.importPageUrl(), true);
	});
	
	$("#copyExstPublication").click(function() {
	  Pages.takeToUrl(CommonURLs.copyPubCopyPageUrl(), true);
	});
	$("#downloadCSRInstructions").click(function() {
	  window.open(CommonURLs.csrInstructionsUrl(), '_blank');
	});
	$("#downloadCSRSheet").click(function() {
	  Pages.takeToUrl(CommonURLs.downloadCSRUrl(), false);
	});
  };
  
 
})();

$(document).on("pageLoad", function() {
  // we will first load up the publishers from the rest URL.
  Pages.Home.attachEventHandlers();
});


