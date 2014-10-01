/*
 * Common class for all ajax requests. send method will call success error
 * handler based on response
 */
CoreStandards.io = {};

CoreStandards.io.AjaxRequestId = (new function() {
	var maxId = 0;
	this.getId = function() {
		maxId++;
		return maxId;
	};
});

CoreStandards.io.AjaxRequest = function(data, url, methodType, 
		successFunction, errorFunction) {


	var myId = CoreStandards.io.AjaxRequestId.getId();
	var myData = data;
	var mySuccessHandler = successFunction;
	var myErrorHandler = errorFunction;
	var myUrl = url;
	var myRetrievedData = null;
	var that = this;

	var myType = methodType;

	this.getSentData = function() {
		return myData;
	};

	this.getRequestId = function() {
		return myId;
	};

	this.send = function() {
		$.ajax({
			contentType : "application/json",
			dataType : 'json',
			type : myType,
			url : myUrl,
			data : myData,
			success : function(respData) {
				myRetrievedData = respData;
				mySuccessHandler(myRetrievedData, that);
			},
			error : function(request, status, error) {
				var responseData = {};
				if (request != null && request.responseText != null)
					responseData = eval('(' + request.responseText + ')');
				myErrorHandler(responseData, status, error, that);
			}
		});
	};
};