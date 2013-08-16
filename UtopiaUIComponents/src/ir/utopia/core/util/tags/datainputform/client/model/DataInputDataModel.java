/**
 * 
 */
package ir.utopia.core.util.tags.datainputform.client.model;



import ir.utopia.core.util.tags.datainputform.client.grid.model.GridData;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Salarkia
 *
 */
public class DataInputDataModel extends AbstractPageDataModel implements IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5735651818531571971L;
	public DataInputDataModel(){
		
	}
	public DataInputDataModel(HashMap<String, String>valueModel){
		this.valueModel=valueModel;
	} 
	HashMap<String, GridData>includedGridsData;
	public HashMap<String, GridData> getIncludedGridsData() {
		return includedGridsData;
	}
	public void setIncludedGridsData(HashMap<String, GridData> includedGridsData) {
		this.includedGridsData = includedGridsData;
	}
	
}
