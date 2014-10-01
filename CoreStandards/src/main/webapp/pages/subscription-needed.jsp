<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- InstanceBegin template="/Templates/header template.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Core Standards Admin Console</title>
<!-- InstanceEndEditable -->
<%@include file="scripts.jsp"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/pages/home.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#userRole").html("");
		$("#systemsDropdown").hide();
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
		<%@include file="popup.jsp"%>

		<!-- InstanceBeginEditable name="Main Content" -->
		      <div id="homeLayout" class="content" role="main"> 
        <div >
<%--         				<%@include file="busyspinner.jsp"%> --%>
            <div>
           <span>
           		<h2>There was an issue logging in, please ensure you have the proper role and tenancy for this application.</h2>
           	</span>
			<a href="saml/logout"><button value="Logout"/></a> 
		</h1>
        </div>
  
        <%@include file="footer.jsp"%>
        
        
      </div> 
      

		<!-- InstanceEndEditable -->

	</div>
</body>
<!-- InstanceEnd -->
</html>