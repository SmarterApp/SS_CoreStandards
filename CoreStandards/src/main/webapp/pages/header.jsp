<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.opentestsystem.shared.security.domain.SbacUser"%>
<%
	String userName = "";
	try {
	  SbacUser user = (SbacUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	  userName = user.getFullName ();
	} catch (Exception e) {
	}
%>
<div class="header">
	<div class="info">
		<ul>
		<li id="systemsDropdown"><span class="icon_sprite icon_setup2"></span>Settings
                        <ul>
                            <li><a id="publisherSetting" href="setup.jsp">Publisher</a></li>
                            <li><a id="gradeSetting" href="setup.jsp">Grade</a></li>
                            <li><a id="subjectSetting" href="setup.jsp">Subject</a></li>
                        </ul>
                    </li>
                    
			<li>Logged in as: <%=userName%> <span id="userRole">(Administrator)</span>
			</li>
			<!-- <li><a href="#">User Management</a></li> -->
			<li><a id="logoutLink" href="#">Logout</a></li>
			<!--  <li><span> <a href="?lang=en">en</a> | <a href="?lang=de">de</a> -->
			</span>
			</li>
		</ul>
	</div>
	<div class="banner" role="banner">
		<span class="logo"> <a href="#"> <img
				 name="SBAC_logo" id="homeButton">
		</a>
		</span> 
		<span class="title"><h1 tabindex="-1">Core Standards Admin Console</h1></span>

	</div>
</div>