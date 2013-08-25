<%@page import="ir.utopia.core.util.WebUtil"%>
<%@page import="ir.utopia.core.constants.Constants"%>
<html dir="<%=WebUtil.getDirection(session)%>">
<script type="text/javascript" src='<%=WebUtil.getJsFile(pageContext, "Nejat2UI.js")%>'></script>
<script type="text/javascript">
	function calculateAmount() {
		var totalAmount = calculateTotalAmount('bgPrAgreementSubdtls', 3);
		var gridTotalValueId = getGWTComponentId('gridTotalValue');
		var gridTotalValue = Ext.getCmp(gridTotalValueId);
		gridTotalValue.setValue(totalAmount.toString());
		var amountId = getGWTComponentId('amount');
		var amount = Ext.getCmp(amountId);
		var amountValue = parseInt(amount.getValue());
		var remainId = getGWTComponentId('remain');
		var remain = Ext.getCmp(remainId);
		remain.setValue((amountValue - totalAmount).toString());
	}
</script>
<head>
<meta http-equiv="content-type" content="text/html;charset=windows-1256">
<title></title>
<link href='<%=WebUtil.getCss("Intcss.css")%>' rel="stylesheet" type="text/css">
</head>
<%@taglib prefix="tag" uri="Util"%>
<body class="clsInputPage">
	<tag:Header />
	<!--  <p align=center> <tag:ActionButton predefindeAction="<%=Constants.predefindedActions.save%>" redirect="true"></tag:ActionButton></p>  -->
	<tag:TreeView />
</body>
</html>
