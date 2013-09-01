package ir.utopia.core.model;

import java.util.ArrayList;
import java.util.List;

public class System {
	private long systemId;
	private String name;
	private String abbreviation;
	private List<SubSystem> subSystems=new ArrayList<SubSystem>();
	private String menuBundelName;
//******************************************************************	
	public void addSubSystem(SubSystem subsytem){
		subSystems.add(subsytem);
	}
//******************************************************************
	public List<SubSystem> getSubSystems(){
		return subSystems;
	}
//******************************************************************	
	public long getSystemId() {
		return systemId;
	}
//******************************************************************	
	public void setSystemId(long systemId) {
		this.systemId = systemId;
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
//******************************************************************	
	public String getMenuBundelName() {
		return menuBundelName;
	}
//******************************************************************
	public void setMenuBundelName(String menuBundelName) {
		this.menuBundelName = menuBundelName;
	}
}
