<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- InstanceBegin template="../../../../../Documents/Templates/header template.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Core Standards Admin Console</title>
<%@include file="scripts.jsp"%>
<%@include file="gridIncudes.jsp"%>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common/publication.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/pages/previewEditPublication.js"></script>


<script>
	$(document).ready(function() {

		/* tabs */
		$("#contGroup div").hide();
		$("#publisher").addClass("tabOn");
		$("#publisher_cont").show();
		$(".tabs li").click(function(event) {
			$("#contGroup div").hide();
			var show = "#" + event.target.id + "_cont";
			$(show).show();
			$(".tabs").find("li").removeClass("tabOn").each(function(element) {
				element.className = null;
			});
			event.target.className = "tabOn";
		});
	});
</script>

<!-- InstanceEndEditable -->

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
				<%@include file="slide-menu.jsp"%>
		

		<!-- InstanceBeginEditable name="Main Content" -->
		<div class="content" role="main">
				  <div class="secContent">
		
			<div class="boxWrap importPrev">
				<%@include file="busyspinner.jsp"%>
				<div class="boxBg">
					 <span class="boxTitle"><h2 tabindex="-1">View
							Publication</h2></span>
					<div class="fieldGroup">
						<h2 tabindex="-1">Preview Selected Publication</h2>
						<span class="note">Please click on the tabs to navigate to
							different sections</span>
						<ul class="tabs" role="navigation">
							<li id="publisher" role="button" tabindex="0">Publisher</li>
							<li id="categories" role="button" tabindex="0">Categories</li>
							<li id="standards" role="button" tabindex="0">Standards</li>
							<li id="socks" role="button" tabindex="0">SOCKS</li>
							<li id="benchmark" role="button" tabindex="0">Benchmark Grades</li>
						</ul>
						<div class="blueRule"></div>
						<div>&nbsp;</div>

						<%@include file="tableGridView.jsp"%>
					</div>
					<div class="btnGroup viewPreviewBtnGroup">
						<form name='fileDownloadForm' enctype='multipart/form-data'
							id='fileDownloadForm'
							action='<%=request.getContextPath()%>/FileDownloadHandler'
							method='post'>
							<button class="boxBtn pviewBtn floatLeft">
								<span class="btnIcon icon_sprite icon_save2" aria-hidden="true"></span> <span
									class="updateBtn" id="downloadOOText">Download to OpenOffice</span>
							</button>
							<button class="boxBtn pviewBtn floatLeft btnCancel" id="cancelPreview" tabindex="0" type="button" value="Cancel">
		                    	<span class="btnIcon icon_sprite icon_cancel2" aria-hidden="true"></span><span class="btnText">Cancel</span>
		                    </button>
					</form>
                   <!--  <button class="boxBtn pviewBtn floatLeft" id="vEditBtn" tabindex="0" type="button" value="Edit">
                    	<span class="btnIcon icon_sprite icon_edit2" aria-hidden="true"></span><span class="btnText">Edit</span>
                    </button>
                    <button class="boxBtn pviewBtn floatLeft" id="vPartialImport" tabindex="0" type="button" value="Partial Update via Spreadsheet">
                    	<span class="btnIcon icon_sprite icon_update2" aria-hidden="true"></span><span class="updateBtn" id="partialUpdateSS">Partial Update via Spreadsheet</span>
                    </button> -->
                    
                    <div style="clear:both"></div>
                </div>
            </div>       
        </div>
        </div>
        <%@include file="footer.jsp"%>
    </div>
	<!-- InstanceEndEditable -->
          
    </div>
</div>
</body>
<!-- InstanceEnd -->
</html>
