Pages.CopyPublication = new (function() {
	var that = this;
	var myListOfPublishers = [];
	var myListOfSubjects = [];
	var myListOfPublications = [];
	var selPublisherVal = '';
	var selSubjectVal = '';
	var selPublicationVal = null;
	var postCall = "POST";
	var putCall = "PUT";
	var getCall = "GET";
	var publisherKey = "publisher";
	var subjectKey = "subject";
	this.getPublisherReqObject = function(pubName) {

		return StringUtils.format(publisherKey + '={0}', [ pubName ]);
	};
	this.getPubANDSubReqObject = function(pubName, subName) {

		return StringUtils.format(publisherKey + '={0}', [ pubName ]) + "&"
				+ StringUtils.format(subjectKey + '={0}', [ subName ]);
	};

	this.loadPublishers = function() {
		Pages.showBusy();
		var url = CoreStandards.Common.Publisher.Settings.restPublisherUrl();
		var publisherSuccessHandler = function(jsondata, requestObject) {

			if (jsondata.status == "success") {
				// go through the json data and create the publishers object.
				var data = jsondata.payload;
				for ( var counter1 = 0; counter1 < data.length; ++counter1) {
					var newPublisher = new CoreStandards.Common.Publisher(
							data[counter1]);
					myListOfPublishers.push(newPublisher);

					// Firebug.log("array List : "+ myListOfPublishers);
				}

				populateDropdown(myListOfPublishers, 'publisherSelection');
			} else if (jsondata.status == "failed") {
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : jsondata.status,
					"errors" : jsondata.errors,
					"warnings" : jsondata.warnings,
					"validationsPassed" : jsondata.validationsPassed,
					"payload" : jsondata.payload != null ? jsondata.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var errorHandler = function(responseData, status, error, requestObject) {

			Pages.hideBusy();
		};

		var ajaxRequest = new CoreStandards.io.AjaxRequest('', url, getCall,
				publisherSuccessHandler, errorHandler);
		ajaxRequest.send();

	};

	this.loadSubjects = function() {
		Pages.showBusy();
		myListOfSubjects = [];
		var subjUrl = CoreStandards.Common.Subject.Settings.restSubjectUrl();
		var subjectSuccessHandler = function(jsondata, requestObject) {
			if (jsondata.status == "success") {
				var data = jsondata.payload;
				// go through the json data and create the subjects object.
				for ( var counter1 = 0; counter1 < data.length; ++counter1) {
					var newSubject = new CoreStandards.Common.Subject(
							data[counter1]);
					myListOfSubjects.push(newSubject);
				}

				populateDropdown(myListOfSubjects, 'subjectSelection');
			} else if (jsondata.status == "failed") {
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : jsondata.status,
					"errors" : jsondata.errors,
					"warnings" : jsondata.warnings,
					"validationsPassed" : jsondata.validationsPassed,
					"payload" : jsondata.payload != null ? jsondata.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			Pages.hideBusy();
		};

		var errorHandler = function(request, status, error, requestObject) {
			Pages.hideBusy();
		};

		var ajaxRequest = new CoreStandards.io.AjaxRequest(this
				.getPublisherReqObject(selPublisherVal), subjUrl, getCall,
				subjectSuccessHandler, errorHandler);
		ajaxRequest.send();

	};

	this.loadPublications = function() {
		Pages.showBusy();
		myListOfPublications = [];
		var publicationUrl = CoreStandards.Common.Publication.Settings
				.restSavePublicationUrl();
		var publicationSuccessHandler = function(jsondata, requestObject) {
			if (jsondata.status == "success") {
				var data = jsondata.payload;
				for ( var counter1 = 0; counter1 < data.length; ++counter1) {
					var newPublication = new CoreStandards.Common.Publication(
							data[counter1]);
					myListOfPublications.push(newPublication);
				}
				populateDropdown(myListOfPublications, 'publicationSelection');
			} else if (jsondata.status == "failed") {
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : jsondata.status,
					"errors" : jsondata.errors,
					"warnings" : jsondata.warnings,
					"validationsPassed" : jsondata.validationsPassed,
					"payload" : jsondata.payload != null ? jsondata.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			Pages.hideBusy();
		};

		var errorHandler = function(request, status, error, requestObject) {
			Pages.hideBusy();
		};

		var ajaxRequest = new CoreStandards.io.AjaxRequest(this
				.getPubANDSubReqObject(selPublisherVal, selSubjectVal),
				publicationUrl, getCall, publicationSuccessHandler,
				errorHandler);
		ajaxRequest.send();

	};

	function populateDropdown(aList, drpDownID) {
		var options = '';
		for ( var i = 0; i < aList.length; i++) {
			options += '<option value="' + aList[i].getKey() + '">'
					+ aList[i].getName() + '</option>';
		}
		$('#' + drpDownID).children('option:not(:first)').remove();
		$('#' + drpDownID).append(options);
	}

	this.attachEventHandlers = function() {
		$("#copyPublication, #viewPublication, #publicationEdit")
				.click(
						function() {
							Pages.showBusy("Copying publication");

							var copyPublicationUrl = CoreStandards.Common.Publication.Settings
									.restPublicationUrl();
							var copyPublicationMethod = postCall;
							
							//view results in a different logic on database side, hence have to call different api
							var pageName = QueryParameters['pageName'];
							if (pageName == 'view') {
								copyPublicationUrl = CoreStandards.Common.Publication.Settings
								.restPublicationEditUrl();
								copyPublicationMethod = putCall;
							}
							
							var copySuccessHandler = function(data,
									requestObject) {

								var pageName = QueryParameters['pageName'];
								var newUrl = null;
								if (pageName == 'view') {
									newUrl = CommonURLs.viewPrvwPubUrl()
											+ "?pageName=" + pageName;
								} else if (pageName == 'copy') {
									newUrl = CommonURLs.copyPrvwPubUrl()
											+ "?pageName=" + pageName;
								}
								if (newUrl != null)
									Pages.takeToUrl(newUrl, true);

							};

							var errorHandler = function(request, status, error,
									requestObject) {
								// Firebug.log("In errorHandler.... " + error);
								Pages.hideBusy();
							};

							var newPublication = new CoreStandards.Common.Publication();
							newPublication.setKey(selPublicationVal);

							var ajaxRequest = new CoreStandards.io.AjaxRequest(
									JSON.stringify(newPublication
											.getJsonObject()),
									copyPublicationUrl, copyPublicationMethod,
									copySuccessHandler, errorHandler);
							ajaxRequest.send();

						});

		$("#cancelViewPubBtn").click(function() {
			// Firebug.log("cancelViewPubBtn Button Clicked ");
			Pages.takeToUrl(CommonURLs.homePageUrl(), true);
		});

		$('#publisherSelection').change(
				(function() {
					// Firebug.log("Publisher Dropdown Change : " + this.value);
					selPublisherVal = this.value;
					$('#subjectSelection option:first-child').attr("selected",
							"selected");
					$('#subjectSelection')[0].selectedIndex = 0;
					$('#publicationSelection').children('option:not(:first)')
							.remove();
					that.loadSubjects();

				}));

		$('#subjectSelection').change(
				(function() {
					selSubjectVal = this.value;
					$('#publicationSelection option:first-child').attr(
							"selected", "selected");
					$('#publicationSelection')[0].selectedIndex = 0;

					that.loadPublications();
				}));

		$('#publicationSelection').change((function() {
			selPublicationVal = this.value;
			// Enabling button as publication selected
			$('#copyPublication').attr('disabled', false);
			$('#viewPublication').attr('disabled', false);
		}));

	};

})();

$(document).on("pageLoad", function() {
	// we will first load up the publishers from the rest URL.
	Pages.CopyPublication.loadPublishers();
	var pageName = QueryParameters['pageName'];
	Pages.CopyPublication.attachEventHandlers();

	// Based on pname appropriate section in copypublication.jsp get enables.
	if (pageName) {
		if (pageName == 'view') {
			$('.copyPage').css('display', 'none');
			$('.viewPage').css('display', 'block');
		} else if (pageName == 'copy') {
			$('.viewPage').css('display', 'none');
			$('.copyPage').css('display', 'block');
		}
	}

	// Disabling buttons on page load and enabling them when publication drop
	// down
	// change event
	$('#viewPublication').attr('disabled', true);
	$('#copyPublication').attr('disabled', true);
});
