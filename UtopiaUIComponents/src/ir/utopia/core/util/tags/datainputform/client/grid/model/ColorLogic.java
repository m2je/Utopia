package ir.utopia.core.util.tags.datainputform.client.grid.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ColorLogic implements Serializable,IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2511154389789674632L;
	String logic;
	String cssStyleName;
	public ColorLogic(){
		
	}
	public ColorLogic(String logic,String cssStyleName){
		this.logic=logic;
		this.cssStyleName=cssStyleName;
	}
	public String getLogic() {
		return logic;
	}
	public void setLogic(String logic) {
		this.logic = logic;
	}
	public String getCssStyleName() {
		return cssStyleName;
	}
	public void setCssStyleName(String cssStyleName) {
		this.cssStyleName = cssStyleName;
	}
	
}
