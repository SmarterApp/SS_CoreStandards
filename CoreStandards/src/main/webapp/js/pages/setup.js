Pages.Setup = new (function() {
	var that = this;

	// variable to track which button clicked by the user
	var setupBtnClicked = '';
	// variable to track whether used clicked save / SaveAndAddMore
	var addMoreClicked = false;

	var addAction = '';

	var editAction = '';

	var deleteCall = 'DELETE';

	this.setSetupBtn = function(value) {
		setupBtnClicked = value;
		if (setupBtnClicked == "publisher") {
			addAction = "addPublisher";
			editAction = "editPublisher";
		} else if (setupBtnClicked == "grade") {
			addAction = "addGrade";
			editAction = "editGrade";
		} else if (setupBtnClicked == "subject") {
			addAction = "addSubject";
			editAction = "editSubject";
		}
	};

	this.attachEventHandlers = function() {

		$("#addPublisher").click(
				function() {
					var url = StringUtils.format(
							'{0}/pages/setupAction.jsp?action=addPublisher',
							[ CoreStandards.baseUrl ]);
					Pages.takeToUrl(url, false);
				});

		$("#addGrade").click(
				function() {
					var url = StringUtils.format(
							'{0}/pages/setupAction.jsp?action=addGrade',
							[ CoreStandards.baseUrl ]);
					Pages.takeToUrl(url, false);
				});

		$("#addSubject").click(
				function() {
					var url = StringUtils.format(
							'{0}/pages/setupAction.jsp?action=addSubject',
							[ CoreStandards.baseUrl ]);
					Pages.takeToUrl(url, false);
				});

		$('[name="edit"]')
				.click(
						function() {
							var name = $(this).parent().parent().children().eq(
									0).text();
							var key = $(this).parent().parent().children()
									.eq(1).text();
							var url = StringUtils
									.format(
											'{0}/pages/setupAction.jsp?action={1}&name={2}&key={3}',
											[ CoreStandards.baseUrl,
													editAction, name, key ]);
							Pages.takeToUrl(url, false);
						});

		$('[name="delete"]').click(function() {
			var name = $(this).parent().parent().children().eq(0).text();
			var key = $(this).parent().parent().children().eq(1).text();
			if (setupBtnClicked == "publisher")
				Pages.Setup.deletePublisher(name, key);
			else if (setupBtnClicked == "grade")
				Pages.Setup.deleteGrade(name, key);
			else if (setupBtnClicked == "subject")
				Pages.Setup.deleteSubject(name, key);
		});
	};

	this.deletePublisher = function(name, key) {
		$(".saveConfirm").hide();

		// showing the busy spinner

		var url = CoreStandards.Common.Publisher.Settings.restPublisherUrl();

		var pubDeleteSuccessHandler = function(data, requestObject) {

			if (data.status == "success") {
				$(".saveConfirm").show();
				Pages.takeToUrl(document.URL, false);
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

		var publisher = new CoreStandards.Common.Publisher();
		publisher.setKey(key);
		publisher.setName(name);

		var d = JSON.stringify(publisher.getJsonObject());

		var ajaxRequest = new CoreStandards.io.AjaxRequest(d, url, deleteCall,
				pubDeleteSuccessHandler, pubErrorHandler);
		ajaxRequest.send();
	};

	this.deleteGrade = function(name, key) {
		$(".saveConfirm").hide();

		// showing the busy spinner

		var url = CoreStandards.Common.Grade.Settings.restAddGradeUrl();

		var gradeDeleteSuccessHandler = function(data, requestObject) {

			if (data.status == "success") {
				$(".saveConfirm").show();
				Pages.takeToUrl(document.URL, false);
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

			Pages.hideBusy();
		};

		var grade = new CoreStandards.Common.Grade();
		grade.setDesc(key);
		grade.setName(name);

		var d = JSON.stringify(grade.getGradeJsonObject());

		var ajaxRequest = new CoreStandards.io.AjaxRequest(d, url, deleteCall,
				gradeDeleteSuccessHandler, gradeErrorHandler);
		ajaxRequest.send();
	};

	this.deleteSubject = function(name, key) {
		$(".saveConfirm").hide();

		// showing the busy spinner

		var url = CoreStandards.Common.Subject.Settings.restSubjectUrl();

		var subjectDeleteSuccessHandler = function(data, requestObject) {

			if (data.status == "success") {
				$(".saveConfirm").show();
				Pages.takeToUrl(document.URL, false);
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

			Pages.hideBusy();
		};

		var subject = new CoreStandards.Common.Subject();
		subject.setCode(key);
		subject.setName(name);

		var d = JSON.stringify(subject.getSubJsonObject());

		var ajaxRequest = new CoreStandards.io.AjaxRequest(d, url, deleteCall,
				subjectDeleteSuccessHandler, subjectErrorHandler);
		ajaxRequest.send();
	};

})();

$(document).on("pageLoad", function() {
	Pages.Setup.attachEventHandlers();
	// $("#fldGrpContainer").hide();
	if (QueryParameters["setupBtnClicked"] != undefined) {
		Pages.Setup.setSetupBtn(QueryParameters["setupBtnClicked"]);
	}
});
