<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- InstanceBegin template="/Templates/header template.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Core Standards Admin Console</title>
<!-- InstanceEndEditable -->

<%@include file="scripts.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath ()%>/js/common/publisher.js"></script>
<script type="text/javascript" src="<%=request.getContextPath ()%>/js/common/subject.js"></script>
<script type="text/javascript" src="<%=request.getContextPath ()%>/js/common/grade.js"></script>
<script type="text/javascript" src="<%=request.getContextPath ()%>/js/pages/setupAction.js"></script>

</head>

<body>
	<div class="container">

		<%@include file="header.jsp"%>
		<%@include file="popup.jsp"%>
		<div class="content" id="setupBoxContainer" role="main">
			<div class="boxWrap setup" id="settings">
				<%@include file="busyspinner.jsp"%>

			</div>

			<div class="boxWrap setup2" id="fldGrpContainer">
				<div id="addDiv" class="boxBg">
					<div class="saveConfirm">Your changes have been saved.</div>

					<div class="fieldGroup" id="publisherBxWrap">
						<ul>
							<h5 tabindex="-1">Publisher</h5>
							<li><label for="Name">*Name:</label> <input name="Name" title="Name" id="sPubName" size="25"></li>
							<li><label for="Key">*Key:</label> <input name="Key" title="Key" id="sPubKey" size="5"></li>
						</ul>
					</div>
					<div class="fieldGroup" id="subBxWrap">
						<ul>
							<h5 tabindex="-1">Subject</h5>
							<li><label for="Name">*Name:</label> <input name="Name" title="Name" id="sSubjectName" size="25"></li>
							<li><label for="Code">*Code:</label> <input name="Code" title="Code" id="sSubjectCode" size="5"></li>
						</ul>
					</div>
					<div class="fieldGroup" id="gradeBxWrap">
						<ul>
							<h5 tabindex="-1">Grade</h5>
							<li><label for="Name">*Name:</label> <input name="Name" title="Name" id="sGradeName" size="25"></li>
							<li><label for="Description">Description:</label> <input name="Description" title="Description" id="sGradeDescription" size="25"></li>
						</ul>
					</div>
					<div class="btnGroup">
						<button class="boxBtn" id="setupSave" tabindex="0" type="button" value="Save">
							<span class="btnIcon icon_sprite icon_save2" aria-hidden="true"></span><span class="btnText">Save</span>
						</button>
						<button class="boxBtn saveBtn moreBtn" id="setupSaveAddBtn" tabindex="0" type="button" value="Save and Add">
							<span class="btnIcon icon_sprite icon_saveAdd2" aria-hidden="true"></span><span class="btnText">Save and Add More</span>
						</button>
						<button class="boxBtn cancelBtn setupCancleBtn btnCancel" tabindex="0" type="button" value="Cancel">
							<span class="btnIcon icon_sprite icon_cancel2" aria-hidden="true"></span><span class="btnText">Cancel</span>
						</button>
					</div>
				</div>
				<div id="editDiv" class="boxBg">
					<div class="saveConfirm">Your changes have been saved.</div>
					<div class="fieldGroup" id="publisherEditBxWrap">
						<ul>
							<h5 tabindex="-1">Edit Publisher</h5>
							<li><label for="Name">*Name:</label> <input name="Name" title="Name" id="ePubName" size="25" /></li>
							<li><label for="Key">*Key:</label> <input name="Key" title="Key" id="ePubKey" size="5" /></li>
						</ul>
					</div>
					<div class="fieldGroup" id="subEditBxWrap">
						<ul>
							<h5 tabindex="-1">Edit Subject</h5>
							<li><label for="Name">*Name:</label> <input name="Name" title="Name" id="eSubjectName" size="25" /></li>
							<li><label for="Code">*Code:</label> <input name="Code" title="Code" id="eSubjectCode" size="5" /></li>
						</ul>
					</div>
					<div class="fieldGroup" id="gradeEditBxWrap">
						<ul>
							<h5 tabindex="-1">Edit Grade</h5>
							<li><label for="Name">*Name:</label> <input name="Name" title="Name" id="eGradeName" size="25" /></li>
							<li><label for="Description">Description:</label> <input name="Description" title="Description" id="eGradeDescription" size="25" /></li>
						</ul>
					</div>
					<div class="btnGroup">
						<button class="boxBtn" id="saveEdit" tabindex="0" type="button" value="Save">
							<span class="btnIcon icon_sprite icon_save2" aria-hidden="true"></span><span class="btnText">Save</span>
						</button>
						<button id="cancelEdit" class="boxBtn cancelBtn setupCancleBtn btnCancel" tabindex="0" type="button" value="Cancel">
							<span class="btnIcon icon_sprite icon_cancel2" aria-hidden="true"></span><span class="btnText">Cancel</span>
						</button>
					</div>
				</div>
			</div>
				<%@include file="footer.jsp"%>
		</div>
	</div>
</body>
</html>
