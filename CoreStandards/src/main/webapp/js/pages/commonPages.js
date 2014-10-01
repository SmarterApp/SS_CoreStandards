Pages.showBusy = function(text) {
  if (typeof (text) != 'undefined' && text != null)
	$('#spinnerMessage').html(text);
  
  Pages.checkForOverlay().show();
};
Pages.hideBusy = function() {
  Pages.checkForOverlay().hide();
};

Pages.checkForOverlay = function() {
  var overlayObject = $('#overlay');
  if (overlayObject.length == 0) {
	// we will just log it for the time being.
	Firebug.log("No element with id 'overlay' found");
  }
  return overlayObject;
};

Pages.reassignLoadingImageUrl = function() {
  /*
   * // hack!!!. if a link is clicked then the busy spinner gif stops playing. //
   * the hack is to reassign the image source. var src =
   * $('#overlayImage').attr("src"); //now reassign
   * $('#overlayImage').attr("src", src);
   */
  // the above hack does not seem to be working.
};

Pages.takeToUrl = function(newUrl, showBusy) {
  if (showBusy)
	Pages.showBusy();
  setTimeout(function() {
	window.location = newUrl;
  }, 0);
  /*
   * hack!!! does not work. possibly because DOM has been unloaded.
   * Pages.reassignLoadingImageUrl();
   */
};

$(document).on("pageLoad", function() {
  // we will first load up the publishers from the rest URL.
  Pages.reassignLoadingImageUrl();
});