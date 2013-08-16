package ir.utopia.core.util.tags.datainputform.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

public class RevisionDescriptionDialog extends Window{
	DataInputFormConstants constants=GWT.create(DataInputFormConstants.class);
	TextArea textArea=new TextArea();
	boolean mandatory=false;
	 VerticalPanel container=new VerticalPanel();
	 Button submit=new Button(constants.save());
	 Button cancel=new Button(constants.cancel());
	 DataInputFormWidget parent;
//*******************************************************************************************************************************
	public RevisionDescriptionDialog(boolean mandatory,DataInputFormWidget parent){
		this.mandatory=mandatory;
		create();
		this.parent=parent;
	}
//*******************************************************************************************************************************
	public void setMandatory(boolean mandatory){
		this.mandatory=mandatory;
	}
//*******************************************************************************************************************************	
	protected void create(){
		setTitle(constants.statusDescription());  
        setClosable(true);  
        setWidth("30%");
        int height=getScreenHeight()*40/100;
        setHeight(height);  
        setPlain(true);  
        setLayout(new BorderLayout());  
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);  
        centerData.setMargins(3, 0, 3, 3);
        
        setCloseAction(HIDE);  
        textArea.setWidth("100%");
        textArea.setHeight(75*height/100+"px");
        container.add(textArea);
        submit.setStylePrimaryName("clsConfirmButton");
        cancel.setStylePrimaryName("clsCancelButton");
        FlexTable buttonTable=new FlexTable();
        buttonTable.setWidget(0, 1, submit);
        buttonTable.setWidget(0, 2, cancel);
        buttonTable.setCellSpacing(5);
        CellFormatter cellformatter=buttonTable.getCellFormatter();
        cellformatter.setWidth(0, 0, "33%");
        cellformatter.setWidth(0, 1, "16%");
        cellformatter.setWidth(0, 2, "16%");
        cellformatter.setWidth(0, 3, "33%");
        container.add(buttonTable);
        buttonTable.getElement().setDir(LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR");
        add(container, centerData);  
        submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!mandatory||(textArea.getText()==null||textArea.getText().trim().length()>0)){
    				hide(); 
    				parent.submitWithRevisionDescription(textArea.getText().trim());
    			}else{
    				MessageUtility.error(constants.error(), constants.pleaseEnterRevisionDescription());
    			}
				
			}
		});
        cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
				
			}
		});
       
	}
//*******************************************************************************************************************************	
	public static native int getScreenHeight() /*-{
	    return $wnd.screen.height;
	 	}-*/;
//*******************************************************************************************************************************
	
}
