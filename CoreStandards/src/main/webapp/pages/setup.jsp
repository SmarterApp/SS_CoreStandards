<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="ex" uri="/WEB-INF/classes/custom.tld"%>

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
<script type="text/javascript" src="<%=request.getContextPath ()%>/js/pages/setup.js"></script>

</head>

<body>
	<div class="container">

		<%@include file="header.jsp"%>
		<%@include file="popup.jsp"%>
		<!-- InstanceBeginEditable name="Main Content" -->
		<div class="content" id="setupBoxContainer" role="main">
			<div class="boxWrap setup" id="settings">
				<%@include file="busyspinner.jsp"%>

			</div>

			<div id="fldGrpContainer">
				<div class="boxWrap setup">
					<%
					  String str = request.getParameter ("setupBtnClicked");
					%>
					<%
					  if (("publisher").equals (str)) {
					%>
					<h5 tabindex="-1">Publisher</h5>
					<ex:ShowTable message="publisher" styleClass="" />

					<button class="boxBtn" id="addPublisher" tabindex="0" type="button" value="Add Publiser">
						<span class="btnText">Add Publisher</span>
					</button>
					<%
					  }
					%>
					<%
					  if (("subject").equals (str)) {
					%>
					<h5 tabindex="-1">Subject</h5>
					<ex:ShowTable message="subject" styleClass="" />
					<div class ="buttonRow">
					</class><button class="boxBtn" id="addSubject" tabindex="0" type="button" value="Add Subject">
						<span class="btnText">Add Subject</span>
					</button>
					</div>
					<%
					  }
					%>
					<%
					  if (("grade").equals (str)) {
					%>
					<h5 tabindex="-1">Grade</h5>
					<ex:ShowTable message="grade" styleClass="" />
					<button class="boxBtn" id="addGrade" tabindex="0" type="button" value="Add Grade">
						<span class="btnText">Add Grade</span>
					</button>
					<%
					  }
					%>
				</div>
			</div>
			<%@include file="footer.jsp"%>
		</div>
			
	</div>

</body>
<!-- InstanceEnd -->
</html>
