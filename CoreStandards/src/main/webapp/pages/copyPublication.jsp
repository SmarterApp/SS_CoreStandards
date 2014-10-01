<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- InstanceBegin template="../../../../../Documents/Templates/header template.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Core Standards Admin Console</title>

<%@include file="scripts.jsp"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common/publisher.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common/subject.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common/publication.js"></script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/pages/copyPublication.js"></script>

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
		<%@include file="slide-menu.jsp"%>

		<!-- InstanceBeginEditable name="Main Content" -->
		<div class="content" role="main">
				  <div class="secContent">
			<div class="boxWrap import">
				<%@include file="busyspinner.jsp"%>
				<div class="boxBg">
						<span class="boxTitle copyPage"><h2 tabindex="-1">Copy Existing
							Publication</h2></span> <span class="boxTitle viewPage"><h2 tabindex="-1">View
							Existing Publication</h2></span>
					<div class="fileGroup">
						<div>
							<label for="Publisher">Publisher:</label> <select
								id="publisherSelection" name="publisher" style="width: 200px;">
								<option val="selectPub">Select a Publisher</option>
							</select>
						</div>
						<div>
							<label for="Subject">Subject:</label> <select name="subject"
								id="subjectSelection" style="width: 200px;">
								<option val="selectSub">Select a Subject</option>
							</select>
						</div>
						<div>
							<label for="Publication">Publication:</label> <select
								name="publication" id="publicationSelection"
								style="width: 200px;">
								<option val="selectPublication">Select a Publication</option>
							</select>
						</div>
					</div>
					<div class="btnGroup">
						<span class="copyPage">
							<button class="boxBtn prvBtn" id="copyPublication" type="button" tabindex="0" value="Copy to New Publication">
								<span class="btnIcon icon_sprite icon_copy2" aria-hidden="true""></span> <span class="btnText">Copy
									to New Publication</span>
							</button>
						</span> <span class="viewPage">
							<button class="boxBtn viewPublication" id="viewPublication" type="button" tabindex="0" value="view">
								<span class="btnIcon icon_sprite icon_save2" aria-hidden="true"></span> <span
									class="btnText">View</span>
							</button>
							<button class="boxBtn cancelBtn cancelViewPubBtn btnCancel" id="cancelViewPubBtn" type="button" tabindex="0" value="cancel">
								<span class="btnIcon icon_sprite icon_cancel2" aria-hidden="true"></span> <span
									class="btnText">Cancel</span>
							</button>
						</span>
					</div>
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
