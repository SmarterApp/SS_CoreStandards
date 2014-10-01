Branding = new (function() {
	 this.loadBranding = function() {
		  $.getJSON(
				  StringUtils.format(
							 '{0}/api/getBranding',
							 [ CoreStandards.baseUrl ]),
				  	function(branding) 
				  	{
						$("#homeButton").attr("src",branding.imgURL);
						$("#homeButton").attr("alt",branding.title);
				  	});
	  };
	  
	  
});
$(document).ready(function() {
	Branding.loadBranding();
});
