BuildInfo = new (function() {
	 this.loadVersionNumber = function() {
		  $.getJSON(
				  StringUtils.format(
							 '{0}/api/version',
							 [ CoreStandards.baseUrl ]),
				  	function(buildInfo) 
				  	{
					  	var versionInfo = '';
					  	if(buildInfo.appVersion!='') {
					  		versionInfo = 'Version: '+buildInfo.appVersion;
						  	if(buildInfo.jenkinsBuildNumber!='') {
						  		versionInfo += '<span> (' +buildInfo.jenkinsBuildNumber+')</span>';
						  	}
					  	}
						$("#versionFooter").html(versionInfo);
				  	});
	  };
	  
	  
});
$(document).ready(function() {
	BuildInfo.loadVersionNumber();
});
