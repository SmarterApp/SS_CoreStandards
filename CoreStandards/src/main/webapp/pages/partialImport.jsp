
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- InstanceBegin template="../../../../../Documents/Templates/header template.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Core Standards Admin Console</title>
<!-- InstanceEndEditable -->
<%@include file="scripts.jsp"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common/partialImport.js"></script>

<!--[if gte IE 9]>
  <style type="text/css">
    .gradient { filter: none; }
  </style>
<![endif]-->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->

</head>

<body>
	<div class="container">

		<%@include file="header.jsp"%>
		<%@include file="popup.jsp"%>
		<!-- InstanceBeginEditable name="Main Content" -->
		<div class="content" role="main">
			<div class="boxWrap import">
				<%@include file="busyspinner.jsp"%>
				<div class="boxBg">
					<span class="boxIcon icon_sprite icon_update1" aria-hidden="true"></span> <span class="boxTitle"><h2 tabindex="-1">Import
							Your OpenOffice Spreadsheet</h2></span>
					<div class="fileGroup">
						<form name='partialImportForm' enctype='multipart/form-data'
							id='partialImportForm'
							action='<%=request.getContextPath()%>/partialUploadHandler'
							method='post'>
							<label for="Upload">File to Upload:</label> <input type="file"
								name="myfile" title="Upload" id="partialupload" />

						</form>
					</div>
					<div class="btnPartialGroup" style="text-align: center">
						<button id="importPartialButton" class="boxBtn prvBtn" tabindex="0" type="button" value="Preview">
							<span class="btnIcon icon_sprite icon_preview2" aria-hidden="true"></span><span class="btnText">Preview
								File</span>
						</button>
					</div>
				</div>
			</div>
				<%@include file="footer.jsp"%>
		</div>
		<!-- InstanceEndEditable -->

	</div>
</body>
<!-- InstanceEnd -->
</html>
