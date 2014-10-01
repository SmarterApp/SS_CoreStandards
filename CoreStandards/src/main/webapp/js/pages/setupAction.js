Pages.SetupAction = new (function() {
	var that = this;

	// variable to track which button clicked by the user
	var action = '';
	// variable to track whether used clicked save / SaveAndAddMore
	var addMoreClicked = false;

	var name = '';

	var key = '';

	// JSP id/class variables
	var subBxWrap = "subBxWrap";
	var gradeBxWrap = "gradeBxWrap";
	var publisherBxWrap = "publisherBxWrap";
	var subEditBxWrap = "subEditBxWrap";
	var gradeEditBxWrap = "gradeEditBxWrap";
	var publisherEditBxWrap = "publisherEditBxWrap";
	var fldGrpContainer = "fldGrpContainer";
	var setup = "setup";
	var postCall = "POST";
	var getCall = "GET";
	var editCall = "PUT";
	var deleteCall = "DELETE";

	this.setAction = function(value) {
		action = value;
	};

	this.setNameKey = function(nameV, keyV) {
		name = nameV;
		key = keyV;
	};

	this.attachEventHandlers = function() {

		$(".setupCancleBtn").click(function() {
			// Pages.takeToUrl(CommonURLs.setupPageUrl(), true);
			/*
			 * $("#" + fldGrpContainer).hide(); $(".saveConfirm").hide(); $("." +
			 * setup).show();
			 */
			window.location = document.referrer;
			return false;
		});

		$("#setupSave").click(function() {
			if (action == "addPublisher") {
				that.savePublisher();
			} else if (action == "addSubject") {
				that.saveSubject();
			} else if (action == "addGrade") {
				that.saveGrade();
			}
			addMoreClicked = false;
			return false;
		});

		$("#setupSaveAddBtn").click(function() {
			if (action == "addPublisher") {
				that.savePublisher();
			} else if (action == "addSubject") {
				that.saveSubject();
			} else if (action == "addGrade") {
				that.saveGrade();
			}
			addMoreClicked = true;
			return false;
		});

		$("#saveEdit").click(function() {
			if (action == "editPublisher") {
				that.editPublisher();
			} else if (action == "editSubject") {
				that.editSubject();
			} else if (action == "editGrade") {
				that.editGrade();
			}
			return false;
		});

	};

	this.showPublisher = function() {
		// $("#addDiv").show();
		$("#editDiv").hide();
		$("#" + subBxWrap).hide();
		$("#" + gradeBxWrap).hide();
		$("." + setup).hide();
		$("#" + publisherBxWrap).show();
		$("#" + fldGrpContainer).show();
		setupBtnClicked = "publisher";
	};

	this.showGrade = function() {
		$("#editDiv").hide();
		$("#" + subBxWrap).hide();
		$("#" + publisherBxWrap).hide();
		$("." + setup).hide();
		$("#" + gradeBxWrap).show();
		$("#" + fldGrpContainer).show();
		setupBtnClicked = "grade";
	};

	this.showSubject = function() {
		$("#editDiv").hide();
		$("#" + publisherBxWrap).hide();
		$("#" + gradeBxWrap).hide();
		$("." + setup).hide();
		$("#" + subBxWrap).show();
		$("#" + fldGrpContainer).show();
		setupBtnClicked = "subject";
	};

	this.showEditPublisher = function() {
		$("#addDiv").hide();
		$("#" + subEditBxWrap).hide();
		$("#" + gradeEditBxWrap).hide();
		$("." + setup).hide();
		$("#" + publisherEditBxWrap).show();
		$("#" + fldGrpContainer).show();
		setupBtnClicked = "publisher";
		$("#ePubName").val(name);
		$("#ePubKey").val(key);
	};

	this.showEditGrade = function() {
		$("#addDiv").hide();
		$("#" + subEditBxWrap).hide();
		$("#" + publisherEditBxWrap).hide();
		$("." + setup).hide();
		$("#" + gradeEditBxWrap).show();
		$("#" + fldGrpContainer).show();
		setupBtnClicked = "grade";
		$("#eGradeName").val(name);
		$("#eGradeDescription").val(key);
	};

	this.showEditSubject = function() {
		$("#addDiv").hide();
		$("#" + publisherEditBxWrap).hide();
		$("#" + gradeEditBxWrap).hide();
		$("." + setup).hide();
		$("#" + subEditBxWrap).show();
		$("#" + fldGrpContainer).show();
		setupBtnClicked = "subject";
		$("#eSubjectName").val(name);
		$("#eSubjectCode").val(key);
	};

	this.savePublisher = function() {
		$(".saveConfirm").hide();

		// showing the busy spinner
		Pages.showBusy("Saving publisher information");
		var pubName = StringUtils.emptyChk($('#sPubName').val());
		var pubKey = StringUtils.emptyChk($('#sPubKey').val());

		var url = CoreStandards.Common.Publisher.Settings.restPublisherUrl();

		var pubSaveSuccessHandler = function(data, requestObject) {

			if (data.status == "success") {
				$(".saveConfirm").show();
				if (addMoreClicked) {
					$("#setupBoxContainer").find('input:text').val('');
				} else {
					Pages.takeToUrl(document.referrer, false);
				}
			} else if (data.status == "failed") {
				// show the popup when the status is failed with appropriate
				// message.
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : data.status,
					"errors" : data.errors,
					"warnings" : data.warnings,
					"validationsPassed" : data.validationsPassed,
					"payload" : data.payload != null ? data.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var pubErrorHandler = function(request, status, error, requestObject) {

			Pages.hideBusy();
		};

		var newPublisher = new CoreStandards.Common.Publisher();
		newPublisher.setKey(pubKey);
		newPublisher.setName(pubName);

		var ajaxRequest = new CoreStandards.io.AjaxRequest(JSON
				.stringify(newPublisher.getJsonObject()), url, postCall,
				pubSaveSuccessHandler, pubErrorHandler);
		ajaxRequest.send();

	};

	this.editPublisher = function() {
		$(".saveConfirm").hide();

		// showing the busy spinner
		Pages.showBusy("Saving publisher information");
		var pubName = StringUtils.emptyChk($('#ePubName').val());
		var pubKey = StringUtils.emptyChk($('#ePubKey').val());

		var url = CoreStandards.Common.Publisher.Settings.restPublisherUrl();

		var pubEditSuccessHandler = function(data, requestObject) {

			if (data.status == "success") {
				$(".saveConfirm").show();
				Pages.takeToUrl(document.referrer, false);
			} else if (data.status == "failed") {
				// show the popup when the status is failed with appropriate
				// message.
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : data.status,
					"errors" : data.errors,
					"warnings" : data.warnings,
					"validationsPassed" : data.validationsPassed,
					"payload" : data.payload != null ? data.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var pubErrorHandler = function(request, status, error, requestObject) {

			Pages.hideBusy();
		};

		var oldPublisher = new CoreStandards.Common.Publisher();
		oldPublisher.setKey(key);
		oldPublisher.setName(name);

		var newPublisher = new CoreStandards.Common.Publisher();
		newPublisher.setKey(pubKey);
		newPublisher.setName(pubName);

		var data = '[' + JSON.stringify(oldPublisher.getJsonObject()) + ','
				+ JSON.stringify(newPublisher.getJsonObject()) + ']';

		var ajaxRequest = new CoreStandards.io.AjaxRequest(data, url, editCall,
				pubEditSuccessHandler, pubErrorHandler);
		ajaxRequest.send();

	};

	this.saveSubject = function() {
		$(".saveConfirm").hide();
		Pages.showBusy('Saving subject information');

		var url = CoreStandards.Common.Subject.Settings.restSubjectUrl();
		var subName = StringUtils.emptyChk($('#sSubjectName').val());
		var subCode = StringUtils.emptyChk($('#sSubjectCode').val());

		var subSaveSuccessHandler = function(data, requestObject) {
			Pages.hideBusy();
			if (data.status == "success") {
				$(".saveConfirm").show();
				if (addMoreClicked) {
					$("#setupBoxContainer").find('input:text').val('');
				} else {
					Pages.takeToUrl(document.referrer, false);
				}
			} else if (data.status == "failed") {
				// show the popup when the status is failed with appropriate
				// message.
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : data.status,
					"errors" : data.errors,
					"warnings" : data.warnings,
					"validationsPassed" : data.validationsPassed,
					"payload" : data.payload != null ? data.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var subErrorHandler = function(request, status, error, requestObject) {
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var newSubject = new CoreStandards.Common.Subject();
		newSubject.setName(subName);
		newSubject.setCode(subCode);

		var ajaxRequest = new CoreStandards.io.AjaxRequest(JSON
				.stringify(newSubject.getSubJsonObject()), url, postCall,
				subSaveSuccessHandler, subErrorHandler);
		ajaxRequest.send();

	};

	this.editSubject = function() {
		$(".saveConfirm").hide();

		var url = CoreStandards.Common.Subject.Settings.restSubjectUrl();
		var subjectEditSuccessHandler = function(data, requestObject) {
			Pages.hideBusy();
			if (data.status == "success") {
				$(".saveConfirm").show();
				Pages.takeToUrl(document.referrer, false);
			} else if (data.status == "failed") {
				// show the popup when the status is failed with appropriate
				// message.
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : data.status,
					"errors" : data.errors,
					"warnings" : data.warnings,
					"validationsPassed" : data.validationsPassed,
					"payload" : data.payload != null ? data.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var subjectErrorHandler = function(request, status, error,
				requestObject) {
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var oldSubject = new CoreStandards.Common.Subject();
		oldSubject.setName(name);
		oldSubject.setCode(key);

		var subjectName = StringUtils.emptyChk($('#eSubjectName').val());
		var subjectCode = StringUtils.emptyChk($('#eSubjectCode').val());
		var newSubject = new CoreStandards.Common.Subject();
		newSubject.setName(subjectName);
		newSubject.setCode(subjectCode);

		var data = '[' + JSON.stringify(oldSubject.getSubJsonObject()) + ','
				+ JSON.stringify(newSubject.getSubJsonObject()) + ']';

		var ajaxRequest = new CoreStandards.io.AjaxRequest(data, url, editCall,
				subjectEditSuccessHandler, subjectErrorHandler);
		ajaxRequest.send();
	};

	this.saveGrade = function() {
		$(".saveConfirm").hide();
		Pages.showBusy('Saving grade information');

		var url = CoreStandards.Common.Grade.Settings.restAddGradeUrl();
		var gradeSaveSuccessHandler = function(data, requestObject) {
			Pages.hideBusy();
			if (data.status == "success") {
				$(".saveConfirm").show();
				if (addMoreClicked) {
					$("#setupBoxContainer").find('input:text').val('');
				} else {
					Pages.takeToUrl(document.referrer, false);
				}
			} else if (data.status == "failed") {
				// show the popup when the status is failed with appropriate
				// message.
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : data.status,
					"errors" : data.errors,
					"warnings" : data.warnings,
					"validationsPassed" : data.validationsPassed,
					"payload" : data.payload != null ? data.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var gradeErrorHandler = function(request, status, error, requestObject) {
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var gradeName = StringUtils.emptyChk($('#sGradeName').val());
		var gradeDesc = StringUtils.emptyChk($('#sGradeDescription').val());
		var newGrade = new CoreStandards.Common.Grade();
		newGrade.setName(gradeName);
		newGrade.setDesc(gradeDesc);

		var ajaxRequest = new CoreStandards.io.AjaxRequest(JSON
				.stringify(newGrade.getGradeJsonObject()), url, postCall,
				gradeSaveSuccessHandler, gradeErrorHandler);
		ajaxRequest.send();

	};

	this.editGrade = function() {
		$(".saveConfirm").hide();

		var url = CoreStandards.Common.Grade.Settings.restAddGradeUrl();
		var gradeEditSuccessHandler = function(data, requestObject) {
			Pages.hideBusy();
			if (data.status == "success") {
				$(".saveConfirm").show();
				Pages.takeToUrl(document.referrer, false);
			} else if (data.status == "failed") {
				// show the popup when the status is failed with appropriate
				// message.
				var popupconfig = new CoreStandards.Common.Popup({
					"status" : data.status,
					"errors" : data.errors,
					"warnings" : data.warnings,
					"validationsPassed" : data.validationsPassed,
					"payload" : data.payload != null ? data.payload
							: "Validation Results"
				}, {});

				Pages.PopupManager.displayReturnStatus(popupconfig);
			}
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var gradeErrorHandler = function(request, status, error, requestObject) {
			// turn off the busy spinner.
			Pages.hideBusy();
		};

		var oldGrade = new CoreStandards.Common.Grade();
		oldGrade.setName(name);
		oldGrade.setDesc(key);

		var gradeName = StringUtils.emptyChk($('#eGradeName').val());
		var gradeDesc = StringUtils.emptyChk($('#eGradeDescription').val());
		var newGrade = new CoreStandards.Common.Grade();
		newGrade.setName(gradeName);
		newGrade.setDesc(gradeDesc);

		var data = '[' + JSON.stringify(oldGrade.getGradeJsonObject()) + ','
				+ JSON.stringify(newGrade.getGradeJsonObject()) + ']';

		var ajaxRequest = new CoreStandards.io.AjaxRequest(data, url, editCall,
				gradeEditSuccessHandler, gradeErrorHandler);
		ajaxRequest.send();
	};

})();

$(document).on(
		"pageLoad",
		function() {
			Pages.SetupAction.attachEventHandlers();
			// $("#fldGrpContainer").hide();
			if (QueryParameters["action"] != undefined) {
				Pages.SetupAction.setAction(QueryParameters["action"]);
				if (QueryParameters["name"] != undefined
						&& QueryParameters["key"] != undefined)
					Pages.SetupAction.setNameKey(QueryParameters["name"],
							QueryParameters["key"]);
				if (QueryParameters["action"] == 'addPublisher')
					Pages.SetupAction.showPublisher();
				else if (QueryParameters["action"] == 'addGrade')
					Pages.SetupAction.showGrade();
				else if (QueryParameters["action"] == 'addSubject')
					Pages.SetupAction.showSubject();
				else if (QueryParameters["action"] == 'editPublisher')
					Pages.SetupAction.showEditPublisher();
				else if (QueryParameters["action"] == 'editGrade')
					Pages.SetupAction.showEditGrade();
				else if (QueryParameters["action"] == 'editSubject')
					Pages.SetupAction.showEditSubject();
			}
		});
