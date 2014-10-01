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
		<%@include file="popup.jsp"%>

		<!-- InstanceBeginEditable name="Main Content" -->
		<div class="content" role="main">
		  <div class="secContent">
			<div class="boxWrap importPrev">
				<%@include file="busyspinner.jsp"%>
				<div class="boxBg">
					<span class="boxTitle"><h2 tabindex="-1">Copy
							Existing Publication</h2></span>
					<div class="fieldGroup">
						<h2 tabindex="-1">File Preview and Edit</h2>
						<span class="note">Please click on the tabs to navigate to
							different sections</span>
						<ul class="tabs" role="navigation">
							<li id="publisher" class="publisher">Publisher</li>
							<li id="categories">Categories</li>
							<li id="standards">Standards</li>
							<li id="socks">SOCKS</li>
							<li id="benchmark">Benchmark Grades</li>
						</ul>
						<div class="blueRule"></div>
						<br /> 
						    <div id="dispVersionNumb">
						    	<span class="note" id="maxVersionNumb">Please provide a new Version Number</span>
							</div>
							<div id="dispCatLbl" style="display:none">
								<span class="note" >Please edit the Category label</span>
							</div>
							<div id="dispStdDesc" style="display:none">
								<span class="note" >Please edit the Standard Description and/or Short Name</span>
							</div>
							<div id="dispKnowledgeCat" style="display:none">
								<span class="note" >Please edit the Knowledge Categories and/or Description</span>
							</div>
							<div id="dispBenchmarkGrd" style="display:none">
								<span class="note" >Please edit the Benchmark Grades</span>
							</div>
						<div>&nbsp;</div>


						<%@include file="tableGridView.jsp"%>

						<div class="btnGroup btnCenter">
							<!-- <button class="boxBtn prvBtn" id="savePrvBtn" onclick="openPopUp()">  -->
							<button class="boxBtn prvBtn" id="savePrvBtn" tabindex="0" type="button" value="Save">
								<span class="btnIcon  icon_sprite icon_save2" aria-hidden="true"></span><span class="btnText">Save
									as New Publication</span>
							</button>
							<button class="boxBtn prvBtn btnCancel" id="cpPrvwPubCancel" tabindex="0" type="button" value="Cancel">
								<span class="btnIcon  icon_sprite icon_cancel2" aria-hidden="true"></span><span class="btnText">Cancel</span>
							</button>
						</div>
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
