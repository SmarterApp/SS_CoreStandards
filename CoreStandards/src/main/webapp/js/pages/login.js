Pages.Login = new (function() {
  // var that = this;
  
  this.attachEventHandlers = function() {
	$("#loginButton").click(function() {
	  Pages.showBusy();
	  // submit the login
	  document.loginForm.submit();
	});
  };
})();

$(document).on("pageLoad", function() {
  // we will first load up the publishers from the rest URL.
  Pages.Login.attachEventHandlers();
});
