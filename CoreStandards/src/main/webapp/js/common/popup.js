/*
 * popup manager. pass in a return status object and let the popup manager deal
 * about showing it.
 */
CoreStandards.Common.Popup = function(returnStatusJson, config) {
  var myStatusJson = returnStatusJson;
  var myConfig = config;
  var myDisplayDiv = null;
  var that = this;
  this.getConfig = function() {
	return myConfig;
  };
  
  this.getStatusJson = function() {
	return myStatusJson;
  };
  
  this.displayInPopupDiv = function(displayDiv) {
	myDisplayDiv = displayDiv;
	
	// step 1: fill in the message first.
	setMessage(myStatusJson.payload, myDisplayDiv);
	
	// step 2: fill in errors div.
	addToErrors(myStatusJson.errors, myDisplayDiv);
	
	// step 3: fill in warnings div.
	addToWarnings(myStatusJson.warnings, myDisplayDiv);
	
	// step 4: fill in alerts div.
	addToAlerts(myStatusJson.validationsPassed, myDisplayDiv);
	
	// step 5: attach the close handler.
	var cancelButton = myDisplayDiv.getElementsByClassName('cancelButton');
	$(cancelButton).click(closeHandler);
  };
  
  this.getDisplayDiv = function() {
	return myDisplayDiv;
  };
  
  function setMessage(messageOjb, displayDiv) {
	var h2Element = displayDiv.getElementsByClassName('messageTitle');
	// Checking and reading message either (object or string). Message will be
	// object when java assigns exception payload
	var msg = typeof (messageOjb.message) == 'undefined' ? messageOjb
	    : messageOjb.message;
	$(h2Element[0]).html(msg);
  }
  
  function addToErrors(errorsArray, displayDiv) {
	var errorBlock = displayDiv.getElementsByClassName('errorBlock')[0];
	var errorMessageDiv = errorBlock.getElementsByClassName('errorMessageDiv')[0]
	    .cloneNode(true);
	// now clear out errorBlock so that we can add new children.
	Dom.removeAllChildren(errorBlock);
	
	var elementsAdded = 0;
	for ( var counter1 = 0; counter1 < errorsArray.length; ++counter1) {
	  var newNode = errorMessageDiv.cloneNode(true);
	  $(newNode.getElementsByClassName('errorMessageSpanBlock')[0]).html(
		  errorsArray[counter1]);
	  Dom.appendChild(errorBlock, newNode);
	  ++elementsAdded;
	}
	
	// show only if we have added messages.
	if (elementsAdded > 0)
	  $(errorBlock).show();
	else
	  $(errorBlock).hide();
  }
  
  function addToWarnings(warningsArray, displayDiv) {
	var warningBlock = displayDiv.getElementsByClassName('warningBlock')[0];
	var warningMessageDiv = warningBlock
	    .getElementsByClassName('warningMessageDiv')[0].cloneNode(true);
	// now clear out errorBlock so that we can add new children.
	Dom.removeAllChildren(warningBlock);
	
	var elementsAdded = 0;
	for ( var counter1 = 0; counter1 < warningsArray.length; ++counter1) {
	  var newNode = warningMessageDiv.cloneNode(true);
	  $(newNode.getElementsByClassName('warningMessageSpanBlock')[0]).html(
		  warningsArray[counter1]);
	  Dom.appendChild(warningBlock, newNode);
	  ++elementsAdded;
	}
	
	// show only if we have added messages.
	if (elementsAdded > 0)
	  $(warningBlock).show();
	else
	  $(warningBlock).hide();
  }
  
  function addToAlerts(validationsArray, displayDiv) {
	var alertBlock = displayDiv.getElementsByClassName('alertBlock')[0];
	var alertMessageDiv = alertBlock.getElementsByClassName('alertMessageDiv')[0]
	    .cloneNode(true);
	// now clear out errorBlock so that we can add new children.
	Dom.removeAllChildren(alertBlock);
	
	var elementsAdded = 0;
	for ( var counter1 = 0; counter1 < validationsArray.length; ++counter1) {
	  var newNode = alertMessageDiv.cloneNode(true);
	  $(newNode.getElementsByClassName('alertMessageSpanBlock')[0]).html(
		  validationsArray[counter1]);
	  Dom.appendChild(alertBlock, newNode);
	  ++elementsAdded;
	}
	
	// show only if we have added messages.
	if (elementsAdded > 0)
	  $(alertBlock).show();
	else
	  $(alertBlock).hide();
  }
  ;
  
  function closeHandler() {
	$.event.trigger({
	  type : "popupClose"
	}, that);
  }
  ;
};

Pages.PopupManager = new (function() {
  // a first-in-first-out queue
  // var displayQueue = [];
  
  this.displayReturnStatus = function(popupConfig) {
	// first we will clone the popup template
	var templateNode = document.getElementsByClassName('popUpTemplate');
	if (templateNode.length == 0) {
	  Firebug.log("No popup template exists");
	  return;
	}
	if (templateNode.length > 1)
	  Firebug.log("More than one popup template exists");
	
	// we will put our data into this cloned node.
	var popUpClone = templateNode[0].cloneNode(true);
	// lets hand it over to the popupConfig to fill in the requisite html data.
	popupConfig.displayInPopupDiv(popUpClone);
	// now that we have put in the data into the div block we will add it to
	// our popups list and render it.
	// displayQueue.push(popupConfig);
	
	// add it to the popups div.
	var allPopUpsDiv = document.getElementById('allPopUps');
	if (allPopUpsDiv == null) {
	  Firebug.log("No allPopUps div found");
	  return;
	}
	allPopUpsDiv.appendChild(popUpClone);
	
	// now render it.
	$(popUpClone).show();
	
	// center align the pop up template object so that all will inherit that
	// CSS attribute
	var templatePopupDiv = $(popUpClone).find(".popUp:first");
	var left = ($(window).innerWidth() - templatePopupDiv.outerWidth(true)) / 2
	    + "px";
	templatePopupDiv.css("left", left);
  };
  
  function closePopup(event, data) {
	var allPopUps = document.getElementById("allPopUps");
	// first hide the pop up.
	var popupDisplayDiv = data.getDisplayDiv();
	$(popupDisplayDiv).hide();
	// remove this popup from the list of pop ups.
	allPopUps.removeChild(popupDisplayDiv);
  }
  
  // attach close event handler
  $(document).on("popupClose", closePopup);
  
})();