package ir.utopia.core.model;

public class SubSystem {
	
	private String usecaseBundelName;
	private long subSystemId;
	private String abbreviation;
	private String name;

//******************************************************************
	public String getUsecaseBundelName() {
		return usecaseBundelName;
	}
//******************************************************************
	public void setUsecaseBundelName(String usecaseBundelName) {
		this.usecaseBundelName = usecaseBundelName;
	}
//******************************************************************
	public long getSubSystemId() {
		return subSystemId;
	}
//******************************************************************
	public void setSubSystemId(long subSystemId) {
		this.subSystemId = subSystemId;
	}
//******************************************************************
	public String getAbbreviation() {
		return abbreviation;
	}
//******************************************************************
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
//******************************************************************
	public String getName() {
		return name;
	}
//******************************************************************	
	public void setName(String name) {
		this.name = name;
	}
}
