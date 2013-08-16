var c= new CalendarPopup('_dateDiv');
function showDate(dateInputName,x,y){
	c.setCssPrefix('TEST');
	var dataInput = window.document.getElementById(dateInputName);
	c.select(dataInput, dateInputName,'yyyy/MM/dd',x,y);
} 
