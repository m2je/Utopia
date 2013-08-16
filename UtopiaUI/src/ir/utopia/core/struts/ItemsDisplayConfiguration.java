package ir.utopia.core.struts;

import java.util.List;

public class ItemsDisplayConfiguration {
	private List<UtopiaFormMethodMetaData> methods;
	private String seperator;
	public ItemsDisplayConfiguration(List<UtopiaFormMethodMetaData> methods,
			String seperator) {
		super();
		this.methods = methods;
		this.seperator = seperator;
	}
	public List<UtopiaFormMethodMetaData> getMethods() {
		return methods;
	}

	public void setMethods(List<UtopiaFormMethodMetaData> methods) {
		this.methods = methods;
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	
	
}
