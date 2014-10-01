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
        <div class="boxWrap home">
<%--         				<%@include file="busyspinner.jsp"%> --%>
            <div class="boxBg">
                                <span class="homeTitle"><h2 tabindex="-1">Create Standards Publications</h2></span>
                <div class="btnGroup">
                    <button class="homeBtn green arrow" id="importExcel" value="Create Publication From Spreadsheet" type="button" tabindex="0">
                    	<span class="btnText" role="button">
                    		Create <br>Publication from Spreadsheet</br>
                   		</span>
                   		<span class="homeIcon createPublication" aria-hidden="true"></span>
                   		<p>Upload publication spreadsheets to create <br>a new publication</br></p>
                   	</button>
                    <button class="homeBtn navy" id="editPublication" value="View Existing Publication" type="button" tabindex="0">
                    	<span class="btnText" role="button">
                    		View <br>Existing Publication</br>
                   		</span>
                   		<span class="homeIcon viewPublications" aria-hidden="true"></span>
                   		<p>View Existing publication <br>including standards and grade alignments</br></p>
                    </button>
                    <!-- <button class="homeBtn maroon" id="copyExstPublication" value="Copy Existing Publication" type="button" tabindex="0"><span class="btnText"><span class="homeIcon copyPublications" aria-hidden="true"></span>Copy <br>Existing Publication</br></span><p>Copy an existing publication to a <br>new publication with an updated version</br></p></button> -->
                    
                </div>
            </div>
            <div class="line"></div>
            <div class="boxBg">
                <span class="homeTitle"><h2 tabindex="-1">Download Information</h2></span>
                <div class="btnGroup">
                    <button class="homeBtn navy" id="downloadCSRInstructions" value="Download Publication Spreadsheet Instructions" type="button" tabindex="0"><span class="btnText">Download Publication <br>Spreadsheet Instructions</span><span class="homeIcon downloadInstructions" aria-hidden="true"></span><p>Download a document with instructions <br>and validation rules for creating <br>publication spreadsheets for upload</br></br></p></button>
                    <button class="homeBtn maroon"  id="downloadCSRSheet" value="Download Sample Publication Spreadsheet" type="button" tabindex="0"><span class="btnText">Download Sample <br>Publication Spreadsheet</span><span class="homeIcon downloadSample" aria-hidden="true"></span><p>Download a sample publication <br>spreadsheet formatted with the necessary <br>tabs and column headings</br></br></p></button>
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