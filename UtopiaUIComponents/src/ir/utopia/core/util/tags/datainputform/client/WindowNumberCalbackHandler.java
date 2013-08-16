package ir.utopia.core.util.tags.datainputform.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;

public class WindowNumberCalbackHandler implements AsyncCallback<Integer> {

	public void onFailure(Throwable caught) {
		MessageUtility.alert("error", "error accessing server", MessageBox.ERROR);
		
	}

	public void onSuccess(Integer result) {
		if(result==null||result.intValue()<0)
			MessageUtility.alert("error", "error accessing server", MessageBox.ERROR);
		
	}

}
