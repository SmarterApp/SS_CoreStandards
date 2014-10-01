Pages.PreviewEditPublication = new (function() {

	// var that = this;
	var gridContainer = 'publicationTableContainer';
	var gridTable = 'publisherTbl';
	var gridNavigator = 'navigator';
	var gridConfig = [];
	var sourceURL = '';
	var saveURL = '';
	var publisherGrid = new CoreStandards.grid.PublisherGrid(gridTable,
			gridNavigator);
	var categoryGrid = '';
	var socksGrid = '';
	var standardsGrid = '';
	var benchmarkGrid = '';
	var fFlag = false;
	var postCall = "POST";
	var getCall = "GET";
	var delCall = "DELETE";
	// Div id(s) for copyPreviewPublication page
	var dispVersionNumb = 'dispVersionNumb';
	var dispCatLbl = 'dispCatLbl';
	var dispStdDesc = 'dispStdDesc';
	var dispKnowledgeCat = 'dispKnowledgeCat';
	var dispBenchmarkGrd = 'dispBenchmarkGrd';

	var tFlag = (function() {
		var pName = QueryParameters['pageName'];
		if (pName == 'copy') {
			return true;
		} else if (pName == 'view') {
			return false;
		}
	})();

	this.attachEventHandlers = function() {

		$('#vEditBtn').click(function(e) {
			Pages.takeToUrl(CommonURLs.prvwPubCopyPageUrl(), true);
		});

		$('#vPartialImport').click(function(e) {
			Pages.takeToUrl(CommonURLs.partialImportPageUrl(), true);
		});

		$('#cpPrvwPubCancel, #cancelPreview').click(
				function(e) {
					// Firebug.log("cpPrvwPubCancel Event.... ");
					var cpPrvwPubCancelUrl = StringUtils.format(
							'{0}/api/staging', [ CoreStandards.baseUrl ]);

					var cpPrvwCancelSuccessHandler = function(jsondata,
							requestObject) {
						Pages.takeToUrl(CommonURLs.homePageUrl(), true);
					};

					var errorHandler = function(request, status, error,
							requestObject) {
						// Pages.hideBusy();
					};

					var ajaxRequest = new CoreStandards.io.AjaxRequest('',
							cpPrvwPubCancelUrl, delCall,
							cpPrvwCancelSuccessHandler, errorHandler);

					ajaxRequest.send();

				});

		$('#publisher').click(
				function(e) {
					buildPublisherGridParam();
					publisherGrid
							.loadPublishers(sourceURL, saveURL, gridConfig);
					// First Parameter should be of versionNumber message
					CoreStandards.Common.Grid.showFirstHideRestDivs(
							dispVersionNumb, dispCatLbl, dispStdDesc,
							dispKnowledgeCat, dispBenchmarkGrd);
					e.preventDefault();
				});

		$('#categories')
				.click(
						function(e) {
							categoryGrid = new CoreStandards.grid.CategoryGrid(
									gridTable, gridNavigator);
							buildCategoryGridParam();
							categoryGrid.loadCategories(sourceURL, saveURL,
									gridConfig);
							// First Parameter should be of categories message
							CoreStandards.Common.Grid.showFirstHideRestDivs(
									dispCatLbl, dispVersionNumb, dispStdDesc,
									dispKnowledgeCat, dispBenchmarkGrd);
							e.preventDefault();
						});

		$('#standards')
				.click(
						function(e) {
							standardsGrid = new CoreStandards.grid.StandardsGrid(
									gridTable, gridNavigator);
							buildStandardGridParam();
							standardsGrid.loadStandards(sourceURL, saveURL,
									gridConfig);
							// First Parameter should be of standards message
							CoreStandards.Common.Grid.showFirstHideRestDivs(
									dispStdDesc, dispCatLbl, dispVersionNumb,
									dispKnowledgeCat, dispBenchmarkGrd);
							e.preventDefault();
						});

		$('#socks').click(
				function(e) {
					socksGrid = new CoreStandards.grid.SocksGrid(gridTable,
							gridNavigator);
					buildSocksGridParam();
					socksGrid.loadSocks(sourceURL, saveURL, gridConfig);
					// First Parameter should be of socks message
					CoreStandards.Common.Grid.showFirstHideRestDivs(
							dispKnowledgeCat, dispStdDesc, dispCatLbl,
							dispVersionNumb, dispBenchmarkGrd);
					e.preventDefault();
				});

		$('#benchmark').click(
				function(e) {
					benchmarkGrid = new CoreStandards.grid.BenchmarkGrid(
							gridTable, gridNavigator);
					buildBenchmarkGridParam();
					benchmarkGrid.loadBenchmarkGrades(sourceURL, saveURL,
							gridConfig);
					// First Parameter should be of benchmark message
					CoreStandards.Common.Grid.showFirstHideRestDivs(
							dispBenchmarkGrd, dispKnowledgeCat, dispStdDesc,
							dispCatLbl, dispVersionNumb);
					e.preventDefault();
				});

		$('#savePrvBtn')
				.click(
						function(e) {
							Pages.showBusy();
							var dataToSend = '';
							var url = CoreStandards.Common.Publication.Settings
									.restSavePublicationUrl();
							var saveSuccessHandler = function(data,
									requestObject) {

								// show the popup when the status is failed with
								// appropriate
								// message.
								if (data.status == "failed") {
									var popupconfig = new CoreStandards.Common.Popup(
											{
												"status" : data.status,
												"errors" : data.errors,
												"warnings" : data.warnings,
												"validationsPassed" : data.validationsPassed,
												"payload" : data.payload != null ? data.payload
														: "Validation Results"
											}, {});

									Pages.PopupManager
											.displayReturnStatus(popupconfig);
									// turn off the busy spinner.
									Pages.hideBusy();
								} else if (data.status == "success") {
									//go home here
									Pages.hideBusy();
									Pages.takeToUrl(CommonURLs.homePageUrl(), true);
									//$("#" + gridTable).trigger('reloadGrid');									
								}
								
							};

							var errorHandler = function(request, status, error,
									requestObject) {
								if (request.status == "failed") {
									var popupconfig = new CoreStandards.Common.Popup(
											{
												"status" : request.status,
												"errors" : request.errors,
												"warnings" : request.warnings,
												"validationsPassed" : request.validationsPassed,
												"payload" : request.payload != null ? request.payload
														: "Validation Results"
											}, {});

									Pages.PopupManager
											.displayReturnStatus(popupconfig);
								}

								Pages.hideBusy();
							};
							var ajaxRequest = new CoreStandards.io.AjaxRequest(
									dataToSend, url,postCall, saveSuccessHandler,
									 errorHandler);
							ajaxRequest.send();
						});
	};

	function buildPublisherGridParam() {
		sourceURL = CoreStandards.Common.Publication.Settings
				.restPublicationUrl();
		saveURL = CoreStandards.Common.Publication.Settings
				.restPublicationVersionUrl();

		var pName = publisherGrid.getPublisherName();
		var pDesc = publisherGrid.getPublicationDescription();
		var pSubj = publisherGrid.getSubject();
		var pVersion = publisherGrid.getVersion();

		gridConfig = [ {
			'columnname' : pName,
			'editable' : fFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : pDesc,
			'editable' : fFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : pSubj,
			'editable' : fFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : pVersion,
			'editable' : tFlag,
			'callbackHandler' : ''
		} ];
	}

	function buildCategoryGridParam() {
		sourceURL = CoreStandards.Common.Publication.Settings
				.restPreviewCategoriesUrl();
		saveURL = CoreStandards.Common.Publication.Settings
				.restSaveCategoriesGridUrl();
		var cName = categoryGrid.getCategoryName();
		var cTreeLevel = categoryGrid.getTreeLevelName();

		// sourceURL = sourceURL + pName;

		gridConfig = [ {
			'columnname' : cName,
			'editable' : tFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : cTreeLevel,
			'editable' : fFlag,
			'callbackHandler' : ''
		} ];
	}

	function buildStandardGridParam() {
		sourceURL = CoreStandards.Common.Publication.Settings
				.restPreviewStandardsUrl();
		saveURL = CoreStandards.Common.Publication.Settings
				.restStandardssGridUrl();

		var stdDescription = standardsGrid.getDescription();
		var stdShortName = standardsGrid.getShortName();
		var stdName = standardsGrid.getName();
		var stdLevel = standardsGrid.getLevel();
		var stdKey = standardsGrid.getKey();

		gridConfig = [ {
			'columnname' : stdLevel,
			'editable' : fFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : stdKey,
			'editable' : fFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : stdName,
			'editable' : fFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : stdDescription,
			'editable' : tFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : stdShortName,
			'editable' : tFlag,
			'callbackHandler' : ''
		} ];
	}

	function buildSocksGridParam() {
		sourceURL = CoreStandards.Common.Publication.Settings
				.restPreviewSocksUrl();
		saveURL = CoreStandards.Common.Publication.Settings
				.restSaveSocksGridUrl();
		var sName = socksGrid.getKnowledgeCategory();
		var sDesc = socksGrid.getDescription();

		gridConfig = [ {
			'columnname' : sName,
			'editable' : tFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : sDesc,
			'editable' : tFlag,
			'callbackHandler' : ''
		} ];
	}

	function buildBenchmarkGridParam() {
		sourceURL = CoreStandards.Common.Publication.Settings
				.restGradesGridUrl();
		saveURL = CoreStandards.Common.Publication.Settings.restGradesGridUrl();

		var benchmarkName = benchmarkGrid.getBenchmarkName();
		var benchmarkGrade = benchmarkGrid.getBenchmarkGrade();

		gridConfig = [ {
			'columnname' : benchmarkName,
			'editable' : fFlag,
			'callbackHandler' : ''
		}, {
			'columnname' : benchmarkGrade,
			'editable' : tFlag,
			'callbackHandler' : ''
		} ];
	}

	this.loadPublisherOnPageLoad = function() {
		// loadPublishers();
		var publisherGrid = new CoreStandards.grid.PublisherGrid(gridTable,
				gridNavigator);
		buildPublisherGridParam();
		publisherGrid.loadPublishers(sourceURL, saveURL, gridConfig);
	};

	this.getNextPublicationVersionOnPageLoad = function() {
		var versUrl = CoreStandards.Common.Publication.Settings
				.restGetNextVersionUrl();
		var versSuccessHandler = function(jsondata, requestObject) {
			if (jsondata.status == "success" && jsondata.payload != null) {
				$('#maxVersionNumb').append(
						"<span class='noteBold'> " + parseInt(jsondata.payload)
								+ "</span><span> and above</span>");
			}
			Pages.hideBusy();
		};

		var errorHandler = function(request, status, error, requestObject) {
			Pages.hideBusy();
		};

		var ajaxRequest = new CoreStandards.io.AjaxRequest('', versUrl,
				getCall, versSuccessHandler, errorHandler);
		ajaxRequest.send();

	};

	/*
	 * Code to resize the grid when user shrink the page horizontal or vertical.
	 * Reloading the grid with new height and width
	 */
	this.resizeGrid = function() {
		setTimeout(function() {
			var toppager_height = $("#" + gridNavigator).outerHeight(true);
			var header_height = $(
					"div#gview_" + gridTable + " > div.ui-jqgrid-hdiv")
					.outerHeight(true);
			var height = $(window).height()
					- $('#' + gridContainer)[0].offsetTop - toppager_height
					- header_height - 2;

			$('#' + gridTable).jqGrid("setGridWidth",
					$('#' + gridContainer).width(), true);
			if (height > 60)
				$('#' + gridTable).jqGrid('setGridHeight', height);
		}, 100);
		$("#" + gridTable).trigger('reloadGrid');
	};

})();

$(document).on("pageLoad", function() {
	Pages.PreviewEditPublication.attachEventHandlers();
	Pages.PreviewEditPublication.loadPublisherOnPageLoad();
	Pages.PreviewEditPublication.getNextPublicationVersionOnPageLoad();

	$(window).resize(function() {
		Pages.PreviewEditPublication.resizeGrid();

		// on window resize, display the partial , download buttons look
		// appropriate
		if ($('#partialUpdateSS').innerWidth() <= 191) {
			$('#partialUpdateSS').addClass("noMargin");
		} else {
			$('#partialUpdateSS').removeClass("noMargin");
		}

		if ($('#downloadOOText').innerWidth() <= 151) {
			$('#downloadOOText').addClass("noMargin");
		} else {
			$('#downloadOOText').removeClass("noMargin");
		}
	});

	// on load, logic to make the partial , download buttons look appropriate
	if ($('#partialUpdateSS').innerWidth() <= 191) {
		$('#partialUpdateSS').addClass("noMargin");
	} else {
		$('#partialUpdateSS').removeClass("noMargin");
	}

	if ($('#downloadOOText').innerWidth() <= 151) {
		$('#downloadOOText').addClass("noMargin");
	} else {
		$('#downloadOOText').removeClass("noMargin");
	}

});