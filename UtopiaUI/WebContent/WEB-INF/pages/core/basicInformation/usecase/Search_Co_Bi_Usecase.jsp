<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en" dir=<%=WebUtil.getDirection(session) %>>


<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256" />
  <title></title>
  <link href="../../css/Intcss.css" rel="stylesheet" type="text/css" />

  

  <!-- (c) 2006, Deluxe-Menu.com -->
   
</head>
<%@taglib prefix="tag" uri="Util" %>
<body class=clsInputPage>
<tag:Header></tag:Header>
<tag:SearchGrid forwardUrl=""></tag:SearchGrid>
 <p align=center> 
 <tag:ActionButton predefindeAction="<%=Constants.predefindedActions.save%>" redirect="true"></tag:ActionButton></p>
</body>
</html>


