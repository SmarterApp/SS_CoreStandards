Pages.Header = new (function() {
	var that = this;

	this.attachEventHandlers = function() {
		$("#setupButton").click(function() {
			Pages.takeToUrl(CommonURLs.setupPageUrl(), true);
		});

		$("#publisherSetting").click(
				function(e) {
					Pages.takeToUrl(CommonURLs.setupPageUrl()
							+ '?setupBtnClicked=publisher', true);
					return false;
				});

		$("#gradeSetting").click(
				function(e) {
					Pages.takeToUrl(CommonURLs.setupPageUrl()
							+ '?setupBtnClicked=grade', true);
					return false;
				});

		$("#subjectSetting").click(
				function(e) {
					Pages.takeToUrl(CommonURLs.setupPageUrl()
							+ '?setupBtnClicked=subject', true);
					return false;
				});

		$("#homeButton").click(function() {
			Pages.takeToUrl(CommonURLs.homePageUrl(), true);
		});

		$("#logoutLink").click(
				function() {
					Pages.takeToUrl(CoreStandards.baseUrl
							+ '/saml/logout', true);
				});
		/*
		 * var logoutLinkElement = document.getElementById('logoutLink'); if
		 * (logoutLinkElement != null) { // attach the logout button.
		 * logoutLinkElement.href = CoreStandards.baseUrl +
		 * "/j_spring_security_logout"; }
		 */
	};

})();

$(document).on("pageLoad", function() {
	// we will first load up the publishers from the rest URL.
	Pages.Header.attachEventHandlers();
});
