package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AbstractUtilityPanel extends VerticalPanel {
	UtilityConstants constants=GWT.create(UtilityConstants.class);
//********************************************************************************************************
	protected AsyncCallback<UILookupInfo> initBox(final ListBox box,String lookupURL,Object service){
		box.clear();
		box.addItem("------","-1");
		
		((ServiceDefTarget)service).setServiceEntryPoint(lookupURL);
		
		return new AsyncCallback<UILookupInfo>() {
			
			@Override
			public void onSuccess(UILookupInfo result) {
				if(result==null){
					Window.alert(constants.failToAccessServer());
					return;
				}
				for (String row[]:result.getData()){
					box.addItem(row[1],row[0]);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				Window.alert(constants.failToAccessServer());
				
			}
		};
	}
}
