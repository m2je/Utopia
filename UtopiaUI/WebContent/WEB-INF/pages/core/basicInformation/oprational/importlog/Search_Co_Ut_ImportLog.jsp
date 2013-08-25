<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<html dir=<%=WebUtil.getDirection(session) %>>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8">
  <TITLE>&#1587;&#1740;&#1587;&#1578;&#1605; &#1740;&#1705;&#1662;&#1575;&#1585;&#1670;&#1607; &#1576;&#1607;&#1575;&#1740; &#1578;&#1605;&#1575;&#1605; &#1588;&#1583;&#1607; &#1608; &#1578;&#1593;&#1740;&#1740;&#1606; &#1578;&#1593;&#1585;&#1601;&#1607;</TITLE>
  <link href="../../css/Intcss.css" rel="stylesheet" type="text/css">

  

  <!-- (c) 2006, Deluxe-Menu.com -->
 <script type="text/javascript">
function openWindow(url) {
		newWindow = window.open(url, 'report', 'toolbar=yes,location=yes,scrollbars=yes,width=500,height=500%')
	}

</script>   
</head>
<%@taglib prefix="tag" uri="Util" %>
<body class=clsInputPage>
<tag:Header></tag:Header>
<tag:SearchGrid forwardUrl="test"></tag:SearchGrid>
<p align=center>
	<table>
	
	<tr>
	<td>
		<tag:ActionButton predefindeAction="<%=Constants.predefindedActions.save%>" redirect="true"></tag:ActionButton>
	</td>
	<td>
		&nbsp; &nbsp;
	</td>
	<td>  
		<tag:ActionButton predefindeAction="<%=Constants.predefindedActions.importData%>" redirect="true"></tag:ActionButton>
	</td>
	<td>
	<%String reportUrl="openWindow('"+request.getContextPath()+"/pages/core/basicInformation/oprational/importlog/Report_Co_Ut_ImportLog.jsp')"; %> 
		<tag:ActionButton redirect="<%=false %>" predefindeAction="<%=Constants.predefindedActions.report %>"  jrxml="/reports/core/Co_Ut_ImportLog"></tag:ActionButton>
		</td>
	</tr>
	</table>
</p>


</body>
</HTML>


