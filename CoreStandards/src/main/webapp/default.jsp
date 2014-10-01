<!DOCTYPE html>
<html>

<head>
<meta charset="ISO-8859-1">
<title>Test page for JSON data post</title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.8.3.js"></script>
<script lang="JavaScript">
	function log(object) {
		if (typeof (console) != 'undefined')
			console.log(object);
	}

	function sendjsonobject() {

		var e = document.getElementById('selectedOperation');
		var selectUrl = e.options[e.selectedIndex].value;
		if (selectUrl == "staging/version") {
			selectUrl = selectUrl + document.getElementById("inputJSON").value;
		}
		var baseUrl = '<%=request.getContextPath()%>' + '/api/';
		//	var baseUrl = ${pageContext.request.getServerName()} + ${pageContext.request.getServerPort()} + ${pageContext.request.contextPath} + "/REST/";
		var url = baseUrl + selectUrl;
		var postUrl = url;
		//	alert("doAjaxPost Called :" + postUrl);

		$.ajax({
			contentType : "application/json",
			dataType : 'json',
			type : "PUT",
			url : postUrl,
			data : document.getElementById("inputJSON").value, //json serialization (like array.serializeArray() etc)

			success : function(data) {
				var txtArea = document.getElementById('outputJSON');
				txtArea.value = JSON.stringify(data);
			},
			error : function(request, status, error) {
				alert('Error: ' + error);
			}
		});
	}
</script>
</head>
<body>
	<div>
		<b> JSON Data to post: </b>
	</div>
	<div>
		<!--  additional attributes for testarea listed in http://www.quackit.com/html/codes/html_text_box_code.cfm -->
		<table>
			<tr>
				<td>
					<textarea name="inputJSON" rows="10" cols="80"
							id="inputJSON">
					</textarea> &nbsp;&nbsp; 
					<select name="mydropdown" id="selectedOperation">
						<option value="publisher">Add Publisher</option>
						<!-- <option value="Publishers/Publisher/Modify">Modify
							Publisher</option>  
						<option value="Publishers/Publisher/Delete">Delete
							Publisher</option> -->
						<option value="subject">Add Subject</option>
						<option value="grade">Add Grade Level</option>
					</select> &nbsp;&nbsp; 
					<INPUT TYPE="button" NAME="button" Value="Submit"
					onClick="sendjsonobject();">
				</td>
			</tr>
		</table>
		<br />
		<div>
			<b> Result: </b>
			<div>
				<!--  additional attributes for testarea listed in http://www.quackit.com/html/codes/html_text_box_code.cfm -->
				<textarea name="resultJSON" rows="10" cols="80" id="outputJSON">
				</textarea>
				<p></p>
			</div>
		</div>
	</div>
</body>